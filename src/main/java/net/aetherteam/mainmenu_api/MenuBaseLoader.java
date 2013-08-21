package net.aetherteam.mainmenu_api;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class MenuBaseLoader extends GuiScreen
{
    private static final ResourceLocation TEXTURE_ICON_DIRT = new ResourceLocation("mainmenu_api", "textures/icons/dirt.png");
    private static final ResourceLocation TEXTURE_MENULIST = new ResourceLocation("mainmenu_api", "textures/gui/menulist.png");
    private int totalHeight;
    private int x2;
    private int y2;
    private int loaderWidth = 256;
    private int loaderHeight = 256;

    /** Reference to the Minecraft object. */
    Minecraft mc = FMLClientHandler.instance().getClient();
    private MenuSlot selectedMenuSlot;
    private List menuSlotList = new ArrayList();
    HashMap<MenuSlot, String> menuSlotToString = new HashMap();
    HashMap<String, MenuSlot> menuSlotFromString = new HashMap();
    private GuiButton launchMenu;
    private boolean slotsCreated = false;
    private boolean pagesCreated = false;
    private GuiButton leftPage;
    private GuiButton rightPage;
    private List menuPages = new ArrayList();
    private int pageNumber = 0;

    public MenuBaseLoader()
    {
        this.updateScreen();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        MenuBaseConfig.ticks = 0;
        MenuBaseConfig.endMusic = true;
        this.muteMusic();
        Keyboard.enableRepeatEvents(true);
        this.updateScreen();
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
            for (int l = 0; l < this.menuSlotList.size(); ++l)
            {
                MenuSlot menuSlot = (MenuSlot)this.menuSlotList.get(l);

                if (menuSlot.mousePressed(this.mc, par1, par2))
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

        if (button.id == 1)
        {
            ++this.pageNumber;
        }

        if (button.id == 2)
        {
            --this.pageNumber;
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
        if (this.menuPages.size() == 1)
        {
            this.leftPage.enabled = false;
            this.rightPage.enabled = false;
        }
        else if (this.pageNumber == 0)
        {
            this.leftPage.enabled = false;
            this.rightPage.enabled = true;
        }
        else if (this.pageNumber == this.menuPages.size() - 1)
        {
            this.leftPage.enabled = true;
            this.rightPage.enabled = false;
        }
        else if (this.pageNumber > 0)
        {
            this.leftPage.enabled = true;
            this.rightPage.enabled = true;
        }

        this.drawBackground(0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.mc.renderEngine.func_110577_a(TEXTURE_MENULIST);
        int dmsy = Mouse.getDWheel();
        int centerX = this.x2 - 70;
        int centerY = this.y2 - 84;
        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.loaderHeight);
        this.totalHeight = 0;
        byte slotW = 100;
        byte slotH = 20;
        byte gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = MenuBaseSorter.getSize() * (slotH + gutter);

        if (this.totalHeight > 103)
        {
            ;
        }

        this.totalHeight = 0;
        int name;

        if (!this.slotsCreated)
        {
            for (name = 0; name < MenuBaseSorter.getSize(); ++name)
            {
                HashMap currentPage = MenuBaseSorter.getMenuHashMap();
                ArrayList count = new ArrayList(currentPage.keySet());
                MenuBase menuSlotButton = MenuBaseSorter.createMenuBaseObject((String)count.get(name));
                System.out.println(menuSlotButton);

                if (menuSlotButton != null)
                {
                    this.totalHeight += slotH + gutter;
                    MenuSlot menuSlot = new MenuSlot(menuSlotButton, this.menuSlotList.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);
                    this.menuSlotList.add(menuSlot);
                    this.menuSlotToString.put(menuSlot, (String)count.get(name));
                    this.menuSlotFromString.put((String)count.get(name), menuSlot);
                    MenuSlot menuSlotButton1 = (MenuSlot)this.menuSlotList.get(name);
                    System.out.println(menuSlotButton1.menu);
                }

                if (this.totalHeight > 83)
                {
                    this.totalHeight = 0;
                }
            }

            this.slotsCreated = true;
        }

        MenuPage var17;
        int var19;

        if (!this.pagesCreated)
        {
            name = 0;

            while (name < this.menuSlotList.size() - 2)
            {
                var17 = new MenuPage();

                for (var19 = 0; var19 < var17.getPageAmount(); ++var19)
                {
                    var17.addMenuSlot((MenuSlot)this.menuSlotList.get(name));
                    ++name;
                }

                this.menuPages.add(var17);
            }

            this.pagesCreated = true;
        }

        for (name = 0; name < this.menuPages.size(); ++name)
        {
            var17 = (MenuPage)this.menuPages.get(this.pageNumber);

            for (var19 = 0; var19 < var17.getPageAmount(); ++var19)
            {
                MenuSlot var20 = var17.getMenuSlot(var19);
                var20.drawMenuSlot(this.mc, centerX + 15, centerY + this.totalHeight + 30);
                this.totalHeight += slotH + gutter;

                if (this.totalHeight > 83)
                {
                    this.totalHeight = 0;
                }
            }
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        String var18 = "Pages: " + (this.pageNumber + 1) + "/" + this.menuPages.size();
        this.fontRenderer.drawStringWithShadow(var18, centerX + 69 - this.fontRenderer.getStringWidth(var18) / 2, centerY + 11, 16777215);
        super.drawScreen(x, y, partialTick);
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
        this.x2 = width / 2;
        this.y2 = height / 2;
    }

    /**
     * Draws the background (i is always 0 as of 1.2.2)
     */
    public void drawBackground(int i)
    {
        super.drawBackground(i);
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
        MainMenuAPI.proxy.getClient().sndManager.sndSystem.stop("streaming");
    }
}
