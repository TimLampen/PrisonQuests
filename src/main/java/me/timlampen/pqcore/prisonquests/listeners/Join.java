package me.timlampen.pqcore.prisonquests.listeners;

import me.timlampen.pqcore.prisonquests.PQuests;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public class Join implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PQuests.getInstance().loadPlayer(player);
    }
}
