package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ValidationGroupsDTO {
    public static class Group1 {}
    public static class Group2 {}
    public static class Group3 {}

    @NotNull(groups = { Group1.class, Group3.class }) public Integer a = 1;
    @NotNull(groups = { Group1.class, Group2.class }) public Integer b = 2;
    @Max(groups = { Group3.class }, value = 10) public Integer c = 3;
    @DecimalMax(groups = { Group2.class }, value = "20") public Integer d = 4;
    @NotNull.List(value = {
            @NotNull(groups = { Group3.class }),
            @NotNull(groups = { Group2.class })
    }) public Integer e = 5;
    @Min.List(value = {
            @Min(groups = { Group3.class }, value = -10),
            @Min(groups = { Group2.class }, value = -20)
    }) public Integer f = 6;
}
