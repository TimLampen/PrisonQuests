package me.timlampen.pqcore.prisonquests;

import org.bukkit.Bukkit;
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

    public boolean addNewQuest(Quest q) {
        if(completedQuests.contains(q.getId()) || activeQuests.stream().anyMatch(p -> p.getId()==q.getId())){
            return false;
        }

        this.activeQuests.add(q);
        return true;
    }

    public boolean completeQuest(Quest q) {
        if(this.activeQuests.stream().anyMatch(p -> q.getId()==p.getId()) && q.getType().isComplete(player)) {
            q.getCompletionCommands().forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", player.getName())));
            return true;
        }
        return false;
    }

    public Set<Integer> getCompletedQuests() {
        return completedQuests;
    }

    public Set<Quest> getActiveQuests() {
        return activeQuests;
    }
}
