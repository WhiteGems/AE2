package net.aetherteam.aether.client.gui;

import java.util.Random;

import net.aetherteam.aether.notifications.client.ClientNotificationHandler;
import net.aetherteam.aether.overlays.AetherOverlays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

public class GuiAetherIngame extends Gui
{
    private Minecraft mc;
    private Random rand = new Random();

    public GuiAetherIngame(Minecraft mc)
    {
        this.mc = mc;
    }

    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void onRenderGui(RenderGameOverlayEvent event)
    {
        if ((event.isCancelable()) || (event.type != RenderGameOverlayEvent.ElementType.TEXT))
        {
            return;
        }
        ClientNotificationHandler.updateNotifications();
        AetherOverlays.renderDungeonQueue(this.mc);
        AetherOverlays.renderDungeonTimer(this.mc);
        AetherOverlays.renderMountHealth(this.mc);
        AetherOverlays.renderBossHP(this.mc);
        AetherOverlays.renderCooldown(this.mc);
        AetherOverlays.renderHearts(this.mc, this.rand);
        AetherOverlays.renderJumps(this.mc);
        AetherOverlays.renderIronBubbles(this.mc, this.rand);
        AetherOverlays.renderCoinbar(this.mc);
        AetherOverlays.renderPartyHUD(this.mc);
        this.mc.renderEngine.resetBoundTexture();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.GuiAetherIngame
 * JD-Core Version:    0.6.2
 */