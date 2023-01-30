package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

public class NegZeroPosMutagen implements Mutagen {
    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, Class<?>... groups) throws Exception {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) throws Exception {
        Integer newValue = getNewValue(writer, groups);
        if (newValue != null && indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            ++indicator.currentMutationIndex;
            gen.writeFieldName(writer.getName());
            gen.writeNumber(newValue);
            indicator.setDescription("Changed value from " + ((BeanPropertyWriter) writer).get(bean) + " to " + newValue + ".");
            indicator.setMutagen(this.getClass());
            indicator.setPath(gen.getOutputContext().pathAsPointer().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveArray(Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) throws Exception {
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveCollection(Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) throws Exception {
        return false;
    }

    protected Integer getNewValue(PropertyWriter writer, Class<?>[] groups) {
        return getBoundedValue(
                getAppliedAnnotation(Negative.class, Negative::groups, Negative.List.class, Negative.List::value, writer, groups) != null,
                getAppliedAnnotation(NegativeOrZero.class, NegativeOrZero::groups, NegativeOrZero.List.class, NegativeOrZero.List::value, writer, groups) != null,
                getAppliedAnnotation(PositiveOrZero.class, PositiveOrZero::groups, PositiveOrZero.List.class, PositiveOrZero.List::value, writer, groups) != null,
                getAppliedAnnotation(Positive.class, Positive::groups, Positive.List.class, Positive.List::value, writer, groups) != null,
                -1, 0, 1
        );
    }
}
