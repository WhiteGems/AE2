package net.aetherteam.aether.client.gui.social.dialogue;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDialogueBox extends GuiScreen
{
    private static final ResourceLocation TEXTURE_DIALOGUE = new ResourceLocation("aether", "textures/gui/dialogue.png");
    private static final ResourceLocation TEXTURE_PARTY = new ResourceLocation("aether", "textures/gui/party.png");
    private static final int LEADER_TEXT_COLOR = 26367;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    private GuiScreen parent;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private boolean created;
    private String createdText;
    private String failureText;

    public GuiDialogueBox(GuiScreen parent, String createdText, String failureText, boolean created)
    {
        this.createdText = createdText;
        this.failureText = failureText;
        this.created = created;
        this.mc = FMLClientHandler.instance().getClient();
        this.wParty = 256;
        this.hParty = 256;
        this.parent = parent;
        this.updateScreen();
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.updateScreen();
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.xParty - 58, this.yParty, 120, 20, "Okay"));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton btn)
    {
        switch (btn.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);

            default:
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
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_DIALOGUE);
        float scaleFactor = 1.3F;
        GL11.glTranslatef((float)this.xParty - 100.0F * scaleFactor, (float)this.yParty - (float)((this.hParty - 201) / 2) * scaleFactor, 0.0F);
        GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
        this.drawTexturedModalRect(0, 0, 0, 0, 201, this.hParty - 201);
        GL11.glPopMatrix();
        String warningLabel = this.created ? this.createdText : this.failureText;
        this.drawString(this.fontRenderer, warningLabel, centerX + 70 - this.fontRenderer.getStringWidth(warningLabel) / 2, centerY + 65, 16777215);
        super.drawScreen(x, y, partialTick);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = width / 2;
        this.yParty = height / 2;
    }
}
