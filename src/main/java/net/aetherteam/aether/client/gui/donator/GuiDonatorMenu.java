package net.aetherteam.aether.client.gui.donator;

import cpw.mods.fml.client.FMLClientHandler;

import java.util.List;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.client.gui.social.PartyData;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.DonatorTexture;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiDonatorMenu extends GuiScreen
{
    private final PartyData pm;
    private int backgroundTexture;
    private int easterTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft f;
    private GuiTextField partyNameField;
    private EntityPlayer player;
    private GuiScreen parent;
    private float rotationY;
    public float dif = 0.0025F;
    Donator donator;
    EntityMoa moaEntity = new EntityMoa(Aether.proxy.getClientWorld(), true, false, false, AetherMoaColour.pickRandomMoa(), Aether.proxy.getClientPlayer(), (this.donator != null) && (this.donator.containsChoiceType(EnumChoiceType.MOA)) ? this.donator.getChoiceFromType(EnumChoiceType.MOA).textureFile.localURL : null);

    public GuiDonatorMenu(EntityPlayer player, GuiScreen parent)
    {
        this(new PartyData(), player, parent);
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

    public GuiDonatorMenu(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.player = player;
        Aether.getInstance();
        this.donator = Aether.syncDonatorList.getDonator(player.username);
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/donatorMenu.png");
        this.easterTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/donatorMenu.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
    }

    public void initGui()
    {
        updateScreen();
    }

    protected void actionPerformed(GuiButton btn)
    {
        switch (btn.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiDonatorMoa(this.player, this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiDonatorCape(this.player, this));
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        this.buttonList.clear();

        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.backgroundTexture);
        int centerX = this.xParty - 97;
        int centerY = this.yParty - 84;

        this.partyNameField = new GuiTextField(this.fontRenderer, this.xParty - 80, this.yParty - 64, 160, 50);
        this.partyNameField.setFocused(false);
        this.partyNameField.setMaxStringLength(5000);

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 194, this.hParty);

        this.buttonList.add(new GuiButton(0, this.xParty - 80, this.yParty - 4, 160, 20, "返回"));

        this.buttonList.add(new GuiButton(1, this.xParty - 30, this.yParty + 27, 60, 20, this.donator != null ? "恐鸟皮肤" : "恐鸟皮肤: 关"));
        this.buttonList.add(new GuiButton(2, this.xParty - 30, this.yParty + 52, 60, 20, this.donator != null ? "披风" : "披风: 关"));

        String[] text = new String[4];

        if (!Aether.syncDonatorList.isDonator(this.player.username))
        {
            this.moaEntity = new EntityMoa(Aether.proxy.getClientWorld(), true, false, false, AetherMoaColour.pickRandomMoa(), Aether.proxy.getClientPlayer(), (this.donator != null) && (this.donator.containsChoiceType(EnumChoiceType.MOA)) ? this.donator.getChoiceFromType(EnumChoiceType.MOA).textureFile.localURL : null);

            ((GuiButton) this.buttonList.get(1)).enabled = false;
            ((GuiButton) this.buttonList.get(2)).enabled = false;

            text[0] = "你并非权限者.";
            text[1] = "该菜单用于激活";
            text[2] = "捐助者的特殊功能, ";
            text[3] = "这些功能不会影响实际游戏.";
        } else
        {
            ((GuiButton) this.buttonList.get(1)).enabled = true;
            ((GuiButton) this.buttonList.get(2)).enabled = true;

            text[0] = ("亲爱的" + this.player.username + ",");
            text[1] = "我们无比感谢您的";
            text[2] = "慷慨捐助, 你将拥有";
            text[3] = "独特的披风和恐鸟皮肤.";
        }

        super.drawScreen(x, y, partialTick);

        this.partyNameField.drawTextBox();

        this.mc.renderEngine.resetBoundTexture();

        drawString(this.fontRenderer, text[0], sr.getScaledWidth() / 2 - this.fontRenderer.getStringWidth(text[0]) / 2, centerY + 24, 15658734);
        drawString(this.fontRenderer, text[1], sr.getScaledWidth() / 2 - this.fontRenderer.getStringWidth(text[1]) / 2, centerY + 37, 15658734);
        drawString(this.fontRenderer, text[2], sr.getScaledWidth() / 2 - this.fontRenderer.getStringWidth(text[2]) / 2, centerY + 47, 15658734);
        drawString(this.fontRenderer, text[3], sr.getScaledWidth() / 2 - this.fontRenderer.getStringWidth(text[3]) / 2, centerY + 57, 15658734);

        renderMoa(this.moaEntity, Minecraft.getMinecraft(), this.xParty + 62, this.yParty + 68, 16, 1.0F, 1.0F, (this.donator != null) && (this.donator.containsChoiceType(EnumChoiceType.MOA)));
        drawPlayerOnGui(this.mc, this.xParty - 60, this.yParty + 69, 30, 0.0F, 0.0F, (this.donator != null) && (this.donator.containsChoiceType(EnumChoiceType.CAPE)));
    }

    public void drawPlayerOnGui(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5, boolean lighting)
    {
        GL11.glEnable(2903);
        GL11.glPushMatrix();
        GL11.glTranslatef(par1, par2, 50.0F);
        GL11.glScalef(20.5F, 20.5F, 50.5F);
        GL11.glRotatef(180.0F, this.rotationY, 0.0F, 1.0F);
        float f2 = par0Minecraft.thePlayer.renderYawOffset;
        float f3 = par0Minecraft.thePlayer.rotationYaw;
        float f4 = par0Minecraft.thePlayer.rotationPitch;
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

        if (!lighting)
        {
            GL11.glColor4f(0.35F, 0.35F, 0.35F, 1.0F);
        }

        RenderManager.instance.renderEntityWithPosYaw(par0Minecraft.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);

        par0Minecraft.thePlayer.renderYawOffset = f2;
        par0Minecraft.thePlayer.rotationYaw = f3;
        par0Minecraft.thePlayer.rotationPitch = f4;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void renderMoa(Entity entity, Minecraft mc, int x, int y, int scale, float par4, float par5, boolean lighting)
    {
        if (this.rotationY > 2.5D)
        {
            this.dif = -0.0025F;
        } else if (this.rotationY < -2.5D)
        {
            this.dif = 0.0025F;
        }

        this.rotationY += this.dif;

        GL11.glEnable(2903);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 100.0F);
        GL11.glScalef(-scale, scale, scale);

        GL11.glRotatef(180.0F, this.rotationY, 0.0F, 1.0F);

        RenderHelper.enableStandardItemLighting();

        if ((entity instanceof EntityLiving))
        {
            ((EntityLiving) entity).rotationYawHead = 0.0F;
        }
        RenderManager.instance.playerViewY = 180.0F;

        if (!lighting) ;
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 660.0F);

        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.donator.GuiDonatorMenu
 * JD-Core Version:    0.6.2
 */
