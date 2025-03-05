package net.pulsir.blackMarket.gui;

import net.kyori.adventure.text.Component;
import net.pulsir.blackMarket.BlackMarket;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
    NamespacedKey buttonEvent();

    default void open(Player player, GuiType type) {
        Inventory inventory = Bukkit.createInventory(player, size(), title());

        inventory.setItem(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getInt("next.slot"), nextPage());
        inventory.setItem(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getInt("previous.slot"), previousPage());

        if (type.equals(GuiType.MARKETPLACE)) {
            List<Integer> slots = BlackMarket.getInstance().getConfiguration().getConfiguration().getIntegerList("marketplace-inventory.market-slots");
            int currentItem = 0;

            for (ItemStack itemStack : pageContent().get(0)) {
                inventory.setItem(slots.get(currentItem), itemStack);

                currentItem++;
            }
        } else if (type.equals(GuiType.BLACK_MARKET)) {
            List<Integer> slots = BlackMarket.getInstance().getConfiguration().getConfiguration().getIntegerList("blackmarket.slots");
            int currentItem = 0;

            for (ItemStack itemStack : pageContent().get(0)) {
                inventory.setItem(slots.get(currentItem), itemStack);

                currentItem++;
            }
        }

        playerPages().put(player.getUniqueId(), 0);
        player.openInventory(inventory);
    }

    default void updateInventory(Player player, boolean add, GuiType type) {
        if (type.equals(GuiType.MARKETPLACE)) {
            int currentPage = playerPages().get(player.getUniqueId());
            if (add) {
                if (pageContent().get(currentPage + 1) != null) {
                    playerPages().replace(player.getUniqueId(), currentPage + 1);

                    Inventory inventory = player.getOpenInventory().getTopInventory();
                    inventory.clear();

                    inventory.setItem(BlackMarket.getInstance().getConfiguration()
                            .getConfiguration().getInt("next.slot"), nextPage());
                    inventory.setItem(BlackMarket.getInstance().getConfiguration()
                            .getConfiguration().getInt("previous.slot"), previousPage());

                    List<Integer> slots = BlackMarket.getInstance().getConfiguration().getConfiguration().getIntegerList("marketplace-inventory.market-slots");
                    int currentItem = 0;

                    for (ItemStack itemStack : pageContent().get(currentPage + 1)) {
                        inventory.setItem(slots.get(currentItem), itemStack);

                        currentItem++;
                    }
                }
            } else {
                if (pageContent().get(currentPage - 1) != null) {
                    playerPages().replace(player.getUniqueId(), currentPage - 1);
                    Inventory inventory = player.getOpenInventory().getTopInventory();
                    inventory.clear();

                    inventory.setItem(BlackMarket.getInstance().getConfiguration()
                            .getConfiguration().getInt("next.slot"), nextPage());
                    inventory.setItem(BlackMarket.getInstance().getConfiguration()
                            .getConfiguration().getInt("previous.slot"), previousPage());

                    List<Integer> slots = BlackMarket.getInstance().getConfiguration().getConfiguration().getIntegerList("marketplace-inventory.market-slots");
                    int currentItem = 0;

                    for (ItemStack itemStack : pageContent().get(currentPage - 1)) {
                        inventory.setItem(slots.get(currentItem), itemStack);

                        currentItem++;
                    }
                }
            }
        } else if (type.equals(GuiType.BLACK_MARKET)) {
            int currentPage = playerPages().get(player.getUniqueId());
            if (add) {
                if (pageContent().get(currentPage + 1) != null) {
                    playerPages().replace(player.getUniqueId(), currentPage + 1);

                    Inventory inventory = player.getOpenInventory().getTopInventory();
                    inventory.clear();

                    inventory.setItem(BlackMarket.getInstance().getConfiguration()
                            .getConfiguration().getInt("next.slot"), nextPage());
                    inventory.setItem(BlackMarket.getInstance().getConfiguration()
                            .getConfiguration().getInt("previous.slot"), previousPage());

                    List<Integer> slots = BlackMarket.getInstance().getConfiguration().getConfiguration().getIntegerList("blackmarket.slots");
                    int currentItem = 0;

                    for (ItemStack itemStack : pageContent().get(currentPage + 1)) {
                        inventory.setItem(slots.get(currentItem), itemStack);

                        currentItem++;
                    }
                }
            } else {
                if (pageContent().get(currentPage - 1) != null) {
                    playerPages().replace(player.getUniqueId(), currentPage - 1);
                    Inventory inventory = player.getOpenInventory().getTopInventory();
                    inventory.clear();

                    inventory.setItem(BlackMarket.getInstance().getConfiguration()
                            .getConfiguration().getInt("next.slot"), nextPage());
                    inventory.setItem(BlackMarket.getInstance().getConfiguration()
                            .getConfiguration().getInt("previous.slot"), previousPage());

                    List<Integer> slots = BlackMarket.getInstance().getConfiguration().getConfiguration().getIntegerList("blackmarket.slots");
                    int currentItem = 0;

                    for (ItemStack itemStack : pageContent().get(currentPage - 1)) {
                        inventory.setItem(slots.get(currentItem), itemStack);

                        currentItem++;
                    }
                }
            }
        }

        player.updateInventory();
    }
}
