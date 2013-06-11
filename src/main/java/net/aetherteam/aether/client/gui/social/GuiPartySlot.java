package net.aetherteam.aether.client.gui.social;

import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyType;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderEngine;
import org.lwjgl.opengl.GL11;

public class GuiPartySlot extends Gui
{
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean drawButton;
    protected boolean field_82253_i;
    public boolean selected;
    private static final int LEADER_TEXT_COLOR = 26367;
    public Party party;

    public GuiPartySlot(Party party, int id, int x, int y, int width, int height)
    {
        this.selected = false;
        this.width = width;
        this.height = height;
        this.enabled = true;
        this.drawButton = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.party = party;
    }

    public void drawPartySlot(int x, int y, int width, int height)
    {
        this.xPosition = x;
        this.yPosition = y;

        drawGradientRect(this.xPosition, this.yPosition, this.xPosition + width, this.yPosition + height, this.selected ? -10439830 : -13421773, this.selected ? -11563178 : -11184811);

        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRenderer;

        mc.renderEngine.resetBoundTexture();

        fontRenderer.drawStringWithShadow(this.party.getName(), x + height - 19, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        fontRenderer.drawString(this.party.getLeader().username, (int) ((x + height) / 0.75F - 22.0F + this.party.getType().name().length() * 6), (int) ((y + 12.0F) / 0.75F), 26367);
        fontRenderer.drawString(this.party.getType().name(), (int) ((x + height) / 0.75F) - 25, (int) ((y + 12.0F) / 0.75F), this.party.getType().getDisplayColor());
        GL11.glPopMatrix();
    }

    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return (this.enabled) && (this.drawButton) && (par2 >= this.xPosition) && (par3 >= this.yPosition) && (par2 < this.xPosition + this.width) && (par3 < this.yPosition + this.height);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiPartySlot
 * JD-Core Version:    0.6.2
 */