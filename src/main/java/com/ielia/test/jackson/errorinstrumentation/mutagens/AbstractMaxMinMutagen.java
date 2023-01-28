package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.databind.ser.PropertyWriter;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.TreeMap;
import java.util.function.Function;

public abstract class AbstractMaxMinMutagen extends AbstractNumberValueMutagen {
    @SuppressWarnings("ForLoopReplaceableByForEach")
    protected <M extends Annotation, L extends Annotation> void addAnnotations(
            TreeMap<BigDecimal, Annotation> annotations, PropertyWriter writer, Class<?>[] groups,
            Class<M> annotationType, Class<L> annotationListType,
            Function<M, Class<?>[]> getGroups, Function<L, M[]> getSubAnnotations, Function<M, BigDecimal> getValue) {
        M annotation = writer.getAnnotation(annotationType);
        addAnnotation(annotations, annotation, groups, getGroups, getValue);
        L listAnnotation = writer.getAnnotation(annotationListType);
        M[] subAnnotations = listAnnotation == null ? null : getSubAnnotations.apply(listAnnotation);
        if (subAnnotations != null) {
            for (int i = 0, len = subAnnotations.length; i < len; ++i) {
                annotation = subAnnotations[i];
                addAnnotation(annotations, annotation, groups, getGroups, getValue);
            }
        }
    }

    protected <M extends Annotation> void addAnnotation(TreeMap<BigDecimal, Annotation> annotations, M annotation, Class<?>[] groups,
                                                        Function<M, Class<?>[]> getGroups, Function<M, BigDecimal> getValue) {
        if (annotation != null && groupsOverlap(getGroups.apply(annotation), groups)) {
            annotations.put(getValue.apply(annotation), annotation);
        }
    }
}
