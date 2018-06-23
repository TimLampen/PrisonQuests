package me.timlampen.pqcore.npcs.interfaces;

import net.citizensnpcs.api.event.NPCCollisionEvent;
import net.citizensnpcs.api.event.NPCPushEvent;

/**
 * Created by Timothy Lampen on 6/23/2018.
 */
public interface CollideableNPC {

    void onCollide(NPCCollisionEvent event);

    void onPush(NPCPushEvent event);
}
