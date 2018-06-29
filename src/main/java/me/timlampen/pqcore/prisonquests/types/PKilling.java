package me.timlampen.pqcore.prisonquests.types;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Timothy Lampen on 6/23/2018.
 * each player will have local copy
 * Can be partially finished
 */
public class PKilling extends PQuestType {

    private HashMap<EntityType, Integer> mobs;

    public PKilling(HashMap<EntityType, Integer> mobs) {
        super(QuestType.KILLING);
        this.mobs = mobs;
    }

    @Override
    public boolean isComplete(Player player) {
        return mobs.values().stream().mapToInt(i -> i).sum()==0;
    }

    public HashMap<EntityType, Integer> getMobs() {
        return mobs;
    }
}
