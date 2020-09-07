package nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import org.apache.log4j.Logger;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.rules.JRip;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.util.Random;

/**
 * this class trains models with JRip classification algorithm
 *
 * @author thilini bhagya
 */
public class RIPPERModelTrainer {
    static Logger LOGGER = Logging.getLogger(C45ModelTrainer.class);

    public static void processWithRipper(String dataFileName, int indexToLearn, int[] indicesToRemove) throws Exception {
        try {
            Instances trainingDataSet = new ConverterUtils.DataSource(dataFileName + ".arff").getDataSet();

            LOGGER.info("Generating RIPPER model for " + trainingDataSet.attribute(indexToLearn).toString().split("@attribute ")[1]);

            // Set class index to dataset
            trainingDataSet.setClassIndex(indexToLearn);

            // Turning numeric targets into nominal ones
            for (int i = 0; i < trainingDataSet.numAttributes(); i++) {
                if (trainingDataSet.attribute(i).isNumeric()) {
                    trainingDataSet = NominalFilter.numericToNominal(trainingDataSet, i);
                }
                if (trainingDataSet.attribute(i).isString()) {
                    trainingDataSet = NominalFilter.stringToNominal(trainingDataSet, i);
                }
            }

            // Remove attributes related to response properties
            Remove removeFilter = new Remove();
            removeFilter.setAttributeIndicesArray(indicesToRemove);
            removeFilter.setInputFormat(trainingDataSet);
            Instances newTrainingDataSet = Filter.useFilter(trainingDataSet, removeFilter);

            // Build model
            JRip classifier = new JRip();
            classifier.buildClassifier(newTrainingDataSet);
            System.out.println(classifier);

        }

        // Handle exception if selected target is unary
        catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}