package me.timlampen.pqcore.menu.listeners;

import me.timlampen.pqcore.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class MenuCloseEvent extends Event {
    private static HandlerList HANDLER_LIST = new HandlerList();

    Player player;
    Menu menu;
    InventoryCloseEvent close;
    public MenuCloseEvent (Player player, InventoryCloseEvent close, Menu menu) {
        this.player = player;
        this.menu = menu;
        this.close = close;
    }

    public InventoryCloseEvent getClose() {
        return close;
    }

    public Player getPlayer() {
        return player;
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
