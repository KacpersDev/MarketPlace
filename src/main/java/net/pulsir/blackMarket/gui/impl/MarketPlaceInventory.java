package net.pulsir.blackMarket.gui.impl;

import net.kyori.adventure.text.Component;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.gui.Gui;
import net.pulsir.blackMarket.utils.color.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MarketPlaceInventory implements Gui {

    private final Map<UUID, Integer> playerPages = new HashMap<>();
    private final Map<Integer, List<ItemStack>> pageContent = new HashMap<>();

    @Override
    public Map<UUID, Integer> playerPages() {
        return playerPages;
    }

    @Override
    public Map<Integer, List<ItemStack>> pageContent() {
        return pageContent;
    }

    @Override
    public ItemStack nextPage() {
        ItemStack itemStack = new ItemStack(Material.valueOf(BlackMarket.getInstance()
                .getConfiguration().getConfiguration().getString("next.item")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Color.translate(Objects.requireNonNull(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getString("next.name"))));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public ItemStack previousPage() {
        ItemStack itemStack = new ItemStack(Material.valueOf(BlackMarket.getInstance()
                .getConfiguration().getConfiguration().getString("previous.item")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Color.translate(Objects.requireNonNull(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getString("previous.name"))));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public Component title() {
        return Color.translate(Objects.requireNonNull(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getString("marketplace-inventory.title")));
    }

    @Override
    public int size() {
        return BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getInt("marketplace-inventory.size");
    }
}
