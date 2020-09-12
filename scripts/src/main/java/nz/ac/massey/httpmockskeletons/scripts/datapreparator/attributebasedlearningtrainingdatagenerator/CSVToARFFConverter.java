package nz.ac.massey.httpmockskeletons.scripts.datapreparator.attributebasedlearningtrainingdatagenerator;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import java.io.File;

/**
 * this class generates arff file based on csv
 * to be used as input to weka
 *
 * @author thilinibhagya
 */

public class CSVToARFFConverter {

    static org.apache.log4j.Logger LOGGER = Logging.getLogger(CSVToARFFConverter.class);

    public static void convertCSVDataFileToARFFFormat(String fileName) throws Exception {
        LOGGER.info("Converting extracted data into ARFF format");
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
        LOGGER.info("ARFF file generated");
    }
}
