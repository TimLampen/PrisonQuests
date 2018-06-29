package me.timlampen.pqcore.prisonquests.listeners;

import me.timlampen.pqcore.Lang;
import me.timlampen.pqcore.prisonquests.PQUser;
import me.timlampen.pqcore.prisonquests.PQuests;
import me.timlampen.pqcore.prisonquests.types.PMining;
import me.timlampen.pqcore.prisonquests.types.PTalking;
import me.timlampen.pqcore.prisonquests.types.QuestType;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Timothy Lampen on 6/27/2018.
 */
public class EntityInteract implements Listener {


    @EventHandler//for handlingt the Quests
    public void onEntityInteract(NPCRightClickEvent event){
        Player player = event.getClicker();
        NPC npc = event.getNPC();
        Optional<PQUser> optUser = PQuests.getInstance().getPQUser(player.getUniqueId());
        if(!optUser.isPresent())
            return;
        optUser.get().getActiveQuests().stream().filter(quest -> quest.getType().getType()== QuestType.TALKING).forEach(quest -> {
            PTalking q = (PTalking) quest.getType();
            Iterator<String> iter = q.getNpcNames().iterator();
            while (iter.hasNext()) {
                String name = iter.next();
                if(!ChatColor.stripColor(name).equals(ChatColor.stripColor(npc.getName())))
                    return;
                iter.remove();
            }
        });
    }


    @EventHandler
    public void onEntityInteract2(NPCRightClickEvent event){
        Player player = event.getClicker();
        NPC npc = event.getNPC();
        PQuests.getInstance().getMasterQuests().stream().filter(q -> ChatColor.stripColor(q.getNpcGiverName()).equals(ChatColor.stripColor(npc.getName()))).forEach(q -> {

            Optional<PQUser> optUser = PQuests.getInstance().getPQUser(player.getUniqueId());
            if(!optUser.isPresent())
                return;

            player.sendMessage(Lang.PRISONTECH.f("&9" + npc.getName() + "&6would like you give you a quest."));
            player.sendMessage(q.getDetails());
            TextComponent msg = new TextComponent("Accept Quest");
            msg.setColor(optUser.get().getActiveQuests().stream().anyMatch(q2 -> q.getId()==q2.getId()) || optUser.get().getCompletedQuests().contains(q.getId()) ? ChatColor.RED : ChatColor.GREEN);

        });
    }
}
