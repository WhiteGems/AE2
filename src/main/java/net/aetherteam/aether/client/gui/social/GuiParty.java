package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiParty extends GuiScreen
{
    private static final String ONLINE_TEXT = "在线";
    private static final String OFFLINE_TEXT = "离线";
    private static final String BUTTON_INVITE_TEXT = "邀请";
    private static final String BUTTON_REMOVE_TEXT = "移除";
    private static final String BUTTON_CONFIRM_TEXT = "确认";
    private static final String BUTTON_CANCEL_TEXT = "取消";
    private static final int ONLINE_FONT_COLOR = 7859831;
    private static final int OFFLINE_FONT_COLOR = 15628151;
    private static final int BUTTON_INVITE = 0;
    private static final int BUTTON_REMOVE = 1;
    private static final int BUTTON_CONFIRM = 2;
    private static final int BUTTON_CANCEL = 3;
    private static final int DIALOGUE_NONE = 4;
    private static final int DIALOGUE_INVITE = 5;
    private static final int DIALOGUE_REMOVE = 6;
    private int dialogueState = 4;
    private boolean dialogue = false;
    private final PartyData pm;
    private GuiTextField partyNameField;
    private GuiTextField dialogueInput;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private String name = "以太公会";
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft mc;

    public GuiParty()
    {
        this(new PartyData());
    }

    public GuiParty(PartyData pm)
    {
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
        pm.pList_online.add(this.mc.thePlayer);
        pm.pList_online.add(this.mc.thePlayer);
        pm.pList_offline.add(this.mc.thePlayer);
        pm.pList_offline.add(this.mc.thePlayer);
        pm.pList_offline.add(this.mc.thePlayer);
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        updateScreen();
        this.buttonList.clear();
        if (this.sbar != null) this.sbarVal = this.sbar.sliderValue;
        this.sbar = new GuiYSlider(-1, this.xParty + 46, this.yParty - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
        this.buttonList.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 52, 20, "邀请"));
        this.buttonList.add(new GuiButton(1, this.xParty - 1, this.yParty + 85 - 28, 60, 20, "移除"));
        this.partyNameField = new GuiTextField(this.fontRenderer, this.xParty - 55, this.yParty - 73, 107, 16);
        this.partyNameField.setFocused(true);
        this.partyNameField.setMaxStringLength(16);
        this.partyNameField.setText(this.name);
        this.partyNameField.setEnableBackgroundDrawing(false);
        if (this.dialogue) openDialogue();
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        if (this.partyNameField.isFocused())
        {
            this.partyNameField.textboxKeyTyped(charTyped, keyTyped);
            this.name = this.partyNameField.getText();
        }
        if (this.dialogue)
        {
            this.dialogueInput.textboxKeyTyped(charTyped, keyTyped);
        }
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        }
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        this.partyNameField.mouseClicked(par1, par2, par3);
        if (!this.dialogue) this.sbar.mousePressed(this.mc, par1, par2);
        super.mouseClicked(par1, par2, par3);
    }

    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (par3 == 0) this.sbar.mouseReleased(par1, par2);
        super.mouseMovedOrUp(par1, par2, par3);
    }

    protected void actionPerformed(GuiButton btn)
    {
        switch (btn.id)
        {
            case 0:
                if (!this.dialogue)
                {
                    this.dialogue = true;
                    this.dialogueState = 5;
                    openDialogue();
                }
                break;
            case 1:
                if (!this.dialogue)
                {
                    this.dialogue = true;
                    this.dialogueState = 6;
                    openDialogue();
                }
                break;
            case 2:
                switch (this.dialogueState)
                {
                    case 5:
                        Minecraft.getMinecraft().theWorld.getPlayerEntityByName(this.dialogueInput.getText());
                        break;
                    case 6:
                }

                this.dialogue = false;
                this.dialogueState = 4;
                initGui();
                break;
            case 3:
                this.dialogue = false;
                this.dialogueState = 4;
                initGui();
        }
    }

    private String getDialogueOption()
    {
        switch (this.dialogueState)
        {
            case 5:
                return "邀请";
            case 6:
                return "移除";
        }
        return "";
    }

    private void openDialogue()
    {
        this.partyNameField.setFocused(false);
        this.dialogueInput = new GuiTextField(this.fontRenderer, this.xParty - 88 + this.fontRenderer.getStringWidth(getDialogueOption()), this.yParty - 7, 193 - this.fontRenderer.getStringWidth(getDialogueOption()) - 10, 16);
        this.dialogueInput.setFocused(true);
        this.dialogueInput.setCanLoseFocus(false);
        this.buttonList.add(new GuiButton(2, this.xParty - 1, this.yParty + 14, 50, 20, "确认"));
        this.buttonList.add(new GuiButton(3, this.xParty + 52, this.yParty + 14, 45, 20, "取消"));
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.backgroundTexture);
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= dmsy / 1000.0F;
        if (this.sbar.sliderValue > 1.0F) this.sbar.sliderValue = 1.0F;
        if (this.sbar.sliderValue < 0.0F) this.sbar.sliderValue = 0.0F;
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        int totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(3089);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        totalHeight = (this.pm.pList_offline.size() + this.pm.pList_online.size()) * (slotH + gutter);
        float sVal = -this.sbar.sliderValue * (totalHeight - 105);

        if (totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }
        totalHeight = 0;
        for (int i = 0; i < this.pm.pList_online.size(); i++)
        {
            EntityPlayer p = (EntityPlayer) this.pm.pList_online.get(i);
            drawPlayerSlot(p, centerX + 15, centerY + totalHeight + 30, slotW, slotH, true);
            totalHeight += slotH + gutter;
        }
        for (int i = 0; i < this.pm.pList_offline.size(); i++)
        {
            EntityPlayer p = (EntityPlayer) this.pm.pList_offline.get(i);
            drawPlayerSlot(p, centerX + 15, centerY + totalHeight + 30, slotW, slotH, false);
            totalHeight += slotH + gutter;
        }
        GL11.glPopMatrix();
        GL11.glDisable(3089);

        this.partyNameField.drawTextBox();

        if (totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        if (this.dialogue)
        {
            drawGradientRect(0, 0, this.width, this.height, -1728053248, -1728053248);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(3553, this.dialogueTexture);
            drawTexturedModalRect(centerX - 30, centerY + 71, 0, 0, 201, this.hParty - 201);
            this.dialogueInput.drawTextBox();
            this.fontRenderer.drawStringWithShadow(getDialogueOption() + ":", centerX - 24, centerY + 80, 16777215);
        }

        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();
        if (this.partyNameField != null)
        {
            this.partyNameField.updateCursorCounter();
        }
        if (this.dialogue)
        {
            this.dialogueInput.updateCursorCounter();
        }
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }

    public void drawPlayerSlot(EntityPlayer p, int x, int y, int width, int height, boolean online)
    {
        drawGradientRect(x, y, x + width, y + height, -11184811, -10066330);

        int icon = this.mc.renderEngine.getTextureForDownloadableImage(p.skinUrl, "/mob/char.png");

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, icon);
        GL11.glEnable(3553);

        float u = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
        GL11.glBegin(7);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex2f(x + 2, y + 2);
        GL11.glTexCoord2f(u, v1);
        GL11.glVertex2f(x + 2, y + 18);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x + 18, y + 18);
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f(x + 18, y + 2);
        GL11.glEnd();

        this.mc.renderEngine.resetBoundTexture();

        this.fontRenderer.drawStringWithShadow(p.username, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        this.fontRenderer.drawString(online ? "在线" : "离线", (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), online ? 7859831 : 15628151);
        GL11.glPopMatrix();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiParty
 * JD-Core Version:    0.6.2
 */
