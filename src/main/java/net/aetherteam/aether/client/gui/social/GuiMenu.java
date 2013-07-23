package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.List;
import net.aetherteam.aether.client.gui.social.options.GuiOptions;
import net.aetherteam.aether.notifications.client.GuiNotificationList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiMenu extends GuiScreen
{
    private final PartyData pm;
    private int backgroundTexture;
    private int easterTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private EntityPlayer player;
    private GuiScreen parent;

    public GuiMenu(EntityPlayer var1, GuiScreen var2)
    {
        this(new PartyData(), var1, var2);
    }

    public GuiMenu(PartyData var1, EntityPlayer var2, GuiScreen var3)
    {
        this.parent = var3;
        this.player = var2;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = var1;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
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

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.updateScreen();
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "返回"));
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
                this.mc.displayGuiScreen(new GuiNotificationList(this.player, this));
                break;

            case 2:
                this.mc.displayGuiScreen(new GuiOptions(this.player, this));
                break;

            case 3:
                this.mc.displayGuiScreen(new GuiPlayerList(this.player, this));

            case 4:
            default:
                break;

            case 5:
                this.mc.displayGuiScreen(new GuiPartyMenu(this.player, this));
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
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        List var7 = this.mc.thePlayer.sendQueue.playerInfoList;

        if (var7.size() > 1 || var7.size() == 0)
        {
            this.buttonList.add(new GuiButton(5, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "公会"));
            this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "消息"));
            this.buttonList.add(new GuiButton(3, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "玩家列表"));
            this.buttonList.add(new GuiButton(2, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "选项"));
            this.buttonList.add(new GuiButton(4, this.xParty - 60, this.yParty + 52 - 28, 120, 20, "好友列表"));

            ((GuiButton) this.buttonList.get(4)).enabled = false;
        }

        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "返回"));

        this.mc.renderEngine.resetBoundTexture();

        if (var7.size() <= 1 && var7.size() != 0)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
            this.drawTexturedModalRect(centerX + 13, centerY + 40, 141, 131, 115, 125);
            this.mc.renderEngine.resetBoundTexture();
            drawString(this.fontRenderer, "注定孤独一生 :(", centerX + 20, centerY + 100, 16750199);
            drawString(this.fontRenderer, "(单人游戏)", centerX + 25, centerY + 112, 16750199);
            this.buttonList.add(new GuiButton(2, this.xParty - 60, this.yParty - 40 - 35, 120, 20, "选项"));
        }
        else
        {
            this.mc.renderEngine.resetBoundTexture();
            drawString(this.fontRenderer, "社区菜单", centerX + 70 - this.fontRenderer.getStringWidth("社区菜单") / 2, centerY + 5, 16777215);
        }

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
        this.xParty = var2 / 2;
        this.yParty = var3 / 2;
    }
}
