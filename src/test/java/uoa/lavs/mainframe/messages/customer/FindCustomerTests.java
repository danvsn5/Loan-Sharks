package uoa.lavs.mainframe.messages.customer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import uoa.lavs.legacy.mainframe.*;
import uoa.lavs.legacy.mainframe.messages.customer.FindCustomer;
import uoa.lavs.mainframe.MockConnection;

import java.time.LocalDate;

class FindCustomerTests {

    @Test
    void sendSetsCustomerId() {
        // arrange
        MockConnection connection = new MockConnection();
        FindCustomer message = new FindCustomer();
        String customerId = "123456-789";

        // act
        message.setCustomerId(customerId);
        message.send(connection);

        // assert
        Request request = connection.getLastRequest();
        assertEquals(customerId, request.getValue("id"));
    }

    @Test
    void sendSetsRequestType() {
        // arrange
        MockConnection connection = new MockConnection();
        FindCustomer message = new FindCustomer();

        // act
        message.send(connection);

        // assert
        Request request = connection.getLastRequest();
        assertEquals(1001, request.getRequestType());
    }

    @Test
    void retrieveCustomerCount() {
        // arrange
        MockConnection connection = generateConnectionWithCustomers();
        FindCustomer message = new FindCustomer();

        // act
        message.send(connection);

        // assert
        assertEquals(1, message.getCustomerCountFromServer());
    }

    @Test
    void retrieveEmptyCustomerCount() {
        // arrange
        MockConnection connection = generateConnectionWithoutCustomers();
        FindCustomer message = new FindCustomer();

        // act
        message.send(connection);

        // assert
        assertEquals(0, message.getCustomerCountFromServer());
    }

    @Test
    void retrieveMissingCustomerCount() {
        // arrange
        MockConnection connection = new MockConnection();
        FindCustomer message = new FindCustomer();

        // act
        message.send(connection);

        // assert
        assertNull( message.getCustomerCountFromServer());
    }

    @Test
    void retrieveCustomerName() {
        // arrange
        MockConnection connection = generateConnectionWithCustomers();
        FindCustomer message = new FindCustomer();

        // act
        message.send(connection);

        // assert
        String value = message.getNameFromServer(1);
        assertEquals("John Doe", value);
    }

    @Test
    void retrieveMissingCustomerName() {
        // arrange
        MockConnection connection = generateConnectionWithCustomers();
        FindCustomer message = new FindCustomer();

        // act
        message.send(connection);

        // assert
        String value = message.getNameFromServer(2);
        assertNull( value);
    }

    @Test
    void retrieveCustomerId() {
        // arrange
        MockConnection connection = generateConnectionWithCustomers();
        FindCustomer message = new FindCustomer();

        // act
        message.send(connection);

        // assert
        String value = message.getIdFromServer(1);
        assertEquals("123456-789", value);
    }

    @Test
    void retrieveMissingCustomerId() {
        // arrange
        MockConnection connection = generateConnectionWithCustomers();
        FindCustomer message = new FindCustomer();

        // act
        message.send(connection);

        // assert
        String value = message.getIdFromServer(2);
        assertNull( value);
    }

    @Test
    void retrieveCustomerDob() {
        // arrange
        MockConnection connection = generateConnectionWithCustomers();
        FindCustomer message = new FindCustomer();

        // act
        message.send(connection);

        // assert
        LocalDate value = message.getDateofBirthFromServer(1);
        assertEquals(LocalDate.of(1990, 11, 12), value);
    }

    @Test
    void retrieveMissingCustomerDob() {
        // arrange
        MockConnection connection = generateConnectionWithCustomers();
        FindCustomer message = new FindCustomer();

        // act
        message.send(connection);

        // assert
        LocalDate value = message.getDateofBirthFromServer(2);
        assertNull( value);
    }

    private MockConnection generateConnectionWithCustomers() {
        MockConnection connection = new MockConnection(
                "count=1,"+
                        "[01].id=123456-789,[01].name=John Doe,[01].dob=12-11-1990");
        return connection;
    }

    private MockConnection generateConnectionWithoutCustomers() {
        MockConnection connection = new MockConnection(
                "count=0");
        return connection;
    }
}