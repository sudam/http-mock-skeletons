package nz.ac.massey.httpmockskeletons.scripts.modelevaluator.attributebasedlearningmodeltester;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer.*;
import org.apache.commons.cli.*;
import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;

/**
 * this is the main class which can be used to run
 * different experiments with different algorithms and datasets
 *
 * @author thilinibhagya
 */

public class ModelTester {

    public static int indexToLearn;
    public static String dataFileName;
    public static String indicesFileName;

    public static String datasetInput = "";
    public static String algorithmInput = "";
    public static String indexInput = "";

    static org.apache.log4j.Logger LOGGER = Logging.getLogger(ModelTrainer.class);

    public static void main(String args[]) throws IOException {

        // Create Options for input values
        Options options = new Options();

        options.addOption("d", "dataset", true, "Dataset");
        options.addOption("a", "algorithm", true, "Algorithm");
        options.addOption("i", "index", true, "Index");
        options.addOption("key", "checkAttributeDetails", true, "Check Attribute Details");

        try {
            // Read from CLI input
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("d") && cmd.hasOption("a") && cmd.hasOption("i")) {
                datasetInput = cmd.getOptionValue("d");
                algorithmInput = cmd.getOptionValue("a");
                indexInput = cmd.getOptionValue("i");

                indexToLearn = Integer.parseInt(indexInput);

                // Set file names to variables of each dataset type
                switch (datasetInput) {
                    case "GoogleTasks":
                        dataFileName = "src/resources/Google-Features";
                        indicesFileName = "src/resources/google-indexes.txt";
                        break;
                    case "Slack":
                        dataFileName = "src/resources/Slack-Features";
                        indicesFileName = "src/resources/slack-indexes.txt";
                        break;
                    case "GHTraffic":
                        dataFileName = "src/resources/GHTraffic-Features";
                        indicesFileName = "src/resources/ghtraffic-indexes.txt";
                        break;
                    case "Twitter":
                        dataFileName = "src/resources/Twitter-Features";
                        indicesFileName = "src/resources/twitter-indexes.txt";
                        break;
                    default: {
                    }
                }

                int[] indices = ReadIndexesForInput.readIndexes(indicesFileName);
                indices = ArrayUtils.removeElement(indices, indexToLearn);

                // Specify different options and related actions to be performed
                switch (algorithmInput) {
                    case "C4.5":
                        C45ModelTester.processWithC45(dataFileName, indexToLearn, indices);
                        break;
                    case "RIPPER":
                        RIPPERModelTester.processWithRipper(dataFileName, indexToLearn, indices);
                        break;
                    case "PART":
                        PARTModelTester.processWithPART(dataFileName, indexToLearn, indices);
                        break;
                    default: {
                    }
                }
            } else if (cmd.hasOption("key")) {
                LOGGER.info("Checking Optimal Targets for Learning " + datasetInput);
                CheckAttributesDetails.AttributeDetails(args, options);
            } else {
                LOGGER.warn("Error! Invalid Arguments \n" + "Key: -d Dataset -a Algorithm -i Index");
            }

        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
