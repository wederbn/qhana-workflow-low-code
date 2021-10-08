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
    public static final String URL_KMEANS = "/plugins/quantum-k-means@v0-1-0/process/";
    public static final String URL_VISUALIZATION = "/plugins/visualization@v0-1-0/process/";

    // constants for the plugins
    public static final String PARAMETER_DISTANCE_ATTRIBUTES = "dominanteFarbe";
    public static final String PARAMETER_DISTANCE_TRANSFORMER = "linear_inverse";
    public static final String PARAMETER_AGGREGATION_AGGREGATOR = "mean";
    public static final String PARAMETER_DEFAULT_DIMENSIONS = "2";
    public static final String PARAMETER_DEFAULT_MAX_ITER = "300";
    public static final String PARAMETER_DEFAULT_NINIT = "4";
    public static final String PARAMETER_MDS_METRIC = "Metric MDS";
    public static final String RESPONSE_TASK_RESULT_URL = "taskResultUrl";
    public static final String RESPONSE_ATTRIBUTE_DISTANCE_FILE = "attr_dist.zip";
    public static final String RESPONSE_ENTITY_DISTANCE_JSON = "entity_distances.json";
    public static final String RESPONSE_OUTPUTS = "outputs";
    public static final String RESPONSE_OUTPUTS_NAME = "name";
    public static final String RESPONSE_OUTPUTS_HREF = "href";
    public static final String RESPONSE_STATUS = "status";
    public static final String REQUEST_ATTRIBUTES = "attributes";
    public static final String REQUEST_AGGREGATOR = "aggregator";
    public static final String REQUEST_TRANSFORMER = "transformer";
    public static final String REQUEST_SIMILARITIES = "attributeSimilaritiesUrl";
    public static final String REQUEST_DISTANCES = "attributeDistancesUrl";
    public static final String STATUS_SUCCESS = "SUCCESS";
}
