package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiParty extends GuiScreen
{
    private static final ResourceLocation TEXTURE_DIALOGUE = new ResourceLocation("aether", "textures/gui/dialogue.png");
    private static final ResourceLocation TEXTURE_PARTY = new ResourceLocation("aether", "textures/gui/party.png");
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
    private int dialogueState;
    private boolean dialogue;
    private final PartyData pm;
    private GuiTextField partyNameField;
    private GuiTextField dialogueInput;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private String name = "以太公会";
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

    public GuiParty(PartyData pm)
    {
        this.dialogueState = 4;
        this.dialogue = false;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
        pm.pList_online.add(this.mc.thePlayer);
        pm.pList_online.add(this.mc.thePlayer);
        pm.pList_offline.add(this.mc.thePlayer);
        pm.pList_offline.add(this.mc.thePlayer);
        pm.pList_offline.add(this.mc.thePlayer);
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
        this.buttonList.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 52, 20, "邀请"));
        this.buttonList.add(new GuiButton(1, this.xParty - 1, this.yParty + 85 - 28, 60, 20, "移除"));
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
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        this.partyNameField.mouseClicked(par1, par2, par3);

        if (!this.dialogue)
        {
            this.sbar.mousePressed(this.mc, par1, par2);
        }

        super.mouseClicked(par1, par2, par3);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            this.sbar.mouseReleased(par1, par2);
        }

        super.mouseMovedOrUp(par1, par2, par3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton btn)
    {
        switch (btn.id)
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
                return "邀请";
            case 6:
                return "移除";
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
        this.buttonList.add(new GuiButton(2, this.xParty - 1, this.yParty + 14, 50, 20, "确认"));
        this.buttonList.add(new GuiButton(3, this.xParty + 52, this.yParty + 14, 45, 20, "取消"));
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
    public void drawScreen(int x, int y, float partialTick)
    {
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_PARTY);
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= (float)dmsy / 1000.0F;

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
        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        boolean totalHeight = false;
        byte slotW = 100;
        byte slotH = 20;
        byte gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        int var15 = (this.pm.pList_offline.size() + this.pm.pList_online.size()) * (slotH + gutter);
        float sVal = -this.sbar.sliderValue * (float)(var15 - 105);

        if (var15 > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        var15 = 0;
        int i;
        EntityPlayer p;

        for (i = 0; i < this.pm.pList_online.size(); ++i)
        {
            p = (EntityPlayer)this.pm.pList_online.get(i);
            this.drawPlayerSlot(p, centerX + 15, centerY + var15 + 30, slotW, slotH, true);
            var15 += slotH + gutter;
        }

        for (i = 0; i < this.pm.pList_offline.size(); ++i)
        {
            p = (EntityPlayer)this.pm.pList_offline.get(i);
            this.drawPlayerSlot(p, centerX + 15, centerY + var15 + 30, slotW, slotH, false);
            var15 += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.partyNameField.drawTextBox();

        if (var15 > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        if (this.dialogue)
        {
            this.drawGradientRect(0, 0, this.width, this.height, -1728053248, -1728053248);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.func_110577_a(TEXTURE_DIALOGUE);
            this.drawTexturedModalRect(centerX - 30, centerY + 71, 0, 0, 201, this.hParty - 201);
            this.dialogueInput.drawTextBox();
            this.fontRenderer.drawStringWithShadow(this.getDialogueOption() + ":", centerX - 24, centerY + 80, 16777215);
        }

        super.drawScreen(x, y, partialTick);
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

        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = width / 2;
        this.yParty = height / 2;
    }

    public void drawPlayerSlot(EntityPlayer p, int x, int y, int width, int height, boolean online)
    {
        this.drawGradientRect(x, y, x + width, y + height, -11184811, -10066330);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(((AbstractClientPlayer)p).func_110306_p());
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float u = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex2f((float)(x + 2), (float)(y + 2));
        GL11.glTexCoord2f(u, v1);
        GL11.glVertex2f((float)(x + 2), (float)(y + 18));
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f((float)(x + 18), (float)(y + 18));
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f((float)(x + 18), (float)(y + 2));
        GL11.glEnd();
        this.fontRenderer.drawStringWithShadow(p.username, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        this.fontRenderer.drawString(online ? "在线" : "离线", (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), online ? 7859831 : 15628151);
        GL11.glPopMatrix();
    }
}
