package org.quantil.quantme.demonstrator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quantil.quantme.demonstrator.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

public class Utils {

    private final static Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * Perform a request to a QHAna plugin, wait for the completion of the task
     * object, and return corresponding output data
     *
     * @param requestUrl the URL of the plugin endpoint
     * @param inputData  the input data for the plugin invocation
     * @return the JSONArray containing the output data
     * @throws InterruptedException
     */
    public static JSONArray performRequestToQHAnaPlugin(String requestUrl, MultiValueMap<String, String> inputData)
            throws InterruptedException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        // perform request to external service
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                inputData, headers);
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

        // return output array
        return jo.getJSONArray(Constants.RESPONSE_OUTPUTS);
    }

    public static String searchForOutput(JSONArray outputArray, String outputName) {
        for (int i = 0, size = outputArray.length(); i < size; i++) {
            final JSONObject output = outputArray.getJSONObject(i);
            if (output.get(Constants.RESPONSE_OUTPUTS_NAME).toString().equals(outputName)) {
                return output.get(Constants.RESPONSE_OUTPUTS_HREF).toString();
            }
        }
        return null;
    }

    public static boolean addFileFromUrlAsVariable(URL downloadUrl, String prefix, String suffix, String variableName,
            String mimeType, DelegateExecution execution) {
        if (Objects.isNull(mimeType)) {
            // use text/plain as default
            mimeType = MediaType.TEXT_PLAIN.toString();
        }

        try {
            // download file to temp
            LOGGER.info("Dowloading file from URL: {}", downloadUrl);
            final File tempFile = File.createTempFile(prefix, suffix);
            tempFile.deleteOnExit();
            FileUtils.copyURLToFile(downloadUrl, tempFile);
            LOGGER.info("File size after download: {}", tempFile.length());

            // download using https if in secure environment
            if (tempFile.length() == 0 && downloadUrl.getProtocol().equals("http")) {
                LOGGER.info("Unable to download file using http. Proceeding with https...");
                final URL httpsUrl = new URL("https://" + downloadUrl.getHost() + "/" + downloadUrl.getPath());
                LOGGER.info("Downloading file from URL: {}", httpsUrl);
                FileUtils.copyURLToFile(httpsUrl, tempFile);
                LOGGER.info("File size after download using https: {}", tempFile.length());
            }

            // set file as variable in the Camunda engine
            final FileValue typedFileValue = Variables.fileValue(tempFile.getName()).file(tempFile).mimeType(mimeType)
                    .encoding("UTF-8").create();
            execution.setVariable(variableName, typedFileValue);

            return true;
        } catch (final IOException e) {
            LOGGER.error("Unable to download and add file as variable: {}", e.getLocalizedMessage());
            return false;
        }
    }

    public static JSONObject pollForResult(String pollingUrl) throws InterruptedException {
        final Client client = ClientBuilder.newClient();
        String status = "";
        JSONObject jo = null;
        while (!status.equals(Constants.STATUS_SUCCESS)) {
            jo = new JSONObject(client.target(pollingUrl).request(MediaType.TEXT_PLAIN.toString()).get(String.class));
            status = jo.get(Constants.RESPONSE_STATUS).toString();
            Thread.sleep(5000);
            LOGGER.info("Waiting 5 seconds for next polling request...");
        }
        return jo;
    }

    public static String getUrlToProcessVariable(String processId, String variableName) {
        return Configuration.getInstance().properties.getProperty("engine") + "/engine-rest/process-instance/"
                + processId + "/variables/" + variableName + "/data";
    }
}
