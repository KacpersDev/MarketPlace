package net.pulsir.blackMarket.marketplace;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.utils.color.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
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
        List<Component> lore = new ArrayList<>();
        BlackMarket.getInstance().getConfiguration().getConfiguration().getStringList("item-lore")
                        .forEach(l -> lore.add(Color.translate(l.replace("{price}", String.valueOf(price))).decoration(TextDecoration.ITALIC, false)));
        itemMeta.lore(lore);
        marketIem.setItemMeta(itemMeta);
        return marketIem;
    }
}
