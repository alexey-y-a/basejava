package ru.javawebinar.basejava.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.javawebinar.basejava.model.ListSection;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.model.TextSection;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonSectionAdapter implements JsonSerializer<Section>, JsonDeserializer<Section> {
    @Override
    public JsonElement serialize(Section section, Type type, JsonSerializationContext context) {
        if (section instanceof TextSection) {
            return new JsonPrimitive(((TextSection) section).getContent());
        } else if (section instanceof ListSection) {
            JsonArray jsonArray = new JsonArray();
            ((ListSection) section).getItems().forEach(item -> jsonArray.add(new JsonPrimitive(item)));
            return jsonArray;
        }
        throw new JsonParseException("Unknown section type: " + section.getClass().getName());
    }

    @Override
    public Section deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            return new TextSection(json.getAsString());
        } else if (json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            ListSection listSection = new ListSection(new ArrayList<>());
            for (JsonElement element : array) {
                if (element.isJsonPrimitive()) {
                    listSection.getItems().add(element.getAsString());
                }
            }
            return listSection;
        } else if (json.isJsonObject()) {
            return null;
        }
        throw new JsonParseException("Unknown section type: " + json.getClass().getName());
    }
}