package nz.ac.massey.httpmockskeletons.scripts.modellearner.dllearningmodeltrainer;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction;
import nz.ac.massey.httpmockskeletons.scripts.commons.OWLClassLabelsJSONArrays;
import nz.ac.massey.httpmockskeletons.scripts.commons.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.dllearner.core.StringRenderer;
import org.json.simple.JSONObject;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction.transactions;
import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.*;

/**
 * this class retrieve all possible target classes names (with positive and negative examples)
 * that are suitable to learn
 * in each datasets
 *
 * @author thilini bhagya
 */

public class CheckTargetClassesToLearn {
    public static String datasetInput = "";
    static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    static OWLOntology ontology;
    public static String preprocessedData = "";
    public static String keyInput = "";
    public static String valueInput = "";
    public static String ResponseTypeString;
    public static List<String> positiveExamplesList = new ArrayList<>();
    public static List<String> negativeExamplesList = new ArrayList<>();
    public static List<OWLClass> owlClassResponseStatusCodeList = new ArrayList<>();
    public static List<OWLClass> owlClassResponseBodyList = new ArrayList<>();
    public static List<OWLClass> owlClassResponseHeaderList = new ArrayList<>();
    public static List<String> owlClassStringList = new ArrayList<>();

    static org.apache.log4j.Logger LOGGER = Logging.getLogger(CheckTargetClassesToLearn.class);

    public enum ResponseType {
        ResponseHeader,
        ResponseStatusCode,
        ResponseBody
    }

    public CheckTargetClassesToLearn(String datasetInput) throws OWLOntologyCreationException {

        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        ontology = manager.createOntology(IOR);
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

        switch (datasetInput) {
            case "googletasks":
                preprocessedData = "src/resources/googletasks-preprocessed.csv";
                owlClassResponseStatusCodeList = getOWLClasses("googletasks-training", ResponseType.ResponseStatusCode.toString());
                owlClassResponseBodyList = getOWLClasses("googletasks-training", ResponseType.ResponseBody.toString());
                owlClassResponseHeaderList = getOWLClasses("googletasks-training", ResponseType.ResponseHeader.toString());
                break;
            case "slack":
                preprocessedData = "src/resources/sub-slack-preprocessed.csv";
                owlClassResponseStatusCodeList = getOWLClasses("slack-training", ResponseType.ResponseStatusCode.toString());
                owlClassResponseBodyList = getOWLClasses("slack-training", ResponseType.ResponseBody.toString());
                owlClassResponseHeaderList = getOWLClasses("slack-training", ResponseType.ResponseHeader.toString());
                break;
            case "ghtraffic":
                preprocessedData = "src/resources/sub-ghtraffic-preprocessed.csv";
                owlClassResponseStatusCodeList = getOWLClasses("ghtraffic-training", ResponseType.ResponseStatusCode.toString());
                owlClassResponseBodyList = getOWLClasses("ghtraffic-training", ResponseType.ResponseBody.toString());
                owlClassResponseHeaderList = getOWLClasses("ghtraffic-training", ResponseType.ResponseHeader.toString());
                break;
            case "twitter":
                preprocessedData = "src/resources/sub-twitter-preprocessed.csv";
                owlClassResponseStatusCodeList = getOWLClasses("twitter-training", ResponseType.ResponseStatusCode.toString());
                owlClassResponseBodyList = getOWLClasses("twitter-training", ResponseType.ResponseBody.toString());
                owlClassResponseHeaderList = getOWLClasses("twitter-training", ResponseType.ResponseHeader.toString());
                break;
            default: {
            }
        }
    }

    public static void validClassDetails(String dataSetTypeInput) {
        try {
            datasetInput = dataSetTypeInput.toLowerCase();

            CheckTargetClassesToLearn checkValidClasses = new CheckTargetClassesToLearn(datasetInput);

            getTargetClass(owlClassResponseStatusCodeList);
            getTargetClass(owlClassResponseHeaderList);
            getTargetClass(owlClassResponseBodyList);

            for (String owlClass : owlClassStringList) {
                String targetClass = owlClass.split("#")[1].replace(">", "");
                if (targetClass.substring(targetClass.length() - 1).contains("\'")) {
                    targetClass = targetClass.substring(0, targetClass.length() - 1);
                }
                getPosNegClasses(targetClass, datasetInput);
            }

            List<String> finalPositiveExampleList = positiveExamplesList.stream().distinct().collect(Collectors.toList());
            List<String> finalNegativeExampleList = negativeExamplesList.stream().distinct().collect(Collectors.toList());

            List<String> myList = new ArrayList<>(finalNegativeExampleList);
            myList.retainAll(finalPositiveExampleList);

            int positiveExampleCount = 0;
            int negativeExampleCount = 0;

            for (String item : myList) {
                for (String pos : positiveExamplesList) {
                    if(item.equals(pos)){
                        positiveExampleCount ++;
                    }
                }

                for (String pos : negativeExamplesList) {
                    if(item.equals(pos)){
                        negativeExampleCount ++;
                    }
                }

                String itemWithoutSingleQuotes = item.contains("\'") ? item.replace("\'", "") : item;
                if(itemWithoutSingleQuotes.contains("ResponseBody_assignee.gravatar_id")
                        || itemWithoutSingleQuotes.contains("ResponseBody_milestone.creator.gravatar_id")
                        || itemWithoutSingleQuotes.contains("ResponseBody_milestone.description")
                        || itemWithoutSingleQuotes.contains("ResponseBody_user.gravatar_id")) {
                } else {
                    System.out.println(itemWithoutSingleQuotes + " Positives: " + positiveExampleCount + " Negatives: " + negativeExampleCount);
                }

                positiveExampleCount = 0;
                negativeExampleCount = 0;
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public static void getPosNegClasses(String targetClassInput, String dataSetTypeInput) throws Exception {

        String responseType = targetClassInput.split("_")[0];
        String key = getDataTypeKeyValue(targetClassInput, responseType).split("~")[0];
        String value = getDataTypeKeyValue(targetClassInput, responseType).split("~")[1];

        ResponseTypeString = responseType;
        keyInput = key;
        valueInput = value;

        StringRenderer.setRenderer(StringRenderer.Rendering.MANCHESTER_SYNTAX);

        try {
            HTTPTransaction.read(preprocessedData);
            addToExamples(dataSetTypeInput);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public static void addToExamples(String dataSetTypeInput) throws Exception {
        List<String> lastIdList = new ArrayList<>();

        // Create a list of last resource of each transaction
        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : transactions.entrySet()) {
            List<String> lastIdLinkedList = new LinkedList<String>();

            for (List<HTTPTransaction> mm : m.getValue().values()) {
                lastIdLinkedList.add(mm.get(0).transaction);
            }

            lastIdList.add(lastIdLinkedList.get(lastIdLinkedList.size() - 1));
        }

        for (Map.Entry<String, TreeMap<String, List<HTTPTransaction>>> m : transactions.entrySet()) {
            for (List<HTTPTransaction> mm : m.getValue().values()) {
                if (lastIdList.contains(mm.get(0).transaction)) {

                    // STATUS CODE
                    if (ResponseTypeString.equals(ResponseType.ResponseStatusCode.toString())) {
                        if (mm.get(0).getCode().equals(valueInput)) {
                            positiveExamplesList.add(ResponseType.ResponseStatusCode + "_" + valueInput);
                        } else {
                            negativeExamplesList.add(ResponseType.ResponseStatusCode + "_" + valueInput);
                        }
                    }

                    // RESPONSE HEADER
                    else if (ResponseTypeString.equals(ResponseType.ResponseHeader.toString())) {
                        String responseHeaderValue = "";
                        JSONObject jsonObject = new JSONObject();

                        switch (datasetInput) {
                            case "googletasks":
                                responseHeaderValue = String.valueOf(Utilities.responseHeaderGoogle(mm, keyInput));
                                jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArrayGoogleTasks();
                                break;
                            case "slack":
                                responseHeaderValue = String.valueOf(Utilities.responseHeaderSlack(mm, keyInput));
                                jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArraySlack();
                                break;
                            case "twitter":
                                responseHeaderValue = String.valueOf(Utilities.responseHeadersTwitter(mm, keyInput));
                                jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArrayTwitter();
                                break;
                            case "ghtraffic":
                                responseHeaderValue = String.valueOf(Utilities.responseHeadersGHTraffic(mm, keyInput));
                                jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArrayGHTraffic();
                                break;
                            default: {
                            }
                        }

                        if (responseHeaderValue.equals(getJsonValueByKey(valueInput, datasetInput))) {
                            positiveExamplesList.add(ResponseType.ResponseHeader + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                        } else {
                            negativeExamplesList.add(ResponseType.ResponseHeader + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                        }
                    }

                    // RESPONSE BODY
                    else if (ResponseTypeString.equals(ResponseType.ResponseBody.toString())) {
                        JSONObject jsonObject = new JSONObject();

                        // GOOGLE
                        if (dataSetTypeInput.equals("googletasks")) {
                            jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArrayGoogleTasks();

                            if (!keyInput.contains(".")) {
                                if (String.valueOf(Utilities.responseBodyGoogle(mm, keyInput, null, null)).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (String.valueOf(Utilities.responseBodyGoogle(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], null)).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 2) {
                                if (String.valueOf(Utilities.responseBodyGoogle(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], keyInput.split("\\.")[2])).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            }
                        }

                        // SLACK
                        else if (dataSetTypeInput.equals("slack")) {
                            jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArraySlack();
                            if (StringUtils.countMatches(keyInput, ".") == 0) {
                                if (getRefinedValue(String.valueOf(Utilities.responseBodySlack(mm, keyInput, null, null))).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (getRefinedValue(String.valueOf(Utilities.responseBodySlack(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], null))).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 2) {
                                if (getRefinedValue(String.valueOf(Utilities.responseBodySlack(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], keyInput.split("\\.")[2]))).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            }
                        }

                        // TWITTER
                        else if (dataSetTypeInput.equals("twitter")) {
                            jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArrayTwitter();
                            if (StringUtils.countMatches(keyInput, ".") == 0) {
                                if (getRefinedValue(String.valueOf(Utilities.responseBodyTwitter(mm, keyInput))).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (getRefinedValue(String.valueOf(Utilities.responseBodyInsideTwitter(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1]))).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            }
                        }

                        // GHTRAFFIC
                        else if (dataSetTypeInput.equals("ghtraffic")) {
                            jsonObject = OWLClassLabelsJSONArrays.getOwlClassLabelsJsonArrayGHTraffic();

                            if (!keyInput.contains(".")) {
                                if (String.valueOf(Utilities.responseBodyGHTraffic(mm, keyInput, null, null)).contains(getJsonValueByKey(valueInput,"ghtraffic"))) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (String.valueOf(Utilities.responseBodyGHTraffic(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], null)).contains(getJsonValueByKey(valueInput,"ghtraffic"))) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 2) {
                                if (String.valueOf(Utilities.responseBodyGHTraffic(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], keyInput.split("\\.")[2])).contains(getJsonValueByKey(valueInput,"ghtraffic"))) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + getJsonKeyByValue(valueInput, jsonObject));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static String getRefinedValue(String value) {
        if (value.contains("message_not_found")) {
            value = "messagenotfound";
        }

        if (value.contains("Thilini Bhagya")) {
            value = "ThiliniBhagya";
        }

        return value;
    }

    public static String getDataTypeKeyValue(String classInput, String responseType) {
        String key = "";
        String value = "";

        if (responseType.equals("ResponseStatusCode")) {
            value = classInput.split("_", 2)[1];
        } else if (StringUtils.countMatches(classInput, "_") >= 2) {
            String keyValue = classInput.split("_", 2)[1];
            int index = keyValue.lastIndexOf("_");
            key = keyValue.substring(0, index);
            value = keyValue.substring(index).replace("_", "");
        }

        return key + "~" + value;
    }

    public static void getTargetClass(List<OWLClass> owlClassList) {
        for (OWLClass owlClass : owlClassList) {
            owlClassStringList.add(owlClass.toString());
        }
    }
}
