package net.aetherteam.aether.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.client.gui.donator.GuiDonatorMenu;
import net.aetherteam.aether.client.gui.social.GuiMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.StringTranslate;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiInventoryAether extends AetherInventoryEffectRenderer
{
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

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/gui/inventory.png");

        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize + 21);

        func_74223_a(this.mc, var5 + 34, var6 + 75, 30, var5 + 51 - this.xSize_lo, var6 + 75 - 50 - this.ySize_lo);

        StringTranslate string = StringTranslate.getInstance();
        this.buttonList.clear();

        GuiButton bookOfLore = new GuiButton(2, this.guiLeft + 7, this.guiTop + 162, 72, 20, string.translateKey("Book of Lore"));

        bookOfLore.enabled = false;

        this.buttonList.add(bookOfLore);
        this.buttonList.add(new GuiButton(3, this.guiLeft + 84, this.guiTop + 162, 34, 20, string.translateKey("Social")));

        this.buttonList.add(new GuiButton(4, this.guiLeft + 123, this.guiTop + 162, 46, 20, string.translateKey("Donator")));
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        this.xSize_lo = par1;
        this.ySize_lo = par2;
    }

    public void func_74223_a(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5)
    {
        GL11.glEnable(2903);
        GL11.glPushMatrix();
        GL11.glTranslatef(par1, par2, 50.0F);
        GL11.glScalef(-par3, par3, par3);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float var6 = par0Minecraft.thePlayer.renderYawOffset;
        float var7 = par0Minecraft.thePlayer.rotationYaw;
        float var8 = par0Minecraft.thePlayer.rotationPitch;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float) Math.atan(par5 / 40.0F) * 20.0F, 1.0F, 0.0F, 0.0F);

        par0Minecraft.thePlayer.renderYawOffset = ((float) Math.atan(par4 / 40.0F) * 20.0F);

        par0Minecraft.thePlayer.rotationYaw = ((float) Math.atan(par4 / 40.0F) * 40.0F);
        par0Minecraft.thePlayer.rotationPitch = (-(float) Math.atan(par5 / 40.0F) * 20.0F);

        par0Minecraft.thePlayer.rotationYawHead = par0Minecraft.thePlayer.rotationYaw;
        GL11.glTranslatef(0.0F, par0Minecraft.thePlayer.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(par0Minecraft.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);

        par0Minecraft.thePlayer.renderYawOffset = var6;
        par0Minecraft.thePlayer.rotationYaw = var7;
        par0Minecraft.thePlayer.rotationPitch = var8;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void initGui()
    {
        this.buttonList.clear();
        super.initGui();
    }

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

        if (var1.id == 4) this.mc.displayGuiScreen(new GuiDonatorMenu(player, this));
    }

    public void updateScreen()
    {
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.GuiInventoryAether
 * JD-Core Version:    0.6.2
 */