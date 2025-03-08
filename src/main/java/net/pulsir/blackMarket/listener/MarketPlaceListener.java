package net.pulsir.blackMarket.listener;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.gui.GuiType;
import net.pulsir.blackMarket.utils.color.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class MarketPlaceListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (MiniMessage.miniMessage().serialize(event.getView().title())
                .equalsIgnoreCase(BlackMarket.getInstance().getConfiguration().getConfiguration().getString("marketplace-inventory.title"))
                || LegacyComponentSerializer.legacyAmpersand().serialize(event.getView().title()).equalsIgnoreCase(BlackMarket.getInstance()
                .getConfiguration().getConfiguration().getString("marketplace-inventory.title"))) {
            event.setCancelled(true);
        }

        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null
        || event.getCurrentItem().getItemMeta() == null
        || !event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(BlackMarket.getInstance().getMarketPlaceInventory()
                .buttonEvent())) return;

        String buttonEvent = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(BlackMarket.getInstance().getMarketPlaceInventory().buttonEvent(), PersistentDataType.STRING);
        if (buttonEvent == null) return;

        if (buttonEvent.equalsIgnoreCase("purchase")) {
            ItemStack itemStack = event.getCurrentItem();

            if (!itemStack.getPersistentDataContainer().has(BlackMarket.getInstance().getSeller())
            || itemStack.getPersistentDataContainer().get(BlackMarket.getInstance().getSeller(), PersistentDataType.STRING) == null) return;
            if (!itemStack.getPersistentDataContainer().has(BlackMarket.getInstance().getPrice())
                    || itemStack.getPersistentDataContainer().get(BlackMarket.getInstance().getPrice(), PersistentDataType.DOUBLE) == null
                    || !itemStack.getPersistentDataContainer().has(BlackMarket.getInstance().getItemId(), PersistentDataType.STRING)
                    || itemStack.getPersistentDataContainer().get(BlackMarket.getInstance().getItemId(), PersistentDataType.STRING) == null) return;

            UUID seller = UUID.fromString(itemStack.getPersistentDataContainer().get(BlackMarket.getInstance().getSeller(), PersistentDataType.STRING));
            UUID itemId = UUID.fromString(itemStack.getPersistentDataContainer().get(BlackMarket.getInstance().getItemId(), PersistentDataType.STRING));
            double price = itemStack.getPersistentDataContainer().get(BlackMarket.getInstance().getPrice(), PersistentDataType.DOUBLE);

            if (!BlackMarket.getEcon().has(player, price)) {
                player.sendMessage(Color.translate(BlackMarket.getInstance().getLanguage()
                        .getConfiguration().getString("insufficient-balance")
                        .replace("{amount}", String.valueOf(price))));
                return;
            }

            BlackMarket.getEcon().withdrawPlayer(player, price);
            BlackMarket.getEcon().depositPlayer(Bukkit.getOfflinePlayer(seller), price);

            player.getInventory().addItem(itemStack);

            BlackMarket.getInstance().getMarketPlaceManager().getMarketPlaceItems().remove(itemId);

            player.sendMessage(Color.translate(BlackMarket.getInstance().getLanguage()
                    .getConfiguration().getString("purchased")));
        } else if (buttonEvent.equalsIgnoreCase("next")) {
            BlackMarket.getInstance().getMarketPlaceInventory().updateInventory(player, true, GuiType.MARKETPLACE);
        } else if (buttonEvent.equalsIgnoreCase("previous")) {
            BlackMarket.getInstance().getMarketPlaceInventory().updateInventory(player, false, GuiType.MARKETPLACE);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        BlackMarket.getInstance().getMarketPlaceInventory().playerPages().remove(event.getPlayer().getUniqueId());
    }
}
/*
        itemMeta.getPersistentDataContainer().set(BlackMarket.getInstance().getSeller(), PersistentDataType.STRING, seller.toString());
        itemMeta.getPersistentDataContainer().set(BlackMarket.getInstance().getPrice(), PersistentDataType.DOUBLE, price);
        itemMeta.getPersistentDataContainer().set(BlackMarket.getInstance().getMarketPlaceInventory().buttonEvent(), PersistentDataType.STRING, "purchase");
 */
