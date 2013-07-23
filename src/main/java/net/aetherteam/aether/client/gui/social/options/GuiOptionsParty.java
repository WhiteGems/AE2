package net.aetherteam.aether.client.gui.social.options;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.List;
import net.aetherteam.aether.data.AetherOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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

public class GuiOptionsParty extends GuiScreen
{
    private int backgroundTexture;
    private int easterTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft g;
    private EntityPlayer player;
    private GuiScreen parent;

    public GuiOptionsParty(EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.player = player;
        this.g = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
    }

    public void initGui()
    {
        updateScreen();
        this.k.clear();
        this.k.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "Back"));
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

    protected void actionPerformed(GuiButton button)
    {
        List playerList = this.g.thePlayer.theWorldClient.c;
        boolean online = playerList.size() > 1;

        switch (button.id)
        {
            case 0:
                this.g.displayGuiScreen(this.parent);
                break;

            case 1:
                AetherOptions.setShowPartyHUD(!AetherOptions.getShowPartyHUD());
                break;

            case 2:
                AetherOptions.setMinimalPartyHUD(!AetherOptions.getMinimalPartyHUD());
                break;

            case 3:
                AetherOptions.setRenderHead(!AetherOptions.getRenderHead());
                break;

            case 4:
                AetherOptions.setShowPartyName(!AetherOptions.getShowPartyName());
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
        boolean online = playerList.size() > 1;
        String renderHeadString = AetherOptions.getRenderHead() ? "True" : "False";
        String showHUDString = AetherOptions.getShowPartyHUD() ? "True" : "False";
        String showNameString = AetherOptions.getShowPartyName() ? "On" : "Off";
        GuiButton minimalHUD = new GuiButton(2, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "HUD Style: " + (AetherOptions.getMinimalPartyHUD() ? "Minimal" : "Heavy"));
        GuiButton renderHead = new GuiButton(3, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Render Head: " + renderHeadString);
        GuiButton showName = new GuiButton(4, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "Party Name: " + showNameString);
        this.k.add(new GuiButton(1, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "Show HUD: " + showHUDString));
        this.k.add(minimalHUD);
        this.k.add(renderHead);
        this.k.add(showName);

        if (AetherOptions.getShowPartyHUD())
        {
            minimalHUD.enabled = true;
            renderHead.enabled = true;
            showName.enabled = true;
        }
        else
        {
            minimalHUD.enabled = false;
            renderHead.enabled = false;
            showName.enabled = false;
        }

        this.g.renderEngine.a();
        String title = "Party HUD";
        drawString(this.m, title, centerX + 70 - this.m.getStringWidth(title) / 2, centerY + 5, 16777215);
        this.k.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "Back"));
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

