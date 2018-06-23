package me.timlampen.pqcore.prisonfishing.listeners;

import me.timlampen.pqcore.prisonfishing.PFishing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class Interact implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!event.getAction().toString().contains("RIGHT"))
            return;
        if(!PFishing.getInstance().getPFishingRod(player.getItemInHand()).isPresent())
            return;
        if(!PFishing.getInstance().getLastCast().containsKey(player.getUniqueId()))
            return;
        PFishing.getInstance().getLastCast().remove(player.getUniqueId());
    }
}
