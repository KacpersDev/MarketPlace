package net.pulsir.blackMarket.gui.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.pulsir.blackMarket.BlackMarket;
import net.pulsir.blackMarket.gui.Gui;
import net.pulsir.blackMarket.utils.color.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class BlackMarketInventory implements Gui {

    private final Map<UUID, Integer> playerPages = new HashMap<>();
    private final Map<Integer, List<ItemStack>> pageContent = new HashMap<>();
    private final NamespacedKey button = new NamespacedKey(BlackMarket.getInstance(), "button");

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
                .getConfiguration().getString("next.name"))).decoration(TextDecoration.ITALIC, false));
        itemMeta.getPersistentDataContainer().set(buttonEvent(), PersistentDataType.STRING, "next");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public ItemStack previousPage() {
        ItemStack itemStack = new ItemStack(Material.valueOf(BlackMarket.getInstance()
                .getConfiguration().getConfiguration().getString("previous.item")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Color.translate(Objects.requireNonNull(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getString("previous.name"))).decoration(TextDecoration.ITALIC, false));
        itemMeta.getPersistentDataContainer().set(buttonEvent(), PersistentDataType.STRING, "previous");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public Component title() {
        return Color.translate(Objects.requireNonNull(BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getString("blackmarket.title")));
    }

    @Override
    public int size() {
        return BlackMarket.getInstance().getConfiguration()
                .getConfiguration().getInt("blackmarket.size");
    }

    @Override
    public NamespacedKey buttonEvent() {
        return button;
    }
}
