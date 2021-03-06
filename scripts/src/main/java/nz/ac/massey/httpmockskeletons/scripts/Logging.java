package nz.ac.massey.httpmockskeletons.scripts;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class implements logging system
 * @author thilini bhagya
 */

public class Logging {
    static {
        PropertyConfigurator.configure("log4j.properties");
    }

    public static Logger getLogger(Class<?> cl) {
        return Logger.getLogger(cl.getSimpleName());
    }
}
