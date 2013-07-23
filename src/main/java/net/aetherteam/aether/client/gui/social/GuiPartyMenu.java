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

    public GuiPartyMenu(EntityPlayer var1, GuiScreen var2)
    {
        this(new PartyData(), var1, var2);
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

    public GuiPartyMenu(PartyData var1, EntityPlayer var2, GuiScreen var3)
    {
        this.parent = var3;
        this.player = var2;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = var1;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.partyCreatedTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyCreated.png");
        this.wMenu = 256;
        this.hMenu = 256;
        this.updateScreen();
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
                Party var2 = PartyController.instance().getParty(this.player);
                PartyMember var3 = PartyController.instance().getMember(this.player);
                boolean var4 = var2.isLeader(var3);

                if (var4)
                {
                    PartyController.instance().removeParty(var2, true);
                }
                else
                {
                    PartyController.instance().leaveParty(var2, var3, true);
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
    public void drawScreen(int var1, int var2, float var3)
    {
        this.buttonList.clear();

        boolean isLeader = PartyController.instance().isLeader(this.player);
        boolean inParty = PartyController.instance().inParty(this.player);

        this.createPartyButton = new GuiButton(5, this.xMenu - 60, this.yMenu - 36 - 28, 120, 20, "创建公会");
        this.joinPartyButton = new GuiButton(4, this.xMenu - 60, this.yMenu - 14 - 28, 120, 20, "加入已有公会");
        this.membersButton = new GuiButton(7, this.xMenu - 60, this.yMenu + 30 - (inParty ? 53 : 28), 120, 20, "会员");

        this.editPartyButton = new GuiButton(3, this.xMenu - 60, this.yMenu + 30 - (inParty ? 53 : 28), 120, 20, "管理公会");
        this.leavePartyButton = new GuiButton(6, this.xMenu - 60, this.yMenu + 74 - (inParty ? 53 : 28), 120, 20, (isLeader ? "解散" : "离开") + "公会");
        this.addMemberButton = new GuiButton(8, this.xMenu - 60, this.yMenu + 52 - (inParty ? 53 : 28), 120, 20, isLeader ? "增加成员" : "发送请求");

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

        Party var6 = PartyController.instance().getParty(PartyController.instance().getMember(this.player));

        if (var6 != null && DungeonHandler.instance().isInDungeon(var6))
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

        this.buttonList.add(new GuiButton(2, this.xMenu - 60, this.yMenu + 8 - (inParty ? 53 : 28), 120, 20, "公会列表"));

        if (isLeader)
        {
            this.buttonList.add(this.editPartyButton);
        }

        this.buttonList.add(new GuiButton(0, this.xMenu - 60, this.yMenu + 81 - 28, 120, 20, "返回"));

        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, inParty ? this.partyCreatedTexture : this.backgroundTexture);
        int var7 = this.xMenu - 70;
        int var8 = this.yMenu - 84;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(var7, var8, 0, 0, 141, this.hMenu);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        this.mc.renderEngine.resetBoundTexture();
        String name = "";

        if (inParty)
        {
            name = "你的公会:";
        } else name = "公会菜单";

        if (!isLeader)
        {
            name = "加入公会:";
        }

        if (!isLeader)
        {
            name = "Joined Party:";
        }

        this.drawString(this.fontRenderer, name, var7 + 69 - this.fontRenderer.getStringWidth(name) / 2, var8 + 5, 16777215);

        if (inParty && var6 != null)
        {
            String var11 = var6.getName();
            this.drawString(this.fontRenderer, var11, var7 + 70 - this.fontRenderer.getStringWidth(var11) / 2, var8 + 20, 16777215);
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
        this.xMenu = var2 / 2;
        this.yMenu = var3 / 2;
    }
}