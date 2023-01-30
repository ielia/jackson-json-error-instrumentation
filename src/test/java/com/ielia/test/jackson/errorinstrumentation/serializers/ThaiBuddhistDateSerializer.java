package com.ielia.test.jackson.errorinstrumentation.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.chrono.ThaiBuddhistDate;

public class ThaiBuddhistDateSerializer extends JsonSerializer<ThaiBuddhistDate> {
    @Override
    public void serialize(ThaiBuddhistDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.toString());
    }
}
