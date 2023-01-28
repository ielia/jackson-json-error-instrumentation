package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.beans.DataTypeChangeDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.EmptyingMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmptyingTest extends TestNGTest {
    @Test(groups = "unit")
    public void testEmptying() {
        // FIXME: Use individual, more manageable tests instead of this too big DataTypeChangeDTO class.
        Mutation[] actual = new JSONMutationInstrumentator(new DataTypeChangeDTO(), new EmptyingMutagen()).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                // FIXME: Allow emptying root object.
                // "{}",
                new Mutation(0, "/internal", EmptyingMutagen.class, "Emptied object.", "{\"internal\":{},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}"),
                new Mutation(1, "/internal/val", EmptyingMutagen.class, "Emptied string.", "{\"internal\":{\"val\":\"\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}"),
                new Mutation(2, "/collected", EmptyingMutagen.class, "Emptied array.", "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}"),
                // new Mutation(3, "/collected[*]", EmptyingMutagen.class, "Emptied collection.", "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"\",\"\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}"),
                new Mutation(3, "/values", EmptyingMutagen.class, "Emptied array.", "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[],\"value\":\"3\",\"methodValue\":{\"a\":4,\"b\":5}}"),
                new Mutation(4, "/value", EmptyingMutagen.class, "Emptied string.", "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"\",\"methodValue\":{\"a\":4,\"b\":5}}"),
                new Mutation(5, "/methodValue", EmptyingMutagen.class, "Emptied object.", "{\"internal\":{\"val\":\"Whatever\"},\"collected\":[\"a\",\"b\"],\"field\":1,\"values\":[2.1,2.2],\"value\":\"3\",\"methodValue\":{}}"),
        });
    }
}
