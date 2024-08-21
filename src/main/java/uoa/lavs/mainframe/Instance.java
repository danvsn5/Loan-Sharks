package uoa.lavs.mainframe;

import java.io.File;

import uoa.lavs.mainframe.simulator.IntermittentConnection;
import uoa.lavs.mainframe.simulator.NitriteConnection;
import uoa.lavs.mainframe.simulator.failures.NFailsPerMRequestsPolicy;

// implements the singleton pattern for a mainframe connection
public class Instance {
    // private constructor so that this class can only be initialized internally
    private Instance(){}

    // the path to the data file
    private static final String dataPath = "lavs-data.txt";

    // internal class to initialize the singleton, this enables lazy-loading
    // for the singleton
    private static class SingletonHelper {
        private static final Connection INSTANCE = new IntermittentConnection(
    new NitriteConnection(dataPath),
    new NFailsPerMRequestsPolicy(1, 10));       // first parameter is the number of failures, second parameter is the number of requests

    }

    // return the underlying connection
    public static Connection getConnection() {
        File file = new File(dataPath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Data file does not exist");
        }
        return SingletonHelper.INSTANCE;
    }
}
