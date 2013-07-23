package net.aetherteam.aether.client;

import java.nio.FloatBuffer;
import java.util.HashMap;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherRanks;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.client.models.ModelAetherWings;
import net.aetherteam.aether.client.models.ModelParachute;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.DonatorTexture;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.items.ItemAccessory;
import net.aetherteam.playercore_api.cores.PlayerCoreRender;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.bdi;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPlayerBaseAether extends PlayerCoreRender
{
    private ModelGhast modelCape;
    private ModelGhast modelMisc;
    private ModelAetherWings modelWings;
    private ModelParachute modelParachute;
    FloatBuffer field_76908_a = GLAllocation.createDirectFloatBuffer(16);

    public RenderPlayerBaseAether(int playerCoreIndex, PlayerCoreRender renderPlayer)
    {
        super(playerCoreIndex, renderPlayer);
        this.modelCape = new ModelGhast(0.0F);
        this.modelMisc = new ModelGhast(0.6F);
        this.modelWings = new ModelAetherWings(0.0F);
        this.modelParachute = new ModelParachute();
    }

    public void a(EntityPlayer entityplayer, float f)
    {
        super.a(entityplayer, f);
        renderCape(entityplayer, f);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glScalef(1.02171F, 1.0271F, 1.0271F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        renderCape(entityplayer, f);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glPopMatrix();
    }

    public void a(EntityPlayer player, double d, double d1, double d2, float f, float f1)
    {
        super.a(player, d, d1, d2, -90.0F, f1);
        renderMisc(player, d, d1, d2, f, f1);
    }

    public void renderMount(Entity entity, double x, double y, double z, float rotationYaw, float partialTickTime)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        RenderEnderman.endermanModel.doRender(entity, x, y, z, rotationYaw, partialTickTime);
        GL11.glPopMatrix();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void b(EntityPlayer player)
    {
        super.b(player);

        if (this.renderPlayer.getRenderManager().e != null)
        {
            renderFirstPersonGlow(player);
            renderFirstPersonGloves(player);
        }
    }

    public void renderParachute(EntityPlayer player, float f)
    {
        if ((Aether.proxy.getClientParachuting().containsKey(player.username)) && (Aether.proxy.getClientParachuteType().containsKey(player.username)))
        {
            this.modelParachute = new ModelParachute();
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            GL11.glTranslatef(0.0F, -0.7F, -0.4F);
            this.renderPlayer.a("/net/aetherteam/aether/client/sprites/mobs/parachute_" + String.valueOf(Aether.proxy.getClientParachuteType().get(player.username)) + ".png");
            this.modelParachute.Cloud1.a(f);
            this.modelParachute.Cloud2.a(f);
            this.modelParachute.Cloud3.a(f);
            this.modelParachute.Cloud4.a(f);
            this.modelParachute.Cloud5.a(f);
            this.modelParachute.Shape1.a(f);
            this.modelParachute.Shape2.a(f);
            this.modelParachute.Shape3.a(f);
            this.modelParachute.Shape4.a(f);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public void renderFirstPersonGlow(EntityPlayer player)
    {
        if (AetherRanks.getRankFromMember(player.username).equals(AetherRanks.DEVELOPER))
        {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glScalef(0.921F, 0.921F, 0.921F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.renderPlayer.a("http://skins.minecraft.net/MinecraftSkins/" + player.username + ".png", "/mob/char.png");
            this.modelMisc.onGround = 0.0F;
            this.modelMisc.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
            GL11.glTranslatef(-0.01F, 0.05F, 0.01F);
            this.modelMisc.f.a(0.0625F);
            char var5 = 61680;
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    public void renderFirstPersonGloves(EntityPlayer player)
    {
        InventoryAether inv = (InventoryAether)Aether.proxy.getClientInventories().get(player.username);

        if (inv == null)
        {
            return;
        }

        if (inv.slots[6] != null)
        {
            player.getBrightness(1.0F);
            this.modelMisc.onGround = 0.0F;
            this.modelMisc.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
            this.modelMisc.f.a(0.0625F);
            ItemAccessory glove = (ItemAccessory)inv.slots[6].getItem();
            this.renderPlayer.a(glove.texture);
            int colour = glove.getColorFromItemStack(new ItemStack(glove, 1, 0), 1);
            float red = (colour >> 16 & 0xFF) / 255.0F;
            float green = (colour >> 8 & 0xFF) / 255.0F;
            float blue = (colour & 0xFF) / 255.0F;

            if (glove.colouriseRender)
            {
                GL11.glColor3f(red, green, blue);
            }

            this.modelMisc.f.a(0.0625F);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
        }
    }

    public void renderCape(EntityPlayer entityplayer, float f)
    {
        InventoryAether inv = (InventoryAether)Aether.proxy.getClientInventories().get(entityplayer.username);

        if (inv == null)
        {
            return;
        }

        if ((inv.slots[1] != null) && (inv.slots[1].getItem() != AetherItems.InvisibilityCloak))
        {
            ItemStack cape = inv.slots[1];

            if ((Aether.syncDonatorList.isDonator(entityplayer.username)) && (Aether.syncDonatorList.getDonator(entityplayer.username).containsChoiceType(EnumChoiceType.CAPE)))
            {
                DonatorTexture textureFile = Aether.syncDonatorList.getDonator(entityplayer.username).getChoiceFromType(EnumChoiceType.CAPE).textureFile;
                String localTexture = textureFile.localURL;
                this.renderPlayer.a(localTexture);
            }
            else
            {
                this.renderPlayer.a(((ItemAccessory)cape.getItem()).texture);
            }

            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d = entityplayer.field_71091_bM + (entityplayer.field_71094_bP - entityplayer.field_71091_bM) * f - (entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * f);
            double d1 = entityplayer.field_71096_bN + (entityplayer.field_71095_bQ - entityplayer.field_71096_bN) * f - (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * f);
            double d2 = entityplayer.field_71097_bO + (entityplayer.field_71085_bR - entityplayer.field_71097_bO) * f - (entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * f);
            float f8 = entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * f;
            double d3 = MathHelper.sin(f8 * (float)Math.PI / 180.0F);
            double d4 = -MathHelper.cos(f8 * (float)Math.PI / 180.0F);
            float f9 = (float)d1 * 10.0F;

            if (f9 < -6.0F)
            {
                f9 = -6.0F;
            }

            if (f9 > 32.0F)
            {
                f9 = 32.0F;
            }

            float f10 = (float)(d * d3 + d2 * d4) * 100.0F;
            float f11 = (float)(d * d4 - d2 * d3) * 100.0F;

            if (f10 < 0.0F)
            {
                f10 = 0.0F;
            }

            float f12 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * f;
            f9 += MathHelper.sin((entityplayer.prevDistanceWalkedModified + (entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified) * f) * 6.0F) * 32.0F * f12;

            if (entityplayer.isSneaking())
            {
                f9 += 25.0F;
            }

            GL11.glRotatef(6.0F + f10 / 2.0F + f9, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f11 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f11 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.modelCape.c(0.0625F);
            GL11.glPopMatrix();
        }
    }

    public void renderMisc(EntityPlayer entityplayer, double d, double d1, double d2, float f, float f1)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        this.modelMisc.m = (itemstack != null ? 1 : 0);
        this.modelMisc.n = entityplayer.isSneaking();
        this.modelWings.m = (itemstack != null ? 1 : 0);
        this.modelWings.n = entityplayer.isSneaking();
        double d3 = d1 - entityplayer.yOffset;

        if ((entityplayer.isSneaking()) && (!(entityplayer instanceof MovementInputFromOptions)))
        {
            d3 -= 0.125D;
        }

        doRenderMisc(entityplayer, d, d3, d2, f, f1);
        this.modelMisc.n = false;
        this.modelMisc.m = 0;
        this.modelWings.n = false;
        this.modelWings.m = 0;
    }

    public void doRenderMisc(EntityPlayer player, double d, double d1, double d2, float f, float f1)
    {
        InventoryAether inv = (InventoryAether)Aether.proxy.getClientInventories().get(player.username);

        if (inv == null)
        {
            return;
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
        this.modelMisc.onGround = this.renderPlayer.d(player, f1);
        this.modelMisc.isRiding = player.isRiding();
        this.modelWings.onGround = this.renderPlayer.d(player, f1);
        this.modelWings.isRiding = player.isRiding();

        try
        {
            float f2 = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * f1;
            float f3 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f1;
            float f4 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f1;
            a(player, d, d1, d2);
            float f5 = b(player, f1);
            a(player, f5, f2, f1);
            float f6 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            a(player, f1);
            GL11.glTranslatef(0.0F, -24.0F * f6 - 0.007813F, 0.0F);
            float f7 = player.prevLimbYaw + (player.limbYaw - player.prevLimbYaw) * f1;
            float f8 = player.limbSwing - player.limbYaw * (1.0F - f1);

            if (f7 > 1.0F)
            {
                f7 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            this.modelMisc.setRotationAngles(f8, f7, f5, f3 - f2, f4, f6, player);
            this.modelWings.setRotationAngles(f8, f7, f5, f3 - f2, f4, f6, player);
            player.getBrightness(f);

            if (inv.slots[0] != null)
            {
                ItemAccessory pendant = (ItemAccessory)inv.slots[0].getItem();
                this.renderPlayer.a(pendant.texture);
                int colour = pendant.getColorFromItemStack(new ItemStack(pendant, 1, 0), 1);
                float red = (colour >> 16 & 0xFF) / 255.0F;
                float green = (colour >> 8 & 0xFF) / 255.0F;
                float blue = (colour & 0xFF) / 255.0F;

                if (pendant.colouriseRender)
                {
                    GL11.glColor3f(red, green, blue);
                }

                this.modelMisc.e.a(f6);
            }

            if (inv.slots[6] != null)
            {
                ItemAccessory pendant = (ItemAccessory)inv.slots[6].getItem();
                this.renderPlayer.a(pendant.texture);
                int colour = pendant.getColorFromItemStack(new ItemStack(pendant, 1, 0), 1);
                float red = (colour >> 16 & 0xFF) / 255.0F;
                float green = (colour >> 8 & 0xFF) / 255.0F;
                float blue = (colour & 0xFF) / 255.0F;

                if (pendant.colouriseRender)
                {
                    GL11.glColor3f(red, green, blue);
                }

                this.modelMisc.g.a(f6);
                this.modelMisc.f.a(f6);
            }

            if ((wearingValkyrieArmour(player, inv)) && (Aether.getClientPlayer(player) != null))
            {
                this.modelWings.sinage = Aether.getClientPlayer(player).getSinage();
                this.modelWings.gonRound = player.onGround;
                this.renderPlayer.a("/net/aetherteam/aether/client/sprites/mobs/valkyrie/valkyrie.png");
                this.modelWings.wingLeft.a(f6);
                this.modelWings.wingRight.a(f6);
            }

            renderParachute(player, f6);

            if ((AetherRanks.getRankFromMember(player.username).equals(AetherRanks.DEVELOPER)) || (AetherRanks.getRankFromMember(player.username).equals(AetherRanks.HELPER)))
            {
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glScalef(0.951F, 0.951F, 0.951F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
                float var4 = 1.0F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                this.renderPlayer.a("http://skins.minecraft.net/MinecraftSkins/" + player.username + ".png", "/mob/char.png");
                this.modelMisc.e.a(f6);
                GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                this.modelMisc.h.a(f6);
                this.modelMisc.i.a(f6);
                GL11.glTranslatef(0.0F, -0.04F, 0.0F);
                this.modelMisc.f.a(f6);
                this.modelMisc.g.a(f6);
                GL11.glTranslatef(0.0F, -0.02F, 0.0F);
                this.modelMisc.c.a(f6);
                char var5 = 61680;
                int var6 = var5 % 65536;
                int var7 = var5 / 65536;
                GL11.glEnable(GL11.GL_LIGHTING);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
                GL11.glPopMatrix();
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        GL11.glPopMatrix();
    }

    public boolean wearingAccessory(int itemID, InventoryAether inv)
    {
        for (int index = 0; index < 8; index++)
        {
            if ((inv.slots[index] != null) && (inv.slots[index].itemID == itemID))
            {
                return true;
            }
        }

        return false;
    }

    public boolean wearingArmour(int itemID, EntityPlayer player)
    {
        for (int index = 0; index < 4; index++)
        {
            if ((player.inventory.armorInventory[index] != null) && (player.inventory.armorInventory[index].itemID == itemID))
            {
                return true;
            }
        }

        return false;
    }

    public boolean wearingNeptuneArmour(EntityPlayer player, InventoryAether inv)
    {
        return (wearingArmour(AetherItems.NeptuneHelmet.itemID, player)) && (wearingArmour(AetherItems.NeptuneChestplate.itemID, player)) && (wearingArmour(AetherItems.NeptuneLeggings.itemID, player)) && (wearingArmour(AetherItems.NeptuneBoots.itemID, player)) && (wearingAccessory(AetherItems.NeptuneGloves.itemID, inv));
    }

    public boolean wearingValkyrieArmour(EntityPlayer player, InventoryAether inv)
    {
        return (wearingArmour(AetherItems.ValkyrieHelmet.itemID, player)) && (wearingArmour(AetherItems.ValkyrieChestplate.itemID, player)) && (wearingArmour(AetherItems.ValkyrieLeggings.itemID, player)) && (wearingArmour(AetherItems.ValkyrieBoots.itemID, player)) && (wearingAccessory(AetherItems.ValkyrieGloves.itemID, inv));
    }

    public boolean wearingObsidianArmour(EntityPlayer player, InventoryAether inv)
    {
        return (wearingArmour(AetherItems.ObsidianHelmet.itemID, player)) && (wearingArmour(AetherItems.ObsidianChestplate.itemID, player)) && (wearingArmour(AetherItems.ObsidianLeggings.itemID, player)) && (wearingArmour(AetherItems.ObsidianBoots.itemID, player)) && (wearingAccessory(AetherItems.ObsidianGloves.itemID, inv));
    }

    public boolean wearingPhoenixArmour(EntityPlayer player, InventoryAether inv)
    {
        return (wearingArmour(AetherItems.PhoenixHelmet.itemID, player)) && (wearingArmour(AetherItems.PhoenixChestplate.itemID, player)) && (wearingArmour(AetherItems.PhoenixLeggings.itemID, player)) && (wearingArmour(AetherItems.PhoenixBoots.itemID, player)) && (wearingAccessory(AetherItems.PhoenixGloves.itemID, inv));
    }

    public boolean wearingGravititeArmour(EntityPlayer player, InventoryAether inv)
    {
        return (wearingArmour(AetherItems.GravititeHelmet.itemID, player)) && (wearingArmour(AetherItems.GravititeChestplate.itemID, player)) && (wearingArmour(AetherItems.GravititeLeggings.itemID, player)) && (wearingArmour(AetherItems.GravititeBoots.itemID, player)) && (wearingAccessory(AetherItems.GravititeGloves.itemID, inv));
    }
}

