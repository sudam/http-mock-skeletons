package nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToNominal;

/**
 * this class converts int and string attributes to nominal
 *
 * @author thilini bhagya
 */

public class NominalFilter {

    public static Instances numericToNominal(Instances fileName, int index) throws Exception {

        // Convert numeric values in column 3 (status code) into nominal
        NumericToNominal convert = new NumericToNominal();
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = String.valueOf(index + 1);  // Variable in column 3 to make numeric

        convert.setOptions(options);
        convert.setInputFormat(fileName);

        Instances newData = Filter.useFilter(fileName, convert);

        return newData;
    }

    public static Instances stringToNominal(Instances fileName, int index) throws Exception {

        // Convert string values in column 3 (status code) into nominal
        StringToNominal convert = new StringToNominal();
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = String.valueOf(index + 1);  // Variable in column 3 to make numeric

        convert.setOptions(options);
        convert.setInputFormat(fileName);

        return Filter.useFilter(fileName, convert);
    }
}
