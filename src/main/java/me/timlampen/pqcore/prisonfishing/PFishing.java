package me.timlampen.pqcore.prisonfishing;

import me.timlampen.pqcore.Lang;
import me.timlampen.pqcore.Module;
import me.timlampen.pqcore.PQCore;
import me.timlampen.pqcore.prisonfishing.listeners.Fishing;
import me.timlampen.pqcore.prisonfishing.listeners.Interact;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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

    @Override
    public void onEnable() {
        instance = this;
        this.config = PQCore.getConfig("fishing.yml");
        loadConfig();
        Bukkit.getPluginManager().registerEvents(new Fishing(), PQCore.getInstance());
        Bukkit.getPluginManager().registerEvents(new Interact(), PQCore.getInstance());
        PQCore.getInstance().getCommand("pfishingrod").setExecutor(new PFishingRodCmd());

        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<UUID, Long>> iter = lastCast.entrySet().iterator();

                while (iter.hasNext()) {
                    Map.Entry<UUID, Long> entry = iter.next();
                    Player player = Bukkit.getPlayer(entry.getKey());
                    if(player==null)
                        iter.remove();
                    ItemStack is = player.getItemInHand();
                    Optional<PFishingRod> rod = getPFishingRod(is);
                    if(rod.isPresent()) {
                        if(lastCast.containsKey(player.getUniqueId()) && System.currentTimeMillis()-lastCast.get(player.getUniqueId()) >= rod.get().getTime()*1000) {
                            lastCast.put(player.getUniqueId(), System.currentTimeMillis());
                            awardFishingReward(player, rod.get());
                        }
                    }
                    else
                        iter.remove();
                }
            }
        }.runTaskTimer(PQCore.getInstance(), 0, 20*5);
    }

    @Override
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
                Bukkit.getPluginManager().callEvent(new PFishEvent(player, rod, entry.getKey()));
                break;
            }
            currentNum += entry.getValue();
        }

    }

    private void loadConfig(){
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
                ItemStack is = PQCore.deserializeItem(sChance);
                chances.put(is, Integer.parseInt(split[2]));
                totalChance += Integer.parseInt(split[2]);
            }

            if(totalChance!=100){
                Bukkit.getConsoleSender().sendMessage(Lang.PRISONTECH.f("&cUnable to load fishing rod: " + id + " as total chance % == " + totalChance));
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
}
