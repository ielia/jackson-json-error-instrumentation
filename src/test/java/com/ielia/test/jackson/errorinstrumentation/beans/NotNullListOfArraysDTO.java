package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class NotNullListOfArraysDTO {
    @NotNull public List<Integer[]> values = Arrays.asList(new Integer[] { 1, 2 }, new Integer[] { 3, 4 });
}
