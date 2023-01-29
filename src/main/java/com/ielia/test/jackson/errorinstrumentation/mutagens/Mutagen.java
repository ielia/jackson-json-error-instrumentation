package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import java.util.Collection;
import java.util.Set;

public interface Mutagen {
    /**
     * @param bean Object to mutate upon serialization.
     * @param gen Jackson JSON generator.
     * @param provider Jackson serializer provider.
     * @param writer Jackson property writer.
     * @param indexIndicator Mutation index indicator.
     * @param groups Validation groups.
     * @return True if mutation happened made, false otherwise.
     * @throws Exception For any internal exception arising.
     */
    boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, Class<?>... groups) throws Exception;

    /**
     * @param bean Object to mutate upon serialization.
     * @param gen Jackson JSON generator.
     * @param provider Jackson serializer provider.
     * @param writer Jackson property writer.
     * @param indexIndicator Mutation index indicator.
     * @param groups Validation groups.
     * @return True if mutation happened made, false otherwise.
     * @throws Exception For any internal exception arising.
     */
    boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, Class<?>... groups) throws Exception;

    /**
     * @param array Array of primitives or strings to mutate upon serialization.
     * @param gen Jackson JSON generator.
     * @param provider Jackson serializer provider.
     * @param writer Jackson property writer.
     * @param indexIndicator Mutation index indicator.
     * @param isField Indicates whether or not to serialize the value with its field name.
     * @param groups Validation groups.
     * @return True if mutation happened made, false otherwise.
     * @throws Exception For any internal exception arising.
     */
    boolean serializeAsPrimitiveArray(Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) throws Exception;

    /**
     * @param collection Collection to mutate upon serialization.
     * @param gen Jackson JSON generator.
     * @param provider Jackson serializer provider.
     * @param writer Jackson property writer.
     * @param indexIndicator Mutation index indicator.
     * @param isField Indicates whether or not to serialize the value with its field name.
     * @param groups Validation groups.
     * @return True if mutation happened made, false otherwise.
     * @throws Exception For any internal exception arising.
     */
    boolean serializeAsPrimitiveCollection(Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) throws Exception;

    /**
     * TODO: See where to move this method to.
     * @param annotationGroups Constraint annotation groups.
     * @param validationGroups Validation groups.
     * @return True if either one of the arrays is empty or if the intersection is not empty, false otherwise.
     */
    default boolean groupsOverlap(Class<?>[] annotationGroups, Class<?>[] validationGroups) {
        if (annotationGroups == null || annotationGroups.length == 0 || validationGroups == null || validationGroups.length == 0) {
            return true;
        }
        Set<Class<?>> checkSet = Set.of(annotationGroups);
        for (Class<?> group : validationGroups) {
            if (checkSet.contains(group)) {
                return true;
            }
        }
        return false;
    }
}
