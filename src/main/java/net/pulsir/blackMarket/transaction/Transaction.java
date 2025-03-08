package net.pulsir.blackMarket.transaction;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

@Data
public class Transaction {

    private UUID transactionUUID;
    private UUID playerUUID;
    private UUID sellerUUID;
    private ItemStack transactionItem;
    private Date transactionDate;
    private double price;

    public Transaction(UUID transactionUUID, UUID playerUUID, UUID sellerUUID, ItemStack transactionItem, Date transactionDate, double price) {
        this.transactionUUID = transactionUUID;
        this.playerUUID = playerUUID;
        this.sellerUUID = sellerUUID;
        this.transactionItem = transactionItem;
        this.transactionDate = transactionDate;
        this.price = price;
    }
}
