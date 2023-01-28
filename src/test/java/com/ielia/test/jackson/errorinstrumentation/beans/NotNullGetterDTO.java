package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.NotNull;

public class NotNullGetterDTO {
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"}) private int value = 1;
    @NotNull public int getValue() { return value; }
}
