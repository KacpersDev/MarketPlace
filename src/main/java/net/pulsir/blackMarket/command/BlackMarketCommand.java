package net.pulsir.blackMarket.command;

import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.gui.GuiType;
import net.pulsir.blackMarket.utils.color.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlackMarketCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) return false;

        if (!player.hasPermission(Objects.requireNonNull(BlackMarket.getInstance().getConfiguration().getConfiguration().getString("permissions.marketplace-blackmarket")))) {
            player.sendMessage(Color.translate(Objects.requireNonNull(BlackMarket.getInstance().getLanguage().getConfiguration()
                    .getString("no-permissions"))));
            return false;
        }

        if (args.length == 0) {
            BlackMarket.getInstance().getBlackMarketInventory().open(player, GuiType.BLACK_MARKET);
        } else if (args[0].equalsIgnoreCase("refresh")) {
            if (!player.hasPermission(Objects.requireNonNull(BlackMarket.getInstance().getConfiguration().getConfiguration().getString("permissions.marketplace-blackmarket-refresh")))) {
                player.sendMessage(Color.translate(Objects.requireNonNull(BlackMarket.getInstance().getLanguage().getConfiguration()
                        .getString("no-permissions"))));
                return false;
            }

            BlackMarket.getInstance().getBlackMarketManager().set();
            BlackMarket.getInstance().getBlackMarketManager().setCurrentTime(86400);

            player.sendMessage(Color.translate(Objects.requireNonNull(BlackMarket.getInstance().getLanguage()
                    .getConfiguration().getString("blackmarket-refresh"))));
        }

        return true;
    }
}
