package net.aetherteam.aether.client.gui.social;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

import java.util.ArrayList;
import java.util.List;

import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.PartyType;
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

public class GuiPartyEdit extends GuiScreen
{
    private final PartyData pm;
    private int backgroundTexture;
    private int easterTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    Minecraft f;
    private ArrayList partyType = new ArrayList();
    private int typeIndex = 0;
    private EntityPlayer player;
    private GuiButton typeButton;
    private String newPartyName;
    private GuiScreen parent;

    public GuiPartyEdit(EntityPlayer player, GuiScreen parent)
    {
        this(new PartyData(), player, parent);
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

    public GuiPartyEdit(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;

        String name = PartyController.instance().getParty(player).getType().name();

        if (name == "打开")
        {
            this.typeIndex = 0;
        }
        if (name == "关闭")
        {
            this.typeIndex = 1;
        }
        if (name == "私人")
        {
            this.typeIndex = 2;
        }
        this.partyType.add("打开");
        this.partyType.add("关闭");
        this.partyType.add("私人");

        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.easterTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyMain.png");
        this.wParty = 256;
        this.hParty = 256;
        ArrayList partyList = PartyController.instance().getParties();

        for (int i = 0; i < partyList.size(); i++)
        {
            if (((Party) partyList.get(i)).getLeader().username.equals(player.username))
                this.newPartyName = ((Party) partyList.get(i)).getName();
        }
        updateScreen();
    }

    public void initGui()
    {
        updateScreen();
        this.buttonList.clear();

        Party party = PartyController.instance().getParty(this.player);

        if (party != null)
        {
            this.typeButton = new GuiButton(4, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "Type: " + PartyController.instance().getParty(this.player).getType().name());
        }

        this.buttonList.add(this.typeButton);
        this.buttonList.add(new GuiButton(5, this.xParty - 60, this.yParty + 12 - 28, 120, 20, "保存"));
        this.buttonList.add(new GuiButton(3, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "重命名"));
        this.buttonList.add(new GuiButton(2, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "管理权限"));
        this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty + 8 - 28, 120, 20, "管理会员"));
        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "返回"));

        ArrayList partyList = new ArrayList();

        for (int i = 0; i < partyList.size(); i++)
            if (((Party) partyList.get(i)).getLeader().username == this.player.username)
                ((GuiButton) this.buttonList.get(1)).enabled = false;
    }

    protected void actionPerformed(GuiButton btn)
    {
        switch (btn.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiManageMembers(this.player, this));
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiEditPartyName(this.player, this));
                break;
            case 4:
                this.typeIndex += 1;

                if (this.typeIndex > this.partyType.size() - 1)
                {
                    this.typeIndex = 0;
                }

                PartyController.instance().getParty(this.player).setType(PartyType.getTypeFromString((String) this.partyType.get(this.typeIndex)));

                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyTypeChange(PartyController.instance().getParty(this.player).getName(), PartyType.getTypeFromString((String) this.partyType.get(this.typeIndex))));
            case 2:
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        drawDefaultBackground();

        this.buttonList.clear();

        this.typeButton = new GuiButton(4, this.xParty - 60, this.yParty + 30 - 28, 120, 20, "Type: " + PartyController.instance().getParty(this.player).getType().name());

        this.buttonList.add(this.typeButton);

        this.buttonList.add(new GuiButton(3, this.xParty - 60, this.yParty - 36 - 28, 120, 20, "重命名"));
        this.buttonList.add(new GuiButton(1, this.xParty - 60, this.yParty - 14 - 28, 120, 20, "管理会员"));
        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 81 - 28, 120, 20, "返回"));

        ArrayList partyList = PartyController.instance().getParties();

        for (int i = 0; i < partyList.size(); i++)
        {
            if ((!((Party) partyList.get(i)).getLeader().username.equals(this.player.username)) ||
                    (((Party) partyList.get(i)).getType().name() == this.partyType.get(this.typeIndex)) || (((Party) partyList.get(i)).getName() != this.newPartyName))
                ;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.backgroundTexture);
        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        GL11.glBindTexture(3553, this.backgroundTexture);

        this.mc.renderEngine.resetBoundTexture();

        String name = "管理公会";

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
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiPartyEdit
 * JD-Core Version:    0.6.2
 */
