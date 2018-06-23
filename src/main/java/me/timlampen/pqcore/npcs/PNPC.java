package me.timlampen.pqcore.npcs;

import me.timlampen.pqcore.Module;
import me.timlampen.pqcore.PQCore;
import me.timlampen.pqcore.npcs.interfaces.ClickableNPC;
import me.timlampen.pqcore.npcs.interfaces.CollideableNPC;
import me.timlampen.pqcore.npcs.interfaces.CombustableNPC;
import me.timlampen.pqcore.npcs.interfaces.DamageableNPC;
import net.citizensnpcs.api.event.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class PNPC extends Module implements Listener {
    private final Set<QuestNPC> npcs = new HashSet<>();
    private static PNPC instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, PQCore.getInstance());
    }

    @Override
    public void onDisable() {
        this.npcs.forEach(npc -> {
            npc.delete();
        });
    }

    public static PNPC getInstance() {
        return instance;
    }

    /**
     * @param e the entity of the npc that you wish to delete.
     */
    public void deleteNPC(Entity e) {
        Set<QuestNPC> copy = new HashSet<>(this.npcs);
        copy.stream().filter(npc -> npc.getNPC().isSpawned() && npc.getNPC().getEntity().equals(e)).forEach(npc -> {
            npc.delete();
            this.npcs.remove(npc);
        });
    }

    /**
     * @param npc the npc that is being added.
     */
    public void registerQuestNPC(QuestNPC npc) {
        this.npcs.add(npc);
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (this.npcs.stream().anyMatch(npc -> npc.getNPC().isSpawned() && npc.getStartingLoc().getChunk().equals(event.getChunk()))) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPush(NPCPushEvent event) {
        this.npcs.stream().filter(npc -> npc instanceof CollideableNPC).filter(npc -> npc.getID() == event.getNPC().getId()).forEach(npc -> ((CollideableNPC) npc).onPush(event));
    }

    @EventHandler
    public void onCollide(NPCCollisionEvent event) {
        this.npcs.stream().filter(npc -> npc instanceof CollideableNPC).filter(npc -> npc.getID() == event.getNPC().getId()).forEach(npc -> ((CollideableNPC) npc).onCollide(event));
    }

    @EventHandler
    public void onDeath(NPCDeathEvent event) {
        this.npcs.stream().filter(npc -> npc instanceof DamageableNPC).filter(npc -> npc.getID() == event.getNPC().getId()).forEach(npc -> ((DamageableNPC) npc).onDeath(event));
    }

    @EventHandler
    public void onDamageBlock(NPCDamageByBlockEvent event) {
        this.npcs.stream().filter(npc -> npc instanceof DamageableNPC).filter(npc -> npc.getID() == event.getNPC().getId()).forEach(npc -> ((DamageableNPC) npc).onDamageByBlock(event));
    }

    @EventHandler
    public void onDamageEntity(NPCDamageByEntityEvent event) {
        this.npcs.stream().filter(npc -> npc instanceof DamageableNPC).filter(npc -> npc.getID() == event.getNPC().getId()).forEach(npc -> ((DamageableNPC) npc).onDamageByEntity(event));
    }

    @EventHandler
    public void onCombustBlock(NPCCombustByBlockEvent event) {
        this.npcs.stream().filter(npc -> npc instanceof CombustableNPC).filter(npc -> npc.getID() == event.getNPC().getId()).forEach(npc -> ((CombustableNPC) npc).onCombustByBlock(event));
    }

    @EventHandler
    public void onCombustEntity(NPCCombustByEntityEvent event) {
        this.npcs.stream().filter(npc -> npc instanceof CombustableNPC).filter(npc -> npc.getID() == event.getNPC().getId()).forEach(npc -> ((CombustableNPC) npc).onCombustByEntity(event));
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        this.npcs.stream().filter(npc -> npc instanceof ClickableNPC).filter(npc -> npc.getID() == event.getNPC().getId()).forEach(npc -> ((ClickableNPC) npc).onRightClick(event));
    }

    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        this.npcs.stream().filter(npc -> npc instanceof ClickableNPC).filter(npc -> npc.getID() == event.getNPC().getId()).forEach(npc -> ((ClickableNPC) npc).onLeftClick(event));
    }
}