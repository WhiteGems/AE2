package net.aetherteam.mainmenu_api;

import cpw.mods.fml.client.FMLClientHandler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import paulscode.sound.SoundSystem;

public class MenuBaseLoader extends GuiScreen
{
    private int totalHeight;
    private int backgroundTexture;
    private int x2;
    private int y2;
    private int loaderWidth;
    private int loaderHeight;
    Minecraft f;
    private MenuSlot selectedMenuSlot;
    private List menuSlotList = new ArrayList();
    HashMap menuSlotToString = new HashMap();
    HashMap menuSlotFromString = new HashMap();
    private GuiButton launchMenu;
    private boolean slotsCreated = false;
    private boolean pagesCreated = false;
    private GuiButton leftPage;
    private GuiButton rightPage;
    private List menuPages = new ArrayList();
    private int pageNumber = 0;

    public MenuBaseLoader()
    {
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/mainmenu_api/gui/menulist.png");
        this.loaderWidth = 256;
        this.loaderHeight = 256;
        updateScreen();
    }

    public void initGui()
    {
        MenuBaseConfig.ticks = 0;
        MenuBaseConfig.endMusic = true;
        muteMusic();

        Keyboard.enableRepeatEvents(true);
        updateScreen();
        this.buttonList.clear();

        this.launchMenu = new GuiButton(0, this.x2 - 35, this.y2 + 85 - 28, 72, 20, "Launch Menu");

        this.launchMenu.enabled = false;

        this.buttonList.add(this.launchMenu);

        this.rightPage = new GuiButton(1, this.x2 + 44, this.y2 + 57, 20, 20, ">");
        this.leftPage = new GuiButton(2, this.x2 - 62, this.y2 + 57, 20, 20, "<");

        this.buttonList.add(this.rightPage);
        this.buttonList.add(this.leftPage);

        MenuBaseConfig.loadConfig();

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
            for (int l = 0; l < this.menuSlotList.size(); l++)
            {
                MenuSlot menuSlot = (MenuSlot) this.menuSlotList.get(l);

                if (menuSlot.mousePressed(this.mc, par1, par2))
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

        if (button.id == 1)
        {
            this.pageNumber += 1;
        }

        if (button.id == 2)
        {
            this.pageNumber -= 1;
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        if (this.menuPages.size() == 1)
        {
            this.leftPage.enabled = false;
            this.rightPage.enabled = false;
        } else if (this.pageNumber == 0)
        {
            this.leftPage.enabled = false;
            this.rightPage.enabled = true;
        } else if (this.pageNumber == this.menuPages.size() - 1)
        {
            this.leftPage.enabled = true;
            this.rightPage.enabled = false;
        } else if (this.pageNumber > 0)
        {
            this.leftPage.enabled = true;
            this.rightPage.enabled = true;
        }

        drawBackground(0);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glBindTexture(3553, this.backgroundTexture);
        int dmsy = Mouse.getDWheel();

        int centerX = this.x2 - 70;
        int centerY = this.y2 - 84;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.loaderHeight);
        this.totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(3089);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();

        this.totalHeight = (MenuBaseSorter.getSize() * (slotH + gutter));

        if (this.totalHeight > 103) ;
        this.totalHeight = 0;

        if (!this.slotsCreated)
        {
            for (int i = 0; i < MenuBaseSorter.getSize(); i++)
            {
                HashMap map = MenuBaseSorter.getMenuHashMap();
                List keys = new ArrayList(map.keySet());

                MenuBase menu = MenuBaseSorter.createMenuBaseObject((String) keys.get(i));

                System.out.println(menu);

                if (menu != null)
                {
                    this.totalHeight += slotH + gutter;

                    MenuSlot menuSlot = new MenuSlot(menu, this.menuSlotList.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);

                    this.menuSlotList.add(menuSlot);
                    this.menuSlotToString.put(menuSlot, (String) keys.get(i));
                    this.menuSlotFromString.put((String) keys.get(i), menuSlot);

                    MenuSlot menuSlotButton = (MenuSlot) this.menuSlotList.get(i);

                    System.out.println(menuSlotButton.menu);
                }

                if (this.totalHeight > 83)
                {
                    this.totalHeight = 0;
                }
            }

            this.slotsCreated = true;
        }

        if (!this.pagesCreated)
        {
            int slotCount = 0;

            while (slotCount < this.menuSlotList.size() - 2)
            {
                MenuPage page = new MenuPage();

                for (int count = 0; count < page.getPageAmount(); count++)
                {
                    page.addMenuSlot((MenuSlot) this.menuSlotList.get(slotCount));
                    slotCount++;
                }

                this.menuPages.add(page);
            }

            this.pagesCreated = true;
        }

        for (int pageCount = 0; pageCount < this.menuPages.size(); pageCount++)
        {
            MenuPage currentPage = (MenuPage) this.menuPages.get(this.pageNumber);

            for (int count = 0; count < currentPage.getPageAmount(); count++)
            {
                MenuSlot menuSlotButton = currentPage.getMenuSlot(count);
                menuSlotButton.drawMenuSlot(this.mc, centerX + 15, centerY + this.totalHeight + 30);

                this.totalHeight += slotH + gutter;

                if (this.totalHeight > 83)
                {
                    this.totalHeight = 0;
                }
            }

        }

        GL11.glPopMatrix();
        GL11.glDisable(3089);

        this.mc.renderEngine.resetBoundTexture();

        String name = "Pages: " + (this.pageNumber + 1) + "/" + this.menuPages.size();

        this.fontRenderer.drawStringWithShadow(name, centerX + 69 - this.fontRenderer.getStringWidth(name) / 2, centerY + 11, 16777215);

        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();

        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.x2 = (width / 2);
        this.y2 = (height / 2);
    }

    public void drawBackground(int i)
    {
        super.drawBackground(i);

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
        SoundManager.sndSystem.stop("streaming");
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.mainmenu_api.MenuBaseLoader
 * JD-Core Version:    0.6.2
 */