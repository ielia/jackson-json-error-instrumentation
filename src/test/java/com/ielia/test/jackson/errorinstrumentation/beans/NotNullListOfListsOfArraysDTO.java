package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class NotNullListOfListsOfArraysDTO {
    @NotNull public List<List<Integer[]>> values = Arrays.asList(
            Arrays.asList(new Integer[] { 1, 2 }, new Integer[] { 3 }),
            Arrays.asList(new Integer[] { 4 }, new Integer[] { 5, 6 })
    );
}
