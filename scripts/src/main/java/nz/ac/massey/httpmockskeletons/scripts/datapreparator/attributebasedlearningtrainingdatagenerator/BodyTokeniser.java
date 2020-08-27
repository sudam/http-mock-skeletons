package nz.ac.massey.httpmockskeletons.scripts.datapreparator.attributebasedlearningtrainingdatagenerator;

import com.google.gson.*;
import com.google.gson.JsonParser;
import java.util.*;
import java.util.Set;

/**
 * This class tokenises each responseBody
 */
public class BodyTokeniser {
    public static void extractKeysGoogle(String section, String jsonString, Set<String> list) {

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonString);
        JsonObject obj = element.getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();

        for (Map.Entry<String, JsonElement> entry : entries) {

            String keyStr = entry.getKey();
            Object keyvalue = obj.get(keyStr);

            if (keyvalue instanceof JsonObject) {
                JsonParser parsers = new JsonParser();
                JsonElement elements = parsers.parse(String.valueOf(keyvalue));
                JsonObject objs = elements.getAsJsonObject();

                Set<Map.Entry<String, JsonElement>> entriess = objs.entrySet();
                for (Map.Entry<String, JsonElement> entrie : entriess) {
                    String keyStr2 = entrie.getKey();
                    Object keyvalue2 = objs.get(keyStr2);

                    String jsonResp = keyvalue2.toString();
                    if (jsonResp.contains("[") && jsonResp.contains("]")) {
                        jsonResp = jsonResp.replace("[", "");
                        jsonResp = jsonResp.replace("]", "");

                        Object jsonRespObj = jsonResp;

                        JsonParser parsers1 = new JsonParser();
                        JsonElement elements1 = parsers1.parse(String.valueOf(jsonRespObj));
                        JsonObject objs1 = elements1.getAsJsonObject();

                        Set<Map.Entry<String, JsonElement>> entriess2 = objs1.entrySet();

                        for (Map.Entry<String, JsonElement> entrie2 : entriess2) {
                            list.add(section + "_" + entry.getKey().concat(".").concat(entrie.getKey()).concat(".").concat(entrie2.getKey()));
                        }
                    } else {
                        list.add(section + "_" + entry.getKey().concat(".").concat(entrie.getKey()));
                    }
                }
            } else {
                if(entry.getKey().toString().equals("Authorization")){

                } else {
                    list.add(section + "_" + entry.getKey());
                }
            }
        }
    }

    public static void extractKeysSlack(String section, String jsonString, Set<String> list) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonString);
        JsonObject obj = element.getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();

        for (Map.Entry<String, JsonElement> entry : entries) {

            String keyStr = entry.getKey();
            Object keyvalue = obj.get(keyStr);

            if (keyvalue instanceof JsonObject) {
                JsonParser parsers = new JsonParser();
                JsonElement elements = parsers.parse(String.valueOf(keyvalue));
                JsonObject objs = elements.getAsJsonObject();

                Set<Map.Entry<String, JsonElement>> entriess = objs.entrySet();
                for (Map.Entry<String, JsonElement> entrie : entriess) {
                    String keyStr2 = entrie.getKey();
                    Object keyvalue2 = objs.get(keyStr2);

                    if (keyvalue2 instanceof JsonObject) {

                        JsonParser parsers1 = new JsonParser();
                        JsonElement elements1 = parsers1.parse(String.valueOf(keyvalue2));
                        JsonObject objs1 = elements1.getAsJsonObject();

                        Set<Map.Entry<String, JsonElement>> entriess2 = objs1.entrySet();

                        for (Map.Entry<String, JsonElement> entrie2 : entriess2) {
                            list.add(section + "_" + entry.getKey().concat(".").concat(entrie.getKey()).concat(".").concat(entrie2.getKey()));
                        }
                    } else {
                        list.add(section + "_" + entry.getKey().concat(".").concat(entrie.getKey()));
                    }
                }
            } else {
                list.add(section + "_" + entry.getKey());
            }
        }
    }

    public static void extractKeysTwitter(String section, String json2, Set<String> list) {

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json2);
        JsonObject obj = element.getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet(); // will return members of your object

        for (Map.Entry<String, JsonElement> entry : entries) {

            String keyStr = entry.getKey();
            Object keyvalue = obj.get(keyStr);

            String jsonResp = keyvalue.toString();

            if (keyvalue instanceof JsonObject) {
                JsonParser parsers = new JsonParser();
                JsonElement elements = parsers.parse(String.valueOf(keyvalue));
                JsonObject objs = elements.getAsJsonObject();

                Set<Map.Entry<String, JsonElement>> entriess = objs.entrySet();
                for (Map.Entry<String, JsonElement> entrie : entriess) {
                    if (!entrie.getKey().equals("entities")) {
                        list.add(section + "_" + entry.getKey().concat(".").concat(entrie.getKey()));
                    }
                }
            } else if (jsonResp.contains("[") && jsonResp.contains("]")) {
                jsonResp = jsonResp.replace("[", "");
                jsonResp = jsonResp.replace("]", "");

                Object jsonRespObj = jsonResp;

                JsonParser parsers1 = new JsonParser();
                JsonElement elements1 = parsers1.parse(String.valueOf(jsonRespObj));
                JsonObject objs1 = elements1.getAsJsonObject();

                Set<Map.Entry<String, JsonElement>> entriess2 = objs1.entrySet();

                for (Map.Entry<String, JsonElement> entrie2 : entriess2) {
                    list.add(section + "_" + entry.getKey().concat(".").concat(entrie2.getKey()));
                }
            } else if (!keyStr.equals("source")
                    && !keyStr.equals("Authorization")
                    && !keyStr.equals("perf")
                    && !keyStr.equals("set-cookie")
                    && !keyStr.equals("x-access-level")
                    && !keyStr.equals("x-transaction-id")
                    && !keyStr.equals("x-tsa-request-body-time")) {
                list.add(section + "_" + entry.getKey());
            }
        }
    }

    public static void extractKeys(String section, String json2, Set<String> list) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json2);
        JsonObject obj = element.getAsJsonObject(); //since you know it's a JsonObject

        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();//will return members of your object

        for (Map.Entry<String, JsonElement> entry : entries) {

            String keyStr = entry.getKey();
            Object keyvalue = obj.get(keyStr);

            if (keyvalue instanceof JsonObject) {
                JsonParser parsers = new JsonParser();
                JsonElement elements = parsers.parse(String.valueOf(keyvalue));
                JsonObject objs = elements.getAsJsonObject();

                Set<Map.Entry<String, JsonElement>> entriess = objs.entrySet();
                for (Map.Entry<String, JsonElement> entrie : entriess) {
                    list.add(section + "_" + entry.getKey().concat(".").concat(entrie.getKey()));
                }
            } else {
                list.add(section + "_" + entry.getKey());
            }
        }
    }

    public static void extractKeysGHTraffic(String section, String json2, Set<String> list) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json2);
        JsonObject obj = element.getAsJsonObject(); //since you know it's a JsonObject

        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();//will return members of your object

        for (Map.Entry<String, JsonElement> entry : entries) {

            String keyStr = entry.getKey();
            Object keyvalue = obj.get(keyStr);

            if (!keyStr.equals("Authorization")) {
                if (keyvalue instanceof JsonObject) {
                    JsonParser parsers = new JsonParser();
                    JsonElement elements = parsers.parse(String.valueOf(keyvalue));
                    JsonObject objs = elements.getAsJsonObject();

                    Set<Map.Entry<String, JsonElement>> entriess = objs.entrySet();
                    for (Map.Entry<String, JsonElement> entrie : entriess) {
                        list.add(section + "_" + entry.getKey().concat(".").concat(entrie.getKey()));
                    }
                } else {
                    list.add(section + "_" + entry.getKey());
                }
            }
        }
    }

    public static void extractKeysGHTrafficResponseBody(String section, String jsonString, Set<String> list) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonString);
        JsonObject obj = element.getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();

        for (Map.Entry<String, JsonElement> entry : entries) {

            String keyStr = entry.getKey();
            Object keyvalue = obj.get(keyStr);

            String jsonResp = keyvalue.toString();

            if (keyvalue instanceof JsonObject) {
                JsonParser parsers = new JsonParser();
                JsonElement elements = parsers.parse(String.valueOf(keyvalue));
                JsonObject objs = elements.getAsJsonObject();

                Set<Map.Entry<String, JsonElement>> entriess = objs.entrySet();
                for (Map.Entry<String, JsonElement> entrie : entriess) {
                    String keyStr2 = entrie.getKey();
                    Object keyvalue2 = objs.get(keyStr2);

                    String jsonResp1 = keyvalue2.toString();

                    if (keyvalue2 instanceof JsonObject) {
                        JsonParser parsers1 = new JsonParser();
                        JsonElement elements1 = parsers1.parse(String.valueOf(keyvalue2));
                        JsonObject objs1 = elements1.getAsJsonObject();

                        Set<Map.Entry<String, JsonElement>> entriess2 = objs1.entrySet();

                        for (Map.Entry<String, JsonElement> entrie2 : entriess2) {
                            list.add(section + "_" + entry.getKey().concat(".").concat(entrie.getKey()).concat(".").concat(entrie2.getKey()));
                        }
                    } else if (jsonResp1.contains("[") && jsonResp1.contains("]")) {
                        jsonResp1 = jsonResp1.replace("[", "");
                        jsonResp1 = jsonResp1.replace("]", "");

                        Object jsonRespObj = jsonResp1;

                        JsonParser parsers1 = new JsonParser();
                        JsonElement elements1 = parsers1.parse(String.valueOf(jsonRespObj));
                        JsonObject objs1 = elements1.getAsJsonObject();

                        Set<Map.Entry<String, JsonElement>> entriess2 = objs1.entrySet();

                        for (Map.Entry<String, JsonElement> entrie2 : entriess2) {
                            list.add(section + "_" + entry.getKey().concat(".").concat(entrie.getKey()).concat(".").concat(entrie2.getKey()));
                        }
                    } else {
                        list.add(section + "_" + entry.getKey().concat(".").concat(entrie.getKey()));
                    }
                }
            } else if (jsonResp.contains("[") && jsonResp.contains("]") && !jsonResp.contains("[]") && !keyStr.equals("body") && !keyStr.equals("labels")) {
                jsonResp = jsonResp.replace("[", "");
                jsonResp = jsonResp.replace("]", "");

                if ((!jsonResp.contains("{") && !jsonResp.contains("}")) || jsonResp.contains("LinkedHashMultimap")) {

                } else {

                    Object jsonRespObj = jsonResp;

                    JsonParser parsers1 = new JsonParser();
                    JsonElement elements1 = parsers1.parse(String.valueOf(jsonRespObj));
                    JsonObject objs1 = elements1.getAsJsonObject();

                    if (objs1 instanceof JsonObject) {

                        Set<Map.Entry<String, JsonElement>> entriess2 = objs1.entrySet();

                        for (Map.Entry<String, JsonElement> entrie2 : entriess2) {
                            list.add(section + "_" + entry.getKey().concat(".").concat(entrie2.getKey()));
                        }
                    }
                }
            } else {
                if(// keyStr.equals("message") ||
                        keyStr.equals("body")
                        || keyStr.equals("labels")
                        || keyStr.equals("labels_url")
                        || keyStr.equals("title")
                        || keyStr.equals("assignee")
                        || keyStr.equals("assignees")
                        || keyStr.equals("closed_by")
                        || keyStr.equals("milestone")){

                } else {
                    list.add(section + "_" + keyStr);
                }
            }
        }
    }
}
