package uoa.lavs.legacy.mainframe;

import java.io.IOException;

// a connection to a mainframe
public interface Connection {
    // sends some data to the mainframe and receives a response
    Response send(Request request);

    // close the connection and clean up any resources
    void close() throws IOException;
}
