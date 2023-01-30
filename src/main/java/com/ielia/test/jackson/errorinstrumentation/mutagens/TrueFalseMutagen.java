package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import java.util.Collection;

public class TrueFalseMutagen implements Mutagen {
    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, Class<?>... groups) throws Exception {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) throws Exception {
        Boolean annApplied = annotationApplied(writer, groups);
        if (annApplied != null && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            boolean newValue = !annApplied;
            gen.writeBooleanField(writer.getName(), newValue); // TODO: Check if it is possible to have any other field types.
            indicator.setDescription("Changed boolean value from " + ((BeanPropertyWriter) writer).get(bean) + " to " + newValue + ".");
            indicator.setMutagen(this.getClass());
            indicator.setPath(gen.getOutputContext().pathAsPointer().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveArray(Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) throws Exception {
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveCollection(Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) throws Exception {
        return false;
    }

    protected Boolean annotationApplied(PropertyWriter writer, Class<?>[] groups) {
        boolean assertsFalse = getAppliedAnnotation(AssertFalse.class, AssertFalse::groups, AssertFalse.List.class, AssertFalse.List::value, writer, groups) != null;
        boolean assertsTrue = getAppliedAnnotation(AssertTrue.class, AssertTrue::groups, AssertTrue.List.class, AssertTrue.List::value, writer, groups) != null;
        return assertsFalse
                ? assertsTrue ? null : Boolean.FALSE
                : assertsTrue ? Boolean.TRUE : null;
    }
}
