package uoa.lavs.legacy.mainframe.simulator.nitrite;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;

import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomerNote;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateCustomerNoteProcessor extends BaseCustomerProcessor {
    public UpdateCustomerNoteProcessor(Nitrite database) {
        super(database);
    }

    @Override
    public Response process(Request request, long transactionId) {
        Response response = loadCustomerDocument(request, transactionId);
        if (response != null) return response;

        Document doc = getCustomerDocument();
        String number = request.getValue("number");

        Integer index = number == null ? Integer.MAX_VALUE : Integer.parseInt(number) - 1;
        ArrayList<Document> items = getCustomerItems(NitriteConnection.Internal.ITEM_NOTES);
        if (items == null) {
            items = new ArrayList<>();
            doc.put("notes", items);
        }

        Document item;
        if (index >= items.size()) {
            item = Document.createDocument("number", number);
            items.add(item);
        } else {
            item = items.get(index);
        }

        Integer count = 0;
        for (String name : UpdateCustomerNote.Fields.INPUT) {
            if (!name.endsWith(".line")) continue;
            String value = request.getValue(name);
            item.put(name, value);
            if (value != null) count++;
        }
        item.put(UpdateCustomerNote.Fields.LINE_COUNT, String.valueOf(count));
        getCustomersCollection().update(doc);

        HashMap<String, String> data = new HashMap<>();
        copyOutputData(item, data, UpdateCustomerNote.Fields.OUTPUT);
        data.put(UpdateCustomerNote.Fields.PAGE_COUNT, Integer.toString(items.size()));
        Integer size = items.size();
        data.put("number", size.toString());
        return new Response(
                new Status(transactionId),
                data);
    }
}
