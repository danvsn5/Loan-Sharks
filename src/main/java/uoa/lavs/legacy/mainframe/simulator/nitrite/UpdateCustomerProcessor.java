package uoa.lavs.legacy.mainframe.simulator.nitrite;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.DocumentCursor;
import org.dizitart.no2.collection.NitriteCollection;

import uoa.lavs.legacy.mainframe.MessageErrorStatus;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerUpdateStatus;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomer;

import java.time.LocalDate;
import java.util.HashMap;

import static org.dizitart.no2.filters.FluentFilter.where;

public class UpdateCustomerProcessor extends BaseProcessor {

    public UpdateCustomerProcessor(Nitrite database) {
        super(database);
    }

    @Override
    public Response process(Request request, long transactionId) {
        NitriteCollection customers = getCustomersCollection();

        String id = request.getValue(UpdateCustomer.Fields.CUSTOMER_ID);
        Document doc;
        if (id == null) {
            id = generateNewId("customer");
            doc = Document.createDocument();
            populateDocument(request, doc);
            doc.put(UpdateCustomer.Fields.CUSTOMER_ID, id);
            doc.put(UpdateCustomer.Fields.STATUS, "Active");    // Status will always be active
            customers.insert(doc);
        } else {
            DocumentCursor cursor = customers.find(where(UpdateCustomer.Fields.CUSTOMER_ID).eq(id));
            doc = cursor.firstOrNull();
            if (doc == null) {
                return MessageErrorStatus.CUSTOMER_NOT_FOUND.generateEmptyResponse(transactionId);
            }
            populateDocument(request, doc);
            customers.update(doc);
        }

        HashMap<String, String> data = new HashMap<>();
        copyOutputData(doc, data, UpdateCustomer.Fields.OUTPUT);
        return new Response(
                new Status(transactionId),
                data);
    }

    private void populateDocument(Request request, Document doc) {
        copyInputData(doc, request, UpdateCustomer.Fields.INPUT);
        doc.put(LoadCustomerUpdateStatus.Fields.LAST_DETAILS_CHANGE, LocalDate.now());
    }
}
