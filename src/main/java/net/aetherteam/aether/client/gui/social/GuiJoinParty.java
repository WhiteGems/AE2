package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.List;

import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.PartyType;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.FontRenderer;
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

public class GuiJoinParty extends GuiScreen
{
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    private GuiScreen parent;
    private ArrayList partySlots = new ArrayList();
    Minecraft f;
    private int totalHeight;
    private GuiPartySlot selectedPartySlot;
    private boolean slotsCreated = false;
    private boolean slotIsSelected = false;
    private GuiButton joinButton;
    private EntityPlayer player;

    public GuiJoinParty(EntityPlayer player, GuiScreen parent)
    {
        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        this.parent = parent;
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
        if (this.sbar != null) this.sbarVal = this.sbar.sliderValue;
        this.sbar = new GuiYSlider(-1, this.xParty + 46, this.yParty - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.mc, par1, par2);
            }
            for (int l = 0; l < this.partySlots.size(); l++)
            {
                int y = (int) (par2 + this.sbar.sliderValue * (this.totalHeight - 103));

                GuiPartySlot partySlot = (GuiPartySlot) this.partySlots.get(l);

                if ((partySlot.mousePressed(this.mc, par1, y)) && (par2 < this.yParty + 50))
                {
                    if (partySlot.party.getType() != PartyType.OPEN)
                    {
                        this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.5F);
                        return;
                    }
                    partySlot.selected = true;
                    this.slotIsSelected = true;
                    this.selectedPartySlot = partySlot;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

                    for (int rr = 0; rr < this.partySlots.size(); rr++)
                    {
                        GuiPartySlot partySlot2 = (GuiPartySlot) this.partySlots.get(rr);

                        if (partySlot2 != partySlot)
                        {
                            partySlot2.selected = false;
                        }
                    }

                    return;
                }
                partySlot.selected = false;
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

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
                break;
            case 1:
                PartyController.instance().joinParty(this.selectedPartySlot.party, new PartyMember(this.player), true);

                this.mc.displayGuiScreen(this.parent);
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        this.buttonList.clear();

        ArrayList<Party> properList = (ArrayList) PartyController.instance().getParties().clone();
        ArrayList partyList = new ArrayList();

        for (Party party : properList)
        {
            if (party.getType() != PartyType.PRIVATE)
            {
                partyList.add(party);
            }
        }

        if (partyList.size() != this.partySlots.size())
        {
            this.partySlots.clear();
            this.slotsCreated = false;
        }

        if ((this.selectedPartySlot != null) && (this.selectedPartySlot.party.getType() != PartyType.OPEN))
        {
            this.selectedPartySlot.selected = false;
            this.slotIsSelected = false;
            this.selectedPartySlot = null;
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
        this.totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(3089);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = (partyList.size() * (slotH + gutter));
        float sVal = -this.sbar.sliderValue * (this.totalHeight - 105);
        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }
        this.totalHeight = 0;

        if (!this.slotsCreated)
        {
            for (int i = 0; i < partyList.size(); i++)
            {
                if (((Party) partyList.get(i)).getType() != PartyType.PRIVATE)
                {
                    this.partySlots.add(new GuiPartySlot((Party) partyList.get(i), this.partySlots.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH));

                    this.totalHeight += slotH + gutter;
                }
            }

            this.slotsCreated = true;
        }

        boolean showNoParties = true;

        for (int i = 0; i < this.partySlots.size(); i++)
        {
            if (((GuiPartySlot) this.partySlots.get(i)).party.getType() != PartyType.PRIVATE)
            {
                showNoParties = false;

                ((GuiPartySlot) this.partySlots.get(i)).drawPartySlot(centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);

                this.totalHeight += slotH + gutter;
            }
        }

        GL11.glPopMatrix();
        GL11.glDisable(3089);

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        this.mc.renderEngine.resetBoundTexture();
        drawString(this.fontRenderer, "公会列表", centerX + 70 - this.fontRenderer.getStringWidth("公会列表") / 2, centerY + 10, 16777215);

        if ((partyList.size() == 0) || (showNoParties))
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(3553, this.dialogueTexture);

            float scaleFactor = 1.3F;

            GL11.glTranslatef(this.xParty - 100.0F * scaleFactor, this.yParty - (this.hParty - 201) / 2 * scaleFactor, 0.0F);

            GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

            drawTexturedModalRect(0, 0, 0, 0, 201, this.hParty - 201);
            GL11.glPopMatrix();
            this.mc.renderEngine.resetBoundTexture();

            String warningLabel = "当前无人建立公会";

            drawString(this.fontRenderer, warningLabel, centerX + 70 - this.fontRenderer.getStringWidth(warningLabel) / 2, centerY + 75, 16777215);
        }

        this.joinButton = new GuiButton(1, this.xParty + 3, this.yParty + 85 - 28, 58, 20, "加入");

        if ((this.selectedPartySlot != null) && (this.slotIsSelected))
        {
            this.joinButton.enabled = true;
        } else this.joinButton.enabled = false;

        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 85 - 28, 58, 20, "返回"));
        this.buttonList.add(this.joinButton);

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
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiJoinParty
 * JD-Core Version:    0.6.2
 */
