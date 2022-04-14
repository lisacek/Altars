package net.candorservices.lisacek.altars;

import net.candorservices.lisacek.altars.commands.AltarCommand;
import net.candorservices.lisacek.altars.cons.ConsoleOutput;
import net.candorservices.lisacek.altars.enums.ConsoleColor;
import net.candorservices.lisacek.altars.events.DamageEntityEvent;
import net.candorservices.lisacek.altars.events.EyePlaceEvent;
import net.candorservices.lisacek.altars.events.mythical.MobDeathEvent;
import net.candorservices.lisacek.altars.manager.AltarsManager;
import net.candorservices.lisacek.altars.manager.PlaceholderManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Altars extends JavaPlugin {

    private static Altars instance = null;

    private final ConsoleOutput console = new ConsoleOutput(ConsoleColor.CYAN + "Altars " + ConsoleColor.DARK_GRAY_BOLD + "| " + ConsoleColor.RESET);

    private final AltarsManager manager = new AltarsManager(this);

    private YamlConfiguration config;

    private YamlConfiguration altars;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        loadAltars();
        manager.loadAltars();
        getCommand("altars").setExecutor(new AltarCommand(this));
        getServer().getPluginManager().registerEvents(new MobDeathEvent(this), this);
        getServer().getPluginManager().registerEvents(new EyePlaceEvent(this), this);
        getServer().getPluginManager().registerEvents(new DamageEntityEvent(), this);
        new PlaceholderManager().register();
        console.info(ConsoleColor.GRAY + "Plugin was enabled!");
    }

    @Override
    public void onDisable() {
        console.info(ConsoleColor.GRAY + "Plugin was disabled!");
    }

    public static Altars getInstance() {
        return instance;
    }

    public void loadConfig() {
        File customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        config = new YamlConfiguration();
        try {
          config.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void loadAltars() {
        File customConfigFile = new File(getDataFolder(), "altars.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("altars.yml", false);
        }
        altars = new YamlConfiguration();
        try {
            altars.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public AltarsManager getManager() {
        return manager;
    }

    public ConsoleOutput getConsole() {
        return console;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public YamlConfiguration getAltars() {
        return altars;
    }

    public void setAltars(YamlConfiguration altars) {
        this.altars = altars;
    }
}
