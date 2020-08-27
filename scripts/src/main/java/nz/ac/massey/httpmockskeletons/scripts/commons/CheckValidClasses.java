package nz.ac.massey.httpmockskeletons.scripts.commons;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.modellearner.dllearningmodeltrainer.OCELModelTrainer;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.dllearner.algorithms.ocel.OCEL;
import org.dllearner.core.KnowledgeSource;
import org.dllearner.core.StringRenderer;
import org.dllearner.kb.OWLFile;
import org.dllearner.learningproblems.PosNegLPStandard;
import org.dllearner.reasoning.ClosedWorldReasoner;
import org.json.simple.JSONObject;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import weka.core.Instances;
import weka.core.Option;
import weka.core.converters.ConverterUtils;

import java.util.*;
import java.util.stream.Collectors;

import static nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction.transactions;
import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.GetJsonKeyByValue;
import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.GetJsonValueByKey;

public class CheckValidClasses {
    public static String datasetInput = "";
    static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    static OWLOntology ontology;

    public static String preprocessedData = "";
    public static String keyInput = ""; // input parameter for Key
    public static String valueInput = ""; // input parameter for Value
    public static ResponseType ResponseTypeInput;
    public static String ResponseTypeString;

    public static List<String> positiveExamplesList = new ArrayList<>();
    public static List<String> negativeExamplesList = new ArrayList<>();

    public static List<OWLClass> owlClassResponseStatusCodeList = new ArrayList<>();
    public static List<OWLClass> owlClassResponseBodyList = new ArrayList<>();
    public static List<OWLClass> owlClassResponseHeaderList = new ArrayList<>();
    public static List<String> owlClassStringList = new ArrayList<>();

    static org.apache.log4j.Logger LOGGER = Logging.getLogger(CheckValidClasses.class);

    public enum ResponseType {
        ResponseHeader,
        ResponseStatusCode,
        ResponseBody
    }

    public static void ValidClassDetails(String dataSetTypeInput) {
        try {
            datasetInput = dataSetTypeInput.toLowerCase();

            CheckValidClasses checkValidClasses = new CheckValidClasses(datasetInput);

            getTargetClass(owlClassResponseStatusCodeList);
            getTargetClass(owlClassResponseHeaderList);
            getTargetClass(owlClassResponseBodyList);

            for(String owlClass : owlClassStringList){
                String targetClass = owlClass.split("#")[1].replace(">","");
                if(targetClass.substring(targetClass.length() - 1).contains("\'")){
                    targetClass = targetClass.substring(0, targetClass.length() - 1);
                }
                getPosNegClasses(targetClass, datasetInput);
            }

            List<String> finalPositiveExampleList = positiveExamplesList.stream().distinct().collect(Collectors.toList());
            List<String> finalNegativeExampleList = negativeExamplesList.stream().distinct().collect(Collectors.toList());

            List<String> myList = new ArrayList<>(finalPositiveExampleList);
            myList.retainAll(finalNegativeExampleList);

            for(String item : myList ){
                String itemWithoutSingleQuotes = item.contains("\'") ? item.replace("\'","") : item;
                System.out.println(itemWithoutSingleQuotes);
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public CheckValidClasses(String datasetInput) throws OWLOntologyCreationException {

        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        ontology = manager.createOntology(IOR);
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

        switch (datasetInput) {
            case "googletasks":
                preprocessedData = "src/resources/googletasks-preprocessed.csv";
                owlClassResponseStatusCodeList = HeaderLabel.getGoogleOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseStatusCode.toString());
                owlClassResponseBodyList = HeaderLabel.getGoogleOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseBody.toString());
                owlClassResponseHeaderList = HeaderLabel.getGoogleOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseHeader.toString());
                break;
            case "slack":
                preprocessedData = "src/resources/sub-slack-preprocessed.csv";
                owlClassResponseStatusCodeList = HeaderLabel.getSlackOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseStatusCode.toString());
                owlClassResponseBodyList = HeaderLabel.getSlackOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseBody.toString());
                owlClassResponseHeaderList = HeaderLabel.getSlackOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseHeader.toString());
                break;
            case "ghtraffic":
                preprocessedData = "src/resources/sub-ghtraffic-preprocessed.csv";
                owlClassResponseStatusCodeList = HeaderLabel.getGHTrafficOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseStatusCode.toString());
                owlClassResponseBodyList = HeaderLabel.getGHTrafficOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseBody.toString());
                owlClassResponseHeaderList = HeaderLabel.getGHTrafficOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseHeader.toString());
                break;
            case "twitter":
                preprocessedData = "src/resources/sub-twitter-preprocessed.csv";
                owlClassResponseStatusCodeList = HeaderLabel.getTwitterOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseStatusCode.toString());
                owlClassResponseBodyList = HeaderLabel.getTwitterOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseBody.toString());
                owlClassResponseHeaderList = HeaderLabel.getTwitterOwlClassList(factory, ontology, manager, IOR, ResponseType.ResponseHeader.toString());
                break;
            default: {
            }
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
                                responseHeaderValue = String.valueOf(Utilities.ResponseHeaderGoogle(mm, keyInput));
                                jsonObject = HeaderLabel.getGoogleArray();
                                break;
                            case "slack":
                                responseHeaderValue = String.valueOf(Utilities.ResponseHeaderSlack(mm, keyInput));
                                jsonObject = HeaderLabel.getSlackArray();
                                break;
                            case "twitter":
                                responseHeaderValue = String.valueOf(Utilities.ResponseHeadersTwitter(mm, keyInput));
                                jsonObject = HeaderLabel.getTwitterArray();
                                break;
                            case "ghtraffic":
                                responseHeaderValue = String.valueOf(Utilities.ResponseHeadersGHTraffic(mm, keyInput));
                                jsonObject = HeaderLabel.getGHTrafficArray();
                                break;
                            default: {
                            }
                        }

                        if (responseHeaderValue.equals(valueInput)) {
                            positiveExamplesList.add(ResponseType.ResponseHeader + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                        } else {
                            negativeExamplesList.add(ResponseType.ResponseHeader + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                        }
                    }

                    // RESPONSE BODY
                    else if (ResponseTypeString.equals(ResponseType.ResponseBody.toString())) {
                        JSONObject jsonObject = new JSONObject();

                        // GOOGLE
                        if (dataSetTypeInput.equals("googletasks")) {
                            jsonObject = HeaderLabel.getGoogleArray();
                            if (!keyInput.contains(".")) {
                                if (String.valueOf(Utilities.ResponseBodyGoogle(mm, keyInput, null, null)).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (String.valueOf(Utilities.ResponseBodyGoogle(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], null)).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 2) {
                                if (String.valueOf(Utilities.ResponseBodyGoogle(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], keyInput.split("\\.")[2])).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                }
                            }
                        }

                        // SLACK
                        else if (dataSetTypeInput.equals("slack")) {
                            jsonObject = HeaderLabel.getSlackArray();
                            if (StringUtils.countMatches(keyInput, ".") == 0) {
                                if (getRefinedValue(String.valueOf(Utilities.ResponseBodySlack(mm, keyInput, null, null))).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (getRefinedValue(String.valueOf(Utilities.ResponseBodySlack(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], null))).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 2) {
                                if (getRefinedValue(String.valueOf(Utilities.ResponseBodySlack(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], keyInput.split("\\.")[2]))).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                }
                            }
                        }

                        // TWITTER
                        else if (dataSetTypeInput.equals("twitter")) {
                            jsonObject = HeaderLabel.getTwitterArray();
                            if (StringUtils.countMatches(keyInput, ".") == 0) {
                                if (getRefinedValue(String.valueOf(Utilities.ResponseBodyTwitter(mm, keyInput))).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (getRefinedValue(String.valueOf(Utilities.ResponseBodyInsideTwitter(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1]))).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                }
                            }
                        }

                        // GHTRAFFIC
                        else if (dataSetTypeInput.equals("ghtraffic")) {
                            jsonObject = HeaderLabel.getGHTrafficArray();

                            if (!keyInput.contains(".")) {
                                if (String.valueOf(Utilities.ResponseBodyGHTraffic(mm, keyInput, null, null)).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (String.valueOf(Utilities.ResponseBodyGHTraffic(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], null)).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 2) {
                                if (String.valueOf(Utilities.ResponseBodyGHTraffic(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], keyInput.split("\\.")[2])).contains(valueInput)) {
                                    positiveExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
                                } else {
                                    negativeExamplesList.add(ResponseType.ResponseBody + "_" + keyInput + "_" + GetJsonKeyByValue(valueInput, jsonObject));
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
