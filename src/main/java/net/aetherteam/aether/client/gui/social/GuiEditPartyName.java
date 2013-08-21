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
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditPartyName extends GuiScreen
{
    private static final ResourceLocation TEXTURE_DIALOGUE = new ResourceLocation("aether", "textures/gui/dialogue.png");
    private GuiTextField dialogueInput;
    private String name;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private GuiScreen parent;
    private EntityPlayer player;

    public GuiEditPartyName(EntityPlayer player, GuiScreen parent)
    {
        this(new PartyData(), player, parent);
    }

    public GuiEditPartyName(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.name = "Aether Party";
        this.parent = parent;
        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
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
    protected void keyTyped(char charTyped, int keyTyped)
    {
        if (this.dialogueInput.isFocused())
        {
            this.dialogueInput.textboxKeyTyped(charTyped, keyTyped);
        }
        else if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }

        super.keyTyped(charTyped, keyTyped);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        this.dialogueInput.mouseClicked(par1, par2, par3);
        super.mouseClicked(par1, par2, par3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                Party party = PartyController.instance().getParty(this.player);
                boolean isLeader = PartyController.instance().isLeader(this.player);

                if (isLeader && party != null)
                {
                    String partyName = party.getName();
                    boolean nameChanged = PartyController.instance().changePartyName(party, this.dialogueInput.getText(), true);
                    this.mc.displayGuiScreen(new GuiDialogueBox(this.parent, "Party name was changed to \'" + this.dialogueInput.getText() + "\'!", "That party name is already taken. Sorry :(", nameChanged));
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
    public void drawScreen(int x, int y, float partialTick)
    {
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        boolean totalHeight = false;
        byte slotW = 100;
        boolean slotH = true;
        boolean gutter = true;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        totalHeight = false;
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.drawGradientRect(0, 0, this.width, this.height, -1728053248, -1728053248);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_DIALOGUE);
        this.drawTexturedModalRect(centerX - 30, centerY + 71, 0, 0, 201, this.hParty - 201);
        this.dialogueInput.drawTextBox();
        this.fontRenderer.drawString("Change Party Name", (int)(((float)centerX + (float)this.height) / 0.75F), (int)(((float)centerY + 12.0F) / 0.75F), -10066330);
        super.drawScreen(x, y, partialTick);
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

        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = width / 2;
        this.yParty = height / 2;
    }
}
