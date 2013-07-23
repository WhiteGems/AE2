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
    protected static final String ONLINE_TEXT = "ONLINE";
    protected static final String OFFLINE_TEXT = "OFFLINE";
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

    public void drawPlayerSlot(int var1, int var2, int var3, int var4)
    {
        this.xPosition = var1;
        this.yPosition = var2;
        this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + var3, this.yPosition + var4, this.selected ? -10439830 : -13421773, this.selected ? -11563178 : -11184811);
        Minecraft var5 = Minecraft.getMinecraft();
        FontRenderer var6 = var5.fontRenderer;
        ArrayList var7 = PartyController.instance().getParties();
        ArrayList var8 = new ArrayList();
        int var9;

        for (var9 = 0; var9 < var7.size(); ++var9)
        {
            PartyMember var10 = PartyController.instance().getMember(this.username);

            if (((Party)var7.get(var9)).hasMember(var10))
            {
                var8.add(this.username);
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
        GL11.glVertex2f((float)(var1 + 2), (float)(var2 + 2));
        GL11.glTexCoord2f(var14, var13);
        GL11.glVertex2f((float)(var1 + 2), (float)(var2 + 18));
        GL11.glTexCoord2f(var12, var13);
        GL11.glVertex2f((float)(var1 + 18), (float)(var2 + 18));
        GL11.glTexCoord2f(var12, var11);
        GL11.glVertex2f((float)(var1 + 18), (float)(var2 + 2));
        GL11.glEnd();
        var5.renderEngine.resetBoundTexture();
        var6.drawStringWithShadow(this.username, var1 + var4, var2 + 2, 15066597);
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 1.0F);

        if (var8.contains(this.username))
        {
            var6.drawString("ONLINE", (int)(((float)var1 + (float)var4) / 0.75F), (int)(((float)var2 + 12.0F) / 0.75F), 6750054);
        }
        else
        {
            var6.drawString("OFFLINE", (int)(((float)var1 + (float)var4) / 0.75F), (int)(((float)var2 + 12.0F) / 0.75F), 16711680);
        }

        GL11.glPopMatrix();
    }

    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        return this.enabled && this.drawButton && var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
    }
}
