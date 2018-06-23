package me.timlampen.pqcore.prisonquests;

import me.timlampen.pqcore.prisonquests.types.PQuestType;
import me.timlampen.pqcore.prisonquests.types.QuestType;

import java.util.Set;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public class Quest {

    private String permission;
    private Set<String> completionCommands;
    private int id;
    private PQuestType type;

    public Quest(int id, PQuestType type, String permission, Set<String> completionCommands) {
        this.id = id;
        this.type = type;
        this.permission = permission;
        this.completionCommands =completionCommands;
    }

    public int getId() {
        return id;
    }

    public PQuestType getType() {
        return type;
    }

    public Set<String> getCompletionCommands() {
        return completionCommands;
    }

    public String getPermission() {
        return permission;
    }
}
