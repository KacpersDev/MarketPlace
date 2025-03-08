package net.pulsir.blackMarket;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.pulsir.blackMarket.command.BlackMarketCommand;
import net.pulsir.blackMarket.command.MarketPlaceCommand;
import net.pulsir.blackMarket.command.SellCommand;
import net.pulsir.blackMarket.command.TransactionCommand;
import net.pulsir.blackMarket.gui.impl.BlackMarketInventory;
import net.pulsir.blackMarket.gui.impl.MarketPlaceInventory;
import net.pulsir.blackMarket.listener.MarketPlaceListener;
import net.pulsir.blackMarket.marketplace.MarketPlaceItem;
import net.pulsir.blackMarket.marketplace.MarketPlaceType;
import net.pulsir.blackMarket.marketplace.manager.BlackMarketManager;
import net.pulsir.blackMarket.marketplace.manager.MarketPlaceManager;
import net.pulsir.blackMarket.mongo.MongoManager;
import net.pulsir.blackMarket.task.BlackMarketTask;
import net.pulsir.blackMarket.transaction.manager.TransactionManager;
import net.pulsir.blackMarket.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public final class BlackMarket extends JavaPlugin {

    @Getter private static BlackMarket instance;

    @Getter private static Economy econ = null;

    private final NamespacedKey seller = new NamespacedKey(this, "seller");
    private final NamespacedKey price = new NamespacedKey(this, "price");
    private final NamespacedKey itemId = new NamespacedKey(this, "itemId");

    private Config configuration, language;

    private MongoManager mongoManager;
    private MarketPlaceManager marketPlaceManager;
    private BlackMarketManager blackMarketManager;
    private TransactionManager transactionManager;

    private MarketPlaceInventory marketPlaceInventory;
    private BlackMarketInventory blackMarketInventory;

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfiguration();
        this.loadCommand();
        this.loadListeners(Bukkit.getPluginManager());

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.mongoManager = new MongoManager();

        this.marketPlaceManager = new MarketPlaceManager();
        this.blackMarketManager = new BlackMarketManager();
        this.transactionManager = new TransactionManager();

        this.marketPlaceManager.load();
        this.blackMarketManager.load();
        this.transactionManager.load();

        this.marketPlaceInventory = new MarketPlaceInventory();
        this.blackMarketInventory = new BlackMarketInventory();

        if (getBlackMarketManager().getBlackMarketItems().values().isEmpty()) {
            getBlackMarketManager().set();
        }

        if (getConfiguration().getConfiguration().getLong("blackmarket-reset") != 0) {
            getBlackMarketManager().setCurrentTime(getConfiguration().getConfiguration()
                    .getLong("blackmarket-reset"));
        }

        this.loadMarket(MarketPlaceType.DEFAULT);
        this.loadMarket(MarketPlaceType.BLACK_MARKET);

        Bukkit.getScheduler().runTaskTimer(this, new BlackMarketTask(), 0L, 20L);
    }

    @Override
    public void onDisable() {
        this.marketPlaceManager.save();
        this.blackMarketManager.save();
        this.transactionManager.save();

        getConfiguration().getConfiguration().set("blackmarket-reset",
                getBlackMarketManager().getCurrentTime());

        instance = null;
    }

    private void loadConfiguration() {
        configuration = new Config(this, new File(getDataFolder(), "configuration.yml"),
                new YamlConfiguration(), "configuration.yml");
        language = new Config(this, new File(getDataFolder(), "language.yml"),
                new YamlConfiguration(), "language.yml");

        configuration.create();
        language.create();
    }

    private void loadCommand() {
        Objects.requireNonNull(getCommand("sell")).setExecutor(new SellCommand());
        Objects.requireNonNull(getCommand("marketplace")).setExecutor(new MarketPlaceCommand());
        Objects.requireNonNull(getCommand("blackmarket")).setExecutor(new BlackMarketCommand());
        getCommand("transaction").setExecutor(new TransactionCommand());
    }

    private void loadListeners(PluginManager pluginManager) {
        pluginManager.registerEvents(new MarketPlaceListener(), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void loadMarket(MarketPlaceType marketPlaceType) {
        if (marketPlaceType.equals(MarketPlaceType.DEFAULT)) {
            int slots = BlackMarket.getInstance().getConfiguration().getConfiguration().getIntegerList("marketplace-inventory.market-slots").size();
            int currentIndex = 0;
            int currentPage = 0;

            for (MarketPlaceItem marketPlaceItem : BlackMarket.getInstance().getMarketPlaceManager().getMarketPlaceItems().values()) {
                ItemStack item = marketPlaceItem.toItem();

                if (getMarketPlaceInventory().pageContent().containsKey(currentPage)) {
                    getMarketPlaceInventory().pageContent().get(currentPage).add(item);
                } else {
                    List<ItemStack> items = new ArrayList<>();
                    items.add(item);
                    getMarketPlaceInventory().pageContent().put(currentPage, items);
                }

                currentIndex++;

                if (currentIndex >= slots - 1) {
                    currentPage += 1;
                    currentIndex = 0;
                }
            }
        } else if (marketPlaceType.equals(MarketPlaceType.BLACK_MARKET)) {
            int slots = BlackMarket.getInstance().getConfiguration().getConfiguration().getIntegerList("blackmarket.slots").size();
            int currentIndex = 0;
            int currentPage = 0;

            for (MarketPlaceItem marketPlaceItem : BlackMarket.getInstance().getBlackMarketManager().getBlackMarketItems().values()) {
                ItemStack item = marketPlaceItem.toItem();

                if (getBlackMarketInventory().pageContent().containsKey(currentPage)) {
                    getBlackMarketInventory().pageContent().get(currentPage).add(item);
                } else {
                    List<ItemStack> items = new ArrayList<>();
                    items.add(item);
                    getBlackMarketInventory().pageContent().put(currentPage, items);
                }

                currentIndex++;

                if (currentIndex >= slots - 1) {
                    currentPage += 1;
                    currentIndex = 0;
                }
            }
        }
    }
}
