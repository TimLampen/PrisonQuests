package me.timlampen.prisonquests.prisonfishing;

import me.timlampen.prisonquests.Lang;
import me.timlampen.prisonquests.Module;
import me.timlampen.prisonquests.PQuests;
import me.timlampen.prisonquests.prisonfishing.listeners.Fishing;
import net.minecraft.server.v1_12_R1.EntityFishingHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Timothy Lampen on 6/20/2018.
 */
public class PFishing extends Module {
    private Set<PFishingRod> fishingRods = new HashSet<PFishingRod>();
    private static PFishing instance;
    private FileConfiguration config;
    private HashMap<UUID, Long> lastCast = new HashMap<>();

    public void onEnable() {
        instance = this;
        loadConfig();
        Bukkit.getPluginManager().registerEvents(new Fishing(), PQuests.getInstance());
        PQuests.getInstance().getCommand("pfishingrod").setExecutor(new PFishingRodCmd());

        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<UUID, Long> copy = new HashMap<>();
                copy.putAll(lastCast);

                for(UUID uuid : copy.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if(player==null)
                        lastCast.remove(uuid);

                    ItemStack is = player.getInventory().getItemInMainHand();
                        Optional<PFishingRod> rod = getPFishingRod(is);
                        if(rod.isPresent()) {
                            if(lastCast.containsKey(player.getUniqueId()) && System.currentTimeMillis()-lastCast.get(player.getUniqueId()) >= rod.get().getTime()*1000) {
                                lastCast.put(player.getUniqueId(), System.currentTimeMillis());
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        awardFishingReward(player, rod.get());
                                    }
                                }.runTaskLater(PQuests.getInstance(), 2);
                            }
                    }
                    else
                        lastCast.remove(uuid);
                }
            }
        }.runTaskTimer(PQuests.getInstance(), 0, 20*5);
    }

    public void onDisable() {

    }

    public static PFishing getInstance() {
        return instance;
    }

    public void awardFishingReward(Player player, PFishingRod rod) {
        HashMap<ItemStack, Integer> drops = rod.getChances();

        int ran = ThreadLocalRandom.current().nextInt(1, 101);
        int currentNum = 0;
        for(Map.Entry<ItemStack, Integer> entry : drops.entrySet()) {
            if(currentNum + entry.getValue() >= ran) {
                player.getWorld().dropItemNaturally(player.getLocation(), entry.getKey());
                break;
            }
            currentNum += entry.getValue();
        }
    }

    private void loadConfig(){
        File file = new File(PQuests.getInstance().getDataFolder(), "fishing.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PQuests.getInstance().saveResource("fishing.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }

        for(String s : config.getConfigurationSection("").getKeys(false)) {
            int id = Integer.parseInt(s);
            String name = ChatColor.translateAlternateColorCodes('&', config.getString(s + ".name"));

            List<String> lore = new ArrayList<>();
            config.getStringList(s + ".lore").forEach(sLore -> lore.add(ChatColor.translateAlternateColorCodes('&', sLore)));

            int time = config.getInt(s + ".time");
            HashMap<ItemStack, Integer> chances = new HashMap<>();
            int totalChance = 0;

            for(String sChance : config.getStringList(s + ".chances")){
                String[] split = sChance.split(",");
                ItemStack is;
                if(split[0].contains(":"))
                    is = new ItemStack(Material.getMaterial(split[0].split(":")[0]), Integer.parseInt(split[1]), Short.parseShort(split[0].split(":")[1]));
                else
                    is = new ItemStack(Material.getMaterial(split[0]), Integer.parseInt(split[1]));
                chances.put(is, Integer.parseInt(split[2]));
                totalChance += Integer.parseInt(split[2]);
            }

            if(totalChance!=100){
                Bukkit.getConsoleSender().sendMessage(Lang.PFISHING.f("&cUnable to load fishing rod: " + id + " as total chance % != 100"));
                continue;
            }

            PFishingRod rod = new PFishingRod(id, name, lore, time, chances);
            fishingRods.add(rod);
        }
    }

    public boolean isPFishingRod(ItemStack is ){
        if(is==null || is.getType()!=Material.FISHING_ROD || !is.hasItemMeta() || !is.getItemMeta().hasDisplayName())
            return false;
        return fishingRods.stream().anyMatch(rod -> rod.getName().equals(is.getItemMeta().getDisplayName()));
    }

    public HashMap<UUID, Long> getLastCast() {
        return lastCast;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Optional<PFishingRod> getPFishingRod(int id){
        return fishingRods.stream().filter(rod -> rod.getId()==id).findFirst();
    }

    public Optional<PFishingRod> getPFishingRod(ItemStack is) {
        if(!isPFishingRod(is))
            return Optional.empty();
        return fishingRods.stream().filter(rod -> {
            ItemStack rodItem = rod.generate();
            return is.getItemMeta().getDisplayName().equals(rodItem.getItemMeta().getDisplayName()) && is.getItemMeta().getLore().equals(rodItem.getItemMeta().getLore());
        }).findFirst();
    }

    public void setBiteTime(FishHook hook, int time) {
        net.minecraft.server.v1_12_R1.EntityFishingHook hookCopy = (EntityFishingHook) ((CraftEntity) hook).getHandle();

        Field fishCatchTime = null;

        try {
            fishCatchTime = net.minecraft.server.v1_12_R1.EntityFishingHook.class.getDeclaredField("aw");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        fishCatchTime.setAccessible(true);

        try {
            fishCatchTime.setInt(hookCopy, time);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        fishCatchTime.setAccessible(false);
    }
}
