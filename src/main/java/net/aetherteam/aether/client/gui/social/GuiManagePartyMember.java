package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

import java.util.List;

import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
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
    Minecraft f;
    private String username;
    private GuiScreen parent;
    private String skinUrl;
    private EntityPlayer player;
    private GuiButton transferButton;
    private GuiButton moderatorButton;
    private GuiButton kickButton;
    private GuiButton banButton;

    public GuiManagePartyMember(EntityPlayer player, String username, String skinUrl, GuiScreen parent)
    {
        this(new PartyData(), player, username, skinUrl, parent);
    }

    public GuiManagePartyMember(PartyData pm, EntityPlayer player, String username, String skinUrl, GuiScreen parent)
    {
        this.parent = parent;
        this.username = username;
        this.skinUrl = skinUrl;
        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        updateScreen();
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        }
    }

    public void initGui()
    {
        updateScreen();
        this.buttonList.clear();

        this.transferButton = new GuiButton(5, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "移交队长");
        this.moderatorButton = new GuiButton(4, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "添加管理");
        this.kickButton = new GuiButton(5, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "踢出");
        this.banButton = new GuiButton(6, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "封禁");

        this.transferButton.enabled = false;
        this.moderatorButton.enabled = false;

        this.buttonList.add(this.transferButton);
        this.buttonList.add(this.moderatorButton);
        this.buttonList.add(this.kickButton);
        this.buttonList.add(this.banButton);

        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "返回"));
    }

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

                if ((party != null) && (kickedMember != null))
                {
                    PartyController.instance().leaveParty(party, kickedMember, true);
                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyMemberChange(false, party.getName(), this.username, this.skinUrl));
                    this.mc.displayGuiScreen(this.parent);
                }
                break;
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);

        String name = "管理权限";

        drawString(this.fontRenderer, name, centerX + 69 - this.fontRenderer.getStringWidth(name) / 2, centerY + 5, 16777215);
        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiManagePartyMember
 * JD-Core Version:    0.6.2
 */
