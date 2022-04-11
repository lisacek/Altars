package net.candorservices.lisacek.altars.commands;

import net.candorservices.lisacek.altars.gui.AltarsListMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AltarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) return true;
        switch (args[0]) {
            case "create":
            case "add":
            case "sound":
            case "delete":
            case "list":
                AltarsListMenu.INVENTORY.open((Player) sender);
                break;
        }

        return true;
    }

}
