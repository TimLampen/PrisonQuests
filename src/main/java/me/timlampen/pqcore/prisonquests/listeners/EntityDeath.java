package me.timlampen.pqcore.prisonquests.listeners;

import me.timlampen.pqcore.prisonquests.PQUser;
import me.timlampen.pqcore.prisonquests.PQuests;
import me.timlampen.pqcore.prisonquests.types.PKilling;
import me.timlampen.pqcore.prisonquests.types.QuestType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Timothy Lampen on 6/27/2018.
 */
public class EntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Entity e = event.getEntity();
        if(e != null && ((LivingEntity)e).getKiller()!=null) {
            Player player = ((LivingEntity) e).getKiller();
            Optional<PQUser> optUser = PQuests.getInstance().getPQUser(player.getUniqueId());
            if(!optUser.isPresent())
                return;
            optUser.get().getActiveQuests().stream().filter(quest -> quest.getType().getType()== QuestType.KILLING).forEach(quest -> {
                    PKilling q = (PKilling)quest.getType();
                    Iterator<Map.Entry<EntityType, Integer>> iter = q.getMobs().entrySet().iterator();

                    if(!quest.playerIsInCorrectRegion(player))
                        return;

                    while (iter.hasNext()) {
                        Map.Entry<EntityType, Integer> entry = iter.next();
                        if(entry.getKey()!=e.getType())
                            continue;
                        if(entry.getValue()==1)
                            iter.remove();
                        else
                            q.getMobs().put(entry.getKey(), entry.getValue()-1);
                    }
            });
        }
    }

}
