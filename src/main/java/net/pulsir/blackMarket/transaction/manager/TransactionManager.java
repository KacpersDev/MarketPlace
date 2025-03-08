package net.pulsir.blackMarket.transaction.manager;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.transaction.Transaction;
import net.pulsir.blackMarket.utils.serializer.ItemStackSerializer;
import org.bson.Document;

import java.util.*;

@Getter
public class TransactionManager {

    private final Map<UUID, List<Transaction>> transactions = new HashMap<>();

    public void load() {
        FindIterable<Document> iterable = BlackMarket.getInstance().getMongoManager().getTransactions().find();
        try (MongoCursor<Document> cursor = iterable.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();

                Transaction transaction = new Transaction(UUID.fromString("transaction"),
                        UUID.fromString("seller"),
                        UUID.fromString("player"),
                        ItemStackSerializer.deSerialize(document.getString("item")),
                        new Date(document.getLong("date")),
                        document.getDouble("price"));

                if (!transactions.containsKey(transaction.getPlayerUUID())) {
                    List<Transaction> transactionList = new ArrayList<>();
                    transactionList.add(transaction);
                    transactions.put(transaction.getPlayerUUID(), transactionList);
                } else {
                    transactions.get(transaction.getPlayerUUID()).add(transaction);
                }
            }
        }
    }

    public void save() {
        transactions.values().forEach(playerTransaction -> playerTransaction.forEach(transaction -> {
            Document document = new Document();
            document.put("transaction", transaction.getTransactionUUID().toString());
            document.put("seller", transaction.getSellerUUID().toString());
            document.put("player", transaction.getPlayerUUID().toString());
            document.put("item", ItemStackSerializer.serialize(transaction.getTransactionItem()));
            document.put("date", transaction.getTransactionDate());
            document.put("price", transaction.getPrice());

            BlackMarket.getInstance().getMongoManager().getTransactions().replaceOne(Filters.eq("transaction", transaction.getTransactionUUID()),
                    document, new ReplaceOptions().upsert(true));
        }));
    }

    public List<Transaction> getTransactionList(UUID uniqueId, int limit) {
        if (transactions.get(uniqueId).size() <= limit) return transactions.get(uniqueId);
        List<Transaction> transactionList = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            transactionList.add(transactions.get(uniqueId).get(i));
        }

        return transactionList;
    }
}
