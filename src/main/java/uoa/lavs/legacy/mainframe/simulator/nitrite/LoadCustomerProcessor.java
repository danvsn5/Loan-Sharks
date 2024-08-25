package uoa.lavs.legacy.mainframe.simulator.nitrite;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.DocumentCursor;
import org.dizitart.no2.collection.NitriteCollection;

import uoa.lavs.legacy.mainframe.MessageErrorStatus;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomer;

import java.util.HashMap;

import static org.dizitart.no2.filters.FluentFilter.where;

public class LoadCustomerProcessor extends BaseCustomerProcessor {

    public LoadCustomerProcessor(Nitrite database) {
        super(database);
    }

    @Override
    public Response process(Request request, long transactionId) {
        Response response = loadCustomerDocument(request, transactionId);
        if (response != null) return response;

        HashMap<String, String> data = new HashMap<>();
        copyOutputData(getCustomerDocument(), data, LoadCustomer.Fields.OUTPUT);
        return new Response(
                new Status(transactionId),
                data);
    }
}
