package uoa.lavs.legacy.mainframe.simulator;

import java.io.IOException;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;

// this is a simple connection stores and returns a single response
public class InMemoryConnection implements Connection {

    private final Response response;

    public InMemoryConnection(Response response) {
        this.response = response;
    }

    public void close() throws IOException {
    }

    @Override
    public Response send(Request request) {
        return response;
    }
}
