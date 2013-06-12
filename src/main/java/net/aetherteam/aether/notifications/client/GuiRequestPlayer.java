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
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.EntityClientPlayerMP;
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
    Minecraft mc;
    private int totalHeight;
    private ArrayList playerSlots = new ArrayList();
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
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wMember = 256;
        this.hMember = 256;
        updateScreen();
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        updateScreen();
        this.buttonList.clear();
        if (this.sbar != null) this.sbarVal = this.sbar.sliderValue;
        this.sbar = new GuiYSlider(-1, this.xMember + 46, this.yMember - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;

        this.buttonList.add(new GuiButton(0, this.xMember - 58, this.yMember + 85 - 28, 120, 20, "返回"));
        this.buttonList.add(new GuiButton(0, this.xMember - 58, this.yMember + 85 - 28, 120, 20, "返回"));
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
        if (par3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.mc, par1, par2);
            }
            for (int l = 0; l < this.playerSlots.size(); l++)
            {
                int y = (int) (par2 + this.sbar.sliderValue * (this.totalHeight - 103));

                GuiPlayerRequestSlot playerSlot = (GuiPlayerRequestSlot) this.playerSlots.get(l);

                if ((playerSlot.mousePressed(this.mc, par1, y)) && (par2 < this.yMember + 50))
                {
                    boolean pending = false;

                    if (playerSlot != null)
                    {
                        pending = NotificationHandler.instance().hasSentToBefore(playerSlot.username, NotificationType.PARTY_REQUEST, this.player.username);
                    }

                    if ((!pending) && (!PartyController.instance().inParty(playerSlot.username)))
                    {
                        playerSlot.selected = true;
                        this.slotIsSelected = true;

                        this.selectedPlayerSlot = playerSlot;
                        this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

                        for (int rr = 0; rr < this.playerSlots.size(); rr++)
                        {
                            GuiPlayerRequestSlot playerSlot2 = (GuiPlayerRequestSlot) this.playerSlots.get(rr);

                            if (playerSlot2 != playerSlot)
                            {
                                playerSlot2.selected = false;
                            }
                        }
                    } else
                    {
                        if (this.selectedPlayerSlot != null)
                        {
                            this.selectedPlayerSlot.selected = false;
                        }

                        this.selectedPlayerSlot = null;
                        this.slotIsSelected = false;
                        this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.5F);
                        return;
                    }

                    return;
                }
                playerSlot.selected = false;
                this.slotIsSelected = false;
            }

        }

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
                break;
            case 1:
                if (this.selectedPlayerSlot != null)
                {
                    Party party = PartyController.instance().getParty(this.player);
                    PartyMember member = PartyController.instance().getMember(this.player);

                    PartyController.instance().requestPlayer(party, member, this.selectedPlayerSlot.username, true);
                    NotificationHandler.instance().sendNotification(new Notification(NotificationType.PARTY_REQUEST, this.player.username, this.selectedPlayerSlot.username));
                }
                break;
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        this.buttonList.clear();

        List playerList = this.mc.thePlayer.sendQueue.playerInfoList;

        if (playerList.size() != this.playerSlots.size())
        {
            this.playerSlots.clear();
            this.slotsCreated = false;
        }

        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.backgroundTexture);
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= dmsy / 1000.0F;
        if (this.sbar.sliderValue > 1.0F) this.sbar.sliderValue = 1.0F;
        if (this.sbar.sliderValue < 0.0F) this.sbar.sliderValue = 0.0F;
        int centerX = this.xMember - 70;
        int centerY = this.yMember - 84;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hMember);
        this.totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(3089);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = (playerList.size() * (slotH + gutter));
        float sVal = -this.sbar.sliderValue * (this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        this.totalHeight = 0;

        if (!this.slotsCreated)
        {
            for (int i = 0; i < playerList.size(); i++)
            {
                GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo) playerList.get(i);

                String name = guiplayerinfo.name;

                String skinUrl = "http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(name) + ".png";

                this.playerSlots.add(new GuiPlayerRequestSlot(name, skinUrl, this.playerSlots.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH));

                this.totalHeight += slotH + gutter;
            }

            this.slotsCreated = true;
        }

        for (int i = 0; i < this.playerSlots.size(); i++)
        {
            ((GuiPlayerRequestSlot) this.playerSlots.get(i)).drawPlayerSlot(centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);

            this.totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(3089);

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        drawString(this.fontRenderer, "玩家列表", centerX + 40, centerY + 10, 16777215);

        String kickName = "请求";

        this.kickButton = new GuiButton(1, this.xMember + 3, this.yMember + 85 - 28, 58, 20, kickName);

        boolean pending = false;

        if (this.selectedPlayerSlot != null)
        {
            pending = NotificationHandler.instance().hasSentToBefore(this.selectedPlayerSlot.username, NotificationType.PARTY_REQUEST, this.player.username);
        }

        if ((this.selectedPlayerSlot != null) && (this.slotIsSelected) && ((!PartyController.instance().inParty(this.selectedPlayerSlot.username)) || (this.playerSlots.size() == 1)) && (!pending))
        {
            this.kickButton.enabled = true;
        } else this.kickButton.enabled = false;

        this.buttonList.add(new GuiButton(0, this.xMember - 60, this.yMember + 85 - 28, 58, 20, "返回"));
        this.buttonList.add(this.kickButton);

        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xMember = (width / 2);
        this.yMember = (height / 2);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.notifications.client.GuiRequestPlayer
 * JD-Core Version:    0.6.2
 */