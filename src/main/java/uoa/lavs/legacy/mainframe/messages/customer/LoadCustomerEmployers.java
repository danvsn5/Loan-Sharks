package uoa.lavs.legacy.mainframe.messages.customer;

import uoa.lavs.legacy.mainframe.*;

public class LoadCustomerEmployers implements Message, MessageDescription {
    public static final int REQUEST_CODE = 1005;
    private Response response;
    private String customerId;

    public class Fields {
        public static final String[] INPUT = {"id"};
        public static final String[] OUTPUT = {"[01].name", "[01].number", "[02].name", "[02].number", "[03].name", "[03].number", "[04].name", "[04].number", "[05].name", "[05].number", "[06].name", "[06].number", "[07].name", "[07].number", "[08].name", "[08].number", "[09].name", "[09].number", "[10].name", "[10].number", "[11].name", "[11].number", "[12].name", "[12].number", "[13].name", "[13].number", "[14].name", "[14].number", "[15].name", "[15].number", "count"};

        public static final String CUSTOMER_ID = "id";
        public static final String COUNT = "count";
        public static final String NUMBER = "[%02d].number";
        public static final String NAME = "[%02d].name";
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

    // gets count from server
    public Integer getCountFromServer()
     {
        String key = Fields.COUNT;
        String value = response.getValue(key);
        if (value == null) return null;
        return Integer.parseInt(value);
     }

    // gets number from server
    public Integer getNumberFromServer(Integer row)
     {
        String key = String.format(Fields.NUMBER, row);
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
