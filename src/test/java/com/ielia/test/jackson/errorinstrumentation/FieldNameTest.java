package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.mutagens.FieldNameCaseSwapMutagen;
import com.ielia.test.jackson.errorinstrumentation.beans.NullableMembersDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.FieldNameEmptyingMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

// FIXME: Add tests for objects within collections and arrays.
public class FieldNameTest extends TestNGTest {
    @Test(groups = "unit")
    public void testCaseSwap() {
        Mutation[] actual = new JSONMutationInstrumentator(new NullableMembersDTO(), new FieldNameCaseSwapMutagen()).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/field", FieldNameCaseSwapMutagen.class, "Swapped field name case.", "{\"FIELD\":1,\"value\":2,\"methodValue\":3}"),
                new Mutation(1, "/value", FieldNameCaseSwapMutagen.class, "Swapped field name case.", "{\"field\":1,\"VALUE\":2,\"methodValue\":3}"),
                new Mutation(2, "/methodValue", FieldNameCaseSwapMutagen.class, "Swapped field name case.", "{\"field\":1,\"value\":2,\"METHODvALUE\":3}"),
        });
    }

    @Test(groups = "unit")
    public void testEmptyingFieldName() {
        Mutation[] actual = new JSONMutationInstrumentator(new NullableMembersDTO(), new FieldNameEmptyingMutagen()).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/field", FieldNameEmptyingMutagen.class, "Emptied field name.", "{\"\":1,\"value\":2,\"methodValue\":3}"),
                new Mutation(1, "/value", FieldNameEmptyingMutagen.class, "Emptied field name.", "{\"field\":1,\"\":2,\"methodValue\":3}"),
                new Mutation(2, "/methodValue", FieldNameEmptyingMutagen.class, "Emptied field name.", "{\"field\":1,\"value\":2,\"\":3}"),
        });
    }
}
