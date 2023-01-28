package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.beans.ValidationGroupsDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.MaxMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.MinMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.NullifierMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ValidationGroupsTest extends TestNGTest {
    @Test(groups = "unit")
    public void testValidationGroupsWork() {
        Mutation[] actual = new JSONMutationInstrumentator(new ValidationGroupsDTO(), new NullifierMutagen(), new MaxMutagen(), new MinMutagen())
                .withGroups(ValidationGroupsDTO.Group2.class)
                .getErrorCombinations()
                .toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[]{
                new Mutation(0, "/b", NullifierMutagen.class, "Nullified value.", "{\"a\":1,\"b\":null,\"c\":3,\"d\":4,\"e\":5,\"f\":6}"),
                new Mutation(1, "/d", MaxMutagen.class, "Changed value from 4 to 21.", "{\"a\":1,\"b\":2,\"c\":3,\"d\":21,\"e\":5,\"f\":6}"),
                new Mutation(2, "/e", NullifierMutagen.class, "Nullified value.", "{\"a\":1,\"b\":2,\"c\":3,\"d\":4,\"e\":null,\"f\":6}"),
                new Mutation(3, "/f", MinMutagen.class, "Changed value from 6 to -21.", "{\"a\":1,\"b\":2,\"c\":3,\"d\":4,\"e\":5,\"f\":-21}")
        });
    }
}
