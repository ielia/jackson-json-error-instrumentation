package com.ielia.test.jackson.errorinstrumentation.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.chrono.JapaneseDate;

public class JapaneseDateSerializer extends JsonSerializer<JapaneseDate> {
    @Override
    public void serialize(JapaneseDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.toString());
    }
}
