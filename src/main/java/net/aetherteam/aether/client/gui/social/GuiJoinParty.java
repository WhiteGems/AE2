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

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private int totalHeight;
    private GuiPartySlot selectedPartySlot;
    private boolean slotsCreated = false;
    private boolean slotIsSelected = false;
    private GuiButton joinButton;
    private EntityPlayer player;

    public GuiJoinParty(EntityPlayer var1, GuiScreen var2)
    {
        this.player = var1;
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        this.parent = var2;
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
    protected void mouseClicked(int var1, int var2, int var3)
    {
        if (var3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.mc, var1, var2);
            }

            for (int var4 = 0; var4 < this.partySlots.size(); ++var4)
            {
                int var5 = (int)((float)var2 + this.sbar.sliderValue * (float)(this.totalHeight - 103));
                GuiPartySlot var6 = (GuiPartySlot)this.partySlots.get(var4);

                if (var6.mousePressed(this.mc, var1, var5) && var2 < this.yParty + 50)
                {
                    if (var6.party.getType() != PartyType.OPEN)
                    {
                        this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.5F);
                        return;
                    }

                    var6.selected = true;
                    this.slotIsSelected = true;
                    this.selectedPartySlot = var6;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

                    for (int var7 = 0; var7 < this.partySlots.size(); ++var7)
                    {
                        GuiPartySlot var8 = (GuiPartySlot)this.partySlots.get(var7);

                        if (var8 != var6)
                        {
                            var8.selected = false;
                        }
                    }

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
    public void drawScreen(int var1, int var2, float var3)
    {
        this.buttonList.clear();
        ArrayList var4 = (ArrayList)PartyController.instance().getParties().clone();
        ArrayList var5 = new ArrayList();
        Iterator var6 = var4.iterator();

        while (var6.hasNext())
        {
            Party var7 = (Party)var6.next();

            if (var7.getType() != PartyType.PRIVATE)
            {
                var5.add(var7);
            }
        }

        if (var5.size() != this.partySlots.size())
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
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
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

        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        ScaledResolution var9 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        this.totalHeight = 0;
        byte var10 = 100;
        byte var11 = 20;
        byte var12 = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * var9.getScaleFactor(), (centerY + 35) * var9.getScaleFactor(), var10 * var9.getScaleFactor(), 103 * var9.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = var5.size() * (var11 + var12);
        float var13 = -this.sbar.sliderValue * (float)(this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, var13, 0.0F);
        }

        this.totalHeight = 0;

        if (!this.slotsCreated)
        {
            for (int var14 = 0; var14 < var5.size(); ++var14)
            {
                if (((Party)var5.get(var14)).getType() != PartyType.PRIVATE)
                {
                    this.partySlots.add(new GuiPartySlot((Party)var5.get(var14), this.partySlots.size(), centerX + 15, centerY + this.totalHeight + 30, var10, var11));
                    this.totalHeight += var11 + var12;
                }
            }

            this.slotsCreated = true;
        }

        boolean var19 = true;

        for (int var15 = 0; var15 < this.partySlots.size(); ++var15)
        {
            if (((GuiPartySlot)this.partySlots.get(var15)).party.getType() != PartyType.PRIVATE)
            {
                var19 = false;
                ((GuiPartySlot)this.partySlots.get(var15)).drawPartySlot(centerX + 15, centerY + this.totalHeight + 30, var10, var11);
                this.totalHeight += var11 + var12;
            }
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, var1, var2);
        }

        this.mc.renderEngine.resetBoundTexture();
        drawString(this.fontRenderer, "公会列表", centerX + 70 - this.fontRenderer.getStringWidth("公会列表") / 2, centerY + 10, 16777215);

        if (var5.size() == 0 || var19)
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.dialogueTexture);
            float var20 = 1.3F;
            GL11.glTranslatef((float)this.xParty - 100.0F * var20, (float)this.yParty - (float)((this.hParty - 201) / 2) * var20, 0.0F);
            GL11.glScalef(var20, var20, var20);
            this.drawTexturedModalRect(0, 0, 0, 0, 201, this.hParty - 201);
            GL11.glPopMatrix();
            this.mc.renderEngine.resetBoundTexture();

            String warningLabel = "当前无人建立公会";

            drawString(this.fontRenderer, warningLabel, centerX + 70 - this.fontRenderer.getStringWidth(warningLabel) / 2, centerY + 75, 16777215);
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
        this.xParty = var2 / 2;
        this.yParty = var3 / 2;
    }
}