package net.candorservices.lisacek.altars.events;

import net.candorservices.lisacek.altars.Altars;
import net.candorservices.lisacek.altars.cons.Altar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class DamageEntityEvent implements Listener {

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        int id = event.getEntity().getEntityId();
        Altar altar = Altars.getInstance().getManager().getAltarByMobId(id);
        if(altar == null) return;
        HashMap<Player, Double> damages = altar.getDamages();
        if(!damages.containsKey((Player) event.getDamager())) {
            damages.put((Player) event.getDamager(), event.getDamage());
        } else {
           damages.put((Player) event.getDamager(), damages.get((Player) event.getDamager()) + event.getDamage());
        }
    }

}
