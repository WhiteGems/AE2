package net.aetherteam.aether.client.gui.donator;

import cpw.mods.fml.client.FMLClientHandler;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.client.gui.social.PartyData;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.EnumChoiceType;
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

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private EntityPlayer player;
    private GuiScreen parent;
    private float rotationY;
    public float dif;
    Donator donator;
    static int capeIndex = 0;

    public GuiDonatorCape(EntityPlayer var1, GuiScreen var2)
    {
        this(new PartyData(), var1, var2);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        super.keyTyped(var1, var2);

        if (var2 == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }

    public GuiDonatorCape(PartyData var1, EntityPlayer var2, GuiScreen var3)
    {
        this.dif = 0.0025F;
        this.parent = var3;
        this.player = var2;
        Aether.getInstance();
        this.donator = Aether.syncDonatorList.getDonator(var2.username);
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = var1;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/choiceMenu.png");
        this.wParty = 256;
        this.hParty = 256;
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
    protected void actionPerformed(GuiButton var1)
    {
        switch (var1.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
                break;

            case 1:
                if (this.donator != null)
                {
                    Aether var10000;

                    if (capeIndex >= Aether.donatorChoices.capeChoices.size())
                    {
                        var10000 = Aether.instance;
                        Aether.syncDonatorList.sendTypeRemoveToAll(this.player.username, EnumChoiceType.CAPE);
                        this.donator.removeChoiceType(EnumChoiceType.CAPE);
                        capeIndex = 0;
                        return;
                    }

                    this.donator.addChoice((DonatorChoice)Aether.donatorChoices.capeChoices.get(capeIndex));
                    var10000 = Aether.instance;
                    Aether.syncDonatorList.sendChoiceToAll(this.player.username, (DonatorChoice)Aether.donatorChoices.capeChoices.get(capeIndex), true);
                    ++capeIndex;
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
    public void drawScreen(int var1, int var2, float var3)
    {
        this.buttonList.clear();
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.backgroundTexture);
        int centerX = this.xParty - 97;
        int centerY = this.yParty - 56;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 194, this.hParty);

        this.buttonList.add(new GuiButton(0, this.xParty + 10, this.yParty + 27, 80, 20, "返回"));

        GuiButton typeButton = null;
        GuiButton overrideButton = null;

        boolean hasChoice = this.donator.containsChoiceType(EnumChoiceType.CAPE);

        if (this.donator != null)
        {
            DonatorChoice choice = null;

            if (hasChoice)
            {
                this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/choiceMenu.png");
                choice = this.donator.getChoiceFromType(EnumChoiceType.CAPE);
            }
            else
            {
                this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/choiceMenu2.png");
            }

            typeButton = new GuiButton(1, this.xParty + 10, this.yParty - 35, 80, 20, choice != null ? choice.name : "关闭");
        }

        if (this.donator == null)
        {
            typeButton.enabled = false;
        }

        this.buttonList.add(typeButton);
        super.drawScreen(var1, var2, var3);
        this.mc.renderEngine.resetBoundTexture();

        String header = "捐赠者披风";

        drawString(this.fontRenderer, header, sr.getScaledWidth() / 2 - this.fontRenderer.getStringWidth(header) / 2 + 49, centerY + 10, 15658734);

        drawPlayerOnGui(this.mc, this.xParty - 40, this.yParty + 35, 30, 1.0F, 1.0F, (this.donator != null) && (this.donator.containsChoiceType(EnumChoiceType.CAPE)));
    }

    public void drawPlayerOnGui(Minecraft var1, int var2, int var3, int var4, float var5, float var6, boolean var7)
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
        GL11.glTranslatef((float)var2, (float)var3, 50.0F);
        GL11.glScalef((float)(-var4), (float)var4, 50.5F);
        GL11.glRotatef(180.0F, this.rotationY, 0.0F, 1.0F);
        float var8 = var1.thePlayer.renderYawOffset;
        float var9 = var1.thePlayer.rotationYaw;
        float var10 = var1.thePlayer.rotationPitch;
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

        if (!var7)
        {
            GL11.glColor4f(0.35F, 0.35F, 0.35F, 1.0F);
        }

        RenderManager.instance.renderEntityWithPosYaw(var1.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        var1.thePlayer.renderYawOffset = var8;
        var1.thePlayer.rotationYaw = var9;
        var1.thePlayer.rotationPitch = var10;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void renderMoa(Entity var1, Minecraft var2, int var3, int var4, int var5, float var6, float var7, boolean var8)
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
        GL11.glTranslatef((float)var3, (float)var4, 100.0F);
        GL11.glScalef((float)(-var5), (float)var5, (float)var5);
        GL11.glRotatef(180.0F, this.rotationY, 0.0F, 1.0F);
        RenderHelper.enableStandardItemLighting();

        if (var1 instanceof EntityLiving)
        {
            ((EntityLiving)var1).rotationYawHead = 0.0F;
        }

        RenderManager.instance.playerViewY = 180.0F;

        if (!var8)
        {
            ;
        }

        RenderManager.instance.renderEntityWithPosYaw(var1, 0.0D, 0.0D, 0.0D, 0.0F, 660.0F);
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
        ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var2 = var1.getScaledWidth();
        int var3 = var1.getScaledHeight();
        this.xParty = var2 / 2;
        this.yParty = var3 / 2;
    }
}