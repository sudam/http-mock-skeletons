package nz.ac.massey.httpmockskeletons.scripts.modelevaluator.attributebasedlearningmodeltester;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer.NominalFilter;
import org.apache.log4j.Logger;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.rules.PART;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.util.Random;

/**
 * this class test models with PART classification algorithm
 *
 * @author thilini bhagya
 */

public class PARTModelTester {
    static Logger LOGGER = Logging.getLogger(C45ModelTester.class);

    public static void processWithPART(String dataFileName, int indexToLearn, int[] indicesToRemove) throws Exception {
        try {
            Instances trainingDataSet = new ConverterUtils.DataSource(dataFileName + ".arff").getDataSet();

            LOGGER.info("Generating PART model for " + trainingDataSet.attribute(indexToLearn).toString().split("@attribute ")[1]);

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
            PART classifier = new PART();
            classifier.buildClassifier(newTrainingDataSet);
            System.out.println(classifier);

            // Evaluate using cross validation
            LOGGER.info("Evaluating the model using 10-fold cross validation");
            Evaluation eval = new Evaluation(newTrainingDataSet);
            eval.crossValidateModel(classifier, newTrainingDataSet, 10, new Random(1));
            System.out.println(eval.toSummaryString("\nCross Validation Results:\n===========\n", false));
            System.out.println(eval.toClassDetailsString());
        }

        // Handle exception if selected target is unary
        catch (Exception e) {
            LOGGER.warn("Exception writing details to log \n", e);
        }
    }
}
