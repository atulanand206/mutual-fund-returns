package com.creations.funds.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

final public class ISODateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private static final String TAG = ISODateAdapter.class.getSimpleName();

    private static final String ISO_8601_FORMAT = "dd-MM-yyyy";

    private static final String ISO_8601_REGEX = "[0-9]{2}-[0-9]{2}-[0-9]{4}";

    private static final SimpleDateFormat iso8601Format = new SimpleDateFormat(ISO_8601_FORMAT, Locale.getDefault());

    public ISODateAdapter() {
    }

    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        final var dateFormatAsString = iso8601Format.format(src);
        return new JsonPrimitive(dateFormatAsString);
    }

    public synchronized Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }

        return parseDate(json.getAsString());
    }

    public static Date parseDate(String dateString) {
        try {
            if (dateString.matches(ISO_8601_REGEX)) {
                return iso8601Format.parse(dateString);
            } else {
                throw new JsonParseException(String.format("Unexpected date format for string: %s", dateString));
            }
        } catch (Exception e) {
            throw new JsonParseException(String.format("Invalid date: %s", dateString), e);
        }
    }
}

