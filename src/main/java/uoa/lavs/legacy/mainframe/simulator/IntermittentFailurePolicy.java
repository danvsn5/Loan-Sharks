package uoa.lavs.legacy.mainframe.simulator;

public interface IntermittentFailurePolicy {
    boolean canSend(boolean checkOnly);
}
