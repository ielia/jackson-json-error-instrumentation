package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.TreeMap;

public class NotNullMapFieldDTO {
    @NotNull public Map<String, Integer> values = new TreeMap<String, Integer>() {{
        put("a", 1);
        put("b", 2);
        put("c", 3);
    }};
}
