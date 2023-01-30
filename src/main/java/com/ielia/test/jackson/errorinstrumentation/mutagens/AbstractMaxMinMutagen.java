package com.ielia.test.jackson.errorinstrumentation.mutagens;

import java.lang.annotation.Annotation;

public abstract class AbstractMaxMinMutagen extends AbstractNumberValueMutagen {
    protected boolean numberApplies(Annotation[] annotations, Class<?> propClass) {
        return annotations.length > 0 || (propClass.isPrimitive() && propClass != boolean.class && propClass != char.class);
    }
}
