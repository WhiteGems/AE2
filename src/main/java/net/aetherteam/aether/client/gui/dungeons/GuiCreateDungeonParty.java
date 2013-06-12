package net.aetherteam.aether.client.gui.dungeons;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

import java.util.ArrayList;
import java.util.List;

import net.aetherteam.aether.client.gui.social.PartyData;
import net.aetherteam.aether.client.gui.social.dialogue.GuiDialogueBox;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.PartyType;
import net.aetherteam.aether.party.members.PartyMember;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class GuiCreateDungeonParty extends GuiScreen
{
    private final PartyData pm;
    private int backgroundTexture;
    private int partyCreatedTexture;
    private int partyX;
    private int partyY;
    private int partyW;
    private int partyH;
    Minecraft mc;
    private ArrayList partyType = new ArrayList();
    private int typeIndex = 0;
    private GuiButton typeButton;
    private GuiButton finishButton;
    private GuiButton backButton;
    private GuiTextField partyNameField;
    private String partyName = "";
    private EntityPlayer player;
    private GuiScreen parent;
    private TileEntityEntranceController controller;

    public GuiCreateDungeonParty(EntityPlayer player, GuiScreen parent, TileEntityEntranceController controller)
    {
        this(new PartyData(), player, parent);

        this.controller = controller;
    }

    public GuiCreateDungeonParty(PartyData pm, EntityPlayer player, GuiScreen parent)
    {
        this.parent = parent;
        this.partyType = new ArrayList();
        this.partyType.add("打开");
        this.partyType.add("关闭");
        this.partyType.add("私人");

        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/createParty.png");
        this.partyCreatedTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyCreated.png");
        this.partyW = 256;
        this.partyH = 256;
        updateScreen();
    }

    public void initGui()
    {
        updateScreen();
        this.buttonList.clear();

        this.typeButton = new GuiButton(1, this.partyX - 60, this.partyY - 16 - 28, 120, 20, "类型: " + (String) this.partyType.get(this.typeIndex));
        this.finishButton = new GuiButton(2, this.partyX - 60, this.partyY + 6 - 28, 120, 20, "开始冒险!");
        this.backButton = new GuiButton(0, this.partyX - 60, this.partyY + 81 - 28, 120, 20, "返回");

        this.buttonList.add(this.typeButton);
        this.buttonList.add(this.finishButton);

        this.buttonList.add(this.backButton);

        this.partyNameField = new GuiTextField(this.fontRenderer, this.partyX - 55, this.partyY - 64, 107, 16);
        this.partyNameField.setFocused(true);
        this.partyNameField.setMaxStringLength(22);
        this.partyNameField.setText(this.partyName);
        this.partyNameField.setEnableBackgroundDrawing(false);
    }

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
                break;
            case 1:
                this.typeIndex += 1;
                break;
            case 2:
                Party party = new Party(this.partyName, new PartyMember(this.player)).setType(PartyType.getTypeFromString((String) this.partyType.get(this.typeIndex)));
                boolean created = PartyController.instance().addParty(party, true);

                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyChange(true, this.partyName, this.player.username, this.player.skinUrl));
                PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendPartyTypeChange(this.partyName, PartyType.getTypeFromString((String) this.partyType.get(this.typeIndex))));

                if (!created)
                {
                    this.mc.displayGuiScreen(new GuiDialogueBox(this, "你成功创建公会!", "你的公会名称已经被占用, 请换一个", created));
                } else if ((this.controller != null) && (this.controller.getDungeon() != null) && (!this.controller.getDungeon().hasQueuedParty()))
                {
                    int x = MathHelper.floor_double(this.controller.xCoord);
                    int y = MathHelper.floor_double(this.controller.yCoord);
                    int z = MathHelper.floor_double(this.controller.zCoord);

                    DungeonHandler.instance().queueParty(this.controller.getDungeon(), party, x, y, z, true);
                    this.mc.displayGuiScreen(null);
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
        this.buttonList.clear();

        if (this.typeIndex > this.partyType.size() - 1)
        {
            this.typeIndex = 0;
        }
        this.typeButton = new GuiButton(1, this.partyX - 60, this.partyY - 16 - 28, 120, 20, "类型: " + (String) this.partyType.get(this.typeIndex));
        this.finishButton = new GuiButton(2, this.partyX - 60, this.partyY + 6 - 28, 120, 20, "开始冒险!");

        if (this.partyName.isEmpty())
        {
            this.finishButton.enabled = false;
        }
        this.buttonList.add(this.typeButton);
        this.buttonList.add(this.finishButton);
        this.buttonList.add(this.backButton);

        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int centerX = this.partyX - 70;
        int centerY = this.partyY - 84;

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.partyH);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);

        this.mc.renderEngine.resetBoundTexture();

        String headerName = "行动代号";

        drawString(this.fontRenderer, headerName, centerX + 68 - this.fontRenderer.getStringWidth(headerName) / 2, centerY + 5, 16777215);

        this.partyNameField.drawTextBox();

        super.drawScreen(x, y, partialTick);
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        if (this.partyNameField.isFocused())
        {
            this.partyNameField.textboxKeyTyped(charTyped, keyTyped);
            this.partyName = this.partyNameField.getText();
        } else if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        }

        super.keyTyped(charTyped, keyTyped);
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        this.partyNameField.mouseClicked(par1, par2, par3);

        super.mouseClicked(par1, par2, par3);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.partyX = (width / 2);
        this.partyY = (height / 2);

        if (this.partyNameField != null) this.partyNameField.updateCursorCounter();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.dungeons.GuiCreateDungeonParty
 * JD-Core Version:    0.6.2
 */
