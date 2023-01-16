package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

public class MinMutagen implements Mutagen {
    protected Map<Class<? extends Number>, BigDecimal> MIN_VALUES_BY_TYPE = new HashMap<Class<? extends Number>, BigDecimal>() {{
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
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator) throws Exception {
        Min min = writer.getAnnotation(Min.class);
        DecimalMin dMin = writer.getAnnotation(DecimalMin.class);
        Class<?> propClass = writer.getType().getRawClass();
        if ((min != null || dMin != null || (propClass.isPrimitive() && propClass != boolean.class && propClass != char.class) ||
                (Number.class.isAssignableFrom(propClass) && BigDecimal.class.isAssignableFrom(propClass) && BigInteger.class.isAssignableFrom(propClass)))
                && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            BigDecimal minValue = min == null
                    ? dMin == null
                            ? MIN_VALUES_BY_TYPE.get(propClass)
                            : new BigDecimal(dMin.value())
                    : new BigDecimal(min.value());
            gen.writeFieldName(writer.getName());
            gen.writeRawValue(minValue.subtract(BigDecimal.ONE).toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveArray(boolean isField, Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator) throws Exception {
        return false; // TODO: See if there is any min spec for array elements.
    }

    @Override
    public boolean serializeAsPrimitiveCollection(boolean isField, Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator) throws Exception {
        return false; // TODO: See if there is any min spec for collection elements.
    }
}
