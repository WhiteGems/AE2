package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.List;
import net.aetherteam.aether.client.gui.social.options.GuiOptions;
import net.aetherteam.aether.notifications.client.GuiNotificationList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.multiplayer.CallableMPL2;
import net.minecraft.client.renderer.Tessellator;
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
    Minecraft g;
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
        this.g = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.g.displayGuiScreen((GuiScreen)null);
            this.g.setIngameFocus();
        }
    }

    public void initGui()
    {
        updateScreen();
        this.k.clear();
        this.k.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "Back"));
    }

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                this.g.displayGuiScreen(this.parent);
                break;

            case 1:
                this.g.displayGuiScreen(new GuiNotificationList(this.player, this));
                break;

            case 3:
                this.g.displayGuiScreen(new GuiPlayerList(this.player, this));
                break;

            case 2:
                this.g.displayGuiScreen(new GuiOptions(this.player, this));
                break;

            case 5:
                this.g.displayGuiScreen(new GuiPartyMenu(this.player, this));

            case 4:
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        this.k.clear();
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        List playerList = this.g.thePlayer.theWorldClient.c;

        if ((playerList.size() > 1) || (playerList.size() == 0))
        {
            this.k.add(new GuiButton(5, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "Party"));
            this.k.add(new GuiButton(1, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "Notifications"));
            this.k.add(new GuiButton(3, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Player List"));
            this.k.add(new GuiButton(2, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "Options"));
            this.k.add(new GuiButton(4, this.xParty - 60, this.yParty + 52 - 28, 120, 20, "Friend List"));
            ((GuiButton)this.k.get(4)).enabled = false;
        }

        this.k.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "Back"));
        this.g.renderEngine.a();

        if ((playerList.size() <= 1) && (playerList.size() != 0))
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
            drawTexturedModalRect(centerX + 13, centerY + 40, 141, 131, 115, 125);
            this.g.renderEngine.a();
            drawString(this.m, "§lForever Alone :(", centerX + 20, centerY + 100, 16750199);
            drawString(this.m, "§l(Single Player)", centerX + 25, centerY + 112, 16750199);
            this.k.add(new GuiButton(2, this.xParty - 60, this.yParty - 40 - 35, 120, 20, "Options"));
        }
        else
        {
            this.g.renderEngine.a();
            drawString(this.m, "Social Menu", centerX + 40, centerY + 5, 16777215);
        }

        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }
}

