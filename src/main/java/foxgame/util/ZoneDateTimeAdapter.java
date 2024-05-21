package foxgame.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A GSON {@link TypeAdapter} for the {@link ZonedDateTime} class.
 */
public class ZoneDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
    /**
     * The formatter used to parse and format {@link ZonedDateTime} objects.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    /**
     * Writes the given {@link ZonedDateTime} object to the given {@link JsonWriter}.
     *
     * @param jsonWriter    the {@link JsonWriter} to write to
     * @param zonedDateTime the {@link ZonedDateTime} object to write
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void write(JsonWriter jsonWriter, ZonedDateTime zonedDateTime) throws IOException {
        jsonWriter.value(zonedDateTime.format(FORMATTER));
    }

    /**
     * Reads a {@link ZonedDateTime} object from the given {@link JsonReader}.
     *
     * @param jsonReader the {@link JsonReader} to read from
     * @return the {@link ZonedDateTime} object read
     * @throws IOException if an I/O error occurs
     */
    @Override
    public ZonedDateTime read(JsonReader jsonReader) throws IOException {
        return ZonedDateTime.parse(jsonReader.nextString(), FORMATTER);
    }
}
