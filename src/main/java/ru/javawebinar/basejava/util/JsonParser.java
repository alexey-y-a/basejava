package ru.javawebinar.basejava.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.javawebinar.basejava.model.Section;

import java.io.IOException;
import java.time.LocalDate;

public class JsonParser {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                @Override
                public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
                    if (localDate == null) {
                        jsonWriter.nullValue();
                        return;
                    }
                    jsonWriter.beginArray()
                            .value(localDate.getYear())
                            .value(localDate.getMonthValue())
                            .value(localDate.getDayOfMonth())
                            .endArray();
                }

                @Override
                public LocalDate read(JsonReader jsonReader) throws IOException {
                    jsonReader.beginArray();
                    double yearDouble = jsonReader.nextDouble();
                    double monthDouble = jsonReader.nextDouble();
                    double dayDouble = jsonReader.nextDouble();
                    jsonReader.endArray();
                    int year = (int) yearDouble;
                    int month = (int) monthDouble;
                    int day = (int) dayDouble;
                    return LocalDate.of(year, month, day);
                }
            })
            .create();

    public static String write(Object object, Class<? extends Section> clazz) {
        return GSON.toJson(object, clazz);
    }

    public static <T> T read(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
}
