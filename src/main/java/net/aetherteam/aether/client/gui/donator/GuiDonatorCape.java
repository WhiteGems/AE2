package net.aetherteam.aether.client.gui.donator;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.client.gui.social.PartyData;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.DonatorChoices;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.CallableMPL2;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiDonatorCape extends GuiScreen
{
    private final PartyData pm;
    private int backgroundTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft g;
    private EntityPlayer player;
    private GuiScreen parent;
    private float rotationY;
    public float dif = 0.0025F;
    Donator donator;
    static int capeIndex = 0;

    public GuiDonatorCape(EntityPlayer player, GuiScreen parent)
    {
        this(new PartyData(), player, parent);
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.g.displayGuiScreen((GuiScreen)null);
            this.g.setIngameFocus();
        }
    }

    public GuiDonatorCape(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.player = player;
        Aether.getInstance();
        this.donator = Aether.syncDonatorList.getDonator(player.username);
        this.g = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/choiceMenu.png");
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
                this.g.displayGuiScreen(this.parent);
                break;

            case 1:
                if (this.donator != null)
                {
                    if (capeIndex >= Aether.donatorChoices.capeChoices.size())
                    {
                        Aether.syncDonatorList.sendTypeRemoveToAll(this.player.username, EnumChoiceType.CAPE);
                        this.donator.removeChoiceType(EnumChoiceType.CAPE);
                        capeIndex = 0;
                        return;
                    }

                    this.donator.addChoice((DonatorChoice)Aether.donatorChoices.capeChoices.get(capeIndex));
                    Aether.syncDonatorList.sendChoiceToAll(this.player.username, (DonatorChoice)Aether.donatorChoices.capeChoices.get(capeIndex), true);
                    capeIndex += 1;
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
        this.k.clear();
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int centerX = this.xParty - 97;
        int centerY = this.yParty - 56;
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 194, this.hParty);
        this.k.add(new GuiButton(0, this.xParty + 10, this.yParty + 27, 80, 20, "Back"));
        GuiButton typeButton = null;
        GuiButton overrideButton = null;
        boolean hasChoice = this.donator.containsChoiceType(EnumChoiceType.CAPE);

        if (this.donator != null)
        {
            DonatorChoice choice = null;

            if (hasChoice)
            {
                this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/choiceMenu.png");
                choice = this.donator.getChoiceFromType(EnumChoiceType.CAPE);
            }
            else
            {
                this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/choiceMenu2.png");
            }

            typeButton = new GuiButton(1, this.xParty + 10, this.yParty - 35, 80, 20, choice != null ? choice.name : "Off");
        }

        if (this.donator == null)
        {
            typeButton.enabled = false;
        }

        this.k.add(typeButton);
        super.drawScreen(x, y, partialTick);
        this.g.renderEngine.a();
        String header = "Donator Cape";
        drawString(this.m, header, sr.getScaledWidth() / 2 - this.m.getStringWidth(header) / 2 + 49, centerY + 10, 15658734);
        drawPlayerOnGui(this.g, this.xParty - 40, this.yParty + 35, 30, 1.0F, 1.0F, (this.donator != null) && (this.donator.containsChoiceType(EnumChoiceType.CAPE)));
    }

    public void drawPlayerOnGui(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5, boolean lighting)
    {
        if (this.rotationY > 2.5D)
        {
            this.dif = -0.0025F;
        }
        else if (this.rotationY < -2.5D)
        {
            this.dif = 0.0025F;
        }

        this.rotationY += this.dif;
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(par1, par2, 50.0F);
        GL11.glScalef(-par3, par3, 50.5F);
        GL11.glRotatef(180.0F, this.rotationY, 0.0F, 1.0F);
        float f2 = par0Minecraft.thePlayer.ay;
        float f3 = par0Minecraft.thePlayer.A;
        float f4 = par0Minecraft.thePlayer.B;
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

        if (!lighting)
        {
            GL11.glColor4f(0.35F, 0.35F, 0.35F, 1.0F);
        }

        RenderEnderman.endermanModel.doRender(par0Minecraft.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        par0Minecraft.thePlayer.ay = f2;
        par0Minecraft.thePlayer.A = f3;
        par0Minecraft.thePlayer.B = f4;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void renderMoa(Entity entity, Minecraft mc, int x, int y, int scale, float par4, float par5, boolean lighting)
    {
        if (this.rotationY > 2.5D)
        {
            this.dif = -0.0025F;
        }
        else if (this.rotationY < -2.5D)
        {
            this.dif = 0.0025F;
        }

        this.rotationY += this.dif;
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 100.0F);
        GL11.glScalef(-scale, scale, scale);
        GL11.glRotatef(180.0F, this.rotationY, 0.0F, 1.0F);
        RenderHelper.enableStandardItemLighting();

        if ((entity instanceof EntityLiving))
        {
            ((EntityLiving)entity).rotationYawHead = 0.0F;
        }

        RenderEnderman.endermanModel.j = 180.0F;

        if (!lighting);

        RenderEnderman.endermanModel.doRender(entity, 0.0D, 0.0D, 0.0D, 0.0F, 660.0F);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }
}

