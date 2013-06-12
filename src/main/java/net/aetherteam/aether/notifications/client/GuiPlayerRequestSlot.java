package net.aetherteam.aether.notifications.client;

import java.util.ArrayList;

import net.aetherteam.aether.notifications.NotificationHandler;
import net.aetherteam.aether.notifications.NotificationType;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

public class GuiPlayerRequestSlot extends Gui
{
    protected static final String AVAILABLE_TEXT = "可用";
    protected static final String INPARTY_TEXT = "已加入公会";
    protected static final String PENDING_TEXT = "请求审理中";
    private static final int AVAILABLE_COLOUR = 6750054;
    private static final int INPARTY_COLOUR = 16711680;
    private static final int PENDING_COLOUR = 16756516;
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

    public GuiPlayerRequestSlot(String username, String skinURL, int id, int x, int y, int width, int height)
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

        boolean inParty = PartyController.instance().inParty(this.username);

        String subText = "";
        int textColour = 0;

        Party party = PartyController.instance().getParty(this.username);

        boolean pending = NotificationHandler.instance().hasSentToBefore(this.username, NotificationType.PARTY_REQUEST, mc.thePlayer.username);

        if (inParty)
        {
            subText = "已加入公会";
            textColour = 16711680;
        } else if (pending)
        {
            subText = "请求审理中";
            textColour = 16756516;
        } else
        {
            subText = "可用";
            textColour = 6750054;
        }

        fontRenderer.drawString(subText, (int) ((x + height) / 0.75F), (int) ((y + 12.0F) / 0.75F), textColour);

        GL11.glPopMatrix();
    }

    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return (this.enabled) && (this.drawButton) && (par2 >= this.xPosition) && (par3 >= this.yPosition) && (par2 < this.xPosition + this.width) && (par3 < this.yPosition + this.height);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.notifications.client.GuiPlayerRequestSlot
 * JD-Core Version:    0.6.2
 */