package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class NotNullListOfListsDTO {
    @NotNull public List<List<Integer>> values = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4));
}
