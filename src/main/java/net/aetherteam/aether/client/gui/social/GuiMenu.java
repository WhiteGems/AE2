package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;

import java.util.List;

import net.aetherteam.aether.client.gui.social.options.GuiOptions;
import net.aetherteam.aether.notifications.client.GuiNotificationList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
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
    Minecraft f;
    private EntityPlayer player;
    private GuiScreen parent;

    public GuiMenu(EntityPlayer player, GuiScreen parent)
    {
        this(new PartyData(), player, parent);
    }

    public GuiMenu(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        }
    }

    public void initGui()
    {
        updateScreen();
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "Back"));
    }

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiNotificationList(this.player, this));
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiPlayerList(this.player, this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiOptions(this.player, this));
                break;
            case 5:
                this.mc.displayGuiScreen(new GuiPartyMenu(this.player, this));
            case 4:
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        this.buttonList.clear();

        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.backgroundTexture);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);

        List playerList = this.mc.thePlayer.sendQueue.playerInfoList;

        if ((playerList.size() > 1) || (playerList.size() == 0))
        {
            this.buttonList.add(new GuiButton(5, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "Party"));
            this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "Notifications"));
            this.buttonList.add(new GuiButton(3, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Player List"));
            this.buttonList.add(new GuiButton(2, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "Options"));
            this.buttonList.add(new GuiButton(4, this.xParty - 60, this.yParty + 52 - 28, 120, 20, "Friend List"));

            ((GuiButton) this.buttonList.get(4)).enabled = false;
        }

        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "Back"));

        this.mc.renderEngine.resetBoundTexture();

        if ((playerList.size() <= 1) && (playerList.size() != 0))
        {
            GL11.glBindTexture(3553, this.backgroundTexture);
            drawTexturedModalRect(centerX + 13, centerY + 40, 141, 131, 115, 125);

            this.mc.renderEngine.resetBoundTexture();
            drawString(this.fontRenderer, "Forever Alone :(", centerX + 26, centerY + 10, 15658734);
            drawString(this.fontRenderer, "(Single Player)", centerX + 31, centerY + 22, 15658734);
        } else
        {
            this.mc.renderEngine.resetBoundTexture();
            drawString(this.fontRenderer, "Social Menu", centerX + 40, centerY + 5, 16777215);
        }

        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiMenu
 * JD-Core Version:    0.6.2
 */