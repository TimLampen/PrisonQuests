package me.timlampen.pqcore.prisonquests.types;

import org.bukkit.entity.Player;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public abstract class PQuestType {

    private final QuestType type;
    public PQuestType(QuestType type){
        this.type = type;
    }


    public abstract boolean isComplete(Player player);

    public QuestType getType() {
        return type;
    }
}
