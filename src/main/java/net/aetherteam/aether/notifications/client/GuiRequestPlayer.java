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
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiRequestPlayer extends GuiScreen
{
    private static final ResourceLocation TEXTURE_DIALOGUE = new ResourceLocation("aether", "textures/gui/dialogue.png");
    private static final ResourceLocation TEXTURE_PARTY = new ResourceLocation("aether", "textures/gui/party.png");
    protected static final String ONLINE_TEXT = "ONLINE";
    protected static final String OFFLINE_TEXT = "OFFLINE";
    private static final int ONLINE_TEXT_COLOR = 6750054;
    private static final int OFFLINE_TEXT_COLOR = 16711680;
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int xMember;
    private int yMember;
    private int wMember;
    private int hMember;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private int totalHeight;
    private ArrayList<GuiPlayerRequestSlot> playerSlots = new ArrayList();
    private GuiPlayerRequestSlot selectedPlayerSlot;
    private boolean slotsCreated = false;
    private boolean slotIsSelected = false;
    private GuiScreen parent;
    private EntityPlayer player;
    private GuiButton kickButton;

    public GuiRequestPlayer(EntityPlayer player, GuiScreen parent)
    {
        this.player = player;
        this.parent = parent;
        this.mc = FMLClientHandler.instance().getClient();
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
        this.buttonList.add(new GuiButton(0, this.xMember - 58, this.yMember + 85 - 28, 120, 20, "Back"));
        this.buttonList.add(new GuiButton(0, this.xMember - 58, this.yMember + 85 - 28, 120, 20, "Back"));
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
        if (par3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.mc, par1, par2);
            }

            for (int l = 0; l < this.playerSlots.size(); ++l)
            {
                int y = (int)((float)par2 + this.sbar.sliderValue * (float)(this.totalHeight - 103));
                GuiPlayerRequestSlot playerSlot = (GuiPlayerRequestSlot)this.playerSlots.get(l);

                if (playerSlot.mousePressed(this.mc, par1, y) && par2 < this.yMember + 50)
                {
                    boolean pending = false;

                    if (playerSlot != null)
                    {
                        pending = NotificationHandler.instance().hasSentToBefore(playerSlot.entityPlayer.username, NotificationType.PARTY_REQUEST, this.player.username);
                    }

                    if (!pending && !PartyController.instance().inParty(playerSlot.entityPlayer.username))
                    {
                        playerSlot.selected = true;
                        this.slotIsSelected = true;
                        this.selectedPlayerSlot = playerSlot;
                        this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

                        for (int rr = 0; rr < this.playerSlots.size(); ++rr)
                        {
                            GuiPlayerRequestSlot playerSlot2 = (GuiPlayerRequestSlot)this.playerSlots.get(rr);

                            if (playerSlot2 != playerSlot)
                            {
                                playerSlot2.selected = false;
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

                playerSlot.selected = false;
                this.slotIsSelected = false;
            }
        }

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
                break;

            case 1:
                if (this.selectedPlayerSlot != null)
                {
                    Party party = PartyController.instance().getParty(this.player);
                    PartyMember member = PartyController.instance().getMember(this.player);
                    PartyController.instance().requestPlayer(party, member, this.selectedPlayerSlot.entityPlayer.username, true);
                    NotificationHandler.instance().sendNotification(new Notification(NotificationType.PARTY_REQUEST, this.player.username, this.selectedPlayerSlot.entityPlayer.username));
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
    public void drawScreen(int x, int y, float partialTick)
    {
        this.buttonList.clear();
        List playerList = this.mc.thePlayer.sendQueue.playerInfoList;

        if (playerList.size() != this.playerSlots.size())
        {
            this.playerSlots.clear();
            this.slotsCreated = false;
        }

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

        int centerX = this.xMember - 70;
        int centerY = this.yMember - 84;
        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hMember);
        this.totalHeight = 0;
        byte slotW = 100;
        byte slotH = 20;
        byte gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = playerList.size() * (slotH + gutter);
        float sVal = -this.sbar.sliderValue * (float)(this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        this.totalHeight = 0;
        int kickName;

        if (!this.slotsCreated)
        {
            for (kickName = 0; kickName < playerList.size(); ++kickName)
            {
                GuiPlayerInfo pending = (GuiPlayerInfo)playerList.get(kickName);
                String name = pending.name;
                this.playerSlots.add(new GuiPlayerRequestSlot(this.mc.theWorld.getPlayerEntityByName(name), this.playerSlots.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH));
                this.totalHeight += slotH + gutter;
            }

            this.slotsCreated = true;
        }

        for (kickName = 0; kickName < this.playerSlots.size(); ++kickName)
        {
            ((GuiPlayerRequestSlot)this.playerSlots.get(kickName)).drawPlayerSlot(centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);
            this.totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        this.drawString(this.fontRenderer, "Player List", centerX + 40, centerY + 10, 16777215);
        String var16 = "Request";
        this.kickButton = new GuiButton(1, this.xMember + 3, this.yMember + 85 - 28, 58, 20, var16);
        boolean var17 = false;

        if (this.selectedPlayerSlot != null)
        {
            var17 = NotificationHandler.instance().hasSentToBefore(this.selectedPlayerSlot.entityPlayer.username, NotificationType.PARTY_REQUEST, this.player.username);
        }

        if (this.selectedPlayerSlot != null && this.slotIsSelected && (!PartyController.instance().inParty(this.selectedPlayerSlot.entityPlayer.username) || this.playerSlots.size() == 1) && !var17)
        {
            this.kickButton.enabled = true;
        }
        else
        {
            this.kickButton.enabled = false;
        }

        this.buttonList.add(new GuiButton(0, this.xMember - 60, this.yMember + 85 - 28, 58, 20, "Back"));
        this.buttonList.add(this.kickButton);
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
        this.xMember = width / 2;
        this.yMember = height / 2;
    }
}
