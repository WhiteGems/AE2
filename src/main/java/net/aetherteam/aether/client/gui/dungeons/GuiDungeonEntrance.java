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
        this.drawString(this.fontRenderer, "\u00a7f\u00a7n\u00a7l警告!", centerX + 46, centerY + 10, 15658734);

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
                    this.description[0] = "抱歉, 此时此刻, 地牢";
                    this.description[1] = "已经被人闯入, 攻略者来自";

                    if (this.controller.getDungeon().isQueuedParty(party) && !this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player)))
                    {
                        this.description[2] = "你的公会";
                    } else this.description[2] = "其他公会";

                    this.description[3] = "";
                    this.description[4] = "请稍等一会";
                    this.description[5] = "儿再来试试";
                }
                else if (this.controller.getDungeon().isQueuedParty(party) && this.controller.getDungeon().hasMember(PartyController.instance().getMember(this.player)))
                {
                    if (this.controller.getDungeon().isActive())
                    {
                        this.description = new String[8];
                        this.description[0] = "你真的想要离开";
                        this.description[1] = "这个地牢?";
                        this.description[2] = "你还有";
                        this.description[3] = 3 - this.controller.getDungeon().getMemberLeaves(PartyController.instance().getMember(this.player)) + "/3 次离开机会";
                        this.description[4] = "";
                        this.description[5] = "在彻底重置之前";
                        this.description[6] = "每个地牢仅仅允许";
                        this.description[7] = "离开三次";
                    }
                    else
                    {
                        this.description = new String[7];
                        this.description[0] = "你的公会正排队";
                        this.description[1] = "进入地牢中";
                        this.description[2] = "";
                        this.description[3] = "";
                        this.description[4] = "请等待你的其他队友";
                        this.description[5] = "接受组队探险的";
                        this.description[6] = "邀请";
                    }
                }
                else if (this.controller.getDungeon().hasAnyConqueredDungeon(var18))
                {
                    this.description = new String[8];
                    this.description[0] = "抱歉, 该地牢正";
                    this.description[1] = "被你的公会成员";
                    this.description[2] = "努力攻略中";
                    this.description[3] = "";
                    this.description[4] = "";
                    this.description[5] = "";
                    this.description[6] = "请你去其他地方探索或者等待";
                    this.description[7] = "队友凯旋而归";
                }
                else if (isLeader)
                {
                    this.description = new String[7];
                    this.description[0] = "你是否想要和你的公会";
                    this.description[1] = "一起勇闯滑行者的迷宫?";
                    this.description[2] = "";
                    this.description[3] = "";
                    this.description[4] = "";
                    this.description[5] = "如果是这样的话, 请发送";
                    this.description[6] = "邀请给你的队友";
                }
                else
                {
                    this.description = new String[8];
                    this.description[0] = "你是否想要和你的公会";
                    this.description[1] = "一起勇闯滑行者的迷宫?";
                    this.description[2] = "";
                    this.description[3] = "";
                    this.description[4] = "";
                    this.description[5] = "如果是这样的话, 请发送";
                    this.description[6] = "邀请给你的队长, ";
                    this.description[7] = "让他申请进入地牢";
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
                this.description[0] = "你试图独闯滑行者的迷宫";
                this.description[1] = "这个迷宫危险无比";
                this.description[2] = "你随时可能付出生命";
                this.description[3] = "并且损失掉全部的物品";
                this.description[4] = "你将因此失去一切";
                this.description[5] = "但地牢深处有值得探索的宝藏";
                this.description[6] = "";
                this.description[7] = "";
                this.description[8] = "那么";
                this.description[9] = "你是否已经准备好进入地牢?";
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
