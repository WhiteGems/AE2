package net.aetherteam.aether.notifications.client;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.client.gui.social.GuiYSlider;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationHandler;
import net.aetherteam.aether.notifications.NotificationType;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
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

public class GuiRequestPlayer extends GuiScreen
{
    protected static final String ONLINE_TEXT = "在线";
    protected static final String OFFLINE_TEXT = "离线";
    private static final int ONLINE_TEXT_COLOR = 6750054;
    private static final int OFFLINE_TEXT_COLOR = 16711680;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int backgroundTexture;
    private int dialogueTexture;
    private int xMember;
    private int yMember;
    private int wMember;
    private int hMember;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private int totalHeight;
    private ArrayList playerSlots = new ArrayList();
    private GuiPlayerRequestSlot selectedPlayerSlot;
    private boolean slotsCreated = false;
    private boolean slotIsSelected = false;
    private GuiScreen parent;
    private EntityPlayer player;
    private GuiButton kickButton;

    public GuiRequestPlayer(EntityPlayer var1, GuiScreen var2)
    {
        this.player = var1;
        this.parent = var2;
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wMember = 256;
        this.hMember = 256;
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

        this.sbar = new GuiYSlider(-1, this.xMember + 46, this.yMember - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;

        this.buttonList.add(new GuiButton(0, this.xMember - 58, this.yMember + 85 - 28, 120, 20, "返回"));
        this.buttonList.add(new GuiButton(0, this.xMember - 58, this.yMember + 85 - 28, 120, 20, "返回"));
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
        if (var3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.mc, var1, var2);
            }

            for (int var4 = 0; var4 < this.playerSlots.size(); ++var4)
            {
                int var5 = (int)((float)var2 + this.sbar.sliderValue * (float)(this.totalHeight - 103));
                GuiPlayerRequestSlot var6 = (GuiPlayerRequestSlot)this.playerSlots.get(var4);

                if (var6.mousePressed(this.mc, var1, var5) && var2 < this.yMember + 50)
                {
                    boolean var7 = false;

                    if (var6 != null)
                    {
                        var7 = NotificationHandler.instance().hasSentToBefore(var6.username, NotificationType.PARTY_REQUEST, this.player.username);
                    }

                    if (!var7 && !PartyController.instance().inParty(var6.username))
                    {
                        var6.selected = true;
                        this.slotIsSelected = true;
                        this.selectedPlayerSlot = var6;
                        this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

                        for (int var8 = 0; var8 < this.playerSlots.size(); ++var8)
                        {
                            GuiPlayerRequestSlot var9 = (GuiPlayerRequestSlot)this.playerSlots.get(var8);

                            if (var9 != var6)
                            {
                                var9.selected = false;
                            }
                        }

                        return;
                    }

                    if (this.selectedPlayerSlot != null)
                    {
                        this.selectedPlayerSlot.selected = false;
                    }

                    this.selectedPlayerSlot = null;
                    this.slotIsSelected = false;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.5F);
                    return;
                }

                var6.selected = false;
                this.slotIsSelected = false;
            }
        }

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
                break;

            case 1:
                if (this.selectedPlayerSlot != null)
                {
                    Party var2 = PartyController.instance().getParty(this.player);
                    PartyMember var3 = PartyController.instance().getMember(this.player);
                    PartyController.instance().requestPlayer(var2, var3, this.selectedPlayerSlot.username, true);
                    NotificationHandler.instance().sendNotification(new Notification(NotificationType.PARTY_REQUEST, this.player.username, this.selectedPlayerSlot.username));
                }
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
        this.buttonList.clear();
        List var4 = this.mc.thePlayer.sendQueue.playerInfoList;

        if (var4.size() != this.playerSlots.size())
        {
            this.playerSlots.clear();
            this.slotsCreated = false;
        }

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

        int centerX = this.xMember - 70;
        int centerY = this.yMember - 84;
        ScaledResolution var8 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hMember);
        this.totalHeight = 0;
        byte var9 = 100;
        byte var10 = 20;
        byte var11 = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * var8.getScaleFactor(), (centerY + 35) * var8.getScaleFactor(), var9 * var8.getScaleFactor(), 103 * var8.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = var4.size() * (var10 + var11);
        float var12 = -this.sbar.sliderValue * (float)(this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, var12, 0.0F);
        }

        this.totalHeight = 0;
        int var13;

        if (!this.slotsCreated)
        {
            for (var13 = 0; var13 < var4.size(); ++var13)
            {
                GuiPlayerInfo var14 = (GuiPlayerInfo)var4.get(var13);
                String var15 = var14.name;
                String var16 = "http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(var15) + ".png";
                this.playerSlots.add(new GuiPlayerRequestSlot(var15, var16, this.playerSlots.size(), centerX + 15, centerY + this.totalHeight + 30, var9, var10));
                this.totalHeight += var10 + var11;
            }

            this.slotsCreated = true;
        }

        for (var13 = 0; var13 < this.playerSlots.size(); ++var13)
        {
            ((GuiPlayerRequestSlot)this.playerSlots.get(var13)).drawPlayerSlot(centerX + 15, centerY + this.totalHeight + 30, var9, var10);
            this.totalHeight += var10 + var11;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, var1, var2);
        }

        drawString(this.fontRenderer, "玩家列表", centerX + 40, centerY + 10, 16777215);

        String kickName = "请求";

        this.kickButton = new GuiButton(1, this.xMember + 3, this.yMember + 85 - 28, 58, 20, kickName);

        boolean pending = false;

        if (this.selectedPlayerSlot != null)
        {
            pending = NotificationHandler.instance().hasSentToBefore(this.selectedPlayerSlot.username, NotificationType.PARTY_REQUEST, this.player.username);
        }

        if (this.selectedPlayerSlot != null && this.slotIsSelected && (!PartyController.instance().inParty(this.selectedPlayerSlot.username) || this.playerSlots.size() == 1) && !pending)
        {
            this.kickButton.enabled = true;
        }
        else
        {
            this.kickButton.enabled = false;
        }

        this.buttonList.add(new GuiButton(0, this.xMember - 60, this.yMember + 85 - 28, 58, 20, "返回"));
        this.buttonList.add(this.kickButton);
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
        this.xMember = var2 / 2;
        this.yMember = var3 / 2;
    }
}
