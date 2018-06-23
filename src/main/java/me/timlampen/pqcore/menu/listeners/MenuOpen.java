package me.timlampen.pqcore.menu.listeners;

import me.timlampen.pqcore.menu.Menu;
import me.timlampen.pqcore.prisoncrafting.PCrafting;
import me.timlampen.pqcore.prisoncrafting.PCraftingItem;
import me.timlampen.pqcore.prisonenchanting.PEnchanting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class MenuOpen implements Listener {

    @EventHandler
    public void onMenuOpen(MenuOpenEvent event) {
        Player player = event.getPlayer();
        Menu m = event.getMenu();
        Inventory inv = null;



        switch (m.getName()) {
            case "crafting_items": {
                inv = Bukkit.createInventory(null, getInvSizeByItems(PCrafting.getInstance().getItems().size()), ChatColor.translateAlternateColorCodes('&',m.getTitle()));
                int counter = 0;
                for(PCraftingItem item : PCrafting.getInstance().getItems()) {
                    if(item == null)
                        continue;
                    inv.setItem(counter, item.getDisplay());
                    counter++;
                }
                break;
            }

            case "enchant_1": {
                inv = Bukkit.createInventory(null, m.getRows()*9, ChatColor.translateAlternateColorCodes('&',m.getTitle()));
                for(int i = 0; i < 27; i++)
                    inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7));
                inv.setItem(11, generateSelector(PEnchanting.getInstance().getEnchants().get(0).getLevel(), (short)5));
                inv.setItem(13, generateSelector(PEnchanting.getInstance().getEnchants().get(1).getLevel(), (short)10));
                inv.setItem(15, generateSelector(PEnchanting.getInstance().getEnchants().get(2).getLevel(), (short)14));
                break;
            }
            case "enchant_2": {
                inv = Bukkit.createInventory(null, m.getRows()*9, ChatColor.translateAlternateColorCodes('&',m.getTitle()));
                for(int i = 0; i < 27; i++) {
                    if(i==10)
                        continue;
                    inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
                }

                ItemStack nullItem = new ItemStack(Material.WOOL, 1, (short)14);
                ItemMeta im = nullItem.getItemMeta();
                im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Place Enchantable Item In Empty Slot");
                nullItem.setItemMeta(im);

                int[] is = new int[]{1,9,11,19};
                for(int i : is){
                    inv.setItem(i, nullItem);
                }
                break;
            }
        }




        player.openInventory(inv);
    }

    private ItemStack generateSelector(int level, short color){
        ItemStack is = new ItemStack(Material.STAINED_GLASS, 1, color);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Level &a&l" + level + " &6Enchant"));
        is.setItemMeta(im);
        return is;
    }

    private int getInvSizeByItems(int numToRound){
        int remainder = numToRound % 9;
        if (remainder == 0){
            return numToRound;
        }
        return numToRound + 9 - remainder;
    }
}
