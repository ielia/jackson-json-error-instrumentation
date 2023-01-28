package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Collection;

public abstract class AbstractNumberValueMutagen implements Mutagen {
    protected abstract Annotation[] getAnnotations(PropertyWriter writer, Class<?>[] groups);

    protected abstract BigDecimal getReplacementValue(Annotation[] annotations, Class<?> propClass);

    protected boolean numberApplies(Annotation[] annotations, Class<?> propClass) {
        return annotations.length > 0 || (propClass.isPrimitive() && propClass != boolean.class && propClass != char.class);
    }

    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) throws Exception {
        Annotation[] annotations = getAnnotations(writer, groups);
        Class<?> propClass = writer.getType().getRawClass();
        if (numberApplies(annotations, propClass) && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            BigDecimal newValue = getReplacementValue(annotations, propClass);
            gen.writeFieldName(writer.getName());
            gen.writeRawValue(newValue.toString());
            indicator.setDescription("Changed value from " + ((BeanPropertyWriter) writer).get(bean) + " to " + newValue + ".");
            indicator.setMutagen(this.getClass());
            indicator.setPath(gen.getOutputContext().pathAsPointer().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveArray(Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) throws Exception {
        return false; // TODO: See if there is any max spec for array elements.
    }

    @Override
    public boolean serializeAsPrimitiveCollection(Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) throws Exception {
        return false; // TODO: See if there is any max spec for collection elements.
    }
}
