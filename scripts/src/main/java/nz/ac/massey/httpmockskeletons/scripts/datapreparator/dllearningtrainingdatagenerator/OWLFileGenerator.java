package nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator;

import nz.ac.massey.httpmockskeletons.scripts.modellearner.dllearningmodeltrainer.OCELModelTrainer;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.attributebasedlearningtrainingdatagenerator.*;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForGHTraffic;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForGoogleTasks;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForSlack;
import nz.ac.massey.httpmockskeletons.scripts.datapreparator.dllearningtrainingdatagenerator.OWLFileGeneratorForTwitter;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

public class OWLFileGenerator {
    private static Logger LOGGER = Logger.getLogger(OCELModelTrainer.class);

    public static void main(String[] args) throws Exception {
        // Create Options for input values
        Options options = new Options();

        options.addOption("d", "dataset", true, "Dataset");
        options.addOption("s", "serviceType", true, "Service Type");

        // Read from CLI input
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);

        String dataSetTypeInput = "";
        String serviceType = "";

        if (cmd.hasOption("d") && cmd.hasOption("s")) {
            dataSetTypeInput = cmd.getOptionValue("d");
            serviceType = cmd.getOptionValue("s");
        }

        if (serviceType.toLowerCase().contains("dllearner")) {
            switch (dataSetTypeInput) {
                case "GHTraffic":
                    LOGGER.info("Generating OWL file for GHTraffic");
                    OWLFileGeneratorForGHTraffic.generateOwlFile();
                    break;
                case "Slack":
                    LOGGER.info("Generating OWL file for Slack");
                    OWLFileGeneratorForSlack.generateOwlFile();
                    break;
                case "Twitter":
                    LOGGER.info("Generating OWL file for Twitter");
                    OWLFileGeneratorForTwitter.generateOwlFile();
                    break;
                case "GoogleTasks":
                    LOGGER.info("Generating OWL file for Google Tasks");
                    OWLFileGeneratorForGoogleTasks.generateOwlFile();
                    break;
                default: {
                }
            }
        } else if (serviceType.toLowerCase().contains("weka")){
            switch (dataSetTypeInput) {
                case "GHTraffic":
                    CSVGeneratorForGHTraffic.csvFileGeneratorWithAttributes("src/resources/ghtraffic-training", "src/resources/ghtraffic-preprocessed");
                    CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/ghtraffic-training");
                    break;
                case "Slack":
                    CSVGeneratorForSlack.csvFileGeneratorWithAttributes("src/resources/slack-training", "src/resources/slack-preprocessed");
                    CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/slack-training");
                    break;
                case "Twitter":
                    CSVGeneratorForTwitter.csvFileGeneratorWithAttributes("src/resources/twitter-training", "src/resources/twitter-preprocessed");
                    CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/twitter-training");
                    break;
                case "GoogleTasks":
                    CSVGeneratorForGoogleTasks.csvFileGeneratorWithAttributes("src/resources/googletasks-training", "src/resources/googletasks-preprocessed");
                    CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/googletasks-training");
                    break;
                default: {
                }
            }
        } else {
            LOGGER.warn("Invalid Arguments!");
        }
    }
}
