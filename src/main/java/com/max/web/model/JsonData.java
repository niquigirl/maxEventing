package com.max.web.model;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.*;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * JSON serialize/deserializer
 */
public abstract class JsonData implements Serializable
{
    /**
     * Format to serialize dates in JSON: "MM/dd/yyyy HH:mm:ss"
     */
    public static final String STANDARD_DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
    public static final String STANDARD_DATE_FORMAT = "MM/dd/yyyy";

    @Override
    public String toString()
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return mapper.writeValueAsString(this);
        }
        catch (IOException e)
        {
            return "INVALID JSON";
        }
    }

    /**
     * Defines dates to be serialized in the format {@link #STANDARD_DATE_FORMAT} : {@code "MM/dd/yyyy HH:mm:ss"}
     */
    public static class CustomDateSerializer extends JsonSerializer<Date>
    {
        @Override
        public void serialize(Date data, JsonGenerator aJsonGenerator, SerializerProvider aSerializerProvider)
                throws IOException
        {
            if (data != null)
            {
                SimpleDateFormat format = new SimpleDateFormat(STANDARD_DATE_TIME_FORMAT);
                aJsonGenerator.writeString(format.format(data));
            }
        }
    }

    /**
     * Defines dates to be deserialized from the format {@link #STANDARD_DATE_FORMAT} : {@code "MM/dd/yyyy HH:mm:ss"}
     */
    public static class CustomDateDeserializer extends JsonDeserializer<Date>
    {
        @Override
        public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException
        {
            SimpleDateFormat format = new SimpleDateFormat(STANDARD_DATE_FORMAT);
            String date = jsonparser.getText();
            try
            {
                return format.parse(date);
            }
            catch (ParseException e)
            {
                throw new RuntimeException(e);
            }

        }
    }
}