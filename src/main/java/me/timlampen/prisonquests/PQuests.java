package me.timlampen.prisonquests;

import me.timlampen.prisonquests.menu.PMenus;
import me.timlampen.prisonquests.prisoncrafting.PCrafting;
import me.timlampen.prisonquests.prisonenchanting.PEnchanting;
import me.timlampen.prisonquests.prisonfishing.PFishing;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Lampen on 6/20/2018.
 */
public class PQuests extends JavaPlugin {

    private static PQuests instance;
    private List<Module> modules = new ArrayList<>();
    private static Economy eco = null;


    public static PQuests getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        modules.add(new PMenus());
        modules.add(new PFishing());
        modules.add(new PCrafting());
        modules.add(new PEnchanting());

        modules.forEach(Module::onEnable);

        setupEconomy();
    }

    public void onDisable() {
        modules.forEach(Module::onDisable);
    }

    public static FileConfiguration getConfig(String name) {
        FileConfiguration config;
        File file = new File(PQuests.getInstance().getDataFolder(), name);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PQuests.getInstance().saveResource(name, false);
        }

        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Lang.PRISONTECH.f("Unable to load config file " + name));
            return null;
        }

        return config;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }

    public static void error(String msg){
        Bukkit.getConsoleSender().sendMessage(Lang.PRISONTECH.f("&c" + msg));
    }

    public static Economy getEco() {
        return eco;
    }

    /*
    * Item must follow convention of name: STONE(:2),1 which would return 1x stone.
    * */
    public static ItemStack deserializeItem(String s){
        String[] split = s.split(",");
        ItemStack is;
        if(split[0].contains(":"))
            is = new ItemStack(Material.getMaterial(split[0].split(":")[0]), Integer.parseInt(split[1]), Short.parseShort(split[0].split(":")[1]));
        else
            is = new ItemStack(Material.getMaterial(split[0]), Integer.parseInt(split[1]));

        return is;
    }
    
    public static String serializeItem(ItemStack is) {
        if (is==null || is.getType()==Material.AIR)
            return null;
        if(is.getDurability()!=0)
            return is.getType().toString() + ":" + is.getDurability() + "," + is.getAmount();
        else
            return is.getType().toString() + "," + is.getAmount();
    }
}
