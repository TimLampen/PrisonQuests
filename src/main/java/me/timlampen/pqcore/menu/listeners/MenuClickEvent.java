package me.timlampen.pqcore.menu.listeners;

import me.timlampen.pqcore.menu.Menu;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class MenuClickEvent extends Event {
    private static HandlerList HANDLER_LIST = new HandlerList();

    Menu menu;
    InventoryClickEvent event;
    public MenuClickEvent (InventoryClickEvent event, Menu menu) {
        this.event = event;
        this.menu = menu;
    }

    public InventoryClickEvent getEvent() {
        return event;
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
