package me.timlampen.pqcore.prisonquests;

import me.timlampen.pqcore.Lang;
import me.timlampen.pqcore.Module;
import me.timlampen.pqcore.PQCore;
import me.timlampen.pqcore.prisonfishing.PFishingRod;
import me.timlampen.pqcore.prisonquests.listeners.*;
import me.timlampen.pqcore.prisonquests.types.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public class PQuests extends Module {
    private static PQuests instance;
    private Set<Quest> masterQuests = new HashSet<>();//Objects in this set SHOULD NOT BE EDITED
    private Set<PQUser> users  =new HashSet<>();
    private FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;
        this.config = PQCore.getConfig("quests.yml");
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDeath(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityInteract(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new Fishing(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new Join(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new Quit(), PQCore.getInstance());

        loadConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public void onDisable() {

    }

    private void loadConfig(){
        for(String s : config.getConfigurationSection("").getKeys(false)) {
            int id = Integer.parseInt(s);
            String permission = config.getString(s + ".permission");
            Set<String> completionCommands = new HashSet<>(config.getStringList(s + ".completion-commands"));
            QuestType type = QuestType.valueOf(config.getString(s + ".quest-type"));
            PQuestType mainType = null;
            String region = null;
            String details = config.getString(s + ".details");
            String npcGiverName = config.getString(s +".npc-giver-name");
            switch (type) {
                case MINING:
                    HashMap<ItemStack, Integer> blocks = new HashMap<>();
                    region = config.getString(s + ".reigon");
                    config.getStringList(s + ".data").stream().map(PQCore::deserializeItem).forEach(is -> blocks.put(new ItemStack(is.getType(), 1, is.getDurability()), is.getAmount()));
                    mainType = new PMining(blocks);
                    break;
                case KILLING:
                    region = config.getString(s + ".reigon");
                    HashMap<EntityType, Integer> entities = new HashMap<>();
                    config.getStringList(s + ".data").stream().forEach(str -> {
                        String[] split = s.split(",");
                        entities.put(EntityType.valueOf(split[0]), Integer.valueOf(split[1]));});
                    mainType = new PKilling(entities);
                    break;
                case CRAFTING:
                    HashMap<ItemStack, Integer> items = new HashMap<>();
                    config.getStringList(s + ".data").stream().map(PQCore::deserializeItem).forEach(is -> items.put(new ItemStack(is.getType(), 1, is.getDurability()), is.getAmount()));
                    mainType = new PCrafting(items);
                    break;
                case FISHING:
                    HashMap<ItemStack, Integer> drops = new HashMap<>();
                    config.getStringList(s + ".data").stream().map(PQCore::deserializeItem).forEach(is -> drops.put(new ItemStack(is.getType(), 1, is.getDurability()), is.getAmount()));
                    mainType = new PFishing(drops);
                    break;
                case TALKING:
                    Set<String> names = new HashSet<>(config.getStringList(s + ".data"));
                    mainType = new PTalking(names);
                    break;
                case ENCHANTING:
                    HashMap<Enchantment, Integer> enchants = new HashMap<>();
                    config.getStringList(s + ".data").stream().forEach(str -> {
                        String[] split = s.split(",");
                        enchants.put(Enchantment.getByName(split[0]), Integer.parseInt(split[1]));
                    });
                    mainType = new PEnchanting(enchants);
                    break;
            }
            if(region==null)
                this.masterQuests.add(new Quest(id, mainType, permission, completionCommands, npcGiverName, details));
            else
                this.masterQuests.add(new Quest(id, mainType, permission, completionCommands, npcGiverName, details, region));
            Bukkit.getConsoleSender().sendMessage(Lang.PRISONTECH.f("&6Loaded quest with ID: &b" + id));
        }
    }

    public Set<Quest> getMasterQuests() {
        return masterQuests;
    }

    public static PQuests getInstance() {
        return instance;
    }

    public void loadPlayer(Player player){
        if(this.users.stream().anyMatch(user -> user.getPlayer().equals(player)))
            return;
        this.users.add(new PQUser(player));
    }

    public void unloadPlayer(Player player){
        this.users.removeIf(pqUser -> pqUser.getPlayer().equals(player));
    }

    public Optional<PQUser> getPQUser(UUID uuid) {
        return this.users.stream().filter(user -> user.getPlayer().getUniqueId().equals(uuid)).findFirst();
    }
}
