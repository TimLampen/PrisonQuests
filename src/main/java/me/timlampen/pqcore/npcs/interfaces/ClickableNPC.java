package me.timlampen.pqcore.npcs.interfaces;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public interface ClickableNPC {

    void onRightClick(NPCRightClickEvent event);

    void onLeftClick(NPCLeftClickEvent event);

}
