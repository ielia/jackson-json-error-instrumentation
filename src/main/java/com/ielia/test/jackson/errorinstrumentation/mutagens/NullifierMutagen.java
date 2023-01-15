package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;

public class NullifierMutagen implements Mutagen {
    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        if (writer.getAnnotation(NotNull.class) != null && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            gen.writeNullField(writer.getName());
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveArray(boolean isField, Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        return trySerializeAsPrimitiveCollectionLike(isField, writer.getName(), gen, Array.getLength(array), indicator);
    }

    @Override
    public boolean serializeAsPrimitiveCollection(boolean isField, Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        return trySerializeAsPrimitiveCollectionLike(isField, writer.getName(), gen, collection.size(), indicator);
    }

    protected boolean trySerializeAsPrimitiveCollectionLike(boolean isField, String fieldName, JsonGenerator gen, int length, MutationIndexIndicator indicator) throws IOException {
        if (length > 0 && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            if (isField) { gen.writeFieldName(fieldName); }
            gen.writeStartArray();
            for (int i = 0; i < length; ++i) { gen.writeNull(); }
            gen.writeEndArray();
            return true;
        }
        return false;
    }
}
