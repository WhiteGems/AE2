package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiManagePartyMember extends GuiScreen
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
    private String username;
    private GuiScreen parent;
    private String skinUrl;
    private EntityPlayer player;
    private GuiButton transferButton;
    private GuiButton moderatorButton;
    private GuiButton kickButton;
    private GuiButton banButton;

    public GuiManagePartyMember(EntityPlayer var1, String var2, String var3, GuiScreen var4)
    {
        this(new PartyData(), var1, var2, var3, var4);
    }

    public GuiManagePartyMember(PartyData var1, EntityPlayer var2, String var3, String var4, GuiScreen var5)
    {
        this.parent = var5;
        this.username = var3;
        this.skinUrl = var4;
        this.player = var2;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = var1;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
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
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.updateScreen();
        this.buttonList.clear();
        this.transferButton = new GuiButton(5, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "Transfer Ownership");
        this.moderatorButton = new GuiButton(4, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "Make Moderator");
        this.kickButton = new GuiButton(5, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "Kick");
        this.banButton = new GuiButton(6, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "Ban");
        this.transferButton.enabled = false;
        this.moderatorButton.enabled = false;
        this.buttonList.add(this.transferButton);
        this.buttonList.add(this.moderatorButton);
        this.buttonList.add(this.kickButton);
        this.buttonList.add(this.banButton);
        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "Back"));
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

            case 5:
                Party var2 = PartyController.instance().getParty(this.player);
                PartyMember var3 = PartyController.instance().getMember(this.username);

                if (var2 != null && var3 != null)
                {
                    PartyController.instance().leaveParty(var2, var3, true);
                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyMemberChange(false, var2.getName(), this.username, this.skinUrl));
                    this.mc.displayGuiScreen(this.parent);
                }
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
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int var4 = this.xParty - 70;
        int var5 = this.yParty - 84;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(var4, var5, 0, 0, 141, this.hParty);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        String var7 = "Manage Permissions";
        this.drawString(this.fontRenderer, var7, var4 + 69 - this.fontRenderer.getStringWidth(var7) / 2, var5 + 5, 16777215);
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
