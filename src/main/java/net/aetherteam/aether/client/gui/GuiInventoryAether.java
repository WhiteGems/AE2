package net.aetherteam.aether.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.client.gui.donator.GuiDonatorMenu;
import net.aetherteam.aether.client.gui.social.GuiMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.CallableMPL2;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEnderman;
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

    public GuiInventoryAether(EntityPlayer par1EntityPlayer)
    {
        super(par1EntityPlayer.inventoryContainer);
        player = par1EntityPlayer;
        this.l = true;
        par1EntityPlayer.addStat(AchievementList.openInventory, 1);
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.g.renderEngine.b("/net/aetherteam/aether/client/sprites/gui/inventory.png");
        int var5 = this.e;
        int var6 = this.guiTop;
        drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize + 21);
        func_74223_a(this.g, var5 + 34, var6 + 75, 30, var5 + 51 - this.xSize_lo, var6 + 75 - 50 - this.ySize_lo);
        StringTranslate string = StringTranslate.getInstance();
        this.k.clear();
        GuiButton bookOfLore = new GuiButton(2, this.e + 7, this.guiTop + 162, 72, 20, string.translateKey("Book of Lore"));
        bookOfLore.enabled = false;
        this.k.add(bookOfLore);
        this.k.add(new GuiButton(3, this.e + 84, this.guiTop + 162, 34, 20, string.translateKey("Social")));
        this.k.add(new GuiButton(4, this.e + 123, this.guiTop + 162, 46, 20, string.translateKey("Donator")));
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
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(par1, par2, 50.0F);
        GL11.glScalef(-par3, par3, par3);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float var6 = par0Minecraft.thePlayer.ay;
        float var7 = par0Minecraft.thePlayer.A;
        float var8 = par0Minecraft.thePlayer.B;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(par5 / 40.0F) * 20.0F, 1.0F, 0.0F, 0.0F);
        par0Minecraft.thePlayer.ay = ((float)Math.atan(par4 / 40.0F) * 20.0F);
        par0Minecraft.thePlayer.A = ((float)Math.atan(par4 / 40.0F) * 40.0F);
        par0Minecraft.thePlayer.B = (-(float)Math.atan(par5 / 40.0F) * 20.0F);
        par0Minecraft.thePlayer.aA = par0Minecraft.thePlayer.A;
        GL11.glTranslatef(0.0F, par0Minecraft.thePlayer.N, 0.0F);
        RenderEnderman.endermanModel.j = 180.0F;
        RenderEnderman.endermanModel.doRender(par0Minecraft.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        par0Minecraft.thePlayer.ay = var6;
        par0Minecraft.thePlayer.A = var7;
        par0Minecraft.thePlayer.B = var8;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void initGui()
    {
        this.k.clear();
        super.initGui();
    }

    protected void actionPerformed(GuiButton var1)
    {
        if (var1.id == 0)
        {
            this.g.displayGuiScreen(new GuiAchievements(this.g.statFileWriter));
        }

        if (var1.id == 1)
        {
            this.g.displayGuiScreen(new GuiStats(this, this.g.statFileWriter));
        }

        if (var1.id == 2)
        {
            int guiID = AetherGuiHandler.realLoreID;
            this.g.displayGuiScreen(new GuiLore(this.g.thePlayer.bK, this.g.thePlayer, 0));
        }

        if (var1.id == 3)
        {
            this.g.displayGuiScreen(new GuiMenu(player, this));
        }

        if (var1.id == 4)
        {
            this.g.displayGuiScreen(new GuiDonatorMenu(player, this));
        }
    }

    public void updateScreen()
    {
    }
}

