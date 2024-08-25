package uoa.lavs.legacy.mainframe.simulator;

import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;

public interface MessageProcessor {
    Response process(Request request, long transactionId);
}
