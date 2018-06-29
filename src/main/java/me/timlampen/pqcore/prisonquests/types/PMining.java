package me.timlampen.pqcore.prisonquests.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Timothy Lampen on 6/23/2018.
 * each player has local copy
 * can be partially finished
 */
public class PMining extends PQuestType {

    private HashMap<ItemStack, Integer> blocks;
    public PMining(HashMap<ItemStack, Integer> blocks){
        super(QuestType.MINING);
        this.blocks = blocks;
    }

    public HashMap<ItemStack, Integer> getBlocks() {
        return blocks;
    }

    @Override
    public boolean isComplete(Player player) {
        return blocks.values().stream().mapToInt(i -> i).sum()==0;
    }
}
