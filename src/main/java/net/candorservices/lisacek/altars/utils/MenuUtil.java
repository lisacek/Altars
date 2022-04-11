package net.candorservices.lisacek.altars.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MenuUtil {

    private MenuUtil() {

    }

    public static ItemStack buildItem(Material mat, String name, List<String> lore, int count) {
        ItemStack item = new ItemStack(mat, count);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);

        if (lore.size() != 0) {
            meta.setLore(lore);
        }

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }
}
