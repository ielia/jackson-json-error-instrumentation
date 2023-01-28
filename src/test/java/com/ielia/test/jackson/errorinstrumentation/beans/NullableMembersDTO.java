package com.ielia.test.jackson.errorinstrumentation.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Null;

public class NullableMembersDTO {
    @Null public Integer field = 1;
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"}) private int value = 2;
    @Null public Integer getValue() { return value; }
    @JsonProperty @Null public Integer methodValue() { return 3; }
}
