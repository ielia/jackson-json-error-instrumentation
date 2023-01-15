package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import java.lang.reflect.Array;
import java.util.Collection;

public class DataTypeMutagen implements Mutagen {
    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        if (indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            if (CharSequence.class.isAssignableFrom(writer.getType().getRawClass())) {
                gen.writeNumber(123);
            } else {
                gen.writeString("abc");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        JavaType propType = writer.getType();
        if (indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            // FIXME: Use key serializer. See MapSerializer and MapProperty.
            if (CharSequence.class.isAssignableFrom(propType.getRawClass())) {
                gen.writeNumberField(writer.getName(), 123);
            } else {
                gen.writeStringField(writer.getName(), "abc");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveArray(boolean isField, Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        return tryWritingArray(isField, gen, writer, Array.getLength(array), indicator);
    }

    @Override
    public boolean serializeAsPrimitiveCollection(boolean isField, Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        return tryWritingArray(isField, gen, writer, collection.size(), indicator);
    }

    protected boolean tryWritingArray(boolean isField, JsonGenerator gen, PropertyWriter writer, int length, MutationIndexIndicator indicator) throws Exception {
        if (indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            if (isField) { gen.writeFieldName(writer.getName()); }
            gen.writeStartArray();
            if (CharSequence.class.isAssignableFrom(writer.getType().getContentType().getRawClass())) {
                for (int i = 0; i < length; ++i) { gen.writeNumber(123); }
            } else {
                for (int i = 0; i < length; ++i) { gen.writeString("abc"); }
            }
            gen.writeEndArray();
            return true;
        }
        return false;
    }
}
