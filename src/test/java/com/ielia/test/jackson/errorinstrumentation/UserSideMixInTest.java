package com.ielia.test.jackson.errorinstrumentation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.ielia.test.jackson.errorinstrumentation.beans.NonAnnotatedDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.NullifierMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserSideMixInTest extends TestNGTest {
    public static class View1 {}
    public static class View2 {}
    public static class View3 {}

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class Mixin {
        @JsonView(View1.class) @JsonProperty private int privateValue;
        @JsonIgnore public int ignored;
        @JsonView({ View2.class, View3.class }) @JsonProperty protected int protectedValue;
        @JsonView(View2.class) @JsonIgnore protected int ignoredProtected;
    }

    @Test(groups = "unit")
    public void testMixins() {
        // FIXME: Use individual, more manageable tests instead of this too big DataTypeChangeDTO class.
        Mutation[] actual = new JSONMutationInstrumentator(new NonAnnotatedDTO(), new NullifierMutagen())
                .addMixIn(NonAnnotatedDTO.class, Mixin.class)
                .getErrorCombinations()
                .toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[]{
                new Mutation(0, "/privateValue", NullifierMutagen.class, "Nullified value.", "{\"privateValue\":null,\"protectedValue\":3}"),
                new Mutation(1, "/protectedValue", NullifierMutagen.class, "Nullified value.", "{\"privateValue\":1,\"protectedValue\":null}"),
        });
    }

    @Test(groups = "unit")
    public void testMixinsWithViews() {
        // FIXME: Use individual, more manageable tests instead of this too big DataTypeChangeDTO class.
        Mutation[] actual = new JSONMutationInstrumentator(new NonAnnotatedDTO(), new NullifierMutagen())
                .addMixIn(NonAnnotatedDTO.class, Mixin.class)
                .withView(View2.class)
                .getErrorCombinations()
                .toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[]{
                new Mutation(0, "/protectedValue", NullifierMutagen.class, "Nullified value.", "{\"protectedValue\":null}"),
        });
    }
}