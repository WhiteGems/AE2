package net.aetherteam.aether.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.client.gui.donator.GuiDonatorMenu;
import net.aetherteam.aether.client.gui.social.GuiMenu;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiInventoryAether extends InventoryEffectRenderer
{
    private static final ResourceLocation INVENTORY = new ResourceLocation("aether", "textures/gui/inventory.png");
    private float xSize_lo;
    private float ySize_lo;
    private static EntityPlayer player;

    public GuiInventoryAether(EntityPlayer par1EntityPlayer)
    {
        super(par1EntityPlayer.inventoryContainer);
        player = par1EntityPlayer;
        this.allowUserInput = true;
        par1EntityPlayer.addStat(AchievementList.openInventory, 1);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(INVENTORY);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize + 21);
        renderEntity(var5 + 34, var6 + 75, 30, (float)(var5 + 51) - this.xSize_lo, (float)(var6 + 75 - 50) - this.ySize_lo, this.mc.thePlayer);
        this.buttonList.clear();
        GuiButton bookOfLore = new GuiButton(2, this.guiLeft + 7, this.guiTop + 162, 72, 20, I18n.func_135053_a("Book of Lore"));
        bookOfLore.enabled = false;
        this.buttonList.add(bookOfLore);
        this.buttonList.add(new GuiButton(3, this.guiLeft + 84, this.guiTop + 162, 34, 20, I18n.func_135053_a("Social")));
        this.buttonList.add(new GuiButton(4, this.guiLeft + 123, this.guiTop + 162, 46, 20, I18n.func_135053_a("Donator")));
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        this.xSize_lo = (float)par1;
        this.ySize_lo = (float)par2;
    }

    public static void renderEntity(int par0, int par1, int par2, float par3, float par4, EntityLivingBase entityToRender)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par0, (float)par1, 50.0F);
        GL11.glScalef((float)(-par2), (float)par2, (float)par2);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = entityToRender.renderYawOffset;
        float f3 = entityToRender.rotationYaw;
        float f4 = entityToRender.rotationPitch;
        float f5 = entityToRender.prevRotationYawHead;
        float f6 = entityToRender.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((double)(par4 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        entityToRender.renderYawOffset = (float)Math.atan((double)(par3 / 40.0F)) * 20.0F;
        entityToRender.rotationYaw = (float)Math.atan((double)(par3 / 40.0F)) * 40.0F;
        entityToRender.rotationPitch = -((float)Math.atan((double)(par4 / 40.0F))) * 20.0F;
        entityToRender.rotationYawHead = entityToRender.rotationYaw;
        entityToRender.prevRotationYawHead = entityToRender.rotationYaw;
        GL11.glTranslatef(0.0F, entityToRender.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(entityToRender, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        entityToRender.renderYawOffset = f2;
        entityToRender.rotationYaw = f3;
        entityToRender.rotationPitch = f4;
        entityToRender.prevRotationYawHead = f5;
        entityToRender.rotationYawHead = f6;
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
            int guiID = AetherGuiHandler.realLoreID;
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
