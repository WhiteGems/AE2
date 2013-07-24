package net.aetherteam.aether.client.gui.social.options;

import cpw.mods.fml.client.FMLClientHandler;

import java.util.List;

import net.aetherteam.aether.data.AetherOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.renderer.RenderEngine;
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
    
    Minecraft mc;
    private EntityPlayer player;
    private GuiScreen parent;

    public GuiOptionsParty(EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
    }

    public void initGui()
    {
        updateScreen();
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "返回"));
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

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
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
        this.buttonList.clear();

        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.backgroundTexture);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);

        String renderHeadString = AetherOptions.getRenderHead() ? "是" : "否";
        String showHUDString = AetherOptions.getShowPartyHUD() ? "是" : "否";
        String showNameString = AetherOptions.getShowPartyName() ? "开" : "关";

        GuiButton minimalHUD = new GuiButton(2, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "HUD风格: " + (AetherOptions.getMinimalPartyHUD() ? "简约" : "华丽"));
        GuiButton renderHead = new GuiButton(3, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "头像渲染: " + renderHeadString);
        GuiButton showName = new GuiButton(4, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "公会名称: " + showNameString);

        this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "显示HUD: " + showHUDString));
        this.buttonList.add(minimalHUD);
        this.buttonList.add(renderHead);
        this.buttonList.add(showName);

        if (AetherOptions.getShowPartyHUD())
        {
            minimalHUD.enabled = true;
            renderHead.enabled = true;
            showName.enabled = true;
        } else
        {
            minimalHUD.enabled = false;
            renderHead.enabled = false;
            showName.enabled = false;
        }

        this.mc.renderEngine.resetBoundTexture();

        String title = "公会HUD";

        drawString(this.fontRenderer, title, centerX + 70 - this.fontRenderer.getStringWidth(title) / 2, centerY + 5, 16777215);
        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "返回"));

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
