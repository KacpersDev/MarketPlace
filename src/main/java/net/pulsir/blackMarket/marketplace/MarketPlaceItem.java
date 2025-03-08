package net.pulsir.blackMarket.marketplace;

import lombok.Data;
import net.pulsir.blackMarket.BlackMarket;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

@Data
public class MarketPlaceItem {

    private UUID itemId, seller;
    private ItemStack itemStack;
    private double price;

    public MarketPlaceItem(UUID itemId, UUID seller, ItemStack itemStack, double price) {
        this.itemId = itemId;
        this.seller = seller;
        this.itemStack = itemStack;
        this.price = price;
    }

    public ItemStack toItem() {
        ItemStack marketIem = itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(BlackMarket.getInstance().getSeller(), PersistentDataType.STRING, seller.toString());
        itemMeta.getPersistentDataContainer().set(BlackMarket.getInstance().getItemId(), PersistentDataType.STRING, itemId.toString());
        itemMeta.getPersistentDataContainer().set(BlackMarket.getInstance().getPrice(), PersistentDataType.DOUBLE, price);
        itemMeta.getPersistentDataContainer().set(BlackMarket.getInstance().getMarketPlaceInventory().buttonEvent(), PersistentDataType.STRING, "purchase");
        marketIem.setItemMeta(itemMeta);
        return marketIem;
    }
}
