package uoa.lavs.legacy.mainframe.messages.customer;

import uoa.lavs.legacy.mainframe.Message;

public interface UpdateCustomerChildMessage extends Message {
    Integer getNumberFromServer();

    void setCustomerId(String value) throws IllegalArgumentException;

    void setNumber(Integer value);
}
