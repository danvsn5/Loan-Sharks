package uoa.lavs.legacy.mainframe.simulator;

import static uoa.lavs.legacy.mainframe.MessageErrorStatus.NETWORK_FAILURE_UNAVAILABLE;

import java.io.IOException;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.ConnectionWithState;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;
import uoa.lavs.legacy.mainframe.simulator.failures.RandomPolicy;

public class IntermittentConnection implements ConnectionWithState {

    private final Connection conn;
    private final IntermittentFailurePolicy policy;
    private long transactionId = 0;

    public IntermittentConnection(Connection conn) {
        this.conn = conn;
        policy = new RandomPolicy();
    }

    public IntermittentConnection(Connection conn, IntermittentFailurePolicy policy) {
        this.conn = conn;
        this.policy = policy;
    }

    @Override
    public Response send(Request request) {
        if (!policy.canSend(false)) {
            return NETWORK_FAILURE_UNAVAILABLE.generateEmptyResponse(++transactionId);
        }

        Response response = conn.send(request);
        response.getStatus().setTransactionId(transactionId);
        return response;
    }

    @Override
    public void close() throws IOException {
        conn.close();
    }

    @Override
    public boolean isConnected() {
        if (!policy.canSend(true)) return false;
        if (conn instanceof ConnectionWithState) {
            return ((ConnectionWithState) conn).isConnected();
        }

        return true;
    }
}
