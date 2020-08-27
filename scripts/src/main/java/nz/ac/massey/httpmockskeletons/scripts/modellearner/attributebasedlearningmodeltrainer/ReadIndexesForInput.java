package nz.ac.massey.httpmockskeletons.scripts.modellearner.attributebasedlearningmodeltrainer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * this class reads user provided file to extract index to learn (index of the target attribute)
 * and indexes of attributes which need to remove
 *
 * @author thilinibhagya
 */

public class ReadIndexesForInput {

    // Read indexes to remove
    public static int[] readIndexes(String filename) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = null;

        int[] intValues = new int[0];
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            // new int[] with "values"'s length
            intValues = new int[values.length];
            // looping over String values
            for (int i = 0; i < values.length; i++) {
                // trying to parse String value as int
                try {
                    // worked, assigning to respective int[] array position
                    intValues[i] = Integer.parseInt(values[i]);
                }
                // didn't work, moving over next String value
                // at that position int will have default value 0
                catch (NumberFormatException nfe) {
                    continue;
                }
            }
        }

        return intValues;
    }

    // Read indexes to remove - RETIRED
    public static int[] readIndexesToRemove(String filename) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = null;

        // consume first line and ignore
        br.readLine();

        int[] intValues = new int[0];
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            // new int[] with "values"'s length
            intValues = new int[values.length];
            // looping over String values
            for (int i = 0; i < values.length; i++) {
                // trying to parse String value as int
                try {
                    // worked, assigning to respective int[] array position
                    intValues[i] = Integer.parseInt(values[i]);
                }
                // didn't work, moving over next String value
                // at that position int will have default value 0
                catch (NumberFormatException nfe) {
                    continue;
                }
            }
        }
        return intValues;
    }

    // Read index of target - RETIRED
    public static int readIndexToLearn(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        return Integer.parseInt(br.readLine()); // consume first line and ignore
    }

}
