package Common.Controller.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Unpacker {
    public JSONObject unpackByteToJson(byte[] bytes) {
        String jsonString = new String(bytes, StandardCharsets.UTF_8);
        return new JSONObject(jsonString);
    }

    public <T> List<T> unpackJsonArray(JSONArray jsonArray, Function<JSONObject, T> mapper) {
        List<T> result = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T item = mapper.apply(jsonObject);
            result.add(item);
        }
        return result;
    }
}
