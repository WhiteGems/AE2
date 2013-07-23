package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.List;
import net.aetherteam.aether.AetherRanks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiPlayerList extends GuiScreen
{
    protected static final String DEV_TEXT = "以太II 开发人员";
    protected static final String DOUBLE_TEXT = "臭熏熏的人 :3";
    protected static final String BETA_TEXT = "以太IIBeta 测试人员";
    private static final int DEV_TEXT_COLOR = 16105765;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private EntityPlayer player;
    private GuiScreen parent;

    public GuiPlayerList(EntityPlayer var1, GuiScreen var2)
    {
        this.player = var1;
        this.parent = var2;
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
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
    public void drawScreen(int x, int y, float partialTick)
    {
        List var4 = this.mc.thePlayer.sendQueue.playerInfoList;
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

        List var14 = this.mc.thePlayer.sendQueue.playerInfoList;
        var17 = 0;

        for (int var15 = 0; var15 < var4.size(); ++var15)
        {
            GuiPlayerInfo var16 = (GuiPlayerInfo)var4.get(var15);
            this.drawPlayerSlot(var16.name, centerX + 15, centerY + var17 + 30, var10, var11, true);
            var17 += var11 + var12;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (var17 > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        drawString(this.fontRenderer, "玩家列表", centerX + 40, centerY + 10, 16777215);
        super.drawScreen(x, y, partialTick);
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

    public void drawPlayerSlot(String var1, int x, int y, int var4, int height, boolean var6)
    {
        this.drawGradientRect(x, y, x + var4, y + height, -11184811, -10066330);
        int var7 = this.mc.renderEngine.getTextureForDownloadableImage("http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(var1) + ".png", "/mob/char.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var7);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float var8 = 0.125F;
        float var9 = 0.25F;
        float var10 = 0.25F;
        float var11 = 0.5F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(var8, var9);
        GL11.glVertex2f((float)(x + 2), (float)(y + 2));
        GL11.glTexCoord2f(var8, var11);
        GL11.glVertex2f((float)(x + 2), (float)(y + 18));
        GL11.glTexCoord2f(var10, var11);
        GL11.glVertex2f((float)(x + 18), (float)(y + 18));
        GL11.glTexCoord2f(var10, var9);
        GL11.glVertex2f((float)(x + 18), (float)(y + 2));
        GL11.glEnd();
        this.mc.renderEngine.resetBoundTexture();
        this.fontRenderer.drawStringWithShadow(var1, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);

        if (AetherRanks.getRankFromMember(var1).equals(AetherRanks.DEVELOPER))
        {
            this.fontRenderer.drawString(DEV_TEXT, (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 16105765);
        }
        else if (var1.toLowerCase().equals("mr360games"))
        {
            this.fontRenderer.drawString(DOUBLE_TEXT, (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 16105765);
        }
        else if (var1.toLowerCase().equals("c_hase"))
        {
            this.fontRenderer.drawString("史诗恐龙", (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 16105765);
        }
        else if (AetherRanks.getRankFromMember(var1).equals(AetherRanks.HELPER))
        {
            this.fontRenderer.drawString(BETA_TEXT, (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 16105765);
        }

        GL11.glPopMatrix();
    }
}