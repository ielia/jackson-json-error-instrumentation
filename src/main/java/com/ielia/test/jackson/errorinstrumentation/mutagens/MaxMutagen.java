package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MaxMutagen implements Mutagen {
    protected Map<Class<?>, BigDecimal> MAX_VALUES_BY_TYPE = new HashMap<Class<?>, BigDecimal>() {{
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
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        Max max = writer.getAnnotation(Max.class);
        DecimalMax dMax = writer.getAnnotation(DecimalMax.class);
        Class<?> propClass = writer.getType().getRawClass();
        if ((max != null || dMax != null || (propClass.isPrimitive() && propClass != boolean.class && propClass != char.class) ||
                (Number.class.isAssignableFrom(propClass) && BigDecimal.class.isAssignableFrom(propClass) && BigInteger.class.isAssignableFrom(propClass)))
                && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            BigDecimal maxValue = max == null
                    ? dMax == null
                        ? MAX_VALUES_BY_TYPE.get(propClass)
                        : new BigDecimal(dMax.value())
                    : new BigDecimal(max.value());
            gen.writeFieldName(writer.getName());
            gen.writeRawValue(maxValue.add(BigDecimal.ONE).toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveArray(boolean isField, Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator) throws Exception {
        return false; // TODO: See if there is any max spec for array elements.
    }

    @Override
    public boolean serializeAsPrimitiveCollection(boolean isField, Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator) throws Exception {
        return false; // TODO: See if there is any max spec for collection elements.
    }
}
