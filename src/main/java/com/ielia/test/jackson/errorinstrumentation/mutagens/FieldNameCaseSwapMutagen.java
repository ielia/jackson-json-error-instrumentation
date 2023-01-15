package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.databind.ser.PropertyWriter;
import org.apache.commons.lang3.StringUtils;

public class FieldNameCaseSwapMutagen extends AbstractFieldNameMutagen {
    @Override
    protected String mutateFieldName(PropertyWriter writer) {
        // FIXME: Serialize key correctly. See MapSerializer and MapProperty.
        // if (writer instanceof MapProperty) { ... }
        return StringUtils.swapCase(writer.getName());
    }
}
