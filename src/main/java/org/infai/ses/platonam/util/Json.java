/*
 * Copyright 2021 InfAI (CC SES)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.infai.ses.platonam.util;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Json {

    public static String toJSON(Map<String, Object> data) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Type collectionType = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.toJson(data, collectionType);
    }

    public static String toJSON(List<Map<String, Object>> data) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Type collectionType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        return gson.toJson(data, collectionType);
    }

    public static List<Map<String, Object>> typeSafeMapListFromJson(String data) {
        Gson gson = getTypeSafeGson();
        return gson.fromJson(data, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
    }

    public static Map<String, Object> typeSafeMapFromJson(String data) {
        Gson gson = getTypeSafeGson();
        return gson.fromJson(data, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static List<Map<String, Object>> listMapFromJson(String data) {
        return new Gson().fromJson(data, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
    }

    public static Map<String, Object> mapFromJson(String data) {
        return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    private static Gson getTypeSafeGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Map.class, new MapDeserializer());
        builder.registerTypeAdapter(List.class, new ListDeserializer());
        return builder.create();
    }

    private static Number parse(String str) {
        Number number;
        try {
            number = Long.parseLong(str);
        } catch (NumberFormatException e) {
            number = Double.parseDouble(str);
        }
        return number;
    }

    private static class MapDeserializer implements JsonDeserializer<Map<String, Object>> {
        @Override
        public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Map<String, Object> m = new LinkedTreeMap<>();
            JsonObject jo = json.getAsJsonObject();
            for (Map.Entry<String, JsonElement> mx : jo.entrySet()) {
                Object value;
                if (mx.getValue().isJsonPrimitive()) {
                    try {
                        value = parse(mx.getValue().getAsString());
                    } catch (Exception e) {
                        value = context.deserialize(mx.getValue(), Object.class);
                    }
                    m.put(mx.getKey(), value);
                } else if (mx.getValue().isJsonArray()) {
                    m.put(mx.getKey(), context.deserialize(mx.getValue(), List.class));
                } else if (mx.getValue().isJsonObject()) {
                    m.put(mx.getKey(), context.deserialize(mx.getValue(), Map.class));
                } else if (mx.getValue().isJsonNull()) {
                    m.put(mx.getKey(), null);
                }
            }
            return m;
        }
    }

    private static class ListDeserializer implements JsonDeserializer<List<Object>> {
        @Override
        public List<Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<Object> l = new ArrayList<>();
            JsonArray ja = json.getAsJsonArray();
            for (JsonElement ax : ja) {
                Object value;
                if (ax.isJsonPrimitive()) {
                    try {
                        value = parse(ax.getAsString());
                    } catch (Exception e) {
                        value = context.deserialize(ax, Object.class);
                    }
                    l.add(value);
                } else if (ax.isJsonArray()) {
                    l.add(context.deserialize(ax, List.class));
                } else if (ax.isJsonObject()) {
                    l.add(context.deserialize(ax, Map.class));
                } else if (ax.isJsonNull()) {
                    l.add(null);
                }
            }
            return l;
        }
    }
}
