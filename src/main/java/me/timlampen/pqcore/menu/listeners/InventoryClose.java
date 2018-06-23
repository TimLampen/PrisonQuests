package me.timlampen.pqcore.menu.listeners;

import me.timlampen.pqcore.menu.Menu;
import me.timlampen.pqcore.menu.PMenus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class InventoryClose implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Inventory inv = event.getInventory();
        Player player = (Player)event.getPlayer();
        if(inv==null || inv.getHolder()!=null)
            return;
        Optional<Menu> optMenu = PMenus.getInstance().getMenus().values().stream().filter(m -> ChatColor.translateAlternateColorCodes('&', m.getTitle()).equals(inv.getName())).findFirst();
        Bukkit.getConsoleSender().sendMessage(optMenu.isPresent() + "");
        if(!optMenu.isPresent())
            return;

        Bukkit.getPluginManager().callEvent(new MenuCloseEvent(player, event, optMenu.get()));
    }
}
