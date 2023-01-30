package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.databind.ser.PropertyWriter;

import javax.validation.constraints.Digits;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.TreeMap;

public class FractionDigitsMutagen extends AbstractNumberValueMutagen {
    @Override
    protected Annotation[] getAnnotations(PropertyWriter writer, Class<?>[] groups) {
        TreeMap<BigDecimal, Annotation> annotations = new TreeMap<>();
        addAnnotations(annotations, writer, groups, Digits.class, Digits.List.class, Digits::groups, Digits.List::value, m -> new BigDecimal(m.fraction()));
        return annotations.size() == 0 ? new Annotation[0] : new Annotation[]{annotations.lastEntry().getValue()};
    }

    @Override
    protected BigDecimal getReplacementValue(Annotation[] annotations, Class<?> propClass) {
        return BigDecimal.ONE.subtract(BigDecimal.valueOf(0.1).pow(((Digits) annotations[0]).fraction() + 1));
    }

    @Override
    protected boolean numberApplies(Annotation[] annotations, Class<?> propClass) {
        return annotations.length > 0 && ((Digits) annotations[0]).fraction() > 0;
    }
}
