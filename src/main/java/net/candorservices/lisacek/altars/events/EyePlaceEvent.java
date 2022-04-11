package net.candorservices.lisacek.altars.events;

import net.candorservices.lisacek.altars.Altars;
import net.candorservices.lisacek.altars.cons.Altar;
import net.candorservices.lisacek.altars.cons.AltarEvent;
import net.candorservices.lisacek.altars.enums.EventType;
import net.candorservices.lisacek.altars.utils.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class EyePlaceEvent implements Listener {

    private final Altars plugin;

    public EyePlaceEvent(Altars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEyePlace(PlayerInteractEvent event) {
        if (plugin.getManager().isNearAltar(event.getPlayer())) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Altar altar = plugin.getManager().getNearAltar(event.getPlayer());
                Block block = event.getClickedBlock();
                ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
                if (stack.getType() == Material.AIR) return;
                ItemMeta meta = stack.getItemMeta();
                if (event.getClickedBlock().getType() == altar.getOriginalMaterial() && altar.getActivationItem().equals(meta.getDisplayName())) {
                    if (altar.isFight()) {
                        event.getPlayer().sendMessage(Colors.translateColors(plugin.getConfig().getString("prefix") + plugin.getConfig().getString("altars.messages.in-fight")));
                        event.setCancelled(true);
                        return;
                    }
                    if (event.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME && altar.getReplaceMaterial() == Material.ENDER_PORTAL_FRAME) {
                        byte data = block.getData();
                        if (data < (byte) 4) {
                            BlockState blockState = block.getState();
                            MaterialData blockData = blockState.getData();
                            blockData.setData((byte) 4);
                            blockState.update(true);
                        } else {
                            event.setCancelled(true);
                            return;
                        }
                    } else {
                        event.getClickedBlock().setType(altar.getReplaceMaterial());
                    }
                    AltarEvent altarEvent = altar.getEvents().get(EventType.ALTAR_PLACED);
                    altar.incrementPlaced();

                    if (plugin.getConfig().getBoolean("altars.messages.eye-placed.enabled")) {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.sendMessage(Colors.translateColors(plugin.getConfig().getString("altars.messages.eye-placed.message")
                                    .replace("%player%", event.getPlayer().getName())
                                    .replace("%altar%", altar.getName()))
                                    .replace("%remaining%", "" + (altar.getTotal() - altar.getPlaced()))
                                    .replace("%placed%", "" + (altar.getPlaced()))
                                    .replace("%max%", "" + altar.getTotal())
                            );
                        });
                    }

                    if (altarEvent.isSoundEnabled()) {
                        event.getPlayer().playSound(event.getPlayer().getLocation(), altarEvent.getSound(), (float) altarEvent.getVolume(), (float) altarEvent.getPitch());
                    }

                    altar.setLastPlacedBy(event.getPlayer());
                    altarEvent.getCommands()
                            .forEach(cmd -> {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                                        .replace("%player%", event.getPlayer().getName())
                                );
                            });
                    ItemStack st = event.getPlayer().getInventory().getItemInMainHand().clone();
                    st.setAmount(st.getAmount() - 1);
                    event.getPlayer().getInventory().setItemInMainHand(st);
                    event.setCancelled(true);
                }
            }
        }
    }
}
