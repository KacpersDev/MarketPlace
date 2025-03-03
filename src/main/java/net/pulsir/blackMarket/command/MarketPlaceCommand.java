package net.pulsir.blackMarket.command;

import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.utils.color.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MarketPlaceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) return false;

        if (!player.hasPermission("marketplace.view")) {
            player.sendMessage(Color.translate(Objects.requireNonNull(BlackMarket.getInstance()
                    .getLanguage().getConfiguration().getString("no-permissions"))));
            return false;
        }

        BlackMarket.getInstance().getMarketPlaceInventory().open(player);

        return true;
    }
}
