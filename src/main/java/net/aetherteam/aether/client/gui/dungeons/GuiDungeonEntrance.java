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
import org.lwjgl.opengl.GL11;

public class GuiDungeonEntrance extends GuiScreen
{
    private int backgroundTexture;
    private int easterTexture;
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

    public GuiDungeonEntrance(EntityPlayer var1, GuiScreen var2, TileEntityEntranceController var3)
    {
        this.parent = var2;
        this.player = var1;
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
        this.controller = var3;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.updateScreen();
        this.buttonList.clear();
        List var1 = this.mc.thePlayer.sendQueue.playerInfoList;

        if (var1.size() > 1 || var1.size() == 0)
        {
            this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Enter"));
            this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Leave"));
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        Party var2 = PartyController.instance().getParty(this.player);

        switch (var1.id)
        {
            case 0:
                if (this.controller != null && this.controller.getDungeon() != null && !this.controller.getDungeon().hasQueuedParty())
                {
                    if (var2 != null)
                    {
                        int var3 = MathHelper.floor_double((double)this.controller.xCoord);
                        int var4 = MathHelper.floor_double((double)this.controller.yCoord);
                        int var5 = MathHelper.floor_double((double)this.controller.zCoord);
                        DungeonHandler.instance().queueParty(this.controller.getDungeon(), var2, var3, var4, var5, true);
                        this.mc.displayGuiScreen((GuiScreen)null);
                    }
                    else
                    {
                        this.mc.displayGuiScreen(new GuiCreateDungeonParty(this.player, this, this.controller));
                    }
                }

                break;

            case 1:
                if (var2 != null && this.controller != null && this.controller.getDungeon() != null && this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player)))
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

    private boolean isQueuedParty(Party var1)
    {
        return var1 != null && this.controller != null && this.controller.getDungeon() != null && this.controller.getDungeon().isActive() && this.controller.getDungeon().isQueuedParty(var1);
    }

    private boolean hasQueuedParty()
    {
        return this.controller != null && this.controller.getDungeon() != null && this.controller.getDungeon().isActive() && this.controller.getDungeon().hasQueuedParty();
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
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        this.buttonList.clear();
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int var4 = this.xParty - 70;
        int var5 = this.yParty - 84;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(var4, var5, 0, 0, 141, this.hParty);
        Party var7 = PartyController.instance().getParty(this.player);
        boolean var8 = PartyController.instance().isLeader(this.player);
        GuiButton var9 = new GuiButton(0, this.xParty - 59, this.yParty + 55, 55, 20, var7 != null && var7.getSize() > 1 ? "Send" : "Enter");
        GuiButton var10 = new GuiButton(1, this.xParty + 6 - (this.hasQueuedParty() ? 32 : 0), this.yParty + 55, 55, 20, "Leave");

        if (this.controller.getDungeon() != null && !this.controller.getDungeon().isActive() && this.controller != null)
        {
            this.buttonList.add(var9);

            if (var7 != null && (this.controller.getDungeon().isQueuedParty(var7) || this.controller.getDungeon().hasAnyConqueredDungeon(var7.getMembers()) || !var8))
            {
                var9.enabled = false;
            }
        }

        this.buttonList.add(var10);
        this.mc.renderEngine.resetBoundTexture();
        this.partyNameField = new GuiTextField(this.fontRenderer, this.xParty - 63, this.yParty - 58, 125, 107);
        this.partyNameField.setFocused(false);
        this.partyNameField.setMaxStringLength(5000);
        this.partyNameField.drawTextBox();
        this.drawString(this.fontRenderer, "\u00a7f\u00a7n\u00a7lWARNING!", var4 + 46, var5 + 10, 15658734);

        if (this.controller != null && this.controller.hasDungeon())
        {
            int var14;

            if ((var7 != null || var8) && (!var8 || var7.getSize() > 1 || var7 == null || this.controller.getDungeon().hasAnyConqueredDungeon(var7.getMembers()) || this.controller.getDungeon().hasQueuedParty()))
            {
                this.mc.renderEngine.resetBoundTexture();
                ArrayList var18 = new ArrayList();

                if (var7 != null)
                {
                    var18 = var7.getMembers();
                }

                if (this.controller.getDungeon().hasQueuedParty() && (!this.controller.getDungeon().isQueuedParty(var7) || this.controller.getDungeon().isQueuedParty(var7) && !this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player))))
                {
                    this.description = new String[6];
                    this.description[0] = "Sorry, but at this time";
                    this.description[1] = "the dungeon is occupied";

                    if (this.controller.getDungeon().isQueuedParty(var7) && !this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player)))
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
                else if (this.controller.getDungeon().isQueuedParty(var7) && this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player)))
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
                else if (var8)
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
                var14 = var19.length;

                for (int var20 = 0; var20 < var14; ++var20)
                {
                    String var16 = var19[var20];
                    this.drawString(this.fontRenderer, var16, var4 + 70 - this.fontRenderer.getStringWidth(var16) / 2, var5 + (var8 ? 30 : 40) + var17 * 10, 15658734);
                    ++var17;
                }
            }
            else
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
                this.mc.renderEngine.resetBoundTexture();
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
                int var11 = 0;
                String[] var12 = this.description;
                int var13 = var12.length;

                for (var14 = 0; var14 < var13; ++var14)
                {
                    String var15 = var12[var14];
                    this.drawString(this.fontRenderer, var15, var4 + 70 - this.fontRenderer.getStringWidth(var15) / 2, var5 + 30 + var11 * 10, 15658734);
                    ++var11;
                }
            }
        }

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
