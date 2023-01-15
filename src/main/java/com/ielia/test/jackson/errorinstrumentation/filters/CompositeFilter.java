package com.ielia.test.jackson.errorinstrumentation.filters;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;
import com.ielia.test.jackson.errorinstrumentation.mutagens.Mutagen;

import java.util.Collection;

public class CompositeFilter extends SimpleBeanPropertyFilter {
    public static final String FILTER_ID = "JSON-Automated-QA-Test-Mutagenic-Composite-Filter";

    protected final MutationIndexIndicator indexIndicator;
    protected final Mutagen[] mutagens;

    @JsonFilter(FILTER_ID)
    public static class Mixin {}

    public CompositeFilter(MutationIndexIndicator indexIndicator, Mutagen... mutagens) {
        this.indexIndicator = indexIndicator;
        this.mutagens = mutagens;
    }

    @Override
    public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        boolean mutated = false;
        for (Mutagen mutagen : mutagens) {
            mutated |= mutagen.serializeAsElement(bean, gen, provider, writer, indexIndicator);
        }
        if (!mutated) {
            super.serializeAsElement(bean, gen, provider, writer);
        }
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        gen.getOutputContext().pathAsPointer();
        boolean mutated = false;
        for (Mutagen mutagen : mutagens) {
            mutated |= mutagen.serializeAsField(bean, gen, provider, writer, indexIndicator);
        }
        if (!mutated) {
            mutated = serializeDirectProps(bean, gen, provider, writer);
        }
        if (!mutated) {
            super.serializeAsField(bean, gen, provider, writer);
        }
    }

    public boolean serializeDirectProps(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        boolean mutated = false;
        JavaType propType = writer.getType();
        JavaType contentType = propType.getContentType();
        if (propType.isArrayType()) {
            if (contentType.isPrimitive() || contentType.isTypeOrSubTypeOf(CharSequence.class) || contentType.isTypeOrSubTypeOf(Number.class)) {
                Object prop = ((BeanPropertyWriter) writer).get(bean);
                if (prop != null) {
                    for (Mutagen mutagen : mutagens) {
                        mutated |= mutagen.serializeAsPrimitiveArray(true, prop, gen, provider, writer, indexIndicator);
                    }
                }
            }
        } else if (Collection.class.isAssignableFrom(propType.getRawClass())) {
            if (contentType.isTypeOrSubTypeOf(CharSequence.class) || contentType.isTypeOrSubTypeOf(Number.class)) {
                Collection<?> prop = (Collection<?>) ((BeanPropertyWriter) writer).get(bean);
                if (prop != null) {
                    for (Mutagen mutagen : mutagens) {
                        mutated |= mutagen.serializeAsPrimitiveCollection(true, prop, gen, provider, writer, indexIndicator);
                    }
                }
            }
        }
        return mutated;
    }
}
