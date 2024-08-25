package uoa.lavs.legacy.mainframe.simulator.nitrite;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;

import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerAddress;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerAddresses;

import java.util.HashMap;

public final class LoadCustomerAddressesProcessor extends BaseCustomerItemsProcessor {
    public LoadCustomerAddressesProcessor(Nitrite database) {
        super(database, "addresses");
    }

    @Override
    protected void copyItemData(String key, HashMap<String, String> data, Integer number, Document doc) {
        data.put(
                String.format(LoadCustomerAddresses.Fields.NUMBER, number),
                key);
        data.put(
                String.format(LoadCustomerAddresses.Fields.TYPE, number),
                doc.get(LoadCustomerAddress.Fields.TYPE, String.class));
        data.put(
                String.format(LoadCustomerAddresses.Fields.IS_PRIMARY, number),
                doc.get(LoadCustomerAddress.Fields.IS_PRIMARY, String.class));
    }
}
