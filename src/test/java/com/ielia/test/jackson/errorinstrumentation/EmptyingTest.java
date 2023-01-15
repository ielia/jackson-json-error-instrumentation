package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.beans.DataTypeChangeDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.EmptyingMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmptyingTest extends TestNGTest {
    @Test(groups = "unit")
    public void testEmptying() {
        // FIXME: Use individual, more manageable tests instead of this too big DataTypeChangeDTO class.
        String[] actual = new JSONMutationInstrumentator(new DataTypeChangeDTO(), new EmptyingMutagen()).getErrorCombinations().toArray(String[]::new);
        Assert.assertEquals(actual, new String[] {
                // FIXME: Allow emptying root object.
                // "{}",
                "{\"internal\":{},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"\",\"\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"\",\"methodValue\":{\"a\":4,\"b\":5}}",
                "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{}}",
        });
    }
}
