package me.timlampen.prisonquests.prisoncrafting.listeners;

import me.timlampen.prisonquests.prisoncrafting.PCrafting;
import me.timlampen.prisonquests.prisoncrafting.PCraftingItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Timothy Lampen on 6/21/2018.
 */
public class Click implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Inventory inv = event.getClickedInventory();
        if(inv == null)
            return;
        if(!inv.getName().equalsIgnoreCase(PCrafting.getInstance().getCraftingInv().getName()))
            return;
        event.setCancelled(true);
        ItemStack is = event.getCurrentItem();
        if(is == null)
            return;

        Optional<PCraftingItem> optItem = PCrafting.getInstance().getItems().stream().filter(item -> item.getDisplay().equals(is)).findFirst();
        if(!optItem.isPresent())
            return;

        HashMap<ItemStack, Integer> copyMats = new HashMap<>();
        optItem.get().getMaterials().forEach(mat -> copyMats.put(mat, mat.getAmount()));

        boolean playerHasItems = true;

        for(ItemStack ingredient :  optItem.get().getMaterials()) {
            if(!player.getInventory().containsAtLeast(ingredient, ingredient.getAmount())) {
                playerHasItems = false;
                break;
            }
        }

        if(playerHasItems) {
            player.getInventory().removeItem(optItem.get().getMaterials().toArray(new ItemStack[]{}));
            player.getInventory().addItem(optItem.get().getProduct());
            player.updateInventory();
            optItem.get().getCommands().forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName())));
        }
    }
}
