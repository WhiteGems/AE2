package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import net.aetherteam.aether.client.gui.social.dialogue.GuiDialogueBox;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditPartyName extends GuiScreen
{
    private GuiTextField dialogueInput;
    private String name;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private GuiScreen parent;
    private EntityPlayer player;

    public GuiEditPartyName(EntityPlayer var1, GuiScreen var2)
    {
        this(new PartyData(), var1, var2);
    }

    public GuiEditPartyName(PartyData var1, EntityPlayer var2, GuiScreen var3)
    {
        this.name = "Aether Party";
        this.parent = var3;
        this.player = var2;
        this.mc = FMLClientHandler.instance().getClient();
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.updateScreen();
        this.buttonList.clear();
        this.dialogueInput = new GuiTextField(this.fontRenderer, this.xParty - 88 + this.fontRenderer.getStringWidth(this.name), this.yParty - 7, 193 - this.fontRenderer.getStringWidth(this.name) - 10, 16);
        this.dialogueInput.setFocused(true);
        this.dialogueInput.setMaxStringLength(22);
        this.dialogueInput.setCanLoseFocus(false);
        this.buttonList.add(new GuiButton(0, this.xParty - 1, this.yParty + 14, 50, 20, "Confirm"));
        this.buttonList.add(new GuiButton(1, this.xParty + 52, this.yParty + 14, 45, 20, "Cancel"));
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        if (this.dialogueInput.isFocused())
        {
            this.dialogueInput.textboxKeyTyped(var1, var2);
        }
        else if (var2 == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }

        super.keyTyped(var1, var2);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        this.dialogueInput.mouseClicked(var1, var2, var3);
        super.mouseClicked(var1, var2, var3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        switch (var1.id)
        {
            case 0:
                Party var2 = PartyController.instance().getParty(this.player);
                boolean var3 = PartyController.instance().isLeader(this.player);

                if (var3 && var2 != null)
                {
                    String var4 = var2.getName();
                    boolean var5 = PartyController.instance().changePartyName(var2, this.dialogueInput.getText(), true);
                    this.mc.displayGuiScreen(new GuiDialogueBox(this.parent, "Party name was changed to \'" + this.dialogueInput.getText() + "\'!", "That party name is already taken. Sorry :(", var5));
                }

                break;

            case 1:
                this.mc.displayGuiScreen(this.parent);
        }
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
        int var4 = this.xParty - 70;
        int var5 = this.yParty - 84;
        ScaledResolution var6 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        boolean var7 = false;
        byte var8 = 100;
        boolean var9 = true;
        boolean var10 = true;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((var4 + 14) * var6.getScaleFactor(), (var5 + 35) * var6.getScaleFactor(), var8 * var6.getScaleFactor(), 103 * var6.getScaleFactor());
        GL11.glPushMatrix();
        var7 = false;
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.drawGradientRect(0, 0, this.width, this.height, -1728053248, -1728053248);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.dialogueTexture);
        this.drawTexturedModalRect(var4 - 30, var5 + 71, 0, 0, 201, this.hParty - 201);
        this.dialogueInput.drawTextBox();
        this.fontRenderer.drawString("Change Party Name", (int)(((float)var4 + (float)this.height) / 0.75F), (int)(((float)var5 + 12.0F) / 0.75F), -10066330);
        super.drawScreen(var1, var2, var3);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

        if (this.dialogueInput != null)
        {
            this.dialogueInput.updateCursorCounter();
        }

        ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var2 = var1.getScaledWidth();
        int var3 = var1.getScaledHeight();
        this.xParty = var2 / 2;
        this.yParty = var3 / 2;
    }
}
