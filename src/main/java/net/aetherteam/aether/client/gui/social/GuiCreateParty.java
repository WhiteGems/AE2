package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.client.gui.social.dialogue.GuiDialogueBox;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.PartyType;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiCreateParty extends GuiScreen
{
    private final PartyData pm;
    private int backgroundTexture;
    private int partyCreatedTexture;
    private int partyX;
    private int partyY;
    private int partyW;
    private int partyH;
    Minecraft g;
    private ArrayList partyType = new ArrayList();
    private int typeIndex = 0;
    private GuiButton typeButton;
    private GuiButton finishButton;
    private GuiButton backButton;
    private GuiTextField partyNameField;
    private String partyName = "";
    private EntityPlayer player;
    private GuiScreen parent;

    public GuiCreateParty(EntityPlayer player, GuiScreen parent)
    {
        this(new PartyData(), player, parent);
    }

    public GuiCreateParty(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.partyType.add("Open");
        this.partyType.add("Closed");
        this.partyType.add("Private");
        this.player = player;
        this.g = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/createParty.png");
        this.partyCreatedTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/partyCreated.png");
        this.partyW = 256;
        this.partyH = 256;
        updateScreen();
    }

    public void initGui()
    {
        updateScreen();
        this.k.clear();
        this.typeButton = new GuiButton(1, this.partyX - 60, this.partyY - 16 - 28, 120, 20, "Type: " + (String)this.partyType.get(this.typeIndex));
        this.finishButton = new GuiButton(2, this.partyX - 60, this.partyY + 6 - 28, 120, 20, "Finish Party");
        this.backButton = new GuiButton(0, this.partyX - 60, this.partyY + 81 - 28, 120, 20, "Back");
        this.k.add(this.typeButton);
        this.k.add(this.finishButton);
        this.k.add(this.backButton);
        this.partyNameField = new GuiTextField(this.m, this.partyX - 55, this.partyY - 64, 107, 16);
        this.partyNameField.setFocused(true);
        this.partyNameField.setMaxStringLength(22);
        this.partyNameField.setText(this.partyName);
        this.partyNameField.setEnableBackgroundDrawing(false);
    }

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                this.g.displayGuiScreen(this.parent);
                break;

            case 1:
                this.typeIndex += 1;
                break;

            case 2:
                Party party = new Party(this.partyName, new PartyMember(this.player)).setType(PartyType.getTypeFromString((String)this.partyType.get(this.typeIndex)));
                boolean created = PartyController.instance().addParty(party, true);
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyChange(true, this.partyName, this.player.username, this.player.skinUrl));
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyTypeChange(this.partyName, PartyType.getTypeFromString((String)this.partyType.get(this.typeIndex))));
                this.g.displayGuiScreen(new GuiDialogueBox(this.parent, "Your party was successfully created!", "Your party name is already taken. Sorry :(", created));
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        this.k.clear();

        if (this.typeIndex > this.partyType.size() - 1)
        {
            this.typeIndex = 0;
        }

        this.typeButton = new GuiButton(1, this.partyX - 60, this.partyY - 16 - 28, 120, 20, "Type: " + (String)this.partyType.get(this.typeIndex));
        this.finishButton = new GuiButton(2, this.partyX - 60, this.partyY + 6 - 28, 120, 20, "Finish Party");

        if (this.partyName.isEmpty())
        {
            this.finishButton.enabled = false;
        }

        this.k.add(this.typeButton);
        this.k.add(this.finishButton);
        this.k.add(this.backButton);
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int centerX = this.partyX - 70;
        int centerY = this.partyY - 84;
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.partyH);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        this.g.renderEngine.a();
        drawString(this.m, "Insert Party Name", centerX + 68 - this.m.getStringWidth("Insert Party Name") / 2, centerY + 5, 16777215);
        this.partyNameField.drawTextBox();
        super.drawScreen(x, y, partialTick);
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        if (this.partyNameField.isFocused())
        {
            this.partyNameField.textboxKeyTyped(charTyped, keyTyped);
            this.partyName = this.partyNameField.getText();
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
        this.partyNameField.mouseClicked(par1, par2, par3);
        super.mouseClicked(par1, par2, par3);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.partyX = (width / 2);
        this.partyY = (height / 2);

        if (this.partyNameField != null)
        {
            this.partyNameField.updateCursorCounter();
        }
    }
}

