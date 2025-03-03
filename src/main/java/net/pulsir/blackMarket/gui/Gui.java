package net.pulsir.blackMarket.gui;

import net.kyori.adventure.text.Component;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.marketplace.MarketPlaceItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.eclipse.sisu.space.BundleClassSpace;

import java.util.Map;
import java.util.UUID;

public interface Gui {

    Map<UUID, Integer> playerPages();
    Map<Integer, ItemStack[]> pageContent();
    ItemStack nextPage();
    ItemStack previousPage();
    Component title();
    int size();

    default void load(int page) {
        int slots = BlackMarket.getInstance().getConfiguration().getConfiguration().getIntegerList("marketplace-inventory.market-slots").size();

        for (MarketPlaceItem marketPlaceItem : BlackMarket.getInstance().getMarketPlaceManager().getMarketPlaceItems().values()) {
            ItemStack itemStack = marketPlaceItem.toItem();


        }
    }

    default void open(Player player) {
        Inventory inventory = Bukkit.createInventory(player, size(), title());
        inventory.setItem(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getInt("next.slot"), nextPage());
        inventory.setItem(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getInt("previous.slot"), previousPage());



        player.openInventory(inventory);
    }
}
