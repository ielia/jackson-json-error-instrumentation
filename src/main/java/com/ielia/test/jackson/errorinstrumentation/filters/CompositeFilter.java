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

import java.util.Arrays;
import java.util.Collection;

public class CompositeFilter extends SimpleBeanPropertyFilter {
    public static final String FILTER_ID = "JSON-Automated-QA-Test-Mutagenic-Composite-Filter";

    protected final Class<?>[] groups;
    protected final MutationIndexIndicator indexIndicator;
    protected final Mutagen[] mutagens;

    @JsonFilter(FILTER_ID)
    public static class Mixin {}

    public CompositeFilter(MutationIndexIndicator indexIndicator, Class<?>[] groups, Mutagen[] mutagens) {
        this.indexIndicator = indexIndicator;
        this.groups = groups;
        this.mutagens = mutagens;
    }

    @Override
    public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        // TODO: See if it makes sense to make the view available to the mutagens.
        if (provider.getActiveView() == null || Arrays.asList(((BeanPropertyWriter) writer).getViews()).contains(provider.getActiveView())) {
            boolean mutated = false;
            if (indexIndicator.currentMutationIndex <= indexIndicator.targetMutationIndex) {
                for (Mutagen mutagen : mutagens) {
                    mutated |= mutagen.serializeAsElement(bean, gen, provider, writer, indexIndicator, groups);
                }
            }
            if (!mutated) {
                super.serializeAsElement(bean, gen, provider, writer);
            }
        }
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        // TODO: See if it makes sense to make the view available to the mutagens.
        if (provider.getActiveView() == null || Arrays.asList(((BeanPropertyWriter) writer).getViews()).contains(provider.getActiveView())) {
            boolean mutated = false;
            if (indexIndicator.currentMutationIndex <= indexIndicator.targetMutationIndex) {
                for (Mutagen mutagen : mutagens) {
                    mutated |= mutagen.serializeAsField(bean, gen, provider, writer, indexIndicator, groups);
                }
                if (!mutated) {
                    mutated = serializeDirectProps(bean, gen, provider, writer, groups);
                }
            }
            if (!mutated) {
                super.serializeAsField(bean, gen, provider, writer);
            }
        }
    }

    protected boolean serializeDirectProps(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, Class<?>[] groups) throws Exception {
        boolean mutated = false;
        JavaType propType = writer.getType();
        JavaType contentType = propType.getContentType();
        if (propType.isArrayType()) {
            if (contentType.isPrimitive() || contentType.isTypeOrSubTypeOf(CharSequence.class) || contentType.isTypeOrSubTypeOf(Number.class)) {
                Object prop = ((BeanPropertyWriter) writer).get(bean);
                if (prop != null) {
                    for (Mutagen mutagen : mutagens) {
                        mutated |= mutagen.serializeAsPrimitiveArray(prop, gen, provider, writer, indexIndicator, true, groups);
                    }
                }
            }
        } else if (Collection.class.isAssignableFrom(propType.getRawClass())) {
            if (contentType.isTypeOrSubTypeOf(CharSequence.class) || contentType.isTypeOrSubTypeOf(Number.class)) {
                Collection<?> prop = (Collection<?>) ((BeanPropertyWriter) writer).get(bean);
                if (prop != null) {
                    for (Mutagen mutagen : mutagens) {
                        mutated |= mutagen.serializeAsPrimitiveCollection(prop, gen, provider, writer, indexIndicator, true, groups);
                    }
                }
            }
        }
        return mutated;
    }
}
