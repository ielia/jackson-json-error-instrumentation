package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.databind.ser.PropertyWriter;
import org.apache.commons.lang3.StringUtils;

public class FieldNameCaseSwapMutagen extends AbstractFieldNameMutagen {
    @Override
    protected String getMutationDescription() {
        return "Swapped field name case.";
    }

    @Override
    protected String mutateFieldName(PropertyWriter writer) {
        return StringUtils.swapCase(writer.getName());
    }
}
