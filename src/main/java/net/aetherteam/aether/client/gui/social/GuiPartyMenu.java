package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.notifications.client.GuiRequestPlayer;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiPartyMenu extends GuiScreen
{
    private static final ResourceLocation TEXTURE_PARTYCREATED = new ResourceLocation("aether", "textures/gui/partyCreated.png");
    private static final ResourceLocation TEXTURE_PARTYMAIN = new ResourceLocation("aether", "textures/gui/partyMain.png");
    private final PartyData pm;
    private int xMenu;
    private int yMenu;
    private int wMenu;
    private int hMenu;

    /** Reference to the Minecraft object. */
    Minecraft mc;
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

    public GuiPartyMenu(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.wMenu = 256;
        this.hMenu = 256;
        this.updateScreen();
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

            case 1:
            default:
                break;

            case 2:
                this.mc.displayGuiScreen(new GuiPartyList(this));
                break;

            case 3:
                this.mc.displayGuiScreen(new GuiPartyEdit(this.player, this));
                break;

            case 4:
                this.mc.displayGuiScreen(new GuiJoinParty(this.player, this));
                break;

            case 5:
                this.mc.displayGuiScreen(new GuiCreateParty(this.player, this));
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
                this.mc.displayGuiScreen(new GuiMemberList(this.player, this));
                break;

            case 8:
                this.mc.displayGuiScreen(new GuiRequestPlayer(this.player, this));
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
            this.buttonList.add(this.createPartyButton);
            this.buttonList.add(this.joinPartyButton);
        }
        else
        {
            this.createPartyButton.enabled = false;
            this.leavePartyButton.enabled = true;
            this.joinPartyButton.enabled = false;
            this.addMemberButton.enabled = true;
            this.buttonList.add(this.leavePartyButton);
            this.buttonList.add(this.membersButton);
            this.buttonList.add(this.addMemberButton);
        }

        Party party = PartyController.instance().getParty(PartyController.instance().getMember(this.player));

        if (party != null && DungeonHandler.instance().isInDungeon(party))
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

        this.buttonList.add(new GuiButton(2, this.xMenu - 60, this.yMenu + 8 - (inParty ? 53 : 28), 120, 20, "Party List"));

        if (isLeader)
        {
            this.buttonList.add(this.editPartyButton);
        }

        this.buttonList.add(new GuiButton(0, this.xMenu - 60, this.yMenu + 81 - 28, 120, 20, "Back"));
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(inParty ? TEXTURE_PARTYCREATED : TEXTURE_PARTYMAIN);
        int centerX = this.xMenu - 70;
        int centerY = this.yMenu - 84;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hMenu);
        this.mc.renderEngine.func_110577_a(TEXTURE_PARTYMAIN);
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

        this.drawString(this.fontRenderer, name, centerX + 69 - this.fontRenderer.getStringWidth(name) / 2, centerY + 5, 16777215);

        if (inParty && party != null)
        {
            String partyName = party.getName();
            this.drawString(this.fontRenderer, partyName, centerX + 70 - this.fontRenderer.getStringWidth(partyName) / 2, centerY + 20, 16777215);
        }

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
        this.xMenu = width / 2;
        this.yMenu = height / 2;
    }
}
