package com.creations.funds.jackson;

import com.creations.funds.gson.ISODateAdapter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Date;

public class DateDeserializer extends StdDeserializer<Date> {


    protected DateDeserializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        final var mapper = (ObjectMapper) jsonParser.getCodec();
        TextNode root = mapper.readTree(jsonParser);
        try {
            return ISODateAdapter.parseDate(root.textValue());
        } catch (Exception e) {
            throw new JsonParseException(String.format("Invalid date: %s", root.textValue()), e);
        }
    }
}
