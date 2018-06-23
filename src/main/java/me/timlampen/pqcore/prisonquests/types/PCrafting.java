package me.timlampen.pqcore.prisonquests.types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Timothy Lampen on 6/23/2018.
 *
 * DO NOT ED
 */
public class PCrafting extends PQuestType {

    private HashMap<ItemStack, Integer> items = new HashMap<>();
    public PCrafting() {
        super(QuestType.CRAFTING);
    }

    @Override
    public boolean isComplete(Player player) {
        return false;
    }

    public HashMap<ItemStack, Integer> getItems() {
        return items;
    }
}
