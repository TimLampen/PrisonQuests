package me.timlampen.pqcore.prisonquests.types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Timothy Lampen on 6/23/2018.
 *
 * each player will have local copy
 * Cannot be partially finished
 */
public class PCrafting extends PQuestType {

    private HashMap<ItemStack, Integer> items;
    public PCrafting(HashMap<ItemStack, Integer> items) {
        super(QuestType.CRAFTING);
        this.items = items;
    }

    @Override
    public boolean isComplete(Player player) {
        return items.entrySet().stream().anyMatch(entry -> !player.getInventory().containsAtLeast(entry.getKey(), entry.getValue()));

    }

    public HashMap<ItemStack, Integer> getItems() {
        return items;
    }
}
