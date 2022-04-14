package net.candorservices.lisacek.altars.commands;

import net.candorservices.lisacek.altars.Altars;
import net.candorservices.lisacek.altars.utils.Colors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class AltarCommand implements CommandExecutor {

    private final Altars plugin;

    public AltarCommand(Altars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return true;
        if (!sender.hasPermission("altars.reload") && !sender.hasPermission("altars.*") && !sender.hasPermission("*")) {
            sender.sendMessage(Colors.translateColors("&cYou don't have permissions on this command!"));
        }
        if ("reload".equals(args[0])) {
            plugin.getManager().getAltars().values().forEach(altar -> {
              altar.getLocation().getWorld().getEntities().stream().filter(e -> e.getEntityId() == altar.getMobId()).forEach(Entity::remove);
            });
            plugin.loadConfig();
            plugin.loadAltars();
            plugin.getManager().loadAltars();
            sender.sendMessage(Colors.translateColors(plugin.getConfig().getString("prefix") + "&aThe plugin was reloaded!"));
        } else {
            sender.sendMessage(Colors.translateColors("&c/altars reload"));
        }
        return true;
    }

}
