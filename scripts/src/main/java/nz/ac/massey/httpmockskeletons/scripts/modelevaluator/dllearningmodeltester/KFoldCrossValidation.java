package nz.ac.massey.httpmockskeletons.scripts.modelevaluator.dllearningmodeltester;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import org.dllearner.core.AbstractCELA;
import org.dllearner.core.AbstractLearningProblem;
import org.dllearner.core.AbstractReasonerComponent;
import org.dllearner.core.ComponentInitException;
import org.dllearner.learningproblems.Heuristics;
import org.dllearner.learningproblems.PosNegLP;
import org.dllearner.learningproblems.PosOnlyLP;
import org.dllearner.utilities.Files;
import org.dllearner.utilities.Helper;
import org.dllearner.utilities.owl.OWLClassExpressionUtils;
import org.dllearner.utilities.statistics.Stat;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import com.google.common.collect.Sets;

/**
 * this class is used instead of original cross validation script
 * implemented in DL-Learner
 *
 * to perform cross validation while calculating predictive accuracy together with precision and recall
 *
 * @author thilini bhagya
 */

public class KFoldCrossValidation {
    // statistical values
    protected Stat runtime = new Stat();
    protected Stat accuracy = new Stat();
    protected Stat length = new Stat();
    protected Stat accuracyTraining = new Stat();
    protected Stat fMeasure = new Stat();
    protected Stat fMeasureTraining = new Stat();
    protected static boolean writeToFile = false;
    protected static File outputFile;

    protected Stat rcall = new Stat();
    protected Stat rcallTraining = new Stat();
    protected Stat pcision = new Stat();
    protected Stat pcisionTraining = new Stat();

    public KFoldCrossValidation(AbstractCELA la, AbstractLearningProblem lp, AbstractReasonerComponent rs, int folds, boolean leaveOneOut) {

        DecimalFormat df = new DecimalFormat();

        // the training and test sets used later on
        List<Set<OWLIndividual>> trainingSetsPos = new LinkedList<Set<OWLIndividual>>();
        List<Set<OWLIndividual>> trainingSetsNeg = new LinkedList<Set<OWLIndividual>>();
        List<Set<OWLIndividual>> testSetsPos = new LinkedList<Set<OWLIndividual>>();
        List<Set<OWLIndividual>> testSetsNeg = new LinkedList<Set<OWLIndividual>>();

        // get examples and shuffle them too
        Set<OWLIndividual> posExamples;
        Set<OWLIndividual> negExamples;
        if (lp instanceof PosNegLP) {
            posExamples = ((PosNegLP) lp).getPositiveExamples();
            negExamples = ((PosNegLP) lp).getNegativeExamples();
        } else if (lp instanceof PosOnlyLP) {
            posExamples = ((PosNegLP) lp).getPositiveExamples();
            negExamples = new HashSet<>();
        } else {
            throw new IllegalArgumentException("Only PosNeg and PosOnly learning problems are supported");
        }
        List<OWLIndividual> posExamplesList = new LinkedList<OWLIndividual>(posExamples);
        List<OWLIndividual> negExamplesList = new LinkedList<OWLIndividual>(negExamples);
        Collections.shuffle(posExamplesList, new Random(1));
        Collections.shuffle(negExamplesList, new Random(2));

        // sanity check whether nr. of folds makes sense for this benchmark
        if (!leaveOneOut && (posExamples.size() < folds && negExamples.size() < folds)) {
            System.out.println("The number of folds is higher than the number of "
                    + "positive/negative examples. This can result in empty test sets. Exiting.");
            System.exit(0);
        }

        if (leaveOneOut) {
            // note that leave-one-out is not identical to k-fold with
            // k = nr. of examples in the current implementation, because
            // with n folds and n examples there is no guarantee that a fold
            // is never empty (this is an implementation issue)
            int nrOfExamples = posExamples.size() + negExamples.size();
            for (int i = 0; i < nrOfExamples; i++) {
                // ...
            }
            System.out.println("Leave-one-out not supported yet.");
            System.exit(1);
        } else {
            // calculating where to split the sets, ; note that we split
            // positive and negative examples separately such that the
            // distribution of positive and negative examples remains similar
            // (note that there are better but more complex ways to implement this,
            // which guarantee that the sum of the elements of a fold for pos
            // and neg differs by at most 1 - it can differ by 2 in our implementation,
            // e.g. with 3 folds, 4 pos. examples, 4 neg. examples)
            int[] splitsPos = calculateSplits(posExamples.size(), folds);
            int[] splitsNeg = calculateSplits(negExamples.size(), folds);

            // calculating training and test sets
            for (int i = 0; i < folds; i++) {
                Set<OWLIndividual> testPos = getTestingSet(posExamplesList, splitsPos, i);
                Set<OWLIndividual> testNeg = getTestingSet(negExamplesList, splitsNeg, i);
                testSetsPos.add(i, testPos);
                testSetsNeg.add(i, testNeg);
                trainingSetsPos.add(i, getTrainingSet(posExamples, testPos));
                trainingSetsNeg.add(i, getTrainingSet(negExamples, testNeg));
            }

        }

        // run the algorithm
        for (int currFold = 0; currFold < folds; currFold++) {

            Set<String> pos = Helper.getStringSet(trainingSetsPos.get(currFold));
            Set<String> neg = Helper.getStringSet(trainingSetsNeg.get(currFold));
            if (lp instanceof PosNegLP) {
                ((PosNegLP) lp).setPositiveExamples(trainingSetsPos.get(currFold));
                ((PosNegLP) lp).setNegativeExamples(trainingSetsNeg.get(currFold));
            } else if (lp instanceof PosOnlyLP) {
                ((PosOnlyLP) lp).setPositiveExamples(new TreeSet<OWLIndividual>(trainingSetsPos.get(currFold)));
            }


            try {
                lp.init();
                la.init();
            } catch (ComponentInitException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            long algorithmStartTime = System.nanoTime();
            la.start();
            long algorithmDuration = System.nanoTime() - algorithmStartTime;
            runtime.addNumber(algorithmDuration / (double) 1000000000);

            OWLClassExpression concept = la.getCurrentlyBestDescription();

            Set<OWLIndividual> tmp = rs.hasType(concept, testSetsPos.get(currFold));
            Set<OWLIndividual> tmp2 = Sets.difference(testSetsPos.get(currFold), tmp);
            Set<OWLIndividual> tmp3 = rs.hasType(concept, testSetsNeg.get(currFold));

            outputWriter("test set errors pos: " + tmp2);
            outputWriter("test set errors neg: " + tmp3);

            // calculate training accuracies
            int trainingCorrectPosClassified = getCorrectPosClassified(rs, concept, trainingSetsPos.get(currFold));
            int trainingCorrectNegClassified = getCorrectNegClassified(rs, concept, trainingSetsNeg.get(currFold));
            int trainingCorrectExamples = trainingCorrectPosClassified + trainingCorrectNegClassified;
            double trainingAccuracy = 100 * ((double) trainingCorrectExamples / (trainingSetsPos.get(currFold).size() +
                    trainingSetsNeg.get(currFold).size()));
            accuracyTraining.addNumber(trainingAccuracy);

            // calculate test accuracies
            int correctPosClassified = getCorrectPosClassified(rs, concept, testSetsPos.get(currFold));
            int correctNegClassified = getCorrectNegClassified(rs, concept, testSetsNeg.get(currFold));
            int correctExamples = correctPosClassified + correctNegClassified;
            double currAccuracy = 100 * ((double) correctExamples / (testSetsPos.get(currFold).size() +
                    testSetsNeg.get(currFold).size()));
            accuracy.addNumber(currAccuracy);

            int negAsPosTraining = rs.hasType(concept, trainingSetsNeg.get(currFold)).size();
            double precisionTraining = trainingCorrectPosClassified + negAsPosTraining == 0 ? 0 : trainingCorrectPosClassified / (double) (trainingCorrectPosClassified + negAsPosTraining);

            // calculate training precision
            double precisionTrainingD = 100 * precisionTraining;
            pcisionTraining.addNumber(precisionTrainingD);

            // calculate training recall
            double recallTraining = trainingCorrectPosClassified / (double) trainingSetsPos.get(currFold).size();
            double recallTrainingD = 100 * recallTraining;
            rcallTraining.addNumber(recallTrainingD);

            // calculate training F-Score
            fMeasureTraining.addNumber(100 * Heuristics.getFScore(recallTraining, precisionTraining));

            // calculate test precision
            int negAsPos = rs.hasType(concept, testSetsNeg.get(currFold)).size();
            double precision = correctPosClassified + negAsPos == 0 ? 0 : correctPosClassified / (double) (correctPosClassified + negAsPos);
            double precisionD = 100 * precision;
            pcision.addNumber(precisionD);

            // calculate test recall
            double recall = correctPosClassified / (double) testSetsPos.get(currFold).size();
            double recallD = 100 * recall;
            rcall.addNumber(recallD);

            // calculate test F-Score
            fMeasure.addNumber(100 * Heuristics.getFScore(recall, precision));

            length.addNumber(OWLClassExpressionUtils.getLength(concept));

            outputWriter("fold " + currFold + ":");
            outputWriter("  training: " + pos.size() + " positive and " + neg.size() + " negative examples");
            outputWriter("  testing: " + correctPosClassified + "/" + testSetsPos.get(currFold).size() + " correct positives, "
                    + correctNegClassified + "/" + testSetsNeg.get(currFold).size() + " correct negatives");
            outputWriter("  concept: " + concept);
            outputWriter("  accuracy: " + df.format(currAccuracy) + "% (" + df.format(trainingAccuracy) + "% on training set)");
            outputWriter("  precision: " + df.format(precisionD) + "% (" + df.format(precisionTrainingD) + "% on training set)");
            outputWriter("  recall: " + df.format(recallD) + "% (" + df.format(recallTrainingD) + "% on training set)");
            outputWriter("  length: " + df.format(OWLClassExpressionUtils.getLength(concept)));
            outputWriter("  runtime: " + df.format(algorithmDuration / (double) 1000000000) + "s");

        }

        outputWriter("");
        outputWriter("Finished " + folds + "-folds cross-validation.");
        outputWriter("runtime: " + statOutput(df, runtime, "s"));
        outputWriter("length: " + statOutput(df, length, ""));
        outputWriter("F-Measure on training set: " + statOutput(df, fMeasureTraining, "%"));
        outputWriter("F-Measure: " + statOutput(df, fMeasure, "%"));
        outputWriter("predictive accuracy on training set: " + statOutput(df, accuracyTraining, "%"));
        outputWriter("predictive accuracy: " + statOutput(df, accuracy, "%"));
        outputWriter("precision on training set: " + statOutput(df, pcisionTraining, "%"));
        outputWriter("precision: " + statOutput(df, pcision, "%"));
        outputWriter("recall on training set: " + statOutput(df, rcallTraining, "%"));
        outputWriter("recall: " + statOutput(df, rcall, "%"));


    }

    protected int getCorrectPosClassified(AbstractReasonerComponent rs, OWLClassExpression concept, Set<OWLIndividual> testSetPos) {
        return rs.hasType(concept, testSetPos).size();
    }

    protected int getCorrectNegClassified(AbstractReasonerComponent rs, OWLClassExpression concept, Set<OWLIndividual> testSetNeg) {
        return testSetNeg.size() - rs.hasType(concept, testSetNeg).size();
    }

    public static Set<OWLIndividual> getTestingSet(List<OWLIndividual> examples, int[] splits, int fold) {
        int fromIndex;
        // we either start from 0 or after the last fold ended
        if (fold == 0)
            fromIndex = 0;
        else
            fromIndex = splits[fold - 1];
        // the split corresponds to the ends of the folds
        int toIndex = splits[fold];

//		System.out.println("from " + fromIndex + " to " + toIndex);

        Set<OWLIndividual> testingSet = new HashSet<>();
        // +1 because 2nd element is exclusive in subList method
        testingSet.addAll(examples.subList(fromIndex, toIndex));
        return testingSet;
    }

    public static Set<OWLIndividual> getTrainingSet(Set<OWLIndividual> examples, Set<OWLIndividual> testingSet) {
        return new TreeSet<>(Sets.difference(examples, testingSet));
    }

    // takes nr. of examples and the nr. of folds for this examples;
    // returns an array which says where each fold ends, i.e.
    // splits[i] is the index of the last element of fold i in the examples
    public static int[] calculateSplits(int nrOfExamples, int folds) {
        int[] splits = new int[folds];
        for (int i = 1; i <= folds; i++) {
            // we always round up to the next integer
            splits[i - 1] = (int) Math.ceil(i * nrOfExamples / (double) folds);
        }
        return splits;
    }

    public static String statOutput(DecimalFormat df, Stat stat, String unit) {
        String str = "av. " + df.format(stat.getMean()) + unit;
        str += " (deviation " + df.format(stat.getStandardDeviation()) + unit + "; ";
        str += "min " + df.format(stat.getMin()) + unit + "; ";
        str += "max " + df.format(stat.getMax()) + unit + ")";
        return str;
    }

    public Stat getLength() {
        return length;
    }

    protected void outputWriter(String output) {
        if (writeToFile) {
            Files.appendToFile(outputFile, output + "\n");
            System.out.println(output);
        } else {
            System.out.println(output);
        }

    }
}
