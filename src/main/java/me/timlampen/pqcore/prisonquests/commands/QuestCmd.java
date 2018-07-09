package me.timlampen.pqcore.prisonquests.commands;

import me.timlampen.pqcore.Lang;
import me.timlampen.pqcore.prisonquests.PQUser;
import me.timlampen.pqcore.prisonquests.PQuests;
import me.timlampen.pqcore.prisonquests.Quest;
import me.timlampen.pqcore.prisonquests.types.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by Timothy Lampen on 6/29/2018.
 */
public class QuestCmd implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player)sender;

        if(args.length==0)
            return false;
        switch (args[0].toLowerCase()) {
            case "aavc": {
                if(args.length!=2)
                    return false;
                int id = Integer.parseInt(args[1]);
                Optional<PQUser> optUser = PQuests.getInstance().getPQUser(player.getUniqueId());
                if(!optUser.isPresent())
                    return false;

                if(optUser.get().getActiveQuests().stream().anyMatch(q2 -> id==q2.getId()) || optUser.get().getCompletedQuests().contains(id))
                    return false;

                optUser.get().getActiveQuests().add(PQuests.getInstance().getMasterQuests().stream().filter(q -> q.getId()==id).findFirst().get().copy());
                player.sendMessage(Lang.PRISONTECH.f("&aYou have started the selected quest!"));
                break;
            }
            case "vvfv":{
                if(args.length!=2)
                    return false;
                int id = Integer.parseInt(args[1]);
                Optional<PQUser> optUser = PQuests.getInstance().getPQUser(player.getUniqueId());
                if(!optUser.isPresent())
                    return false;

                Optional<Quest> quest = optUser.get().getActiveQuests().stream().filter(q -> q.getId()==id).findFirst();
                if(!quest.isPresent())
                    return false;
                if(!quest.get().getType().isComplete(player)) {
                    player.sendMessage(Lang.PRISONTECH.f("&cError: You have not completed this quest! You are missing:"));
                    switch (quest.get().getType().getType()) {

                        case MINING: {
                            PMining type = (PMining) quest.get().getType();
                            type.getBlocks().entrySet().stream().forEach(entry -> {
                                player.sendMessage(Lang.PRISONTECH.f("&6" + entry.getKey().getType() + ": &a" + entry.getValue()));
                            });
                            break;
                        }
                        case KILLING: {
                            PKilling type = (PKilling) quest.get().getType();
                            type.getMobs().entrySet().stream().forEach(entry -> {
                                player.sendMessage(Lang.PRISONTECH.f("&6" + entry.getKey() + ": &a" + entry.getValue()));
                            });
                            break;
                        }
                        case CRAFTING: {
                            PCrafting type = (PCrafting) quest.get().getType();
                            type.getItems().entrySet().stream().forEach(entry -> {
                                player.sendMessage(Lang.PRISONTECH.f("&6" + entry.getKey().getType() + ": &a" + entry.getValue()));
                            });
                            break;
                        }
                        case FISHING: {
                            PFishing type = (PFishing) quest.get().getType();
                            type.getDrops().entrySet().stream().forEach(entry -> {
                                player.sendMessage(Lang.PRISONTECH.f("&6" + entry.getKey().getType() + ": &a" + entry.getValue()));
                            });
                            break;
                        }
                        case TALKING: {
                            PTalking type = (PTalking) quest.get().getType();
                            type.getNpcNames().stream().forEach(entry -> {
                                player.sendMessage(Lang.PRISONTECH.f("&6" + entry));
                            });
                            break;
                        }
                        case ENCHANTING: {
                            PEnchanting type = (PEnchanting) quest.get().getType();
                            type.remainingEnchants(player).entrySet().stream().forEach(entry -> {
                                player.sendMessage(Lang.PRISONTECH.f("&6" + entry.getKey() + ": &a" + entry.getValue()));
                            });
                            break;
                        }
                    }

                    return false;
                }

                player.sendMessage(Lang.PRISONTECH.f("&aCongratulations on completing the quest!"));
                quest.get().getCompletionCommands().forEach(str -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str.replace("%player%", player.getName())));
                optUser.get().getCompletedQuests().add(id);
                optUser.get().getActiveQuests().remove(quest.get());


            }
        }
        return true;
    }
}
