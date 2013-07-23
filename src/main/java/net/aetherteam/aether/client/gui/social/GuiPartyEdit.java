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
import org.lwjgl.opengl.GL11;

public class GuiPartyEdit extends GuiScreen
{
    private final PartyData pm;
    private int backgroundTexture;
    private int easterTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private ArrayList partyType;
    private int typeIndex;
    private EntityPlayer player;
    private GuiButton typeButton;
    private String newPartyName;
    private GuiScreen parent;

    public GuiPartyEdit(EntityPlayer var1, GuiScreen var2)
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

    public GuiPartyEdit(PartyData var1, EntityPlayer var2, GuiScreen var3)
    {
        this.partyType = new ArrayList();
        this.typeIndex = 0;
        this.parent = var3;
        String var4 = PartyController.instance().getParty(var2).getType().name();

        if (var4 == "Open")
        {
            this.typeIndex = 0;
        }

        if (var4 == "Closed")
        {
            this.typeIndex = 1;
        }

        if (var4 == "Private")
        {
            this.typeIndex = 2;
        }

        this.partyType.add("Open");
        this.partyType.add("Closed");
        this.partyType.add("Private");
        this.player = var2;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = var1;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        ArrayList var5 = PartyController.instance().getParties();

        for (int var6 = 0; var6 < var5.size(); ++var6)
        {
            if (((Party)var5.get(var6)).getLeader().username.equals(var2.username))
            {
                this.newPartyName = ((Party)var5.get(var6)).getName();
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
        Party var1 = PartyController.instance().getParty(this.player);

        if (var1 != null)
        {
            this.typeButton = new GuiButton(4, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "Type: " + PartyController.instance().getParty(this.player).getType().name());
        }

        this.buttonList.add(this.typeButton);
        this.buttonList.add(new GuiButton(5, this.xParty - 60, this.yParty + 12 - 28, 120, 20, "Save"));
        this.buttonList.add(new GuiButton(3, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "Change Name"));
        this.buttonList.add(new GuiButton(2, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "Manage Permissions"));
        this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Manage Members"));
        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "Back"));
        ArrayList var2 = new ArrayList();

        for (int var3 = 0; var3 < var2.size(); ++var3)
        {
            if (((Party)var2.get(var3)).getLeader().username == this.player.username)
            {
                ((GuiButton)this.buttonList.get(1)).enabled = false;
            }
        }
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
    public void drawScreen(int var1, int var2, float var3)
    {
        this.drawDefaultBackground();
        this.buttonList.clear();
        this.typeButton = new GuiButton(4, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "Type: " + PartyController.instance().getParty(this.player).getType().name());
        this.buttonList.add(this.typeButton);
        this.buttonList.add(new GuiButton(3, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "Change Name"));
        this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "Manage Members"));
        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "Back"));
        ArrayList var4 = PartyController.instance().getParties();
        int var5;

        for (var5 = 0; var5 < var4.size(); ++var5)
        {
            if (((Party)var4.get(var5)).getLeader().username.equals(this.player.username) && ((Party)var4.get(var5)).getType().name() != this.partyType.get(this.typeIndex) && ((Party)var4.get(var5)).getName() == this.newPartyName)
            {
                ;
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        var5 = this.xParty - 70;
        int var6 = this.yParty - 84;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(var5, var6, 0, 0, 141, this.hParty);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        this.mc.renderEngine.resetBoundTexture();
        String var8 = "Manage Party";
        this.drawString(this.fontRenderer, var8, var5 + 69 - this.fontRenderer.getStringWidth(var8) / 2, var6 + 5, 16777215);
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
