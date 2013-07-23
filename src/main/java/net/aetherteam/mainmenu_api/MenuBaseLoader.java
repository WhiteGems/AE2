package net.aetherteam.mainmenu_api;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class MenuBaseLoader extends GuiScreen
{
    private int totalHeight;
    private int backgroundTexture;
    private int x2;
    private int y2;
    private int loaderWidth;
    private int loaderHeight;

    /** Reference to the Minecraft object. */
    Minecraft mc = FMLClientHandler.instance().getClient();
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
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/mainmenu_api/gui/menulist.png");
        this.loaderWidth = 256;
        this.loaderHeight = 256;
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

        this.launchMenu = new GuiButton(0, this.x2 - 35, this.y2 + 85 - 28, 72, 20, "启动菜单");

        this.launchMenu.enabled = false;
        this.buttonList.add(this.launchMenu);
        this.rightPage = new GuiButton(1, this.x2 + 44, this.y2 + 57, 20, 20, ">");
        this.leftPage = new GuiButton(2, this.x2 - 62, this.y2 + 57, 20, 20, "<");
        this.buttonList.add(this.rightPage);
        this.buttonList.add(this.leftPage);
        MenuBaseConfig.loadConfig();
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
            for (int var4 = 0; var4 < this.menuSlotList.size(); ++var4)
            {
                MenuSlot var5 = (MenuSlot)this.menuSlotList.get(var4);

                if (var5.mousePressed(this.mc, var1, var2))
                {
                    var5.selected = true;
                    this.launchMenu.enabled = true;
                    this.selectedMenuSlot = var5;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                }
                else
                {
                    var5.selected = false;
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

        if (var1.id == 1)
        {
            ++this.pageNumber;
        }

        if (var1.id == 2)
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
    public void drawScreen(int var1, int var2, float var3)
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
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int var4 = Mouse.getDWheel();
        int var5 = this.x2 - 70;
        int var6 = this.y2 - 84;
        ScaledResolution var7 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(var5, var6, 0, 0, 141, this.loaderHeight);
        this.totalHeight = 0;
        byte var8 = 100;
        byte var9 = 20;
        byte var10 = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((var5 + 14) * var7.getScaleFactor(), (var6 + 35) * var7.getScaleFactor(), var8 * var7.getScaleFactor(), 103 * var7.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = MenuBaseSorter.getSize() * (var9 + var10);

        if (this.totalHeight > 103)
        {
            ;
        }

        this.totalHeight = 0;
        int var11;

        if (!this.slotsCreated)
        {
            for (var11 = 0; var11 < MenuBaseSorter.getSize(); ++var11)
            {
                HashMap var12 = MenuBaseSorter.getMenuHashMap();
                ArrayList var13 = new ArrayList(var12.keySet());
                MenuBase var14 = MenuBaseSorter.createMenuBaseObject((String)var13.get(var11));
                System.out.println(var14);

                if (var14 != null)
                {
                    this.totalHeight += var9 + var10;
                    MenuSlot var15 = new MenuSlot(var14, this.menuSlotList.size(), var5 + 15, var6 + this.totalHeight + 30, var8, var9);
                    this.menuSlotList.add(var15);
                    this.menuSlotToString.put(var15, (String)var13.get(var11));
                    this.menuSlotFromString.put((String)var13.get(var11), var15);
                    MenuSlot var16 = (MenuSlot)this.menuSlotList.get(var11);
                    System.out.println(var16.menu);
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
            var11 = 0;

            while (var11 < this.menuSlotList.size() - 2)
            {
                var17 = new MenuPage();

                for (var19 = 0; var19 < var17.getPageAmount(); ++var19)
                {
                    var17.addMenuSlot((MenuSlot)this.menuSlotList.get(var11));
                    ++var11;
                }

                this.menuPages.add(var17);
            }

            this.pagesCreated = true;
        }

        for (var11 = 0; var11 < this.menuPages.size(); ++var11)
        {
            var17 = (MenuPage)this.menuPages.get(this.pageNumber);

            for (var19 = 0; var19 < var17.getPageAmount(); ++var19)
            {
                MenuSlot var20 = var17.getMenuSlot(var19);
                var20.drawMenuSlot(this.mc, var5 + 15, var6 + this.totalHeight + 30);
                this.totalHeight += var9 + var10;

                if (this.totalHeight > 83)
                {
                    this.totalHeight = 0;
                }
            }
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.mc.renderEngine.resetBoundTexture();
        String var18 = "Pages: " + (this.pageNumber + 1) + "/" + this.menuPages.size();
        this.fontRenderer.drawStringWithShadow(var18, var5 + 69 - this.fontRenderer.getStringWidth(var18) / 2, var6 + 11, 16777215);
        super.drawScreen(var1, var2, var3);
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
        this.x2 = var2 / 2;
        this.y2 = var3 / 2;
    }

    /**
     * Draws the background (i is always 0 as of 1.2.2)
     */
    public void drawBackground(int var1)
    {
        super.drawBackground(var1);
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
        SoundManager var10000 = MainMenuAPI.proxy.getClient().sndManager;
        SoundManager.sndSystem.stop("streaming");
    }
}
