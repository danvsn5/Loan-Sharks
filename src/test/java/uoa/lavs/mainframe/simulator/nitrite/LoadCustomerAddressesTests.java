package uoa.lavs.mainframe.simulator.nitrite;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerAddresses;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;

import static org.junit.jupiter.api.Assertions.*;
import static uoa.lavs.legacy.mainframe.MessageErrorStatus.CUSTOMER_NOT_FOUND;
import static uoa.lavs.legacy.mainframe.MessageErrorStatus.INVALID_REQUEST_CUSTOMER_ID;

class LoadCustomerAddressesTests {
    @Test
    public void handlesMissingCustomer() {
        // Arrange
        Connection connection = new NitriteConnection();
        LoadCustomerAddresses message = new LoadCustomerAddresses();
        message.setCustomerId("123");

        // Act
        Status status = message.send(connection);

        // Assert
        assertAll(
                () -> assertEquals(CUSTOMER_NOT_FOUND.getCode(), status.getErrorCode()),
                () -> assertEquals(CUSTOMER_NOT_FOUND.getMessage(), status.getErrorMessage())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678901", "abc"})
    @NullAndEmptySource
    public void handlesInvalidCustomerNumbers(String id) {
        // Arrange - these tests need to be low-level as we are bypassing the validation provided by the message
        Connection connection = new NitriteConnection();
        Request request = new Request(LoadCustomerAddresses.REQUEST_CODE);
        request.setValue(LoadCustomerAddresses.Fields.CUSTOMER_ID, id);

        // Act
        Response response = connection.send(request);

        // Assert
        Status status = response.getStatus();
        assertAll(
                () -> assertEquals(INVALID_REQUEST_CUSTOMER_ID.getCode(), status.getErrorCode()),
                () -> assertEquals(INVALID_REQUEST_CUSTOMER_ID.getMessage(), status.getErrorMessage())
        );
    }

    @Test
    public void handlesExistingCustomerAddress() {
        // Arrange
        Connection connection = new NitriteConnection(
                DatabaseHelper.generateDefaultDatabase());
        LoadCustomerAddresses message = new LoadCustomerAddresses();
        message.setCustomerId("123");

        // Act
        Status status = message.send(connection);

        // Assert
        assertAll(
                () -> assertTrue(status.getWasSuccessful()),
                () -> assertEquals(1, message.getCountFromServer()),
                () -> assertEquals("Home", message.getTypeFromServer(1)),
                () -> assertTrue(message.getIsPrimaryFromServer(1)),
                () -> assertTrue(message.getIsMailingFromServer(1))
        );
    }
}