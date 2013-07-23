package net.aetherteam.aether.client;

import java.nio.FloatBuffer;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherRanks;
import net.aetherteam.aether.client.models.ModelAetherWings;
import net.aetherteam.aether.client.models.ModelParachute;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.donator.DonatorTexture;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.items.ItemAccessory;
import net.aetherteam.playercore_api.cores.PlayerCoreRender;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPlayerBaseAether extends PlayerCoreRender
{
    private ModelBiped modelCape = new ModelBiped(0.0F);
    private ModelBiped modelMisc = new ModelBiped(0.6F);
    private ModelAetherWings modelWings = new ModelAetherWings(0.0F);
    private ModelParachute modelParachute = new ModelParachute();
    FloatBuffer field_76908_a = GLAllocation.createDirectFloatBuffer(16);

    public RenderPlayerBaseAether(int var1, PlayerCoreRender var2)
    {
        super(var1, var2);
    }

    /**
     * Method for adding special render rules
     */
    public void renderSpecials(EntityPlayer var1, float var2)
    {
        super.renderSpecials(var1, var2);
        this.renderCape(var1, var2);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glScalef(1.02171F, 1.0271F, 1.0271F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        this.renderCape(var1, var2);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glPopMatrix();
    }

    public void renderPlayer(EntityPlayer var1, double var2, double var4, double var6, float var8, float var9)
    {
        super.renderPlayer(var1, var2, var4, var6, -90.0F, var9);
        this.renderMisc(var1, var2, var4, var6, var8, var9);
    }

    public void renderMount(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        RenderManager.instance.renderEntityWithPosYaw(var1, var2, var4, var6, var8, var9);
        GL11.glPopMatrix();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void renderFirstPersonArm(EntityPlayer var1)
    {
        super.renderFirstPersonArm(var1);

        if (this.renderPlayer.getRenderManager().renderEngine != null)
        {
            this.renderFirstPersonGlow(var1);
            this.renderFirstPersonGloves(var1);
        }
    }

    public void renderParachute(EntityPlayer var1, float var2)
    {
        if (Aether.proxy.getClientParachuting().containsKey(var1.username) && Aether.proxy.getClientParachuteType().containsKey(var1.username))
        {
            this.modelParachute = new ModelParachute();
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            GL11.glTranslatef(0.0F, -0.7F, -0.4F);
            this.renderPlayer.loadTexture("/net/aetherteam/aether/client/sprites/mobs/parachute_" + String.valueOf(Aether.proxy.getClientParachuteType().get(var1.username)) + ".png");
            this.modelParachute.Cloud1.render(var2);
            this.modelParachute.Cloud2.render(var2);
            this.modelParachute.Cloud3.render(var2);
            this.modelParachute.Cloud4.render(var2);
            this.modelParachute.Cloud5.render(var2);
            this.modelParachute.Shape1.render(var2);
            this.modelParachute.Shape2.render(var2);
            this.modelParachute.Shape3.render(var2);
            this.modelParachute.Shape4.render(var2);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public void renderFirstPersonGlow(EntityPlayer var1)
    {
        if (AetherRanks.getRankFromMember(var1.username).equals(AetherRanks.DEVELOPER))
        {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glScalef(0.921F, 0.921F, 0.921F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            float var2 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.renderPlayer.loadDownloadableImageTexture("http://skins.minecraft.net/MinecraftSkins/" + var1.username + ".png", "/mob/char.png");
            this.modelMisc.onGround = 0.0F;
            this.modelMisc.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, var1);
            GL11.glTranslatef(-0.01F, 0.05F, 0.01F);
            this.modelMisc.bipedRightArm.render(0.0625F);
            char var3 = 61680;
            int var4 = var3 % 65536;
            int var5 = var3 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var4 / 1.0F, (float)var5 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var2);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    public void renderFirstPersonGloves(EntityPlayer var1)
    {
        InventoryAether var2 = (InventoryAether)Aether.proxy.getClientInventories().get(var1.username);

        if (var2 != null)
        {
            if (var2.slots[6] != null)
            {
                var1.getBrightness(1.0F);
                this.modelMisc.onGround = 0.0F;
                this.modelMisc.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, var1);
                this.modelMisc.bipedRightArm.render(0.0625F);
                ItemAccessory var3 = (ItemAccessory)((ItemAccessory)var2.slots[6].getItem());
                this.renderPlayer.loadTexture(var3.texture);
                int var4 = var3.getColorFromItemStack(new ItemStack(var3, 1, 0), 1);
                float var5 = (float)(var4 >> 16 & 255) / 255.0F;
                float var6 = (float)(var4 >> 8 & 255) / 255.0F;
                float var7 = (float)(var4 & 255) / 255.0F;

                if (var3.colouriseRender)
                {
                    GL11.glColor3f(var5, var6, var7);
                }

                this.modelMisc.bipedRightArm.render(0.0625F);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
            }
        }
    }

    public void renderCape(EntityPlayer var1, float var2)
    {
        InventoryAether var3 = (InventoryAether)Aether.proxy.getClientInventories().get(var1.username);

        if (var3 != null)
        {
            if (var3.slots[1] != null && var3.slots[1].getItem() != AetherItems.InvisibilityCloak)
            {
                ItemStack var4 = var3.slots[1];

                if (Aether.syncDonatorList.isDonator(var1.username) && Aether.syncDonatorList.getDonator(var1.username).containsChoiceType(EnumChoiceType.CAPE))
                {
                    DonatorTexture var5 = Aether.syncDonatorList.getDonator(var1.username).getChoiceFromType(EnumChoiceType.CAPE).textureFile;
                    String var6 = var5.localURL;
                    this.renderPlayer.loadTexture(var6);
                }
                else
                {
                    this.renderPlayer.loadTexture(((ItemAccessory)((ItemAccessory)var4.getItem())).texture);
                }

                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, 0.0F, 0.125F);
                double var20 = var1.field_71091_bM + (var1.field_71094_bP - var1.field_71091_bM) * (double)var2 - (var1.prevPosX + (var1.posX - var1.prevPosX) * (double)var2);
                double var7 = var1.field_71096_bN + (var1.field_71095_bQ - var1.field_71096_bN) * (double)var2 - (var1.prevPosY + (var1.posY - var1.prevPosY) * (double)var2);
                double var9 = var1.field_71097_bO + (var1.field_71085_bR - var1.field_71097_bO) * (double)var2 - (var1.prevPosZ + (var1.posZ - var1.prevPosZ) * (double)var2);
                float var11 = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var2;
                double var12 = (double)MathHelper.sin(var11 * (float)Math.PI / 180.0F);
                double var14 = (double)(-MathHelper.cos(var11 * (float)Math.PI / 180.0F));
                float var16 = (float)var7 * 10.0F;

                if (var16 < -6.0F)
                {
                    var16 = -6.0F;
                }

                if (var16 > 32.0F)
                {
                    var16 = 32.0F;
                }

                float var17 = (float)(var20 * var12 + var9 * var14) * 100.0F;
                float var18 = (float)(var20 * var14 - var9 * var12) * 100.0F;

                if (var17 < 0.0F)
                {
                    var17 = 0.0F;
                }

                float var19 = var1.prevCameraYaw + (var1.cameraYaw - var1.prevCameraYaw) * var2;
                var16 += MathHelper.sin((var1.prevDistanceWalkedModified + (var1.distanceWalkedModified - var1.prevDistanceWalkedModified) * var2) * 6.0F) * 32.0F * var19;

                if (var1.isSneaking())
                {
                    var16 += 25.0F;
                }

                GL11.glRotatef(6.0F + var17 / 2.0F + var16, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var18 / 2.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-var18 / 2.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                this.modelCape.renderCloak(0.0625F);
                GL11.glPopMatrix();
            }
        }
    }

    public void renderMisc(EntityPlayer var1, double var2, double var4, double var6, float var8, float var9)
    {
        ItemStack var10 = var1.inventory.getCurrentItem();
        this.modelMisc.heldItemRight = var10 != null ? 1 : 0;
        this.modelMisc.isSneak = var1.isSneaking();
        this.modelWings.heldItemRight = var10 != null ? 1 : 0;
        this.modelWings.isSneak = var1.isSneaking();
        double var11 = var4 - (double)var1.yOffset;

        if (var1.isSneaking() && !(var1 instanceof EntityPlayerSP))
        {
            var11 -= 0.125D;
        }

        this.doRenderMisc(var1, var2, var11, var6, var8, var9);
        this.modelMisc.isSneak = false;
        this.modelMisc.heldItemRight = 0;
        this.modelWings.isSneak = false;
        this.modelWings.heldItemRight = 0;
    }

    public void doRenderMisc(EntityPlayer var1, double var2, double var4, double var6, float var8, float var9)
    {
        InventoryAether var10 = (InventoryAether)Aether.proxy.getClientInventories().get(var1.username);

        if (var10 != null)
        {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_CULL_FACE);
            this.modelMisc.onGround = this.renderPlayer.renderSwingProgress(var1, var9);
            this.modelMisc.isRiding = var1.isRiding();
            this.modelWings.onGround = this.renderPlayer.renderSwingProgress(var1, var9);
            this.modelWings.isRiding = var1.isRiding();

            try
            {
                float var11 = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var9;
                float var12 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var9;
                float var13 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9;
                this.renderLivingAt(var1, var2, var4, var6);
                float var14 = this.handleRotationFloat(var1, var9);
                this.rotateCorpse(var1, var14, var11, var9);
                float var15 = 0.0625F;
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glScalef(-1.0F, -1.0F, 1.0F);
                this.preRenderCallback(var1, var9);
                GL11.glTranslatef(0.0F, -24.0F * var15 - 0.0078125F, 0.0F);
                float var16 = var1.prevLimbYaw + (var1.limbYaw - var1.prevLimbYaw) * var9;
                float var17 = var1.limbSwing - var1.limbYaw * (1.0F - var9);

                if (var16 > 1.0F)
                {
                    var16 = 1.0F;
                }

                GL11.glEnable(GL11.GL_ALPHA_TEST);
                this.modelMisc.setRotationAngles(var17, var16, var14, var12 - var11, var13, var15, var1);
                this.modelWings.setRotationAngles(var17, var16, var14, var12 - var11, var13, var15, var1);
                var1.getBrightness(var8);
                int var19;
                ItemAccessory var18;
                float var21;
                float var20;
                float var22;

                if (var10.slots[0] != null)
                {
                    var18 = (ItemAccessory)((ItemAccessory)var10.slots[0].getItem());
                    this.renderPlayer.loadTexture(var18.texture);
                    var19 = var18.getColorFromItemStack(new ItemStack(var18, 1, 0), 1);
                    var20 = (float)(var19 >> 16 & 255) / 255.0F;
                    var21 = (float)(var19 >> 8 & 255) / 255.0F;
                    var22 = (float)(var19 & 255) / 255.0F;

                    if (var18.colouriseRender)
                    {
                        GL11.glColor3f(var20, var21, var22);
                    }

                    this.modelMisc.bipedBody.render(var15);
                }

                if (var10.slots[6] != null)
                {
                    var18 = (ItemAccessory)((ItemAccessory)var10.slots[6].getItem());
                    this.renderPlayer.loadTexture(var18.texture);
                    var19 = var18.getColorFromItemStack(new ItemStack(var18, 1, 0), 1);
                    var20 = (float)(var19 >> 16 & 255) / 255.0F;
                    var21 = (float)(var19 >> 8 & 255) / 255.0F;
                    var22 = (float)(var19 & 255) / 255.0F;

                    if (var18.colouriseRender)
                    {
                        GL11.glColor3f(var20, var21, var22);
                    }

                    this.modelMisc.bipedLeftArm.render(var15);
                    this.modelMisc.bipedRightArm.render(var15);
                }

                if (this.wearingValkyrieArmour(var1, var10) && Aether.getClientPlayer(var1) != null)
                {
                    this.modelWings.sinage = Aether.getClientPlayer(var1).getSinage();
                    this.modelWings.gonRound = var1.onGround;
                    this.renderPlayer.loadTexture("/net/aetherteam/aether/client/sprites/mobs/valkyrie/valkyrie.png");
                    this.modelWings.wingLeft.render(var15);
                    this.modelWings.wingRight.render(var15);
                }

                this.renderParachute(var1, var15);

                if (AetherRanks.getRankFromMember(var1.username).equals(AetherRanks.DEVELOPER) || AetherRanks.getRankFromMember(var1.username).equals(AetherRanks.HELPER))
                {
                    GL11.glPushMatrix();
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glScalef(0.951F, 0.951F, 0.951F);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glDepthMask(true);
                    float var24 = 1.0F;
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                    this.renderPlayer.loadDownloadableImageTexture("http://skins.minecraft.net/MinecraftSkins/" + var1.username + ".png", "/mob/char.png");
                    this.modelMisc.bipedBody.render(var15);
                    GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                    this.modelMisc.bipedRightLeg.render(var15);
                    this.modelMisc.bipedLeftLeg.render(var15);
                    GL11.glTranslatef(0.0F, -0.04F, 0.0F);
                    this.modelMisc.bipedRightArm.render(var15);
                    this.modelMisc.bipedLeftArm.render(var15);
                    GL11.glTranslatef(0.0F, -0.02F, 0.0F);
                    this.modelMisc.bipedHead.render(var15);
                    char var26 = 61680;
                    int var25 = var26 % 65536;
                    int var27 = var26 / 65536;
                    GL11.glEnable(GL11.GL_LIGHTING);
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var25 / 1.0F, (float)var27 / 1.0F);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, var24);
                    GL11.glPopMatrix();
                }

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            }
            catch (Exception var23)
            {
                var23.printStackTrace();
            }

            GL11.glPopMatrix();
        }
    }

    public boolean wearingAccessory(int var1, InventoryAether var2)
    {
        for (int var3 = 0; var3 < 8; ++var3)
        {
            if (var2.slots[var3] != null && var2.slots[var3].itemID == var1)
            {
                return true;
            }
        }

        return false;
    }

    public boolean wearingArmour(int var1, EntityPlayer var2)
    {
        for (int var3 = 0; var3 < 4; ++var3)
        {
            if (var2.inventory.armorInventory[var3] != null && var2.inventory.armorInventory[var3].itemID == var1)
            {
                return true;
            }
        }

        return false;
    }

    public boolean wearingNeptuneArmour(EntityPlayer var1, InventoryAether var2)
    {
        return this.wearingArmour(AetherItems.NeptuneHelmet.itemID, var1) && this.wearingArmour(AetherItems.NeptuneChestplate.itemID, var1) && this.wearingArmour(AetherItems.NeptuneLeggings.itemID, var1) && this.wearingArmour(AetherItems.NeptuneBoots.itemID, var1) && this.wearingAccessory(AetherItems.NeptuneGloves.itemID, var2);
    }

    public boolean wearingValkyrieArmour(EntityPlayer var1, InventoryAether var2)
    {
        return this.wearingArmour(AetherItems.ValkyrieHelmet.itemID, var1) && this.wearingArmour(AetherItems.ValkyrieChestplate.itemID, var1) && this.wearingArmour(AetherItems.ValkyrieLeggings.itemID, var1) && this.wearingArmour(AetherItems.ValkyrieBoots.itemID, var1) && this.wearingAccessory(AetherItems.ValkyrieGloves.itemID, var2);
    }

    public boolean wearingObsidianArmour(EntityPlayer var1, InventoryAether var2)
    {
        return this.wearingArmour(AetherItems.ObsidianHelmet.itemID, var1) && this.wearingArmour(AetherItems.ObsidianChestplate.itemID, var1) && this.wearingArmour(AetherItems.ObsidianLeggings.itemID, var1) && this.wearingArmour(AetherItems.ObsidianBoots.itemID, var1) && this.wearingAccessory(AetherItems.ObsidianGloves.itemID, var2);
    }

    public boolean wearingPhoenixArmour(EntityPlayer var1, InventoryAether var2)
    {
        return this.wearingArmour(AetherItems.PhoenixHelmet.itemID, var1) && this.wearingArmour(AetherItems.PhoenixChestplate.itemID, var1) && this.wearingArmour(AetherItems.PhoenixLeggings.itemID, var1) && this.wearingArmour(AetherItems.PhoenixBoots.itemID, var1) && this.wearingAccessory(AetherItems.PhoenixGloves.itemID, var2);
    }

    public boolean wearingGravititeArmour(EntityPlayer var1, InventoryAether var2)
    {
        return this.wearingArmour(AetherItems.GravititeHelmet.itemID, var1) && this.wearingArmour(AetherItems.GravititeChestplate.itemID, var1) && this.wearingArmour(AetherItems.GravititeLeggings.itemID, var1) && this.wearingArmour(AetherItems.GravititeBoots.itemID, var1) && this.wearingAccessory(AetherItems.GravititeGloves.itemID, var2);
    }
}
