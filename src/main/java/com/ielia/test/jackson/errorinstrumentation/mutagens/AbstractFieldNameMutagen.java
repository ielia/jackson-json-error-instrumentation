package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import java.util.Collection;

public abstract class AbstractFieldNameMutagen implements Mutagen {
    protected abstract String mutateFieldName(PropertyWriter writer);

    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        if (indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            gen.writeFieldName(mutateFieldName(writer));
            writer.serializeAsElement(bean, gen, provider);
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveArray(boolean isField, Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator) {
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveCollection(boolean isField, Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator) {
        return false;
    }
}
