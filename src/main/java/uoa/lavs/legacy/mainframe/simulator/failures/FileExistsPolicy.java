package uoa.lavs.legacy.mainframe.simulator.failures;

import java.io.File;

import uoa.lavs.legacy.mainframe.simulator.IntermittentFailurePolicy;

public class FileExistsPolicy implements IntermittentFailurePolicy {

    private final String filePath;

    public FileExistsPolicy(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean canSend(boolean checkOnly) {
        File file = new File(filePath);
        return !file.exists();
    }
}
