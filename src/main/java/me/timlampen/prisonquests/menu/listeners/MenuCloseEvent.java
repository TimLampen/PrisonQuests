package me.timlampen.prisonquests.menu.listeners;

import me.timlampen.prisonquests.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class MenuCloseEvent extends Event {
    private static HandlerList HANDLER_LIST = new HandlerList();

    Player player;
    Menu menu;
    public MenuCloseEvent (Player player, Menu menu) {
        this.player = player;
        this.menu = menu;
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
