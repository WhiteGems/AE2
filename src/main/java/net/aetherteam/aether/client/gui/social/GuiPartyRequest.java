package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiPartyRequest extends GuiScreen
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

    public GuiPartyRequest(EntityPlayer player, GuiScreen parent)
    {
        this(new PartyData(), player, parent);
    }

    public GuiPartyRequest(PartyData pm, EntityPlayer player, GuiScreen parent)
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
        this.k.add(new GuiButton(5, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "Ownership"));
        this.k.add(new GuiButton(4, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "Operator"));
        this.k.add(new GuiButton(3, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Leave"));
        this.k.add(new GuiButton(2, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "Disband"));
        this.k.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "Back"));
    }

    protected void actionPerformed(GuiButton btn)
    {
        switch (btn.id)
        {
            case 0:
                this.g.displayGuiScreen(this.parent);
                break;

            case 3:
                this.g.displayGuiScreen(new GuiPlayerList(this.player, this));
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        this.g.renderEngine.a();
        String name = "Request Party Position";
        drawString(this.m, name, centerX + 69 - this.m.getStringWidth(name) / 2, centerY + 5, 16777215);
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

