package net.candorservices.lisacek.altars.gui;

import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.candorservices.lisacek.altars.Altars;
import net.candorservices.lisacek.altars.utils.Colors;
import org.bukkit.entity.Player;

public class AltarsSettingsMenu implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("altars-settings")
            .provider(new AltarsSettingsMenu())
            .size(6, 9)
            .manager(Altars.getInstance().getInvManager())
            .title(Colors.translateColors("Altars Settings"))
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        update(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
