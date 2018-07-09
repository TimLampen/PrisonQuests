package me.timlampen.pqcore.menu.listeners;

import me.timlampen.pqcore.Lang;
import me.timlampen.pqcore.PQCore;
import me.timlampen.pqcore.menu.Menu;
import me.timlampen.pqcore.menu.PMenus;
import me.timlampen.pqcore.prisoncrafting.PCrafting;
import me.timlampen.pqcore.prisoncrafting.PCraftingItem;
import me.timlampen.pqcore.prisonenchanting.MaterialEnchantChance;
import me.timlampen.pqcore.prisonenchanting.PEnchant;
import me.timlampen.pqcore.prisonenchanting.PEnchanting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class MenuClick implements Listener {

    @EventHandler
    public void onClick(MenuClickEvent event) {
        InventoryClickEvent click = event.getEvent();
        Player player = (Player) click.getWhoClicked();
        Menu m = event.getMenu();
        click.setCancelled(true);

        switch (m.getName()) {
            case "enchant_1": {
                ItemStack is = click.getCurrentItem();
                if(is==null)
                    return;
                if(is.getType()!= Material.STAINED_GLASS)
                    return;
                int slot = click.getSlot();
                int index = -1;

                switch (slot) {
                    case 11:
                        index = 0;
                        break;
                    case 13:
                        index = 1;
                        break;
                    case 15:
                        index = 2;
                        break;
                }


                PEnchanting.getInstance().getSelectedEnchants().put(player.getUniqueId(), PEnchanting.getInstance().getEnchants().get(index));
                player.closeInventory();
                PMenus.getInstance().showMenu(player, "enchant_2");
                break;
            }
            case "enchant_2": {
                PEnchant pEnchant = PEnchanting.getInstance().getSelectedEnchants().get(player.getUniqueId());
                if(click.getClick()!= ClickType.LEFT){
                    player.sendMessage(Lang.PRISONTECH.f("&cError: You can only left click items into this inventory."));
                    return;
                }
                switch (click.getSlot()) {
                    case 10: {
                        ItemStack newSelected = click.getCursor()==null || click.getCursor().getType()==Material.AIR ? null : click.getCursor();
                        if(newSelected==null || !pEnchant.getMatChances().containsKey(newSelected.getType())) {
                            PMenus.getInstance().showMenu(player, "enchant_1");//reset the inventory
                            return;
                        }

                        click.setCancelled(false);
                        IntStream.of(1,9,11,19).forEach(i -> click.getInventory().setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7)));

                        ItemStack reroll = new ItemStack(Material.WATCH);
                        ItemMeta im = reroll.getItemMeta();
                        im.setDisplayName(ChatColor.GOLD + "Click to Reroll Enchantments (" + ChatColor.BLUE + "$" + pEnchant.getRerollCost() + ChatColor.GOLD + ")");
                        reroll.setItemMeta(im);
                        click.getInventory().setItem(12, reroll);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.updateInventory();
                            }
                        }.runTaskLater(PQCore.getInstance(), 2);
                        break;
                    }
                    case 12: {
                        ItemStack is = click.getCurrentItem();
                        if(is==null || is.getType()!=Material.WATCH)
                            return;
                        ItemStack beingEnchanted = click.getInventory().getItem(10);
                        if(beingEnchanted==null || !pEnchant.getMatChances().containsKey(beingEnchanted.getType())) {
                            PMenus.getInstance().showMenu(player, "enchant_1");//reset the inventory
                            return;
                        }
                        if(!PQCore.getEco().has(player, pEnchant.getRerollCost())){
                            player.sendMessage(Lang.PRISONTECH.f("&cError: You do not have enough money to reroll!"));
                            return;
                        }
                        PQCore.getEco().withdrawPlayer(player, pEnchant.getRerollCost());

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for(int i= 14; i<17; i++){
                                    int ran = ThreadLocalRandom.current().nextInt(1, 101);
                                    int currentNum = 0;
                                    int numberOfEnchants = -1;
                                    for(Map.Entry<Integer, Integer> entry : pEnchant.getCompoundChances().entrySet()) {
                                        PQCore.error("looping through compound with " + entry.getKey());
                                        if(currentNum + entry.getValue() >= ran) {
                                            numberOfEnchants = entry.getKey();
                                            break;
                                        }
                                        currentNum += entry.getValue();
                                    }


                                    List<MaterialEnchantChance> mECs = new ArrayList<>(pEnchant.getMatChances().get(beingEnchanted.getType()));
                                    Collections.shuffle(mECs);
                                    Set<MaterialEnchantChance> pendingEnchants = new HashSet<>();
                                    int currentEnchantments = 0;
                                    while (currentEnchantments < numberOfEnchants) {
                                        PQCore.error(currentEnchantments + " -> current enchants mECS size= " + mECs.size());
                                        for(MaterialEnchantChance mEC : mECs) {
                                            ran = ThreadLocalRandom.current().nextInt(1, 100);
                                            PQCore.error(mEC.getChance() + " " + ran);
                                            if(mEC.getChance() >= ran && !pendingEnchants.stream().anyMatch(me -> me.getEnchantment().equals(mEC.getEnchantment()))) {
                                                PQCore.error("added enchant");
                                                currentEnchantments++;
                                                pendingEnchants.add(mEC);
                                            }
                                        }
                                    }

                                    ItemStack finalList = new ItemStack(Material.ENCHANTED_BOOK);
                                    ItemMeta im = finalList.getItemMeta();
                                    im.setDisplayName(ChatColor.GOLD + "Enchantments:");
                                    im.setLore(pendingEnchants.stream().map(mEC -> ChatColor.GREEN + mEC.getEnchantment().getName() + " " + mEC.getLevel()).collect(Collectors.toList()));
                                    finalList.setItemMeta(im);


                                    final int finalI = i;
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            click.getInventory().setItem(finalI, finalList);
                                            player.updateInventory();
                                        }
                                    }.runTaskLater(PQCore.getInstance(), 2); }
                            }
                        }.runTaskAsynchronously(PQCore.getInstance());

                        break;
                    }
                    case 14:
                    case 15:
                    case 16:
                        ItemStack is = click.getCurrentItem();
                        if(is.getType()!=Material.ENCHANTED_BOOK) {
                            return;
                        }

                        ItemStack beingEnchanted = click.getInventory().getItem(10);
                        if(beingEnchanted==null || !pEnchant.getMatChances().containsKey(beingEnchanted.getType())) {
                            PMenus.getInstance().showMenu(player, "enchant_1");//reset the inventory
                            return;
                        }

                        is.getItemMeta().getLore().stream().forEach(s -> {
                            String[] split = ChatColor.stripColor(s).split(" ");
                            PQCore.error("|" + split[0] + "|" + split[1]);
                            Enchantment ench = Enchantment.getByName(split[0]);
                            int level = Integer.parseInt(split[1]);
                            beingEnchanted.getEnchantments().keySet().forEach(beingEnchanted::removeEnchantment);
                            beingEnchanted.addUnsafeEnchantment(ench, level);
                        });
                        player.closeInventory();
                        break;
                }
                break;

            }
            case "crafting_items": {
                ItemStack is = click.getCurrentItem();
                if(is == null)
                    return;

                Optional<PCraftingItem> optItem = PCrafting.getInstance().getItems().stream().filter(item -> item.getDisplay().equals(is)).findFirst();
                if(!optItem.isPresent())
                    return;

                HashMap<ItemStack, Integer> copyMats = new HashMap<>();
                optItem.get().getMaterials().forEach(mat -> copyMats.put(mat, mat.getAmount()));

                boolean playerHasItems = true;

                for(ItemStack ingredient :  optItem.get().getMaterials()) {
                    if(!player.getInventory().containsAtLeast(ingredient, ingredient.getAmount())) {
                        playerHasItems = false;
                        break;
                    }
                }

                if(playerHasItems) {
                    player.getInventory().removeItem(optItem.get().getMaterials().toArray(new ItemStack[]{}));
                    player.getInventory().addItem(optItem.get().getProduct());
                    player.updateInventory();
                    optItem.get().getCommands().forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName())));
                }
                break;
            }
        }
    }
}
