package me.timlampen.prisonquests.menu;

import me.timlampen.prisonquests.Module;
import me.timlampen.prisonquests.PQuests;
import me.timlampen.prisonquests.menu.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class PMenus extends Module {

    HashMap<String, Menu> menus = new HashMap<>();
    private static PMenus instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), PQuests.getInstance());
        Bukkit.getPluginManager().registerEvents(new MenuClick(), PQuests.getInstance());
        Bukkit.getPluginManager().registerEvents(new MenuOpen(), PQuests.getInstance());
        Bukkit.getPluginManager().registerEvents(new MenuClose(), PQuests.getInstance());
        Bukkit.getPluginManager().registerEvents(new InventoryClose(), PQuests.getInstance());
    }

    @Override
    public void onDisable() {

    }

    public static PMenus getInstance() {
        return instance;
    }

    public void addMenu(String name, String title, int rows) {
        this.menus.put(name, new Menu(name, title, rows));
    }

    public void showMenu(Player player, String name){
        if(menus.containsKey(name)) {
            Menu m = menus.get(name);
            Bukkit.getPluginManager().callEvent(new MenuOpenEvent(player, m));
        }
    }

    public HashMap<String, Menu> getMenus() {
        return menus;
    }
}
