package nz.ac.massey.httpmockskeletons.scripts.modellearner;

import nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer.CheckTargetAttributesToLearn;
import nz.ac.massey.httpmockskeletons.scripts.modellearner.dllearningmodeltrainer.CheckTargetClassesToLearn;
import nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer.C45ModelTrainer;
import nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer.PARTModelTrainer;
import nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer.RIPPERModelTrainer;
import org.apache.commons.cli.*;
import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;

import static nz.ac.massey.httpmockskeletons.scripts.commons.Utilities.readIndices;
import static nz.ac.massey.httpmockskeletons.scripts.modellearner.dllearningmodeltrainer.OCELModelTrainer.processWithOCEL;

/**
 * this is the main class which can be used
 * to train models with different algorithms and datasets
 *
 * @author thilini bhagya
 */

public class ModelTrainer {

    public static int indexToLearn;
    public static String dataFileName;
    public static String datasetInput = "";
    public static String algorithmInput = "";
    public static String indexInput = "";
    public static String classNameInput = "";

    public ModelTrainer(String datasetInput) throws Exception {
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

                ModelTrainer modelTrainer = new ModelTrainer(datasetInput);

                int[] indices = readIndices(datasetInput);
                indices = ArrayUtils.removeElement(indices, indexToLearn);

                // Specify different options and related actions to be performed
                switch (algorithmInput) {
                    case "c4.5":
                        C45ModelTrainer.processWithC45(dataFileName, indexToLearn, indices);
                        break;
                    case "ripper":
                        RIPPERModelTrainer.processWithRipper(dataFileName, indexToLearn, indices);
                        break;
                    case "part":
                        PARTModelTrainer.processWithPART(dataFileName, indexToLearn, indices);
                        break;
                    default: {
                    }
                }
            } else if (cmd.hasOption("d") && cmd.hasOption("a") && cmd.hasOption("c")) {

                datasetInput = cmd.getOptionValue("d").toString().toLowerCase();
                algorithmInput = cmd.getOptionValue("a").toString().toLowerCase();
                classNameInput = cmd.getOptionValue("c");

                ModelTrainer modelTrainer = new ModelTrainer(datasetInput);

                processWithOCEL(datasetInput, algorithmInput, classNameInput);

            } else if (cmd.hasOption("ilist")) { // Check attributes
                datasetInput = cmd.getOptionValue("ilist").toString().toLowerCase();
                ModelTrainer modelTrainer = new ModelTrainer(datasetInput);
                System.out.println("Optimal target attribute indexes for predictions in " + cmd.getOptionValue("ilist") +" dataset (with name and type) \n");
                CheckTargetAttributesToLearn.AttributeDetails(args, options);

            } else if (cmd.hasOption("clist")) { // Check valid classes
                datasetInput = cmd.getOptionValue("clist").toString().toLowerCase();
                ModelTrainer modelTrainer = new ModelTrainer(datasetInput);
                System.out.println("Optimal target class names for predictions in " + cmd.getOptionValue("clist") +" dataset (with number of positive and negative examples) \n");
                CheckTargetClassesToLearn.validClassDetails(datasetInput);

            } else {
                System.out.println("Invalid arguments");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
