package net.pulsir.blackMarket.listener;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.gui.GuiType;
import net.pulsir.blackMarket.transaction.Transaction;
import net.pulsir.blackMarket.utils.color.Color;
import net.pulsir.blackMarket.utils.discord.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.*;

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

            if (!itemStack.getItemMeta().getPersistentDataContainer().has(BlackMarket.getInstance().getSeller())
            || itemStack.getItemMeta().getPersistentDataContainer().get(BlackMarket.getInstance().getSeller(), PersistentDataType.STRING) == null) return;
            if (!itemStack.getItemMeta().getPersistentDataContainer().has(BlackMarket.getInstance().getPrice())
                    || itemStack.getItemMeta().getPersistentDataContainer().get(BlackMarket.getInstance().getPrice(), PersistentDataType.DOUBLE) == null
                    || !itemStack.getItemMeta().getPersistentDataContainer().has(BlackMarket.getInstance().getItemId(), PersistentDataType.STRING)
                    || itemStack.getItemMeta().getPersistentDataContainer().get(BlackMarket.getInstance().getItemId(), PersistentDataType.STRING) == null) return;

            UUID seller = UUID.fromString(itemStack.getItemMeta().getPersistentDataContainer().get(BlackMarket.getInstance().getSeller(), PersistentDataType.STRING));
            UUID itemId = UUID.fromString(itemStack.getItemMeta().getPersistentDataContainer().get(BlackMarket.getInstance().getItemId(), PersistentDataType.STRING));
            double price = itemStack.getItemMeta().getPersistentDataContainer().get(BlackMarket.getInstance().getPrice(), PersistentDataType.DOUBLE);

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

            Transaction transaction = new Transaction(UUID.randomUUID(), player.getUniqueId(), seller, itemStack, new Date(), price);
            if (!BlackMarket.getInstance().getTransactionManager().getTransactions().containsKey(player.getUniqueId())) {
                List<Transaction> transactions = new ArrayList<>();
                transactions.add(transaction);
                BlackMarket.getInstance().getTransactionManager().getTransactions().put(player.getUniqueId(), transactions);
            } else {
                BlackMarket.getInstance().getTransactionManager().getTransactions().get(player.getUniqueId()).add(transaction);
            }

            DiscordWebhook discordWebhook = new DiscordWebhook(BlackMarket.getInstance().getConfiguration().getConfiguration().getString("webhook"));
            DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
            embedObject.setAuthor(player.getName() + "s purchase", null, null);
            embedObject.setTitle(BlackMarket.getInstance().getConfiguration()
                    .getConfiguration().getString("transaction-title"));
            for (final String field : BlackMarket.getInstance().getConfiguration().getConfiguration().getStringList("transaction-fields")) {
                embedObject.addField(field.split(":")[0],
                        field.split(":")[1]
                                .replace("{price}", String.valueOf(price))
                                .replace("{item}", itemStack.getType().name())
                                .replace("{seller}", player.getName()), false);
            }
            embedObject.setColor(java.awt.Color.RED);
            discordWebhook.addEmbed(embedObject);
            try {
                discordWebhook.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


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
