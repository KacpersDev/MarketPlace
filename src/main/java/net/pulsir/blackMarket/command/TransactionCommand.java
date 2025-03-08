package net.pulsir.blackMarket.command;

import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.transaction.Transaction;
import net.pulsir.blackMarket.utils.color.Color;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TransactionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length == 0) {
            for (final String l : BlackMarket.getInstance().getLanguage().getConfiguration().getStringList("transaction-usage")) {
                sender.sendMessage(Color.translate(l));
            }
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            int limit;

            if (args.length == 1) {
                limit = 10;
            } else {
                try {
                    limit = Integer.parseInt(args[1]);

                    if (!BlackMarket.getInstance().getTransactionManager().getTransactions().containsKey(offlinePlayer.getUniqueId())) {
                        sender.sendMessage(Color.translate(BlackMarket.getInstance().getLanguage()
                                .getConfiguration().getString("transaction-empty-player").replace("{player}", args[0])));
                        return false;
                    }

                    List<Transaction> transactions = BlackMarket.getInstance().getTransactionManager().getTransactionList(offlinePlayer.getUniqueId(), limit);

                    sender.sendMessage(Color.translate(BlackMarket.getInstance().getLanguage()
                            .getConfiguration().getString("transaction-history")
                            .replace("{amount}", String.valueOf(transactions.size()))
                            .replace("{player}", offlinePlayer.getName())));

                    int i = 1;
                    for (Transaction transaction : transactions) {
                        sender.sendMessage(Color.translate(BlackMarket.getInstance()
                                .getLanguage().getConfiguration().getString("transaction-view")
                                .replace("{index}", String.valueOf(i))
                                .replace("{date}", String.valueOf(transaction.getTransactionDate()))
                                .replace("{price}", String.valueOf(transaction.getPrice()))
                                .replace("{item_name}", transaction.getTransactionItem().getItemMeta().getDisplayName())
                                .replace("{item}", transaction.getTransactionItem().getType().name())
                                .replace("{player}", offlinePlayer.getName())));
                        i++;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(Color.translate("&cArgument must be a number."));
                }
            }
        }

        return true;
    }
}
