package net.aetherteam.aether.client.gui.social.dialogue;

import cpw.mods.fml.client.FMLClientHandler;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.opengl.GL11;

public class GuiDialogueBox extends GuiScreen
{
    private static final int LEADER_TEXT_COLOR = 26367;
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    private GuiScreen parent;
    Minecraft f;
    private boolean created;
    private String createdText;
    private String failureText;

    public GuiDialogueBox(GuiScreen parent, String createdText, String failureText, boolean created)
    {
        this.createdText = createdText;
        this.failureText = failureText;
        this.created = created;
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        this.parent = parent;
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

        this.buttonList.add(new GuiButton(0, this.xParty - 58, this.yParty, 120, 20, "确认"));
    }

    protected void actionPerformed(GuiButton btn)
    {
        switch (btn.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        drawDefaultBackground();

        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;

        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.dialogueTexture);

        float scaleFactor = 1.3F;

        GL11.glTranslatef(this.xParty - 100.0F * scaleFactor, this.yParty - (this.hParty - 201) / 2 * scaleFactor, 0.0F);

        GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

        drawTexturedModalRect(0, 0, 0, 0, 201, this.hParty - 201);
        GL11.glPopMatrix();
        this.mc.renderEngine.resetBoundTexture();

        String warningLabel = this.created ? this.createdText : this.failureText;

        drawString(this.fontRenderer, warningLabel, centerX + 70 - this.fontRenderer.getStringWidth(warningLabel) / 2, centerY + 65, 16777215);

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
 * Qualified Name:     net.aetherteam.aether.client.gui.social.dialogue.GuiDialogueBox
 * JD-Core Version:    0.6.2
 */