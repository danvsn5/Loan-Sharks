package uoa.lavs.mainframe.simulator.nitrite;

import org.junit.jupiter.api.Test;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomerEmail;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;

import static org.junit.jupiter.api.Assertions.*;
import static uoa.lavs.legacy.mainframe.MessageErrorStatus.CUSTOMER_NOT_FOUND;

class UpdateCustomerEmailTests {
    @Test
    public void handlesMissingCustomer() {
        // Arrange
        Connection connection = new NitriteConnection();
        UpdateCustomerEmail message = new UpdateCustomerEmail();
        message.setCustomerId("123");

        // Act
        Status status = message.send(connection);

        // Assert
        assertAll(
                () -> assertEquals(CUSTOMER_NOT_FOUND.getCode(), status.getErrorCode()),
                () -> assertEquals(CUSTOMER_NOT_FOUND.getMessage(), status.getErrorMessage())
        );
    }

    @Test
    public void handlesNewEmail() {
        // Arrange
        Connection connection = new NitriteConnection(
                DatabaseHelper.generateDefaultDatabase());
        UpdateCustomerEmail message = new UpdateCustomerEmail();
        setMessageParameters(message);
        message.setNumber(2);

        // Act
        Status status = message.send(connection);

        // Assert
        assertExpectedProperties(status, message);
    }

    @Test
    public void handlesEmailUpdate() {
        // Arrange
        Connection connection = new NitriteConnection(
                DatabaseHelper.generateDefaultDatabase());
        UpdateCustomerEmail message = new UpdateCustomerEmail();
        setMessageParameters(message);

        // Act
        Status status = message.send(connection);

        // Assert
        assertExpectedProperties(status, message);
    }

    private static void assertExpectedProperties(Status status, UpdateCustomerEmail message) {
        assertAll(
                () -> assertTrue(status.getWasSuccessful()),
                () -> assertEquals("me@nowhere.com", message.getAddressFromServer()),
                () -> assertFalse( message.getIsPrimaryFromServer())
        );
    }

    private static void setMessageParameters(UpdateCustomerEmail message) {
        message.setCustomerId("123");
        message.setNumber(1);
        message.setAddress("me@nowhere.com");
        message.setIsPrimary(false);
    }
}