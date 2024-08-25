package uoa.lavs.mainframe.simulator;

import org.junit.jupiter.api.Test;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static uoa.lavs.legacy.mainframe.MessageErrorStatus.UNKNOWN_MESSAGE;

class NitrateConnectionTests {

    @Test
    public void handlesUnknownMessage() throws IOException {
        // Arrange
        Connection connection = new NitriteConnection();

        // Act
        Response response = connection.send(new Request(-999));
        connection.close();

        // Assert
        Status status = response.getStatus();
        assertAll(
                () -> assertEquals(UNKNOWN_MESSAGE.getCode(), status.getErrorCode()),
                () -> assertEquals(UNKNOWN_MESSAGE.getMessage(), status.getErrorMessage())
        );
    }

}