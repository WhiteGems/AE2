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
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiParty extends GuiScreen
{
    private static final String ONLINE_TEXT = "Online";
    private static final String OFFLINE_TEXT = "Offline";
    private static final String BUTTON_INVITE_TEXT = "Invite";
    private static final String BUTTON_REMOVE_TEXT = "Remove";
    private static final String BUTTON_CONFIRM_TEXT = "Confirm";
    private static final String BUTTON_CANCEL_TEXT = "Cancel";
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
    private String name = "Aether Party";
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft g;

    public GuiParty()
    {
        this(new PartyData());
    }

    public GuiParty(PartyData pm)
    {
        this.g = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
        pm.pList_online.add(this.g.thePlayer);
        pm.pList_online.add(this.g.thePlayer);
        pm.pList_offline.add(this.g.thePlayer);
        pm.pList_offline.add(this.g.thePlayer);
        pm.pList_offline.add(this.g.thePlayer);
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        updateScreen();
        this.k.clear();

        if (this.sbar != null)
        {
            this.sbarVal = this.sbar.sliderValue;
        }

        this.sbar = new GuiYSlider(-1, this.xParty + 46, this.yParty - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
        this.k.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 52, 20, "Invite"));
        this.k.add(new GuiButton(1, this.xParty - 1, this.yParty + 85 - 28, 60, 20, "Remove"));
        this.partyNameField = new GuiTextField(this.m, this.xParty - 55, this.yParty - 73, 107, 16);
        this.partyNameField.setFocused(true);
        this.partyNameField.setMaxStringLength(16);
        this.partyNameField.setText(this.name);
        this.partyNameField.setEnableBackgroundDrawing(false);

        if (this.dialogue)
        {
            openDialogue();
        }
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
            this.g.displayGuiScreen((GuiScreen)null);
            this.g.setIngameFocus();
        }
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        this.partyNameField.mouseClicked(par1, par2, par3);

        if (!this.dialogue)
        {
            this.sbar.mousePressed(this.g, par1, par2);
        }

        super.mouseClicked(par1, par2, par3);
    }

    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            this.sbar.mouseReleased(par1, par2);
        }

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
                        Minecraft.getMinecraft().theWorld.a(this.dialogueInput.getText());
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
                return "Invite";

            case 6:
                return "Remove";
        }

        return "";
    }

    private void openDialogue()
    {
        this.partyNameField.setFocused(false);
        this.dialogueInput = new GuiTextField(this.m, this.xParty - 88 + this.m.getStringWidth(getDialogueOption()), this.yParty - 7, 193 - this.m.getStringWidth(getDialogueOption()) - 10, 16);
        this.dialogueInput.setFocused(true);
        this.dialogueInput.setCanLoseFocus(false);
        this.k.add(new GuiButton(2, this.xParty - 1, this.yParty + 14, 50, 20, "Confirm"));
        this.k.add(new GuiButton(3, this.xParty + 52, this.yParty + 14, 45, 20, "Cancel"));
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
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= dmsy / 1000.0F;

        if (this.sbar.sliderValue > 1.0F)
        {
            this.sbar.sliderValue = 1.0F;
        }

        if (this.sbar.sliderValue < 0.0F)
        {
            this.sbar.sliderValue = 0.0F;
        }

        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        int totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
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
            EntityPlayer p = (EntityPlayer)this.pm.pList_online.get(i);
            drawPlayerSlot(p, centerX + 15, centerY + totalHeight + 30, slotW, slotH, true);
            totalHeight += slotH + gutter;
        }

        for (int i = 0; i < this.pm.pList_offline.size(); i++)
        {
            EntityPlayer p = (EntityPlayer)this.pm.pList_offline.get(i);
            drawPlayerSlot(p, centerX + 15, centerY + totalHeight + 30, slotW, slotH, false);
            totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.partyNameField.drawTextBox();

        if (totalHeight > 103)
        {
            this.sbar.drawButton(this.g, x, y);
        }

        if (this.dialogue)
        {
            drawGradientRect(0, 0, this.height, this.i, -1728053248, -1728053248);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.dialogueTexture);
            drawTexturedModalRect(centerX - 30, centerY + 71, 0, 0, 201, this.hParty - 201);
            this.dialogueInput.drawTextBox();
            this.m.drawStringWithShadow(getDialogueOption() + ":", centerX - 24, centerY + 80, 16777215);
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

        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }

    public void drawPlayerSlot(EntityPlayer p, int x, int y, int width, int height, boolean online)
    {
        drawGradientRect(x, y, x + width, y + height, -11184811, -10066330);
        int icon = this.g.renderEngine.a(p.skinUrl, "/mob/char.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, icon);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float u = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex2f(x + 2, y + 2);
        GL11.glTexCoord2f(u, v1);
        GL11.glVertex2f(x + 2, y + 18);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x + 18, y + 18);
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f(x + 18, y + 2);
        GL11.glEnd();
        this.g.renderEngine.a();
        this.m.drawStringWithShadow(p.username, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        this.m.drawString(online ? "Online" : "Offline", (int)((x + height) / 0.75F), (int)((y + 12.0F) / 0.75F), online ? 7859831 : 15628151);
        GL11.glPopMatrix();
    }
}

