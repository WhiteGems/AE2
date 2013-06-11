package net.aetherteam.mainmenu_api;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
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
    Minecraft f;
    private MenuSlot selectedMenuSlot;
    private List menuSlotList = new ArrayList();
    HashMap menuSlotToString = new HashMap();
    HashMap menuSlotFromString = new HashMap();
    private GuiButton launchMenu;
    private boolean slotsCreated = false;

    public SoundManager soundManager = MainMenuAPI.proxy.getClient().sndManager;

    public MenuBaseLoaderWithSlider()
    {
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/mainmenu_api/gui/menulist.png");
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
        this.buttonList.clear();
        if (this.sbar != null) this.sbarVal = this.sbar.sliderValue;
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

        MenuBase menu = null;

        if ((MenuBaseConfig.selectedMenuName != null) && (!MenuBaseConfig.selectedMenuName.isEmpty()))
        {
            menu = MenuBaseSorter.createMenuBaseObject(MenuBaseConfig.selectedMenuName);
        }

        if (menu == null)
        {
            System.out.println("The Menu Base '" + MenuBaseConfig.selectedMenuName + "' failed to initialize! Reverting to Menu selection.");
        } else
        {
            this.mc.displayGuiScreen(menu);
            this.mc.displayGuiScreen(menu);
            this.mc.displayGuiScreen(menu);
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
                this.sbar.mousePressed(this.mc, par1, par2);
            }
            for (int l = 0; l < this.menuSlotList.size(); l++)
            {
                int y = (int) (par2 + this.sbar.sliderValue * (this.totalHeight - 103));

                MenuSlot menuSlot = (MenuSlot) this.menuSlotList.get(l);

                if ((menuSlot.mousePressed(this.mc, par1, y)) && (par2 < this.loaderY + 50))
                {
                    menuSlot.selected = true;
                    this.launchMenu.enabled = true;
                    this.selectedMenuSlot = menuSlot;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                } else
                {
                    menuSlot.selected = false;
                }
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if ((par3 == 0) && (this.totalHeight > 103)) this.sbar.mouseReleased(par1, par2);
        super.mouseMovedOrUp(par1, par2, par3);
    }

    protected void actionPerformed(GuiButton button)
    {
        if ((button.id == 0) && (this.selectedMenuSlot != null))
        {
            String menuName = (String) this.menuSlotToString.get(this.selectedMenuSlot);

            if (menuName != null)
            {
                MenuBaseConfig.setProperty("selectedMenu", menuName);
            }

            this.mc.displayGuiScreen(this.selectedMenuSlot.menu);
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
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glBindTexture(3553, this.backgroundTexture);
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= (this.totalHeight > 103 ? dmsy : 0) / 1000.0F;
        if (this.sbar.sliderValue > 1.0F) this.sbar.sliderValue = 1.0F;
        if (this.sbar.sliderValue < 0.0F) this.sbar.sliderValue = 0.0F;
        int centerX = this.loaderX - 70;
        int centerY = this.loaderY - 84;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.loaderH);
        this.totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(3089);
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

                MenuBase menu = MenuBaseSorter.createMenuBaseObject((String) keys.get(i));

                if (menu != null)
                {
                    MenuSlot menuSlot = new MenuSlot(menu, this.menuSlotList.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);

                    this.menuSlotList.add(menuSlot);
                    this.menuSlotToString.put(menuSlot, (String) keys.get(i));
                    this.menuSlotFromString.put((String) keys.get(i), menuSlot);

                    MenuSlot menuSlotButton = (MenuSlot) this.menuSlotList.get(i);

                    this.totalHeight += slotH + gutter;

                    System.out.println(menuSlotButton.menu);
                }
            }

            this.slotsCreated = true;
        }

        for (int i = 0; i < MenuBaseSorter.getSize(); i++)
        {
            MenuSlot menuSlotButton = (MenuSlot) this.menuSlotList.get(i);
            menuSlotButton.drawMenuSlot(this.mc, centerX + 15, centerY + this.totalHeight + 30);

            this.totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(3089);

        this.descriptionField.drawTextBox();

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
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

        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.loaderX = (width / 2);
        this.loaderY = (height / 2);
    }

    public void drawBackground(int i)
    {
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.instance;

        this.mc.renderEngine.bindTexture("/net/aetherteam/mainmenu_api/icons/dirt.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(10066329);
        tessellator.addVertexWithUV(0.0D, this.height, 0.0D, 0.0D, this.height / f + i);
        tessellator.addVertexWithUV(this.width, this.height, 0.0D, this.width / f, this.height / f + i);
        tessellator.addVertexWithUV(this.width, 0.0D, 0.0D, this.width / f, 0 + i);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0 + i);
        tessellator.draw();
    }

    private void muteMusic()
    {
        if ((this.soundManager != null) && (SoundManager.sndSystem != null))
        {
            SoundManager.sndSystem.stop("streaming");
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.mainmenu_api.MenuBaseLoaderWithSlider
 * JD-Core Version:    0.6.2
 */