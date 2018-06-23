package me.timlampen.pqcore.prisonquests.types;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public class PTalking extends PQuestType {

    private Set<String> npcNames = new HashSet<>();
    public PTalking() {
        super(QuestType.TALKING);
    }

    public Set<String> getNpcNames() {
        return npcNames;
    }

    @Override
    public boolean isComplete(Player player) {
        return npcNames.size()==0;
    }
}
