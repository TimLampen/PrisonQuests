package me.timlampen.prisonquests;

import org.bukkit.ChatColor;

/**
 * Created by Timothy Lampen on 6/20/2018.
 */
public enum Lang {

    PRISONTECH("&2[&6PrisonTech&2]&r "),
    NULL_PLAYER(PRISONTECH.toString() + "&cError: That player is not currently online."),
    SYNTAX_ERROR(PRISONTECH.toString() + "&cSyntax Error: Please review your command arguments as they were incorrect."),
    NULL_NUMBER(PRISONTECH.toString() + "&cNumber Error: the argument passed for an integer value was not a number.");

    private final String s;

    Lang(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', this.s);
    }

    public String f(String s){
        return ChatColor.translateAlternateColorCodes('&', this.s + s);
    }

}
