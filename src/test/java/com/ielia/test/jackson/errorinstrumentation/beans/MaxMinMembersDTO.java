package com.ielia.test.jackson.errorinstrumentation.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class MaxMinMembersDTO {
    @Min(1) @Max(10) public int field = 1;
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"}) private int value = -2;
    @Min(-10) @Max(-1) public int getValue() { return value; }
    @JsonProperty @Min(-5) @Max(5) public int methodValue() { return 3; }
}
