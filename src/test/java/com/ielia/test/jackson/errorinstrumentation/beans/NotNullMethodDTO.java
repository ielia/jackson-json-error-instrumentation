package com.ielia.test.jackson.errorinstrumentation.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class NotNullMethodDTO {
    @JsonProperty @NotNull public int value() { return 1; }
}
