package me.timlampen.prisonquests.menu.listeners;

import me.timlampen.prisonquests.menu.Menu;
import me.timlampen.prisonquests.prisonenchanting.PEnchanting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class MenuClose implements Listener {

    @EventHandler
    public void onMenuClose(MenuCloseEvent event){
        Player player = event.getPlayer();
        Menu menu = event.getMenu();
        InventoryCloseEvent close = event.getClose();

        switch (menu.getName()) {
            case "enchant_2": {
                PEnchanting.getInstance().getSelectedEnchants().remove(player.getUniqueId());

                ItemStack inputtedItem = close.getInventory().getItem(10);
                if(inputtedItem==null || inputtedItem.getType()==Material.AIR)
                    return;
                player.getInventory().addItem(inputtedItem);
                break;
            }
        }
    }
}
