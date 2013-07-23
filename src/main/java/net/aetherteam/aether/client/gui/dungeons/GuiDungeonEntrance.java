package net.aetherteam.aether.client.gui.dungeons;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.multiplayer.CallableMPL2;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class GuiDungeonEntrance extends GuiScreen
{
    private int backgroundTexture;
    private int easterTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft g;
    public String[] description;
    private GuiTextField partyNameField;
    private EntityPlayer player;
    private GuiScreen parent;
    private TileEntityEntranceController controller;

    public GuiDungeonEntrance(EntityPlayer player, GuiScreen parent, TileEntityEntranceController controller)
    {
        this.parent = parent;
        this.player = player;
        this.g = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
        this.controller = controller;
    }

    public void initGui()
    {
        updateScreen();
        this.k.clear();
        List playerList = this.g.thePlayer.theWorldClient.c;

        if ((playerList.size() > 1) || (playerList.size() == 0))
        {
            this.k.add(new GuiButton(0, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Enter"));
            this.k.add(new GuiButton(1, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Leave"));
        }
    }

    protected void actionPerformed(GuiButton button)
    {
        Party party = PartyController.instance().getParty(this.player);

        switch (button.id)
        {
            case 0:
                if ((this.controller != null) && (this.controller.getDungeon() != null) && (!this.controller.getDungeon().hasQueuedParty()))
                {
                    if (party != null)
                    {
                        int x = MathHelper.floor_double(this.controller.xCoord);
                        int y = MathHelper.floor_double(this.controller.yCoord);
                        int z = MathHelper.floor_double(this.controller.zCoord);
                        DungeonHandler.instance().queueParty(this.controller.getDungeon(), party, x, y, z, true);
                        this.g.displayGuiScreen((GuiScreen)null);
                    }
                    else
                    {
                        this.g.displayGuiScreen(new GuiCreateDungeonParty(this.player, this, this.controller));
                    }
                }

                break;

            case 1:
                if ((party != null) && (this.controller != null) && (this.controller.getDungeon() != null) && (this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player))))
                {
                    DungeonHandler.instance().disbandMember(this.controller.getDungeon(), PartyController.instance().getMember(this.player), true);
                }

                this.g.displayGuiScreen((GuiScreen)null);
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private boolean isQueuedParty(Party party)
    {
        if ((party != null) && (this.controller != null) && (this.controller.getDungeon() != null) && (this.controller.getDungeon().isActive()) && (this.controller.getDungeon().isQueuedParty(party)))
        {
            return true;
        }

        return false;
    }

    private boolean hasQueuedParty()
    {
        if ((this.controller != null) && (this.controller.getDungeon() != null) && (this.controller.getDungeon().isActive()) && (this.controller.getDungeon().hasQueuedParty()))
        {
            return true;
        }

        return false;
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

    public void drawScreen(int x, int y, float partialTick)
    {
        this.k.clear();
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        Party party = PartyController.instance().getParty(this.player);
        boolean isLeader = PartyController.instance().isLeader(this.player);
        GuiButton sendButton = new GuiButton(0, this.xParty - 59, this.yParty + 55, 55, 20, (party == null) || (party.getSize() <= 1) ? "Enter" : "Send");
        GuiButton leaveButton = new GuiButton(1, this.xParty + 6 - (hasQueuedParty() ? 32 : 0), this.yParty + 55, 55, 20, "Leave");

        if ((this.controller.getDungeon() != null) && (!this.controller.getDungeon().isActive()) && (this.controller != null))
        {
            this.k.add(sendButton);

            if ((party != null) && ((this.controller.getDungeon().isQueuedParty(party)) || (this.controller.getDungeon().hasAnyConqueredDungeon(party.getMembers())) || (!isLeader)))
            {
                sendButton.enabled = false;
            }
        }

        this.k.add(leaveButton);
        this.g.renderEngine.a();
        this.partyNameField = new GuiTextField(this.m, this.xParty - 63, this.yParty - 58, 125, 107);
        this.partyNameField.setFocused(false);
        this.partyNameField.setMaxStringLength(5000);
        this.partyNameField.drawTextBox();
        drawString(this.m, "§f§n§lWARNING!", centerX + 46, centerY + 10, 15658734);

        if ((this.controller != null) && (this.controller.hasDungeon()))
        {
            if (((party == null) && (!isLeader)) || ((isLeader) && (party.getSize() <= 1) && (party != null) && (!this.controller.getDungeon().hasAnyConqueredDungeon(party.getMembers())) && (!this.controller.getDungeon().hasQueuedParty())))
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
                this.g.renderEngine.a();
                this.description = new String[10];
                this.description[0] = "You are attempting the";
                this.description[1] = "Slider's Labyrinth on";
                this.description[2] = "your own. This dungeon";
                this.description[3] = "is a very dangerous";
                this.description[4] = "place, and you could";
                this.description[5] = "lose all your items as";
                this.description[6] = "a result.";
                this.description[7] = "";
                this.description[8] = "Are you prepared to";
                this.description[9] = "enter these depths?";
                int count = 0;

                for (String string : this.description)
                {
                    drawString(this.m, string, centerX + 70 - this.m.getStringWidth(string) / 2, centerY + 30 + count * 10, 15658734);
                    count++;
                }
            }
            else
            {
                this.g.renderEngine.a();
                ArrayList members = new ArrayList();

                if (party != null)
                {
                    members = party.getMembers();
                }

                if ((this.controller.getDungeon().hasQueuedParty()) && ((!this.controller.getDungeon().isQueuedParty(party)) || ((this.controller.getDungeon().isQueuedParty(party)) && (!this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player))))))
                {
                    this.description = new String[6];
                    this.description[0] = "Sorry, but at this time";
                    this.description[1] = "the dungeon is occupied";

                    if ((this.controller.getDungeon().isQueuedParty(party)) && (!this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player))))
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
                else if ((this.controller.getDungeon().isQueuedParty(party)) && (this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player))))
                {
                    if (this.controller.getDungeon().isActive())
                    {
                        this.description = new String[8];
                        this.description[0] = "Would you like to";
                        this.description[1] = "leave this dungeon?";
                        this.description[2] = "";
                        this.description[3] = (3 - this.controller.getDungeon().getMemberLeaves(PartyController.instance().getMember(this.player)) + "/3 Quits Left");
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
                else if (this.controller.getDungeon().hasAnyConqueredDungeon(members))
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
                    this.description[1] = "attempt the Slider's";
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
                    this.description[1] = "attempt the Slider's";
                    this.description[2] = "Labyrinth with your";
                    this.description[3] = "party? If so:";
                    this.description[4] = "";
                    this.description[5] = "Ask your leader";
                    this.description[6] = "to start a dungeon";
                    this.description[7] = "queue at this door.";
                }

                int count = 0;

                for (String string : this.description)
                {
                    drawString(this.m, string, centerX + 70 - this.m.getStringWidth(string) / 2, centerY + (isLeader ? 30 : 40) + count * 10, 15658734);
                    count++;
                }
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
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }
}

