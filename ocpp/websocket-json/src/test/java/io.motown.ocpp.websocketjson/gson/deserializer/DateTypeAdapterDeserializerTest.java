package io.motown.ocpp.websocketjson.gson.deserializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateTypeAdapterDeserializerTest {

    @Test
    public void defaultFormatShouldBeSupported() throws ParseException {
        String dateString = "2015-01-02T12:12:02Z";
        DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        iso8601Format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date expectedDate = iso8601Format.parse(dateString);

        assertEquals(expectedDate, new DateTypeAdapterDeserializer().deserialize(getJsonElement(dateString), null, null));
    }

    @Test
    public void multipleFormatsShouldBeSupported() throws ParseException {
        String format1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        String date1 = "2015-01-02T12:12:02Z";
        String format2 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        String date2 = "2015-01-02T12:12:02.312+01:00";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");

        DateTypeAdapterDeserializer deserializer = new DateTypeAdapterDeserializer(Arrays.asList(format1, format2), timeZone);

        deserializer.deserialize(getJsonElement(date1), null, null);
        deserializer.deserialize(getJsonElement(date2), null, null);
    }

    @Test(expected = JsonParseException.class)
    public void unknownFormatShouldResultInException() {
        // by default the format with GMT offset is not supported
        String dateString = "2015-01-04T13:42:33+01:00";

        new DateTypeAdapterDeserializer().deserialize(getJsonElement(dateString), null, null);
    }

    private JsonElement getJsonElement(String date) {
        return new JsonPrimitive(date);
    }

}
