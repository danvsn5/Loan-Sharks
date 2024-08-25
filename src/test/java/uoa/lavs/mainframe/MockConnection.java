package uoa.lavs.mainframe;

import java.io.IOException;
import java.util.HashMap;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;
import uoa.lavs.legacy.mainframe.Status;

public class MockConnection implements Connection {
    private Request request;
    private Response response;

    public MockConnection() {
        response =  new Response(
                new Status(1),
                new HashMap<>());
    }

    public MockConnection(Response response) {
        this.response = response;
    }

    public MockConnection(String responseData) {
        HashMap<String,String> map = new HashMap<>();
        String[] data = responseData.split(",");
        for (String keyValuePair : data) {
            String[] keyValue = keyValuePair.split("=");
            map.put(keyValue[0],keyValue[1]);
        }

        response =  new Response(
                new Status(1),
                map);
    }

    @Override
    public Response send(Request request) {
        this.request = request;
        return response;
    }

    @Override
    public void close() throws IOException {
        // nothing to do here - just ignore this method
    }

    public Request getLastRequest() {
        return request;
    }
}
