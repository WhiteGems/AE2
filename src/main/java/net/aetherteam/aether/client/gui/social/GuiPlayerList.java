package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiPlayerList extends GuiScreen
{
    protected static final String DEV_TEXT = "以太II开发人员";
    protected static final String DOUBLE_TEXT = "臭熏熏的人 :3";
    protected static final String BETA_TEXT = "以太IIBeta测试人员";
    private static final int DEV_TEXT_COLOR = 16105765;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft f;
    private EntityPlayer player;
    private GuiScreen parent;

    public GuiPlayerList(EntityPlayer player, GuiScreen parent)
    {
        this.player = player;
        this.parent = parent;
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        }
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        updateScreen();
        this.buttonList.clear();
        if (this.sbar != null) this.sbarVal = this.sbar.sliderValue;
        this.sbar = new GuiYSlider(-1, this.xParty + 46, this.yParty - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
        this.buttonList.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 120, 20, "返回"));
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        this.sbar.mousePressed(this.mc, par1, par2);
        super.mouseClicked(par1, par2, par3);
    }

    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (par3 == 0) this.sbar.mouseReleased(par1, par2);
        super.mouseMovedOrUp(par1, par2, par3);
    }

    protected void actionPerformed(GuiButton btn)
    {
        switch (btn.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        List playerList = this.mc.thePlayer.sendQueue.playerInfoList;
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.backgroundTexture);
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= dmsy / 1000.0F;
        if (this.sbar.sliderValue > 1.0F) this.sbar.sliderValue = 1.0F;
        if (this.sbar.sliderValue < 0.0F) this.sbar.sliderValue = 0.0F;
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        int totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(3089);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        totalHeight = playerList.size() * (slotH + gutter);
        float sVal = -this.sbar.sliderValue * (totalHeight - 105);

        if (totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        List list = this.mc.thePlayer.sendQueue.playerInfoList;
        totalHeight = 0;

        for (int i = 0; i < playerList.size(); i++)
        {
            GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo) playerList.get(i);
            drawPlayerSlot(guiplayerinfo.name, centerX + 15, centerY + totalHeight + 30, slotW, slotH, true);
            totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(3089);

        if (totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        drawString(this.fontRenderer, "玩家列表", centerX + 40, centerY + 10, 16777215);
        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }

    public void drawPlayerSlot(String playername, int x, int y, int width, int height, boolean online)
    {
        drawGradientRect(x, y, x + width, y + height, -11184811, -10066330);

        int icon = this.mc.renderEngine.getTextureForDownloadableImage("http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(playername) + ".png", "/mob/char.png");

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, icon);
        GL11.glEnable(3553);

        float u = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
        GL11.glBegin(7);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex2f(x + 2, y + 2);
        GL11.glTexCoord2f(u, v1);
        GL11.glVertex2f(x + 2, y + 18);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x + 18, y + 18);
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f(x + 18, y + 2);
        GL11.glEnd();

        this.mc.renderEngine.resetBoundTexture();

        this.fontRenderer.drawStringWithShadow(playername, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        if ((playername.toLowerCase().equals("ijaryt23")) || (playername.toLowerCase().equals("kingbdogz")) || (playername.toLowerCase().equals("saspiron")))
            this.fontRenderer.drawString("以太II 开发人员", (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 16105765);
        else if (playername.toLowerCase().equals("mr360games"))
            this.fontRenderer.drawString("臭熏熏的人 :3", (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 16105765);
        else if (playername.toLowerCase().equals("some beta tester <3"))
            this.fontRenderer.drawString("臭熏熏的人 :3", (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 16105765);
        else if (playername.toLowerCase().equals("zestybaby"))
        	this.fontRenderer.drawString("白宝石小组组长sama!", (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 16105765);
        else if ((playername.toLowerCase().equals("crafteverywhere")) || (playername.toLowerCase().equals("pa001024")) || (playername.toLowerCase().equals("sun")) || (playername.toLowerCase().equals("waidely")))
        	this.fontRenderer.drawString("汉化人员", (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 16105765);	
        GL11.glPopMatrix();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiPlayerList
 * JD-Core Version:    0.6.2
 */
