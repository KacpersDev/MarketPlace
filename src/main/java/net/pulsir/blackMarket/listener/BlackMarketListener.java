package net.pulsir.blackMarket.listener;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.gui.GuiType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.persistence.PersistentDataType;

public class BlackMarketListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (MiniMessage.miniMessage().serialize(event.getView().title())
                .equalsIgnoreCase(BlackMarket.getInstance().getConfiguration().getConfiguration().getString("blackmarket.title"))
                || LegacyComponentSerializer.legacyAmpersand().serialize(event.getView().title()).equalsIgnoreCase(BlackMarket.getInstance()
                .getConfiguration().getConfiguration().getString("blackmarket.title"))) {
            event.setCancelled(true);
        }

        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null
                || event.getCurrentItem().getItemMeta() == null
                || !event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(BlackMarket.getInstance().getMarketPlaceInventory()
                .buttonEvent())) return;

        String buttonEvent = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(BlackMarket.getInstance().getBlackMarketInventory().buttonEvent(), PersistentDataType.STRING);
        if (buttonEvent == null) return;

        if (buttonEvent.equalsIgnoreCase("purchase")) {
            // todo purchase
        } else if (buttonEvent.equalsIgnoreCase("next")) {
            BlackMarket.getInstance().getBlackMarketInventory().updateInventory(player, true, GuiType.BLACK_MARKET);
        } else if (buttonEvent.equalsIgnoreCase("previous")) {
            BlackMarket.getInstance().getBlackMarketInventory().updateInventory(player, false, GuiType.BLACK_MARKET);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        BlackMarket.getInstance().getBlackMarketInventory().playerPages().remove(event.getPlayer().getUniqueId());
    }
}
