package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
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

    /** Reference to the Minecraft object. */
    Minecraft mc;

    public PartyView()
    {
        this(new PartyModel());
    }

    public PartyView(PartyModel var1)
    {
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = var1;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
        var1.pList_online.add(this.mc.thePlayer);
        var1.pList_online.add(this.mc.thePlayer);
        var1.pList_online.add(this.mc.thePlayer);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 52, 20, "Invite"));
        this.buttonList.add(new GuiButton(0, this.xParty - 1, this.yParty + 85 - 28, 60, 20, "Remove"));
        super.initGui();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        int var10000 = var1.id;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int var4 = this.xParty - 70;
        int var5 = this.yParty - 84;
        this.drawTexturedModalRect(var4, var5, 0, 0, 141, this.hParty);
        int var6 = 0;
        byte var7 = 111;
        byte var8 = 20;
        byte var9 = 2;
        int var10;
        EntityPlayer var11;

        for (var10 = 0; var10 < this.pm.pList_online.size(); ++var10)
        {
            var11 = (EntityPlayer)this.pm.pList_online.get(var10);
            this.drawPlayerSlot(var11, var4 + 15, var5 + var6 + 30, var7, var8);
            var6 += var8 + var9;
        }

        for (var10 = 0; var10 < this.pm.pList_offline.size(); ++var10)
        {
            var11 = (EntityPlayer)this.pm.pList_online.get(var10);
            this.drawPlayerSlot(var11, this.xParty, this.yParty + var6, var7, var8);
            var6 += var8 + var9;
        }

        this.fontRenderer.drawStringWithShadow("Kingbdogz\' party", var4 + 16, var5 + 11, 16777215);
        super.drawScreen(var1, var2, var3);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var2 = var1.getScaledWidth();
        int var3 = var1.getScaledHeight();
        this.xParty = var2 / 2;
        this.yParty = var3 / 2;
    }

    public void drawPlayerSlot(EntityPlayer var1, int var2, int var3, int var4, int var5)
    {
        this.drawGradientRect(var2, var3, var2 + var4, var3 + var5, -5592406, -11184811);
        int var6 = this.mc.renderEngine.getTextureForDownloadableImage(var1.skinUrl, "/mob/char.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var6);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float var7 = 0.125F;
        float var8 = 0.25F;
        float var9 = 0.25F;
        float var10 = 0.5F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(var7, var8);
        GL11.glVertex2f((float)(var2 + 2), (float)(var3 + 2));
        GL11.glTexCoord2f(var7, var10);
        GL11.glVertex2f((float)(var2 + 2), (float)(var3 + 18));
        GL11.glTexCoord2f(var9, var10);
        GL11.glVertex2f((float)(var2 + 18), (float)(var3 + 18));
        GL11.glTexCoord2f(var9, var8);
        GL11.glVertex2f((float)(var2 + 18), (float)(var3 + 2));
        GL11.glEnd();
        this.fontRenderer.drawStringWithShadow(var1.username, var2 + var5, var3 + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        this.fontRenderer.drawString("Online", (int)(((float)var2 + (float)var5) / 0.75F), (int)(((float)var3 + 12.0F) / 0.75F), 7859831);
        GL11.glPopMatrix();
    }
}
