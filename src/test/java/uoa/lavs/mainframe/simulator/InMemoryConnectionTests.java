package uoa.lavs.mainframe.simulator;

import org.junit.jupiter.api.Test;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.simulator.InMemoryConnection;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryConnectionTests {
    @Test
    public void testConnection() {
        // Arrange
        Response stored = new Response(new Status(1), new HashMap<>());
        Connection conn = new InMemoryConnection(stored);

        // Act
        Response actual = conn.send(new Request(100));

        // Assert
        assertSame(stored, actual);
    }

}