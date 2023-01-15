package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.beans.DataTypeChangeDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.DataTypeMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DataTypeChangeTest extends TestNGTest {
    /*
    public static class A {
        public Map<String, Integer> x = new LinkedHashMap<>() {{ put("a", 11); put("b", 12); put("c", 13); }};
        public int[] y = { 1, 2, 3 };
    }

    @Test(groups = "unit")
    public void testA() {
        String[] actual = new JSONMutationInstrumentator(new A(), new DataTypeMutagen()).getErrorCombinations().toArray(String[]::new);
        Assert.assertEquals(actual, new String[] {
                "{x:[\"abc\",2,3]}",
                "{x:[1,\"abc\",3]}",
                "{x:[1,2,\"abc\"]}",
        });
    }
    */

    @Test(groups = "unit")
    public void testDataTypeChange() {
        // FIXME: Use individual, more manageable tests instead of this too big DataTypeChangeDTO class.
        String[] actual = new JSONMutationInstrumentator(new DataTypeChangeDTO(), new DataTypeMutagen()).getErrorCombinations().toArray(String[]::new);
        Assert.assertEquals(actual, new String[] {
                // FIXME: Make all these commented-out results below be part of the mutations.
                "{\"internal\":\"abc\",\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":123},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":\"abc\",\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[123,123],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":\"abc\",\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":\"abc\",\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[\"abc\",\"abc\"],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":123,\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":\"abc\"}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":\"abc\",\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":\"abc\"}}",
        });
    }
}
