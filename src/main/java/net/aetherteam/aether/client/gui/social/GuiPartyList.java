package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.PartyType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiPartyList extends GuiScreen
{
    private static final int LEADER_TEXT_COLOR = 26367;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    private GuiScreen parent;

    /** Reference to the Minecraft object. */
    Minecraft mc = FMLClientHandler.instance().getClient();

    public GuiPartyList(GuiScreen var1)
    {
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        this.parent = var1;
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

        if (this.sbar != null)
        {
            this.sbarVal = this.sbar.sliderValue;
        }

        this.sbar = new GuiYSlider(-1, this.xParty + 46, this.yParty - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
        this.buttonList.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 120, 20, "返回"));
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
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
        this.sbar.mousePressed(this.mc, var1, var2);
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
    public void drawScreen(int var1, int var2, float var3)
    {
        ArrayList var4 = PartyController.instance().getParties();
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int var5 = Mouse.getDWheel();
        this.sbar.sliderValue -= (float)var5 / 1000.0F;

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
        ScaledResolution var8 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        boolean var9 = false;
        byte var10 = 100;
        byte var11 = 20;
        byte var12 = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * var8.getScaleFactor(), (centerY + 35) * var8.getScaleFactor(), var10 * var8.getScaleFactor(), 103 * var8.getScaleFactor());
        GL11.glPushMatrix();
        int var17 = var4.size() * (var11 + var12);
        float var13 = -this.sbar.sliderValue * (float)(var17 - 105);

        if (var17 > 103)
        {
            GL11.glTranslatef(0.0F, var13, 0.0F);
        }

        var17 = 0;
        boolean var14 = true;

        for (int var15 = 0; var15 < var4.size(); ++var15)
        {
            if (((Party)var4.get(var15)).getType() != PartyType.PRIVATE)
            {
                var14 = false;
                this.drawPartySlot((Party)var4.get(var15), centerX + 15, centerY + var17 + 30, var10, var11, true);
                var17 += var11 + var12;
            }
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (var17 > 103)
        {
            this.sbar.drawButton(this.mc, var1, var2);
        }

        this.mc.renderEngine.resetBoundTexture();
        drawString(this.fontRenderer, "公会列表", centerX + 70 - this.fontRenderer.getStringWidth("公会列表") / 2, centerY + 10, 16777215);

        if (var4.size() == 0 || var14)
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.dialogueTexture);
            float var18 = 1.3F;
            GL11.glTranslatef((float)this.xParty - 100.0F * var18, (float)this.yParty - (float)((this.hParty - 201) / 2) * var18, 0.0F);
            GL11.glScalef(var18, var18, var18);
            this.drawTexturedModalRect(0, 0, 0, 0, 201, this.hParty - 201);
            GL11.glPopMatrix();
            this.mc.renderEngine.resetBoundTexture();

            String warningLabel = "当前无人创建公会";

            drawString(this.fontRenderer, warningLabel, centerX + 70 - this.fontRenderer.getStringWidth(warningLabel) / 2, centerY + 75, 16777215);
        }

        super.drawScreen(var1, var2, var3);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var2 = var1.getScaledWidth();
        int var3 = var1.getScaledHeight();
        this.xParty = var2 / 2;
        this.yParty = var3 / 2;
    }

    public void drawPartySlot(Party party, int x, int y, int var4, int height, boolean var6)
    {
        this.drawGradientRect(x, y, x + var4, y + height, -11184811, -10066330);
        this.mc.renderEngine.resetBoundTexture();
        this.fontRenderer.drawStringWithShadow(party.getName(), x + height - 19, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        this.fontRenderer.drawString(party.getLeader().username, (int) ((x + this.height) / 0.75F - 18.0F + party.getType().realname.length() * 6), (int) ((y + 12.0F) / 0.75F), 26367);
        this.fontRenderer.drawString(party.getType().realname, (int) ((x + this.height) / 0.75F) - 25, (int) ((y + 12.0F) / 0.75F), party.getType().getDisplayColor());
        GL11.glPopMatrix();
    }
}
