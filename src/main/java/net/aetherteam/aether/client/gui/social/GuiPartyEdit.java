package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import java.util.ArrayList;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.PartyType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiPartyEdit extends GuiScreen
{
    private static final ResourceLocation TEXTURE_PARTYMAIN = new ResourceLocation("aether", "textures/gui/partyMain.png");
    private final PartyData pm;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private ArrayList<String> partyType;
    private int typeIndex = 0;
    private EntityPlayer player;
    private GuiButton typeButton;
    private String newPartyName;
    private GuiScreen parent;

    public GuiPartyEdit(EntityPlayer player, GuiScreen parent)
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

    public GuiPartyEdit(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.partyType = new ArrayList();
        this.parent = parent;
        String name = PartyController.instance().getParty(player).getType().name();

        if (name.equals("OPEN"))
        {
            this.typeIndex = 0;
        }
        if (name.equals("CLOSE"))
        {
            this.typeIndex = 1;
        }
        if (name.equals("PRIVATE"))
        {
            this.typeIndex = 2;
        }
        this.partyType.add("公开");
        this.partyType.add("关闭");
        this.partyType.add("私有");

        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.wParty = 256;
        this.hParty = 256;
        ArrayList partyList = PartyController.instance().getParties();

        for (int i = 0; i < partyList.size(); ++i)
        {
            if (((Party)partyList.get(i)).getLeader().username.equals(player.username))
            {
                this.newPartyName = ((Party)partyList.get(i)).getName();
            }
        }

        this.updateScreen();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.updateScreen();
        this.buttonList.clear();
        Party party = PartyController.instance().getParty(this.player);

        if (party != null)
        {
            this.typeButton = new GuiButton(4, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "类型: " + PartyController.instance().getParty(this.player).getType().realname);
        }

        this.buttonList.add(this.typeButton);
        this.buttonList.add(new GuiButton(5, this.xParty - 60, this.yParty + 12 - 28, 120, 20, "保存"));
        this.buttonList.add(new GuiButton(3, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "重命名"));
        this.buttonList.add(new GuiButton(2, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "管理权限"));
        this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "管理会员"));
        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "返回"));
        ArrayList partyList = new ArrayList();

        for (int i = 0; i < partyList.size(); ++i)
        {
            if (((Party)partyList.get(i)).getLeader().username == this.player.username)
            {
                ((GuiButton)this.buttonList.get(1)).enabled = false;
            }
        }
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
                break;

            case 1:
                this.mc.displayGuiScreen(new GuiManageMembers(this.player, this));

            case 2:
            default:
                break;

            case 3:
                this.mc.displayGuiScreen(new GuiEditPartyName(this.player, this));
                break;

            case 4:
                ++this.typeIndex;

                if (this.typeIndex > this.partyType.size() - 1)
                {
                    this.typeIndex = 0;
                }

                PartyController.instance().getParty(this.player).setType(PartyType.getTypeFromString((String)this.partyType.get(this.typeIndex)));
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyTypeChange(PartyController.instance().getParty(this.player).getName(), PartyType.getTypeFromString((String)this.partyType.get(this.typeIndex))));
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
        this.drawDefaultBackground();
        this.buttonList.clear();

        this.typeButton = new GuiButton(4, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "类型: " + PartyController.instance().getParty(this.player).getType().realname);

        this.buttonList.add(this.typeButton);
        this.buttonList.add(new GuiButton(3, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "重命名"));
        this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "管理会员"));
        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "返回"));
        ArrayList partyList = PartyController.instance().getParties();
        int centerX;

        for (centerX = 0; centerX < partyList.size(); ++centerX)
        {
            if (((Party)partyList.get(centerX)).getLeader().username.equals(this.player.username) && ((Party)partyList.get(centerX)).getType().name() != this.partyType.get(this.typeIndex) && ((Party)partyList.get(centerX)).getName() == this.newPartyName)
            {
                ;
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_PARTYMAIN);
        centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        this.mc.renderEngine.func_110577_a(TEXTURE_PARTYMAIN);
        String name = "管理公会";
        this.drawString(this.fontRenderer, name, centerX + 69 - this.fontRenderer.getStringWidth(name) / 2, centerY + 5, 16777215);
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
