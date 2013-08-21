package net.aetherteam.aether.client.gui.social;

import java.util.ArrayList;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class GuiPlayerSlot extends Gui
{
    protected static final String ONLINE_TEXT = "在线";
    protected static final String OFFLINE_TEXT = "离线";
    private static final int ONLINE_TEXT_COLOR = 6750054;
    private static final int OFFLINE_TEXT_COLOR = 16711680;
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
    public PartyMember partyMember;

    public GuiPlayerSlot(PartyMember partyMember, int id, int x, int y, int width, int height)
    {
        this.partyMember = partyMember;
        this.selected = false;
        this.width = width;
        this.height = height;
        this.enabled = true;
        this.drawButton = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
    }

    public void drawPlayerSlot(int x, int y, int width, int height)
    {
        this.xPosition = x;
        this.yPosition = y;
        this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + width, this.yPosition + height, this.selected ? -10439830 : -13421773, this.selected ? -11563178 : -11184811);
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRenderer;
        ArrayList partyList = PartyController.instance().getParties();
        ArrayList playerStringList = new ArrayList();

        for (int u = 0; u < partyList.size(); ++u)
        {
            if (((Party)partyList.get(u)).hasMember(this.partyMember))
            {
                playerStringList.add(this.partyMember.username);
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.func_110577_a(((AbstractClientPlayer)this.partyMember.getPlayer()).func_110306_p());
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float var13 = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(var13, v);
        GL11.glVertex2f((float)(x + 2), (float)(y + 2));
        GL11.glTexCoord2f(var13, v1);
        GL11.glVertex2f((float)(x + 2), (float)(y + 18));
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f((float)(x + 18), (float)(y + 18));
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f((float)(x + 18), (float)(y + 2));
        GL11.glEnd();
        fontRenderer.drawStringWithShadow(this.partyMember.username, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);

        if (playerStringList.contains(this.partyMember.username))
        {
            fontRenderer.drawString("在线", (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 6750054);
        }
        else
        {
            fontRenderer.drawString("离线", (int)(((float)x + (float)height) / 0.75F), (int)(((float)y + 12.0F) / 0.75F), 16711680);
        }

        GL11.glPopMatrix();
    }

    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
    }
}
