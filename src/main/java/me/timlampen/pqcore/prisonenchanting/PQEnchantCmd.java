package me.timlampen.pqcore.prisonenchanting;

import me.timlampen.pqcore.Lang;
import me.timlampen.pqcore.menu.PMenus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Timothy Lampen on 7/1/2018.
 */
public class PQEnchantCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;
        Player player = (Player)sender;

        if(args.length==0 || args[0].equalsIgnoreCase("help")) {
            player.sendMessage(Lang.PRISONTECH.f("&cError: /pqenchant open"));
        }

        switch (args[0].toLowerCase()) {
            case "open": {
                PMenus.getInstance().showMenu((Player)sender, "enchant_1");
                break;
            }
        }
        return true;
    }
}
