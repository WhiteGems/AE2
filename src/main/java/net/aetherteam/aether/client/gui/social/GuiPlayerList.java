package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.List;
import net.aetherteam.aether.AetherRanks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.multiplayer.CallableMPL2;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiPlayerList extends GuiScreen
{
    protected static final String DEV_TEXT = "Aether II Developer";
    protected static final String DOUBLE_TEXT = "Smelly Person :3";
    protected static final String BETA_TEXT = "Aether II Beta Tester";
    private static final int DEV_TEXT_COLOR = 16105765;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft g;
    private EntityPlayer player;
    private GuiScreen parent;

    public GuiPlayerList(EntityPlayer player, GuiScreen parent)
    {
        this.player = player;
        this.parent = parent;
        this.g = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.g.displayGuiScreen((GuiScreen)null);
            this.g.setIngameFocus();
        }
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        updateScreen();
        this.k.clear();

        if (this.sbar != null)
        {
            this.sbarVal = this.sbar.sliderValue;
        }

        this.sbar = new GuiYSlider(-1, this.xParty + 46, this.yParty - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
        this.k.add(new GuiButton(0, this.xParty - 58, this.yParty + 85 - 28, 120, 20, "Back"));
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        this.sbar.mousePressed(this.g, par1, par2);
        super.mouseClicked(par1, par2, par3);
    }

    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            this.sbar.mouseReleased(par1, par2);
        }

        super.mouseMovedOrUp(par1, par2, par3);
    }

    protected void actionPerformed(GuiButton btn)
    {
        switch (btn.id)
        {
            case 0:
                this.g.displayGuiScreen(this.parent);
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        List playerList = this.g.thePlayer.theWorldClient.c;
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= dmsy / 1000.0F;

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
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        int totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        totalHeight = playerList.size() * (slotH + gutter);
        float sVal = -this.sbar.sliderValue * (totalHeight - 105);

        if (totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        List list = this.g.thePlayer.theWorldClient.c;
        totalHeight = 0;

        for (int i = 0; i < playerList.size(); i++)
        {
            EntityClientPlayerMP guiplayerinfo = (EntityClientPlayerMP)playerList.get(i);
            drawPlayerSlot(guiplayerinfo.sendQueue, centerX + 15, centerY + totalHeight + 30, slotW, slotH, true);
            totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (totalHeight > 103)
        {
            this.sbar.drawButton(this.g, x, y);
        }

        drawString(this.m, "Player List", centerX + 40, centerY + 10, 16777215);
        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }

    public void drawPlayerSlot(String playername, int x, int y, int width, int height, boolean online)
    {
        drawGradientRect(x, y, x + width, y + height, -11184811, -10066330);
        int icon = this.g.renderEngine.a("http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(playername) + ".png", "/mob/char.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, icon);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float u = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex2f(x + 2, y + 2);
        GL11.glTexCoord2f(u, v1);
        GL11.glVertex2f(x + 2, y + 18);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x + 18, y + 18);
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f(x + 18, y + 2);
        GL11.glEnd();
        this.g.renderEngine.a();
        this.m.drawStringWithShadow(playername, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);

        if (AetherRanks.getRankFromMember(playername).equals(AetherRanks.DEVELOPER))
        {
            this.m.drawString("Aether II Developer", (int)((x + height) / 0.75F), (int)((y + 12.0F) / 0.75F), 16105765);
        }
        else if (playername.toLowerCase().equals("mr360games"))
        {
            this.m.drawString("Smelly Person :3", (int)((x + height) / 0.75F), (int)((y + 12.0F) / 0.75F), 16105765);
        }
        else if (playername.toLowerCase().equals("c_hase"))
        {
            this.m.drawString("Epic Dinosaur", (int)((x + height) / 0.75F), (int)((y + 12.0F) / 0.75F), 16105765);
        }
        else if (AetherRanks.getRankFromMember(playername).equals(AetherRanks.HELPER))
        {
            this.m.drawString("Aether II Beta Tester", (int)((x + height) / 0.75F), (int)((y + 12.0F) / 0.75F), 16105765);
        }

        GL11.glPopMatrix();
    }
}

