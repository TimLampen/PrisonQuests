package me.timlampen.prisonquests.prisonenchanting;

import org.bukkit.enchantments.Enchantment;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class MaterialEnchantChance {
    private final Enchantment enchantment;
    private final int level;
    private final int chance;

    public MaterialEnchantChance(Enchantment enchantment, int level, int chance) {
        this.level = level;
        this.enchantment = enchantment;
        this.chance = chance;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }

    public int getChance() {
        return chance;
    }
}
