package me.timlampen.prisonquests.prisoncrafting;

import me.timlampen.prisonquests.Module;
import me.timlampen.prisonquests.PQuests;
import me.timlampen.prisonquests.menu.PMenus;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Timothy Lampen on 6/21/2018.
 */
public class PCrafting extends Module {

    private static PCrafting instance;
    private FileConfiguration config;
    private Set<PCraftingItem> items = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;
        this.config = PQuests.getConfig("crafting.yml");
        loadConfig();
        PQuests.getInstance().getCommand("pcrafting").setExecutor(new PCraftingCmd());

        PMenus.getInstance().addMenu("crafting_items", "&6Custom Crafting Recipes", -1);

    }

    @Override
    public void onDisable() {

    }

    public Set<PCraftingItem> getItems() {
        return items;
    }

    public static PCrafting getInstance() {
        return instance;
    }

    public void loadConfig(){
        File file = new File(PQuests.getInstance().getDataFolder(), "crafting.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PQuests.getInstance().saveResource("crafting.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }

        for(String s : config.getConfigurationSection("").getKeys(false)) {

            /**
             * Loading the display item and materials.
             */
            int id = Integer.parseInt(s);
            String dname = ChatColor.translateAlternateColorCodes('&', config.getString(s + ".dname"));
            List<String> dlore = new ArrayList<>();
            config.getStringList(s + ".dlore").forEach(sLore -> dlore.add(ChatColor.translateAlternateColorCodes('&', sLore)));
            ItemStack dItem = PQuests.deserializeItem(config.getString(s + ".ditem"));
            ItemMeta im = dItem.getItemMeta();
            im.setDisplayName(dname);
            im.setLore(dlore);
            dItem.setItemMeta(im);

            Set<ItemStack> materials = config.getStringList(s + ".materials").stream().map(PQuests::deserializeItem).collect(Collectors.toSet());

            /*
            * Now loading the product
            * */

            String name = ChatColor.translateAlternateColorCodes('&', config.getString(s + ".product.name"));
            List<String> lore = new ArrayList<>();
            config.getStringList(s + ".product.lore").forEach(sLore -> dlore.add(ChatColor.translateAlternateColorCodes('&', sLore)));
            ItemStack product = PQuests.deserializeItem(config.getString(s + ".product.item"));
            im = product.getItemMeta();
            im.setDisplayName(name);
            im.setLore(lore);
            product.setItemMeta(im);

            /**
             * Loading commands.
             */


            items.add(new PCraftingItem(id, product, dItem, materials, config.getStringList(s + ".product.commands")));

        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
