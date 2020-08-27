package nz.ac.massey.httpmockskeletons.scripts.datapreparator.rawdatapreprocessor;
import jdk.jfr.events.FileReadEvent;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RawJSONToCSVConverterForGHTrafficTest {
    @Test
    public void testFileWriter() throws ParseException, org.json.simple.parser.ParseException, IOException {
        RawJSONToCSVConverterForGHTraffic underTest = new RawJSONToCSVConverterForGHTraffic();

        String fileName=System.getProperty("user.dir") + File.separator + "src/resources/ghtraffic-preprocessed.csv";
        String xmlFileName="ghtraffic-S-2.0.0.json";

        File file = underTest.fileWriter(fileName, xmlFileName);

        assertTrue(file.exists());
    }
}
