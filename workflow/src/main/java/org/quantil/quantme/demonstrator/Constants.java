package org.quantil.quantme.demonstrator;

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

public class Constants {

    // internal variable names
    public static final String VARIABLE_NAME_INPUT_DATA_URL = "inputDataUrl";
    public static final String VARIABLE_NAME_INPUT_DATA_FILE = "inputDataFile";
    public static final String VARIABLE_NAME_DISTANCES_FILE = "distancesFile";
    public static final String VARIABLE_NAME_ENTITY_DISTANCES_FILE = "entityDistancesFile";

    // URL snippets
    public static final String URL_SIM_TO_DIST = "/plugins/sim-to-dist-transformers@v0-1-0/process/";
    public static final String URL_AGGREGATION = "/plugins/distance-aggregator@v0-1-0/process/";
    public static final String URL_MDS = "/plugins/mds@v0-1-0/process/";

    // constants for the plugins
    public static final String PARAMETER_DISTANCE_ATTRIBUTES = "dominanteFarbe dominaterZustand dominanteCharacktereigenschaft dominanterAlterseindruck genre";
    public static final String PARAMETER_DISTANCE_TRANSFORMER = "linear_inverse";
    public static final String PARAMETER_AGGREGATION_AGGREGATOR = "Mean";
    public static final String PARAMETER_DEFAULT_DIMENSIONS = "2";
    public static final String PARAMETER_DEFAULT_MAX_ITER = "300";
    public static final String PARAMETER_DEFAULT_NINIT = "4";
    public static final String PARAMETER_MDS_METRIC = "Metric MDS";
    public static final String RESPONSE_TASK_RESULT_URL = "taskResultUrl";
    public static final String RESPONSE_ATTRIBUTE_DISTANCE_URL = "attributeDistancesUrl";
    public static final String RESPONSE_ENTITY_DISTANCE_URL = "entityDistancesUrl";
    public static final String RESPONSE_OUTPUTS = "outputs";
    public static final String RESPONSE_OUTPUTS_NAME = "name";
    public static final String RESPONSE_OUTPUTS_HREF = "href";
    public static final String RESPONSE_STATUS = "status";
    public static final String STATUS_SUCCESS = "SUCCESS";
}
