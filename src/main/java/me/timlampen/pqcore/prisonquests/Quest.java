package me.timlampen.pqcore.prisonquests;

import me.timlampen.pqcore.PQCore;
import me.timlampen.pqcore.prisonquests.types.PQuestType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public class Quest {

    private final String permission;
    private final Set<String> completionCommands;
    private final int id;
    private final String npcGiverName;
    private final PQuestType type;
    private String region;
    private final String details;

    public Quest(int id, PQuestType type, String permission, Set<String> completionCommands, String npcGiverName, String details) {
        this.id = id;
        this.npcGiverName = npcGiverName;
        this.type = type;
        this.permission = permission;
        this.completionCommands = completionCommands;
        this.details = ChatColor.translateAlternateColorCodes('&', details);
    }


    public Quest(int id, PQuestType type, String permission, Set<String> completionCommands, String npcGiverName, String details, String region){
        this(id, type, permission, completionCommands, npcGiverName, details);
        this.region = region;
    }

    public String getDetails() {
        return details;
    }

    public boolean playerIsInCorrectRegion(Player player){
        if(this.region == null || this.region.equals(""))
            return true;
        return PQCore.getInstance().getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation()).getRegions().stream().anyMatch(region1 -> region1.getId().equals(this.region));
    }

    public String getRegion() {
        return region;
    }

    public String getNpcGiverName() {
        return npcGiverName;
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
