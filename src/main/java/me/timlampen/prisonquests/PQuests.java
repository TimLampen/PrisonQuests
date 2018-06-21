package me.timlampen.prisonquests;

import me.timlampen.prisonquests.prisonfishing.PFishing;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Timothy Lampen on 6/20/2018.
 */
public class PQuests extends JavaPlugin {

    private static PQuests instance;
    private Set<Module> modules = new HashSet<>();

    public static PQuests getInstance() {
        return instance;
    }

    @Override
    public void onEnable(){
        instance = this;
        modules.add(new PFishing());

        modules.forEach(Module::onEnable);
    }

    public void onDisable(){
        modules.forEach(Module::onDisable);
    }
}
