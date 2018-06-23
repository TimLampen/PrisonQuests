package me.timlampen.pqcore.prisonfishing;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Timothy Lampen on 6/20/2018.
 */
public class PFishingRod {
    private final String name;
    private final List<String> lore;
    private final int time, id;
    private final HashMap<ItemStack, Integer> chances;

    public PFishingRod(int id, String name, List<String> lore, int time, HashMap<ItemStack, Integer> chances){
        this.name = name;
        this.id = id;
        this.lore = lore;
        this.time = time;
        this.chances = chances;
    }

    public ItemStack generate(){
        ItemStack is = new ItemStack(Material.FISHING_ROD);
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        im.setDisplayName(name);
        is.setItemMeta(im);
        return is;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public HashMap<ItemStack, Integer> getChances() {
        return chances;
    }
}
