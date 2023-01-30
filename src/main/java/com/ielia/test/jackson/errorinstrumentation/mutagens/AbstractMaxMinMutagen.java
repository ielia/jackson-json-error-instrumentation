package com.ielia.test.jackson.errorinstrumentation.mutagens;

import java.lang.annotation.Annotation;

public abstract class AbstractMaxMinMutagen extends AbstractVariableNumberValueMutagen {
    protected boolean fieldApplies(Annotation[] annotations, Class<?> propClass) {
        return annotations.length > 0 || (propClass.isPrimitive() && propClass != boolean.class && propClass != char.class);
    }
}
