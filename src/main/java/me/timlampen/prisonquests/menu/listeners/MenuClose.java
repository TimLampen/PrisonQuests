package me.timlampen.prisonquests.menu.listeners;

import me.timlampen.prisonquests.menu.Menu;
import me.timlampen.prisonquests.prisonenchanting.PEnchanting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class MenuClose implements Listener {

    @EventHandler
    public void onMenuClose(MenuCloseEvent event){
        Player player = event.getPlayer();
        Menu menu = event.getMenu();

        switch (menu.getName()) {
            case "enchant_2": {
                PEnchanting.getInstance().getSelectedEnchants().remove(player.getUniqueId());
                break;
            }
        }
    }
}
