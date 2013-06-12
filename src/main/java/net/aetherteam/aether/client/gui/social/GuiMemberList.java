package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.List;

import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiMemberList extends GuiScreen
{
    protected static final String ONLINE_TEXT = "在线";
    protected static final String OFFLINE_TEXT = "离线";
    private static final int ONLINE_TEXT_COLOR = 6750054;
    private static final int OFFLINE_TEXT_COLOR = 16711680;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft f;
    private GuiScreen parent;
    private EntityPlayer player;

    public GuiMemberList(EntityPlayer player, GuiScreen parent)
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

    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        }
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
        if (PartyController.instance().getParty(this.player) == null)
        {
            this.mc.displayGuiScreen((GuiScreen) null);
        }

        List playerList = new ArrayList();

        if (PartyController.instance().getParty(this.player) != null)
        {
            playerList = PartyController.instance().getParty(this.player).getMembers();
        }

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

        totalHeight = 0;
        for (int i = 0; i < playerList.size(); i++)
        {
            String name = ((PartyMember) playerList.get(i)).username;
            drawPlayerSlot(name, centerX + 15, centerY + totalHeight + 30, slotW, slotH, true);
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
        ArrayList partyList = PartyController.instance().getParties();
        ArrayList playerStringList = new ArrayList();

        for (int i = 0; i < partyList.size(); i++)
        {
            PartyMember member = PartyController.instance().getMember(playername);

            if (((Party) partyList.get(i)).hasMember(member))
            {
                playerStringList.add(playername);
            }
        }
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
        if (playerStringList.contains(playername))
            this.fontRenderer.drawString("在线", (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 6750054);
        else
            this.fontRenderer.drawString("离线", (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 16711680);
        GL11.glPopMatrix();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiMemberList
 * JD-Core Version:    0.6.2
 */
