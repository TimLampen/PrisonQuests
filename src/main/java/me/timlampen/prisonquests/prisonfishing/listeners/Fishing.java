package me.timlampen.prisonquests.prisonfishing.listeners;

import me.timlampen.prisonquests.prisonfishing.PFishing;
import me.timlampen.prisonquests.prisonfishing.PFishingRod;
import org.bukkit.Bukkit;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Timothy Lampen on 6/20/2018.
 */
public class Fishing implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent event){
        Player player = event.getPlayer();
        PlayerFishEvent.State state = event.getState();
        Optional<PFishingRod> rod = PFishing.getInstance().getPFishingRod(player.getInventory().getItemInMainHand());
        if(PFishing.getInstance().getLastCast().containsKey(player.getUniqueId()))
            PFishing.getInstance().getLastCast().remove(player.getUniqueId());

        if(!rod.isPresent())
            return;

        switch (state) {
            case CAUGHT_ENTITY:
            case CAUGHT_FISH:

                if(event.getCaught()!=null)
                    event.getCaught().remove();

                if(event.getHook()!=null)
                    event.getHook().remove();

                PFishing.getInstance().awardFishingReward(player, rod.get());

            case FISHING:
                PFishing.getInstance().getLastCast().put(player.getUniqueId(), System.currentTimeMillis());
                break;
        }
    }
}
