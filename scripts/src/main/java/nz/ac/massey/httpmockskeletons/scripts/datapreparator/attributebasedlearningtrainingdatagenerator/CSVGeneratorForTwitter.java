package nz.ac.massey.httpmockskeletons.scripts.datapreparator.attributebasedlearningtrainingdatagenerator;

import com.opencsv.CSVWriter;
import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction;
import nz.ac.massey.httpmockskeletons.scripts.commons.URITokeniser;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.util.*;

import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.*;

/**
 * this class extracts attributes-values and generates a csv file
 * for Twitter dataset
 *
 * @author thilini bhagya
 */

public class CSVGeneratorForTwitter {
    static org.apache.log4j.Logger LOGGER = Logging.getLogger(CSVGeneratorForTwitter.class);

    static Set<String> requestHeaderKeySet = new TreeSet<String>();
    static Set<String> responseHeaderKeySet = new TreeSet<String>();
    static Set<String> responseBodyKeySet = new TreeSet<String>();

    public static void csvFileGeneratorWithAttributes(String writeFileName, String readFileName) throws Exception {
        LOGGER.info("Extracting attributes-values from preprocessed Twitter data");
        String csv = writeFileName + ".csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        List<String[]> data = new ArrayList<String[]>();

        // add feature value lists extracted from json string and add them in below http features lists, then later add to data[] array
        ArrayList<String> requestHeaderKeyList = new ArrayList<>();
        ArrayList<String> responseHeaderKeyList = new ArrayList<>();
        ArrayList<String> responseBodyKeyList = new ArrayList<>();

        ArrayList<String> allFeatureKeyList = new ArrayList<>();

        HTTPTransaction.read(readFileName + ".csv");

        extractKeySetForRequestHeaders();
        extractKeySetForResponseHeaders();
        extractKeySetForResponseBody();

        Iterator<String> requestHeaderIterator = requestHeaderKeySet.iterator();
        Iterator<String> responseHeaderIterator = responseHeaderKeySet.iterator();
        Iterator<String> responseBodyIterator = responseBodyKeySet.iterator();

        while (requestHeaderIterator.hasNext()) {
            requestHeaderKeyList.add(requestHeaderIterator.next());
        }
        while (responseHeaderIterator.hasNext()) {
            responseHeaderKeyList.add(responseHeaderIterator.next());
        }
        while (responseBodyIterator.hasNext()) {
            responseBodyKeyList.add(responseBodyIterator.next());
        }

        String[] genericFeatureKeyArray;
        String[] transactionHistoryFeatureKeyArray;

        // set headers of the csv file
        genericFeatureKeyArray = new String[]{
                "RequestMethod", "ResponseStatusCode",
                "RequestUriSchema", "RequestUriHost",
                "RequestUriPathToken1", "RequestUriPathToken2", "RequestUriPathToken3", "RequestUriPathToken4", "RequestUriPathToken5", "RequestUriPathToken6",
                "RequestUriQueryToken1", "RequestUriQueryToken2", "RequestUriQueryToken3", "RequestUriQueryToken4",
                "RequestUriFragmentToken1", "RequestUriFragmentToken2",
                "HasRequestPayload", "HasValidRequestPayload",
        };

        transactionHistoryFeatureKeyArray = new String[]{
                "HasImmediatePreviousTransaction", "HasImmediatePreviousTransactionSucceeded", "ImmediatelyPreviousStatusCode", "ImmediatelyPreviousMethod",
                "HasURLInImmediatelyPreviousTransactionContainsATokenToCreate", "HasURLInImmediatelyPreviousTransactionContainsATokenToRead",
                "HasURLInImmediatelyPreviousTransactionContainsATokenToUpdate", "HasURLInImmediatelyPreviousTransactionContainsATokenToDelete",
                "HasSuccessfulCreateOperationOccurredBefore", "HasSuccessfulReadOperationOccurredBefore", "HasSuccessfulUpdateOperationOccurredBefore", "HasSuccessfulDeleteOperationOccurredBefore"
        };

        List<String> genericFeatureKeyList = Arrays.asList(genericFeatureKeyArray);
        List<String> transactionHistoryFeatureKeyList = Arrays.asList(transactionHistoryFeatureKeyArray);

        requestHeaderKeyList.add(0, "HasAuthorisationToken");

        allFeatureKeyList.addAll(genericFeatureKeyList);
        allFeatureKeyList.addAll(requestHeaderKeyList);
        allFeatureKeyList.addAll(responseHeaderKeyList);
        allFeatureKeyList.addAll(responseBodyKeyList);
        allFeatureKeyList.addAll(transactionHistoryFeatureKeyList);

        data.add(allFeatureKeyList.toArray(new String[allFeatureKeyList.size()]));

        // fetch values
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            List<String> methodList = new LinkedList<String>();
            List<String> codeList = new LinkedList<String>();
            List<String> actionList = new LinkedList<String>();
            List<String> actionCodeList = new LinkedList<String>();

            // static lists (status codes, token types)
            ArrayList<String> statusTempCodesList = addStatusCodesToList();

            for (List<HTTPTransaction> mm : m.getValue().values()) {

                // string lists to add different values and loop through to later add them into data[] array
                ArrayList<String> allFeatureValueList = new ArrayList<>();
                ArrayList<String> requestHeaderValueList = new ArrayList<>();
                ArrayList<String> responseHeaderValueList = new ArrayList<>();
                ArrayList<String> responseBodyValueList = new ArrayList<>();
                ArrayList<String> statusCodeValueList = new ArrayList<>();

                methodList.add(mm.get(0).getMethod());
                codeList.add(mm.get(0).getCode());
                actionList.add(mm.get(0).getURL());
                actionCodeList.add(mm.get(0).getURL() + "-" + mm.get(0).getCode());

                for (String value : requestHeaderKeyList) {
                    String key = value.replace("RequestHeader_", "");
                    if (key.contains("HasAuthorisationToken")) {
                        requestHeaderValueList.add(String.valueOf(hasAuthorizationToken(mm)));
                    } else {
                        requestHeaderValueList.add(String.valueOf(requestHeadersTwitter(mm, key)));
                    }
                }

                for (String value : responseHeaderKeyList) {
                    String key = value.replace("ResponseHeader_", "");
                    responseHeaderValueList.add(String.valueOf(responseHeadersTwitter(mm, key)));
                }

                for (String value : responseBodyKeyList) {
                    String key = value.replace("ResponseBody_", "");
                    if (StringUtils.countMatches(key, ".") == 0) {
                        responseBodyValueList.add(String.valueOf(responseBodyTwitter(mm, key)));
                    } else if (StringUtils.countMatches(key, ".") == 1) {
                        responseBodyValueList.add(String.valueOf(responseBodyInsideTwitter(mm, key.split("\\.")[0], key.split("\\.")[1])));
                    }
                }

//                for (String statusCode : statusTempCodesList) {
//                    statusCodeValueList.add(String.valueOf(HasStatusCodeOccurredBefore(codeList, statusCode)));
//                }

                String[] genericValueArr;
                String[] transactionHistoryValueArr;

                genericValueArr = new String[]{
                        String.valueOf(mm.get(0).getMethod()),
                        String.valueOf(mm.get(0).getCode()),
                        String.valueOf(URITokeniser.getURLScheme(mm.get(0).getURL())) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLScheme(mm.get(0).getURL())),
                        String.valueOf(URITokeniser.getUriHost(mm.get(0).getURL())) == "null" ? "not-exist" : String.valueOf(URITokeniser.getUriHost(mm.get(0).getURL())),
                        String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken1")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken1")),
                        String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken2")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken2")),
                        String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken3")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken3")),
                        String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken4")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken4")),
                        String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken5")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken5")),
                        String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken6")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLCoreTokenMap(mm.get(0).getURL()).get("pathToken6")),
                        String.valueOf(URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken1")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken1")),
                        String.valueOf(URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken2")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken2")),
                        String.valueOf(URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken3")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken3")),
                        String.valueOf(URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken4")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getURLQueryTokenMap(mm.get(0).getURL()).get("queryToken4")),
                        String.valueOf(URITokeniser.getFragmentMap(mm.get(0).getURL()).get("fragmentToken1")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getFragmentMap(mm.get(0).getURL()).get("fragmentToken1")),
                        String.valueOf(URITokeniser.getFragmentMap(mm.get(0).getURL()).get("fragmentToken2")) == "null" ? "not-exist" : String.valueOf(URITokeniser.getFragmentMap(mm.get(0).getURL()).get("fragmentToken2")),
                        String.valueOf(hasRequestPayload()),
                        String.valueOf(hasValidRequestPayload())
                };

                transactionHistoryValueArr = new String[]{
                        String.valueOf(hasImmediatePreviousTransaction(codeList)),
                        String.valueOf(hasImmediatelyPreviousTransactionSucceeded(codeList)),
                        String.valueOf(immediatelyPreviousStatusCode(codeList)),
                        String.valueOf(immediatelyPreviousMethod(methodList)),
                        String.valueOf(hasURLInImmediatelyPreviousTransactionContainsATokenToCreateTwitter(codeList, actionList)),
                        String.valueOf(hasURLInImmediatelyPreviousTransactionContainsATokenToReadTwitter(codeList, actionList)),
                        String.valueOf(hasURLInImmediatelyPreviousTransactionContainsATokenToUpdateTwitter(codeList, actionList)),
                        String.valueOf(hasURLInImmediatelyPreviousTransactionContainsATokenToDeleteTwitter(codeList, actionList)),
                        String.valueOf(hasSuccessfulCreateOperationOccurredBeforeTwitter(actionList, actionCodeList)),
                        String.valueOf(hasSuccessfulReadOperationOccurredBeforeTwitter(actionList, actionCodeList)),
                        String.valueOf(hasSuccessfulUpdateOperationOccurredBeforeTwitter(actionList, actionCodeList)),
                        String.valueOf(hasSuccessfulDeleteOperationOccurredBeforeTwitter(actionList, actionCodeList))
                };

                List<String> genericValueList = Arrays.asList(genericValueArr);
                List<String> transactionHistoryValueList = Arrays.asList(transactionHistoryValueArr);

                allFeatureValueList.addAll(genericValueList);
                allFeatureValueList.addAll(requestHeaderValueList);
                allFeatureValueList.addAll(responseHeaderValueList);
                allFeatureValueList.addAll(responseBodyValueList);
                allFeatureValueList.addAll(transactionHistoryValueList);
                // allFeatureValueList.addAll(statusCodeValueList);

                data.add(allFeatureValueList.toArray(new String[allFeatureValueList.size()]));
            }
        }

        writer.writeAll(data);
        writer.close();
    }

    private static void extractKeySetForResponseHeaders() {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                int index = mm.get(0).getResponseHeaders().indexOf("\t");
                String string_without_version = mm.get(0).getResponseHeaders().substring(index + 1, mm.get(0).getResponseHeaders().length());
                String stuff_with_curlyB = "{" + string_without_version + "}";
                stuff_with_curlyB = stuff_with_curlyB.replace("\"", "\'");
                String reg = stuff_with_curlyB.replaceAll("[^\\{\\}\t]+", "\"$0\"");
                String value = reg.replace("\"[\"{", "[{").replace("~", "\":\"").replace("}\"]\"", "}]").replace("\"true\"", "true").replace("\"false\"", "false");
                BodyTokeniser.extractKeysTwitter("ResponseHeader", value.replaceAll("\t", ","), responseHeaderKeySet);
            }
        }
    }

    private static void extractKeySetForRequestHeaders() {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                String stuff_with_curlyB = "{" + mm.get(0).getRequestHeaders() + "}";
                stuff_with_curlyB = stuff_with_curlyB.replace("\"", "\'");
                String reg = stuff_with_curlyB.replaceAll("[^\\{\\}\t]+", "\"$0\"");
                String value = reg.replace("\"[\"{", "[{").replace("~", "\":\"").replace("}\"]\"", "}]").replace("\"true\"", "true").replace("\"false\"", "false");
                // value=value.replaceAll("\t",",");

                BodyTokeniser.extractKeysTwitter("RequestHeader", value.replaceAll("\t", ","), requestHeaderKeySet);
            }
        }
    }

    private static void extractKeySetForResponseBody() {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                BodyTokeniser.extractKeysTwitter("ResponseBody", mm.get(0).getResponseBody(), responseBodyKeySet);
            }
        }
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
