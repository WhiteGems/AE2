package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.List;
import net.aetherteam.aether.AetherRanks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiPlayerList extends GuiScreen
{
    private static final ResourceLocation TEXTURE_DIALOGUE = new ResourceLocation("aether", "textures/gui/dialogue.png");
    private static final ResourceLocation TEXTURE_PARTY = new ResourceLocation("aether", "textures/gui/party.png");
    protected static final String DEV_TEXT = "Aether II Developer";
    protected static final String DOUBLE_TEXT = "Nice Person";
    protected static final String BETA_TEXT = "Aether II Beta Tester";
    private static final int DEV_TEXT_COLOR = 16105765;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private EntityPlayer player;
    private GuiScreen parent;

    public GuiPlayerList(EntityPlayer player, GuiScreen parent)
    {
        this.player = player;
        this.parent = parent;
        this.mc = FMLClientHandler.instance().getClient();
        this.wParty = 256;
        this.hParty = 256;
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
        List playerList = this.mc.thePlayer.sendQueue.playerInfoList;
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
        int var17 = playerList.size() * (slotH + gutter);
        float sVal = -this.sbar.sliderValue * (float)(var17 - 105);

        if (var17 > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        List list = this.mc.thePlayer.sendQueue.playerInfoList;
        var17 = 0;

        for (int i = 0; i < playerList.size(); ++i)
        {
            GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo)playerList.get(i);
            this.drawPlayerSlot(guiplayerinfo.name, centerX + 15, centerY + var17 + 30, slotW, slotH, true);
            var17 += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (var17 > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        this.drawString(this.fontRenderer, "Player List", centerX + 40, centerY + 10, 16777215);
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

    public void drawPlayerSlot(String playername, int x, int y, int width, int height, boolean online)
    {
        this.drawGradientRect(x, y, x + width, y + height, -11184811, -10066330);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(((AbstractClientPlayer)this.mc.theWorld.getPlayerEntityByName(playername)).func_110306_p());
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
        this.fontRenderer.drawStringWithShadow(playername, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);

        if (AetherRanks.getRankFromMember(playername).equals(AetherRanks.DEVELOPER))
        {
            this.fontRenderer.drawString("Aether II Developer", (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 16105765);
        }
        else if (playername.toLowerCase().equals("mr360games"))
        {
            this.fontRenderer.drawString("Nice Person", (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 16105765);
        }
        else if (playername.toLowerCase().equals("c_hase"))
        {
            this.fontRenderer.drawString("Epic Dinosaur", (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 16105765);
        }
        else if (AetherRanks.getRankFromMember(playername).equals(AetherRanks.HELPER))
        {
            this.fontRenderer.drawString("Aether II Beta Tester", (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 16105765);
        }

        GL11.glPopMatrix();
    }
}
