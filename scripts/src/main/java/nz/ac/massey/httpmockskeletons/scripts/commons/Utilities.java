package nz.ac.massey.httpmockskeletons.scripts.commons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;

/**
 * this class having used by other classes
 *
 * @author thilini bhagya
 */

public class Utilities {

    // COMMON

    public static boolean hasAuthorizationToken(List<HTTPTransaction> mm) {

        return String.valueOf(mm.get(0).getCode()).matches("401") ? false : true;
    }

    public static boolean hasRequestPayload() {
        return false;
    }

    public static boolean hasValidRequestPayload() {
        return false;
    }

    public static boolean hasStatusCodeOccurredBefore(List<String> codeList, String statusCode) {
        return codeList.subList(0, codeList.size() - 1).contains((statusCode)) ? true : false;
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToCreate(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("chat.postMessage")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToRead(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("show")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToUpdate(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("chat.update")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToDelete(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("chat.delete")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasSuccessfulReadOperationOccurredBefore(List<String> action, List<String> methodStatusCodeList) throws URISyntaxException {
        List Urls = action.subList(0, action.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            results.add(URITokeniser.checkURLCoreTokenMapContainsValue((String) value, "chat.postMessage"));
        }

        List methods = methodStatusCodeList.subList(0, methodStatusCodeList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        // System.out.println(methodResults);

        return methodResults.contains("GET-200");
    }

    public static boolean hasSuccessfulUpdateOperationOccurredBefore(List<String> action, List<String> methodStatusCodeList) throws URISyntaxException {
        List Urls = action.subList(0, action.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            results.add(URITokeniser.checkURLCoreTokenMapContainsValue((String) value, "chat.postMessage"));
        }

        List methods = methodStatusCodeList.subList(0, methodStatusCodeList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        return methodResults.contains("PATCH-200");
    }

    public static boolean hasSuccessfulDeleteOperationOccurredBefore(List<String> action, List<String> methodStatusCodeList) throws URISyntaxException {
        List Urls = action.subList(0, action.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            results.add(URITokeniser.checkURLCoreTokenMapContainsValue((String) value, "chat.postMessage"));
        }

        List methods = methodStatusCodeList.subList(0, methodStatusCodeList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        return methodResults.contains("DELETE-204");
    }

    public static boolean hasSuccessfulCreateOperationOccurredBefore(List<String> action, List<String> methodStatusCodeList) throws URISyntaxException {
        List Urls = action.subList(0, action.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            results.add(URITokeniser.checkURLCoreTokenMapContainsValue((String) value, "chat.postMessage"));
        }

        List methods = methodStatusCodeList.subList(0, methodStatusCodeList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        // System.out.println(methodResults);

        return methodResults.contains("POST-200");
    }

    public static boolean hasImmediatePreviousTransaction(List<String> n) {
        return n.subList(0, n.size() - 1).size() != 0 ? true : false;
    }

    public static String immediatelyPreviousStatusCode(List<String> code) {
        String immediatelyPreviousStatusCode = "not-exist";

        if (code.subList(0, code.size() - 1).size() != 0) {

            immediatelyPreviousStatusCode = code.subList(0, code.size() - 1).get(code.subList(0, code.size() - 1).size() - 1);
        } else {
            immediatelyPreviousStatusCode = "not-exist";
        }

        return immediatelyPreviousStatusCode;
    }

    public static boolean hasImmediatelyPreviousTransactionSucceeded(List<String> code) {
        boolean immediatelyPreviousStatusCode = false;

        if (code.subList(0, code.size() - 1).size() != 0) {
            String previousTransactionCode = code.subList(0, code.size() - 1).get(code.subList(0, code.size() - 1).size() - 1);
            immediatelyPreviousStatusCode = previousTransactionCode.contains("200") || previousTransactionCode.contains("204") || previousTransactionCode.contains("201");
        }

        return immediatelyPreviousStatusCode;
    }

    public static String immediatelyPreviousMethod(List<String> n) {
        String method = "not-exist";

        if (n.subList(0, n.size() - 1).size() != 0) {
            method = n.subList(0, n.size() - 1).get(n.subList(0, n.size() - 1).size() - 1);
        } else {
            method = "not-exist";
        }

        return method;
    }

    // GOOGLE

    public static String requestHeaderGoogle(List<HTTPTransaction> mm, String value) {
        String header = "not-exist";
        //convert to json style
        String stuff_with_curlyB = "{" + mm.get(0).getRequestHeaders() + "}";
        String reg = stuff_with_curlyB.replaceAll("[^\\{\\}\t]+", "\"$0\"");
        String val = reg.replace("\"[\"{", "[{").replace("~", "\":\"").replace("}\"]\"", "}]").replace("\"true\"", "true").replace("\"false\"", "false");
        //System.out.println(value);
        String a = val.replaceAll("\t", ",");
        JSONObject jsonObject = new JSONObject(a);
        if (jsonObject.has(value)) {
            header = jsonObject.get(value).toString();
            //enclose entire field which contains commas to convert into .arff without any error

        }

        return header;
    }

    public static String responseHeaderGoogle(List<HTTPTransaction> mm, String value) {
        String header = "not-exist";
        //convert to json style
        int index = mm.get(0).getResponseHeaders().indexOf("\t");
        String string_without_version = mm.get(0).getResponseHeaders().substring(index, mm.get(0).getResponseHeaders().length()).replaceAll("\"", "");
        String stuff_with_curlyB = "{" + string_without_version + "}";
        String reg = stuff_with_curlyB.replaceAll("[^\\{\\}\t]+", "\"$0\"");
        String val = reg.replace("\"[\"{", "[{").replace("~", "\":\"").replace("}\"]\"", "}]").replace("\"true\"", "true").replace("\"false\"", "false");
        String values = val.replaceFirst("\t", "");

        String a = values.replaceAll("\t", ",");
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(a);
        JsonObject obj = element.getAsJsonObject();
        if (obj.has(value)) {
            header = (String.valueOf(obj.get(value)).replaceAll("\"", ""));

            if (header.indexOf(",") >= 0) {
                header = "\'" + header + "\'";
            }
        }

        return header;
    }

    public static String responseBodyGoogle(List<HTTPTransaction> mm, String value, String subvalue, String subsubvalue) {
        String body = "not-exist";
        String string_without_version = mm.get(0).getResponseBody().replaceAll("\"\"", "\"");

        if (string_without_version.contains("Non-TEXTresponsedata,cannotrecord")) {
        } else {
            JSONObject jsonObject = new JSONObject(string_without_version);
            if (jsonObject.has(value)) {

                if (subvalue == null && subsubvalue == null) { // if got only one level
                    body = jsonObject.get(value).toString();
                } else if (subsubvalue == null) { // if got two levels
                    JSONObject jsonObject2 = new JSONObject(jsonObject.get(value).toString());
                    if (jsonObject2.has(subvalue)) {
                        body = jsonObject2.get(subvalue).toString();
                    }
                } else { // if got three levels
                    JSONArray jArray1 = jsonObject.getJSONObject(value).getJSONArray(subvalue);
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(String.valueOf(jArray1.get(0)));
                    JsonObject obj = element.getAsJsonObject();
                    if (obj.has(subsubvalue)) {
                        body = obj.get(subsubvalue).toString().replaceAll("\"", "");
                    }
                }
            }
        }
        return body;
    }

    public static boolean hasRequestPayloadGoogle(String method, String statusCode) {
        if ((method.equals("POST") && statusCode.equals("200")) ||
                (method.equals("PATCH") && statusCode.equals("200")) ||
                (method.equals("PATCH") && statusCode.equals("404")) ||
                (method.equals("POST") && statusCode.equals("500")) ||
                (method.equals("PATCH") && statusCode.equals("503"))) {

            return true;
        }

        return false;
    }

    public static boolean hasValidRequestPayloadGoogle(String method, String statusCode) {
        if ((method.equals("POST") && statusCode.equals("200")) ||
                (method.equals("PATCH") && statusCode.equals("200")) ||
                (method.equals("PATCH") && statusCode.equals("404")) ||
                (method.equals("POST") && statusCode.equals("500")) ||
                (method.equals("PATCH") && statusCode.equals("503"))) {

            return true;
        }

        return false;
    }

    // SLACK

    public static String requestHeaderSlack(List<HTTPTransaction> mm, String value) {
        String header = "not-exist";
        //convert to json style
        String stuff_with_curlyB = "{" + mm.get(0).getRequestHeaders() + "}";
        String reg = stuff_with_curlyB.replaceAll("[^\\{\\}\t]+", "\"$0\"");
        String val = reg.replace("\"[\"{", "[{").replace("~", "\":\"").replace("}\"]\"", "}]").replace("\"true\"", "true").replace("\"false\"", "false");
        //System.out.println(value);
        String a = val.replaceAll("\t", ",");
        JSONObject jsonObject = new JSONObject(a);
        if (jsonObject.has(value)) {
            header = jsonObject.get(value).toString();
            //enclose entire field which contains commas to convert into .arff without any error
            if (header.indexOf(",") >= 0) {
                header = "\'" + header + "\'";
            }
        }

        return header;
    }

    public static String responseHeaderSlack(List<HTTPTransaction> mm, String value) {
        String header = "not-exist";
        //convert to json style
        int index = mm.get(0).getResponseHeaders().indexOf("\t");
        String string_without_version = mm.get(0).getResponseHeaders().substring(index + 1, mm.get(0).getResponseHeaders().length());
        String stuff_with_curlyB = "{" + string_without_version + "}";
        String reg = stuff_with_curlyB.replaceAll("[^\\{\\}\t]+", "\"$0\"");
        String val = reg.replace("\"[\"{", "[{").replace("~", "\":\"").replace("}\"]\"", "}]").replace("\"true\"", "true").replace("\"false\"", "false");
        String a = val.replaceAll("\t", ",");
        JSONObject jsonObject = new JSONObject(a);

        if (jsonObject.has(value)) {
            header = jsonObject.get(value).toString();
            //enclose entire field which contains commas to convert into .arff without any error
            if (header.indexOf(",") >= 0) {
                header = "\'" + header + "\'";
            }


        }

        return header;
    }

    public static String responseBodySlack(List<HTTPTransaction> mm, String value, String subvalue, String subsubvalue) {
        String body = "not-exist";
        JSONObject jsonObject = new JSONObject(mm.get(0).getResponseBody());

        if (jsonObject.has(value)) {

            if (subvalue == null && subsubvalue == null) { // if got only one level
                body = jsonObject.get(value).toString();
            } else if (subsubvalue == null) { // if got two levels
                JSONObject jsonObject2 = new JSONObject(jsonObject.get(value).toString());
                if (jsonObject2.has(subvalue)) {
                    body = jsonObject2.get(subvalue).toString();
                }
            } else { // if got three levels
                JSONObject jsonObject2 = new JSONObject(jsonObject.get(value).toString());
                if (jsonObject2.has(subvalue)) {

                    JSONObject jsonObject3 = new JSONObject(jsonObject2.get(subvalue).toString());
                    if (jsonObject3.has(subsubvalue)) {
                        body = jsonObject3.get(subsubvalue).toString();
                    }
                }
            }
        }

        return body;
    }

    public static boolean hasSuccessfulCreateOperationOccurredBeforeSlack(List<String> urlList, List<String> bodyList) throws URISyntaxException {

        List Urls = urlList.subList(0, urlList.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            results.add(URITokeniser.checkURLCoreTokenMapContainsValue((String) value, "chat.postMessage"));
        }

        if (bodyList.subList(0, bodyList.size() - 1).size() != 0) {
            String previousBody = bodyList.subList(0, bodyList.size() - 1).get(bodyList.subList(0, bodyList.size() - 1).size() - 1);
            JSONObject jsonObject = new JSONObject(previousBody);

            if (jsonObject.has("ok")) {
                if (jsonObject.get("ok").toString().toUpperCase().equals("TRUE")) {
                    return true;
                }
            }
        }

        return results.contains(true);
    }

    public static boolean hasSuccessfulDeleteOperationOccurredBeforeSlack(List<String> action, List<String> methodList) throws URISyntaxException {
        List Urls = action.subList(0, action.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            results.add(URITokeniser.checkURLCoreTokenMapContainsValue((String) value, "chat.delete"));
        }

        List methods = methodList.subList(0, methodList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        return results.contains(true) || methodResults.contains("DELETE");
    }

    public static boolean hasSuccessfulReadOperationOccurredBeforeSlack(List<String> action, List<String> methodList) throws URISyntaxException {
        List Urls = action.subList(0, action.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            results.add(URITokeniser.checkURLCoreTokenMapContainsValue((String) value, "show"));
        }

        List methods = methodList.subList(0, methodList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        return results.contains(true) || methodResults.contains("GET");
    }

    public static boolean hasSuccessfulUpdateOperationOccurredBeforeSlack(List<String> action, List<String> methodList) throws URISyntaxException {
        List Urls = action.subList(0, action.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            results.add(URITokeniser.checkURLCoreTokenMapContainsValue((String) value, "chat.update"));
        }

        List methods = methodList.subList(0, methodList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        return results.contains(true) || methodResults.contains("PATCH");
    }

    public static boolean hasImmediatelyPreviousTransactionSucceededSlack(List<String> body) {

        if (body.subList(0, body.size() - 1).size() != 0) {
            String previousBody = body.subList(0, body.size() - 1).get(body.subList(0, body.size() - 1).size() - 1);
            JSONObject jsonObject = new JSONObject(previousBody);

            if (jsonObject.has("ok")) {
                if (jsonObject.get("ok").toString().toUpperCase().equals("TRUE")) {
                    return true;
                }
            }
        }

        return false;
    }

    // TWITTER

    public static String requestHeadersTwitter(List<HTTPTransaction> mm, String value) {
        String header = "not-exist";
        //convert to json style
        String stuff_with_curlyB = "{" + mm.get(0).getRequestHeaders() + "}";
        String myVal = stuff_with_curlyB.replace("\"", "\'");
        String reg = myVal.replaceAll("[^\\{\\}\t]+", "\"$0\"");


        String vals = reg.replace("=\"", "=").replace("\",", ",");

        String val = vals.replace("\"[\"{", "[{").replace("~", "\":\"").replace("}\"]\"", "}]").replace("\"true\"", "true").replace("\"false\"", "false");

        String a = val.replaceAll("\t", ",");

        JSONObject jsonObject = new JSONObject(a);
        if (jsonObject.has(value)) {
            header = jsonObject.get(value).toString();
            //enclose entire field which contains commas to convert into .arff without any error
            if (header.indexOf(",") >= 0) {
                header = "\'" + header + "\'";
            }
        }

        return header;
    }

    public static String responseHeadersTwitter(List<HTTPTransaction> mm, String value) {
        String header = "not-exist";
        //convert to json style

        int index = mm.get(0).getResponseHeaders().indexOf("\t");
        String string_without_version = mm.get(0).getResponseHeaders().substring(index + 1, mm.get(0).getResponseHeaders().length());
        String stuff_with_curlyB = "{" + string_without_version + "}";
        String myVal = stuff_with_curlyB.replace("\"", "\'");
        String reg = myVal.replaceAll("[^\\{\\}\t]+", "\"$0\"");
        String val = reg.replace("\"[\"{", "[{").replace("~", "\":\"").replace("}\"]\"", "}]").replace("\"true\"", "true").replace("\"false\"", "false");
        String a = val.replaceAll("\t", ",");

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(a);
        JsonObject obj = element.getAsJsonObject();

        if (obj.has(value)) {
            header = (String.valueOf(obj.get(value)).replaceAll("\"", ""));
            if (header.contains("application/json") && header.contains(" ")) {
                header = header.replace(" ", "");
            }
            if (header.indexOf(",") >= 0) {
                header = "\'" + header + "\'";
            }
        }

        return header;
    }

    public static String responseBodyTwitter(List<HTTPTransaction> mm, String value) {
        String body = "not-exist";

        JSONObject jsonObject = new JSONObject(mm.get(0).getResponseBody());
        if (jsonObject.has(value)) {
            body = jsonObject.get(value).toString();
        }

        return body;
    }

    public static String responseBodyInsideTwitter(List<HTTPTransaction> mm, String value, String subvalue) {
        String body = "not-exist";

        if (subvalue.equals("code") || subvalue.equals("message")) {
            return responseBodyInsideArrayTwitter(mm, value, subvalue);
        } else {
            JSONObject jsonObject = new JSONObject(mm.get(0).getResponseBody());
            if (jsonObject.has(value)) {
                JSONObject jsonObject2 = new JSONObject(jsonObject.get(value).toString());
                if (jsonObject2.has(subvalue)) {
                    body = jsonObject2.get(subvalue).toString();
                }
            }
        }

        return body;
    }

    public static String responseBodyInsideArrayTwitter(List<HTTPTransaction> mm, String value, String subvalue) {
        String body = "not-exist";

        JSONObject jsonObject = new JSONObject(mm.get(0).getResponseBody());

        if (jsonObject.has(value)) {
            JSONArray array = jsonObject.getJSONArray(value);
            JSONObject firstSport = array.getJSONObject(0);

            if (firstSport.has(subvalue)) {
                body = firstSport.get(subvalue).toString();
            }
        }

        return body;
    }

    public static boolean hasSuccessfulCreateOperationOccurredBeforeTwitter(List<String> actionList, List<String> actionCodeList) throws URISyntaxException {
        List actionCodes = actionCodeList.subList(0, actionCodeList.size() - 1);
        List actionCodeResults = new LinkedList<String>();
        for (Object actionCode : actionCodes) {
            String action = actionCode.toString().split("-")[0].toString();
            String code = actionCode.toString().split("-")[1].toString();

            actionCodeResults.add(action.contains("update.json") && code.contains("200"));
        }

        return actionCodeResults.contains(true);
    }

    public static boolean hasSuccessfulDeleteOperationOccurredBeforeTwitter(List<String> action, List<String> actionCodeList) throws URISyntaxException {
        List Urls = actionCodeList.subList(0, action.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            String url = value.toString().split("-")[0];
            String code = value.toString().split("-")[1];

            results.add(url.contains("destroy") && code.contains("200"));
        }

        return results.contains(true);
    }

    public static boolean hasSuccessfulReadOperationOccurredBeforeTwitter(List<String> action, List<String> actionCodeList) throws URISyntaxException {
        List Urls = actionCodeList.subList(0, action.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            String url = value.toString().split("-")[0];
            String code = value.toString().split("-")[1];

            results.add(url.contains("show.json") && code.contains("200"));
        }

        return results.contains(true);
    }

    public static boolean hasSuccessfulUpdateOperationOccurredBeforeTwitter(List<String> action, List<String> codeList) throws URISyntaxException {
        List Urls = action.subList(0, action.size() - 1);
        List results = new LinkedList<String>();
        for (Object value : Urls) {
            results.add(URITokeniser.checkURLCoreTokenMapContainsValue((String) value, "modify"));
        }
        return results.contains(true);
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToCreateTwitter(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("update")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToReadTwitter(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("show")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToUpdateTwitter(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("modify")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToDeleteTwitter(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("destroy")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    // GHTRAFFIC

    public static String requestHeadersGHTraffic(List<HTTPTransaction> mm, String value) {
        String header = "not-exist";
        JSONObject jsonObject = new JSONObject(mm.get(0).getRequestHeaders());
        if (jsonObject.has(value)) {
            header = (String.valueOf(jsonObject.get(value)).replaceAll("\"", ""));

            if (header.indexOf(",") >= 0) {
                header = "\'" + header + "\'";
            }

        }
        return header;
    }

    public static String responseHeadersGHTraffic(List<HTTPTransaction> mm, String value) {
        String header = "not-exist";

        JSONObject jsonObject = new JSONObject(mm.get(0).getResponseHeaders());
        if (jsonObject.has(value)) {
            header = (String.valueOf(jsonObject.get(value)).replaceAll("\"", ""));

            if (header.indexOf(",") >= 0) {
                header = "\'" + header + "\'";
            }
        }
        return header;
    }

    public static String requestBodyGHTraffic(List<HTTPTransaction> mm, String value) {
        String body = "not-exist";
        String string_without_version = mm.get(0).getRequestBody();
        if (string_without_version.matches("null")) {
        } else {
            if (isJSONValid(string_without_version)) {
                JSONObject jsonObject = new JSONObject(mm.get(0).getRequestBody());

                if (jsonObject.has(value)) {

                    body = (String) jsonObject.get(value);

                    if (body.contains("open")) {
                        // System.out.println(body);
                    }
                }
            }
        }

        return body;
    }

    public static String responseBodyGHTraffic(List<HTTPTransaction> mm, String value, String subvalue, String subsubvalue) {
        String body = "not-exist";

        String string_without_version = mm.get(0).getResponseBody();

        if (string_without_version.matches("null")) {

        } else {
            if (subvalue == null && subsubvalue == null) { // body
                JSONObject jsonObject = new JSONObject(mm.get(0).getResponseBody());
                if (jsonObject.has(value)) {
                    if (jsonObject.get(value).toString().contains("Invalid request.\\n\\nFor 'links/0/schema', nil is not an object.")) {
                        body = "Invalid request";
                    } else {
                        body = jsonObject.get(value).toString();
                    }
                }
            } else if (subsubvalue == null) { // bodyInside
                JSONObject jsonObject = new JSONObject(mm.get(0).getResponseBody());
                if (jsonObject.has(value)) {
                    String a = jsonObject.get(value).toString();

                    if (a.matches("null")) {
                    } else if (jsonObject.has(value)) {

                        if (a.contains("[") && a.contains("]")) { // bodyInsideArray
                            if (!jsonObject.get(value).toString().contains("[]")) {
                                JSONArray array = jsonObject.getJSONArray(value);
                                JSONObject firstSport = array.getJSONObject(0);
                                if (firstSport.has(subvalue)) {
                                    body = firstSport.get(subvalue).toString();
                                }
                            }
                        } else { // bodyInside
                            JSONObject jsonObject2 = new JSONObject(jsonObject.get(value).toString());
                            if (jsonObject2.has(subvalue)) {
                                body = jsonObject2.get(subvalue).toString();
                            }
                        }
                    }
                }
            } else { // bodyInsideInside
                JSONObject jsonObject = new JSONObject(mm.get(0).getResponseBody());

                if (jsonObject.has(value)) {
                    String a = jsonObject.get(value).toString();
                    if (a.matches("null")) {
                    } else {
                        JSONObject jsonObject2 = new JSONObject(jsonObject.get(value).toString());
                        if (jsonObject2.has(subvalue)) {

                            String ab = jsonObject.get(value).toString();
                            if (ab.matches("null")) {

                            } else {
                                JSONObject jsonObject3 = new JSONObject(jsonObject2.get(subvalue).toString());
                                if (jsonObject3.has(subsubvalue)) {
                                    body = jsonObject3.get(subsubvalue).toString();
                                }
                            }
                        }
                    }
                }
            }
        }

        return body;
    }

    public static String hasRequestPayloadGHTraffic(List<HTTPTransaction> mm) {
        String hasPayload;
        if (((String.valueOf(mm.get(0).getMethod()).matches("POST")) ||
                (String.valueOf(mm.get(0).getMethod()).matches("PATCH"))) &&
                ((String.valueOf(mm.get(0).getCode()).matches("201"))
                        || ((String.valueOf(mm.get(0).getCode()).matches("404")))
                        || ((String.valueOf(mm.get(0).getCode()).matches("200")))
                        || ((String.valueOf(mm.get(0).getCode()).matches("400")))
                        || ((String.valueOf(mm.get(0).getCode()).matches("401"))))) {
            hasPayload = "true";

        } else if (((String.valueOf(mm.get(0).getMethod()).matches("POST"))
                || (String.valueOf(mm.get(0).getMethod()).matches("PATCH")))
                && ((String.valueOf(mm.get(0).getCode()).matches("422")))) {
            hasPayload = "false";

        } else {
            hasPayload = "false";
        }
        return hasPayload;
    }

    public static String hasValidRequestPayloadGHTraffic(List<HTTPTransaction> mm) {
        String hasValidJson = "false";
        if (hasRequestPayloadGHTraffic(mm) == "true") {
            if (((String.valueOf(mm.get(0).getMethod()).matches("POST"))
                    || (String.valueOf(mm.get(0).getMethod()).matches("PATCH"))) && String.valueOf(mm.get(0).getCode()).matches("400")) {
                hasValidJson = "false";
            } else if (((String.valueOf(mm.get(0).getMethod()).matches("POST"))
                    || (String.valueOf(mm.get(0).getMethod()).matches("PATCH"))) && ((String.valueOf(mm.get(0).getCode()).matches("201"))
                    || (String.valueOf(mm.get(0).getCode()).matches("404"))
                    || (String.valueOf(mm.get(0).getCode()).matches("200")))) {
                hasValidJson = "true";
            }

        } else if (hasRequestPayloadGHTraffic(mm) == "false") {
            hasValidJson = "false";
        } else {
            hasValidJson = "false";
        }
        return hasValidJson;
    }

    public static boolean hasSuccessfulCreateOperationOccurredBeforeGHTraffic(List<String> methodStatusCodeList) throws URISyntaxException {

        List results = new LinkedList<String>();
        List methods = methodStatusCodeList.subList(0, methodStatusCodeList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        // System.out.println(methodResults);

        return methodResults.contains("POST-201");
    }

    public static boolean hasSuccessfulDeleteOperationOccurredBeforeGHTraffic(List<String> methodStatusCodeList) throws URISyntaxException {
        List results = new LinkedList<String>();
        List methods = methodStatusCodeList.subList(0, methodStatusCodeList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        // System.out.println(methodResults);

        return methodResults.contains("DELETE-204");
    }

    public static boolean hasSuccessfulReadOperationOccurredBeforeGHTraffic(List<String> methodStatusCodeList) throws URISyntaxException {
        List results = new LinkedList<String>();
        List methods = methodStatusCodeList.subList(0, methodStatusCodeList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        // System.out.println(methodResults);

        return methodResults.contains("GET-200") || methodResults.contains("HEAD-200");
    }

    public static boolean hasSuccessfulUpdateOperationOccurredBeforeGHTraffic(List<String> methodStatusCodeList) throws URISyntaxException {
        List results = new LinkedList<String>();
        List methods = methodStatusCodeList.subList(0, methodStatusCodeList.size() - 1);
        List methodResults = new LinkedList<String>();
        for (Object method : methods) {
            methodResults.add(method);
        }

        // System.out.println(methodResults);

        return methodResults.contains("PATCH-200") || methodResults.contains("PUT-204");
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToCreateGHTraffic(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {
            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("update")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToReadGHTraffic(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("show")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToUpdateGHTraffic(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("modify")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasURLInImmediatelyPreviousTransactionContainsATokenToDeleteGHTraffic(List<String> code, List<String> action) {
        boolean hasImmediatePreviousTransaction = false;

        if (code.subList(0, code.size() - 1).size() != 0) {


            if (((action.subList(0, action.size() - 1).get(action.subList(0, action.size() - 1).size() - 1).contains("destroy")))) {
                hasImmediatePreviousTransaction = true;
            }
        } else {
            hasImmediatePreviousTransaction = false;
        }

        return hasImmediatePreviousTransaction;
    }

    public static boolean hasAuthorizationTokenGHTraffic(List<HTTPTransaction> mm) {

        return String.valueOf(mm.get(0).getCode()).matches("401")
                || (String.valueOf(mm.get(0).getMethod()).matches("POST")
                && (String.valueOf(mm.get(0).getCode()).matches("404"))) ? false : true;
    }

    // UTILS

    public static boolean isJSONValid(String jsonText) {
        try {
            new JSONObject(jsonText);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(jsonText);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static int[] readIndices(String datasetInput) {

        int[] indices;

        switch (datasetInput) {
            case "googletasks":
                indices = new int[]{1, 9, 20, 24, 25, 27, 26, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49};
                return indices;
            case "slack":
                indices = new int[]{12, 13, 20, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59};
                return indices;
            case "ghtraffic":
                indices = new int[]{1, 8, 20, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150};
                return indices;
            case "twitter":
                indices = new int[]{1, 7, 10, 20, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111};
                return indices;
            default: {
            }
        }

        return null;
    }

    public static String getJsonValueByKey(String key, String dataSetType) throws Exception {
        org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();

        switch (dataSetType) {
            case "googletasks":
                jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArrayGoogleTasks();
                break;
            case "slack":
                jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArraySlack();
                break;
            case "twitter":
                jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArrayTwitter();
                break;
            case "ghtraffic":
                jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArrayGHTraffic();

                break;
        }

        String value = jsonObject.get(key) == null ? null : jsonObject.get(key).toString();

        if (value.indexOf(",") >= 0) {
            value = "\'" + value + "\'";
        }

        return value;
    }

    public static String getJsonKeyByValue(String value, org.json.simple.JSONObject jsonObject){
        value = value.contains("\'")? value.replace("\'","") : value;

        for (Object key: jsonObject.keySet()){
            if(jsonObject.get(key).toString().equals(value)){
                return key.toString();
            }
        }

        return value;
    }

    public static List<OWLClass> getOWLClasses(String owlFileName, String responseType) throws OWLOntologyCreationException {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File("src/resources/" + owlFileName + ".owl");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);

        List<OWLClass> owlClassResponseHeaderList = new ArrayList<>();
        List<OWLClass> owlClassResponseStatusCodeList = new ArrayList<>();
        List<OWLClass> owlClassResponseBodyList = new ArrayList<>();

        Set<OWLClass> owlClassSet = ontology.getClassesInSignature();

        for(OWLClass owlClass : owlClassSet){
            String response = owlClass.toString().split("#")[1].split("_")[0];
            if(response.equals("ResponseHeader")){
                owlClassResponseHeaderList.add(owlClass);
            } else if (response.equals("ResponseStatusCode")){
                owlClassResponseStatusCodeList.add(owlClass);
            } else if (response.equals("ResponseBody")){
                owlClassResponseBodyList.add(owlClass);
            }
        }

        switch (responseType){
            case "ResponseHeader" :
                return owlClassResponseHeaderList;
            case "ResponseStatusCode" :
                return owlClassResponseStatusCodeList;
            case "ResponseBody" :
                return owlClassResponseBodyList;
            default:{
            }
        }

        return null;
    }

}
