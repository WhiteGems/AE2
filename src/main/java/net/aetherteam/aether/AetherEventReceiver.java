package net.aetherteam.aether;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

public class AetherEventReceiver
{
    @ForgeSubscribe(
        priority = EventPriority.NORMAL
    )
    public void onRenderGui(RenderGameOverlayEvent event) {}
}
