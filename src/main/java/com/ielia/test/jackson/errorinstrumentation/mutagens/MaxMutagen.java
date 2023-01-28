package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.databind.ser.PropertyWriter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MaxMutagen extends AbstractMaxMinMutagen {
    protected Map<Class<?>, BigDecimal> MAX_VALUES_BY_TYPE = new HashMap<>() {{
        put(AtomicInteger.class, BigDecimal.valueOf(Integer.MAX_VALUE));
        put(AtomicLong.class, BigDecimal.valueOf(Long.MAX_VALUE));
        put(Byte.class, BigDecimal.valueOf(Byte.MAX_VALUE));
        put(byte.class, BigDecimal.valueOf(Byte.MAX_VALUE));
        put(Double.class, BigDecimal.valueOf(Double.MAX_VALUE));
        put(double.class, BigDecimal.valueOf(Double.MAX_VALUE));
        put(Float.class, BigDecimal.valueOf(Float.MAX_VALUE));
        put(float.class, BigDecimal.valueOf(Float.MAX_VALUE));
        put(Integer.class, BigDecimal.valueOf(Integer.MAX_VALUE));
        put(int.class, BigDecimal.valueOf(Integer.MAX_VALUE));
        put(Long.class, BigDecimal.valueOf(Long.MAX_VALUE));
        put(long.class, BigDecimal.valueOf(Long.MAX_VALUE));
        put(Short.class, BigDecimal.valueOf(Short.MAX_VALUE));
        put(short.class, BigDecimal.valueOf(Short.MAX_VALUE));
    }};

    @Override
    protected Annotation[] getAnnotations(PropertyWriter writer, Class<?>[] groups) {
        TreeMap<BigDecimal, Annotation> annotations = new TreeMap<>();
        addAnnotations(annotations, writer, groups, DecimalMax.class, DecimalMax.List.class, DecimalMax::groups, DecimalMax.List::value, m -> new BigDecimal(m.value()));
        addAnnotations(annotations, writer, groups, Max.class, Max.List.class, Max::groups, Max.List::value, m -> BigDecimal.valueOf(m.value()));
        return annotations.size() == 0 ? new Annotation[0] : new Annotation[]{annotations.lastEntry().getValue()};
    }

    @Override
    protected BigDecimal getReplacementValue(Annotation[] annotations, Class<?> propClass) {
        BigDecimal maxValue;
        if (annotations.length == 0) {
            maxValue = MAX_VALUES_BY_TYPE.get(propClass);
        } else {
            Annotation annotation = annotations[0];
            maxValue = annotation instanceof Max
                    ? BigDecimal.valueOf(((Max) annotation).value())
                    : new BigDecimal(((DecimalMax) annotation).value());
        }
        return maxValue.add(BigDecimal.ONE);
    }
}
