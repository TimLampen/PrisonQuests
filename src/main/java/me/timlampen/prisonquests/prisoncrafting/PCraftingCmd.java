package me.timlampen.prisonquests.prisoncrafting;

import me.timlampen.prisonquests.Lang;
import me.timlampen.prisonquests.PQuests;
import me.timlampen.prisonquests.prisonfishing.PFishing;
import me.timlampen.prisonquests.prisonfishing.PFishingRod;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Timothy Lampen on 6/21/2018.
 */
public class PCraftingCmd implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equals("pcrafting")) {
            if(!sender.isOp()){
                sender.sendMessage(Lang.PRISONTECH.f("&cYou do not have permission to use this command."));
                return false;
            }
            if(!(sender instanceof Player)) {
                sender.sendMessage(Lang.PRISONTECH.f("&cError: You must be a player to issue this command."));
                return false;
            }
            Player player = (Player)sender;
            if(args.length==0 || args[0].equalsIgnoreCase("help")){
                sender.sendMessage(Lang.PRISONTECH.f("&6Here are the avaliable sub-commands:"));
                sender.sendMessage(Lang.PRISONTECH.f("&c/pcrafting addingredient <id> &6- Adds the item in your hand to the cost of the crafting recipe"));
                sender.sendMessage(Lang.PRISONTECH.f("&c/pcrafting openinv &6- Opens the inventory with all avaliable crafting recipes."));
                sender.sendMessage(Lang.PRISONTECH.f("&c/pcrafting help &6- Views avaliable commands."));
                return false;
            }


            switch (args[0].toLowerCase()) {
                case "addingredient": {
                    int id;
                    try {
                        id = Integer.parseInt(args[1]);
                    }catch (NumberFormatException nfe) {
                        sender.sendMessage(Lang.NULL_NUMBER.f(""));
                        return false;
                    }

                    Optional<PCraftingItem> optItem = PCrafting.getInstance().getItems().stream().filter(item -> item.getId()==id).findFirst();
                    if(!optItem.isPresent()) {
                        sender.sendMessage(Lang.PRISONTECH.f("&cError: Unable to find a crafting recipe with that id."));
                    }

                    ItemStack hand = player.getInventory().getItemInMainHand();
                    if(hand==null || hand.getType()==Material.AIR) {
                        player.sendMessage(Lang.PRISONTECH.f("&cError: You cannot add air as an ingredient"));
                        return false;
                    }

                    optItem.get().getMaterials().add(hand);
                    PCrafting.getInstance().getConfig().set(id + ".materials", optItem.get().getMaterials().stream().map(PQuests::serializeItem).collect(Collectors.toList()));
                    try {
                        PCrafting.getInstance().getConfig().save(new File(PQuests.getInstance().getDataFolder(), "crafting.yml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }

                    break;
                }

                case "openinv": {
                    PCrafting.getInstance().getCraftingInv().show(player);
                    break;
                }
            }
        }
        return true;
    }
}
