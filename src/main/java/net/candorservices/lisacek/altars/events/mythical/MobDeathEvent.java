package net.candorservices.lisacek.altars.events.mythical;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import net.candorservices.lisacek.altars.Altars;
import net.candorservices.lisacek.altars.cons.Altar;
import net.candorservices.lisacek.altars.enums.EventType;
import net.candorservices.lisacek.altars.utils.Colors;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MobDeathEvent implements Listener {

    private final Altars plugin;

    public MobDeathEvent(Altars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDead(MythicMobDeathEvent e) {
        if (plugin.getManager().getInFight().containsKey(e.getEntity().getEntityId())) {
            Altar altar = plugin.getManager().getInFight().get(e.getEntity().getEntityId());
            altar.setInFight(false);
            altar.distributeRewards();
            altar.getDamages().clear();
            plugin.getManager().getInFight().remove(e.getEntity().getEntityId());
            if (Altars.getInstance().getConfig().getBoolean("altars.messages.fight-ended.enabled")) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    Colors.translateColors(Altars.getInstance().getConfig().getStringList("altars.messages.fight-ended.message")).forEach(msg -> {
                        player.sendMessage(msg
                                .replace("%boss%", e.getEntity().getCustomName())
                                .replace("%player%", e.getKiller() != null ? e.getKiller().getName() : "No one")
                        );
                    });
                });
                altar.getEvents().get(EventType.ALTAR_END).getCommands().forEach(cmd -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                });
            }
        }
    }

}
