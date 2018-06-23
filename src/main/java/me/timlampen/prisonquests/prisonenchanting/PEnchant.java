package me.timlampen.prisonquests.prisonenchanting;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class PEnchant {

    private final HashMap<Integer, Integer> compoundChances;
    private final HashMap<Material, Set<MaterialEnchantChance>> matChances;
    private final int level, rerollCost;

    public PEnchant(int level, int rerollCost, HashMap<Integer, Integer> compoundChances, HashMap<Material, Set<MaterialEnchantChance>> matChances) {
        this.level = level;
        this.rerollCost = rerollCost;
        this.compoundChances = compoundChances;
        this.matChances = matChances;

    }

    public int getRerollCost() {
        return rerollCost;
    }

    public int getLevel() {
        return level;
    }

    public HashMap<Integer, Integer> getCompoundChances() {
        return compoundChances;
    }

    public HashMap<Material, Set<MaterialEnchantChance>> getMatChances() {
        return matChances;
    }
}
