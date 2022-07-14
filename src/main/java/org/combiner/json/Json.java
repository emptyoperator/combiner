package org.combiner.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Json {
    private static final Gson BEAUTIFIER = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private static final Gson MINIFIER = new GsonBuilder().serializeNulls().create();

    public static String beautify(String maybeJson) {
        return reformat(BEAUTIFIER, maybeJson);
    }

    public static String minify(String maybeJson) {
        return reformat(MINIFIER, maybeJson);
    }

    private static String reformat(Gson gson, String maybeJson) {
        try {
            return gson.toJson(JsonParser.parseString(maybeJson));
        } catch (JsonSyntaxException e) {
            return maybeJson;
        }
    }
}
