package nz.ac.massey.httpmockskeletons.scripts.datapreparator;

import nz.ac.massey.httpmockskeletons.scripts.datapreparator.rawdatapreprocessor.RawJSONToCSVConverterForGHTraffic;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.rawdatapreprocessor.RawXMLToCSVConverterForGoogleTasks;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.rawdatapreprocessor.RawXMLToCSVConverterForSlack;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.rawdatapreprocessor.RawXMLToCSVConverterForTwitter;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.attributebasedlearningtrainingdatagenerator.*;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForGHTraffic;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForGoogleTasks;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForSlack;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForTwitter;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.File;

public class TrainingDataGenerator {
    private static Logger LOGGER = Logger.getLogger(TrainingDataGenerator.class);

    public static void main(String[] args) throws Exception {
        // Create Options for input values
        Options options = new Options();

        options.addOption("d", "dataset", true, "Dataset");
        options.addOption("t", "learningType", true, "Learning Type");

        // Read from CLI input
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);

        String dataSetTypeInput = "";
        String learningType = "";

        if (cmd.hasOption("d") && cmd.hasOption("t")) {
            dataSetTypeInput = cmd.getOptionValue("d").toLowerCase();
            learningType = cmd.getOptionValue("t").toLowerCase();
        } else {
            logInvalidInput();
        }

        if (learningType.toLowerCase().contains("dllearner")) {
            switch (dataSetTypeInput) {
                case "ghtraffic":
                    LOGGER.info("Generating OWL file for GHTraffic");
                    RawJSONToCSVConverterForGHTraffic.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/sub-ghtraffic-preprocessed.csv", "sub-ghtraffic-S-2.0.0.json");
                    OWLFileGeneratorForGHTraffic.generateOwlFile("ghtraffic-training-owl","sub-ghtraffic-preprocessed");
                    break;
                case "slack":
                    LOGGER.info("Generating OWL file for Slack");
                    RawXMLToCSVConverterForSlack.csvFileWriter(System.getProperty("user.dir") + File.separator + "src/resources/sub-slack-preprocessed.csv", "sub-slack-1.0.0.xml");
                    OWLFileGeneratorForSlack.generateOwlFile("slack-training-owl","sub-slack-preprocessed");
                    break;
                case "twitter":
                    LOGGER.info("Generating OWL file for Twitter");
                    RawXMLToCSVConverterForTwitter.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/sub-twitter-preprocessed.csv", "sub-twitter-1.0.0.xml");
                    OWLFileGeneratorForTwitter.generateOwlFile("twitter-training-owl","sub-twitter-preprocessed");
                    break;
                case "googletasks":
                    LOGGER.info("Generating OWL file for Google Tasks");
                    RawXMLToCSVConverterForGoogleTasks.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/googletasks-preprocessed.csv", "googletasks-1.0.0.xml");
                    OWLFileGeneratorForGoogleTasks.generateOwlFile("googletasks-training-owl","googletasks-preprocessed");
                    break;
                default: {
                    logInvalidDataType();
                    break;
                }
            }
        } else if (learningType.toLowerCase().contains("weka")){
            switch (dataSetTypeInput) {
                case "ghtraffic":
                    RawJSONToCSVConverterForGHTraffic.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/ghtraffic-preprocessed.csv", "ghtraffic-S-2.0.0.json");
                    CSVGeneratorForGHTraffic.csvFileGeneratorWithAttributes("src/resources/ghtraffic-training", "src/resources/ghtraffic-preprocessed");
                    CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/ghtraffic-training");
                    break;
                case "slack":
                    RawXMLToCSVConverterForSlack.csvFileWriter(System.getProperty("user.dir") + File.separator + "src/resources/slack-preprocessed.csv", "slack-1.0.0.xml");
                    CSVGeneratorForSlack.csvFileGeneratorWithAttributes("src/resources/slack-training", "src/resources/slack-preprocessed");
                    CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/slack-training");
                    break;
                case "twitter":
                    RawXMLToCSVConverterForTwitter.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/twitter-preprocessed.csv", "twitter-1.0.0.xml");
                    CSVGeneratorForTwitter.csvFileGeneratorWithAttributes("src/resources/twitter-training", "src/resources/twitter-preprocessed");
                    CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/twitter-training");
                    break;
                case "googletasks":
                    RawXMLToCSVConverterForGoogleTasks.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/googletasks-preprocessed.csv", "googletasks-1.0.0.xml");
                    CSVGeneratorForGoogleTasks.csvFileGeneratorWithAttributes("src/resources/googletasks-training", "src/resources/googletasks-preprocessed");
                    CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/googletasks-training");
                    break;
                default: {
                    logInvalidDataType();
                    break;
                }
            }
        } else {
            logInvalidLearningType();
        }
    }

    public static void logInvalidLearningType(){
        LOGGER.warn("Invalid Learning Type!");
    }

    public static void logInvalidDataType(){
        LOGGER.warn("Invalid Learning Type!");
    }

    public static void logInvalidInput(){
        LOGGER.warn("Invalid Input!");
    }
}
