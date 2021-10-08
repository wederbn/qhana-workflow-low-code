package org.quantil.quantme.demonstrator.tasks;

import java.net.URL;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.quantil.quantme.demonstrator.Constants;
import org.quantil.quantme.demonstrator.Utils;
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

public class DataLoadingTask implements JavaDelegate {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataLoadingTask.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Downloading input data file with MUSE data...");

        final URL inputDataUrl = new URL(execution.getVariable(Constants.VARIABLE_NAME_INPUT_DATA_URL).toString());
        LOGGER.info("Input Data URL: {}", inputDataUrl);

        Utils.addFileFromUrlAsVariable(inputDataUrl, "input-data-", null, Constants.VARIABLE_NAME_INPUT_DATA_FILE, null,
                execution);
    }
}
