package net.aetherteam.aether.client.gui.social;

import java.util.ArrayList;

import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.util.StringUtils;
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
    public String username;
    public String skinURL;

    public GuiPlayerSlot(String username, String skinURL, int id, int x, int y, int width, int height)
    {
        this.skinURL = skinURL;
        this.username = username;
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

        drawGradientRect(this.xPosition, this.yPosition, this.xPosition + width, this.yPosition + height, this.selected ? -10439830 : -13421773, this.selected ? -11563178 : -11184811);

        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRenderer;

        ArrayList partyList = PartyController.instance().getParties();
        ArrayList playerStringList = new ArrayList();

        for (int i = 0; i < partyList.size(); i++)
        {
            PartyMember member = PartyController.instance().getMember(this.username);

            if (((Party) partyList.get(i)).hasMember(member))
            {
                playerStringList.add(this.username);
            }
        }

        int icon = mc.renderEngine.getTextureForDownloadableImage("http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(this.username) + ".png", "/mob/char.png");

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, icon);
        GL11.glEnable(3553);

        float u = 0.125F;
        float v = 0.25F;
        float u1 = 0.25F;
        float v1 = 0.5F;
        GL11.glBegin(7);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex2f(x + 2, y + 2);
        GL11.glTexCoord2f(u, v1);
        GL11.glVertex2f(x + 2, y + 18);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x + 18, y + 18);
        GL11.glTexCoord2f(u1, v);
        GL11.glVertex2f(x + 18, y + 2);
        GL11.glEnd();

        mc.renderEngine.resetBoundTexture();

        fontRenderer.drawStringWithShadow(this.username, x + height, y + 2, 15066597);

        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        if (playerStringList.contains(this.username))
            fontRenderer.drawString("在线", (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 6750054);
        else fontRenderer.drawString("离线", (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 16711680);
        GL11.glPopMatrix();
    }

    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return (this.enabled) && (this.drawButton) && (par2 >= this.xPosition) && (par3 >= this.yPosition) && (par2 < this.xPosition + this.width) && (par3 < this.yPosition + this.height);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.social.GuiPlayerSlot
 * JD-Core Version:    0.6.2
 */