package me.timlampen.pqcore.npcs.interfaces;

import net.citizensnpcs.api.event.NPCCombustByBlockEvent;
import net.citizensnpcs.api.event.NPCCombustByEntityEvent;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public interface CombustableNPC {

    void onCombustByBlock(NPCCombustByBlockEvent event);

    void onCombustByEntity(NPCCombustByEntityEvent event);
}
