package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.beans.TrueFalseDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.TrueFalseMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AssertTrueFalseTest extends TestNGTest {
    @Test(groups = "unit")
    public void testAssertTrueFalse() {
        Mutation[] actual = new JSONMutationInstrumentator(new TrueFalseDTO(), new TrueFalseMutagen()).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[]{
                new Mutation(0, "/y", TrueFalseMutagen.class, "Changed boolean value from false to true.", "{\"w\":false,\"x\":true,\"y\":true,\"z\":true}"),
                new Mutation(1, "/z", TrueFalseMutagen.class, "Changed boolean value from true to false.", "{\"w\":false,\"x\":true,\"y\":false,\"z\":false}")
        });
    }
}
