package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.List;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.notifications.client.GuiRequestPlayer;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiPartyMenu extends GuiScreen
{
    private final PartyData pm;
    private int backgroundTexture;
    private int partyCreatedTexture;
    private int xMenu;
    private int yMenu;
    private int wMenu;
    private int hMenu;
    Minecraft g;
    private EntityPlayer player;
    private GuiButton createPartyButton;
    private GuiButton leavePartyButton;
    private GuiButton editPartyButton;
    private GuiButton joinPartyButton;
    private GuiButton membersButton;
    private GuiButton addMemberButton;
    private GuiScreen parent;

    public GuiPartyMenu(EntityPlayer player, GuiScreen parent)
    {
        this(new PartyData(), player, parent);
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

    public GuiPartyMenu(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.player = player;
        this.g = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.partyCreatedTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/partyCreated.png");
        this.wMenu = 256;
        this.hMenu = 256;
        updateScreen();
    }

    protected void actionPerformed(GuiButton btn)
    {
        switch (btn.id)
        {
            case 0:
                this.g.displayGuiScreen(this.parent);
                break;

            case 2:
                this.g.displayGuiScreen(new GuiPartyList(this));
                break;

            case 3:
                this.g.displayGuiScreen(new GuiPartyEdit(this.player, this));
                break;

            case 4:
                this.g.displayGuiScreen(new GuiJoinParty(this.player, this));
                break;

            case 5:
                this.g.displayGuiScreen(new GuiCreateParty(this.player, this));
                break;

            case 6:
                Party party = PartyController.instance().getParty(this.player);
                PartyMember potentialLeader = PartyController.instance().getMember(this.player);
                boolean isLeader = party.isLeader(potentialLeader);

                if (isLeader)
                {
                    PartyController.instance().removeParty(party, true);
                }
                else
                {
                    PartyController.instance().leaveParty(party, potentialLeader, true);
                }

                break;

            case 7:
                this.g.displayGuiScreen(new GuiMemberList(this.player, this));
                break;

            case 8:
                this.g.displayGuiScreen(new GuiRequestPlayer(this.player, this));

            case 1:
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        this.k.clear();
        boolean isLeader = PartyController.instance().isLeader(this.player);
        boolean inParty = PartyController.instance().inParty(this.player);
        this.createPartyButton = new GuiButton(5, this.xMenu - 60, this.yMenu - 36 - 28, 120, 20, "Create Party");
        this.joinPartyButton = new GuiButton(4, this.xMenu - 60, this.yMenu - 14 - 28, 120, 20, "Join Existing Party");
        this.membersButton = new GuiButton(7, this.xMenu - 60, this.yMenu + 30 - (inParty ? 53 : 28), 120, 20, "Members");
        this.editPartyButton = new GuiButton(3, this.xMenu - 60, this.yMenu + 30 - (inParty ? 53 : 28), 120, 20, "Manage Party");
        this.leavePartyButton = new GuiButton(6, this.xMenu - 60, this.yMenu + 74 - (inParty ? 53 : 28), 120, 20, (isLeader ? "Disband" : "Leave") + " Party");
        this.addMemberButton = new GuiButton(8, this.xMenu - 60, this.yMenu + 52 - (inParty ? 53 : 28), 120, 20, isLeader ? "Add Member" : "Make Request");

        if (!inParty)
        {
            this.leavePartyButton.enabled = false;
            this.createPartyButton.enabled = true;
            this.joinPartyButton.enabled = true;
            this.k.add(this.createPartyButton);
            this.k.add(this.joinPartyButton);
        }
        else
        {
            this.createPartyButton.enabled = false;
            this.leavePartyButton.enabled = true;
            this.joinPartyButton.enabled = false;
            this.addMemberButton.enabled = true;
            this.k.add(this.leavePartyButton);
            this.k.add(this.membersButton);
            this.k.add(this.addMemberButton);
        }

        Party party = PartyController.instance().getParty(PartyController.instance().getMember(this.player));

        if ((party != null) && (DungeonHandler.instance().isInDungeon(party)))
        {
            this.addMemberButton.enabled = false;
        }
        else if (isLeader)
        {
            this.editPartyButton.enabled = true;
            this.addMemberButton.enabled = true;
        }
        else
        {
            this.editPartyButton.enabled = false;
            this.addMemberButton.enabled = false;
        }

        this.k.add(new GuiButton(2, this.xMenu - 60, this.yMenu + 8 - (inParty ? 53 : 28), 120, 20, "Party List"));

        if (isLeader)
        {
            this.k.add(this.editPartyButton);
        }

        this.k.add(new GuiButton(0, this.xMenu - 60, this.yMenu + 81 - 28, 120, 20, "Back"));
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, inParty ? this.partyCreatedTexture : this.backgroundTexture);
        int centerX = this.xMenu - 70;
        int centerY = this.yMenu - 84;
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hMenu);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        this.g.renderEngine.a();
        String name = "";

        if (inParty)
        {
            name = "Your Party:";
        }
        else
        {
            name = "Party Menu";
        }

        if (!isLeader)
        {
            name = "Joined Party:";
        }

        drawString(this.m, name, centerX + 69 - this.m.getStringWidth(name) / 2, centerY + 5, 16777215);

        if (inParty)
        {
            if (party != null)
            {
                String partyName = party.getName();
                drawString(this.m, partyName, centerX + 70 - this.m.getStringWidth(partyName) / 2, centerY + 20, 16777215);
            }
        }

        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xMenu = (width / 2);
        this.yMenu = (height / 2);
    }
}

