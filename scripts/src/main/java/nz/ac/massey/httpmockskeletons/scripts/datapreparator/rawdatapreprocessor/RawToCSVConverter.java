package nz.ac.massey.httpmockskeletons.scripts.datapreparator.rawdatapreprocessor;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

import java.io.*;

/**
 * This class extracts resource,time,method,status,url,request/response body and headers data of each json/xml record
 * in raw ghtraffic-S-1.0.0.json / Twitter-1.0.0.xml / Slack-1.0.0.xml / GoogleTasks-1.0.0.xml files
 * sorts data using resource identifiers
 * saves data in CSV format
 *
 * @author thilini bhagya
 */

public class RawToCSVConverter {
    static org.apache.log4j.Logger LOGGER = Logging.getLogger(RawToCSVConverter.class);

    public static void main(String[] args) throws Exception {
        String rawDataSetFileName = "";

        // Create Options for input values
        Options options = new Options();

        options.addOption("f", "rawDatasetFile", true, "Raw Dataset File");

        // Read from CLI input
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("f")) {
            rawDataSetFileName = cmd.getOptionValue("f");

            switch (rawDataSetFileName) {
                case "ghtraffic-S-2.0.0.json":
                    LOGGER.info("Converting GHTraffic JSON dataset to CSV file");
                    RawJSONToCSVConverterForGHTraffic.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/ghtraffic-preprocessed.csv", rawDataSetFileName);
                    break;
                case "twitter-1.0.0.xml":
                    LOGGER.info("Converting Twitter XML dataset to CSV file");
                    RawXMLToCSVConverterForTwitter.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/twitter-preprocessed.csv", rawDataSetFileName);
                    break;
                case "slack-1.0.0.xml":
                    LOGGER.info("Converting Slack XML dataset to CSV file");
                        RawXMLToCSVConverterForSlack.csvFileWriter(System.getProperty("user.dir") + File.separator + "src/resources/slack-preprocessed.csv", rawDataSetFileName);
                    break;
                case "googletasks-1.0.0.xml":
                    LOGGER.info("Converting Google XML dataset to CSV file");
                    RawXMLToCSVConverterForGoogleTasks.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/googletasks-preprocessed.csv", rawDataSetFileName);
                    break;
                case "sub-ghtraffic-S-2.0.0.json":
                    LOGGER.info("Converting GHTraffic JSON dataset to CSV file");
                    RawJSONToCSVConverterForGHTraffic.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/sub-ghtraffic-preprocessed.csv", rawDataSetFileName);
                    break;
                case "sub-twitter-1.0.0.xml":
                    LOGGER.info("Converting Twitter XML dataset to CSV file");
                    RawXMLToCSVConverterForTwitter.fileWriter(System.getProperty("user.dir") + File.separator + "src/resources/sub-twitter-preprocessed.csv", rawDataSetFileName);
                    break;
                case "sub-slack-1.0.0.xml":
                    LOGGER.info("Converting Slack XML dataset to CSV file");
                    RawXMLToCSVConverterForSlack.csvFileWriter(System.getProperty("user.dir") + File.separator + "src/resources/sub-slack-preprocessed.csv",rawDataSetFileName);
                    break;
                default: {
                }
            }
        } else {
            LOGGER.warn("Invalid arguments!");
        }
    }
}
