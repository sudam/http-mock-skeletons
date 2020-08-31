package nz.ac.massey.httpmockskeletons.scripts.modellearner.dllearningmodeltrainer;


import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction;
import nz.ac.massey.httpmockskeletons.scripts.commons.HeaderLabel;
import nz.ac.massey.httpmockskeletons.scripts.commons.Utilities;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForGHTraffic;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForSlack;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForTwitter;
import org.apache.commons.lang3.StringUtils;
import org.dllearner.core.StringRenderer;
import org.json.simple.JSONObject;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

import static nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction.transactions;
import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.GetJsonKeyByValue;

/**
 * this class allows to check what are the possible target classes
 * to learn
 */

public class CheckTargetClassesToLearn {
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

    static org.apache.log4j.Logger LOGGER = Logging.getLogger(CheckTargetClassesToLearn.class);

    public enum ResponseType {
        ResponseHeader,
        ResponseStatusCode,
        ResponseBody
    }

    public static void ValidClassDetails(String dataSetTypeInput) {
        try {
            datasetInput = dataSetTypeInput.toLowerCase();

            CheckTargetClassesToLearn checkValidClasses = new CheckTargetClassesToLearn(datasetInput);

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

    public CheckTargetClassesToLearn(String datasetInput) throws OWLOntologyCreationException {

        IRI IOR = IRI.create("http://owl.api/httptransactions.owl");
        ontology = manager.createOntology(IOR);
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();

        switch (datasetInput) {
            case "googletasks":
                preprocessedData = "src/resources/googletasks-preprocessed.csv";
                owlClassResponseStatusCodeList = readGoogleTasksOwlClassList(ResponseType.ResponseStatusCode);
                owlClassResponseBodyList = readGoogleTasksOwlClassList(ResponseType.ResponseBody);
                owlClassResponseHeaderList = readGoogleTasksOwlClassList(ResponseType.ResponseHeader);
                break;
            case "slack":
                preprocessedData = "src/resources/sub-slack-preprocessed.csv";
                owlClassResponseStatusCodeList = readSlackOwlClassList(ResponseType.ResponseStatusCode);
                owlClassResponseBodyList = readSlackOwlClassList(ResponseType.ResponseBody);
                owlClassResponseHeaderList = readSlackOwlClassList(ResponseType.ResponseHeader);
                break;
            case "ghtraffic":
                preprocessedData = "src/resources/sub-ghtraffic-preprocessed.csv";
                owlClassResponseStatusCodeList = readGHTrafficOwlClassList(ResponseType.ResponseStatusCode);
                owlClassResponseBodyList = readGHTrafficOwlClassList(ResponseType.ResponseBody);
                owlClassResponseHeaderList = readGHTrafficOwlClassList(ResponseType.ResponseHeader);
                break;
            case "twitter":
                preprocessedData = "src/resources/sub-twitter-preprocessed.csv";
                owlClassResponseStatusCodeList = readTwitterClassLists(ResponseType.ResponseStatusCode);
                owlClassResponseBodyList = readTwitterClassLists(ResponseType.ResponseBody);
                owlClassResponseHeaderList = readTwitterClassLists(ResponseType.ResponseHeader);
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

    public static ArrayList<OWLClass> readGoogleTasksOwlClassList(ResponseType featureType) {
        ArrayList<OWLClass> ontologyClassList = new ArrayList<>();
        String listPath = "src/resources/googleClassNameLists/";

        try {
            FileInputStream fis = null;

            switch (featureType) {
                case ResponseHeader:
                    fis = new FileInputStream(listPath + "ResponseHeaderOwlClassList");
                    break;
                case ResponseBody:
                    fis = new FileInputStream(listPath + "ResponseBodyOwlClassList");
                    break;
                case ResponseStatusCode:
                    fis = new FileInputStream(listPath + "ResponseStatusCodeOwlClassList");
                    break;
                default: {
                }
            }

            ObjectInputStream ois = new ObjectInputStream(fis);

            ontologyClassList = (ArrayList) ois.readObject();

            ois.close();
            fis.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;

        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }

        return ontologyClassList;
    }

    public static ArrayList<OWLClass> readGHTrafficOwlClassList(ResponseType featureType) {
        ArrayList<OWLClass> URIList = new ArrayList<OWLClass>();
        String listPath = "src/resources/ghTrafficClassNameLists/";

        try {
            FileInputStream fis = null;

            switch (featureType) {
                case ResponseHeader:
                    fis = new FileInputStream(listPath + "ResponseHeaderOwlClassList");
                    break;
                case ResponseBody:
                    fis = new FileInputStream(listPath + "ResponseBodyOwlClassList");
                    break;
                case ResponseStatusCode:
                    fis = new FileInputStream(listPath + "ResponseStatusCodeOwlClassList");
                    break;
                default: {
                }
            }

            ObjectInputStream ois = new ObjectInputStream(fis);

            URIList = (ArrayList) ois.readObject();

            ois.close();
            fis.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;

        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }

        return URIList;
    }

    public static ArrayList<OWLClass> readSlackOwlClassList(ResponseType featureType) {
        ArrayList<OWLClass> URIList = new ArrayList<OWLClass>();
        String listPath = "src/resources/slackClassNameLists/";

        try {
            FileInputStream fis = null;

            switch (featureType) {
                case ResponseHeader:
                    fis = new FileInputStream(listPath + "ResponseHeaderOwlClassList");
                    break;
                case ResponseBody:
                    fis = new FileInputStream(listPath + "ResponseBodyOwlClassList");
                    break;
                case ResponseStatusCode:
                    fis = new FileInputStream(listPath + "ResponseStatusCodeOwlClassList");
                    break;
                default: {
                }
            }

            ObjectInputStream ois = new ObjectInputStream(fis);

            URIList = (ArrayList) ois.readObject();

            ois.close();
            fis.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;

        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }

        return URIList;
    }

    public static ArrayList<OWLClass> readTwitterClassLists(ResponseType featureType) {
        ArrayList<OWLClass> URIList = new ArrayList<OWLClass>();
        String listPath = "src/resources/twitterClassNameLists/";

        try {
            FileInputStream fis = null;

            switch (featureType) {
                case ResponseHeader:
                    fis = new FileInputStream(listPath + "ResponseHeaderOwlClassList");
                    break;
                case ResponseBody:
                    fis = new FileInputStream(listPath + "ResponseBodyOwlClassList");
                    break;
                case ResponseStatusCode:
                    fis = new FileInputStream(listPath + "ResponseStatusCodeOwlClassList");
                    break;
                default: {
                }
            }

            ObjectInputStream ois = new ObjectInputStream(fis);

            URIList = (ArrayList) ois.readObject();

            ois.close();
            fis.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;

        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }

        return URIList;
    }
}
