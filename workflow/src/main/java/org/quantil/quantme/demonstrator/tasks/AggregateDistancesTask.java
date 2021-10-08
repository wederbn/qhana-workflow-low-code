package org.quantil.quantme.demonstrator.tasks;

import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quantil.quantme.demonstrator.Constants;
import org.quantil.quantme.demonstrator.Utils;
import org.quantil.quantme.demonstrator.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/********************************************************************************
 * Copyright (c) 2021 Institute of Architecture of Application Systems -
 * University of Stuttgart, Author: Benjamin Weder
 *
 * This program and the accompanying materials are made available under the
 * terms the Apache Software License 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

public class AggregateDistancesTask implements JavaDelegate {

    private final static Logger LOGGER = LoggerFactory.getLogger(AggregateDistancesTask.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Aggregating distances...");

        // send request to external service
        final String requestUrl = Configuration.getInstance().properties.getProperty("qhana-plugin-runner")
                + Constants.URL_AGGREGATION;
        LOGGER.info("Sending request to URL: {}", requestUrl);
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            // create request containing form encoded data
            final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add(Constants.REQUEST_DISTANCES, Utils.getUrlToProcessVariable(execution.getProcessInstanceId(),
                    Constants.VARIABLE_NAME_DISTANCES_FILE));
            map.add(Constants.REQUEST_AGGREGATOR, Constants.PARAMETER_AGGREGATION_AGGREGATOR);

            // perform request to external service
            final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
                    headers);
            final ResponseEntity<String> response = new RestTemplate().postForEntity(requestUrl, request, String.class);

            // receive redirect to task object
            LOGGER.info("Server responded with status code: {}", response.getStatusCode());
            if (!response.getStatusCode().equals(HttpStatus.SEE_OTHER)) {
                LOGGER.error("Status code does not equal 303, aborting: {}", response.getStatusCode());
                throw new BpmnError("Status code does not equal 303, aborting: " + response.getStatusCode());
            }

            // get URL for task objects
            final String pollingUrl = response.getHeaders().getLocation().toString();
            LOGGER.info("Polling at URL: {}", pollingUrl);

            // poll for long-running tasks
            final JSONObject jo = Utils.pollForResult(pollingUrl);

            // search for the required outputs
            final JSONArray outputArray = jo.getJSONArray(Constants.RESPONSE_OUTPUTS);
            String entityDistancesUrl = null;
            for (int i = 0, size = outputArray.length(); i < size; i++) {
                final JSONObject output = outputArray.getJSONObject(i);
                if (output.get(Constants.RESPONSE_OUTPUTS_NAME).toString()
                        .equals(Constants.RESPONSE_ENTITY_DISTANCE_JSON)) {
                    entityDistancesUrl = output.get(Constants.RESPONSE_OUTPUTS_HREF).toString();
                }
            }

            if (Objects.isNull(entityDistancesUrl)) {
                LOGGER.error("Unable to find required output: {}", Constants.RESPONSE_ENTITY_DISTANCE_JSON);
                throw new BpmnError("Unable to find required output: " + Constants.RESPONSE_ENTITY_DISTANCE_JSON);
            }

            final boolean success = Utils.addFileFromUrlAsVariable(new URL(entityDistancesUrl), "entity-distances-",
                    null, Constants.VARIABLE_NAME_ENTITY_DISTANCES_FILE, MediaType.TEXT_PLAIN.toString(), execution);
            LOGGER.info("Downloading and adding of entity distance file returned: {}", success);
        } catch (final Exception e) {
            LOGGER.error("Exception while sending post request to URL: {}", requestUrl);
            throw new BpmnError("Exception while sending post request to URL: " + requestUrl);
        }
    }
}
