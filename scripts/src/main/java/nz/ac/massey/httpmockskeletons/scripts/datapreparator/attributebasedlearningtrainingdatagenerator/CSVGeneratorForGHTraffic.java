package nz.ac.massey.httpmockskeletons.scripts.datapreparator.attributebasedlearningtrainingdatagenerator;

import com.opencsv.CSVWriter;
import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction;
import nz.ac.massey.httpmockskeletons.scripts.commons.URITokeniser;
import nz.ac.massey.httpmockskeletons.scripts.commons.Utilities;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.util.*;

import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.hasImmediatelyPreviousTransactionSucceeded;

/**
 * This class generates csv file for GHTraffic dataset
 * related with attributes
 *
 * @author thilinibhagya
 */

public class CSVGeneratorForGHTraffic {

    static org.apache.log4j.Logger LOGGER = Logging.getLogger(CSVGeneratorForGHTraffic.class);

    static Set<String> ResponseBodyKeySet = new TreeSet<String>();
    static Set<String> RequestHeaderKeySet = new TreeSet<String>();
    static Set<String> ResponseHeaderKeySet = new TreeSet<String>();
    static Set<String> RequestBodyKeySet = new TreeSet<String>();

    public static void csvFileGeneratorWithAttributes(String writeFileName, String readFileName) throws Exception {
        LOGGER.info("Extracting attributes-values from preprocessed GHTraffic data");
        String csv = writeFileName + ".csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        List<String[]> data = new ArrayList<String[]>();

        // add feature value lists extracted from json string and add them in below http features lists, then later add to data[] array
        ArrayList<String> ResponseHeaderKeyList = new ArrayList<>();
        ArrayList<String> RequestHeaderKeyList = new ArrayList<>();
        ArrayList<String> ResponseBodyKeyList = new ArrayList<>();
        ArrayList<String> RequestBodyKeyList = new ArrayList<>();

        ArrayList<String> allFeatureKeyList = new ArrayList<>();

        HTTPTransaction.read(readFileName + ".csv");

        extractKeySetForResponseHeaders();
        extractKeySetForRequestHeaders();
        extractKeySetForResponseBody();
        extractKeySetForRequestBody();

        Iterator<String> ResponseHeaderIterator = ResponseHeaderKeySet.iterator();
        Iterator<String> RequestHeaderIterator = RequestHeaderKeySet.iterator();
        Iterator<String> ResponseBodyIterator = ResponseBodyKeySet.iterator();
        Iterator<String> RequestBodyIterator = RequestBodyKeySet.iterator();

        while (ResponseHeaderIterator.hasNext()) {
            ResponseHeaderKeyList.add(ResponseHeaderIterator.next());
        }
        while (RequestHeaderIterator.hasNext()) {
            RequestHeaderKeyList.add(RequestHeaderIterator.next());
        }
        while (ResponseBodyIterator.hasNext()) {
            ResponseBodyKeyList.add(ResponseBodyIterator.next());
        }
        while (RequestBodyIterator.hasNext()) {
            RequestBodyKeyList.add(RequestBodyIterator.next());
        }

        String[] genericHeaderArr;
        String[] transactionHistoryArr;

        // set headers of the csv file
        genericHeaderArr = new String[]{
                "RequestMethod", "ResponseStatusCode",
                "RequestUriSchema", "RequestUriHost",
                "RequestUriPathToken1", "RequestUriPathToken2", "RequestUriPathToken3", "RequestUriPathToken4", "RequestUriPathToken5", "RequestUriPathToken6",
                "RequestUriQueryToken1", "RequestUriQueryToken2", "RequestUriQueryToken3", "RequestUriQueryToken4",
                "RequestUriFragmentToken1", "RequestUriFragmentToken2",
                "HasRequestPayload", "HasValidRequestPayload"
        };

        transactionHistoryArr = new String[]{
                "HasImmediatePreviousTransaction", "HasImmediatePreviousTransactionSucceeded", "ImmediatelyPreviousStatusCode", "ImmediatelyPreviousMethod",
                "HasURLInImmediatelyPreviousTransactionContainsATokenToCreate", "HasURLInImmediatelyPreviousTransactionContainsATokenToRead",
                "HasURLInImmediatelyPreviousTransactionContainsATokenToUpdate", "HasURLInImmediatelyPreviousTransactionContainsATokenToDelete",
                "HasSuccessfulCreateOperationOccurredBefore", "HasSuccessfulReadOperationOccurredBefore", "HasSuccessfulUpdateOperationOccurredBefore", "HasSuccessfulDeleteOperationOccurredBefore"
        };

        List<String> genericHeaderList = Arrays.asList(genericHeaderArr);
        List<String> transactionHistoryList = Arrays.asList(transactionHistoryArr);

        RequestHeaderKeyList.add(0, "HasAuthorisationToken");

        allFeatureKeyList.addAll(genericHeaderList);
        allFeatureKeyList.addAll(RequestHeaderKeyList);
        allFeatureKeyList.addAll(ResponseHeaderKeyList);
        allFeatureKeyList.addAll(RequestBodyKeyList);
        allFeatureKeyList.addAll(ResponseBodyKeyList);
        allFeatureKeyList.addAll(transactionHistoryList);

        data.add(allFeatureKeyList.toArray(new String[allFeatureKeyList.size()]));

        // fetch values
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            List<String> methodList = new LinkedList<String>();
            List<String> codeList = new LinkedList<String>();
            List<String> actionList = new LinkedList<String>();
            List<String> methodStatusCodeList = new LinkedList<String>();
            // static lists (status codes, token types)
            ArrayList<String> statusTempCodesList = addStatusCodesToList();

            for (List<HTTPTransaction> mm : m.getValue().values()) {
                // string lists to add different values and loop through to later add them into data[] array
                ArrayList<String> allFeatureValueList = new ArrayList<>();
                ArrayList<String> requestHeaderValueList = new ArrayList<>();
                ArrayList<String> responseHeaderValueList = new ArrayList<>();
                ArrayList<String> responseBodyValueList = new ArrayList<>();
                ArrayList<String> requestBodyValueList = new ArrayList<>();
                ArrayList<String> statusCodeValueList = new ArrayList<>();

                methodList.add(mm.get(0).getMethod());
                codeList.add(mm.get(0).getCode());
                actionList.add(mm.get(0).getURL());
                methodStatusCodeList.add(mm.get(0).getMethod() + "-" + mm.get(0).getCode());

                for (String value : RequestHeaderKeyList) {
                    String key = value.replace("RequestHeader_", "");
                    if (key.contains("HasAuthorisationToken")) {
                        requestHeaderValueList.add(String.valueOf(Utilities.HasAuthorizationTokenGHTraffic(mm)));
                    } else {
                        requestHeaderValueList.add(String.valueOf(Utilities.RequestHeadersGHTraffic(mm, key)));
                    }
                }

                for (String value : ResponseHeaderKeyList) {
                    String key = value.replace("ResponseHeader_", "");
                    responseHeaderValueList.add(String.valueOf(Utilities.ResponseHeadersGHTraffic(mm, key)));
                }

                for (String value : ResponseBodyKeyList) {
                    String key = value.replace("ResponseBody_", "");
                    if (StringUtils.countMatches(key, ".") == 0) {
                        responseBodyValueList.add(String.valueOf(Utilities.ResponseBodyGHTraffic(mm, key, null, null)));
                    } else if (StringUtils.countMatches(key, ".") == 1) {
                        responseBodyValueList.add(String.valueOf(Utilities.ResponseBodyGHTraffic(mm, key.split("\\.")[0], key.split("\\.")[1], null)));
                    } else if (StringUtils.countMatches(key, ".") == 2) {
                        responseBodyValueList.add(String.valueOf(Utilities.ResponseBodyGHTraffic(mm, key.split("\\.")[0], key.split("\\.")[1], key.split("\\.")[2])));
                    }
                }

                for (String value : RequestBodyKeyList) {
                    String key = value.replace("RequestBody_", "");
                    requestBodyValueList.add(String.valueOf(Utilities.RequestBodyGHTraffic(mm, key)));
                }

                for (String statusCode : statusTempCodesList) {
                    statusCodeValueList.add(String.valueOf(Utilities.HasStatusCodeOccurredBefore(codeList, statusCode)));
                }

                String[] genericValueArr;
                String[] transactionHistoryValueArr;
                genericValueArr = new String[]{
                        String.valueOf(mm.get(0).getMethod()),
                        String.valueOf(mm.get(0).getCode()),

                        String.valueOf(URITokeniser.GetURLScheme(mm.get(0).getURL())) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLScheme(mm.get(0).getURL())),
                        String.valueOf(URITokeniser.GetUriHost(mm.get(0).getURL())) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetUriHost(mm.get(0).getURL())),

                        String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken1")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken1")),
                        String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken2")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken2")),
                        String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken3")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken3")),
                        String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken4")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken4")),
                        String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken5")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken5")),
                        String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken6")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLCoreTokenMap(mm.get(0).getURL()).get("pathToken6")),

                        String.valueOf(URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken1")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken1")),
                        String.valueOf(URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken2")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken2")),
                        String.valueOf(URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken3")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken3")),
                        String.valueOf(URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken4")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetURLQueryTokenMap(mm.get(0).getURL()).get("queryToken4")),

                        String.valueOf(URITokeniser.GetFragmentMap(mm.get(0).getURL()).get("fragmentToken1")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetFragmentMap(mm.get(0).getURL()).get("fragmentToken1")),
                        String.valueOf(URITokeniser.GetFragmentMap(mm.get(0).getURL()).get("fragmentToken2")) == "null" ? "not-exist" : String.valueOf(URITokeniser.GetFragmentMap(mm.get(0).getURL()).get("fragmentToken2")),

                        String.valueOf(Utilities.HasRequestPayloadGHTraffic(mm)),
                        String.valueOf(Utilities.HasValidRequestPayloadGHTraffic(mm))

                };

                transactionHistoryValueArr = new String[]{
                        String.valueOf(Utilities.HasImmediatePreviousTransaction(codeList)),
                        String.valueOf(hasImmediatelyPreviousTransactionSucceeded(codeList)),
                        String.valueOf(Utilities.ImmediatelyPreviousStatusCode(codeList)),
                        String.valueOf(Utilities.ImmediatelyPreviousMethod(methodList)),

                        String.valueOf(Utilities.HasURLInImmediatelyPreviousTransactionContainsATokenToCreateGHTraffic(codeList, actionList)),
                        String.valueOf(Utilities.HasURLInImmediatelyPreviousTransactionContainsATokenToReadGHTraffic(codeList, actionList)),
                        String.valueOf(Utilities.HasURLInImmediatelyPreviousTransactionContainsATokenToUpdateGHTraffic(codeList, actionList)),
                        String.valueOf(Utilities.HasURLInImmediatelyPreviousTransactionContainsATokenToDeleteGHTraffic(codeList, actionList)),

                        String.valueOf(Utilities.hasSuccessfulCreateOperationOccurredBeforeGHTraffic(methodStatusCodeList)),
                        String.valueOf(Utilities.hasSuccessfulReadOperationOccurredBeforeGHTraffic(methodStatusCodeList)),
                        String.valueOf(Utilities.hasSuccessfulUpdateOperationOccurredBeforeGHTraffic(methodStatusCodeList)),
                        String.valueOf(Utilities.hasSuccessfulDeleteOperationOccurredBeforeGHTraffic(methodStatusCodeList))
                };

                List<String> genericValueList = Arrays.asList(genericValueArr);
                List<String> transactionHistoryValueList = Arrays.asList(transactionHistoryValueArr);

                allFeatureValueList.addAll(genericValueList);
                allFeatureValueList.addAll(requestHeaderValueList);
                allFeatureValueList.addAll(responseHeaderValueList);
                allFeatureValueList.addAll(requestBodyValueList);
                allFeatureValueList.addAll(responseBodyValueList);
                allFeatureValueList.addAll(transactionHistoryValueList);

                data.add(allFeatureValueList.toArray(new String[allFeatureValueList.size()]));
            }
        }

        writer.writeAll(data);
        writer.close();
    }

    private static void extractKeySetForResponseHeaders() {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                BodyTokeniser.extractKeysGHTraffic("ResponseHeader", mm.get(0).getResponseHeaders(), ResponseHeaderKeySet);
            }
        }
    }

    private static void extractKeySetForRequestHeaders() {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                BodyTokeniser.extractKeysGHTraffic("RequestHeader", mm.get(0).getRequestHeaders(), RequestHeaderKeySet);
            }
        }
    }

    private static void extractKeySetForResponseBody() {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                String string_without_version = mm.get(0).getResponseBody();
                if (string_without_version.matches("null")) {
                } else {
                    BodyTokeniser.extractKeysGHTrafficResponseBody("ResponseBody", string_without_version, ResponseBodyKeySet);
                }
            }
        }
    }

    private static void extractKeySetForRequestBody() {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                String string_without_version = mm.get(0).getRequestBody();
                if (string_without_version.matches("null") || IsNotToBeAddedToRequestBodyList(string_without_version)) {
                } else {
                    BodyTokeniser.extractKeys("RequestBody", mm.get(0).getRequestBody(), RequestBodyKeySet);
                }
            }
        }
    }

    private static boolean IsNotToBeAddedToRequestBodyList(String key) {
        if (key.contains("title")) {
            return true;
        }

        if (!key.contains(":")) {
            return true;
        }

        return false;
    }

    private static ArrayList<String> addStatusCodesToList() {
        ArrayList<String> list = new ArrayList<>();

        list.add("200");
        list.add("201");
        list.add("204");
        list.add("400");
        list.add("401");
        list.add("404");
        list.add("422");
        list.add("500");
        list.add("503");

        return list;
    }
}
