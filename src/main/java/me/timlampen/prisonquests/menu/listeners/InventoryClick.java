package me.timlampen.prisonquests.menu.listeners;

import me.timlampen.prisonquests.menu.Menu;
import me.timlampen.prisonquests.menu.PMenus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class InventoryClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory inv = event.getClickedInventory();
        if(inv==null || inv.getHolder()!=null)
            return;
        Optional<Menu> optMenu = PMenus.getInstance().getMenus().values().stream().filter(m -> ChatColor.translateAlternateColorCodes('&', m.getTitle()).equals(inv.getName())).findFirst();
        Bukkit.getConsoleSender().sendMessage(optMenu.isPresent() + "");
        if(!optMenu.isPresent())
            return;


        Bukkit.getPluginManager().callEvent(new MenuClickEvent(event, optMenu.get()));
    }
}
