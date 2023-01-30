package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.databind.ser.PropertyWriter;

import javax.validation.constraints.Digits;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.TreeMap;

public class IntegerDigitsMutagen extends AbstractVariableNumberValueMutagen {
    @Override
    protected Annotation[] getAnnotations(PropertyWriter writer, Class<?>[] groups) {
        TreeMap<BigDecimal, Annotation> annotations = new TreeMap<>();
        addAnnotations(annotations, writer, groups, Digits.class, Digits.List.class, Digits::groups, Digits.List::value, m -> new BigDecimal(m.integer()));
        return annotations.size() == 0 ? new Annotation[0] : new Annotation[]{annotations.lastEntry().getValue()};
    }

    @Override
    protected BigDecimal getReplacementValue(Annotation[] annotations, Class<?> propClass) {
        return BigDecimal.TEN.pow(((Digits) annotations[0]).integer());
    }

    @Override
    protected boolean fieldApplies(Annotation[] annotations, Class<?> propClass) {
        return annotations.length > 0 && ((Digits) annotations[0]).integer() > 0;
    }
}
