package net.candorservices.lisacek.altars.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.candorservices.lisacek.altars.Altars;
import net.candorservices.lisacek.altars.utils.Colors;
import net.candorservices.lisacek.altars.utils.MenuUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AltarsListMenu implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("altars-list")
            .provider(new AltarsListMenu())
            .size(6, 9)
            .manager(Altars.getInstance().getInvManager())
            .title(Colors.translateColors("Altars"))
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        update(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        AtomicInteger row = new AtomicInteger(0);
        AtomicInteger col = new AtomicInteger(0);
        Altars.getInstance().getManager().getAltars().forEach((id, altar) -> {
            if (col.get() == 8) {
                col.set(0);
                row.getAndIncrement();
            }
            contents.set(row.get(), col.get(), ClickableItem.of(MenuUtil.buildItem(altar.getReplaceMaterial(), "&6" + altar.getId(), new ArrayList<>(), 1), e -> {
                AltarsSettingsMenu.INVENTORY.open(player);
            }));
        });
    }


}