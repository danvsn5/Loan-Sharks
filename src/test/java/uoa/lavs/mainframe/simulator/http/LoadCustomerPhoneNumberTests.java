package uoa.lavs.mainframe.simulator.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerPhoneNumber;
import uoa.lavs.legacy.mainframe.simulator.HttpConnection;

import static org.junit.jupiter.api.Assertions.*;
import static uoa.lavs.legacy.mainframe.MessageErrorStatus.*;

@EnabledIfEnvironmentVariable(named = "LAVS_ENVIRONMENT", matches = "http_test")
class LoadCustomerPhoneNumberTests {
    @Test
    public void handlesMissingCustomer() {
        // Arrange
        Connection connection = new HttpConnection(Constants.BASE_URL);
        LoadCustomerPhoneNumber message = new LoadCustomerPhoneNumber();
        message.setCustomerId("0000021");
        message.setNumber(1);

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
        Connection connection = new HttpConnection(Constants.BASE_URL);
        Request request = new Request(LoadCustomerPhoneNumber.REQUEST_CODE);
        request.setValue(LoadCustomerPhoneNumber.Fields.CUSTOMER_ID, id);

        // Act
        Response response = connection.send(request);

        // Assert
        Status status = response.getStatus();
        assertAll(
                () -> assertEquals(INVALID_REQUEST_CUSTOMER_ID.getCode(), status.getErrorCode())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678901", "abc"})
    @NullAndEmptySource
    public void handlesInvalidItemNumbers(String id) {
        // Arrange - these tests need to be low-level as we are bypassing the validation provided by the message
        Connection connection = new HttpConnection(Constants.BASE_URL);
        Request request = new Request(LoadCustomerPhoneNumber.REQUEST_CODE);
        request.setValue(LoadCustomerPhoneNumber.Fields.CUSTOMER_ID, "123");
        request.setValue(LoadCustomerPhoneNumber.Fields.NUMBER, id);

        // Act
        Response response = connection.send(request);

        // Assert
        Status status = response.getStatus();
        assertAll(
                () -> assertEquals(INVALID_REQUEST_NUMBER.getCode(), status.getErrorCode())
        );
    }

    @Test
    public void handlesMissingCustomerEmail() {
        // Arrange
        Connection connection = new HttpConnection(Constants.BASE_URL);
        LoadCustomerPhoneNumber message = new LoadCustomerPhoneNumber();
        message.setCustomerId("0000022");
        message.setNumber(2);

        // Act
        Status status = message.send(connection);

        // Assert
        assertAll(
                () -> assertEquals(CUSTOMER_PHONE_NUMBER_NOT_FOUND.getCode(), status.getErrorCode()),
                () -> assertEquals(CUSTOMER_PHONE_NUMBER_NOT_FOUND.getMessage(), status.getErrorMessage())
        );
    }

    @Test
    public void handlesExistingCustomerEmail() {
        // Arrange
        Connection connection = new HttpConnection(Constants.BASE_URL);
        LoadCustomerPhoneNumber message = new LoadCustomerPhoneNumber();
        message.setCustomerId("0000022");
        message.setNumber(1);

        // Act
        Status status = message.send(connection);

        // Assert
        assertAll(
                () -> assertTrue(status.getWasSuccessful()),
                () -> assertEquals("Mobile", message.getTypeFromServer()),
                () -> assertEquals("+20", message.getPrefixFromServer()),
                () -> assertEquals("123-4567", message.getPhoneNumberFromServer()),
                () -> assertTrue(message.getIsPrimaryFromServer()),
                () -> assertTrue(message.getCanSendTxtFromServer())
        );
    }
}