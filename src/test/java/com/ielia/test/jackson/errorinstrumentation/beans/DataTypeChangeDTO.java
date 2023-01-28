package com.ielia.test.jackson.errorinstrumentation.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataTypeChangeDTO {
    public static class Internal {
        public String val = "Whatever";
    }

    public Internal internal = new Internal();
    public List<String> collected = Arrays.asList("a", "b");
    public int field = 1;
    public double[] values = new double[] { 2.1, 2.2 };
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"}) private String value = "3";
    public String getValue() { return value; }
    @JsonProperty public Map<String, Integer> methodValue() { return new TreeMap<String, Integer>() {{ put("a", 4); put("b", 5); }}; }
}
