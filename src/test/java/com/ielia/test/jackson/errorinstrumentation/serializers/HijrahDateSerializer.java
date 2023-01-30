package com.ielia.test.jackson.errorinstrumentation.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.chrono.HijrahDate;

public class HijrahDateSerializer extends JsonSerializer<HijrahDate> {
    @Override
    public void serialize(HijrahDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.toString());
    }
}
