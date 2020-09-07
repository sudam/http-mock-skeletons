package nz.ac.massey.httpmockskeletons.scripts.modelevaluator;

import nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer.CheckTargetAttributesToLearn;
import nz.ac.massey.httpmockskeletons.scripts.modellearner.dllearningmodeltrainer.CheckTargetClassesToLearn;
import nz.ac.massey.httpmockskeletons.scripts.modelevaluator.attributebasedlearningmodeltester.C45ModelTester;
import nz.ac.massey.httpmockskeletons.scripts.modelevaluator.attributebasedlearningmodeltester.PARTModelTester;
import nz.ac.massey.httpmockskeletons.scripts.modelevaluator.attributebasedlearningmodeltester.RIPPERModelTester;
import org.apache.commons.cli.*;
import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;

import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.readIndices;
import static nz.ac.massey.httpmockskeletons.scripts.modelevaluator.dllearningmodeltester.OCELModelTester.processWithOCEL;

/**
 * this is the main class which can be used
 * to test models with different algorithms and datasets
 *
 * @author thilini bhagya
 */

public class ModelTester {

    public static int indexToLearn;
    public static String dataFileName;
    public static String datasetInput = "";
    public static String algorithmInput = "";
    public static String indexInput = "";
    public static String classNameInput = "";

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
        options.addOption("ilist", "checkOptimalAttribute", true, "Check optimal attributes");
        options.addOption("clist", "checkOptimalClasses", true, "check optimal classes");

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

            } else if (cmd.hasOption("ilist")) {
                datasetInput = cmd.getOptionValue("ilist").toString().toLowerCase();
                ModelTester modelTester = new ModelTester(datasetInput);
                System.out.println("Optimal target attribute indexes for predictions in " + datasetInput+" dataset (with name and type)");
                System.out.println();
                CheckTargetAttributesToLearn.AttributeDetails(args, options);

            } else if (cmd.hasOption("clist")) { // Check valid classes

                datasetInput = cmd.getOptionValue("clist").toString().toLowerCase();
                ModelTester modelTester = new ModelTester(datasetInput);
                System.out.println("Optimal target class names for predictions in " + datasetInput+" dataset");
                System.out.println();
                CheckTargetClassesToLearn.validClassDetails(datasetInput);

            } else {
                System.out.println("Invalid arguments");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
