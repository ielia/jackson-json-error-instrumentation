package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.beans.MaxMinMembersDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.MaxMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.MinMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

// FIXME: Add tests for objects inside arrays and collections.
public class MaxMinTest extends TestNGTest {
    @Test(groups = "unit")
    public void testMax() {
        String[] actual = new JSONMutationInstrumentator(new MaxMinMembersDTO(), new MaxMutagen()).getErrorCombinations().toArray(String[]::new);
        Assert.assertEquals(actual, new String[] {
                "{\"field\":11,\"value\":-2,\"methodValue\":3}",
                "{\"field\":1,\"value\":0,\"methodValue\":3}",
                "{\"field\":1,\"value\":-2,\"methodValue\":6}",
        });
    }

    @Test(groups = "unit")
    public void testMin() {
        String[] actual = new JSONMutationInstrumentator(new MaxMinMembersDTO(), new MinMutagen()).getErrorCombinations().toArray(String[]::new);
        Assert.assertEquals(actual, new String[] {
                "{\"field\":0,\"value\":-2,\"methodValue\":3}",
                "{\"field\":1,\"value\":-11,\"methodValue\":3}",
                "{\"field\":1,\"value\":-2,\"methodValue\":-6}",
        });
    }
}
