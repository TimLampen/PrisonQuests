package me.timlampen.prisonquests.prisonenchanting;

import org.bukkit.Material;

import java.util.HashMap;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class PEnchant {

    private final HashMap<Integer, Integer> compoundChances;
    private final HashMap<Material, MaterialEnchantChance> matChances;
    private final int level;

    public PEnchant(int level, HashMap<Integer, Integer> compoundChances, HashMap<Material, MaterialEnchantChance> matChances) {
        this.level = level;
        this.compoundChances = compoundChances;
        this.matChances = matChances;

    }

    public int getLevel() {
        return level;
    }

    public HashMap<Integer, Integer> getCompoundChances() {
        return compoundChances;
    }

    public HashMap<Material, MaterialEnchantChance> getMatChances() {
        return matChances;
    }
}
