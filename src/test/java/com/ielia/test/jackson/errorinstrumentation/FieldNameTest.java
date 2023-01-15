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
        String[] actual = new JSONMutationInstrumentator(new NullableMembersDTO(), new FieldNameCaseSwapMutagen()).getErrorCombinations().toArray(String[]::new);
        Assert.assertEquals(actual, new String[] {
                "{\"FIELD\":1,\"value\":2,\"methodValue\":3}",
                "{\"field\":1,\"VALUE\":2,\"methodValue\":3}",
                "{\"field\":1,\"value\":2,\"METHODvALUE\":3}",
        });
    }

    @Test(groups = "unit")
    public void testEmptyingFieldName() {
        String[] actual = new JSONMutationInstrumentator(new NullableMembersDTO(), new FieldNameEmptyingMutagen()).getErrorCombinations().toArray(String[]::new);
        Assert.assertEquals(actual, new String[] {
                "{\"\":1,\"value\":2,\"methodValue\":3}",
                "{\"field\":1,\"\":2,\"methodValue\":3}",
                "{\"field\":1,\"value\":2,\"\":3}",
        });
    }
}
