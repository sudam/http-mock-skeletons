package nz.ac.massey.httpmockskeletons.scripts.modelevaluator;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.commons.CheckValidClasses;
import nz.ac.massey.httpmockskeletons.scripts.modelevaluator.attributebasedlearningmodeltester.C45ModelTester;
import nz.ac.massey.httpmockskeletons.scripts.modelevaluator.attributebasedlearningmodeltester.PARTModelTester;
import nz.ac.massey.httpmockskeletons.scripts.modelevaluator.attributebasedlearningmodeltester.RIPPERModelTester;
import nz.ac.massey.httpmockskeletons.scripts.commons.CheckAttributesDetails;
import nz.ac.massey.httpmockskeletons.scripts.modellearner.ModelTrainer;
import org.apache.commons.cli.*;
import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;

import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.readIndices;
import static nz.ac.massey.httpmockskeletons.scripts.modelevaluator.dllearningmodeltester.OCELModelTester.processWithOCEL;

/**
 * this is the main class which can be used to run
 * different experiments with different algorithms and datasets
 *
 * @author thilinibhagya
 */

public class ModelTester {

    public static int indexToLearn;
    public static String dataFileName;

    public static String datasetInput = "";
    public static String algorithmInput = "";
    public static String indexInput = "";
    public static String classNameInput = "";

    static org.apache.log4j.Logger LOGGER = Logging.getLogger(ModelTester.class);

    public ModelTester(String datasetInput) throws Exception {
        // Set file names to variables of each dataset type
        switch (datasetInput) {
            case "googletasks":
                dataFileName = "src/resources/googletasks-training";
                break;
            case "slack":
                dataFileName = "src/resources/slack-training";
                break;
            case "ghtraffic":
                dataFileName = "src/resources/ghtraffic-training";
                break;
            case "twitter":
                dataFileName = "src/resources/twitter-training";
                break;
            default: {
            }
        }
    }

    public static void main(String args[]) throws IOException {
        // Create Options for input values
        Options options = new Options();

        options.addOption("d", "dataset", true, "Dataset");
        options.addOption("a", "algorithm", true, "Algorithm");
        options.addOption("i", "index", true, "Index");
        options.addOption("c", "className", true, "Class Name");
        options.addOption("key", "checkAttributeDetails", true, "Check Attribute Details");

        try {
            // Read from CLI input
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("d") && cmd.hasOption("a") && cmd.hasOption("i")) {
                datasetInput = cmd.getOptionValue("d").toString().toLowerCase();
                algorithmInput = cmd.getOptionValue("a").toString().toLowerCase();
                indexInput = cmd.getOptionValue("i");

                indexToLearn = Integer.parseInt(indexInput);

                ModelTester modelTester = new ModelTester(datasetInput);

                int[] indices = readIndices(datasetInput);
                indices = ArrayUtils.removeElement(indices, indexToLearn);

                // Specify different options and related actions to be performed
                switch (algorithmInput) {
                    case "c4.5":
                        C45ModelTester.processWithC45(dataFileName, indexToLearn, indices);
                        break;
                    case "ripper":
                        RIPPERModelTester.processWithRipper(dataFileName, indexToLearn, indices);
                        break;
                    case "part":
                        PARTModelTester.processWithPART(dataFileName, indexToLearn, indices);
                        break;
                    default: {
                    }
                }
            } else if (cmd.hasOption("d") && cmd.hasOption("a") && cmd.hasOption("c")) {
                datasetInput = cmd.getOptionValue("d").toString().toLowerCase();
                algorithmInput = cmd.getOptionValue("a").toString().toLowerCase();
                classNameInput = cmd.getOptionValue("c");

                ModelTester modelTester = new ModelTester(datasetInput);

                processWithOCEL(datasetInput, algorithmInput, classNameInput);

            } else if (cmd.hasOption("key")) {
                ModelTester modelTester = new ModelTester(datasetInput);
                LOGGER.info("Attribute Details " + datasetInput);
                CheckAttributesDetails.AttributeDetails(args, options);

            } else if (cmd.hasOption("class")) { // Check valid classes

                datasetInput = cmd.getOptionValue("class").toString().toLowerCase();
                ModelTester modelTester = new ModelTester(datasetInput);
                System.out.println("Valid Classes for " + datasetInput);
                CheckValidClasses.ValidClassDetails(datasetInput);

            } else {
                LOGGER.warn("Invalid Arguments!");
            }

        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
