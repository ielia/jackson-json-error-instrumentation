package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.NotNull;

public class NotNullArrayFieldDTO {
    @NotNull public int[] values = new int[] { 1, 2, 3 };
}
