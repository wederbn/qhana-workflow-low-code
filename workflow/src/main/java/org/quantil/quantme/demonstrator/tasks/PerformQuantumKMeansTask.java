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

public class PerformQuantumKMeansTask implements JavaDelegate {

    private final static Logger LOGGER = LoggerFactory.getLogger(PerformQuantumKMeansTask.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Performing Quantum K-Means...");

        // send request to external service
        final String requestUrl = Configuration.getInstance().properties.getProperty("qhana-plugin-runner")
                + Constants.URL_KMEANS;
        LOGGER.info("Sending request to URL: {}", requestUrl);
        try {
            // create request containing form encoded data
            final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add(Constants.REQUEST_ENTITY_POINTS, Utils.getUrlToProcessVariable(execution.getProcessInstanceId(),
                    Constants.VARIABLE_NAME_ENTITY_POINTS_FILE));
            map.add(Constants.REQUEST_K_MEANS_VARIANT, Constants.PARAMETER_K_MEANS_VARIANT_DEFAULT);
            map.add(Constants.REQUEST_CLUSTER_COUNT, Constants.PARAMETER_DEFAULT_DIMENSIONS);
            map.add(Constants.REQUEST_BACKEND,
                    execution.getVariable(Constants.VARIABLE_NAME_INPUT_IBMQ_BACKEND).toString());
            map.add(Constants.REQUEST_TOKEN,
                    execution.getVariable(Constants.VARIABLE_NAME_INPUT_IBMQ_TOKEN).toString());
            map.add(Constants.REQUEST_CUSTOM_BACKEND, "test");

            // search for the required outputs
            final JSONArray outputArray = Utils.performRequestToQHAnaPlugin(requestUrl, map);
            final String clustersUrl = Utils.searchForOutput(outputArray, Constants.RESPONSE_CLUSTERS_JSON);

            if (Objects.isNull(clustersUrl)) {
                LOGGER.error("Unable to find required output: {}", Constants.RESPONSE_CLUSTERS_JSON);
                throw new BpmnError("Unable to find required output: " + Constants.RESPONSE_CLUSTERS_JSON);
            }

            final boolean success = Utils.addFileFromUrlAsVariable(new URL(clustersUrl), "clusters-", null,
                    Constants.VARIABLE_NAME_CLUSTERS_FILE, MediaType.TEXT_PLAIN.toString(), execution);
            LOGGER.info("Downloading and adding of clusters file returned: {}", success);
        } catch (final Exception e) {
            LOGGER.error("Exception while sending post request to URL: {}", requestUrl);
            throw new BpmnError("Exception while sending post request to URL: " + requestUrl);
        }
    }
}
