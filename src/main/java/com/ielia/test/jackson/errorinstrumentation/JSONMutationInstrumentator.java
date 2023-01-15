package com.ielia.test.jackson.errorinstrumentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ielia.test.jackson.errorinstrumentation.filters.CompositeFilter;
import com.ielia.test.jackson.errorinstrumentation.mutagens.DataTypeMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.EmptyingMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.FieldNameCaseSwapMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.FieldNameEmptyingMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.MaxMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.MinMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.Mutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.NullifierMutagen;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class JSONMutationInstrumentator {
    protected static final Mutagen[] DEFAULT_MUTAGENS = {
            new DataTypeMutagen(),
            new EmptyingMutagen(),
            new FieldNameCaseSwapMutagen(),
            new FieldNameEmptyingMutagen(),
            new MaxMutagen(),
            new MinMutagen(),
            new NullifierMutagen()
    };

    protected final Object bean;
    protected final Mutagen[] mutagens;

    public JSONMutationInstrumentator(Object bean) {
        this(bean, DEFAULT_MUTAGENS);
    }

    public JSONMutationInstrumentator(Object bean, Mutagen... mutagens) {
        this.bean = bean;
        this.mutagens = mutagens;
    }

    public Stream<String> getErrorCombinations() {
        AtomicLong targetMutationIndex = new AtomicLong(-1);

        // ObjectMapper firstMapper = new ObjectMapper();
        // MutationIndexIndicator firstIndexIndicator = new MutationIndexIndicator(targetMutationIndex.get());

        try {
            String originalJSON = new ObjectMapper().writeValueAsString(bean);
            // String firstErroneousJSON = getWriter(firstMapper, firstIndexIndicator).writeValueAsString(bean);
            return Stream.iterate(
                    "",
                    json -> !originalJSON.equals(json),
                    json -> {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            return getWriter(mapper, new MutationIndexIndicator(targetMutationIndex.incrementAndGet()))
                                    .writeValueAsString(bean);
                        } catch (JsonProcessingException exception) {
                            throw new RuntimeException(exception);
                        }
                    }
            ).skip(1).filter(Objects::nonNull);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Error while serializing DTO into JSON", exception);
        }
    }

    // @SuppressWarnings({"unchecked", "rawtypes"})
    protected ObjectWriter getWriter(ObjectMapper mapper, MutationIndexIndicator indexIndicator) throws JsonMappingException {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter(CompositeFilter.FILTER_ID, new CompositeFilter(indexIndicator, mutagens));
        /*
        SimpleSerializers serializers = new SimpleSerializers();
        Class<?>[] types = new Class<?>[] {
                boolean[].class,
                byte[].class,
                char[].class,
                short[].class,
                int[].class,
                long[].class,
                float[].class,
                double[].class,
                // ArrayList.class,
        };
        SerializationConfig serializationConfig = mapper.getSerializationConfig();
        SerializerFactory serializerFactory = mapper.getSerializerFactory();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        for (Class<?> type : types) {
            JavaType javaType = mapper.constructType(type);
            TypeSerializer typeSerializer = serializerFactory.createTypeSerializer(serializationConfig, javaType);
            JsonSerializer serializer = new ObjectArraySerializer(javaType.getContentType(), false, typeSerializer, null);
            serializers.addSerializer(type, serializer);
        }
        for (Class<?> type : types) {
            serializers.addSerializer(OverrideStdArraySerializers.findStandardImpl(type));
        }
        */
        return mapper.setFilterProvider(filterProvider)
                // .setSerializerFactory(PatchedSerializerFactory.instance)
                // .setSerializerFactory(BeanSerializerFactory.instance.withConfig(
                //         new SerializerFactoryConfig().withAdditionalSerializers(serializers))
                // )
                .addMixIn(Array.class, CompositeFilter.Mixin.class)
                .addMixIn(Collection.class, CompositeFilter.Mixin.class)
                .addMixIn(Map.class, CompositeFilter.Mixin.class)
                .addMixIn(Object.class, CompositeFilter.Mixin.class)
                .addMixIn(Object[].class, CompositeFilter.Mixin.class)
                .addMixIn(boolean[].class, CompositeFilter.Mixin.class)
                .addMixIn(byte[].class, CompositeFilter.Mixin.class)
                .addMixIn(char[].class, CompositeFilter.Mixin.class)
                .addMixIn(double[].class, CompositeFilter.Mixin.class)
                .addMixIn(float[].class, CompositeFilter.Mixin.class)
                .addMixIn(int[].class, CompositeFilter.Mixin.class)
                .addMixIn(long[].class, CompositeFilter.Mixin.class)
                .addMixIn(short[].class, CompositeFilter.Mixin.class)
                .writer();
    }
}
