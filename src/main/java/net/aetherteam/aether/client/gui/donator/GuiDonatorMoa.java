package net.aetherteam.aether.client.gui.donator;

import cpw.mods.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.List;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.client.gui.social.PartyData;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.DonatorChoices;
import net.aetherteam.aether.donator.DonatorTexture;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.aetherteam.aether.donator.choices.MoaChoice;
import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
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

public class GuiDonatorMoa extends GuiScreen
{
    private final PartyData pm;
    private int backgroundTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft mc;
    private EntityPlayer player;
    private GuiScreen parent;
    private float rotationY;
    public float dif = 0.0025F;
    EntityMoa moaEntity;
    Donator donator;
    static int moaIndex = 0;
    static int colourIndex = 0;
    MoaChoice choice;

    public GuiDonatorMoa(EntityPlayer player, GuiScreen parent)
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

    public GuiDonatorMoa(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.player = player;
        Aether.getInstance();
        this.donator = Aether.syncDonatorList.getDonator(player.username);
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/choiceMenu.png");
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
                if (this.donator != null)
                {
                    if (moaIndex >= Aether.donatorChoices.moaChoices.size())
                    {
                        Aether.syncDonatorList.sendTypeRemoveToAll(this.player.username, EnumChoiceType.MOA);
                        this.donator.removeChoiceType(EnumChoiceType.MOA);
                        moaIndex = 0;
                        return;
                    }
                    this.donator.addChoice((DonatorChoice) Aether.donatorChoices.moaChoices.get(moaIndex));
                    Aether.syncDonatorList.sendChoiceToAll(this.player.username, (DonatorChoice) Aether.donatorChoices.moaChoices.get(moaIndex), true);

                    moaIndex += 1;
                }
                break;
            case 2:
                if (this.donator != null)
                {
                    if (colourIndex >= AetherMoaColour.colours.size())
                    {
                        ((MoaChoice) this.donator.getChoiceFromType(EnumChoiceType.MOA)).setOverrideAll(true);
                        colourIndex = 0;
                        return;
                    }
                    ((MoaChoice) this.donator.getChoiceFromType(EnumChoiceType.MOA)).setOverrideAll(false);
                    ((MoaChoice) this.donator.getChoiceFromType(EnumChoiceType.MOA)).setOverridingColour((AetherMoaColour) AetherMoaColour.colours.get(colourIndex));

                    colourIndex += 1;
                }
                break;
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
        int centerY = this.yParty - 56;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 194, this.hParty);

        this.buttonList.add(new GuiButton(0, this.xParty + 10, this.yParty + 27, 80, 20, "返回"));

        GuiButton typeButton = null;
        GuiButton overrideButton = null;

        boolean hasChoice = this.donator.containsChoiceType(EnumChoiceType.MOA);

        if (this.donator != null)
        {
            this.choice = null;

            if (hasChoice)
            {
                this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/choiceMenu.png");
                this.choice = ((MoaChoice) this.donator.getChoiceFromType(EnumChoiceType.MOA));
            } else
            {
                this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/choiceMenu2.png");
            }

            this.moaEntity = new EntityMoa(Aether.proxy.getClientWorld(), true, false, false, AetherMoaColour.pickRandomMoa(), Aether.proxy.getClientPlayer(), hasChoice ? this.choice.textureFile.localURL : null);

            typeButton = new GuiButton(1, this.xParty + 10, this.yParty - 35, 80, 20, this.choice != null ? this.choice.name : "关");
            overrideButton = new GuiButton(2, this.xParty + 10, this.yParty, 80, 20, this.choice != null ? "无" : this.choice.getOverridingColour() != null ? this.choice.getOverridingColour().name + "恐鸟皮肤" : this.choice.getOverrideAll() ? "全部" : "无");
        }

        if (this.donator == null)
        {
            typeButton.enabled = false;
            overrideButton.enabled = false;
        }

        if ((this.donator != null) && (!this.donator.containsChoiceType(EnumChoiceType.MOA)))
        {
            overrideButton.enabled = false;
        }

        this.buttonList.add(typeButton);
        this.buttonList.add(overrideButton);

        super.drawScreen(x, y, partialTick);

        this.mc.renderEngine.resetBoundTexture();

        String header = "捐助特有恐鸟皮肤";
        //String override = "Override";

        drawString(this.fontRenderer, header, sr.getScaledWidth() / 2 - this.fontRenderer.getStringWidth(header) / 2 + 49, centerY + 10, 15658734);
        //drawString(this.fontRenderer, override, sr.getScaledWidth() / 2 - this.fontRenderer.getStringWidth(override) / 2 + 49, centerY + 45, 15658734);

        if (this.choice != null)
        {
            renderMoa(this.moaEntity, Minecraft.getMinecraft(), this.xParty - 40, this.yParty + 35, 30, 1.0F, 1.0F, (this.donator != null) && (this.donator.containsChoiceType(EnumChoiceType.MOA)));
        }
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
 * Qualified Name:     net.aetherteam.aether.client.gui.donator.GuiDonatorMoa
 * JD-Core Version:    0.6.2
 */