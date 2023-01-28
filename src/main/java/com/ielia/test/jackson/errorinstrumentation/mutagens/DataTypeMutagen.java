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
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) throws Exception {
        return tryWritingValue(gen, writer, indicator, false);
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) throws Exception {
        return tryWritingValue(gen, writer, indicator, true);
    }

    @Override
    public boolean serializeAsPrimitiveArray(Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, boolean isField, Class<?>... groups) throws Exception {
        return tryWritingArray(gen, writer, Array.getLength(array), indicator, isField);
    }

    @Override
    public boolean serializeAsPrimitiveCollection(Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, boolean isField, Class<?>... groups) throws Exception {
        return tryWritingArray(gen, writer, collection.size(), indicator, isField);
    }

    protected String getTypeName(JavaType type) {
        if (type.isArrayType()) {
            return type.getContentType().toCanonical() + "[]";
        }
        return type.toCanonical();
    }

    protected boolean tryWritingArray(JsonGenerator gen, PropertyWriter writer, int length, MutationIndexIndicator indicator, boolean withFieldName, Class<?>... groups) throws Exception {
        if (indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            JavaType propType = writer.getType();
            if (withFieldName) { gen.writeFieldName(writer.getName()); }
            gen.writeStartArray();
            if (CharSequence.class.isAssignableFrom(writer.getType().getContentType().getRawClass())) {
                for (int i = 0; i < length; ++i) {
                    gen.writeNumber(123);
                    indicator.setDescription("Replaced strings with numbers.");
                }
            } else {
                for (int i = 0; i < length; ++i) {
                    gen.writeString("abc");
                    indicator.setDescription("Replaced " + getTypeName(propType.getContentType()) + " values with strings.");
                }
            }
            gen.writeEndArray();
            indicator.setMutagen(DataTypeMutagen.class);
            indicator.setPath(gen.getOutputContext().pathAsPointer().toString() + "[*]");
            return true;
        }
        return false;
    }

    protected boolean tryWritingValue(JsonGenerator gen, PropertyWriter writer, MutationIndexIndicator indicator, boolean withFieldName, Class<?>... groups) throws Exception {
        if (indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            JavaType propType = writer.getType();
            if (withFieldName) { gen.writeFieldName(writer.getName()); }
            if (CharSequence.class.isAssignableFrom(propType.getRawClass())) {
                gen.writeNumber(123);
                indicator.setDescription("Replaced string with number.");
            } else {
                gen.writeString("abc");
                indicator.setDescription("Replaced " + getTypeName(propType) + " value with string.");
            }
            indicator.setMutagen(DataTypeMutagen.class);
            indicator.setPath(gen.getOutputContext().pathAsPointer().toString());
            return true;
        }
        return false;
    }
}
