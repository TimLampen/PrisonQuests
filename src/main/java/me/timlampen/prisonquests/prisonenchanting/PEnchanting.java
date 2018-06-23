package me.timlampen.prisonquests.prisonenchanting;

import me.timlampen.prisonquests.Lang;
import me.timlampen.prisonquests.Module;
import me.timlampen.prisonquests.PQuests;
import me.timlampen.prisonquests.menu.PMenus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class PEnchanting extends Module {

    private static PEnchanting instance;
    private FileConfiguration config;
    private List<PEnchant> enchants = new ArrayList<>();
    private HashMap<UUID, PEnchant> selectedEnchants = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        this.config = PQuests.getConfig("enchanting.yml");
        loadConfig();
        PMenus.getInstance().addMenu("enchant_1","&5Pick an Enchant Level", 3);
        PMenus.getInstance().addMenu("enchant_2","&5The Enchantment Lottery", 3);
    }

    @Override
    public void onDisable() {

    }

    public List<PEnchant> getEnchants() {
        return enchants;
    }

    public HashMap<UUID, PEnchant> getSelectedEnchants() {
        return selectedEnchants;
    }

    public static PEnchanting getInstance() {
        return instance;
    }

    public void loadConfig(){
        File file = new File(PQuests.getInstance().getDataFolder(), "enchanting.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PQuests.getInstance().saveResource("enchanting.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }

        for(String s : config.getConfigurationSection("").getKeys(false)) {
            int level = Integer.parseInt(s);

            HashMap<Integer, Integer> compoundEnchants = new HashMap<>();
            config.getStringList(s + ".compound-enchants").forEach(sEnch -> {
                String[] split = sEnch.split(",");
                compoundEnchants.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            });

            HashMap<Material, Set<MaterialEnchantChance>> matChances = new HashMap<>();
            PQuests.error("for enchantment level " + level);
            for(String sMat : config.getConfigurationSection(s + ".items").getKeys(false)){
                PQuests.error("now loading enchants for " + sMat);
                Set<MaterialEnchantChance> chances = new HashSet<>();
                Material mat = Material.getMaterial(sMat);
                config.getStringList(s + ".items." + sMat).forEach(sMEC -> {
                    String[] split = sMEC.split(",");
                    Enchantment ench = Enchantment.getByName(split[0]);
                    int enchLevel = Integer.parseInt(split[1]);
                    int enchChance = Integer.parseInt(split[2]);
                    PQuests.error("added " + ench.getName() + " from level " + enchLevel + " with chance " + enchChance + " to " + sMat);
                    chances.add(new MaterialEnchantChance(ench, enchLevel, enchChance));
                });
                matChances.put(mat, chances);
            }
            PQuests.error("added enchants for level " + level + " with " + matChances.size() + " size");
            enchants.add(new PEnchant(level, config.getInt(s + ".cost"), compoundEnchants, matChances));
        }

        if(enchants.size()!=3){
            Bukkit.getConsoleSender().sendMessage(Lang.PRISONTECH.f("&cError: There are not three enchant sections!"));
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
