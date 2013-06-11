package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.List;

import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiManageMembers extends GuiScreen
{
    protected static final String ONLINE_TEXT = "ONLINE";
    protected static final String OFFLINE_TEXT = "OFFLINE";
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
    Minecraft f;
    private int totalHeight;
    private ArrayList playerSlots = new ArrayList();
    private GuiPlayerSlot selectedPlayerSlot;
    private boolean slotsCreated = false;
    private boolean slotIsSelected = false;
    private GuiScreen parent;
    private EntityPlayer player;
    private GuiButton kickButton;

    public GuiManageMembers(EntityPlayer player, GuiScreen parent)
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
        this.sbar = new GuiYSlider(-1, this.xMember + 46, this.yMember - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;

        this.buttonList.add(new GuiButton(0, this.xMember - 58, this.yMember + 85 - 28, 120, 20, "Back"));
        this.buttonList.add(new GuiButton(0, this.xMember - 58, this.yMember + 85 - 28, 120, 20, "Back"));
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

                GuiPlayerSlot playerSlot = (GuiPlayerSlot) this.playerSlots.get(l);

                if ((playerSlot.mousePressed(this.mc, par1, y)) && (par2 < this.yMember + 50))
                {
                    playerSlot.selected = true;
                    this.slotIsSelected = true;

                    this.selectedPlayerSlot = playerSlot;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

                    for (int rr = 0; rr < this.playerSlots.size(); rr++)
                    {
                        GuiPlayerSlot playerSlot2 = (GuiPlayerSlot) this.playerSlots.get(rr);

                        if (playerSlot2 != playerSlot)
                        {
                            playerSlot2.selected = false;
                        }
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
                Party party = PartyController.instance().getParty(this.player);
                PartyMember potentialLeader = PartyController.instance().getMember(this.player);
                boolean isLeader = party.isLeader(potentialLeader);

                if ((isLeader) && (this.playerSlots.size() == 1))
                {
                    PartyController.instance().removeParty(party, true);

                    this.mc.displayGuiScreen((GuiScreen) null);
                } else
                {
                    this.mc.displayGuiScreen(new GuiManagePartyMember(this.player, this.selectedPlayerSlot.username, this.selectedPlayerSlot.skinURL, this));
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

        Party party = PartyController.instance().getParty(this.player);

        List playerList = new ArrayList();

        if (party != null)
        {
            playerList = PartyController.instance().getParty(this.player).getMembers();
        }

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
                String name = ((PartyMember) playerList.get(i)).username;
                String skinUrl = ((PartyMember) playerList.get(i)).skinUrl;

                this.playerSlots.add(new GuiPlayerSlot(name, skinUrl, this.playerSlots.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH));

                this.totalHeight += slotH + gutter;
            }

            this.slotsCreated = true;
        }

        for (int i = 0; i < this.playerSlots.size(); i++)
        {
            ((GuiPlayerSlot) this.playerSlots.get(i)).drawPlayerSlot(centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);

            this.totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(3089);

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        drawString(this.fontRenderer, "Player List", centerX + 40, centerY + 10, 16777215);
        String kickName;
        if ((this.selectedPlayerSlot != null) && (!PartyController.instance().isLeader(this.selectedPlayerSlot.username)))
        {
            kickName = "Manage";
        } else kickName = "Disband";

        this.kickButton = new GuiButton(1, this.xMember + 3, this.yMember + 85 - 28, 58, 20, kickName);

        if ((this.selectedPlayerSlot != null) && (this.slotIsSelected) && ((!PartyController.instance().isLeader(this.selectedPlayerSlot.username)) || (this.playerSlots.size() == 1)))
        {
            this.kickButton.enabled = true;
        } else this.kickButton.enabled = false;

        this.buttonList.add(new GuiButton(0, this.xMember - 60, this.yMember + 85 - 28, 58, 20, "Back"));
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
