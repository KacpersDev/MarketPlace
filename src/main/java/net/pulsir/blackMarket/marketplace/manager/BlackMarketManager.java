package net.pulsir.blackMarket.marketplace.manager;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.marketplace.MarketPlaceItem;
import net.pulsir.blackMarket.utils.serializer.ItemStackSerializer;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class BlackMarketManager {

    private final Map<UUID, MarketPlaceItem> blackMarketItems = new HashMap<>();

    @Setter
    private long currentTime = 86400;

    public void set() {
        List<MarketPlaceItem> copy = new ArrayList<>(BlackMarket.getInstance().getMarketPlaceManager().getMarketPlaceItems().values());
        if (copy.isEmpty()) return;

        int maxItems = BlackMarket.getInstance().getConfiguration().getConfiguration().getInt("blackmarket-max");
        if (maxItems > 54) return;

        Collections.shuffle(copy);

        int loopFor = maxItems - 1;
        if (copy.size() < maxItems) {
            loopFor = copy.size() - 1;
        }

        for (int i = 0 ; i < loopFor ; i++) {
            MarketPlaceItem item = copy.get(i);
            if (item != null) {
                blackMarketItems.put(item.getItemId(), item);
            }
        }
    }

    public void load() {
        FindIterable<Document> iterable = BlackMarket.getInstance().getMongoManager().getBlackmarket().find();
        try (MongoCursor<Document> cursor = iterable.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();

                UUID itemId = UUID.fromString(document.getString("itemId"));
                UUID sellerId = UUID.fromString(document.getString("sellerId"));
                ItemStack itemStack = ItemStackSerializer.deSerialize(document.getString("item"));
                double price = document.getDouble("price");

                blackMarketItems.put(itemId, new MarketPlaceItem(itemId, sellerId, itemStack, price));
            }
        }
    }

    public void save() {
        blackMarketItems.values().forEach(marketPlaceItem -> {
            Document document = new Document();
            document.put("itemId", marketPlaceItem.getItemId().toString());
            document.put("sellerId", marketPlaceItem.getSeller().toString());
            document.put("item", ItemStackSerializer.serialize(marketPlaceItem.getItemStack()));
            document.put("price", marketPlaceItem.getPrice());

            BlackMarket.getInstance().getMongoManager().getBlackmarket().replaceOne(Filters.eq("itemId", marketPlaceItem.getItemId().toString()),
                    document, new ReplaceOptions().upsert(true));
        });
    }
}
