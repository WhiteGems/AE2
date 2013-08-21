package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import java.util.Iterator;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.PartyType;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiJoinParty extends GuiScreen
{
    private static final ResourceLocation TEXTURE_DIALOGUE = new ResourceLocation("aether", "textures/gui/dialogue.png");
    private static final ResourceLocation TEXTURE_PARTY = new ResourceLocation("aether", "textures/gui/party.png");
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    private GuiScreen parent;
    private ArrayList<GuiPartySlot> partySlots = new ArrayList();

    /** Reference to the Minecraft object. */
    Minecraft mc;
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
        this.wParty = 256;
        this.hParty = 256;
        this.parent = parent;
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

        if (this.sbar != null)
        {
            this.sbarVal = this.sbar.sliderValue;
        }

        this.sbar = new GuiYSlider(-1, this.xParty + 46, this.yParty - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
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

            for (int l = 0; l < this.partySlots.size(); ++l)
            {
                int y = (int)((float)par2 + this.sbar.sliderValue * (float)(this.totalHeight - 103));
                GuiPartySlot partySlot = (GuiPartySlot)this.partySlots.get(l);

                if (partySlot.mousePressed(this.mc, par1, y) && par2 < this.yParty + 50)
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

                    for (int rr = 0; rr < this.partySlots.size(); ++rr)
                    {
                        GuiPartySlot partySlot2 = (GuiPartySlot)this.partySlots.get(rr);

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
        ArrayList properList = (ArrayList)PartyController.instance().getParties().clone();
        ArrayList partyList = new ArrayList();
        Iterator dmsy = properList.iterator();

        while (dmsy.hasNext())
        {
            Party centerX = (Party)dmsy.next();

            if (centerX.getType() != PartyType.PRIVATE)
            {
                partyList.add(centerX);
            }
        }

        if (partyList.size() != this.partySlots.size())
        {
            this.partySlots.clear();
            this.slotsCreated = false;
        }

        if (this.selectedPartySlot != null && this.selectedPartySlot.party.getType() != PartyType.OPEN)
        {
            this.selectedPartySlot.selected = false;
            this.slotIsSelected = false;
            this.selectedPartySlot = null;
        }

        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_PARTY);
        int var17 = Mouse.getDWheel();
        this.sbar.sliderValue -= (float)var17 / 1000.0F;

        if (this.sbar.sliderValue > 1.0F)
        {
            this.sbar.sliderValue = 1.0F;
        }

        if (this.sbar.sliderValue < 0.0F)
        {
            this.sbar.sliderValue = 0.0F;
        }

        int var18 = this.xParty - 70;
        int centerY = this.yParty - 84;
        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(var18, centerY, 0, 0, 141, this.hParty);
        this.totalHeight = 0;
        byte slotW = 100;
        byte slotH = 20;
        byte gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((var18 + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = partyList.size() * (slotH + gutter);
        float sVal = -this.sbar.sliderValue * (float)(this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        this.totalHeight = 0;

        if (!this.slotsCreated)
        {
            for (int showNoParties = 0; showNoParties < partyList.size(); ++showNoParties)
            {
                if (((Party)partyList.get(showNoParties)).getType() != PartyType.PRIVATE)
                {
                    this.partySlots.add(new GuiPartySlot((Party)partyList.get(showNoParties), this.partySlots.size(), var18 + 15, centerY + this.totalHeight + 30, slotW, slotH));
                    this.totalHeight += slotH + gutter;
                }
            }

            this.slotsCreated = true;
        }

        boolean var19 = true;

        for (int scaleFactor = 0; scaleFactor < this.partySlots.size(); ++scaleFactor)
        {
            if (((GuiPartySlot)this.partySlots.get(scaleFactor)).party.getType() != PartyType.PRIVATE)
            {
                var19 = false;
                ((GuiPartySlot)this.partySlots.get(scaleFactor)).drawPartySlot(var18 + 15, centerY + this.totalHeight + 30, slotW, slotH);
                this.totalHeight += slotH + gutter;
            }
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        this.drawString(this.fontRenderer, "公会列表", var18 + 70 - this.fontRenderer.getStringWidth("公会列表") / 2, centerY + 10, 16777215);

        if (partyList.size() == 0 || var19)
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.func_110577_a(TEXTURE_DIALOGUE);
            float var20 = 1.3F;
            GL11.glTranslatef((float)this.xParty - 100.0F * var20, (float)this.yParty - (float)((this.hParty - 201) / 2) * var20, 0.0F);
            GL11.glScalef(var20, var20, var20);
            this.drawTexturedModalRect(0, 0, 0, 0, 201, this.hParty - 201);
            GL11.glPopMatrix();
            String warningLabel = "当前无人建立公会.";
            this.drawString(this.fontRenderer, warningLabel, var18 + 70 - this.fontRenderer.getStringWidth(warningLabel) / 2, centerY + 75, 16777215);
        }

        this.joinButton = new GuiButton(1, this.xParty + 3, this.yParty + 85 - 28, 58, 20, "加入");

        if (this.selectedPartySlot != null && this.slotIsSelected)
        {
            this.joinButton.enabled = true;
        }
        else
        {
            this.joinButton.enabled = false;
        }

        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 85 - 28, 58, 20, "返回"));
        this.buttonList.add(this.joinButton);
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
}
