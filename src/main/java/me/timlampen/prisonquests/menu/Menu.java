package me.timlampen.prisonquests.menu;

/**
 * Created by Timothy Lampen on 6/22/2018.
 */
public class Menu {

    private final String name, title;
    private final int rows;

    public Menu(String name, String title, int rows) {
        this.name = name;
        this.title = title;
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public String getTitle() {
        return title;
    }
}
