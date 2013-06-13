package net.aetherteam.aether.tile_entities;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TileEntityEntranceRenderer extends TileEntitySpecialRenderer
{
    protected static final String AVAILABLE = "未攻略";
    protected static final String OCCUPIED = "攻略中";
    protected static final String CONQUERED = "已攻略";
    private static final int AVAILABLE_COLOUR = 6750054;
    private static final int OCCUPIED_COLOUR = 16756516;
    private static final int CONQUERED_COLOUR = 10688793;

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        this.renderTileEntityEntranceAt((TileEntityEntranceController) var1, var2, var4, var6, var8);
    }

    public void renderTileEntityEntranceAt(TileEntityEntranceController var1, double var2, double var4, double var6, float var8)
    {
        Dungeon var9 = var1.getDungeon();

        if (var9 != null && !var9.hasMember(PartyController.instance().getMember((EntityPlayer) Minecraft.getMinecraft().thePlayer)))
        {
            PartyMember var10 = PartyController.instance().getMember(Minecraft.getMinecraft().thePlayer.username.toLowerCase());
            Party var11 = PartyController.instance().getParty(var10);
            String var12 = (var11 == null || !var9.hasAnyConqueredDungeon(var11.getMembers())) && !var9.hasConqueredDungeon((EntityPlayer) Minecraft.getMinecraft().thePlayer) ? (!var9.isActive() && !var9.hasQueuedParty() ? "Available" : "Occupied") : "Conquered";
            int var13 = (var11 == null || !var9.hasAnyConqueredDungeon(var11.getMembers())) && !var9.hasConqueredDungeon((EntityPlayer) Minecraft.getMinecraft().thePlayer) ? (!var9.isActive() && !var9.hasQueuedParty() ? 6750054 : 16756516) : 10688793;
            GL11.glPushMatrix();
            this.renderLabel(var1, var12, var2, var4 + 5.0D, var6, 24, 2.0F, var13);
            GL11.glPopMatrix();
        }
    }

    protected void renderLabel(TileEntity var1, String var2, double var3, double var5, double var7, int var9, float var10, int var11)
    {
        RenderManager var12 = RenderManager.instance;
        double var13 = var1.getDistanceFrom(var12.livingPlayer.posX, var12.livingPlayer.posY, var12.livingPlayer.posZ);

        if (var13 <= (double) (var9 * var9))
        {
            FontRenderer var15 = var12.getFontRenderer();
            float var16 = 1.6F;
            float var17 = 0.016666668F * var16;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) var3 + 0.0F, (float) var5 + 0.5F, (float) var7);
            GL11.glScalef(var10, var10, var10);
            GL11.glNormal3f(1.0F, 1.0F, 1.0F);
            GL11.glRotatef(-var12.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var12.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var17, -var17, var17);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator var18 = Tessellator.instance;
            byte var19 = 0;

            if (var2.equals("deadmau5"))
            {
                var19 = -10;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            var18.startDrawingQuads();
            int var20 = var15.getStringWidth(var2) / 2;
            var18.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var18.addVertex((double) (-var20 - 1), (double) (-1 + var19), 0.0D);
            var18.addVertex((double) (-var20 - 1), (double) (8 + var19), 0.0D);
            var18.addVertex((double) (var20 + 1), (double) (8 + var19), 0.0D);
            var18.addVertex((double) (var20 + 1), (double) (-1 + var19), 0.0D);
            var18.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            var15.drawString(var2, -var15.getStringWidth(var2) / 2, var19, var11);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            var15.drawString(var2, -var15.getStringWidth(var2) / 2, var19, var11);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}
