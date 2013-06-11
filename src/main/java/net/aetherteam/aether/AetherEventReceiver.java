package net.aetherteam.aether;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

public class AetherEventReceiver
{
    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void onRenderGui(RenderGameOverlayEvent event)
    {
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherEventReceiver
 * JD-Core Version:    0.6.2
 */