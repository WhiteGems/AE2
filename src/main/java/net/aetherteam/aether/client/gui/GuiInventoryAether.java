package net.aetherteam.aether.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.client.gui.donator.GuiDonatorMenu;
import net.aetherteam.aether.client.gui.social.GuiMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.StringTranslate;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiInventoryAether extends InventoryEffectRenderer
{
    private float xSize_lo;
    private float ySize_lo;
    private static EntityPlayer player;

    public GuiInventoryAether(EntityPlayer var1)
    {
        super(var1.inventoryContainer);
        player = var1;
        this.allowUserInput = true;
        var1.addStat(AchievementList.openInventory, 1);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/gui/inventory.png");
        int var4 = this.guiLeft;
        int var5 = this.guiTop;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize + 21);
        this.drawPlayerOnGui(this.mc, var4 + 34, var5 + 75, 30, (float)(var4 + 51) - this.xSize_lo, (float)(var5 + 75 - 50) - this.ySize_lo);
        StringTranslate var6 = StringTranslate.getInstance();
        this.buttonList.clear();
        GuiButton var7 = new GuiButton(2, this.guiLeft + 7, this.guiTop + 162, 72, 20, var6.translateKey("以太Ⅱ物品百科"));
        var7.enabled = false;
        this.buttonList.add(var7);
        this.buttonList.add(new GuiButton(3, this.guiLeft + 84, this.guiTop + 162, 34, 20, var6.translateKey("社区")));
        this.buttonList.add(new GuiButton(4, this.guiLeft + 123, this.guiTop + 162, 46, 20, var6.translateKey("捐赠特区")));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2) {}

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        super.drawScreen(var1, var2, var3);
        this.xSize_lo = (float)var1;
        this.ySize_lo = (float)var2;
    }

    public void drawPlayerOnGui(Minecraft var1, int var2, int var3, int var4, float var5, float var6)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2, (float)var3, 50.0F);
        GL11.glScalef((float)(-var4), (float)var4, (float)var4);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float var7 = var1.thePlayer.renderYawOffset;
        float var8 = var1.thePlayer.rotationYaw;
        float var9 = var1.thePlayer.rotationPitch;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((double)(var6 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        var1.thePlayer.renderYawOffset = (float)Math.atan((double)(var5 / 40.0F)) * 20.0F;
        var1.thePlayer.rotationYaw = (float)Math.atan((double)(var5 / 40.0F)) * 40.0F;
        var1.thePlayer.rotationPitch = -((float)Math.atan((double)(var6 / 40.0F))) * 20.0F;
        var1.thePlayer.rotationYawHead = var1.thePlayer.rotationYaw;
        GL11.glTranslatef(0.0F, var1.thePlayer.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(var1.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        var1.thePlayer.renderYawOffset = var7;
        var1.thePlayer.rotationYaw = var8;
        var1.thePlayer.rotationPitch = var9;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        super.initGui();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        if (var1.id == 0)
        {
            this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
        }

        if (var1.id == 1)
        {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
        }

        if (var1.id == 2)
        {
            int var2 = AetherGuiHandler.realLoreID;
            this.mc.displayGuiScreen(new GuiLore(this.mc.thePlayer.inventory, this.mc.thePlayer, 0));
        }

        if (var1.id == 3)
        {
            this.mc.displayGuiScreen(new GuiMenu(player, this));
        }

        if (var1.id == 4)
        {
            this.mc.displayGuiScreen(new GuiDonatorMenu(player, this));
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {}
}
