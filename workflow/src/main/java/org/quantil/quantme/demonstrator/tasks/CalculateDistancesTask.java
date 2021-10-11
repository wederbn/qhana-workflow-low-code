package org.quantil.quantme.demonstrator.tasks;

import java.net.URL;
import java.util.Objects;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.quantil.quantme.demonstrator.Constants;
import org.quantil.quantme.demonstrator.Utils;
import org.quantil.quantme.demonstrator.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

        // send request to external service
        final String requestUrl = Configuration.getInstance().properties.getProperty("qhana-plugin-runner")
                + Constants.URL_SIM_TO_DIST;
        LOGGER.info("Sending request to URL: {}", requestUrl);
        try {
            // create request containing form encoded data
            final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add(Constants.REQUEST_SIMILARITIES, Utils.getUrlToProcessVariable(execution.getProcessInstanceId(),
                    Constants.VARIABLE_NAME_INPUT_DATA_FILE));
            map.add(Constants.REQUEST_ATTRIBUTES, Constants.PARAMETER_DISTANCE_ATTRIBUTES);
            map.add(Constants.REQUEST_TRANSFORMER, Constants.PARAMETER_DISTANCE_TRANSFORMER);

            // search for the required outputs
            final JSONArray outputArray = Utils.performRequestToQHAnaPlugin(requestUrl, map);
            final String attributeDistancesUrl = Utils.searchForOutput(outputArray,
                    Constants.RESPONSE_ATTRIBUTE_DISTANCE_FILE);

            if (Objects.isNull(attributeDistancesUrl)) {
                LOGGER.error("Unable to find required output: {}", Constants.RESPONSE_ATTRIBUTE_DISTANCE_FILE);
                throw new BpmnError("Unable to find required output: " + Constants.RESPONSE_ATTRIBUTE_DISTANCE_FILE);
            }

            final boolean success = Utils.addFileFromUrlAsVariable(new URL(attributeDistancesUrl), "distances-", null,
                    Constants.VARIABLE_NAME_DISTANCES_FILE, MediaType.TEXT_PLAIN.toString(), execution);
            LOGGER.info("Downloading and adding of distance file returned: {}", success);
        } catch (final Exception e) {
            LOGGER.error("Exception while sending post request to URL: {}", requestUrl);
            throw new BpmnError("Exception while sending post request to URL: " + requestUrl);
        }
    }
}
