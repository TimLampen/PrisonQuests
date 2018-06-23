package me.timlampen.pqcore.prisonfishing;

import me.timlampen.pqcore.Lang;
import me.timlampen.pqcore.menu.PMenus;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by Timothy Lampen on 6/20/2018.
 */
public class PFishingRodCmd implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equals("pfishingrod")) {
            if(!sender.isOp()){
                sender.sendMessage(Lang.PRISONTECH.f("&cYou do not have permission to use this command."));
                return false;
            }
            if(args.length==0 || args[0].equalsIgnoreCase("help")){
                sender.sendMessage(Lang.PRISONTECH.f("&6Here are the avaliable sub-commands:"));
                sender.sendMessage(Lang.PRISONTECH.f("&c/pfishingrod give <player> <id> &6- Gives the specified player the fishing rod."));
                sender.sendMessage(Lang.PRISONTECH.f("&c/pfishingrod help &6- Views avaliable commands."));
                return false;
            }


            switch (args[0].toLowerCase()) {
                case "aa": {
                    PMenus.getInstance().showMenu((Player)sender, "enchant_1");
                }
                case "give": {
                    if(args.length!=3){
                        sender.sendMessage(Lang.SYNTAX_ERROR.f(""));
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target==null) {
                        sender.sendMessage(Lang.NULL_PLAYER.f(""));
                        return false;
                    }

                    int id;
                    try {
                        id = Integer.parseInt(args[2]);
                    }catch (NumberFormatException nfe) {
                        sender.sendMessage(Lang.NULL_NUMBER.f(""));
                        return false;
                    }

                    Optional<PFishingRod> rod = PFishing.getInstance().getPFishingRod(id);
                    if(!rod.isPresent()) {
                        sender.sendMessage(Lang.PRISONTECH.f("&cError: No fishing rod exists with that id."));
                        return false;
                    }

                    target.getInventory().addItem(rod.get().generate());
                    target.sendMessage(Lang.PRISONTECH.f("&aYou have been given a custom fishing rod from " + sender.getName() + "."));
                    sender.sendMessage(Lang.PRISONTECH.f("&aThe requested fishing rod has been sent to " + target.getName()) + ".");
                    return true;
                }
            }
        }
        return false;
    }
}
