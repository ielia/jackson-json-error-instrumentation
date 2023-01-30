package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.beans.NegZeroPosDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.NegZeroPosMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NegZeroPosTest extends TestNGTest {
    @Test(groups = "unit")
    public void testAll() {
        Mutation[] actual = new JSONMutationInstrumentator(new NegZeroPosDTO(), new NegZeroPosMutagen()).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[]{
                new Mutation(0, "/toBePosOrZero", NegZeroPosMutagen.class, "Changed value from -1 to 1.", "{\"immutable\":0,\"toBePosOrZero\":1,\"toBePos\":-2,\"toBeNeg\":3,\"toBeNegOrZero\":4,\"toBeZero\":5,\"toBePos2\":-6,\"toBeNeg2\":7}"),
                new Mutation(1, "/toBePos", NegZeroPosMutagen.class, "Changed value from -2 to 1.", "{\"immutable\":0,\"toBePosOrZero\":-1,\"toBePos\":1,\"toBeNeg\":3,\"toBeNegOrZero\":4,\"toBeZero\":5,\"toBePos2\":-6,\"toBeNeg2\":7}"),
                new Mutation(2, "/toBeNeg", NegZeroPosMutagen.class, "Changed value from 3 to -1.", "{\"immutable\":0,\"toBePosOrZero\":-1,\"toBePos\":-2,\"toBeNeg\":-1,\"toBeNegOrZero\":4,\"toBeZero\":5,\"toBePos2\":-6,\"toBeNeg2\":7}"),
                new Mutation(3, "/toBeNegOrZero", NegZeroPosMutagen.class, "Changed value from 4 to -1.", "{\"immutable\":0,\"toBePosOrZero\":-1,\"toBePos\":-2,\"toBeNeg\":3,\"toBeNegOrZero\":-1,\"toBeZero\":5,\"toBePos2\":-6,\"toBeNeg2\":7}"),
                new Mutation(4, "/toBeZero", NegZeroPosMutagen.class, "Changed value from 5 to 0.", "{\"immutable\":0,\"toBePosOrZero\":-1,\"toBePos\":-2,\"toBeNeg\":3,\"toBeNegOrZero\":4,\"toBeZero\":0,\"toBePos2\":-6,\"toBeNeg2\":7}"),
                new Mutation(5, "/toBePos2", NegZeroPosMutagen.class, "Changed value from -6 to 1.", "{\"immutable\":0,\"toBePosOrZero\":-1,\"toBePos\":-2,\"toBeNeg\":3,\"toBeNegOrZero\":4,\"toBeZero\":5,\"toBePos2\":1,\"toBeNeg2\":7}"),
                new Mutation(6, "/toBeNeg2", NegZeroPosMutagen.class, "Changed value from 7 to -1.", "{\"immutable\":0,\"toBePosOrZero\":-1,\"toBePos\":-2,\"toBeNeg\":3,\"toBeNegOrZero\":4,\"toBeZero\":5,\"toBePos2\":-6,\"toBeNeg2\":-1}"),
        });
    }
}
