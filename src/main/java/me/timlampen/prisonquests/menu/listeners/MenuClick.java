package me.timlampen.prisonquests.menu.listeners;

import me.timlampen.prisonquests.menu.Menu;
import me.timlampen.prisonquests.menu.PMenus;
import me.timlampen.prisonquests.prisoncrafting.PCrafting;
import me.timlampen.prisonquests.prisoncrafting.PCraftingItem;
import me.timlampen.prisonquests.prisonenchanting.PEnchanting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class MenuClick implements Listener {

    @EventHandler
    public void onClick(MenuClickEvent event) {
        InventoryClickEvent click = event.getEvent();
        Player player = (Player) click.getWhoClicked();
        Menu m = event.getMenu();
        click.setCancelled(true);

        switch (m.getName()) {
            case "enchant_1": {
                ItemStack is = click.getCurrentItem();
                if(is==null)
                    return;
                if(is.getType()!= Material.STAINED_GLASS)
                    return;
                int slot = click.getSlot();
                int index = -1;

                switch (slot) {
                    case 11:
                        index = 0;
                        break;
                    case 13:
                        index = 1;
                        break;
                    case 15:
                        index = 2;
                        break;
                }

                PEnchanting.getInstance().getSelectedEnchants().put(player.getUniqueId(), PEnchanting.getInstance().getEnchants().get(index));
                PMenus.getInstance().showMenu(player, "enchant_2");
                break;
            }
            case "crafting_items": {
                ItemStack is = click.getCurrentItem();
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
                break;
            }
        }
    }
}
