package nz.ac.massey.httpmockskeletons.scripts.datapreparator.attributebasedlearningtrainingdatagenerator;


import nz.ac.massey.httpmockskeletons.scripts.Logging;
import org.apache.commons.cli.*;

/**
 * this is the main class which can be used to create input files (arff)
 * suitable for attribute-based learning algorithms
 * from datasets
 *
 * @author thilinibhagya
 */
public class ARFFFileGenerator {
    static org.apache.log4j.Logger LOGGER = Logging.getLogger(ARFFFileGenerator.class);

    //create an Options for datasets

    public static final Option dataset1 = Option.builder("googletasks")
            .required(true)
            .longOpt("Google Tasks dataset")
            .build();

    public static final Option dataset2 = Option.builder("slack")
            .required(true)
            .longOpt("Slack dataset")
            .build();

    public static final Option dataset3 = Option.builder("ghtraffic")
            .required(true)
            .longOpt("GHTraffic dataset")
            .build();

    public static final Option dataset4 = Option.builder("twitter")
            .required(true)
            .longOpt("Twitter dataset")
            .build();

    public static void main(String args[]) throws Exception {

        //create an OptionGroup for datasets
        Options options = new Options();
        OptionGroup optgrp = new OptionGroup();
        optgrp.addOption(dataset1);
        optgrp.addOption(dataset2);
        optgrp.addOption(dataset3);
        optgrp.addOption(dataset4);
        options.addOptionGroup(optgrp);

        try {

            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);

            //specify different options and related actions to be performed
            if (((cmd.hasOption("googletasks")))) {
                //generate csv file
                CSVGeneratorForGoogleTasks.csvFileGeneratorWithAttributes("src/resources/googletasks-training", "src/resources/googletasks-preprocessed");
                //generate arff file from csv
                CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/googletasks-training");
            }

            if (((cmd.hasOption("slack")))) {
                //generate csv file
                CSVGeneratorForSlack.csvFileGeneratorWithAttributes("src/resources/slack-training", "src/resources/slack-preprocessed");
                //generate arff file from csv
                CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/slack-training");
            }

            if (((cmd.hasOption("ghtraffic")))) {
                //generate csv file
                CSVGeneratorForGHTraffic.csvFileGeneratorWithAttributes("src/resources/ghtraffic-training", "src/resources/ghtraffic-preprocessed");

                CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/ghtraffic-training");
            }

            if (((cmd.hasOption("twitter")))) {
                //generate csv file
                CSVGeneratorForTwitter.csvFileGeneratorWithAttributes("src/resources/twitter-training", "src/resources/twitter-preprocessed");
                //generate arff file from csv
                CSVToARFFConverter.convertCSVDataFileToARFFFormat("src/resources/twitter-training");
            }
        } catch (Exception x) {
            LOGGER.warn("Exception writing details to log ", x);
        }

    }
}
