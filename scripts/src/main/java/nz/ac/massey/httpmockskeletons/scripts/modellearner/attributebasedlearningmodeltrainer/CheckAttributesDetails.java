package nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * this class allows to check what are the attributes and their indexes
 * for selecting which attribute to learn and which to remove
 */
public class CheckAttributesDetails {
    public static String datasetInput = "";
    public static String dataFileName = "";

    static org.apache.log4j.Logger LOGGER = Logging.getLogger(CheckAttributesDetails.class);

    public static void AttributeDetails(String args[], Options options) {
        try {
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);

            datasetInput = cmd.getOptionValue("key");

            switch (datasetInput) {
                case "GoogleTasks":
                    dataFileName = "src/resources/Google-Features";
                    break;
                case "GHTraffic":
                    dataFileName = "src/resources/GHTraffic-Features";
                    break;
                case "Twitter":
                    dataFileName = "src/resources/Twitter-Features";
                    break;
                case "Slack":
                    dataFileName = "src/resources/Slack-Features";
                    break;
            }

            Instances instances = new ConverterUtils.DataSource(dataFileName + ".arff").getDataSet();

            for (int i = 0; i < instances.numAttributes(); i++) {
                int distinctValueCount = instances.numDistinctValues(i);
                String name = instances.attribute(i).name();
                String type = instances.attribute(i).toString().split(" ",3)[2];
                String feature = name.contains("_") ? name.split("_")[0] : name;

                if ((feature.equals("ResponseHeader") || feature.equals("ResponseBody") || feature.equals("ResponseStatusCode")) && distinctValueCount < 10 && distinctValueCount != 1) {
                    System.out.println(i + " " + name + " " + type );
                }
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}


