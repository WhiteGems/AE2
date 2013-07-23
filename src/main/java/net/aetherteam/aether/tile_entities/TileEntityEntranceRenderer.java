package net.aetherteam.aether.tile_entities;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.CallableMPL2;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TileEntityEntranceRenderer extends TileEntityRenderer
{
    protected static final String AVAILABLE = "Available";
    protected static final String OCCUPIED = "Occupied";
    protected static final String CONQUERED = "Conquered";
    private static final int AVAILABLE_COLOUR = 6750054;
    private static final int OCCUPIED_COLOUR = 16756516;
    private static final int CONQUERED_COLOUR = 10688793;

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderTileEntityEntranceAt((TileEntityEntranceController)tileentity, d, d1, d2, f);
    }

    public void renderTileEntityEntranceAt(TileEntityEntranceController entrance, double d, double d1, double d2, float f)
    {
        Dungeon dungeon = entrance.getDungeon();

        if ((dungeon != null) && (!dungeon.hasMember(PartyController.instance().getMember(Minecraft.getMinecraft().thePlayer))))
        {
            PartyMember member = PartyController.instance().getMember(Minecraft.getMinecraft().thePlayer.bS.toLowerCase());
            Party party = PartyController.instance().getParty(member);
            String label = (dungeon.isActive()) || (dungeon.hasQueuedParty()) ? "Occupied" : ((party != null) && (dungeon.hasAnyConqueredDungeon(party.getMembers()))) || (dungeon.hasConqueredDungeon(Minecraft.getMinecraft().thePlayer)) ? "Conquered" : "Available";
            int label_colour = (dungeon.isActive()) || (dungeon.hasQueuedParty()) ? 16756516 : ((party != null) && (dungeon.hasAnyConqueredDungeon(party.getMembers()))) || (dungeon.hasConqueredDungeon(Minecraft.getMinecraft().thePlayer)) ? 10688793 : 6750054;
            GL11.glPushMatrix();
            renderLabel(entrance, label, d, d1 + 5.0D, d2, 24, 2.0F, label_colour);
            GL11.glPopMatrix();
        }
    }

    protected void renderLabel(TileEntity tile, String description, double posX, double posY, double posZ, int distanceSeenAt, float scale, int text_colour)
    {
        RenderEnderman renderManager = RenderEnderman.endermanModel;
        double d3 = tile.getDistanceFrom(renderManager.h.posX, renderManager.h.posY, renderManager.h.posZ);

        if (d3 <= distanceSeenAt * distanceSeenAt)
        {
            FontRenderer fontrenderer = renderManager.getFontRendererFromRenderManager();
            float f = 1.6F;
            float f1 = 0.01666667F * f;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)posX + 0.0F, (float)posY + 0.5F, (float)posZ);
            GL11.glScalef(scale, scale, scale);
            GL11.glNormal3f(1.0F, 1.0F, 1.0F);
            GL11.glRotatef(-renderManager.j, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(renderManager.k, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Rect2i tessellator = Rect2i.rectX;
            byte b0 = 0;

            if (description.equals("deadmau5"))
            {
                b0 = -10;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.b();
            int j = fontrenderer.getStringWidth(description) / 2;
            tessellator.a(0.0F, 0.0F, 0.0F, 0.25F);
            tessellator.a(-j - 1, -1 + b0, 0.0D);
            tessellator.a(-j - 1, 8 + b0, 0.0D);
            tessellator.a(j + 1, 8 + b0, 0.0D);
            tessellator.a(j + 1, -1 + b0, 0.0D);
            tessellator.getRectX();
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

