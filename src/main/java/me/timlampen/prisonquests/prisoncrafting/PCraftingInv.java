package me.timlampen.prisonquests.prisoncrafting;

import me.timlampen.prisonquests.PQuests;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by Timothy Lampen on 6/21/2018.
 */
public class PCraftingInv {

    private Inventory inv;
    private String name;

    public PCraftingInv(String name) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.inv = Bukkit.createInventory(null, getInvSize(PCrafting.getInstance().getItems().size()), ChatColor.translateAlternateColorCodes('&', "&6Custom Crafting Recipes"));

        int counter = 0;
        for(PCraftingItem item : PCrafting.getInstance().getItems()) {
            if(item == null)
                continue;
            inv.setItem(counter, item.getDisplay());
            counter++;
        }
    }

    public String getName(){
        return name;
    }

    private int getInvSize(int numToRound){
        int remainder = numToRound % 9;
        if (remainder == 0){
            return numToRound;
        }
        return numToRound + 9 - remainder;
    }

    public void show(Player owner){
        if(owner == null || !owner.isOnline())
            return;
        owner.openInventory(inv);
    }
}
