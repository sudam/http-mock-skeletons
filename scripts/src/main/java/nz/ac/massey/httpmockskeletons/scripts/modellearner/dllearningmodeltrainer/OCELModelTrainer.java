package nz.ac.massey.httpmockskeletons.scripts.modellearner.dllearningmodeltrainer;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dllearner.algorithms.ocel.OCEL;
import org.dllearner.core.KnowledgeSource;
import org.dllearner.core.StringRenderer;
import org.dllearner.core.StringRenderer.Rendering;
import org.dllearner.kb.OWLFile;
import org.dllearner.learningproblems.PosNegLPStandard;
import org.dllearner.reasoning.ClosedWorldReasoner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction;
import nz.ac.massey.httpmockskeletons.scripts.commons.Utilities;

import java.util.*;

import org.apache.commons.cli.*;

import static nz.ac.massey.httpmockskeletons.scripts.commons.HTTPTransaction.transactions;
import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.GetJsonValueByKey;

import nz.ac.massey.httpmockskeletons.scripts.modelevaluator.dllearningmodeltester.KFoldCrossValidation;

public class OCELModelTrainer {
    public static String owlFile = "";
    public static String preprocessedData = "";
    public static String keyInput = ""; // input parameter for Key
    public static String valueInput = ""; // input parameter for Value
    public static String datasetInput = ""; // Google / Slack / GHTraffic / Twitter
    public static String jsonFileName = ""; // file name of json key value pair list for each dataset
    public static ResponseType ResponseTypeInput; // input parameter for Response Type
    public static List<String> classInstanceList = new ArrayList<String>();

    // private static Logger LOGGER = Logger.getLogger(OCELModelTrainer.class);

    public enum ResponseType {
        ResponseHeader,
        ResponseStatusCode,
        ResponseBody
    }

    public static void main(String[] args) throws Exception {
         processWithOCEL("slack", "", "ResponseBody_ok_false");
    }

    public OCELModelTrainer(String datasetInput, String responseTypeInput, String key, String value) {
        keyInput = key;
        valueInput = value;

        switch (datasetInput) {
            case "googletasks":
                owlFile = "src/resources/googletasks-training.owl";
                preprocessedData = "src/resources/googletasks-preprocessed.csv";
                jsonFileName = "googleHeaderLabels";
                break;
            case "slack":
                owlFile = "src/resources/slack-training.owl";
                preprocessedData = "src/resources/sub-slack-preprocessed.csv";
                jsonFileName = "slackHeaderLabels";
                break;
            case "ghtraffic":
                owlFile = "src/resources/ghtraffic-training.owl";
                preprocessedData = "src/resources/sub-ghtraffic-preprocessed.csv";
                jsonFileName = "ghTrafficHeaderLabels";
                break;
            case "twitter":
                owlFile = "src/resources/twitter-training.owl";
                preprocessedData = "src/resources/sub-twitter-preprocessed.csv";
                jsonFileName = "twitterHeaderLabels";
                break;
            default: {
            }
        }

        switch (responseTypeInput) {
            case "ResponseStatusCode":
                ResponseTypeInput = ResponseType.ResponseStatusCode;
                break;
            case "ResponseBody":
                ResponseTypeInput = ResponseType.ResponseBody;
                break;
            case "ResponseHeader":
                ResponseTypeInput = ResponseType.ResponseHeader;
                break;
        }
    }

    public static void processWithOCEL(String dataSetTypeInput, String algorithm, String targetClassInput) throws Exception {

        String responseType = targetClassInput.split("_")[0];
        String key = getDataTypeKeyValue(targetClassInput, responseType).split("~")[0];
        String value = getDataTypeKeyValue(targetClassInput, responseType).split("~")[1];

        datasetInput = dataSetTypeInput.toLowerCase();

        OCELModelTrainer ocelModelTrainer = new OCELModelTrainer(dataSetTypeInput.toLowerCase(), responseType, key, value);

        StringRenderer.setRenderer(Rendering.MANCHESTER_SYNTAX);

        KnowledgeSource source = new OWLFile(owlFile);

        // Setup the reasoner
        try {
            ClosedWorldReasoner reasoner = new ClosedWorldReasoner(source);
            reasoner.setDefaultNegation(false);
            reasoner.init();

            String classList[] = reasoner.getClasses().toString().replace("[", "").replace("]", "").split(",");

            for (String classItem : classList) {
                classItem = classItem.trim();
                classInstanceList.add(classItem);
            }

            OWLDataFactory df = new OWLDataFactoryImpl();
            PrefixManager pm = new DefaultPrefixManager();
            pm.setDefaultPrefix("http://owl.api/httptransactions.owl#");

            // Create a learning problem and set positive and negative examples
            PosNegLPStandard lp = new PosNegLPStandard(reasoner);
            Set<OWLIndividual> positiveExamples = new TreeSet<>();
            Set<OWLIndividual> negativeExamples = new TreeSet<>();

            HTTPTransaction.read(preprocessedData);

            addToExamples(positiveExamples, negativeExamples, df, pm, dataSetTypeInput);

            lp.setPositiveExamples(positiveExamples);
            lp.setNegativeExamples(negativeExamples);

            // create the learning algorithm
            OCEL las = new OCEL(lp, reasoner);

            if (positiveExamples.size() > 0 && negativeExamples.size() > 0) {

                las.setMaxExecutionTimeInSeconds(120);

                OWLClass cls = df.getOWLClass("Transaction", pm);
                las.setStartClass(cls);
                las.setTerminateOnNoiseReached(true);

                las.init();

                // run the learning algorithm
                lp.init();

                las.start();
                System.out.println("Current Best Description: "+las.getCurrentlyBestDescription());

            } else if (positiveExamples.size() == 0) {
                System.out.println("No positive examples have been set.");
                las.stop();

            } else if (negativeExamples.size() == 0) {
                System.out.println("No negative examples have been set.");
                las.stop();
            }
        } catch (Exception e) {
            // LOGGER.info(e.getMessage());
        }
    }

    public static void addToExamples(Set<OWLIndividual> positiveExamples, Set<OWLIndividual> negativeExamples, OWLDataFactory df, PrefixManager pm, String dataSetTypeInput) throws Exception {
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
                    if (ResponseTypeInput == ResponseType.ResponseStatusCode) {
                        if (mm.get(0).getCode().equals(valueInput)) {
                            addToPositiveExamples(positiveExamples, mm, df, pm);
                        } else {
                            addToNegativeExamples(negativeExamples, mm, df, pm);
                        }
                    }

                    // RESPONSE BODY
                    else if (ResponseTypeInput == ResponseType.ResponseBody) {
                        // GOOGLE
                        if (dataSetTypeInput.equals("googletasks")) {
                            if (!keyInput.contains(".")) {
                                if (String.valueOf(Utilities.ResponseBodyGoogle(mm, keyInput, null, null)).contains(GetJsonValueByKey(valueInput, datasetInput).toString())) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (String.valueOf(Utilities.ResponseBodyGoogle(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], null)).contains(GetJsonValueByKey(valueInput, datasetInput).toString())) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 2) {
                                if (String.valueOf(Utilities.ResponseBodyGoogle(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], keyInput.split("\\.")[2])).contains(GetJsonValueByKey(valueInput, datasetInput).toString())) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            }
                        }

                        // SLACK
                        else if (dataSetTypeInput.equals("slack")) {
                            if (StringUtils.countMatches(keyInput, ".") == 0) {
                                if (getRefinedValue(String.valueOf(Utilities.ResponseBodySlack(mm, keyInput, null, null))).contains(valueInput)) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (getRefinedValue(String.valueOf(Utilities.ResponseBodySlack(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], null))).contains(valueInput)) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 2) {
                                if (getRefinedValue(String.valueOf(Utilities.ResponseBodySlack(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], keyInput.split("\\.")[2]))).contains(valueInput)) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            }
                        }

                        // TWITTER
                        else if (dataSetTypeInput.equals("twitter")) {
                            if (StringUtils.countMatches(keyInput, ".") == 0) {
                                if (getRefinedValue(String.valueOf(Utilities.ResponseBodyTwitter(mm, keyInput))).contains(GetJsonValueByKey(valueInput, datasetInput).toString())) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (getRefinedValue(String.valueOf(Utilities.ResponseBodyInsideTwitter(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1]))).contains(GetJsonValueByKey(valueInput, datasetInput).toString())) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            }
                        }

                        // GHTRAFFIC
                        else if (dataSetTypeInput.equals("ghtraffic")) {
                            if (!keyInput.contains(".")) {
                                if (String.valueOf(Utilities.ResponseBodyGHTraffic(mm, keyInput, null, null)).contains(GetJsonValueByKey(valueInput, datasetInput).toString())) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 1) {
                                if (String.valueOf(Utilities.ResponseBodyGHTraffic(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], null)).contains(GetJsonValueByKey(valueInput, datasetInput).toString())) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            } else if (StringUtils.countMatches(keyInput, ".") == 2) {
                                if (String.valueOf(Utilities.ResponseBodyGHTraffic(mm, keyInput.split("\\.")[0], keyInput.split("\\.")[1], keyInput.split("\\.")[2])).contains(GetJsonValueByKey(valueInput, datasetInput).toString())) {
                                    addToPositiveExamples(positiveExamples, mm, df, pm);
                                } else {
                                    addToNegativeExamples(negativeExamples, mm, df, pm);
                                }
                            }
                        }
                    }

                    // RESPONSE HEADER
                    else if (ResponseTypeInput == ResponseType.ResponseHeader) {
                        String responseHeaderValue = "";

                        switch (datasetInput) {
                            case "googletasks":
                                responseHeaderValue = String.valueOf(Utilities.ResponseHeaderGoogle(mm, keyInput));
                                break;
                            case "slack":
                                responseHeaderValue = String.valueOf(Utilities.ResponseHeaderSlack(mm, keyInput));
                                break;
                            case "twitter":
                                responseHeaderValue = String.valueOf(Utilities.ResponseHeadersTwitter(mm, keyInput));
                                break;
                            case "ghtraffic":
                                responseHeaderValue = String.valueOf(Utilities.ResponseHeadersGHTraffic(mm, keyInput));
                                break;
                            default: {
                            }
                        }

                        if (responseHeaderValue.equals(GetJsonValueByKey(valueInput, datasetInput))) {
                            addToPositiveExamples(positiveExamples, mm, df, pm);
                        } else {
                            addToNegativeExamples(negativeExamples, mm, df, pm);
                        }
                    }
                }
            }
        }
    }

    public static void addToPositiveExamples(Set<OWLIndividual> positiveExamples, List<HTTPTransaction> mm, OWLDataFactory df, PrefixManager pm) {
        positiveExamples.add(df.getOWLNamedIndividual("T" + mm.get(0).transaction, pm));
    }

    public static void addToNegativeExamples(Set<OWLIndividual> negativeExamples, List<HTTPTransaction> mm, OWLDataFactory df, PrefixManager pm) {
        negativeExamples.add(df.getOWLNamedIndividual("T" + mm.get(0).transaction, pm));
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

}
