package utils;

import com.google.gson.Gson;
import org.json.JSONObject;

public class JsonConverter {

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static String toJsonString(Object object) {
        return new Gson().toJson(object);
    }

    public static JSONObject toJsonObject(Object jsonString) {
        return new JSONObject(toJsonString(jsonString));
    }
}
