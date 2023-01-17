package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.std.MapProperty;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.function.BiConsumer;

public class EmptyingMutagen implements Mutagen {
    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) {
        return false; // serializeAsPotentialField(gen, writer.getType(), writer.getName(), indicator, (g, name) -> {});
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        JavaType type = writer instanceof MapProperty ? writer.getType().getContentType() : writer.getType();
        return trySerializeAsField(gen, type, writer.getName(), indicator, (g, name) -> { try { g.writeFieldName(name); } catch (Exception e) { throw new RuntimeException(e); }});
    }

    @Override
    public boolean serializeAsPrimitiveArray(Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, boolean isField) throws IOException {
        return trySerializeAsPrimitiveCollectionLike(gen, writer, Array.getLength(array), indicator, isField);
    }

    @Override
    public boolean serializeAsPrimitiveCollection(Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, boolean isField) throws IOException {
        return trySerializeAsPrimitiveCollectionLike(gen, writer, collection.size(), indicator, isField);
    }

    protected boolean trySerializeAsPrimitiveCollectionLike(JsonGenerator gen, PropertyWriter writer, int length, MutationIndexIndicator indicator, boolean isField) throws IOException {
        JavaType type = writer.getType().getContentType();
        if (type.isTypeOrSubTypeOf(CharSequence.class) && length > 0 && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            if (isField) { gen.writeFieldName(writer.getName()); }
            gen.writeStartArray();
            for (int i = 0; i < length; ++i) { gen.writeString(""); }
            gen.writeEndArray();
            indicator.setDescription("Emptied collection.");
            indicator.setMutagen(this.getClass());
            indicator.setPath(gen.getOutputContext().pathAsPointer().toString() + "[*]");
            return true;
        }
        return false;
    }

    protected boolean trySerializeAsField(JsonGenerator gen, JavaType type, String name, MutationIndexIndicator indicator, BiConsumer<JsonGenerator, String> handleFieldName) throws Exception {
        // TODO: Check for @NotEmpty @NotBlank @Size
        boolean result = false;
        if (type.isTypeOrSubTypeOf(CharSequence.class)) {
            if (indicator.targetMutationIndex == indicator.currentMutationIndex++) {
                handleFieldName.accept(gen, name);
                gen.writeString("");
                indicator.setDescription("Emptied string.");
                indicator.setMutagen(this.getClass());
                indicator.setPath(gen.getOutputContext().pathAsPointer().toString());
                result = true;
            }
        } else if (type.isArrayType() || type.isCollectionLikeType()) {
            if (indicator.targetMutationIndex == indicator.currentMutationIndex++) {
                handleFieldName.accept(gen, name);
                gen.writeStartArray();
                gen.writeEndArray();
                indicator.setDescription("Emptied array.");
                indicator.setMutagen(this.getClass());
                indicator.setPath(gen.getOutputContext().pathAsPointer().toString());
                result = true;
            }
        } else if (!type.isPrimitive() && !type.isTypeOrSubTypeOf(Number.class)) {
            if (indicator.targetMutationIndex == indicator.currentMutationIndex++) {
                handleFieldName.accept(gen, name);
                gen.writeStartObject();
                gen.writeEndObject();
                indicator.setDescription("Emptied object.");
                indicator.setMutagen(this.getClass());
                indicator.setPath(gen.getOutputContext().pathAsPointer().toString());
                result = true;
            }
        }
        return result;
    }
}
