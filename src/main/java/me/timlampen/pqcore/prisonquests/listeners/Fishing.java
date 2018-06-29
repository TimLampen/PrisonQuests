package me.timlampen.pqcore.prisonquests.listeners;

import me.timlampen.pqcore.prisonfishing.PFishEvent;
import me.timlampen.pqcore.prisonquests.PQUser;
import me.timlampen.pqcore.prisonquests.PQuests;
import me.timlampen.pqcore.prisonquests.types.PFishing;
import me.timlampen.pqcore.prisonquests.types.QuestType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Timothy Lampen on 6/27/2018.
 */
public class Fishing implements Listener {
    
    @EventHandler
    public void onPFishing(PFishEvent event) {
        Player player = event.getPlayer();
        ItemStack award = event.getAward();

        Optional<PQUser> optUser = PQuests.getInstance().getPQUser(player.getUniqueId());
        if(!optUser.isPresent())
            return;
        optUser.get().getActiveQuests().stream().filter(quest -> quest.getType().getType()== QuestType.FISHING).forEach(quest -> {
            PFishing q = (PFishing) quest.getType();
            Iterator<Map.Entry<ItemStack, Integer>> iter = q.getDrops().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<ItemStack, Integer> entry = iter.next();
                if(entry.getKey().getType()!=award.getType())
                    continue;
                if(entry.getValue()==1)
                    iter.remove();
                else
                    q.getDrops().put(entry.getKey(), entry.getValue()-1);
            }
        });
    }
}
