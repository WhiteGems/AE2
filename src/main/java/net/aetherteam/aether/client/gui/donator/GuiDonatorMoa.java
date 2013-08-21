package net.aetherteam.aether.client.gui.donator;

import cpw.mods.fml.client.FMLClientHandler;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.client.gui.social.PartyData;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.choices.MoaChoice;
import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiDonatorMoa extends GuiScreen
{
    private static final ResourceLocation TEXTURE_CHOICE_MENU = new ResourceLocation("aether", "textures/gui/choiceMenu.png");
    private static final ResourceLocation TEXTURE_CHOICE_MENU_2 = new ResourceLocation("aether", "textures/gui/choiceMenu2.png");
    private ResourceLocation backgroundTexture;
    private final PartyData pm;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private EntityPlayer player;
    private GuiScreen parent;
    private float rotationY;
    public float dif;
    EntityMoa moaEntity;
    Donator donator;
    static int moaIndex = 0;
    static int colourIndex = 0;
    MoaChoice choice;

    public GuiDonatorMoa(EntityPlayer player, GuiScreen parent)
    {
        this(new PartyData(), player, parent);
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

    public GuiDonatorMoa(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.dif = 0.0025F;
        this.parent = parent;
        this.player = player;
        Aether.getInstance();
        this.donator = Aether.syncDonatorList.getDonator(player.username);
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.wParty = 256;
        this.hParty = 256;
        this.backgroundTexture = TEXTURE_CHOICE_MENU;
        this.updateScreen();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.updateScreen();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
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
                    Aether var10000;

                    if (moaIndex >= Aether.donatorChoices.moaChoices.size())
                    {
                        var10000 = Aether.instance;
                        Aether.syncDonatorList.sendTypeRemoveToAll(this.player.username, EnumChoiceType.MOA);
                        this.donator.removeChoiceType(EnumChoiceType.MOA);
                        moaIndex = 0;
                        return;
                    }

                    this.donator.addChoice((DonatorChoice)Aether.donatorChoices.moaChoices.get(moaIndex));
                    var10000 = Aether.instance;
                    Aether.syncDonatorList.sendChoiceToAll(this.player.username, (DonatorChoice)Aether.donatorChoices.moaChoices.get(moaIndex), true);
                    ++moaIndex;
                }

                break;

            case 2:
                if (this.donator != null)
                {
                    if (colourIndex >= AetherMoaColour.colours.size())
                    {
                        ((MoaChoice)this.donator.getChoiceFromType(EnumChoiceType.MOA)).setOverrideAll(true);
                        colourIndex = 0;
                        return;
                    }

                    ((MoaChoice)this.donator.getChoiceFromType(EnumChoiceType.MOA)).setOverrideAll(false);
                    ((MoaChoice)this.donator.getChoiceFromType(EnumChoiceType.MOA)).setOverridingColour((AetherMoaColour)AetherMoaColour.colours.get(colourIndex));
                    ++colourIndex;
                }
        }
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
        this.buttonList.clear();
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_CHOICE_MENU);
        int centerX = this.xParty - 97;
        int centerY = this.yParty - 56;
        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 194, this.hParty);
        this.buttonList.add(new GuiButton(0, this.xParty + 10, this.yParty + 27, 80, 20, "Back"));
        GuiButton typeButton = null;
        GuiButton overrideButton = null;
        boolean hasChoice = this.donator.containsChoiceType(EnumChoiceType.MOA);

        if (this.donator != null)
        {
            this.choice = null;

            if (hasChoice)
            {
                this.backgroundTexture = TEXTURE_CHOICE_MENU;
                this.choice = (MoaChoice)this.donator.getChoiceFromType(EnumChoiceType.MOA);
            }
            else
            {
                this.backgroundTexture = TEXTURE_CHOICE_MENU_2;
            }

            this.moaEntity = new EntityMoa(Aether.proxy.getClientWorld(), true, false, false, AetherMoaColour.pickRandomMoa(), Aether.proxy.getClientPlayer(), hasChoice ? this.choice.textureFile.localURL : null);
            typeButton = new GuiButton(1, this.xParty + 10, this.yParty - 35, 80, 20, this.choice != null ? this.choice.name : "Off");
            overrideButton = new GuiButton(2, this.xParty + 10, this.yParty, 80, 20, this.choice != null ? (this.choice.getOverrideAll() ? "All" : (this.choice.getOverridingColour() != null ? this.choice.getOverridingColour().name + " Moa" : "None")) : "None");
        }

        if (this.donator == null)
        {
            typeButton.enabled = false;
            overrideButton.enabled = false;
        }

        if (this.donator != null && !this.donator.containsChoiceType(EnumChoiceType.MOA))
        {
            overrideButton.enabled = false;
        }

        this.buttonList.add(typeButton);
        this.buttonList.add(overrideButton);
        super.drawScreen(x, y, partialTick);
        String header = "Donator Moa";
        String override = "Override";
        this.drawString(this.fontRenderer, header, sr.getScaledWidth() / 2 - this.fontRenderer.getStringWidth(header) / 2 + 49, centerY + 10, 15658734);
        this.drawString(this.fontRenderer, override, sr.getScaledWidth() / 2 - this.fontRenderer.getStringWidth(override) / 2 + 49, centerY + 45, 15658734);

        if (this.choice != null)
        {
            this.renderMoa(this.moaEntity, Minecraft.getMinecraft(), this.xParty - 40, this.yParty + 35, 30, 1.0F, 1.0F, this.donator != null && this.donator.containsChoiceType(EnumChoiceType.MOA));
        }
    }

    public void drawPlayerOnGui(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5, boolean lighting)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par1, (float)par2, 50.0F);
        GL11.glScalef(20.5F, 20.5F, 50.5F);
        GL11.glRotatef(180.0F, this.rotationY, 0.0F, 1.0F);
        float f2 = par0Minecraft.thePlayer.renderYawOffset;
        float f3 = par0Minecraft.thePlayer.rotationYaw;
        float f4 = par0Minecraft.thePlayer.rotationPitch;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((double)(par5 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        par0Minecraft.thePlayer.renderYawOffset = (float)Math.atan((double)(par4 / 40.0F)) * 20.0F;
        par0Minecraft.thePlayer.rotationYaw = (float)Math.atan((double)(par4 / 40.0F)) * 40.0F;
        par0Minecraft.thePlayer.rotationPitch = -((float)Math.atan((double)(par5 / 40.0F))) * 20.0F;
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
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void renderMoa(Entity entity, Minecraft mc, int x, int y, int scale, float par4, float par5, boolean lighting)
    {
        if ((double)this.rotationY > 2.5D)
        {
            this.dif = -0.0025F;
        }
        else if ((double)this.rotationY < -2.5D)
        {
            this.dif = 0.0025F;
        }

        this.rotationY += this.dif;
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, 100.0F);
        GL11.glScalef((float)(-scale), (float)scale, (float)scale);
        GL11.glRotatef(180.0F, this.rotationY, 0.0F, 1.0F);
        RenderHelper.enableStandardItemLighting();

        if (entity instanceof EntityLiving)
        {
            ((EntityLiving)entity).rotationYawHead = 0.0F;
        }

        RenderManager.instance.playerViewY = 180.0F;

        if (!lighting)
        {
            ;
        }

        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 660.0F);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
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
}
