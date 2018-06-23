package me.timlampen.pqcore.prisonfishing.listeners;

import me.timlampen.pqcore.prisonfishing.PFishing;
import me.timlampen.pqcore.prisonfishing.PFishingRod;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Optional;

/**
 * Created by Timothy Lampen on 6/20/2018.
 */
public class Fishing implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent event){
        Player player = event.getPlayer();
        PlayerFishEvent.State state = event.getState();
        Optional<PFishingRod> rod = PFishing.getInstance().getPFishingRod(player.getItemInHand());

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
