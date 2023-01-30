package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import java.util.Collection;

public abstract class AbstractFieldNameMutagen implements Mutagen {
    protected abstract String getMutationDescription();

    protected abstract String mutateFieldName(PropertyWriter writer);

    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) throws Exception {
        if (indicator.targetMutationIndex == indicator.currentMutationIndex++) {
            String newFieldName = mutateFieldName(writer);
            gen.writeFieldName(newFieldName);
            writer.serializeAsElement(bean, gen, provider);
            setIndicatorFields(gen, writer.getName(), newFieldName, indicator);
            return true;
        }
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveArray(Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) {
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveCollection(Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) {
        return false;
    }

    protected void setIndicatorFields(JsonGenerator gen, String origFieldName, String newFieldName, MutationIndexIndicator indicator) {
        indicator.setDescription(getMutationDescription());
        String path = gen.getOutputContext().pathAsPointer().toString();
        indicator.setPath(path.substring(0, path.length() - newFieldName.length()) + origFieldName);
        indicator.setMutagen(this.getClass());
    }
}
