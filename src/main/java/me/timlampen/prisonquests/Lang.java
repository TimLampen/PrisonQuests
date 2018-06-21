package me.timlampen.prisonquests;

import org.bukkit.ChatColor;

/**
 * Created by Timothy Lampen on 6/20/2018.
 */
public enum Lang {

    PFISHING("&2[&6PrisonFishing&2]&r "),
    NULL_PLAYER(PFISHING + "&cError: That player is not currently online."),
    SYNTAX_ERROR(PFISHING + "&cSyntax Error: Please review your command arguments as they were incorrect."),
    NULL_NUMBER(PFISHING + "&cNumber Error: the argument passed for an integer value was not a number.");

    private String disp;

    private final String s;

    Lang(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', this.s);
    }

    public String f(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
