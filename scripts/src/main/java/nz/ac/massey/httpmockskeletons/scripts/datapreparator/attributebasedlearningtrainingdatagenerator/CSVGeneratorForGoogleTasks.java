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
 * for Google Tasks dataset
 *
 * @author thilini bhagya
 */

public class CSVGeneratorForGoogleTasks {
    static org.apache.log4j.Logger LOGGER = Logging.getLogger(CSVGeneratorForGoogleTasks.class);
    static Set<String> requestHeaderKeySet = new TreeSet<String>();
    static Set<String> responseHeaderKeySet = new TreeSet<String>();
    static Set<String> responseBodyKeySet = new TreeSet<String>();

    public static void main(String[] args) throws Exception {
        csvFileGeneratorWithAttributes("src/resources/Google-Features", "src/resources/GoogleTasks-PreprocessedData");
        CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/Google-Features");
    }

    public static void csvFileGeneratorWithAttributes(String writeFileName, String readFileName) throws Exception {
        LOGGER.info("Extracting attributes-values from preprocessed Google Tasks data");
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
                "HasRequestPayload", "HasValidRequestPayload"
        };

        transactionHistoryFeatureKeyArray = new String[]{
                "HasImmediatePreviousTransaction", "HasImmediatePreviousTransactionSucceeded", "ImmediatelyPreviousResponseStatusCode", "ImmediatelyPreviousRequestMethod",
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

        // fetch values from
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {

            List<String> methodList = new LinkedList<String>();
            List<String> codeList = new LinkedList<String>();
            List<String> actionList = new LinkedList<String>();
            List<String> methodStatusCodeList = new LinkedList<String>();

            ArrayList<String> statusTempCodesList = addStatusCodesToList();

            for (List<HTTPTransaction> mm : m.getValue().values()) {

                // string lists to add each types of feature values and loop through to later add them into data[] array
                ArrayList<String> allFeatureValueList = new ArrayList<>();
                ArrayList<String> requestHeaderValueList = new ArrayList<>();
                ArrayList<String> responseHeaderValueList = new ArrayList<>();
                ArrayList<String> responseBodyValueList = new ArrayList<>();
                ArrayList<String> statusCodeValueList = new ArrayList<>();
                ArrayList<String> urlCoreTokenValueList = new ArrayList<>();

                methodList.add(mm.get(0).getMethod());
                codeList.add(mm.get(0).getCode());
                actionList.add(mm.get(0).getURL());
                methodStatusCodeList.add(mm.get(0).getMethod() + "-" + mm.get(0).getCode());

                for (String value : requestHeaderKeyList) {
                    String key = value.replace("RequestHeader_", "");
                    if (key.equals("HasAuthorisationToken")) {
                        requestHeaderValueList.add(String.valueOf(HasAuthorizationToken(mm)));
                    } else if (key.equals("Authorization")) {

                    } else {
                        requestHeaderValueList.add(String.valueOf(RequestHeaderGoogle(mm, key)));
                    }
                }

                for (String value : responseHeaderKeyList) {
                    String key = value.replace("ResponseHeader_", "");
                    responseHeaderValueList.add(String.valueOf(ResponseHeaderGoogle(mm, key)));
                }

                for (String value : responseBodyKeyList) {
                    String key = value.replace("ResponseBody_", "");
                    if (StringUtils.countMatches(key, ".") == 0) {
                        responseBodyValueList.add(String.valueOf(ResponseBodyGoogle(mm, key, null, null)));
                    } else if (StringUtils.countMatches(key, ".") == 1) {
                        responseBodyValueList.add(String.valueOf(ResponseBodyGoogle(mm, key.split("\\.")[0], key.split("\\.")[1], null)));
                    } else if (StringUtils.countMatches(key, ".") == 2) {
                        responseBodyValueList.add(String.valueOf(ResponseBodyGoogle(mm, key.split("\\.")[0], key.split("\\.")[1], key.split("\\.")[2])));
                    }
                }

                for (String statusCode : statusTempCodesList) {
                    statusCodeValueList.add(String.valueOf(HasStatusCodeOccurredBefore(codeList, statusCode)));
                }

                String[] genericValueArr;
                String[] transactionHistoryFeatureValueArr;

                genericValueArr = new String[]{
                        String.valueOf(mm.get(0).getMethod()),
                        String.valueOf(mm.get(0).getCode()),
                        String.valueOf(URITokeniser.GetURLScheme(mm.get(0).getURL())),
                        String.valueOf(URITokeniser.GetUriHost(mm.get(0).getURL())),
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
                        String.valueOf(HasRequestPayloadGoogle(String.valueOf(mm.get(0).getMethod()), String.valueOf(mm.get(0).getCode()))),
                        String.valueOf(HasValidRequestPayloadGoogle(String.valueOf(mm.get(0).getMethod()), String.valueOf(mm.get(0).getCode()))),
                };

                transactionHistoryFeatureValueArr = new String[]{
                        String.valueOf(HasImmediatePreviousTransaction(codeList)),
                        String.valueOf(hasImmediatelyPreviousTransactionSucceeded(codeList)),
                        String.valueOf(ImmediatelyPreviousStatusCode(codeList)),
                        String.valueOf(ImmediatelyPreviousMethod(methodList)),
                        String.valueOf(HasURLInImmediatelyPreviousTransactionContainsATokenToCreate(codeList, actionList)),
                        String.valueOf(HasURLInImmediatelyPreviousTransactionContainsATokenToRead(codeList, actionList)),
                        String.valueOf(HasURLInImmediatelyPreviousTransactionContainsATokenToUpdate(codeList, actionList)),
                        String.valueOf(HasURLInImmediatelyPreviousTransactionContainsATokenToDelete(codeList, actionList)),
                        String.valueOf(HasSuccessfulCreateOperationOccurredBefore(actionList, methodStatusCodeList)),
                        String.valueOf(HasSuccessfulReadOperationOccurredBefore(actionList, methodStatusCodeList)),
                        String.valueOf(HasSuccessfulUpdateOperationOccurredBefore(actionList, methodStatusCodeList)),
                        String.valueOf(HasSuccessfulDeleteOperationOccurredBefore(actionList, methodStatusCodeList)),
                };

                List<String> genericValueList = Arrays.asList(genericValueArr);
                List<String> transactionHistoryFeatureValueList = Arrays.asList(transactionHistoryFeatureValueArr);

                allFeatureValueList.addAll(genericValueList);
                allFeatureValueList.addAll(urlCoreTokenValueList);
                allFeatureValueList.addAll(requestHeaderValueList);
                allFeatureValueList.addAll(responseHeaderValueList);
                allFeatureValueList.addAll(responseBodyValueList);
                allFeatureValueList.addAll(transactionHistoryFeatureValueList);

                data.add(allFeatureValueList.toArray(new String[allFeatureValueList.size()]));
            }
        }

        // write out records
        writer.writeAll(data);
        // close the writer
        writer.close();
    }

    private static void extractKeySetForResponseHeaders() {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                int index = mm.get(0).getResponseHeaders().indexOf("\t");
                String string_without_version = mm.get(0).getResponseHeaders().substring(index, mm.get(0).getResponseHeaders().length()).replaceAll("\"", "");
                String stuff_with_curlyB = "{" + string_without_version + "}";
                String reg = stuff_with_curlyB.replaceAll("[^\\{\\}\t]+", "\"$0\"");
                String value = reg.replace("\"[\"{", "[{").replace("~", "\":\"").replace("}\"]\"", "}]").replace("\"true\"", "true").replace("\"false\"", "false");
                String values = value.replaceFirst("\t", "");
                String a = values.replaceAll("\t", ",");
                BodyTokeniser.extractKeysGoogle("ResponseHeader", a, responseHeaderKeySet);
            }
        }
    }

    private static void extractKeySetForRequestHeaders() {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                String stuff_with_curlyB = "{" + mm.get(0).getRequestHeaders() + "}";
                String reg = stuff_with_curlyB.replaceAll("[^\\{\\}\t]+", "\"$0\"");
                String value = reg.replace("\"[\"{", "[{").replace("~", "\":\"").replace("}\"]\"", "}]").replace("\"true\"", "true").replace("\"false\"", "false");
                String header = value.replaceAll("\t", ",");
                BodyTokeniser.extractKeysGoogle("RequestHeader", value.replaceAll("\t", ","), requestHeaderKeySet);
            }
        }
    }

    private static void extractKeySetForResponseBody() {
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : HTTPTransaction.transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                String string_without_version = mm.get(0).getResponseBody().replaceAll("\"\"", "\"");

                if (string_without_version.contains("Non-TEXTresponsedata,cannotrecord")) {

                } else {
                    BodyTokeniser.extractKeysGoogle("ResponseBody", string_without_version, responseBodyKeySet);
                }
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
