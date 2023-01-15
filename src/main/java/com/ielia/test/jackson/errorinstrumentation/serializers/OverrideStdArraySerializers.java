package com.ielia.test.jackson.errorinstrumentation.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.std.ArraySerializerBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.ielia.test.jackson.errorinstrumentation.filters.CompositeFilter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.HashMap;

// TODO: Remove unused.
public class OverrideStdArraySerializers {
    protected final static HashMap<Class<?>, JsonSerializer<?>> _arraySerializers = new HashMap<>() {{
        put(boolean[].class, new GenericArraySerializer<>(boolean[].class, Boolean.class, JsonFormatTypes.BOOLEAN));
        put(byte[].class, new GenericArraySerializer<>(byte[].class, Byte.class, JsonFormatTypes.INTEGER, "byte"));
        put(char[].class, new GenericArraySerializer<>(char[].class, Character.class, JsonFormatTypes.STRING));
        put(short[].class, new GenericArraySerializer<>(short[].class, Short.class, JsonFormatTypes.INTEGER));
        put(int[].class, new GenericArraySerializer<>(int[].class, Integer.class, JsonFormatTypes.INTEGER));
        put(long[].class, new GenericArraySerializer<>(long[].class, Long.class, JsonFormatTypes.NUMBER));
        put(float[].class, new GenericArraySerializer<>(float[].class, Float.class, JsonFormatTypes.NUMBER));
        put(double[].class, new GenericArraySerializer<>(double[].class, Double.class, JsonFormatTypes.NUMBER));
    }};

    protected OverrideStdArraySerializers() {}

    /**
     * Accessor for checking to see if there is a standard serializer for
     * given primitive value type.
     */
    public static JsonSerializer<?> findStandardImpl(Class<?> cls) {
        return _arraySerializers.get(cls);
    }

    @JacksonStdImpl
    @SuppressWarnings({ "deprecation", "unchecked" })
    public static class GenericArraySerializer<T, U> extends ArraySerializerBase<T> {
        private final Class<T> baseClass;
        private final Class<U> elementClass;
        private final JavaType valueType;
        private final JsonFormatTypes formatType;
        private final String itemSchema;

        public GenericArraySerializer(Class<T> beanClass, Class<U> elementClass, JsonFormatTypes formatType) {
            this(beanClass, elementClass, formatType, formatType.name().toLowerCase());
        }

        public GenericArraySerializer(Class<T> beanClass, Class<U> elementClass, JsonFormatTypes formatType, String itemSchema) {
            super(beanClass);
            this.baseClass = beanClass;
            this.elementClass = elementClass;
            this.valueType = TypeFactory.defaultInstance().uncheckedSimpleType(elementClass);
            this.formatType = formatType;
            this.itemSchema = itemSchema;
        }

        protected GenericArraySerializer(GenericArraySerializer src, BeanProperty prop, Boolean unwrapSingle) {
            super(src, prop, unwrapSingle);
            this.baseClass = src.baseClass;
            this.elementClass = src.elementClass;
            this.valueType = TypeFactory.defaultInstance().uncheckedSimpleType(src.elementClass);
            this.formatType = src.formatType;
            this.itemSchema = src.itemSchema;
        }

        @Override
        public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
            return new GenericArraySerializer<>(this, prop, unwrapSingle);
        }

        @Override
        public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
            // TODO: Check if this is correct:
            return this;
        }

        @Override
        public JavaType getContentType() {
            return valueType;
        }

        @Override
        public JsonSerializer<?> getContentSerializer() {
            return null;
        }

        @Override
        public boolean hasSingleElement(T value) {
            return Array.getLength(value) == 1;
        }

        @Override
        public boolean isEmpty(SerializerProvider prov, T value) {
            return Array.getLength(value) == 0;
        }

        @Override
        public final void serialize(T value, JsonGenerator g, SerializerProvider provider) throws IOException {
            final int len = Array.getLength(value);
            if ((len == 1) && _shouldUnwrapSingle(provider)) {
                serializeContents(value, g, provider);
                return;
            }
            g.writeStartArray(value, len);
            serializeContents(value, g, provider);
            g.writeEndArray();
        }

        @Override
        public void serializeContents(T value, JsonGenerator g, SerializerProvider provider) throws IOException {
            // JsonSerializer<Object> ser = provider.findValueSerializer(valueType);
            PropertyFilter filter = provider.getFilterProvider().findPropertyFilter(CompositeFilter.FILTER_ID, elementClass);
            /*
            for (int i = 0, len = Array.getLength(value); i < len; ++i) {
                // ser.serialize(Array.get(value, i), g, provider);
                // filter.serializeAsElement(Array.get(value, i), g, provider);
            }
            */
            throw new IOException("Not implemented yet.");
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            ObjectNode o = createSchemaNode("array", true);
            o.set("items", createSchemaNode(itemSchema));
            return o;
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            visitArrayFormat(visitor, typeHint, formatType);
        }
    }
}
