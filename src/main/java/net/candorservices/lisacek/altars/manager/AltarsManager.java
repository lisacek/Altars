package net.candorservices.lisacek.altars.manager;

import net.candorservices.lisacek.altars.Altars;
import net.candorservices.lisacek.altars.cons.Altar;
import net.candorservices.lisacek.altars.enums.ConsoleColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AltarsManager {

    private final Altars plugin;

    private final HashMap<Integer, Altar> altars = new HashMap<>();

    private final HashMap<Integer, Altar> inFight = new HashMap<>();

    public AltarsManager(Altars plugin) {
        this.plugin = plugin;
    }

    public void loadAltars() {
        plugin.getConsole().info(ConsoleColor.GRAY + "Loading altars...");
        altars.clear();
        inFight.clear();
        ConfigurationSection altarsSection = plugin.getAltars().getConfigurationSection("altars");
        altarsSection.getKeys(false).forEach(id -> {
            Altar altar = new Altar(Integer.parseInt(id), altarsSection.getConfigurationSection(id));
            altar.loadAltar();
            altar.startTask();
            altars.put(altar.getId(), altar);
        });
        plugin.getConsole().info(ConsoleColor.GRAY + "Loaded " + ConsoleColor.CYAN + altars.size() + ConsoleColor.GRAY + " altars.");
    }

    public HashMap<Integer, Altar> getAltars() {
        return altars;
    }

    public HashMap<Integer, Altar> getInFight() {
        return inFight;
    }

    public Altar getAltarByMobId(int mobId) {
        for (Altar altar : altars.values()) {
            if (altar.getMobId() == mobId) {
                return altar;
            }
        }
        return null;
    }

    public Altar getNearAltar(Player player) {
        for (Altar altar : altars.values()) {
            if (altar.getLocation().distance(player.getLocation()) < 5) {
                return altar;
            }
        }
        return null;
    }

    public boolean isNearAltar(Player player) {
        for (Altar altar : altars.values()) {
            if (altar.getLocation().distance(player.getLocation()) < 5) {
                return true;
            }
        }
        return false;
    }
}
