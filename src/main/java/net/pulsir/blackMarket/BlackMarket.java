package net.pulsir.blackMarket;

import lombok.Getter;
import net.pulsir.blackMarket.command.SellCommand;
import net.pulsir.blackMarket.marketplace.manager.MarketPlaceManager;
import net.pulsir.blackMarket.mongo.MongoManager;
import net.pulsir.blackMarket.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

@Getter
public final class BlackMarket extends JavaPlugin {

    @Getter private static BlackMarket instance;

    private Config configuration, language;

    private MongoManager mongoManager;
    private MarketPlaceManager marketPlaceManager;

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfiguration();
        this.loadCommand();
        this.loadListeners(Bukkit.getPluginManager());

        this.mongoManager = new MongoManager();

        this.marketPlaceManager = new MarketPlaceManager();
        this.marketPlaceManager.load();
    }

    @Override
    public void onDisable() {
        this.marketPlaceManager.save();

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
    }

    private void loadListeners(PluginManager pluginManager) {

    }
}
