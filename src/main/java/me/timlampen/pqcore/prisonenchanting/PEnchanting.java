package me.timlampen.pqcore.prisonenchanting;

import me.timlampen.pqcore.Lang;
import me.timlampen.pqcore.Module;
import me.timlampen.pqcore.PQCore;
import me.timlampen.pqcore.menu.PMenus;
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
        this.config = PQCore.getConfig("enchanting.yml");
        loadConfig();
        PMenus.getInstance().addMenu("enchant_1","&5Pick an Enchant Level", 3);
        PMenus.getInstance().addMenu("enchant_2","&5The Enchantment Lottery", 3);
        PQCore.getInstance().getCommand("pqenchant").setExecutor(new PQEnchantCmd());
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
        File file = new File(PQCore.getInstance().getDataFolder(), "enchanting.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PQCore.getInstance().saveResource("enchanting.yml", false);
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
            for(String sMat : config.getConfigurationSection(s + ".items").getKeys(false)){
                Set<MaterialEnchantChance> chances = new HashSet<>();
                Material mat = Material.getMaterial(sMat);
                config.getStringList(s + ".items." + sMat).forEach(sMEC -> {
                    String[] split = sMEC.split(",");
                    Enchantment ench = Enchantment.getByName(split[0]);
                    int enchLevel = Integer.parseInt(split[1]);
                    int enchChance = Integer.parseInt(split[2]);
                    chances.add(new MaterialEnchantChance(ench, enchLevel, enchChance));
                });
                matChances.put(mat, chances);
            }
            PQCore.log("added enchants for level " + level + " with " + matChances.size() + " size");
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
