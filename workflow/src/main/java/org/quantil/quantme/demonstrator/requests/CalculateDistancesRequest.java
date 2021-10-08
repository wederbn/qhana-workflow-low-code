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
public class CalculateDistancesRequest {

    @JsonProperty("attributeSimilaritiesUrl")
    private String attributeSimilaritiesUrl;

    public String getAttributeSimilaritiesUrl() {
        return attributeSimilaritiesUrl;
    }

    public void setAttributeSimilaritiesUrl(String attributeSimilaritiesUrl) {
        this.attributeSimilaritiesUrl = attributeSimilaritiesUrl;
    }

    @JsonProperty("attributes")
    private String attributes;

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("transformer")
    private String transformer;

    public String getTransformer() {
        return transformer;
    }

    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }
}
