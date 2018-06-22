package me.timlampen.prisonquests.prisoncrafting;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Timothy Lampen on 6/21/2018.
 */
public class PCraftingItem {

    private Set<ItemStack> materials;
    private ItemStack product, display;
    private int id;
    private List<String> commands;

    public PCraftingItem(int id, ItemStack product, ItemStack display, Set<ItemStack> materials, List<String> commands){
        this.materials = materials;
        this.product = product;
        this.id = id;
        this.display = display;
        this.commands = commands;
    }

    public int getId() {
        return id;
    }

    public List<String> getCommands() {
        return commands;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public Set<ItemStack> getMaterials() {
        return materials;
    }

    public ItemStack getProduct() {
        return product;
    }
}
