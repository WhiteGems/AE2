package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class PartyView extends GuiScreen
{
    private static final ResourceLocation TEXTURE_PARTY = new ResourceLocation("aether", "textures/gui/party.png");
    private final PartyModel pm;
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

    public PartyView(PartyModel pm)
    {
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
        pm.pList_online.add(this.mc.thePlayer);
        pm.pList_online.add(this.mc.thePlayer);
        pm.pList_online.add(this.mc.thePlayer);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 52, 20, "邀请"));
        this.buttonList.add(new GuiButton(0, this.xParty - 1, this.yParty + 85 - 28, 60, 20, "移除"));
        super.initGui();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton btn)
    {
        int var10000 = btn.id;
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
    public void drawScreen(int x, int y, float partialTick)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_PARTY);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        int totalHeight = 0;
        byte slotW = 111;
        byte slotH = 20;
        byte gutter = 2;
        int i;
        EntityPlayer p;

        for (i = 0; i < this.pm.pList_online.size(); ++i)
        {
            p = (EntityPlayer)this.pm.pList_online.get(i);
            this.drawPlayerSlot(p, centerX + 15, centerY + totalHeight + 30, slotW, slotH);
            totalHeight += slotH + gutter;
        }

        for (i = 0; i < this.pm.pList_offline.size(); ++i)
        {
            p = (EntityPlayer)this.pm.pList_online.get(i);
            this.drawPlayerSlot(p, this.xParty, this.yParty + totalHeight, slotW, slotH);
            totalHeight += slotH + gutter;
        }

        this.fontRenderer.drawStringWithShadow("Kingbdogz的公会", centerX + 16, centerY + 11, 16777215);
        super.drawScreen(x, y, partialTick);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = width / 2;
        this.yParty = height / 2;
    }

    public void drawPlayerSlot(EntityPlayer p, int x, int y, int width, int height)
    {
        this.drawGradientRect(x, y, x + width, y + height, -5592406, -11184811);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(((AbstractClientPlayer)p).func_110306_p());
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float u = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
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
        this.fontRenderer.drawStringWithShadow(p.username, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        this.fontRenderer.drawString("在线", (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 7859831);
        GL11.glPopMatrix();
    }
}
