package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
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
    private int dialogueState;
    private boolean dialogue;
    private final PartyData pm;
    private GuiTextField partyNameField;
    private GuiTextField dialogueInput;
    private GuiYSlider sbar;
    private float sbarVal;
    private String name;
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;

    public GuiParty()
    {
        this(new PartyData());
    }

    public GuiParty(PartyData var1)
    {
        this.dialogueState = 4;
        this.dialogue = false;
        this.sbarVal = 0.0F;
        this.name = "Aether Party";
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = var1;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
        var1.pList_online.add(this.mc.thePlayer);
        var1.pList_online.add(this.mc.thePlayer);
        var1.pList_offline.add(this.mc.thePlayer);
        var1.pList_offline.add(this.mc.thePlayer);
        var1.pList_offline.add(this.mc.thePlayer);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.updateScreen();
        this.buttonList.clear();

        if (this.sbar != null)
        {
            this.sbarVal = this.sbar.sliderValue;
        }

        this.sbar = new GuiYSlider(-1, this.xParty + 46, this.yParty - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
        this.buttonList.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 52, 20, "Invite"));
        this.buttonList.add(new GuiButton(1, this.xParty - 1, this.yParty + 85 - 28, 60, 20, "Remove"));
        this.partyNameField = new GuiTextField(this.fontRenderer, this.xParty - 55, this.yParty - 73, 107, 16);
        this.partyNameField.setFocused(true);
        this.partyNameField.setMaxStringLength(16);
        this.partyNameField.setText(this.name);
        this.partyNameField.setEnableBackgroundDrawing(false);

        if (this.dialogue)
        {
            this.openDialogue();
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        if (this.partyNameField.isFocused())
        {
            this.partyNameField.textboxKeyTyped(var1, var2);
            this.name = this.partyNameField.getText();
        }

        if (this.dialogue)
        {
            this.dialogueInput.textboxKeyTyped(var1, var2);
        }

        super.keyTyped(var1, var2);

        if (var2 == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        this.partyNameField.mouseClicked(var1, var2, var3);

        if (!this.dialogue)
        {
            this.sbar.mousePressed(this.mc, var1, var2);
        }

        super.mouseClicked(var1, var2, var3);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int var1, int var2, int var3)
    {
        if (var3 == 0)
        {
            this.sbar.mouseReleased(var1, var2);
        }

        super.mouseMovedOrUp(var1, var2, var3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        switch (var1.id)
        {
            case 0:
                if (!this.dialogue)
                {
                    this.dialogue = true;
                    this.dialogueState = 5;
                    this.openDialogue();
                }

                break;

            case 1:
                if (!this.dialogue)
                {
                    this.dialogue = true;
                    this.dialogueState = 6;
                    this.openDialogue();
                }

                break;

            case 2:
                switch (this.dialogueState)
                {
                    case 5:
                        Minecraft.getMinecraft().theWorld.getPlayerEntityByName(this.dialogueInput.getText());

                    case 6:
                    default:
                        this.dialogue = false;
                        this.dialogueState = 4;
                        this.initGui();
                        return;
                }

            case 3:
                this.dialogue = false;
                this.dialogueState = 4;
                this.initGui();
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

            default:
                return "";
        }
    }

    private void openDialogue()
    {
        this.partyNameField.setFocused(false);
        this.dialogueInput = new GuiTextField(this.fontRenderer, this.xParty - 88 + this.fontRenderer.getStringWidth(this.getDialogueOption()), this.yParty - 7, 193 - this.fontRenderer.getStringWidth(this.getDialogueOption()) - 10, 16);
        this.dialogueInput.setFocused(true);
        this.dialogueInput.setCanLoseFocus(false);
        this.buttonList.add(new GuiButton(2, this.xParty - 1, this.yParty + 14, 50, 20, "Confirm"));
        this.buttonList.add(new GuiButton(3, this.xParty + 52, this.yParty + 14, 45, 20, "Cancel"));
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
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int var4 = Mouse.getDWheel();
        this.sbar.sliderValue -= (float)var4 / 1000.0F;

        if (this.sbar.sliderValue > 1.0F)
        {
            this.sbar.sliderValue = 1.0F;
        }

        if (this.sbar.sliderValue < 0.0F)
        {
            this.sbar.sliderValue = 0.0F;
        }

        int var5 = this.xParty - 70;
        int var6 = this.yParty - 84;
        ScaledResolution var7 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(var5, var6, 0, 0, 141, this.hParty);
        boolean var8 = false;
        byte var9 = 100;
        byte var10 = 20;
        byte var11 = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((var5 + 14) * var7.getScaleFactor(), (var6 + 35) * var7.getScaleFactor(), var9 * var7.getScaleFactor(), 103 * var7.getScaleFactor());
        GL11.glPushMatrix();
        int var15 = (this.pm.pList_offline.size() + this.pm.pList_online.size()) * (var10 + var11);
        float var12 = -this.sbar.sliderValue * (float)(var15 - 105);

        if (var15 > 103)
        {
            GL11.glTranslatef(0.0F, var12, 0.0F);
        }

        var15 = 0;
        int var13;
        EntityPlayer var14;

        for (var13 = 0; var13 < this.pm.pList_online.size(); ++var13)
        {
            var14 = (EntityPlayer)this.pm.pList_online.get(var13);
            this.drawPlayerSlot(var14, var5 + 15, var6 + var15 + 30, var9, var10, true);
            var15 += var10 + var11;
        }

        for (var13 = 0; var13 < this.pm.pList_offline.size(); ++var13)
        {
            var14 = (EntityPlayer)this.pm.pList_offline.get(var13);
            this.drawPlayerSlot(var14, var5 + 15, var6 + var15 + 30, var9, var10, false);
            var15 += var10 + var11;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.partyNameField.drawTextBox();

        if (var15 > 103)
        {
            this.sbar.drawButton(this.mc, var1, var2);
        }

        if (this.dialogue)
        {
            this.drawGradientRect(0, 0, this.width, this.height, -1728053248, -1728053248);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.dialogueTexture);
            this.drawTexturedModalRect(var5 - 30, var6 + 71, 0, 0, 201, this.hParty - 201);
            this.dialogueInput.drawTextBox();
            this.fontRenderer.drawStringWithShadow(this.getDialogueOption() + ":", var5 - 24, var6 + 80, 16777215);
        }

        super.drawScreen(var1, var2, var3);
    }

    /**
     * Called from the main game loop to update the screen.
     */
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

        ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var2 = var1.getScaledWidth();
        int var3 = var1.getScaledHeight();
        this.xParty = var2 / 2;
        this.yParty = var3 / 2;
    }

    public void drawPlayerSlot(EntityPlayer var1, int var2, int var3, int var4, int var5, boolean var6)
    {
        this.drawGradientRect(var2, var3, var2 + var4, var3 + var5, -11184811, -10066330);
        int var7 = this.mc.renderEngine.getTextureForDownloadableImage(var1.skinUrl, "/mob/char.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var7);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float var8 = 0.125F;
        float var9 = 0.25F;
        float var10 = 0.25F;
        float var11 = 0.5F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(var8, var9);
        GL11.glVertex2f((float)(var2 + 2), (float)(var3 + 2));
        GL11.glTexCoord2f(var8, var11);
        GL11.glVertex2f((float)(var2 + 2), (float)(var3 + 18));
        GL11.glTexCoord2f(var10, var11);
        GL11.glVertex2f((float)(var2 + 18), (float)(var3 + 18));
        GL11.glTexCoord2f(var10, var9);
        GL11.glVertex2f((float)(var2 + 18), (float)(var3 + 2));
        GL11.glEnd();
        this.mc.renderEngine.resetBoundTexture();
        this.fontRenderer.drawStringWithShadow(var1.username, var2 + var5, var3 + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        this.fontRenderer.drawString(var6 ? "Online" : "Offline", (int)(((float)var2 + (float)var5) / 0.75F), (int)(((float)var3 + 12.0F) / 0.75F), var6 ? 7859831 : 15628151);
        GL11.glPopMatrix();
    }
}
