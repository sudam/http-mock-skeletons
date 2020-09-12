package nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer;

import nz.ac.massey.httpmockskeletons.scripts.Logging;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.*;
import java.awt.*;
import org.apache.log4j.*;

/**
 * this class trains models with J48 classification algorithm
 *
 * @author thilini bhagya
 */

public class C45ModelTrainer {
    static Logger LOGGER = Logging.getLogger(C45ModelTrainer.class);

    public static void processWithC45(String dataFileName, int indexToLearn, int[] indicesToRemove) throws Exception {
        try {
            Instances trainingDataSet = new ConverterUtils.DataSource(dataFileName + ".arff").getDataSet();

            LOGGER.info("Generating C4.5 model for " + trainingDataSet.attribute(indexToLearn).toString().split("@attribute ")[1]);

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
            J48 classifier = new J48();
            classifier.buildClassifier(newTrainingDataSet);
            System.out.println(classifier);

            // Print model
            final javax.swing.JFrame jf = new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
            jf.setSize(2000, 1000);
            jf.getContentPane().setLayout(new BorderLayout());
            TreeVisualizer tv = new TreeVisualizer(null, classifier.graph(), new PlaceNode2());
            jf.getContentPane().add(tv, BorderLayout.CENTER);
            jf.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    jf.dispose();
                }
            });
            jf.setVisible(true);
            tv.fitToScreen();
        }

        // Handle exception if selected target is unary
        catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
