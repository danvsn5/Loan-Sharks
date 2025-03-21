package uoa.lavs.legacy.mainframe.messages.customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import uoa.lavs.legacy.mainframe.*;

public class FindCustomer implements Message, MessageDescription {
    public static final int REQUEST_CODE = 1001;
    private Response response;
    private String customerId;

    public class Fields {
        public static final String[] INPUT = {"id"};
        public static final String[] OUTPUT = {"[01].dob", "[01].id", "[01].name", "[02].dob", "[02].id", "[02].name", "[03].dob", "[03].id", "[03].name", "[04].dob", "[04].id", "[04].name", "[05].dob", "[05].id", "[05].name", "[06].dob", "[06].id", "[06].name", "[07].dob", "[07].id", "[07].name", "[08].dob", "[08].id", "[08].name", "[09].dob", "[09].id", "[09].name", "[10].dob", "[10].id", "[10].name", "[11].dob", "[11].id", "[11].name", "[12].dob", "[12].id", "[12].name", "[13].dob", "[13].id", "[13].name", "[14].dob", "[14].id", "[14].name", "[15].dob", "[15].id", "[15].name", "count"};

        public static final String CUSTOMER_ID = "id";
        public static final String CUSTOMER_COUNT = "count";
        public static final String NAME = "[%02d].name";
        public static final String DATE_OF_BIRTH = "[%02d].dob";
        public static final String ID = "[%02d].id";
    }

    @Override
    public Status send(Connection connection) {
        Request request = new Request(REQUEST_CODE);
        if (customerId != null) request.setValue(Fields.CUSTOMER_ID, customerId);
        response = connection.send(request);
        return response.getStatus();
    }

    // sets customer id [customerId]
    public void setCustomerId(String value)
        throws IllegalArgumentException
     {
        if (value != null && value.length() > 10) {
            throw new IllegalArgumentException("customerId is too long - max length is 10");
        }
        customerId = value;
     }

    // gets customer count [count] from server
    public Integer getCustomerCountFromServer()
     {
        String key = Fields.CUSTOMER_COUNT;
        String value = response.getValue(key);
        if (value == null) return null;
        return Integer.parseInt(value);
     }

    // gets name from server
    public String getNameFromServer(Integer row)
     {
        String key = String.format(Fields.NAME, row);
        return response.getValue(key);
     }

    // gets date of birth [dob] from server
    public LocalDate getDateofBirthFromServer(Integer row)
     {
        String key = String.format(Fields.DATE_OF_BIRTH, row);
        String value = response.getValue(key);
        if (value == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(value, formatter);
     }

    // gets id from server
    public String getIdFromServer(Integer row)
     {
        String key = String.format(Fields.ID, row);
        return response.getValue(key);
     }

    @Override
    public String[] getInputFields()
    {
        return Fields.INPUT;
    }

    @Override
    public String[] getOutputFields()
    {
        return Fields.OUTPUT;
    }
}
