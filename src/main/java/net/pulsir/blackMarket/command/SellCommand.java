package net.pulsir.blackMarket.command;

import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.marketplace.MarketPlaceItem;
import net.pulsir.blackMarket.utils.color.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class SellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) return false;

        if (!player.hasPermission(Objects.requireNonNull(BlackMarket.getInstance().getConfiguration().getConfiguration().getString("permissions.marketplace-sell")))) {
            player.sendMessage(Color.translate(Objects.requireNonNull(BlackMarket.getInstance()
                    .getLanguage().getConfiguration().getString("no-permissions"))));
            return false;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getItemMeta() == null) {
            player.sendMessage(Color.translate(Objects.requireNonNull(BlackMarket.getInstance()
                    .getLanguage().getConfiguration().getString("item-null"))));
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(Color.translate(Objects.requireNonNull(BlackMarket.getInstance().getLanguage()
                    .getConfiguration().getString("sell-usage"))));
        } else {
            try {
                double price = Double.parseDouble(args[0]);
                UUID itemId = UUID.randomUUID();

                MarketPlaceItem marketPlaceItem = new MarketPlaceItem(itemId, player.getUniqueId(),
                        itemStack, price);
                BlackMarket.getInstance().getMarketPlaceManager().getMarketPlaceItems()
                        .put(itemId, marketPlaceItem);
                player.sendMessage(Color.translate(Objects.requireNonNull(BlackMarket.getInstance()
                        .getLanguage().getConfiguration().getString("sell-added"))));

                BlackMarket.getInstance().getMarketPlaceManager().addItem(marketPlaceItem);
            } catch (NumberFormatException e) {
                player.sendMessage(Color.translate("&cArgument must be a number."));
                e.printStackTrace();
            }
        }


        return true;
    }
}
