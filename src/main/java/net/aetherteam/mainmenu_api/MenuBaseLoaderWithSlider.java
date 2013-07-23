package net.aetherteam.mainmenu_api;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MenuBaseLoaderWithSlider extends GuiScreen
{
    private int totalHeight;
    private GuiTextField descriptionField;
    private GuiTextField dialogueInput;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private String description = "Choose A Main Menu";
    private int backgroundTexture;
    private int loaderX;
    private int loaderY;
    private int loaderW;
    private int loaderH;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private MenuSlot selectedMenuSlot;
    private List menuSlotList = new ArrayList();
    HashMap menuSlotToString = new HashMap();
    HashMap menuSlotFromString = new HashMap();
    private GuiButton launchMenu;
    private boolean slotsCreated = false;
    public SoundManager soundManager;

    public MenuBaseLoaderWithSlider()
    {
        this.soundManager = MainMenuAPI.proxy.getClient().sndManager;
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/mainmenu_api/gui/menulist.png");
        this.loaderW = 256;
        this.loaderH = 256;
        this.updateScreen();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        MenuBaseConfig.jukebox.process();
        this.muteMusic();
        Keyboard.enableRepeatEvents(true);
        this.updateScreen();
        this.buttonList.clear();

        if (this.sbar != null)
        {
            this.sbarVal = this.sbar.sliderValue;
        }

        this.sbar = new GuiYSlider(-1, this.loaderX + 46, this.loaderY - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
        this.launchMenu = new GuiButton(0, this.loaderX - 36, this.loaderY + 85 - 28, 72, 20, "Launch Menu");
        this.launchMenu.enabled = false;
        this.buttonList.add(this.launchMenu);
        this.descriptionField = new GuiTextField(this.fontRenderer, this.loaderX - 49, this.loaderY - 73, 112, 16);
        this.descriptionField.setFocused(false);
        this.descriptionField.setMaxStringLength(24);
        this.descriptionField.setText(this.description);
        this.descriptionField.setEnableBackgroundDrawing(false);
        MenuBaseConfig.loadConfig();
        MenuBaseConfig.ticks = 0;
        MenuBaseConfig.endMusic = true;
        MenuBase var1 = null;

        if (MenuBaseConfig.selectedMenuName != null && !MenuBaseConfig.selectedMenuName.isEmpty())
        {
            var1 = MenuBaseSorter.createMenuBaseObject(MenuBaseConfig.selectedMenuName);
        }

        if (var1 == null)
        {
            System.out.println("The Menu Base \'" + MenuBaseConfig.selectedMenuName + "\' failed to initialize! Reverting to Menu selection.");
        }
        else
        {
            this.mc.displayGuiScreen(var1);
            this.mc.displayGuiScreen(var1);
            this.mc.displayGuiScreen(var1);
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2) {}

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        if (var3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.mc, var1, var2);
            }

            for (int var4 = 0; var4 < this.menuSlotList.size(); ++var4)
            {
                int var5 = (int)((float)var2 + this.sbar.sliderValue * (float)(this.totalHeight - 103));
                MenuSlot var6 = (MenuSlot)this.menuSlotList.get(var4);

                if (var6.mousePressed(this.mc, var1, var5) && var2 < this.loaderY + 50)
                {
                    var6.selected = true;
                    this.launchMenu.enabled = true;
                    this.selectedMenuSlot = var6;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                }
                else
                {
                    var6.selected = false;
                }
            }
        }

        super.mouseClicked(var1, var2, var3);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int var1, int var2, int var3)
    {
        if (var3 == 0 && this.totalHeight > 103)
        {
            this.sbar.mouseReleased(var1, var2);
        }

        super.mouseMovedOrUp(var1, var2, var3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        if (var1.id == 0 && this.selectedMenuSlot != null)
        {
            String var2 = (String)this.menuSlotToString.get(this.selectedMenuSlot);

            if (var2 != null)
            {
                MenuBaseConfig.setProperty("selectedMenu", var2);
            }

            this.mc.displayGuiScreen(this.selectedMenuSlot.menu);
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
        this.drawBackground(0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int var4 = Mouse.getDWheel();
        this.sbar.sliderValue -= (float)(this.totalHeight > 103 ? var4 : 0) / 1000.0F;

        if (this.sbar.sliderValue > 1.0F)
        {
            this.sbar.sliderValue = 1.0F;
        }

        if (this.sbar.sliderValue < 0.0F)
        {
            this.sbar.sliderValue = 0.0F;
        }

        int var5 = this.loaderX - 70;
        int var6 = this.loaderY - 84;
        ScaledResolution var7 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(var5, var6, 0, 0, 141, this.loaderH);
        this.totalHeight = 0;
        byte var8 = 100;
        byte var9 = 20;
        byte var10 = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((var5 + 14) * var7.getScaleFactor(), (var6 + 35) * var7.getScaleFactor(), var8 * var7.getScaleFactor(), 103 * var7.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = MenuBaseSorter.getSize() * (var9 + var10);
        float var11 = -this.sbar.sliderValue * (float)(this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, var11, 0.0F);
        }

        this.totalHeight = 0;
        int var12;

        if (!this.slotsCreated)
        {
            for (var12 = 0; var12 < MenuBaseSorter.getSize(); ++var12)
            {
                HashMap var13 = MenuBaseSorter.getMenuHashMap();
                ArrayList var14 = new ArrayList(var13.keySet());
                MenuBase var15 = MenuBaseSorter.createMenuBaseObject((String)var14.get(var12));

                if (var15 != null)
                {
                    MenuSlot var16 = new MenuSlot(var15, this.menuSlotList.size(), var5 + 15, var6 + this.totalHeight + 30, var8, var9);
                    this.menuSlotList.add(var16);
                    this.menuSlotToString.put(var16, (String)var14.get(var12));
                    this.menuSlotFromString.put((String)var14.get(var12), var16);
                    MenuSlot var17 = (MenuSlot)this.menuSlotList.get(var12);
                    this.totalHeight += var9 + var10;
                    System.out.println(var17.menu);
                }
            }

            this.slotsCreated = true;
        }

        for (var12 = 0; var12 < MenuBaseSorter.getSize(); ++var12)
        {
            MenuSlot var18 = (MenuSlot)this.menuSlotList.get(var12);
            var18.drawMenuSlot(this.mc, var5 + 15, var6 + this.totalHeight + 30);
            this.totalHeight += var9 + var10;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.descriptionField.drawTextBox();

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, var1, var2);
        }

        super.drawScreen(var1, var2, var3);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

        if (this.descriptionField != null)
        {
            this.descriptionField.updateCursorCounter();
        }

        ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var2 = var1.getScaledWidth();
        int var3 = var1.getScaledHeight();
        this.loaderX = var2 / 2;
        this.loaderY = var3 / 2;
    }

    /**
     * Draws the background (i is always 0 as of 1.2.2)
     */
    public void drawBackground(int var1)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator var2 = Tessellator.instance;
        this.mc.renderEngine.bindTexture("/net/aetherteam/mainmenu_api/icons/dirt.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var3 = 32.0F;
        var2.startDrawingQuads();
        var2.setColorOpaque_I(10066329);
        var2.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / var3 + (float)var1));
        var2.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / var3), (double)((float)this.height / var3 + (float)var1));
        var2.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / var3), (double)(0 + var1));
        var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)(0 + var1));
        var2.draw();
    }

    private void muteMusic()
    {
        if (this.soundManager != null)
        {
            SoundManager var10000 = this.soundManager;

            if (SoundManager.sndSystem != null)
            {
                var10000 = this.soundManager;
                SoundManager.sndSystem.stop("streaming");
            }
        }
    }
}
