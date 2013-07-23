package net.aetherteam.mainmenu_api;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.Rect2i;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import paulscode.sound.SoundSystem;

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
    Minecraft g;
    private MenuSlot selectedMenuSlot;
    private List menuSlotList = new ArrayList();
    HashMap menuSlotToString = new HashMap();
    HashMap menuSlotFromString = new HashMap();
    private GuiButton launchMenu;
    private boolean slotsCreated = false;

    public SoundPoolEntry soundManager = MainMenuAPI.proxy.getClient().sndManager;

    public MenuBaseLoaderWithSlider()
    {
        this.g = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/mainmenu_api/gui/menulist.png");
        this.loaderW = 256;
        this.loaderH = 256;
        updateScreen();
    }

    public void initGui()
    {
        MenuBaseConfig.jukebox.process();
        muteMusic();
        Keyboard.enableRepeatEvents(true);
        updateScreen();
        this.k.clear();

        if (this.sbar != null)
        {
            this.sbarVal = this.sbar.sliderValue;
        }

        this.sbar = new GuiYSlider(-1, this.loaderX + 46, this.loaderY - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
        this.launchMenu = new GuiButton(0, this.loaderX - 36, this.loaderY + 85 - 28, 72, 20, "Launch Menu");
        this.launchMenu.enabled = false;
        this.k.add(this.launchMenu);
        this.descriptionField = new GuiTextField(this.m, this.loaderX - 49, this.loaderY - 73, 112, 16);
        this.descriptionField.setFocused(false);
        this.descriptionField.setMaxStringLength(24);
        this.descriptionField.setText(this.description);
        this.descriptionField.setEnableBackgroundDrawing(false);
        MenuBaseConfig.loadConfig();
        MenuBaseConfig.ticks = 0;
        MenuBaseConfig.endMusic = true;
        MenuBase menu = null;

        if ((MenuBaseConfig.selectedMenuName != null) && (!MenuBaseConfig.selectedMenuName.isEmpty()))
        {
            menu = MenuBaseSorter.createMenuBaseObject(MenuBaseConfig.selectedMenuName);
        }

        if (menu == null)
        {
            System.out.println("The Menu Base '" + MenuBaseConfig.selectedMenuName + "' failed to initialize! Reverting to Menu selection.");
        }
        else
        {
            this.g.displayGuiScreen(menu);
            this.g.displayGuiScreen(menu);
            this.g.displayGuiScreen(menu);
        }
    }

    protected void keyTyped(char par1, int par2)
    {
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.g, par1, par2);
            }

            for (int l = 0; l < this.menuSlotList.size(); l++)
            {
                int y = (int)(par2 + this.sbar.sliderValue * (this.totalHeight - 103));
                MenuSlot menuSlot = (MenuSlot)this.menuSlotList.get(l);

                if ((menuSlot.mousePressed(this.g, par1, y)) && (par2 < this.loaderY + 50))
                {
                    menuSlot.selected = true;
                    this.launchMenu.enabled = true;
                    this.selectedMenuSlot = menuSlot;
                    this.g.sndManager.a("random.click", 1.0F, 1.0F);
                }
                else
                {
                    menuSlot.selected = false;
                }
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if ((par3 == 0) && (this.totalHeight > 103))
        {
            this.sbar.mouseReleased(par1, par2);
        }

        super.mouseMovedOrUp(par1, par2, par3);
    }

    protected void actionPerformed(GuiButton button)
    {
        if ((button.id == 0) && (this.selectedMenuSlot != null))
        {
            String menuName = (String)this.menuSlotToString.get(this.selectedMenuSlot);

            if (menuName != null)
            {
                MenuBaseConfig.setProperty("selectedMenu", menuName);
            }

            this.g.displayGuiScreen(this.selectedMenuSlot.menu);
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        drawBackground(0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= (this.totalHeight > 103 ? dmsy : 0) / 1000.0F;

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
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.loaderH);
        this.totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = (MenuBaseSorter.getSize() * (slotH + gutter));
        float sVal = -this.sbar.sliderValue * (this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        this.totalHeight = 0;

        if (!this.slotsCreated)
        {
            for (int i = 0; i < MenuBaseSorter.getSize(); i++)
            {
                HashMap map = MenuBaseSorter.getMenuHashMap();
                List keys = new ArrayList(map.keySet());
                MenuBase menu = MenuBaseSorter.createMenuBaseObject((String)keys.get(i));

                if (menu != null)
                {
                    MenuSlot menuSlot = new MenuSlot(menu, this.menuSlotList.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);
                    this.menuSlotList.add(menuSlot);
                    this.menuSlotToString.put(menuSlot, (String)keys.get(i));
                    this.menuSlotFromString.put((String)keys.get(i), menuSlot);
                    MenuSlot menuSlotButton = (MenuSlot)this.menuSlotList.get(i);
                    this.totalHeight += slotH + gutter;
                    System.out.println(menuSlotButton.menu);
                }
            }

            this.slotsCreated = true;
        }

        for (int i = 0; i < MenuBaseSorter.getSize(); i++)
        {
            MenuSlot menuSlotButton = (MenuSlot)this.menuSlotList.get(i);
            menuSlotButton.drawMenuSlot(this.g, centerX + 15, centerY + this.totalHeight + 30);
            this.totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.descriptionField.drawTextBox();

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.g, x, y);
        }

        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();

        if (this.descriptionField != null)
        {
            this.descriptionField.updateCursorCounter();
        }

        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.loaderX = (width / 2);
        this.loaderY = (height / 2);
    }

    public void drawBackground(int i)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Rect2i tessellator = Rect2i.rectX;
        this.g.renderEngine.b("/net/aetherteam/mainmenu_api/icons/dirt.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.b();
        tessellator.d(10066329);
        tessellator.a(0.0D, this.i, 0.0D, 0.0D, this.i / f + i);
        tessellator.a(this.height, this.i, 0.0D, this.height / f, this.i / f + i);
        tessellator.a(this.height, 0.0D, 0.0D, this.height / f, 0 + i);
        tessellator.a(0.0D, 0.0D, 0.0D, 0.0D, 0 + i);
        tessellator.getRectX();
    }

    private void muteMusic()
    {
        if ((this.soundManager != null) && (SoundPoolEntry.soundName != null))
        {
            SoundPoolEntry.soundName.stop("streaming");
        }
    }
}

