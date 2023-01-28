package com.ielia.test.jackson.errorinstrumentation.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class NotNullMembersDTO {
    @NotNull public int field = 1;
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"}) private int value = 2;
    @NotNull public int getValue() { return value; }
    @JsonProperty @NotNull public int methodValue() { return 3; }
}
