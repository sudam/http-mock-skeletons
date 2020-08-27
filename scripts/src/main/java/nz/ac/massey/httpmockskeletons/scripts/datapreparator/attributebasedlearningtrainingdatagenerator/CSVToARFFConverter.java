package nz.ac.massey.httpmockskeletons.scripts.datapreparator.attributebasedlearningtrainingdatagenerator;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import java.io.File;

/**
 * This class uses to generate arff file based on csv
 * to be used as input to weka
 * @author thilinibhagya
 */
public class CSVToARFFConverter {
    static org.apache.log4j.Logger LOGGER = Logging.getLogger(CSVGeneratorForGHTraffic.class);
    public static void convertCSVDataFileToARFFFormat(String fileName) throws Exception {
        LOGGER.info("Generating an ARFF file from the CSV");
        // load CSV file
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(fileName+".csv"));
        Instances data = loader.getDataSet();

        // save data in ARFF format
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(fileName+".arff"));
        saver.writeBatch();
        // .arff file will be created in the output location
    }
}
