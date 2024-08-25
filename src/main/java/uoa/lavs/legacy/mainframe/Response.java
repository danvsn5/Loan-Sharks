package uoa.lavs.legacy.mainframe;

import java.util.HashMap;

// a response from the mainframe
public final class Response {
    private HashMap<String, String> rawData;
    private Status status;

    public Response(Status status, HashMap<String, String> rawData) {
        this.status = status;
        this.rawData = rawData;
    }

    // gets a data value
    public String getValue(String key) {
        return rawData.get(key);
    }

    // gets the status of the transaction.
    public Status getStatus() {
        return status;
    }
}
