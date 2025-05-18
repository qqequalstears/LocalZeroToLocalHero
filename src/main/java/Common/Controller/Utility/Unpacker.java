package Common.Controller.Utility;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class Unpacker {
    public JSONObject unpackByteToJson(byte[] bytes) {
        String jsonString = new String(bytes, StandardCharsets.UTF_8);
        return new JSONObject(jsonString);
    }
}
