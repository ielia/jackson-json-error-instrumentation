package com.ielia.test.jackson.errorinstrumentation.serializers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.std.ArraySerializerBase;
import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;

// TODO: Remove unused.
public class PatchedSerializerFactory extends BeanSerializerFactory {
    public final static PatchedSerializerFactory instance = new PatchedSerializerFactory(null);

    /**
     * Constructor for creating instances with specified configuration.
     *
     * @param config Configuration.
     */
    protected PatchedSerializerFactory(SerializerFactoryConfig config) {
        super(config);
    }

    @Override
    public SerializerFactory withConfig(SerializerFactoryConfig config)
    {
        if (_factoryConfig == config) {
            return this;
        }
        if (getClass() != PatchedSerializerFactory.class) {
            throw new IllegalStateException("Subtype of PatchedSerializerFactory (" + getClass().getName()
                    + ") has not properly overridden method 'withAdditionalSerializers': cannot instantiate subtype with "
                    + "additional serializer definitions");
        }
        return new PatchedSerializerFactory(config);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType baseType) throws JsonMappingException {
        JsonSerializer<Object> serializer = super.createSerializer(prov, baseType);
        if (serializer instanceof ArraySerializerBase<?> arraySerializerBase && !(arraySerializerBase instanceof ObjectArraySerializer)) {
            // Class<?> handledType = serializer.handledType();
            JavaType contentType = arraySerializerBase.getContentType();
            TypeSerializer typeSerializer = prov.findTypeSerializer(baseType);
            JsonSerializer<Object> elementSerializer = prov.findValueSerializer(contentType);
            return (JsonSerializer) new ObjectArraySerializer(baseType, baseType.useStaticType(), typeSerializer, elementSerializer);
        }
        return serializer;
    }
}
