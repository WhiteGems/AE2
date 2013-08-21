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
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiPartyList extends GuiScreen
{
    private static final ResourceLocation TEXTURE_DIALOGUE = new ResourceLocation("aether", "textures/gui/dialogue.png");
    private static final ResourceLocation TEXTURE_PARTY = new ResourceLocation("aether", "textures/gui/party.png");
    private static final int LEADER_TEXT_COLOR = 26367;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int xParty;
    private int yParty;
    private int wParty = 256;
    private int hParty = 256;
    private GuiScreen parent;

    /** Reference to the Minecraft object. */
    Minecraft mc = FMLClientHandler.instance().getClient();

    public GuiPartyList(GuiScreen parent)
    {
        this.parent = parent;
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
        this.buttonList.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 120, 20, "Back"));
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
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        this.sbar.mousePressed(this.mc, par1, par2);
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
        ArrayList partyList = PartyController.instance().getParties();
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
        int var17 = partyList.size() * (slotH + gutter);
        float sVal = -this.sbar.sliderValue * (float)(var17 - 105);

        if (var17 > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        var17 = 0;
        boolean showNoParties = true;

        for (int scaleFactor = 0; scaleFactor < partyList.size(); ++scaleFactor)
        {
            if (((Party)partyList.get(scaleFactor)).getType() != PartyType.PRIVATE)
            {
                showNoParties = false;
                this.drawPartySlot((Party)partyList.get(scaleFactor), centerX + 15, centerY + var17 + 30, slotW, slotH, true);
                var17 += slotH + gutter;
            }
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (var17 > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        this.drawString(this.fontRenderer, "Party List", centerX + 70 - this.fontRenderer.getStringWidth("Party List") / 2, centerY + 10, 16777215);

        if (partyList.size() == 0 || showNoParties)
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.func_110577_a(TEXTURE_DIALOGUE);
            float var18 = 1.3F;
            GL11.glTranslatef((float)this.xParty - 100.0F * var18, (float)this.yParty - (float)((this.hParty - 201) / 2) * var18, 0.0F);
            GL11.glScalef(var18, var18, var18);
            this.drawTexturedModalRect(0, 0, 0, 0, 201, this.hParty - 201);
            GL11.glPopMatrix();
            String warningLabel = "There are no parties to display at this time.";
            this.drawString(this.fontRenderer, warningLabel, centerX + 70 - this.fontRenderer.getStringWidth(warningLabel) / 2, centerY + 75, 16777215);
        }

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

    public void drawPartySlot(Party party, int x, int y, int width, int height, boolean online)
    {
        this.drawGradientRect(x, y, x + width, y + height, -11184811, -10066330);
        this.fontRenderer.drawStringWithShadow(party.getName(), x + height - 19, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        this.fontRenderer.drawString(party.getLeader().username, (int)(((float)x + (float)height) / 0.75F - 22.0F + (float)(party.getType().name().length() * 6)), (int)(((float)y + 12.0F) / 0.75F), 26367);
        this.fontRenderer.drawString(party.getType().name(), (int)(((float)x + (float)height) / 0.75F) - 25, (int)(((float)y + 12.0F) / 0.75F), party.getType().getDisplayColor());
        GL11.glPopMatrix();
    }
}
