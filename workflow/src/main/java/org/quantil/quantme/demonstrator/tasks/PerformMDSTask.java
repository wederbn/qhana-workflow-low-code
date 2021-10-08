package org.quantil.quantme.demonstrator.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
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

public class PerformMDSTask implements JavaDelegate {

    private final static Logger LOGGER = LoggerFactory.getLogger(PerformMDSTask.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Performing MDS...");

        // create request
//        request.setEntityDistancesUrl(Utils.getUrlToProcessVariable(execution.getProcessInstanceId(),
//                Constants.VARIABLE_NAME_ENTITY_DISTANCES_FILE));
//        request.setDimensions(Constants.PARAMETER_DEFAULT_DIMENSIONS);
//        request.setMaxIter(Constants.PARAMETER_DEFAULT_MAX_ITER);
//        request.setMetric(Constants.PARAMETER_MDS_METRIC);
//        request.setNInit(Constants.PARAMETER_DEFAULT_NINIT);
        // TODO
    }
}
