package net.aetherteam.aether.client.gui.social;

import java.util.ArrayList;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
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

    public GuiPlayerSlot(String var1, String var2, int var3, int var4, int var5, int var6, int var7)
    {
        this.skinURL = var2;
        this.username = var1;
        this.selected = false;
        this.width = var6;
        this.height = var7;
        this.enabled = true;
        this.drawButton = true;
        this.id = var3;
        this.xPosition = var4;
        this.yPosition = var5;
    }

    public void drawPlayerSlot(int x, int y, int var3, int height)
    {
        this.xPosition = x;
        this.yPosition = y;
        this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + var3, this.yPosition + height, this.selected ? -10439830 : -13421773, this.selected ? -11563178 : -11184811);
        Minecraft var5 = Minecraft.getMinecraft();
        FontRenderer fontRenderer = var5.fontRenderer;
        ArrayList var7 = PartyController.instance().getParties();
        ArrayList playerStringList = new ArrayList();
        int var9;

        for (var9 = 0; var9 < var7.size(); ++var9)
        {
            PartyMember var10 = PartyController.instance().getMember(this.username);

            if (((Party)var7.get(var9)).hasMember(var10))
            {
                playerStringList.add(this.username);
            }
        }

        var9 = var5.renderEngine.getTextureForDownloadableImage("http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(this.username) + ".png", "/mob/char.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var9);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float var14 = 0.125F;
        float var11 = 0.25F;
        float var12 = 0.25F;
        float var13 = 0.5F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(var14, var11);
        GL11.glVertex2f((float)(x + 2), (float)(y + 2));
        GL11.glTexCoord2f(var14, var13);
        GL11.glVertex2f((float)(x + 2), (float)(y + 18));
        GL11.glTexCoord2f(var12, var13);
        GL11.glVertex2f((float)(x + 18), (float)(y + 18));
        GL11.glTexCoord2f(var12, var11);
        GL11.glVertex2f((float)(x + 18), (float)(y + 2));
        GL11.glEnd();
        var5.renderEngine.resetBoundTexture();
        fontRenderer.drawStringWithShadow(this.username, x + height, y + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);
        if (playerStringList.contains(this.username))
            fontRenderer.drawString("在线", (int) ((x + this.height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 6750054);
        else fontRenderer.drawString("离线", (int) ((x + this.height) / 0.75F), (int) ((y + 12.0F) / 0.75F), 16711680);
        GL11.glPopMatrix();
    }

    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        return this.enabled && this.drawButton && var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
    }
}
