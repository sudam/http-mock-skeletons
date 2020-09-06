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

/**
 * Prepare necessary datasets for each learning type [attribute based learning / description logic learning]
 *
 * @author Thilini Bhagya
 **/

public class TrainingDataGenerator {
    private static Logger LOGGER = Logger.getLogger(TrainingDataGenerator.class);

    public static void main(String[] args) throws Exception {
        // Create Options for input values
        Options options = new Options();

        options.addOption("d", "dataset", true, "Dataset");
        options.addOption("l", "learningType", true, "Learning Type");

        // Read from CLI input
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);

        String dataSetTypeInput = "";
        String learningType = "";

        if (cmd.hasOption("d") && cmd.hasOption("l")) {
            dataSetTypeInput = cmd.getOptionValue("d").toLowerCase();
            learningType = cmd.getOptionValue("l").toLowerCase();
        } else {
            logInvalidInput();
        }

        if (learningType.toLowerCase().contains("dll")) {
            switch (dataSetTypeInput) {
                case "ghtraffic":
                    RawJSONToCSVConverterForGHTraffic.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/sub-ghtraffic-preprocessed.csv", "sub-ghtraffic-S-2.0.0.json");
                    CSVGeneratorForGHTraffic.csvFileGeneratorWithAttributes("src/resources/sub-ghtraffic-training", "src/resources/sub-ghtraffic-preprocessed");
                    OWLFileGeneratorForGHTraffic.generateOwlFile("ghtraffic-training","sub-ghtraffic-preprocessed", "sub-ghtraffic-training");
                    break;
                case "slack":
                    RawXMLToCSVConverterForSlack.csvFileWriter(System.getProperty("user.dir") + File.separator + "src/resources/sub-slack-preprocessed.csv", "sub-slack-1.0.0.xml");
                    CSVGeneratorForSlack.csvFileGeneratorWithAttributes("src/resources/sub-slack-training", "src/resources/sub-slack-preprocessed");
                    OWLFileGeneratorForSlack.generateOwlFile("slack-training","sub-slack-preprocessed","sub-slack-training");
                    break;
                case "twitter":
                    RawXMLToCSVConverterForTwitter.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/sub-twitter-preprocessed.csv", "sub-twitter-1.0.0.xml");
                    CSVGeneratorForTwitter.csvFileGeneratorWithAttributes("src/resources/sub-twitter-training", "src/resources/sub-twitter-preprocessed");
                    OWLFileGeneratorForTwitter.generateOwlFile("twitter-training","sub-twitter-preprocessed","sub-twitter-training");
                    break;
                case "googletasks":
                    RawXMLToCSVConverterForGoogleTasks.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/googletasks-preprocessed.csv", "googletasks-1.0.0.xml");
                    CSVGeneratorForGoogleTasks.csvFileGeneratorWithAttributes("src/resources/googletasks-training", "src/resources/googletasks-preprocessed");
                    OWLFileGeneratorForGoogleTasks.generateOwlFile("googletasks-training","googletasks-preprocessed","googletasks-training");
                    break;
                default: {
                    logInvalidDataType();
                    break;
                }
            }
        } else if (learningType.toLowerCase().contains("abl")){
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
        LOGGER.warn("Invalid learning type!");
    }

    public static void logInvalidDataType(){
        LOGGER.warn("Invalid data type!");
    }

    public static void logInvalidInput(){
        LOGGER.warn("Invalid input!");
    }
}
