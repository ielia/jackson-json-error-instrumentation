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
                new Object[] {new NullableMembersDTO(), new String[] {}},
                new Object[] {new NotNullFieldDTO(), new String[] {"{\"value\":null}"}},
                new Object[] {new NotNullGetterDTO(), new String[] {"{\"value\":null}"}},
                new Object[] {new NotNullMethodDTO(), new String[] {"{\"value\":null}"}},
                new Object[] {new NotNullArrayFieldDTO(), new String[] {
                        "{\"values\":null}",
                        "{\"values\":[null,null,null]}", // TODO: See if this is actually correct.
                }},
                new Object[] {new NotNullListFieldDTO(), new String[] {
                        "{\"values\":null}",
                        "{\"values\":[null,null,null]}", // TODO: See if this is actually correct.
                }},
                new Object[] {new NotNullMapFieldDTO(), new String[] {
                        "{\"values\":null}",
                        "{\"values\":{\"a\":null,\"b\":2,\"c\":3}}",
                        "{\"values\":{\"a\":1,\"b\":null,\"c\":3}}",
                        "{\"values\":{\"a\":1,\"b\":2,\"c\":null}}",
                }},
                new Object[] {new NotNullMembersDTO(), new String[] {
                        "{\"field\":null,\"value\":2,\"methodValue\":3}",
                        "{\"field\":1,\"value\":null,\"methodValue\":3}",
                        "{\"field\":1,\"value\":2,\"methodValue\":null}",
                }},
                new Object[] {new NotNullListOfArraysDTO(), new String[] {
                        "{\"values\":null}",
                        // "{\"values\":[null,[3,4]]}",
                        // "{\"values\":[[1,2],null]}",
                }},
                new Object[] {new NotNullListOfListsDTO(), new String[] {
                        "{\"values\":null}",
                        // "{\"values\":[null,[3,4]]}",
                        // "{\"values\":[[1,2],null]}",
                }},
                new Object[] {new NotNullListOfListsOfArraysDTO(), new String[] {
                        "{\"values\":null}",
                        // "{\"values\":[null,null]}",
                        // "{\"values\":[[null,null],[[4],[5,6]]]}",
                        // "{\"values\":[[[1,2],[3]],[null,null]]}",
                }},
                new Object[] {new NotNullListOfObjectsDTO(), new String[] {
                        "{\"values\":null}",
                        // "{\"values\":[null,null,null]}",
                        "{\"values\":[{\"x\":null},{\"x\":2},{\"x\":3}]}",
                        "{\"values\":[{\"x\":1},{\"x\":null},{\"x\":3}]}",
                        "{\"values\":[{\"x\":1},{\"x\":2},{\"x\":null}]}",
                }},
        };
        Object[][] result = new Object[params.length][];
        for (int i = 0; i < params.length; ++i) {
            result[i] = new Object[] { params[i][0].getClass().getSimpleName(), params[i][0], params[i][1] };
        }
        return result;
    }

    @Test(groups = "unit", dataProvider = "not-null-dtos", testName = "test NotNull mutations for %s")
    public void testNotNull(String beanClassName, Object bean, String[] expected) {
        String[] actual = new JSONMutationInstrumentator(bean, new NullifierMutagen()).getErrorCombinations().toArray(String[]::new);
        Assert.assertEquals(actual, expected);
    }
}
