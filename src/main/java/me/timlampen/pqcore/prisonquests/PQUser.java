package me.timlampen.pqcore.prisonquests;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public class PQUser {

    private final Player player;
    private final Set<Quest> activeQuests = new HashSet<>();
    private final Set<Integer> completedQuests = new HashSet<>();
    public PQUser(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void addNewQuest(Quest q) {
        if(completedQuests.contains(q.getId()) || activeQuests.str)
    }
}
