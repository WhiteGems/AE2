package net.aetherteam.aether.client.gui.dungeons;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDungeonEntrance extends GuiScreen
{
    private static final ResourceLocation TEXTURE_PARTY_MAIN = new ResourceLocation("aether", "textures/gui/partyMain.png");
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    public String[] description;
    private GuiTextField partyNameField;
    private EntityPlayer player;
    private GuiScreen parent;
    private TileEntityEntranceController controller;

    public GuiDungeonEntrance(EntityPlayer player, GuiScreen parent, TileEntityEntranceController controller)
    {
        this.parent = parent;
        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
        this.controller = controller;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.updateScreen();
        this.buttonList.clear();
        List playerList = this.mc.thePlayer.sendQueue.playerInfoList;

        if (playerList.size() > 1 || playerList.size() == 0)
        {
            this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Enter"));
            this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Leave"));
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton button)
    {
        Party party = PartyController.instance().getParty(this.player);

        switch (button.id)
        {
            case 0:
                if (this.controller != null && this.controller.getDungeon() != null && !this.controller.getDungeon().hasQueuedParty())
                {
                    if (party != null)
                    {
                        int x = MathHelper.floor_double((double)this.controller.xCoord);
                        int y = MathHelper.floor_double((double)this.controller.yCoord);
                        int z = MathHelper.floor_double((double)this.controller.zCoord);
                        DungeonHandler.instance().queueParty(this.controller.getDungeon(), party, x, y, z, true);
                        this.mc.displayGuiScreen((GuiScreen)null);
                    }
                    else
                    {
                        this.mc.displayGuiScreen(new GuiCreateDungeonParty(this.player, this, this.controller));
                    }
                }

                break;

            case 1:
                if (party != null && this.controller != null && this.controller.getDungeon() != null && this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player)))
                {
                    DungeonHandler.instance().disbandMember(this.controller.getDungeon(), PartyController.instance().getMember(this.player), true);
                }

                this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private boolean isQueuedParty(Party party)
    {
        return party != null && this.controller != null && this.controller.getDungeon() != null && this.controller.getDungeon().isActive() && this.controller.getDungeon().isQueuedParty(party);
    }

    private boolean hasQueuedParty()
    {
        return this.controller != null && this.controller.getDungeon() != null && this.controller.getDungeon().isActive() && this.controller.getDungeon().hasQueuedParty();
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
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int x, int y, float partialTick)
    {
        this.buttonList.clear();
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_PARTY_MAIN);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        Party party = PartyController.instance().getParty(this.player);
        boolean isLeader = PartyController.instance().isLeader(this.player);
        GuiButton sendButton = new GuiButton(0, this.xParty - 59, this.yParty + 55, 55, 20, party != null && party.getSize() > 1 ? "Send" : "Enter");
        GuiButton leaveButton = new GuiButton(1, this.xParty + 6 - (this.hasQueuedParty() ? 32 : 0), this.yParty + 55, 55, 20, "Leave");

        if (this.controller.getDungeon() != null && !this.controller.getDungeon().isActive() && this.controller != null)
        {
            this.buttonList.add(sendButton);

            if (party != null && (this.controller.getDungeon().isQueuedParty(party) || this.controller.getDungeon().hasAnyConqueredDungeon(party.getMembers()) || !isLeader))
            {
                sendButton.enabled = false;
            }
        }

        this.buttonList.add(leaveButton);
        this.partyNameField = new GuiTextField(this.fontRenderer, this.xParty - 63, this.yParty - 58, 125, 107);
        this.partyNameField.setFocused(false);
        this.partyNameField.setMaxStringLength(5000);
        this.partyNameField.drawTextBox();
        this.drawString(this.fontRenderer, "\u00a7f\u00a7n\u00a7lWARNING!", centerX + 46, centerY + 10, 15658734);

        if (this.controller != null && this.controller.hasDungeon())
        {
            int len$;

            if ((party != null || isLeader) && (!isLeader || party.getSize() > 1 || party == null || this.controller.getDungeon().hasAnyConqueredDungeon(party.getMembers()) || this.controller.getDungeon().hasQueuedParty()))
            {
                ArrayList var18 = new ArrayList();

                if (party != null)
                {
                    var18 = party.getMembers();
                }

                if (this.controller.getDungeon().hasQueuedParty() && (!this.controller.getDungeon().isQueuedParty(party) || this.controller.getDungeon().isQueuedParty(party) && !this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player))))
                {
                    this.description = new String[6];
                    this.description[0] = "Sorry, but at this time";
                    this.description[1] = "the dungeon is occupied";

                    if (this.controller.getDungeon().isQueuedParty(party) && !this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player)))
                    {
                        this.description[2] = "by your party.";
                    }
                    else
                    {
                        this.description[2] = "by another party.";
                    }

                    this.description[3] = "";
                    this.description[4] = "Please come back at";
                    this.description[5] = "a later time.";
                }
                else if (this.controller.getDungeon().isQueuedParty(party) && this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player)))
                {
                    if (this.controller.getDungeon().isActive())
                    {
                        this.description = new String[8];
                        this.description[0] = "Would you like to";
                        this.description[1] = "leave this dungeon?";
                        this.description[2] = "";
                        this.description[3] = 3 - this.controller.getDungeon().getMemberLeaves(PartyController.instance().getMember(this.player)) + "/3 Quits Left";
                        this.description[4] = "";
                        this.description[5] = "Each dungeon can only";
                        this.description[6] = "be left 3 times before";
                        this.description[7] = "it becomes unusable.";
                    }
                    else
                    {
                        this.description = new String[7];
                        this.description[0] = "Your party has been";
                        this.description[1] = "queued into this";
                        this.description[2] = "dungeon.";
                        this.description[3] = "";
                        this.description[4] = "Please wait for others";
                        this.description[5] = "to accept the dungeon";
                        this.description[6] = "queue.";
                    }
                }
                else if (this.controller.getDungeon().hasAnyConqueredDungeon(var18))
                {
                    this.description = new String[8];
                    this.description[0] = "Sorry, but this";
                    this.description[1] = "dungeon has already";
                    this.description[2] = "been conquered by";
                    this.description[3] = "someone in your";
                    this.description[4] = "party.";
                    this.description[5] = "";
                    this.description[6] = "Please search for";
                    this.description[7] = "another one.";
                }
                else if (isLeader)
                {
                    this.description = new String[7];
                    this.description[0] = "Would you like to";
                    this.description[1] = "attempt the Slider\'s";
                    this.description[2] = "Labyrinth with your";
                    this.description[3] = "party? If so:";
                    this.description[4] = "";
                    this.description[5] = "Send requests to";
                    this.description[6] = "your members?";
                }
                else
                {
                    this.description = new String[8];
                    this.description[0] = "Would you like to";
                    this.description[1] = "attempt the Slider\'s";
                    this.description[2] = "Labyrinth with your";
                    this.description[3] = "party? If so:";
                    this.description[4] = "";
                    this.description[5] = "Ask your leader";
                    this.description[6] = "to start a dungeon";
                    this.description[7] = "queue at this door.";
                }

                int var17 = 0;
                String[] var19 = this.description;
                len$ = var19.length;

                for (int var20 = 0; var20 < len$; ++var20)
                {
                    String string = var19[var20];
                    this.drawString(this.fontRenderer, string, centerX + 70 - this.fontRenderer.getStringWidth(string) / 2, centerY + (isLeader ? 30 : 40) + var17 * 10, 15658734);
                    ++var17;
                }
            }
            else
            {
                this.mc.renderEngine.func_110577_a(TEXTURE_PARTY_MAIN);
                this.description = new String[10];
                this.description[0] = "You are attempting the";
                this.description[1] = "Slider\'s Labyrinth on";
                this.description[2] = "your own. This dungeon";
                this.description[3] = "is a very dangerous";
                this.description[4] = "place, and you could";
                this.description[5] = "lose all your items as";
                this.description[6] = "a result.";
                this.description[7] = "";
                this.description[8] = "Are you prepared to";
                this.description[9] = "enter these depths?";
                int members = 0;
                String[] count = this.description;
                int arr$ = count.length;

                for (len$ = 0; len$ < arr$; ++len$)
                {
                    String i$ = count[len$];
                    this.drawString(this.fontRenderer, i$, centerX + 70 - this.fontRenderer.getStringWidth(i$) / 2, centerY + 30 + members * 10, 15658734);
                    ++members;
                }
            }
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
        this.xParty = width / 2;
        this.yParty = height / 2;
    }
}
