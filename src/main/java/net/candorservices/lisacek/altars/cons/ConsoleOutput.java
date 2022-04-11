package net.candorservices.lisacek.altars.cons;

import net.candorservices.lisacek.altars.utils.Colors;
import org.bukkit.Bukkit;

public class ConsoleOutput {

    private final String prefix;

    public ConsoleOutput(String prefix) {
        this.prefix = prefix;
    }

    public void info(String message) {
        Bukkit.getLogger().info(prefix + message);
    }

    public void warn(String message) {
        Bukkit.getLogger().warning(prefix + message);
    }

}
