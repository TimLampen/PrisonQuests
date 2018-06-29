package me.timlampen.pqcore.prisonquests.types;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Timothy Lampen on 6/23/2018.
 * each player will have local copy
 * Cannot be partially finished
 */
public class PEnchanting extends PQuestType {

    private HashMap<Enchantment, Integer> enchants;

    public PEnchanting(HashMap<Enchantment, Integer> enchants) {
        super(QuestType.ENCHANTING);
        this.enchants = enchants;
    }

    @Override
    public boolean isComplete(Player player){
        HashMap<Enchantment, Integer> copyEnchants = new HashMap<>(enchants);
        for(ItemStack is : player.getInventory().getContents()) {
            if(is==null || is.getEnchantments().size()==0)
                continue;
            for(Map.Entry<Enchantment, Integer> entry : is.getEnchantments().entrySet()) {
                if(copyEnchants.containsKey(entry.getKey()) && entry.getValue()==copyEnchants.get(entry.getKey()))
                    copyEnchants.remove(entry.getKey());
            }
        }
        return copyEnchants.size()==0;
    }
}
