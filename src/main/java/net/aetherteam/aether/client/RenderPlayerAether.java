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
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPlayerAether extends PlayerCoreRender
{
    private static final ResourceLocation TEXTURE_VALKYRIE = new ResourceLocation("aether", "textures/mobs/valkyrie/valkyrie.png");
    private ModelBiped modelCape = new ModelBiped(0.0F);
    private ModelBiped modelMisc = new ModelBiped(0.6F);
    private ModelAetherWings modelWings = new ModelAetherWings(0.0F);
    private ModelParachute modelParachute = new ModelParachute();
    FloatBuffer field_76908_a = GLAllocation.createDirectFloatBuffer(16);

    public RenderPlayerAether(int playerCoreIndex, PlayerCoreRender renderPlayer)
    {
        super(playerCoreIndex, renderPlayer);
    }

    /**
     * Method for adding special render rules
     */
    public void renderSpecials(AbstractClientPlayer entityplayer, float f)
    {
        super.renderSpecials(entityplayer, f);
        this.renderCape(entityplayer, f);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glScalef(1.02171F, 1.0271F, 1.0271F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        this.renderCape(entityplayer, f);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glPopMatrix();
    }

    public void func_130009_a(AbstractClientPlayer player, double d, double d1, double d2, float f, float f1)
    {
        super.func_130009_a(player, d, d1, d2, -90.0F, f1);
        this.renderMisc(player, d, d1, d2, f, f1);
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

    public void renderFirstPersonArm(EntityPlayer player)
    {
        super.renderFirstPersonArm(player);

        if (this.renderPlayer.getRenderManager().renderEngine != null)
        {
            this.renderFirstPersonGlow(player);
            this.renderFirstPersonGloves(player);
        }
    }

    public void renderParachute(EntityPlayer player, float f)
    {
        if (Aether.proxy.getClientParachuting().containsKey(player.username) && Aether.proxy.getClientParachuteType().containsKey(player.username))
        {
            this.modelParachute = new ModelParachute();
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            GL11.glTranslatef(0.0F, -0.7F, -0.4F);
            this.renderPlayer.getRenderManager().renderEngine.func_110577_a(new ResourceLocation("aether", "textures/mobs/parachute_" + String.valueOf(Aether.proxy.getClientParachuteType().get(player.username)) + ".png"));
            this.modelParachute.Cloud1.render(f);
            this.modelParachute.Cloud2.render(f);
            this.modelParachute.Cloud3.render(f);
            this.modelParachute.Cloud4.render(f);
            this.modelParachute.Cloud5.render(f);
            this.modelParachute.Shape1.render(f);
            this.modelParachute.Shape2.render(f);
            this.modelParachute.Shape3.render(f);
            this.modelParachute.Shape4.render(f);
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
            this.renderPlayer.getRenderManager().renderEngine.func_110577_a(((AbstractClientPlayer)player).func_110306_p());
            this.modelMisc.onGround = 0.0F;
            this.modelMisc.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
            GL11.glTranslatef(-0.01F, 0.05F, 0.01F);
            this.modelMisc.bipedRightArm.render(0.0625F);
            char var5 = 61680;
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
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

        if (inv != null)
        {
            if (inv.slots[6] != null)
            {
                GL11.glPushMatrix();
                player.getBrightness(1.0F);
                this.modelMisc.onGround = 0.0F;
                this.modelMisc.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
                this.modelMisc.bipedRightArm.render(0.0625F);
                ItemAccessory glove = (ItemAccessory)inv.slots[6].getItem();
                this.renderPlayer.getRenderManager().renderEngine.func_110577_a(glove.texture);
                int colour = glove.getColorFromItemStack(new ItemStack(glove, 1, 0), 1);
                float red = (float)(colour >> 16 & 255) / 255.0F;
                float green = (float)(colour >> 8 & 255) / 255.0F;
                float blue = (float)(colour & 255) / 255.0F;

                if (glove.colouriseRender)
                {
                    GL11.glColor3f(red, green, blue);
                }

                this.modelMisc.bipedRightArm.render(0.0625F);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
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
            GL11.glPushMatrix();
            ItemStack cape = inv.slots[1];
            if ((Aether.syncDonatorList.isDonator(entityplayer.username)) && (Aether.syncDonatorList.getDonator(entityplayer.username).containsChoiceType(EnumChoiceType.CAPE)))
            {
                DonatorTexture textureFile = Aether.syncDonatorList.getDonator(entityplayer.username).getChoiceFromType(EnumChoiceType.CAPE).textureFile;
                ResourceLocation localTexture = textureFile.localURL;

                this.renderPlayer.getRenderManager().renderEngine.func_110577_a(localTexture);
            }
            else
            {
                this.renderPlayer.getRenderManager().renderEngine.func_110577_a(((ItemAccessory)cape.getItem()).texture);
            }

            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d = entityplayer.field_71091_bM + (entityplayer.field_71094_bP - entityplayer.field_71091_bM) * f - (entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * f);
            double d1 = entityplayer.field_71096_bN + (entityplayer.field_71095_bQ - entityplayer.field_71096_bN) * f - (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * f);
            double d2 = entityplayer.field_71097_bO + (entityplayer.field_71085_bR - entityplayer.field_71097_bO) * f - (entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * f);
            float f8 = entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * f;
            double d3 = MathHelper.sin(f8 * 3.141593F / 180.0F);
            double d4 = -MathHelper.cos(f8 * 3.141593F / 180.0F);
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
            this.modelCape.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }
    }

    public void renderMisc(EntityPlayer entityplayer, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        this.modelMisc.heldItemRight = itemstack != null ? 1 : 0;
        this.modelMisc.isSneak = entityplayer.isSneaking();
        this.modelWings.heldItemRight = itemstack != null ? 1 : 0;
        this.modelWings.isSneak = entityplayer.isSneaking();
        double d3 = d1 - (double)entityplayer.yOffset;

        if (entityplayer.isSneaking() && !(entityplayer instanceof EntityPlayerSP))
        {
            d3 -= 0.125D;
        }

        this.doRenderMisc(entityplayer, d, d3, d2, f, f1);
        this.modelMisc.isSneak = false;
        this.modelMisc.heldItemRight = 0;
        this.modelWings.isSneak = false;
        this.modelWings.heldItemRight = 0;
        GL11.glPopMatrix();
    }

    public void doRenderMisc(EntityPlayer player, double d, double d1, double d2, float f, float f1)
    {
        InventoryAether inv = (InventoryAether)Aether.proxy.getClientInventories().get(player.username);

        if (inv != null)
        {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_CULL_FACE);
            this.modelMisc.onGround = this.renderPlayer.renderSwingProgress(player, f1);
            this.modelMisc.isRiding = player.isRiding();
            this.modelWings.onGround = this.renderPlayer.renderSwingProgress(player, f1);
            this.modelWings.isRiding = player.isRiding();

            try
            {
                float exception = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * f1;
                float f3 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f1;
                float f4 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f1;
                this.renderLivingAt(player, d, d1, d2);
                float f5 = this.handleRotationFloat(player, f1);
                this.rotateCorpse(player, f5, exception, f1);
                float f6 = 0.0625F;
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glScalef(-1.0F, -1.0F, 1.0F);
                this.preRenderCallback(player, f1);
                GL11.glTranslatef(0.0F, -24.0F * f6 - 0.0078125F, 0.0F);
                float f7 = player.prevLimbYaw + (player.limbYaw - player.prevLimbYaw) * f1;
                float f8 = player.limbSwing - player.limbYaw * (1.0F - f1);

                if (f7 > 1.0F)
                {
                    f7 = 1.0F;
                }

                GL11.glEnable(GL11.GL_ALPHA_TEST);
                this.modelMisc.setRotationAngles(f8, f7, f5, f3 - exception, f4, f6, player);
                this.modelWings.setRotationAngles(f8, f7, f5, f3 - exception, f4, f6, player);
                player.getBrightness(f);
                int var5;
                ItemAccessory var4;
                float var7;
                float var6;
                float blue;

                if (inv.slots[0] != null)
                {
                    var4 = (ItemAccessory)((ItemAccessory)inv.slots[0].getItem());
                    this.renderPlayer.getRenderManager().renderEngine.func_110577_a(var4.texture);
                    var5 = var4.getColorFromItemStack(new ItemStack(var4, 1, 0), 1);
                    var6 = (float)(var5 >> 16 & 255) / 255.0F;
                    var7 = (float)(var5 >> 8 & 255) / 255.0F;
                    blue = (float)(var5 & 255) / 255.0F;

                    if (var4.colouriseRender)
                    {
                        GL11.glColor3f(var6, var7, blue);
                    }

                    this.modelMisc.bipedBody.render(f6);
                }

                if (inv.slots[6] != null)
                {
                    var4 = (ItemAccessory)((ItemAccessory)inv.slots[6].getItem());
                    this.renderPlayer.getRenderManager().renderEngine.func_110577_a(var4.texture);
                    var5 = var4.getColorFromItemStack(new ItemStack(var4, 1, 0), 1);
                    var6 = (float)(var5 >> 16 & 255) / 255.0F;
                    var7 = (float)(var5 >> 8 & 255) / 255.0F;
                    blue = (float)(var5 & 255) / 255.0F;

                    if (var4.colouriseRender)
                    {
                        GL11.glColor3f(var6, var7, blue);
                    }

                    this.modelMisc.bipedLeftArm.render(f6);
                    this.modelMisc.bipedRightArm.render(f6);
                }

                if (this.wearingValkyrieArmour(player, inv) && Aether.getClientPlayer(player) != null)
                {
                    this.modelWings.sinage = Aether.getClientPlayer(player).getSinage();
                    this.modelWings.gonRound = player.onGround;
                    this.renderPlayer.getRenderManager().renderEngine.func_110577_a(TEXTURE_VALKYRIE);
                    this.modelWings.wingLeft.render(f6);
                    this.modelWings.wingRight.render(f6);
                }

                this.renderParachute(player, f6);

                if (AetherRanks.getRankFromMember(player.username).equals(AetherRanks.DEVELOPER) || AetherRanks.getRankFromMember(player.username).equals(AetherRanks.HELPER))
                {
                    GL11.glPushMatrix();
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glScalef(0.951F, 0.951F, 0.951F);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glDepthMask(true);
                    float var41 = 1.0F;
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                    this.renderPlayer.getRenderManager().renderEngine.func_110577_a(((AbstractClientPlayer)player).func_110306_p());
                    this.modelMisc.bipedBody.render(f6);
                    GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                    this.modelMisc.bipedRightLeg.render(f6);
                    this.modelMisc.bipedLeftLeg.render(f6);
                    GL11.glTranslatef(0.0F, -0.04F, 0.0F);
                    this.modelMisc.bipedRightArm.render(f6);
                    this.modelMisc.bipedLeftArm.render(f6);
                    GL11.glTranslatef(0.0F, -0.02F, 0.0F);
                    this.modelMisc.bipedHead.render(f6);
                    char var51 = 61680;
                    int var61 = var51 % 65536;
                    int var71 = var51 / 65536;
                    GL11.glEnable(GL11.GL_LIGHTING);
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var61 / 1.0F, (float)var71 / 1.0F);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, var41);
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

    public boolean wearingAccessory(int itemID, InventoryAether inv)
    {
        for (int index = 0; index < 8; ++index)
        {
            if (inv.slots[index] != null && inv.slots[index].itemID == itemID)
            {
                return true;
            }
        }

        return false;
    }

    public boolean wearingArmour(int itemID, EntityPlayer player)
    {
        for (int index = 0; index < 4; ++index)
        {
            if (player.inventory.armorInventory[index] != null && player.inventory.armorInventory[index].itemID == itemID)
            {
                return true;
            }
        }

        return false;
    }

    public boolean wearingNeptuneArmour(EntityPlayer player, InventoryAether inv)
    {
        return this.wearingArmour(AetherItems.NeptuneHelmet.itemID, player) && this.wearingArmour(AetherItems.NeptuneChestplate.itemID, player) && this.wearingArmour(AetherItems.NeptuneLeggings.itemID, player) && this.wearingArmour(AetherItems.NeptuneBoots.itemID, player) && this.wearingAccessory(AetherItems.NeptuneGloves.itemID, inv);
    }

    public boolean wearingValkyrieArmour(EntityPlayer player, InventoryAether inv)
    {
        return this.wearingArmour(AetherItems.ValkyrieHelmet.itemID, player) && this.wearingArmour(AetherItems.ValkyrieChestplate.itemID, player) && this.wearingArmour(AetherItems.ValkyrieLeggings.itemID, player) && this.wearingArmour(AetherItems.ValkyrieBoots.itemID, player) && this.wearingAccessory(AetherItems.ValkyrieGloves.itemID, inv);
    }

    public boolean wearingObsidianArmour(EntityPlayer player, InventoryAether inv)
    {
        return this.wearingArmour(AetherItems.ObsidianHelmet.itemID, player) && this.wearingArmour(AetherItems.ObsidianChestplate.itemID, player) && this.wearingArmour(AetherItems.ObsidianLeggings.itemID, player) && this.wearingArmour(AetherItems.ObsidianBoots.itemID, player) && this.wearingAccessory(AetherItems.ObsidianGloves.itemID, inv);
    }

    public boolean wearingPhoenixArmour(EntityPlayer player, InventoryAether inv)
    {
        return this.wearingArmour(AetherItems.PhoenixHelmet.itemID, player) && this.wearingArmour(AetherItems.PhoenixChestplate.itemID, player) && this.wearingArmour(AetherItems.PhoenixLeggings.itemID, player) && this.wearingArmour(AetherItems.PhoenixBoots.itemID, player) && this.wearingAccessory(AetherItems.PhoenixGloves.itemID, inv);
    }

    public boolean wearingGravititeArmour(EntityPlayer player, InventoryAether inv)
    {
        return this.wearingArmour(AetherItems.GravititeHelmet.itemID, player) && this.wearingArmour(AetherItems.GravititeChestplate.itemID, player) && this.wearingArmour(AetherItems.GravititeLeggings.itemID, player) && this.wearingArmour(AetherItems.GravititeBoots.itemID, player) && this.wearingAccessory(AetherItems.GravititeGloves.itemID, inv);
    }
}
