package me.timlampen.pqcore.prisonquests.types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Timothy Lampen on 6/23/2018.
 * each player has local copy
 * Can be partially finished
 */
public class PFishing extends PQuestType {
    private HashMap<ItemStack, Integer> drops;

    public PFishing(HashMap<ItemStack, Integer> drops) {
        super(QuestType.FISHING);
        this.drops = drops;
    }

    public HashMap<ItemStack, Integer> getDrops() {
        return drops;
    }

    @Override
    public boolean isComplete(Player player) {
        return drops.values().stream().mapToInt(i -> i).sum()==0;
    }
}
