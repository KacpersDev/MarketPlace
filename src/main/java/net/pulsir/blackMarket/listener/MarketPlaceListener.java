package net.pulsir.blackMarket.listener;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pulsir.blackMarket.BlackMarket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.persistence.PersistentDataType;

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
            // todo purchase
        } else if (buttonEvent.equalsIgnoreCase("next")) {
            BlackMarket.getInstance().getMarketPlaceInventory().updateInventory(player, true);
        } else if (buttonEvent.equalsIgnoreCase("previous")) {
            BlackMarket.getInstance().getMarketPlaceInventory().updateInventory(player, false);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        BlackMarket.getInstance().getMarketPlaceInventory().playerPages().remove(event.getPlayer().getUniqueId());
    }
}
