package me.timlampen.pqcore.prisonquests.listeners;

import me.timlampen.pqcore.PQCore;
import me.timlampen.pqcore.prisonquests.PQUser;
import me.timlampen.pqcore.prisonquests.PQuests;
import me.timlampen.pqcore.prisonquests.types.PKilling;
import me.timlampen.pqcore.prisonquests.types.PMining;
import me.timlampen.pqcore.prisonquests.types.QuestType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Timothy Lampen on 6/27/2018.
 */
public class BlockBreak implements Listener {
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block b = event.getBlock();
        Optional<PQUser> optUser = PQuests.getInstance().getPQUser(player.getUniqueId());
        if(!optUser.isPresent())
            return;
        optUser.get().getActiveQuests().stream().filter(quest -> quest.getType().getType()== QuestType.MINING).forEach(quest -> {
            PMining q = (PMining) quest.getType();
            Iterator<Map.Entry<ItemStack, Integer>> iter = q.getBlocks().entrySet().iterator();

            if(!quest.playerIsInCorrectRegion(player))
                return;

            while (iter.hasNext()) {
                Map.Entry<ItemStack, Integer> entry = iter.next();
                if(entry.getKey().getType()!=b.getType())
                    continue;
                if(entry.getValue()==1)
                    iter.remove();
                else
                    q.getBlocks().put(entry.getKey(), entry.getValue()-1);
            }
        });
    }
}
