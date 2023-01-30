package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.TreeMap;
import java.util.function.Function;

public abstract class AbstractVariableNumberValueMutagen implements Mutagen {
    protected abstract Annotation[] getAnnotations(PropertyWriter writer, Class<?>[] groups);

    protected abstract BigDecimal getReplacementValue(Annotation[] annotations, Class<?> propClass);

    protected abstract boolean fieldApplies(Annotation[] annotations, Class<?> propClass);

    @SuppressWarnings("ForLoopReplaceableByForEach")
    protected <A extends Annotation, L extends Annotation> void addAnnotations(
            TreeMap<BigDecimal, Annotation> annotations, PropertyWriter writer, Class<?>[] groups,
            Class<A> annotationType, Class<L> annotationListType,
            Function<A, Class<?>[]> getGroups, Function<L, A[]> getSubAnnotations, Function<A, BigDecimal> getValue) {
        A annotation = writer.getAnnotation(annotationType);
        addAnnotation(annotations, annotation, groups, getGroups, getValue);
        L listAnnotation = writer.getAnnotation(annotationListType);
        A[] subAnnotations = listAnnotation == null ? null : getSubAnnotations.apply(listAnnotation);
        if (subAnnotations != null) {
            for (int i = 0, len = subAnnotations.length; i < len; ++i) {
                annotation = subAnnotations[i];
                addAnnotation(annotations, annotation, groups, getGroups, getValue);
            }
        }
    }

    protected <A extends Annotation> void addAnnotation(TreeMap<BigDecimal, Annotation> annotations, A annotation, Class<?>[] groups,
                                                        Function<A, Class<?>[]> getGroups, Function<A, BigDecimal> getValue) {
        if (annotation != null && groupsOverlap(getGroups.apply(annotation), groups)) {
            annotations.put(getValue.apply(annotation), annotation);
        }
    }

    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) throws Exception {
        Annotation[] annotations = getAnnotations(writer, groups);
        Class<?> propClass = writer.getType().getRawClass();
        if (fieldApplies(annotations, propClass) && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
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
