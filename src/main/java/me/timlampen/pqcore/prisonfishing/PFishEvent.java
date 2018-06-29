package me.timlampen.pqcore.prisonfishing;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Timothy Lampen on 6/27/2018.
 */
public class PFishEvent extends PlayerEvent {
    private final static HandlerList HANDLER_LIST = new HandlerList();
    private final PFishingRod rod;
    private final ItemStack award;

    public PFishEvent(Player who, PFishingRod rod, ItemStack award) {
        super(who);
        this.rod = rod;
        this.award = award;
    }

    public ItemStack getAward() {
        return award;
    }

    public PFishingRod getRod() {
        return rod;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
