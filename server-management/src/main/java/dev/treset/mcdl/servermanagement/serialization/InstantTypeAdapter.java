package dev.treset.mcdl.servermanagement.serialization;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class InstantTypeAdapter extends TypeAdapter<Instant> {
    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        out.value(DateTimeFormatter.ISO_INSTANT.format(value));
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        return DateTimeFormatter.ISO_INSTANT.parse(in.nextString(), Instant::from);
    }
}
