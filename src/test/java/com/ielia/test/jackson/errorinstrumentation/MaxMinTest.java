package com.ielia.test.jackson.errorinstrumentation;

import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.beans.MaxMinMembersDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.MaxMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.MinMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

// FIXME: Add tests for objects inside arrays and collections.
public class MaxMinTest extends TestNGTest {
    @Test(groups = "unit")
    public void testMax() {
        Mutation[] actual = new JSONMutationInstrumentator(new MaxMinMembersDTO(), new MaxMutagen()).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/field", MaxMutagen.class, "Changed value from 1 to 11.", "{\"field\":11,\"value\":-2,\"methodValue\":3}"),
                new Mutation(1, "/value", MaxMutagen.class, "Changed value from -2 to 0.", "{\"field\":1,\"value\":0,\"methodValue\":3}"),
                new Mutation(2, "/methodValue", MaxMutagen.class, "Changed value from 3 to 6.", "{\"field\":1,\"value\":-2,\"methodValue\":6}"),
        });
    }

    @Test(groups = "unit")
    public void testMin() {
        Mutation[] actual = new JSONMutationInstrumentator(new MaxMinMembersDTO(), new MinMutagen()).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/field", MinMutagen.class, "Changed value from 1 to 0.", "{\"field\":0,\"value\":-2,\"methodValue\":3}"),
                new Mutation(1, "/value", MinMutagen.class, "Changed value from -2 to -11.", "{\"field\":1,\"value\":-11,\"methodValue\":3}"),
                new Mutation(2, "/methodValue", MinMutagen.class, "Changed value from 3 to -6.", "{\"field\":1,\"value\":-2,\"methodValue\":-6}"),
        });
    }
}
