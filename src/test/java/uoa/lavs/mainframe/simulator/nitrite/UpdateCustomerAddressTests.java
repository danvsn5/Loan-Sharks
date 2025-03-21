package uoa.lavs.mainframe.simulator.nitrite;

import org.junit.jupiter.api.Test;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomerAddress;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;

import static org.junit.jupiter.api.Assertions.*;
import static uoa.lavs.legacy.mainframe.MessageErrorStatus.CUSTOMER_NOT_FOUND;

class UpdateCustomerAddressTests {
    @Test
    public void handlesMissingCustomer() {
        // Arrange
        Connection connection = new NitriteConnection();
        UpdateCustomerAddress message = new UpdateCustomerAddress();
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
    public void handlesNewAddress() {
        // Arrange
        Connection connection = new NitriteConnection(
                DatabaseHelper.generateDefaultDatabase());
        UpdateCustomerAddress message = new UpdateCustomerAddress();
        setMessageParameters(message);
        message.setNumber(2);

        // Act
        Status status = message.send(connection);

        // Assert
        assertExpectedProperties(status, message);
    }

    @Test
    public void handlesAddressUpdate() {
        // Arrange
        Connection connection = new NitriteConnection(
                DatabaseHelper.generateDefaultDatabase());
        UpdateCustomerAddress message = new UpdateCustomerAddress();
        setMessageParameters(message);

        // Act
        Status status = message.send(connection);

        // Assert
        assertExpectedProperties(status, message);
    }

    private static void assertExpectedProperties(Status status, UpdateCustomerAddress message) {
        assertAll(
                () -> assertTrue(status.getWasSuccessful()),
                () -> assertEquals("Business", message.getTypeFromServer()),
                () -> assertEquals("99 Here Road", message.getLine1FromServer()),
                () -> assertEquals("MiddleOf", message.getLine2FromServer()),
                () -> assertEquals("Nowhere", message.getSuburbFromServer()),
                () -> assertEquals("Sydney", message.getCityFromServer()),
                () -> assertEquals("9876", message.getPostCodeFromServer()),
                () -> assertEquals("Australia", message.getCountryFromServer()),
                () -> assertFalse( message.getIsPrimaryFromServer()),
                () -> assertFalse( message.getIsMailingFromServer())
        );
    }

    private static void setMessageParameters(UpdateCustomerAddress message) {
        message.setCustomerId("123");
        message.setNumber(1);
        message.setType("Business");
        message.setLine1("99 Here Road");
        message.setLine2("MiddleOf");
        message.setSuburb("Nowhere");
        message.setCity("Sydney");
        message.setPostCode("9876");
        message.setCountry("Australia");
        message.setIsPrimary(false);
        message.setIsMailing(false);
    }

}