package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.databind.ser.PropertyWriter;

public class FieldNameEmptyingMutagen extends AbstractFieldNameMutagen {
    @Override
    protected String getMutationDescription() {
        return "Emptied field name.";
    }

    @Override
    protected String mutateFieldName(PropertyWriter writer) {
        return "";
    }
}
