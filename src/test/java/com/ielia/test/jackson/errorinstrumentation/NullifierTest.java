package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.beans.NotNullArrayFieldDTO;
import com.ielia.test.jackson.errorinstrumentation.beans.NotNullListFieldDTO;
import com.ielia.test.jackson.errorinstrumentation.beans.NotNullListOfArraysDTO;
import com.ielia.test.jackson.errorinstrumentation.beans.NotNullListOfListsDTO;
import com.ielia.test.jackson.errorinstrumentation.beans.NotNullListOfListsOfArraysDTO;
import com.ielia.test.jackson.errorinstrumentation.beans.NotNullListOfObjectsDTO;
import com.ielia.test.jackson.errorinstrumentation.beans.NotNullMapFieldDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.NullifierMutagen;
import com.ielia.test.jackson.errorinstrumentation.beans.NotNullFieldDTO;
import com.ielia.test.jackson.errorinstrumentation.beans.NotNullGetterDTO;
import com.ielia.test.jackson.errorinstrumentation.beans.NotNullMembersDTO;
import com.ielia.test.jackson.errorinstrumentation.beans.NotNullMethodDTO;
import com.ielia.test.jackson.errorinstrumentation.beans.NullableMembersDTO;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NullifierTest extends TestNGTest {
    @DataProvider(name = "not-null-dtos")
    public Object[][] notNullDTOs() {
        // FIXME: See if the commented-out cases make sense.
        Object[][] params = new Object[][] {
                new Object[] { new NullableMembersDTO(), new Mutation[] {}},
                new Object[] { new NotNullFieldDTO(), new Mutation[] { new Mutation(0, "/value", NullifierMutagen.class, "Nullified value.", "{\"value\":null}") }},
                new Object[] { new NotNullGetterDTO(), new Mutation[] { new Mutation(0, "/value", NullifierMutagen.class, "Nullified value.", "{\"value\":null}") }},
                new Object[] { new NotNullMethodDTO(), new Mutation[] { new Mutation(0, "/value", NullifierMutagen.class, "Nullified value.", "{\"value\":null}") }},
                new Object[] { new NotNullArrayFieldDTO(), new Mutation[] {
                        new Mutation(0, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":null}"),
                        new Mutation(1, "/values[*]", NullifierMutagen.class, "Nullified all values of the collection.", "{\"values\":[null,null,null]}"), // TODO: See if this is actually correct.
                }},
                new Object[] { new NotNullListFieldDTO(), new Mutation[] {
                        new Mutation(0, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":null}"),
                        new Mutation(1, "/values[*]", NullifierMutagen.class, "Nullified all values of the collection.", "{\"values\":[null,null,null]}"), // TODO: See if this is actually correct.
                }},
                new Object[] { new NotNullMapFieldDTO(), new Mutation[] {
                        new Mutation(0, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":null}"),
                        new Mutation(1, "/values/a", NullifierMutagen.class, "Nullified value.", "{\"values\":{\"a\":null,\"b\":2,\"c\":3}}"),
                        new Mutation(2, "/values/b", NullifierMutagen.class, "Nullified value.", "{\"values\":{\"a\":1,\"b\":null,\"c\":3}}"),
                        new Mutation(3, "/values/c", NullifierMutagen.class, "Nullified value.", "{\"values\":{\"a\":1,\"b\":2,\"c\":null}}"),
                }},
                new Object[] { new NotNullMembersDTO(), new Mutation[] {
                        new Mutation(0, "/field", NullifierMutagen.class, "Nullified value.", "{\"field\":null,\"value\":2,\"methodValue\":3}"),
                        new Mutation(1, "/value", NullifierMutagen.class, "Nullified value.", "{\"field\":1,\"value\":null,\"methodValue\":3}"),
                        new Mutation(2, "/methodValue", NullifierMutagen.class, "Nullified value.", "{\"field\":1,\"value\":2,\"methodValue\":null}"),
                }},
                new Object[] { new NotNullListOfArraysDTO(), new Mutation[] {
                        new Mutation(0, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":null}"),
                        // new Mutation(1, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":[null,[3,4]]}"),
                        // new Mutation(2, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":[[1,2],null]}"),
                }},
                new Object[] { new NotNullListOfListsDTO(), new Mutation[] {
                        new Mutation(0, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":null}"),
                        // new Mutation(1, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":[null,[3,4]]}"),
                        // new Mutation(2, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":[[1,2],null]}"),
                }},
                new Object[] { new NotNullListOfListsOfArraysDTO(), new Mutation[] {
                        new Mutation(0, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":null}"),
                        // new Mutation(1, "/values[*]", NullifierMutagen.class, "Nullified value.", "{\"values\":[null,null]}"),
                        // new Mutation(2, "/values[*][*]", NullifierMutagen.class, "Nullified value.", "{\"values\":[[null,null],[[4],[5,6]]]}"),
                        // new Mutation(3, "/values[*][*][*]", NullifierMutagen.class, "Nullified value.", "{\"values\":[[[1,2],[3]],[null,null]]}"),
                }},
                new Object[] { new NotNullListOfObjectsDTO(), new Mutation[] {
                        new Mutation(0, "/values", NullifierMutagen.class, "Nullified value.", "{\"values\":null}"),
                        // new Mutation(1, "/values[*]", NullifierMutagen.class, "Nullified value.", "{\"values\":[null,null,null]}"),
                        new Mutation(1, "/values/0/x", NullifierMutagen.class, "Nullified value.", "{\"values\":[{\"x\":null},{\"x\":2},{\"x\":3}]}"),
                        new Mutation(2, "/values/1/x", NullifierMutagen.class, "Nullified value.", "{\"values\":[{\"x\":1},{\"x\":null},{\"x\":3}]}"),
                        new Mutation(3, "/values/2/x", NullifierMutagen.class, "Nullified value.", "{\"values\":[{\"x\":1},{\"x\":2},{\"x\":null}]}"),
                }},
        };
        Object[][] result = new Object[params.length][];
        for (int i = 0; i < params.length; ++i) {
            result[i] = new Object[] { params[i][0].getClass().getSimpleName(), params[i][0], params[i][1] };
        }
        return result;
    }

    @Test(groups = "unit", dataProvider = "not-null-dtos", testName = "test NotNull mutations for %s")
    public void testNotNull(String beanClassName, Object bean, Mutation[] expected) {
        Mutation[] actual = new JSONMutationInstrumentator(bean, new NullifierMutagen()).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, expected);
    }
}
