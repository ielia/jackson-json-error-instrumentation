package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class NotNullListOfObjectsDTO {
    public static class Val {
        @NotNull public int x;
        public Val(int x) { this.x = x; }
    }

    @NotNull public List<Val> values = Arrays.asList(new Val(1), new Val(2), new Val(3));
}
