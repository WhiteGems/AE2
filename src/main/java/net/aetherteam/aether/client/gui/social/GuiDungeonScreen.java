package net.aetherteam.aether.client.gui.social;

import java.util.ArrayList;
import java.util.Iterator;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDungeonScreen extends GuiScreen
{
    private static final ResourceLocation TEXTURE_LOADING_SCREEN_BG = new ResourceLocation("aether", "textures/gui/loadingScreenBackground.png");
    private static final ResourceLocation TEXTURE_LOADING_SCREEN = new ResourceLocation("aether", "textures/gui/loadingScreen.png");

    /** Reference to the Minecraft object. */
    private Minecraft mc;
    private GuiButton readyUpButton;
    private boolean isPlayerReady;
    private String readyString;
    private String notReadyString;
    private int difficulty;

    public GuiDungeonScreen(Minecraft mc)
    {
        this.mc = mc;
        this.isPlayerReady = false;
        this.notReadyString = "取消准备";
        this.readyString = "已准备";
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawBackground(0);
        this.mc.renderEngine.func_110577_a(TEXTURE_LOADING_SCREEN);
        this.drawTexturedModalRect(14, 14, 7, 7, 162, 152);
        this.drawParty();
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }

    public void drawParty()
    {
        this.mc.renderEngine.func_110577_a(TEXTURE_LOADING_SCREEN);
        Party party = PartyController.instance().getParty((EntityPlayer)this.mc.thePlayer);
        ArrayList members = party != null ? party.getMembers() : null;
        int count = 0;
        byte xcount = 0;

        if (members != null)
        {
            int i$;

            for (i$ = 0; i$ < members.size(); ++i$)
            {
                if (i$ == 4)
                {
                    xcount = 2;
                }
                else if (i$ > 4 && i$ < 8)
                {
                    xcount = 3;
                }
                else if (i$ > 8 && i$ < 12)
                {
                    xcount = 4;
                }
                else
                {
                    xcount = 1;
                }
            }

            for (i$ = 0; i$ < members.size(); ++i$)
            {
                this.drawTexturedModalRect(85 + xcount * 10, 21 + i$ * 16, 7, 160, 6, 6);
            }

            for (Iterator var7 = members.iterator(); var7.hasNext(); ++count)
            {
                PartyMember member = (PartyMember)var7.next();
                this.drawPlayerHead(85 + xcount * 10, 16 + count * 20, (AbstractClientPlayer)member.getPlayer());
                GL11.glPushMatrix();
                GL11.glTranslatef(0.5F, 0.5F, 0.0F);
                GL11.glScalef(0.5F, 0.5F, 0.0F);
                this.fontRenderer.drawString(member.username, 169, 30 + count * 32, 16777062);
                GL11.glPopMatrix();
            }
        }
    }

    public void drawPlayerHead(int x, int y, AbstractClientPlayer player)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(player.func_110306_p());
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPushMatrix();
        float u = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
        GL11.glScalef(0.8F, 0.8F, 1.0F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex2f((float)(x + 2), (float)(y + 2));
        GL11.glTexCoord2f(u, v1);
        GL11.glVertex2f((float)(x + 2), (float)(y + 18));
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f((float)(x + 18), (float)(y + 18));
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f((float)(x + 18), (float)(y + 2));
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    /**
     * Draws the background (i is always 0 as of 1.2.2)
     */
    public void drawBackground(int par1)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.mc.renderEngine.func_110577_a(TEXTURE_LOADING_SCREEN_BG);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(0.7F, 0.7F, 0.7F);
        tessellator.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / f + (float)par1));
        tessellator.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / f), (double)((float)this.height / f + (float)par1));
        tessellator.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / f), (double)par1);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)par1);
        tessellator.draw();
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
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        int id = par1GuiButton.id;

        switch (id)
        {
            case 0:
                this.isPlayerReady = !this.isPlayerReady;

            default:
        }
    }
}
