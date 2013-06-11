package net.aetherteam.aether.client.gui.social;

import java.util.ArrayList;
import java.util.List;

import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

public class GuiDungeonScreen extends GuiScreen
{
    private int background;
    private Minecraft f;
    private GuiButton readyUpButton;
    private boolean isPlayerReady;
    private String readyString;
    private String notReadyString;
    private int difficulty;

    public GuiDungeonScreen(Minecraft mc)
    {
        this.mc = mc;
        this.isPlayerReady = false;
        this.notReadyString = "CANCEL READY";
        this.readyString = "READY UP";
        this.background = mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/loadingScreen.png");
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        drawBackground(0);
        GL11.glBindTexture(3553, this.background);
        drawTexturedModalRect(14, 14, 7, 7, 162, 152);
        drawParty();
        this.mc.renderEngine.resetBoundTexture();
        super.drawScreen(par1, par2, par3);
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        }
    }

    public void drawParty()
    {
        GL11.glBindTexture(3553, this.background);
        Party party = PartyController.instance().getParty(this.mc.thePlayer);
        ArrayList<PartyMember> members = party != null ? party.getMembers() : null;
        int count = 0;
        int xcount = 0;
        if (members != null)
        {
            for (int i1 = 0; i1 < members.size(); i1++)
            {
                if (i1 == 4) xcount = 2;
                else if ((i1 > 4) && (i1 < 8)) xcount = 3;
                else if ((i1 > 8) && (i1 < 12)) xcount = 4;
                else xcount = 1;
            }
            for (int i = 0; i < members.size(); i++)
            {
                drawTexturedModalRect(85 + xcount * 10, 21 + i * 16, 7, 160, 6, 6);
            }
            for (PartyMember member : members)
            {
                drawPlayerHead(85 + xcount * 10, 16 + count * 20, member.username);
                this.mc.renderEngine.resetBoundTexture();
                GL11.glPushMatrix();
                GL11.glTranslatef(0.5F, 0.5F, 0.0F);
                GL11.glScalef(0.5F, 0.5F, 0.0F);
                this.fontRenderer.drawString(member.username, 169, 30 + count * 32, 16777062);
                GL11.glPopMatrix();
                count++;
            }
        }
    }

    public void drawPlayerHead(int x, int y, String playername)
    {
        int icon = this.mc.renderEngine.getTextureForDownloadableImage("http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(playername) + ".png", "/mob/char.png");

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, icon);
        GL11.glEnable(3553);

        GL11.glPushMatrix();
        float u = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
        GL11.glScalef(0.8F, 0.8F, 1.0F);
        GL11.glBegin(7);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex2f(x + 2, y + 2);
        GL11.glTexCoord2f(u, v1);
        GL11.glVertex2f(x + 2, y + 18);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x + 18, y + 18);
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f(x + 18, y + 2);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public void drawBackground(int par1)
    {
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.instance;
        this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/gui/loadingScreenBackground.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(0.7F, 0.7F, 0.7F);
        tessellator.addVertexWithUV(0.0D, this.height, 0.0D, 0.0D, this.height / f + par1);
        tessellator.addVertexWithUV(this.width, this.height, 0.0D, this.width / f, this.height / f + par1);
        tessellator.addVertexWithUV(this.width, 0.0D, 0.0D, this.width / f, par1);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, par1);
        tessellator.draw();
    }

    public void updateScreen()
    {
        super.updateScreen();
        this.buttonList.clear();
        this.readyUpButton = new GuiButton(0, 14, 170, 200, 20, this.isPlayerReady ? this.notReadyString : this.readyString);
        this.buttonList.add(this.readyUpButton);
    }

    protected void actionPerformed(GuiButton par1GuiButton)
    {
        int id = par1GuiButton.id;
        switch (id)
        {
            case 0:
                this.isPlayerReady = (!this.isPlayerReady);
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiDungeonScreen
 * JD-Core Version:    0.6.2
 */