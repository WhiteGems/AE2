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
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiManagePartyMember extends GuiScreen
{
    private static final ResourceLocation TEXTURE_PARTYMAIN = new ResourceLocation("aether", "textures/gui/partyMain.png");
    private final PartyData pm;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private String username;
    private GuiScreen parent;
    private EntityPlayer player;
    private GuiButton transferButton;
    private GuiButton moderatorButton;
    private GuiButton kickButton;
    private GuiButton banButton;

    public GuiManagePartyMember(EntityPlayer player, String username, GuiScreen parent)
    {
        this(new PartyData(), player, username, parent);
    }

    public GuiManagePartyMember(PartyData pm, EntityPlayer player, String username, GuiScreen parent)
    {
        this.parent = parent;
        this.username = username;
        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.wParty = 256;
        this.hParty = 256;
        this.updateScreen();
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
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
                break;

            case 5:
                Party party = PartyController.instance().getParty(this.player);
                PartyMember kickedMember = PartyController.instance().getMember(this.username);

                if (party != null && kickedMember != null)
                {
                    PartyController.instance().leaveParty(party, kickedMember, true);
                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyMemberChange(false, party.getName(), this.username));
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
    public void drawScreen(int x, int y, float partialTick)
    {
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_PARTYMAIN);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        this.mc.renderEngine.func_110577_a(TEXTURE_PARTYMAIN);
        String name = "Manage Permissions";
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
