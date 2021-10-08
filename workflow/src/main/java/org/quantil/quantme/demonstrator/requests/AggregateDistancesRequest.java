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
public class AggregateDistancesRequest {

    @JsonProperty("attributeDistancesUrl")
    private String attributeDistancesUrl;

    public String getAttributeDistancesUrl() {
        return attributeDistancesUrl;
    }

    public void setAttributeDistancesUrl(String attributeDistancesUrl) {
        this.attributeDistancesUrl = attributeDistancesUrl;
    }

    @JsonProperty("aggregator")
    private String aggregator;

    public String getAggregator() {
        return aggregator;
    }

    public void setAggregator(String aggregator) {
        this.aggregator = aggregator;
    }
}
