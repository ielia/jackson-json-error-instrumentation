package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.databind.ser.PropertyWriter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MinMutagen extends AbstractMaxMinMutagen {
    protected Map<Class<? extends Number>, BigDecimal> MIN_VALUES_BY_TYPE = new HashMap<>() {{
        put(AtomicInteger.class, BigDecimal.valueOf(Integer.MIN_VALUE));
        put(AtomicLong.class, BigDecimal.valueOf(Long.MIN_VALUE));
        put(Byte.class, BigDecimal.valueOf(Byte.MAX_VALUE));
        put(byte.class, BigDecimal.valueOf(Byte.MAX_VALUE));
        put(Double.class, BigDecimal.valueOf(Double.MIN_VALUE));
        put(double.class, BigDecimal.valueOf(Double.MIN_VALUE));
        put(Float.class, BigDecimal.valueOf(Float.MIN_VALUE));
        put(float.class, BigDecimal.valueOf(Float.MIN_VALUE));
        put(Integer.class, BigDecimal.valueOf(Integer.MIN_VALUE));
        put(int.class, BigDecimal.valueOf(Integer.MIN_VALUE));
        put(Long.class, BigDecimal.valueOf(Long.MIN_VALUE));
        put(long.class, BigDecimal.valueOf(Long.MIN_VALUE));
        put(Short.class, BigDecimal.valueOf(Short.MIN_VALUE));
        put(short.class, BigDecimal.valueOf(Short.MIN_VALUE));
    }};

    @Override
    protected Annotation[] getAnnotations(PropertyWriter writer, Class<?>[] groups) {
        TreeMap<BigDecimal, Annotation> annotations = new TreeMap<>();
        addAnnotations(annotations, writer, groups, DecimalMin.class, DecimalMin.List.class, DecimalMin::groups, DecimalMin.List::value, m -> new BigDecimal(m.value()));
        addAnnotations(annotations, writer, groups, Min.class, Min.List.class, Min::groups, Min.List::value, m -> BigDecimal.valueOf(m.value()));
        return annotations.size() == 0 ? new Annotation[0] : new Annotation[]{annotations.firstEntry().getValue()};
    }

    @Override
    protected BigDecimal getReplacementValue(Annotation[] annotations, Class<?> propClass) {
        BigDecimal minValue;
        if (annotations.length == 0) {
            minValue = MIN_VALUES_BY_TYPE.get(propClass);
        } else {
            Annotation annotation = annotations[0];
            minValue = annotation instanceof Min
                    ? BigDecimal.valueOf(((Min) annotation).value())
                    : new BigDecimal(((DecimalMin) annotation).value());
        }
        return minValue.subtract(BigDecimal.ONE);
    }
}
