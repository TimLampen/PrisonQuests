package me.timlampen.pqcore.prisonquests.listeners;

import me.timlampen.pqcore.prisonquests.PQuests;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public class Quit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        PQuests.getInstance().unloadPlayer(player);
    }
}
