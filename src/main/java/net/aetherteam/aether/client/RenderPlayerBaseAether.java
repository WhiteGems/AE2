package net.aetherteam.aether.client;

import java.nio.FloatBuffer;
import java.util.HashMap;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherRanks;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.client.models.ModelAetherWings;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.DonatorTexture;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.items.ItemAccessory;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.src.RenderPlayerBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPlayerBaseAether extends RenderPlayerBase
{
    private ModelBiped modelCape;
    private ModelBiped modelMisc;
    private ModelAetherWings modelWings;
    FloatBuffer field_76908_a = GLAllocation.createDirectFloatBuffer(16);

    public RenderPlayerBaseAether(RenderPlayerAPI var1)
    {
        super(var1);
        this.modelCape = new ModelBiped(0.0F);
        this.modelMisc = new ModelBiped(0.6F);
        this.modelWings = new ModelAetherWings(0.0F);
    }

    public void afterRenderSpecials(EntityPlayer entityplayer, float f)
    {
        renderCape(entityplayer, f);
        if ((AetherRanks.getRankFromMember(entityplayer.username).equals(AetherRanks.DEVELOPER)) || (AetherRanks.getRankFromMember(entityplayer.username).equals(AetherRanks.HELPER)))
        {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glScalef(1.02171F, 1.0271F, 1.0271F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            float var4 = 1.0F;
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            renderCape(entityplayer, f);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glPopMatrix();
        }
    }

    public void renderPlayer(EntityPlayer player, double d, double d1, double d2, float f, float f1)
    {
        super.renderPlayer(player, d, d1, d2, -90.0F, f1);

        renderMisc(player, d, d1, d2, f, f1);
    }

    public void renderMount(Entity entity, double x, double y, double z, float rotationYaw, float partialTickTime)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();

        RenderManager.instance.renderEntityWithPosYaw(entity, x, y, z, rotationYaw, partialTickTime);

        GL11.glPopMatrix();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void afterRenderFirstPersonArm(EntityPlayer player)
    {
        if (this.renderPlayer.getRenderManagerField().renderEngine != null)
        {
            renderFirstPersonGlow(player);
            renderFirstPersonGloves(player);
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

            this.renderPlayer.localLoadDownloadableImageTexture("http://skins.minecraft.net/MinecraftSkins/" + player.username + ".png", "/mob/char.png");
            this.modelMisc.onGround = 0.0F;
            this.modelMisc.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
            GL11.glTranslatef(-0.01F, 0.05F, 0.01F);
            this.modelMisc.bipedRightArm.render(0.0625F);
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
        InventoryAether inv = (InventoryAether) Aether.proxy.getClientInventories().get(player.username);

        if (inv == null)
        {
            return;
        }
        if (inv.slots[6] != null)
        {
            float brightness = player.getBrightness(1.0F);
            this.modelMisc.onGround = 0.0F;
            this.modelMisc.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
            this.modelMisc.bipedRightArm.render(0.0625F);
            ItemAccessory glove = (ItemAccessory) inv.slots[6].getItem();
            this.renderPlayer.localLoadTexture(glove.texture);
            int colour = glove.getColorFromItemStack(new ItemStack(glove, 1, 0), 1);
            float red = (colour >> 16 & 0xFF) / 255.0F;
            float green = (colour >> 8 & 0xFF) / 255.0F;
            float blue = (colour & 0xFF) / 255.0F;
            if (glove.colouriseRender)
            {
                GL11.glColor3f(red, green, blue);
            }
            this.modelMisc.bipedRightArm.render(0.0625F);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
        }
    }

    public void renderSpecialHeadEars(EntityPlayer player, float var2)
    {
        InventoryAether inv = (InventoryAether) Aether.proxy.getClientInventories().get(player.username);

        if (inv == null)
        {
            return;
        }
        if ((wearingAccessory(AetherItems.Deadmau5Ears.itemID, inv)) && (!player.isPotionActive(Potion.invisibility)))
        {
            for (int var3 = 0; var3 < 2; var3++)
            {
                this.renderPlayer.localLoadTexture("/net/aetherteam/aether/client/sprites/capes/deadmau5.png");
                float var4 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * var2 - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * var2);
                float var5 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * var2;
                GL11.glPushMatrix();
                GL11.glRotatef(var4, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(var5, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (var3 * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-var5, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-var4, 0.0F, 1.0F, 0.0F);
                float var6 = 1.333333F;
                GL11.glScalef(var6, var6, var6);
                this.renderPlayer.getModelBipedMainField().renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }
    }

    public void renderCape(EntityPlayer entityplayer, float f)
    {
        InventoryAether inv = (InventoryAether) Aether.proxy.getClientInventories().get(entityplayer.username);

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
                String onlineTexture = textureFile.onlineURL;

                this.renderPlayer.localLoadTexture(localTexture);
            } else
            {
                this.renderPlayer.localLoadTexture(((ItemAccessory) cape.getItem()).texture);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d = entityplayer.field_71091_bM + (entityplayer.field_71094_bP - entityplayer.field_71091_bM) * f - (entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * f);
            double d1 = entityplayer.field_71096_bN + (entityplayer.field_71095_bQ - entityplayer.field_71096_bN) * f - (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * f);
            double d2 = entityplayer.field_71097_bO + (entityplayer.field_71085_bR - entityplayer.field_71097_bO) * f - (entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * f);
            float f8 = entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * f;
            double d3 = MathHelper.sin(f8 * (float)Math.PI / 180.0F);
            double d4 = -MathHelper.cos(f8 * (float)Math.PI / 180.0F);
            float f9 = (float) d1 * 10.0F;
            if (f9 < -6.0F)
            {
                f9 = -6.0F;
            }
            if (f9 > 32.0F)
            {
                f9 = 32.0F;
            }
            float f10 = (float) (d * d3 + d2 * d4) * 100.0F;
            float f11 = (float) (d * d4 - d2 * d3) * 100.0F;
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
            this.modelCape.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }
    }

    private FloatBuffer func_76907_a(float par1, float par2, float par3, float par4)
    {
        this.field_76908_a.clear();
        this.field_76908_a.put(par1).put(par2).put(par3).put(par4);
        this.field_76908_a.flip();
        return this.field_76908_a;
    }

    public void renderMisc(EntityPlayer entityplayer, double d, double d1, double d2, float f, float f1)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        this.modelMisc.heldItemRight = (itemstack != null ? 1 : 0);
        this.modelMisc.isSneak = entityplayer.isSneaking();
        this.modelWings.heldItemRight = (itemstack != null ? 1 : 0);
        this.modelWings.isSneak = entityplayer.isSneaking();
        double d3 = d1 - entityplayer.yOffset;
        if ((entityplayer.isSneaking()) && (!(entityplayer instanceof EntityPlayerSP)))
        {
            d3 -= 0.125D;
        }

        doRenderMisc(entityplayer, d, d3, d2, f, f1);
        this.modelMisc.isSneak = false;
        this.modelMisc.heldItemRight = 0;
        this.modelWings.isSneak = false;
        this.modelWings.heldItemRight = 0;
    }

    public void doRenderMisc(EntityPlayer player, double d, double d1, double d2, float f, float f1)
    {
        InventoryAether inv = (InventoryAether) Aether.proxy.getClientInventories().get(player.username);

        if (inv == null)
        {
            return;
        }
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
        this.modelMisc.onGround = this.renderPlayer.localRenderSwingProgress(player, f1);
        this.modelMisc.isRiding = player.isRiding();
        this.modelWings.onGround = this.renderPlayer.localRenderSwingProgress(player, f1);
        this.modelWings.isRiding = player.isRiding();
        try
        {
            float f2 = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * f1;
            float f3 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f1;
            float f4 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f1;
            this.renderPlayer.realRenderLivingAt(player, d, d1, d2);
            float f5 = this.renderPlayer.realHandleRotationFloat(player, f1);
            this.renderPlayer.realRotateCorpse(player, f5, f2, f1);
            float f6 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            this.renderPlayer.realPreRenderCallback(player, f1);
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

            float brightness = player.getBrightness(f);

            if (inv.slots[0] != null)
            {
                ItemAccessory pendant = (ItemAccessory) inv.slots[0].getItem();
                this.renderPlayer.localLoadTexture(pendant.texture);
                int colour = pendant.getColorFromItemStack(new ItemStack(pendant, 1, 0), 1);
                float red = (colour >> 16 & 0xFF) / 255.0F;
                float green = (colour >> 8 & 0xFF) / 255.0F;
                float blue = (colour & 0xFF) / 255.0F;
                if (pendant.colouriseRender) GL11.glColor3f(red, green, blue);
                this.modelMisc.bipedBody.render(f6);
            }
            if (inv.slots[6] != null)
            {
                ItemAccessory pendant = (ItemAccessory) inv.slots[6].getItem();
                this.renderPlayer.localLoadTexture(pendant.texture);
                int colour = pendant.getColorFromItemStack(new ItemStack(pendant, 1, 0), 1);
                float red = (colour >> 16 & 0xFF) / 255.0F;
                float green = (colour >> 8 & 0xFF) / 255.0F;
                float blue = (colour & 0xFF) / 255.0F;
                if (pendant.colouriseRender) GL11.glColor3f(red, green, blue);
                this.modelMisc.bipedLeftArm.render(f6);
                this.modelMisc.bipedRightArm.render(f6);
            }
            if ((wearingValkyrieArmour(player, inv)) && (Aether.getClientPlayer(player) != null))
            {
                this.modelWings.sinage = Aether.getClientPlayer(player).getSinage();
                this.modelWings.gonRound = player.onGround;

                this.renderPlayer.localLoadTexture("/net/aetherteam/aether/client/sprites/mobs/valkyrie/valkyrie.png");

                this.modelWings.wingLeft.render(f6);
                this.modelWings.wingRight.render(f6);
            }
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

                this.renderPlayer.localLoadDownloadableImageTexture("http://skins.minecraft.net/MinecraftSkins/" + player.username + ".png", "/mob/char.png");
                this.modelMisc.bipedBody.render(f6);
                GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                this.modelMisc.bipedRightLeg.render(f6);
                this.modelMisc.bipedLeftLeg.render(f6);
                GL11.glTranslatef(0.0F, -0.04F, 0.0F);
                this.modelMisc.bipedRightArm.render(f6);
                this.modelMisc.bipedLeftArm.render(f6);
                GL11.glTranslatef(0.0F, -0.02F, 0.0F);
                this.modelMisc.bipedHead.render(f6);
                char var5 = 61680;
                int var6 = var5 % 65536;
                int var7 = var5 / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
                GL11.glPopMatrix();
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        } catch (Exception exception)
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

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.RenderPlayerBaseAether
 * JD-Core Version:    0.6.2
 */