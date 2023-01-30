package com.ielia.test.jackson.errorinstrumentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ielia.test.jackson.errorinstrumentation.filters.CompositeFilter;
import com.ielia.test.jackson.errorinstrumentation.mutagens.DataTypeMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.EmptyingMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.FieldNameCaseSwapMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.FieldNameEmptyingMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.FractionDigitsMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.IntegerDigitsMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.MaxMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.MinMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.Mutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.NegZeroPosMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.NullifierMutagen;
import com.ielia.test.jackson.errorinstrumentation.mutagens.PastPresentFutureMutagen;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
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
            new FractionDigitsMutagen(),
            new IntegerDigitsMutagen(),
            new MaxMutagen(),
            new MinMutagen(),
            new NegZeroPosMutagen(),
            new NullifierMutagen(),
            new PastPresentFutureMutagen()
    };

    protected final Object bean;
    protected final Mutagen[] mutagens;
    protected final Map<Class<?>, Class<?>> mixins;
    protected Class<?>[] groups;
    protected Class<?> view;

    public JSONMutationInstrumentator(Object bean) {
        this(bean, DEFAULT_MUTAGENS);
    }

    public JSONMutationInstrumentator(Object bean, Mutagen... mutagens) {
        this.bean = bean;
        this.mutagens = mutagens;
        mixins = new HashMap<>();
        view = null;
    }

    public JSONMutationInstrumentator addMixIn(Class<?> target, Class<?> mixIn) {
        mixins.put(target, mixIn);
        return this;
    }

    public JSONMutationInstrumentator withGroups(Class<?>... groups) {
        this.groups = groups;
        return this;
    }

    public JSONMutationInstrumentator withView(Class<?> view) {
        this.view = view;
        return this;
    }

    public Stream<Mutation> getErrorCombinations() {
        AtomicLong targetMutationIndex = new AtomicLong(-1);

        try {
            final String originalJSON = getWriter(new ObjectMapper()).writeValueAsString(bean);
            return Stream.iterate(
                    new Mutation(),
                    mutation -> !originalJSON.equals(mutation.getJSON()),
                    mutation -> {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            MutationIndexIndicator indexIndicator = new MutationIndexIndicator(targetMutationIndex.incrementAndGet());
                            String json = getWriter(mapper, indexIndicator).writeValueAsString(bean);
                            return new Mutation(indexIndicator.targetMutationIndex, indexIndicator.getPath(),
                                    indexIndicator.getMutagen(), indexIndicator.getDescription(), json);
                        } catch (JsonProcessingException exception) {
                            throw new RuntimeException(exception);
                        }
                    }
            ).skip(1).filter(Objects::nonNull);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Error while serializing DTO into JSON", exception);
        }
    }

    protected ObjectWriter getWriter(ObjectMapper mapper, MutationIndexIndicator indexIndicator) throws JsonMappingException {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter(CompositeFilter.FILTER_ID, new CompositeFilter(indexIndicator, groups, mutagens));
        mapper.setFilterProvider(filterProvider)
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
                .addMixIn(short[].class, CompositeFilter.Mixin.class);
        return getWriter(mapper);
    }

    protected ObjectWriter getWriter(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
        for (Map.Entry<Class<?>, Class<?>> mixinSpec : mixins.entrySet()) {
            mapper.addMixIn(mixinSpec.getKey(), mixinSpec.getValue());
        }
        return mapper.writerWithView(view);
    }
}
