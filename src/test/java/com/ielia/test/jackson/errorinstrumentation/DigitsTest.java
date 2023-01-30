package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.beans.DigitsDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.FractionDigitsMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.IntegerDigitsMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DigitsTest extends TestNGTest {
    @Test(groups = "unit")
    public void testWithoutGroups() {
        Mutation[] actual = new JSONMutationInstrumentator(new DigitsDTO(), new FractionDigitsMutagen(), new IntegerDigitsMutagen())
                .getErrorCombinations()
                .toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/x", IntegerDigitsMutagen.class, "Changed value from 1 to 10000000000.", "{\"x\":10000000000,\"y\":1,\"z\":1}"),
                new Mutation(1, "/y", FractionDigitsMutagen.class, "Changed value from 1 to 0.99999999999.", "{\"x\":1,\"y\":0.99999999999,\"z\":1}"),
                new Mutation(2, "/z", FractionDigitsMutagen.class, "Changed value from 1 to 0.9999.", "{\"x\":1,\"y\":1,\"z\":0.9999}"),
                new Mutation(3, "/z", IntegerDigitsMutagen.class, "Changed value from 1 to 1000.", "{\"x\":1,\"y\":1,\"z\":1000}"),
        });
    }

    @Test(groups = "unit")
    public void testWithGroup() {
        Mutation[] actual = new JSONMutationInstrumentator(new DigitsDTO(), new FractionDigitsMutagen(), new IntegerDigitsMutagen())
                .withGroups(DigitsDTO.Group1.class)
                .getErrorCombinations()
                .toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/x", IntegerDigitsMutagen.class, "Changed value from 1 to 1000.", "{\"x\":1000,\"y\":1,\"z\":1}"),
                new Mutation(1, "/y", FractionDigitsMutagen.class, "Changed value from 1 to 0.9999.", "{\"x\":1,\"y\":0.9999,\"z\":1}"),
                new Mutation(2, "/z", FractionDigitsMutagen.class, "Changed value from 1 to 0.9999.", "{\"x\":1,\"y\":1,\"z\":0.9999}"),
                new Mutation(3, "/z", IntegerDigitsMutagen.class, "Changed value from 1 to 1000.", "{\"x\":1,\"y\":1,\"z\":1000}"),
        });
    }
}
