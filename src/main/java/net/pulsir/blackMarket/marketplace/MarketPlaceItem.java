package net.pulsir.blackMarket.marketplace;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

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
}
