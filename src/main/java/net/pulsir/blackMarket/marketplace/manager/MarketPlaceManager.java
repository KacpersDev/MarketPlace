package net.pulsir.blackMarket.marketplace.manager;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.marketplace.MarketPlaceItem;
import net.pulsir.blackMarket.utils.serializer.ItemStackSerializer;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MarketPlaceManager {

    private final Map<UUID, MarketPlaceItem> marketPlaceItems = new HashMap<>();

    public void load() {
        FindIterable<Document> iterable = BlackMarket.getInstance().getMongoManager().getItems().find();
        try (MongoCursor<Document> cursor = iterable.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();

                UUID itemId = UUID.fromString(document.getString("itemId"));
                UUID sellerId = UUID.fromString(document.getString("sellerId"));
                ItemStack itemStack = ItemStackSerializer.deSerialize(document.getString("item"));
                double price = document.getDouble("price");

                marketPlaceItems.put(itemId, new MarketPlaceItem(itemId, sellerId, itemStack, price));
            }
        }
    }

    public void save() {
        marketPlaceItems.values().forEach(marketPlaceItem -> {
            Document document = new Document();
            document.put("itemId", marketPlaceItem.getItemId().toString());
            document.put("sellerId", marketPlaceItem.getSeller().toString());
            document.put("item", ItemStackSerializer.serialize(marketPlaceItem.getItemStack()));
            document.put("price", marketPlaceItem.getPrice());

            BlackMarket.getInstance().getMongoManager().getItems().replaceOne(Filters.eq("itemId", marketPlaceItem.getItemId().toString()),
                    document, new ReplaceOptions().upsert(true));
        });
    }
}