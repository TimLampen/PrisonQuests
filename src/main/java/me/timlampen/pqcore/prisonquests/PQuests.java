package me.timlampen.pqcore.prisonquests;

import me.timlampen.pqcore.Lang;
import me.timlampen.pqcore.Module;
import me.timlampen.pqcore.PQCore;
import me.timlampen.pqcore.prisonquests.commands.QuestCmd;
import me.timlampen.pqcore.prisonquests.listeners.*;
import me.timlampen.pqcore.prisonquests.types.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public class PQuests extends Module {
    private static PQuests instance;
    private Set<Quest> masterQuests = new HashSet<>();//Objects in this set SHOULD NOT BE EDITED
    private Set<PQUser> users  =new HashSet<>();
    private FileConfiguration config, playerData;

    @Override
    public void onEnable() {
        instance = this;
        this.config = PQCore.getConfig("quests.yml");
        this.playerData = PQCore.getConfig("questdata.yml");
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDeath(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityInteract(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new Fishing(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new Join(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new Quit(), PQCore.getInstance());
        PQCore.getInstance().getCommand("qeta").setExecutor(new QuestCmd());
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
                    region = config.getString(s + ".region");
                    PQCore.log(region + " / " + config.getStringList(s + ""));
                    config.getStringList(s + ".data").stream().map(PQCore::deserializeItem).forEach(is -> blocks.put(new ItemStack(is.getType(), 1, is.getDurability()), is.getAmount()));
                    mainType = new PMining(blocks);
                    break;
                case KILLING:
                    region = config.getString(s + ".region");
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
                    config.getStringList(s + ".data").forEach(str -> {
                        String[] split = str.split(",");
                        enchants.put(Enchantment.getByName(split[0]), Integer.parseInt(split[1]));
                    });
                    mainType = new PEnchanting(enchants);
                    break;
            }
            if(region==null)
                this.masterQuests.add(new Quest(id, mainType, permission, completionCommands, npcGiverName, details));
            else
                this.masterQuests.add(new Quest(id, mainType, permission, completionCommands, npcGiverName, details, region));
            PQCore.log(Lang.PRISONTECH.f("Loaded quest with ID: &6" + id));
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
        PQUser user = new PQUser(player);
        this.users.add(user);
        if(!this.playerData.contains(player.getUniqueId().toString()))
            return;
        user.getCompletedQuests().addAll(this.playerData.getIntegerList(player.getUniqueId() + ".completed-quests"));
        for(String strID : this.playerData.getConfigurationSection(player.getUniqueId() + ".quests").getKeys(false)) {
            int id = Integer.parseInt(strID);
            QuestType type = QuestType.valueOf(this.playerData.getString(player.getUniqueId() + ".quests." + id + ".type"));

            Optional<Quest> optQuest = this.masterQuests.stream().filter(q -> q.getId()==id).findFirst();
            if(!optQuest.isPresent())
                continue;
            user.addNewQuest(optQuest.get());

            config.getStringList(player.getUniqueId() + ".quests." + id + ".data").forEach(str -> {
                switch (type) {
                    case MINING: {
                        PMining t = (PMining) optQuest.get().getType();
                        ItemStack is = PQCore.deserializeItem(str);
                        final int amt = is.getAmount();
                        is.setAmount(1);

                        t.getBlocks().put(is, amt);
                        break;
                    }
                    case KILLING: {
                        PKilling t = (PKilling)optQuest.get().getType();
                        String[] split = str.split(",");
                        t.getMobs().put(EntityType.valueOf(split[0]), Integer.parseInt(split[1]));
                        break;
                    }
                    case CRAFTING: {
                        PCrafting t = (PCrafting) optQuest.get().getType();
                        ItemStack is = PQCore.deserializeItem(str);
                        final int amt = is.getAmount();
                        is.setAmount(1);

                        t.getItems().put(is, amt);
                        break;
                    }
                    case FISHING: {
                        PFishing t = (PFishing) optQuest.get().getType();
                        ItemStack is = PQCore.deserializeItem(str);
                        final int amt = is.getAmount();
                        is.setAmount(1);

                        t.getDrops().put(is, amt);
                        break;
                    }
                    case TALKING: {
                        PTalking t = (PTalking) optQuest.get().getType();
                        t.getNpcNames().add(str);
                        break;
                    }
                    case ENCHANTING: {
                        PEnchanting t = (PEnchanting) optQuest.get().getType();
                        String[] split = str.split(",");
                        t.getEnchants().put(Enchantment.getByName(split[0]), Integer.parseInt(split[1]));
                        break;
                    }
                }
            });
        }
    }

    public void unloadPlayer(Player player){
        Optional<PQUser> optUser = this.users.stream().filter(u -> u.getPlayer().equals(player)).findFirst();
        if(!optUser.isPresent())
            return;
        String path  = optUser.get().getPlayer().getUniqueId().toString();
        this.playerData.set(path + ".completed-quests", optUser.get().getCompletedQuests());

        for(Quest q : optUser.get().getActiveQuests()) {
            this.playerData.set(path + ".quests." + q.getId() + ".type", q.getType().getType().toString());
            switch (q.getType().getType()) {

                case MINING:
                    this.playerData.set(path + ".quests." + q.getId() + ".data", ((PMining)q.getType()).getBlocks().entrySet().stream().map(entry -> PQCore.serializeItem(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
                    break;
                case KILLING:
                    this.playerData.set(path + ".quests." + q.getId() + ".data", ((PKilling)q.getType()).getMobs().entrySet().stream().map(entry -> entry.getKey().toString() + "," + entry.getValue()).collect(Collectors.toList()));
                    break;
                case CRAFTING:
                    this.playerData.set(path + ".quests." + q.getId() + ".data", ((PCrafting)q.getType()).getItems().entrySet().stream().map(entry -> PQCore.serializeItem(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
                    break;
                case FISHING:
                    this.playerData.set(path + ".quests." + q.getId() + ".data", ((PFishing)q.getType()).getDrops().entrySet().stream().map(entry -> PQCore.serializeItem(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
                    break;
                case TALKING:
                    this.playerData.set(path + ".quests." + q.getId() + ".data", ((PTalking)q.getType()).getNpcNames());
                    break;
                case ENCHANTING:
                    this.playerData.set(path + ".quests." + q.getId() + ".data", ((PEnchanting)q.getType()).getEnchants().entrySet().stream().map(entry -> entry.getKey().getName() + "," + entry.getValue()).collect(Collectors.toList()));
                    break;
            }
        }
        PQCore.getInstance().saveResource("questdata.yml", false);
        this.users.remove(optUser.get());
    }

    public Optional<PQUser> getPQUser(UUID uuid) {
        return this.users.stream().filter(user -> user.getPlayer().getUniqueId().equals(uuid)).findFirst();
    }
}
