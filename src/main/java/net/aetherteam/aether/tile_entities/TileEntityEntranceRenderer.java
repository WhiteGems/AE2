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
    protected static final String AVAILABLE = "Available";
    protected static final String OCCUPIED = "Occupied";
    protected static final String CONQUERED = "Conquered";
    private static final int AVAILABLE_COLOUR = 6750054;
    private static final int OCCUPIED_COLOUR = 16756516;
    private static final int CONQUERED_COLOUR = 10688793;

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        this.renderTileEntityEntranceAt((TileEntityEntranceController)tileentity, d, d1, d2, f);
    }

    public void renderTileEntityEntranceAt(TileEntityEntranceController entrance, double d, double d1, double d2, float f)
    {
        Dungeon dungeon = entrance.getDungeon();

        if (dungeon != null && !dungeon.hasMember(PartyController.instance().getMember((EntityPlayer)Minecraft.getMinecraft().thePlayer)))
        {
            PartyMember member = PartyController.instance().getMember(Minecraft.getMinecraft().thePlayer.username.toLowerCase());
            Party party = PartyController.instance().getParty(member);
            String label = (party == null || !dungeon.hasAnyConqueredDungeon(party.getMembers())) && !dungeon.hasConqueredDungeon((EntityPlayer)Minecraft.getMinecraft().thePlayer) ? (!dungeon.isActive() && !dungeon.hasQueuedParty() ? "Available" : "Occupied") : "Conquered";
            int label_colour = (party == null || !dungeon.hasAnyConqueredDungeon(party.getMembers())) && !dungeon.hasConqueredDungeon((EntityPlayer)Minecraft.getMinecraft().thePlayer) ? (!dungeon.isActive() && !dungeon.hasQueuedParty() ? 6750054 : 16756516) : 10688793;
            GL11.glPushMatrix();
            this.renderLabel(entrance, label, d, d1 + 5.0D, d2, 24, 2.0F, label_colour);
            GL11.glPopMatrix();
        }
    }

    protected void renderLabel(TileEntity tile, String description, double posX, double posY, double posZ, int distanceSeenAt, float scale, int text_colour)
    {
        RenderManager renderManager = RenderManager.instance;
        double d3 = tile.getDistanceFrom(renderManager.livingPlayer.posX, renderManager.livingPlayer.posY, renderManager.livingPlayer.posZ);

        if (d3 <= (double)(distanceSeenAt * distanceSeenAt))
        {
            FontRenderer fontrenderer = renderManager.getFontRenderer();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)posX + 0.0F, (float)posY + 0.5F, (float)posZ);
            GL11.glScalef(scale, scale, scale);
            GL11.glNormal3f(1.0F, 1.0F, 1.0F);
            GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator tessellator = Tessellator.instance;
            byte b0 = 0;

            if (description.equals("deadmau5"))
            {
                b0 = -10;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            int j = fontrenderer.getStringWidth(description) / 2;
            tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            tessellator.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
            tessellator.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
            tessellator.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
            tessellator.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            fontrenderer.drawString(description, -fontrenderer.getStringWidth(description) / 2, b0, text_colour);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontrenderer.drawString(description, -fontrenderer.getStringWidth(description) / 2, b0, text_colour);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}
