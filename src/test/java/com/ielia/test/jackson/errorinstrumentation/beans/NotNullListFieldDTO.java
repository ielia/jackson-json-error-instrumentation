package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class NotNullListFieldDTO {
    @NotNull public List<Integer> values = Arrays.asList(1, 2, 3);
}
