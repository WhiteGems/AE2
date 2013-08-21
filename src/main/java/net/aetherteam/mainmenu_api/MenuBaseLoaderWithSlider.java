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
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MenuBaseLoaderWithSlider extends GuiScreen
{
    private static final ResourceLocation TEXTURE_ICON_DIRT = new ResourceLocation("mainmenu_api", "textures/icons/dirt.png");
    private static final ResourceLocation TEXTURE_MENULIST = new ResourceLocation("mainmenu_api", "textures/gui/menulist.png");
    private int totalHeight;
    private GuiTextField descriptionField;
    private GuiTextField dialogueInput;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private String description = "   请选择主界面菜单";
    private int loaderX;
    private int loaderY;
    private int loaderW;
    private int loaderH;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private MenuSlot selectedMenuSlot;
    private List menuSlotList = new ArrayList();
    HashMap<MenuSlot, String> menuSlotToString = new HashMap();
    HashMap<String, MenuSlot> menuSlotFromString = new HashMap();
    private GuiButton launchMenu;
    private boolean slotsCreated = false;
    public SoundManager soundManager;

    public MenuBaseLoaderWithSlider()
    {
        this.soundManager = MainMenuAPI.proxy.getClient().sndManager;
        this.mc = FMLClientHandler.instance().getClient();
        this.loaderW = 256;
        this.loaderH = 256;
        this.updateScreen();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
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

        this.launchMenu = new GuiButton(0, this.loaderX - 36, this.loaderY + 85 - 28, 72, 20, "启动！");

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
        MenuBase menu = null;

        if (MenuBaseConfig.selectedMenuName != null && !MenuBaseConfig.selectedMenuName.isEmpty())
        {
            menu = MenuBaseSorter.createMenuBaseObject(MenuBaseConfig.selectedMenuName);
        }

        if (menu == null)
        {
            System.out.println("The Menu Base \'" + MenuBaseConfig.selectedMenuName + "\' failed to initialize! Reverting to Menu selection.");
        }
        else
        {
            this.mc.displayGuiScreen(menu);
            this.mc.displayGuiScreen(menu);
            this.mc.displayGuiScreen(menu);
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2) {}

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.mc, par1, par2);
            }

            for (int l = 0; l < this.menuSlotList.size(); ++l)
            {
                int y = (int)((float)par2 + this.sbar.sliderValue * (float)(this.totalHeight - 103));
                MenuSlot menuSlot = (MenuSlot)this.menuSlotList.get(l);

                if (menuSlot.mousePressed(this.mc, par1, y) && par2 < this.loaderY + 50)
                {
                    menuSlot.selected = true;
                    this.launchMenu.enabled = true;
                    this.selectedMenuSlot = menuSlot;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                }
                else
                {
                    menuSlot.selected = false;
                }
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (par3 == 0 && this.totalHeight > 103)
        {
            this.sbar.mouseReleased(par1, par2);
        }

        super.mouseMovedOrUp(par1, par2, par3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0 && this.selectedMenuSlot != null)
        {
            String menuName = (String)this.menuSlotToString.get(this.selectedMenuSlot);

            if (menuName != null)
            {
                MenuBaseConfig.setProperty("selectedMenu", menuName);
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
    public void drawScreen(int x, int y, float partialTick)
    {
        this.drawBackground(0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.mc.renderEngine.func_110577_a(TEXTURE_MENULIST);
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= (float)(this.totalHeight > 103 ? dmsy : 0) / 1000.0F;

        if (this.sbar.sliderValue > 1.0F)
        {
            this.sbar.sliderValue = 1.0F;
        }

        if (this.sbar.sliderValue < 0.0F)
        {
            this.sbar.sliderValue = 0.0F;
        }

        int centerX = this.loaderX - 70;
        int centerY = this.loaderY - 84;
        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.loaderH);
        this.totalHeight = 0;
        byte slotW = 100;
        byte slotH = 20;
        byte gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = MenuBaseSorter.getSize() * (slotH + gutter);
        float sVal = -this.sbar.sliderValue * (float)(this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        this.totalHeight = 0;
        int i;

        if (!this.slotsCreated)
        {
            for (i = 0; i < MenuBaseSorter.getSize(); ++i)
            {
                HashMap menuSlotButton = MenuBaseSorter.getMenuHashMap();
                ArrayList keys = new ArrayList(menuSlotButton.keySet());
                MenuBase menu = MenuBaseSorter.createMenuBaseObject((String)keys.get(i));

                if (menu != null)
                {
                    MenuSlot menuSlot = new MenuSlot(menu, this.menuSlotList.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);
                    this.menuSlotList.add(menuSlot);
                    this.menuSlotToString.put(menuSlot, (String)keys.get(i));
                    this.menuSlotFromString.put((String)keys.get(i), menuSlot);
                    MenuSlot menuSlotButton1 = (MenuSlot)this.menuSlotList.get(i);
                    this.totalHeight += slotH + gutter;
                    System.out.println(menuSlotButton1.menu);
                }
            }

            this.slotsCreated = true;
        }

        for (i = 0; i < MenuBaseSorter.getSize(); ++i)
        {
            MenuSlot var18 = (MenuSlot)this.menuSlotList.get(i);
            var18.drawMenuSlot(this.mc, centerX + 15, centerY + this.totalHeight + 30);
            this.totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.descriptionField.drawTextBox();

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        super.drawScreen(x, y, partialTick);
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

        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.loaderX = width / 2;
        this.loaderY = height / 2;
    }

    /**
     * Draws the background (i is always 0 as of 1.2.2)
     */
    public void drawBackground(int i)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.mc.renderEngine.func_110577_a(TEXTURE_ICON_DIRT);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(10066329);
        tessellator.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / f + (float)i));
        tessellator.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / f), (double)((float)this.height / f + (float)i));
        tessellator.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / f), (double)(0 + i));
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)(0 + i));
        tessellator.draw();
    }

    private void muteMusic()
    {
        if (this.soundManager != null && this.soundManager.sndSystem != null)
        {
            ;
        }
    }
}
