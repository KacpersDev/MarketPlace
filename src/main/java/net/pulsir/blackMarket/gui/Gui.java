package net.pulsir.blackMarket.gui;

import net.kyori.adventure.text.Component;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.marketplace.MarketPlaceItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.eclipse.sisu.space.BundleClassSpace;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Gui {

    Map<UUID, Integer> playerPages();
    Map<Integer, List<ItemStack>> pageContent();
    ItemStack nextPage();
    ItemStack previousPage();
    Component title();
    int size();

    default void open(Player player) {
        Inventory inventory = Bukkit.createInventory(player, size(), title());
        inventory.setItem(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getInt("next.slot"), nextPage());
        inventory.setItem(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getInt("previous.slot"), previousPage());

        List<Integer> slots = BlackMarket.getInstance().getConfiguration().getConfiguration().getIntegerList("marketplace-inventory.market-slots");
        int currentItem = 0;

        for (ItemStack itemStack : pageContent().get(0)) {
            inventory.setItem(slots.get(currentItem), itemStack);

            currentItem++;
        }

        player.openInventory(inventory);
    }
}
