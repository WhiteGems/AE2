package net.aetherteam.aether.client.gui.social;

import net.aetherteam.aether.party.Party;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
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
    public boolean selected = false;
    private static final int LEADER_TEXT_COLOR = 26367;
    public Party party;

    public GuiPartySlot(Party var1, int var2, int var3, int var4, int var5, int var6)
    {
        this.width = var5;
        this.height = var6;
        this.enabled = true;
        this.drawButton = true;
        this.id = var2;
        this.xPosition = var3;
        this.yPosition = var4;
        this.party = var1;
    }

    public void drawPartySlot(int var1, int var2, int var3, int var4)
    {
        this.xPosition = var1;
        this.yPosition = var2;
        this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + var3, this.yPosition + var4, this.selected ? -10439830 : -13421773, this.selected ? -11563178 : -11184811);
        Minecraft var5 = Minecraft.getMinecraft();
        FontRenderer var6 = var5.fontRenderer;
        var5.renderEngine.resetBoundTexture();
        var6.drawStringWithShadow(this.party.getName(), var1 + var4 - 19, var2 + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        var6.drawString(this.party.getLeader().username, (int)(((float)var1 + (float)var4) / 0.75F - 22.0F + (float)(this.party.getType().name().length() * 6)), (int)(((float)var2 + 12.0F) / 0.75F), 26367);
        var6.drawString(this.party.getType().name(), (int)(((float)var1 + (float)var4) / 0.75F) - 25, (int)(((float)var2 + 12.0F) / 0.75F), this.party.getType().getDisplayColor());
        GL11.glPopMatrix();
    }

    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        return this.enabled && this.drawButton && var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
    }
}
