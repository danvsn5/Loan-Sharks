package uoa.lavs.mainframe.simulator.nitrite;

import org.junit.jupiter.api.Test;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomer;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static uoa.lavs.legacy.mainframe.MessageErrorStatus.CUSTOMER_NOT_FOUND;

class UpdateCustomerTests {
    private static void setMessageParameters(UpdateCustomer message, String id) {
        message.setCustomerId(id);
        message.setName("Jane Doe");
        message.setTitle("Mrs");
        message.setOccupation("Test Mannequin");
        message.setCitizenship("Australia");
        message.setVisa("Automatic");
        message.setDateofBirth(LocalDate.of(1998, 7, 6));
    }

    @Test
    public void handlesMissingCustomer() {
        // Arrange
        Connection connection = new NitriteConnection();
        UpdateCustomer message = new UpdateCustomer();
        setMessageParameters(message, "789");

        // Act
        Status status = message.send(connection);

        // Assert
        assertAll(
                () -> assertEquals(CUSTOMER_NOT_FOUND.getCode(), status.getErrorCode()),
                () -> assertEquals(CUSTOMER_NOT_FOUND.getMessage(), status.getErrorMessage())
        );
    }

    @Test
    public void handlesNewCustomer() {
        // Arrange
        Connection connection = new NitriteConnection(
                DatabaseHelper.generateDefaultDatabase());
        UpdateCustomer message = new UpdateCustomer();
        setMessageParameters(message, null);

        // Act
        Status status = message.send(connection);

        // Assert
        assertAll(
                () -> assertTrue(status.getWasSuccessful()),
                () -> assertEquals("125", message.getCustomerIdFromServer()),
                () -> assertEquals("Jane Doe", message.getNameFromServer()),
                () -> assertEquals("Mrs", message.getTitleFromServer()),
                () -> assertEquals("Test Mannequin", message.getOccupationFromServer()),
                () -> assertEquals("Automatic", message.getVisaFromServer()),
                () -> assertEquals("Australia", message.getCitizenshipFromServer()),
                () -> assertEquals("Active", message.getStatusFromServer()),
                () -> assertEquals(LocalDate.of(1998, 7, 6), message.getDateofBirthFromServer())
        );
    }

    @Test
    public void handlesExistingCustomer() {
        // Arrange
        Connection connection = new NitriteConnection(
                DatabaseHelper.generateDefaultDatabase());
        UpdateCustomer message = new UpdateCustomer();
        setMessageParameters(message, "123");

        // Act
        Status status = message.send(connection);

        // Assert
        assertAll(
                () -> assertTrue(status.getWasSuccessful()),
                () -> assertEquals("123", message.getCustomerIdFromServer()),
                () -> assertEquals("Jane Doe", message.getNameFromServer()),
                () -> assertEquals("Mrs", message.getTitleFromServer()),
                () -> assertEquals("Test Mannequin", message.getOccupationFromServer()),
                () -> assertEquals("Automatic", message.getVisaFromServer()),
                () -> assertEquals("Australia", message.getCitizenshipFromServer()),
                () -> assertEquals("Active", message.getStatusFromServer()),
                () -> assertEquals(LocalDate.of(1998, 7, 6), message.getDateofBirthFromServer())
        );
    }

}