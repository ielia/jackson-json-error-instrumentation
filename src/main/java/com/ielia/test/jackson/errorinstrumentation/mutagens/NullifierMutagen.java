package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public class NullifierMutagen implements Mutagen {
    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) throws Exception {
        if ((writer.getType().isPrimitive() || annotationApplies(writer, groups)) && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            gen.writeNullField(writer.getName());
            indicator.setDescription("Nullified value.");
            indicator.setMutagen(this.getClass());
            indicator.setPath(gen.getOutputContext().pathAsPointer().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveArray(Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, boolean isField, Class<?>... groups) throws Exception {
        return false;
        // return trySerializeAsPrimitiveCollectionLike(writer.getName(), gen, Array.getLength(array), indicator, isField);
    }

    @Override
    public boolean serializeAsPrimitiveCollection(Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, boolean isField, Class<?>... groups) throws Exception {
        return false;
        // return trySerializeAsPrimitiveCollectionLike(writer.getName(), gen, collection.size(), indicator, isField);
    }

    protected boolean annotationApplies(PropertyWriter writer, Class<?>[] groups) {
        return getAppliedAnnotation(NotNull.class, NotNull::groups, NotNull.List.class, NotNull.List::value, writer, groups) != null;
    }

    // protected boolean trySerializeAsPrimitiveCollectionLike(String fieldName, JsonGenerator gen, int length, MutationIndexIndicator indicator, boolean isField) throws IOException {
    //     if (length > 0 && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
    //         if (isField) { gen.writeFieldName(fieldName); }
    //         gen.writeStartArray();
    //         for (int i = 0; i < length; ++i) { gen.writeNull(); }
    //         gen.writeEndArray();
    //         indicator.setDescription("Nullified all values of the collection.");
    //         indicator.setMutagen(this.getClass());
    //         indicator.setPath(gen.getOutputContext().pathAsPointer().toString() + "[*]");
    //         return true;
    //     }
    //     return false;
    // }
}
