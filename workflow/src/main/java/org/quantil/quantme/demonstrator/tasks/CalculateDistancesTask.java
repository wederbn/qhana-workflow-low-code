package org.quantil.quantme.demonstrator.tasks;

import java.net.URL;
import java.util.Objects;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quantil.quantme.demonstrator.Constants;
import org.quantil.quantme.demonstrator.Utils;
import org.quantil.quantme.demonstrator.config.Configuration;
import org.quantil.quantme.demonstrator.requests.CalculateDistancesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class CalculateDistancesTask implements JavaDelegate {

    private final static Logger LOGGER = LoggerFactory.getLogger(CalculateDistancesTask.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Calculating distances...");

        // create request
        final CalculateDistancesRequest request = new CalculateDistancesRequest();
        request.setAttributeSimilaritiesUrl(Utils.getUrlToProcessVariable(execution.getProcessInstanceId(),
                Constants.VARIABLE_NAME_INPUT_DATA_FILE));
        request.setAttributes(Constants.PARAMETER_DISTANCE_ATTRIBUTES);
        request.setTransformer(Constants.PARAMETER_DISTANCE_TRANSFORMER);

        // send request to external service
        final String requestUrl = Configuration.getInstance().properties.getProperty("qhana-plugin-runner")
                + Constants.URL_SIM_TO_DIST;
        LOGGER.info("Sending request to URL: {}", requestUrl);
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = client.target(requestUrl).request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(request, MediaType.APPLICATION_JSON));

            LOGGER.info("Server responded with status code: {}", response.getStatus());
            if (response.getStatus() != 200) {
                LOGGER.error("Status code does not equal 200, aborting: {}", response.getStatus());
                throw new BpmnError("Status code does not equal 200, aborting: " + response.getStatus());
            }

            // get URL for task objects
            JSONObject jo = new JSONObject(response.readEntity(String.class));
            final String pollingUrl = jo.get(Constants.RESPONSE_TASK_RESULT_URL).toString();
            LOGGER.info("Polling at URL: {}", pollingUrl);

            // TODO
            System.out.print("Starting polling!");
            System.out.print(pollingUrl);

            // poll for long-running tasks
            jo = Utils.pollForResult(pollingUrl);

            // TODO
            System.out.print("Polling terminated!");
            System.out.print(jo.toString());

            // search for the required outputs
            final JSONArray outputArray = jo.getJSONArray(Constants.RESPONSE_OUTPUTS);
            String attributeDistancesUrl = null;
            System.out.print(outputArray); // TODO
            for (int i = 0, size = outputArray.length(); i < size; i++) {
                final JSONObject output = outputArray.getJSONObject(i);
                if (output.get(Constants.RESPONSE_OUTPUTS_NAME).toString()
                        .equals(Constants.RESPONSE_ATTRIBUTE_DISTANCE_URL)) {
                    attributeDistancesUrl = output.get(Constants.RESPONSE_OUTPUTS_HREF).toString();
                }
            }

            if (Objects.isNull(attributeDistancesUrl)) {
                LOGGER.error("Unable to find required output: {}", Constants.RESPONSE_ATTRIBUTE_DISTANCE_URL);
                throw new BpmnError("Unable to find required output: " + Constants.RESPONSE_ATTRIBUTE_DISTANCE_URL);
            }

            final boolean success = Utils.addFileFromUrlAsVariable(new URL(attributeDistancesUrl), "distances-", null,
                    Constants.VARIABLE_NAME_DISTANCES_FILE, MediaType.TEXT_PLAIN, execution);
            LOGGER.info("Downloading and adding of distance file returned: {}", success);
        } catch (final Exception e) {
            LOGGER.error("Exception while sending post request to URL: {}", requestUrl);
            throw new BpmnError("Exception while sending post request to URL: " + requestUrl);
        }
    }
}
