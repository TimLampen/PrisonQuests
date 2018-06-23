package me.timlampen.pqcore.prisonquests;

import me.timlampen.pqcore.Module;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public class PQuests extends Module {
    private static PQuests instance;
    private Set<Quest> masterQuests = new HashSet<>();//Objects in this set SHOULD NOT BE EDITED
    private Set<PQUser> users  =new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }

    public static PQuests getInstance() {
        return instance;
    }

    public void loadPlayer(Player player){
        if(this.users.stream().anyMatch(user -> user.getPlayer().equals(player)))
            return;
        this.users.add(new PQUser(player));
    }

    public void unloadPlayer(Player player){
        this.users.removeIf(pqUser -> pqUser.getPlayer().equals(player));
    }
}
