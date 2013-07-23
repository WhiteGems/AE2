package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class PartyView extends GuiScreen
{
    private final PartyModel pm;
    private int backgroundTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft g;

    public PartyView()
    {
        this(new PartyModel());
    }

    public PartyView(PartyModel pm)
    {
        this.g = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
        pm.pList_online.add(this.g.thePlayer);
        pm.pList_online.add(this.g.thePlayer);
        pm.pList_online.add(this.g.thePlayer);
    }

    public void initGui()
    {
        this.k.clear();
        this.k.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 52, 20, "Invite"));
        this.k.add(new GuiButton(0, this.xParty - 1, this.yParty + 85 - 28, 60, 20, "Remove"));
        super.initGui();
    }

    protected void actionPerformed(GuiButton btn)
    {
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        int totalHeight = 0;
        int slotW = 111;
        int slotH = 20;
        int gutter = 2;

        for (int i = 0; i < this.pm.pList_online.size(); i++)
        {
            EntityPlayer p = (EntityPlayer)this.pm.pList_online.get(i);
            drawPlayerSlot(p, centerX + 15, centerY + totalHeight + 30, slotW, slotH);
            totalHeight += slotH + gutter;
        }

        for (int i = 0; i < this.pm.pList_offline.size(); i++)
        {
            EntityPlayer p = (EntityPlayer)this.pm.pList_online.get(i);
            drawPlayerSlot(p, this.xParty, this.yParty + totalHeight, slotW, slotH);
            totalHeight += slotH + gutter;
        }

        this.m.drawStringWithShadow("Kingbdogz' party", centerX + 16, centerY + 11, 16777215);
        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }

    public void drawPlayerSlot(EntityPlayer p, int x, int y, int width, int height)
    {
        drawGradientRect(x, y, x + width, y + height, -5592406, -11184811);
        int icon = this.g.renderEngine.a(p.skinUrl, "/mob/char.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, icon);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float u = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex2f(x + 2, y + 2);
        GL11.glTexCoord2f(u, v1);
        GL11.glVertex2f(x + 2, y + 18);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x + 18, y + 18);
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f(x + 18, y + 2);
        GL11.glEnd();
        this.m.drawStringWithShadow(p.username, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        this.m.drawString("Online", (int)((x + height) / 0.75F), (int)((y + 12.0F) / 0.75F), 7859831);
        GL11.glPopMatrix();
    }
}

