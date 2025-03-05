package net.pulsir.blackMarket.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.pulsir.blackMarket.BlackMarket;
import org.bson.Document;

import java.util.Objects;

@Getter
public class MongoManager {

    private final MongoCollection<Document> profiles, items, transactions, blackmarket;

    public MongoManager() {
        MongoClient mongoClient = MongoClients.create(new ConnectionString(Objects.requireNonNull(BlackMarket.getInstance()
                .getConfiguration().getConfiguration().getString("mongo-uri"))));
        MongoDatabase database = mongoClient.getDatabase("blackMarket");

        profiles = database.getCollection("profiles");
        items = database.getCollection("items");
        transactions = database.getCollection("transactions");
        blackmarket = database.getCollection("blackmarket");
    }
}
