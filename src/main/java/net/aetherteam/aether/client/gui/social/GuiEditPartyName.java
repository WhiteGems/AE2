package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.List;
import net.aetherteam.aether.client.gui.social.dialogue.GuiDialogueBox;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditPartyName extends GuiScreen
{
    private GuiTextField dialogueInput;
    private String name = "Aether Party";
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft g;
    private GuiScreen parent;
    private EntityPlayer player;

    public GuiEditPartyName(EntityPlayer player, GuiScreen parent)
    {
        this(new PartyData(), player, parent);
    }

    public GuiEditPartyName(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.player = player;
        this.g = FMLClientHandler.instance().getClient();
        this.dialogueTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        updateScreen();
        this.k.clear();
        this.dialogueInput = new GuiTextField(this.m, this.xParty - 88 + this.m.getStringWidth(this.name), this.yParty - 7, 193 - this.m.getStringWidth(this.name) - 10, 16);
        this.dialogueInput.setFocused(true);
        this.dialogueInput.setMaxStringLength(22);
        this.dialogueInput.setCanLoseFocus(false);
        this.k.add(new GuiButton(0, this.xParty - 1, this.yParty + 14, 50, 20, "Confirm"));
        this.k.add(new GuiButton(1, this.xParty + 52, this.yParty + 14, 45, 20, "Cancel"));
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        if (this.dialogueInput.isFocused())
        {
            this.dialogueInput.textboxKeyTyped(charTyped, keyTyped);
        }
        else if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.g.displayGuiScreen((GuiScreen)null);
            this.g.setIngameFocus();
        }

        super.keyTyped(charTyped, keyTyped);
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        this.dialogueInput.mouseClicked(par1, par2, par3);
        super.mouseClicked(par1, par2, par3);
    }

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                Party party = PartyController.instance().getParty(this.player);
                boolean isLeader = PartyController.instance().isLeader(this.player);

                if (isLeader)
                {
                    if (party != null)
                    {
                        String partyName = party.getName();
                        boolean nameChanged = PartyController.instance().changePartyName(party, this.dialogueInput.getText(), true);
                        this.g.displayGuiScreen(new GuiDialogueBox(this.parent, "Party name was changed to '" + this.dialogueInput.getText() + "'!", "That party name is already taken. Sorry :(", nameChanged));
                    }
                }

                break;

            case 1:
                this.g.displayGuiScreen(this.parent);
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        totalHeight = 0;
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        drawGradientRect(0, 0, this.height, this.i, -1728053248, -1728053248);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.dialogueTexture);
        drawTexturedModalRect(centerX - 30, centerY + 71, 0, 0, 201, this.hParty - 201);
        this.dialogueInput.drawTextBox();
        this.m.drawString("Change Party Name", (int)((centerX + this.i) / 0.75F), (int)((centerY + 12.0F) / 0.75F), -10066330);
        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();

        if (this.dialogueInput != null)
        {
            this.dialogueInput.updateCursorCounter();
        }

        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }
}

