package net.aetherteam.aether.client.gui.social;

import java.util.ArrayList;
import java.util.Iterator;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

public class GuiDungeonScreen extends GuiScreen
{
    private int background;

    /** Reference to the Minecraft object. */
    private Minecraft mc;
    private GuiButton readyUpButton;
    private boolean isPlayerReady;
    private String readyString;
    private String notReadyString;
    private int difficulty;

    public GuiDungeonScreen(Minecraft var1)
    {
        this.mc = var1;
        this.isPlayerReady = false;
        this.notReadyString = "取消准备";
        this.readyString = "已准备";
        this.background = mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/loadingScreen.png");
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        this.drawBackground(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.background);
        this.drawTexturedModalRect(14, 14, 7, 7, 162, 152);
        this.drawParty();
        this.mc.renderEngine.resetBoundTexture();
        super.drawScreen(var1, var2, var3);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        super.keyTyped(var1, var2);

        if (var2 == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }

    public void drawParty()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.background);
        Party var1 = PartyController.instance().getParty((EntityPlayer)this.mc.thePlayer);
        ArrayList var2 = var1 != null ? var1.getMembers() : null;
        int var3 = 0;
        byte var4 = 0;

        if (var2 != null)
        {
            int var5;

            for (var5 = 0; var5 < var2.size(); ++var5)
            {
                if (var5 == 4)
                {
                    var4 = 2;
                }
                else if (var5 > 4 && var5 < 8)
                {
                    var4 = 3;
                }
                else if (var5 > 8 && var5 < 12)
                {
                    var4 = 4;
                }
                else
                {
                    var4 = 1;
                }
            }

            for (var5 = 0; var5 < var2.size(); ++var5)
            {
                this.drawTexturedModalRect(85 + var4 * 10, 21 + var5 * 16, 7, 160, 6, 6);
            }

            for (Iterator var7 = var2.iterator(); var7.hasNext(); ++var3)
            {
                PartyMember var6 = (PartyMember)var7.next();
                this.drawPlayerHead(85 + var4 * 10, 16 + var3 * 20, var6.username);
                this.mc.renderEngine.resetBoundTexture();
                GL11.glPushMatrix();
                GL11.glTranslatef(0.5F, 0.5F, 0.0F);
                GL11.glScalef(0.5F, 0.5F, 0.0F);
                this.fontRenderer.drawString(var6.username, 169, 30 + var3 * 32, 16777062);
                GL11.glPopMatrix();
            }
        }
    }

    public void drawPlayerHead(int var1, int var2, String var3)
    {
        int var4 = this.mc.renderEngine.getTextureForDownloadableImage("http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(var3) + ".png", "/mob/char.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPushMatrix();
        float var5 = 0.125F;
        float var6 = 0.25F;
        float var7 = 0.25F;
        float var8 = 0.5F;
        GL11.glScalef(0.8F, 0.8F, 1.0F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(var5, var6);
        GL11.glVertex2f((float)(var1 + 2), (float)(var2 + 2));
        GL11.glTexCoord2f(var5, var8);
        GL11.glVertex2f((float)(var1 + 2), (float)(var2 + 18));
        GL11.glTexCoord2f(var7, var8);
        GL11.glVertex2f((float)(var1 + 18), (float)(var2 + 18));
        GL11.glTexCoord2f(var7, var6);
        GL11.glVertex2f((float)(var1 + 18), (float)(var2 + 2));
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    /**
     * Draws the background (i is always 0 as of 1.2.2)
     */
    public void drawBackground(int var1)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator var2 = Tessellator.instance;
        this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/gui/loadingScreenBackground.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var3 = 32.0F;
        var2.startDrawingQuads();
        var2.setColorOpaque_F(0.7F, 0.7F, 0.7F);
        var2.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / var3 + (float)var1));
        var2.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / var3), (double)((float)this.height / var3 + (float)var1));
        var2.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / var3), (double)var1);
        var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)var1);
        var2.draw();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        this.buttonList.clear();
        this.readyUpButton = new GuiButton(0, 14, 170, 200, 20, this.isPlayerReady ? this.notReadyString : this.readyString);
        this.buttonList.add(this.readyUpButton);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        int var2 = var1.id;

        switch (var2)
        {
            case 0:
                this.isPlayerReady = !this.isPlayerReady;

            default:
        }
    }
}