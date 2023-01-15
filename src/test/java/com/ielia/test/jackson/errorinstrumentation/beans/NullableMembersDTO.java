package com.ielia.test.jackson.errorinstrumentation.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Null;

public class NullableMembersDTO {
    @Null public int field = 1;
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"}) private int value = 2;
    @Null public int getValue() { return value; }
    @JsonProperty @Null public int methodValue() { return 3; }
}
