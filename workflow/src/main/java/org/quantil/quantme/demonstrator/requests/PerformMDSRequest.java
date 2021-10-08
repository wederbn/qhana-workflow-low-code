package org.quantil.quantme.demonstrator.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class PerformMDSRequest {

    @JsonProperty("entityDistancesUrl")
    private String entityDistancesUrl;

    public String getAttributeDistancesUrl() {
        return entityDistancesUrl;
    }

    public void setEntityDistancesUrl(String entityDistancesUrl) {
        this.entityDistancesUrl = entityDistancesUrl;
    }

    @JsonProperty("dimensions")
    private String dimensions;

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    @JsonProperty("metric")
    private String metric;

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    @JsonProperty("nInit")
    private String nInit;

    public String getNInit() {
        return nInit;
    }

    public void setNInit(String nInit) {
        this.nInit = nInit;
    }

    @JsonProperty("maxIter")
    private String maxIter;

    public String getMaxIter() {
        return maxIter;
    }

    public void setMaxIter(String maxIter) {
        this.maxIter = maxIter;
    }
}
