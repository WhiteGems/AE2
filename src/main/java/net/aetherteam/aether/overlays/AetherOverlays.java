package net.aetherteam.aether.overlays;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.aetherteam.aether.data.AetherOptions;
import net.aetherteam.aether.data.PlayerClientInfo;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.entities.bosses.EntityBossMob;
import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.aetherteam.aether.entities.mounts.Mount;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.notifications.NotificationHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.ForgeHooks;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class AetherOverlays
{
    private static long slideTime;
    private static boolean hasSlided;
    private static float scale = 1.0F;
    private static int partyAmount = 0;
    private static boolean dirty;
    private static float bossStaticHP;
    private static float linearDecrement;
    private static int bossPrevHP;
    private static int diff;
    private static int init;
    private static int bossHPTimer;

    public static void renderIronBubbles(Minecraft var0, Random var1)
    {
        ScaledResolution var2 = new ScaledResolution(var0.gameSettings, var0.displayWidth, var0.displayHeight);
        int var3 = var2.getScaledWidth();
        int var4 = var2.getScaledHeight();
        PlayerBaseAetherClient var5 = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler var6 = var5.getPlayerHandler();
        EntityPlayer var7 = var5.getPlayer();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var0.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/aethericons.png"));
        int var8 = var5.getAccessoryCount(AetherItems.IronBubble.itemID);

        if (var0.playerController.shouldDrawHUD() && var7.isInWater() && var7.isInsideOfMaterial(Material.water))
        {
            for (int var9 = 0; var9 < var8; ++var9)
            {
                drawTexturedModalRect((float)(var3 / 2 - 8 * var9 + 81), (float)(var4 - 49), 16.0F, 18.0F, 9.0F, 9.0F);
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderCooldown(Minecraft var0)
    {
        PlayerBaseAetherClient var1 = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler var2 = var1.getPlayerHandler();

        if (var1.generalcooldown != 0)
        {
            ScaledResolution var3 = new ScaledResolution(var0.gameSettings, var0.displayWidth, var0.displayHeight);
            int var4 = var3.getScaledWidth();
            int var5 = var3.getScaledHeight();
            EntityPlayer var6 = var1.getPlayer();
            var0.renderEngine.resetBoundTexture();
            var0.fontRenderer.drawStringWithShadow(Aether.proxy.getClientCooldownName().get(var6.username) + " 冷却", var4 / 2 - var0.fontRenderer.getStringWidth(Aether.proxy.getClientCooldownName().get(var6.username) + " 冷却") / 2, 32 + (var2.getCurrentBoss() != null ? 20 : 0), -1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var0.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/cooldownBar.png"));
            drawTexturedModalRect((float)(var4 / 2 - 64), (float)(42 + (var2.getCurrentBoss() != null ? 20 : 0)), 0.0F, 8.0F, 128.0F, 8.0F);
            int var7 = (int)((float)var1.generalcooldown / (float)var1.generalcooldownmax * 128.0F);
            drawTexturedModalRect((float)(var4 / 2 - 64), (float)(42 + (var2.getCurrentBoss() != null ? 20 : 0)), 0.0F, 0.0F, (float)var7, 8.0F);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public static void renderDungeonQueue(Minecraft var0)
    {
        PartyMember var1 = PartyController.instance().getMember(var0.thePlayer.username);
        Party var2 = PartyController.instance().getParty(var1);
        Dungeon var3 = DungeonHandler.instance().getDungeon(var2);
        EntityClientPlayerMP var4 = FMLClientHandler.instance().getClient().thePlayer;

        if (var3 != null && var2 != null && !var3.hasStarted())
        {
            ScaledResolution var5 = new ScaledResolution(var0.gameSettings, var0.displayWidth, var0.displayHeight);
            int var6 = var5.getScaledWidth();
            int var7 = var5.getScaledHeight();
            var0.renderEngine.resetBoundTexture();
            String var8 = "已准备成员: " + var3.getAmountQueued() + "/" + var2.getMembers().size();
            var0.fontRenderer.drawStringWithShadow(var8, var6 / 2 - var0.fontRenderer.getStringWidth(var8) / 2, 16 + (var4.ridingEntity instanceof Mount ? 16 : 0), -1);
        }
    }

    public static void renderDungeonTimer(Minecraft var0)
    {
        PartyMember var1 = PartyController.instance().getMember(var0.thePlayer.username);
        Party var2 = PartyController.instance().getParty(var1);
        Dungeon var3 = DungeonHandler.instance().getDungeon(var2);
        EntityClientPlayerMP var4 = FMLClientHandler.instance().getClient().thePlayer;

        if (var3 != null && var2 != null && var3.timerStarted() && var3.hasMember(PartyController.instance().getMember((EntityPlayer)var0.thePlayer)))
        {
            ScaledResolution var5 = new ScaledResolution(var0.gameSettings, var0.displayWidth, var0.displayHeight);
            int var6 = var5.getScaledWidth();
            int var7 = var5.getScaledHeight();

            if (!var3.timerFinished())
            {
                var0.renderEngine.resetBoundTexture();
                int var8 = (var3.getTimerLength() - var3.getTimerSeconds()) / 60;
                int var9 = var3.getTimerLength() - var3.getTimerSeconds() - var8 * 60;
                String var10 = String.valueOf(var8 + ":" + String.format("%02d", new Object[] {Integer.valueOf(var9)}));
                String var11 = "地牢即将关闭,倒计时: " + var10;
                var0.fontRenderer.drawStringWithShadow(var11, var6 / 2 - var0.fontRenderer.getStringWidth(var11) / 2, 16 + (var4.ridingEntity instanceof Mount ? 16 : 0), -1);
            }
        }
    }

    public static void renderMountHealth(Minecraft var0)
    {
        PlayerBaseAetherClient var1 = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler var2 = var1.getPlayerHandler();
        EntityClientPlayerMP var3 = FMLClientHandler.instance().getClient().thePlayer;

        if (var3.ridingEntity instanceof Mount && var3.ridingEntity instanceof EntityLiving)
        {
            EntityLiving var4 = (EntityLiving)var3.ridingEntity;

            if (var4 != null)
            {
                ScaledResolution var5 = new ScaledResolution(var0.gameSettings, var0.displayWidth, var0.displayHeight);
                int var6 = var5.getScaledWidth();
                int var7 = var5.getScaledHeight();
                float var8 = 77.0F;
                float var9 = 6.0F;
                byte var10 = 16;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var0.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/mountHealthBar.png"));
                drawTexturedModalRect((float)(var6 / 2) - var8 / 2.0F, (float)var10, 0.0F, var9, var8, var9);
                float var11 = (float)var4.getMaxHealth();
                float var12 = (float)((Mount)var4).getHealthTracked();
                int var13 = (int)(var12 / var11 * var8);
                drawTexturedModalRect((float)(var6 / 2) - var8 / 2.0F, (float)var10, 0.0F, 0.0F, (float)var13, var9);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    public static void queueCoinbarSlide()
    {
        slideTime = Minecraft.getSystemTime();
        hasSlided = false;
    }

    public static void renderCoinbar(Minecraft var0)
    {
        PlayerBaseAetherClient var1 = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler var2 = var1.getPlayerHandler();
        ScaledResolution var3 = new ScaledResolution(var0.gameSettings, var0.displayWidth, var0.displayHeight);
        int var4 = var3.getScaledWidth();
        int var5 = var3.getScaledHeight();

        if (slideTime != 0L)
        {
            double var6 = (double)(Minecraft.getSystemTime() - slideTime) / 3000.0D;

            if (!hasSlided && (var6 < 0.0D || var6 > 1.0D))
            {
                slideTime = 0L;
            }
            else
            {
                hasSlided = true;
                double var8 = var6 * 2.0D;

                if (var8 > 1.0D)
                {
                    var8 = 2.0D - var8;
                }

                var8 *= 4.0D;
                var8 = 1.0D - var8;

                if (var8 < 0.0D)
                {
                    var8 = 0.0D;
                }

                var8 *= var8;
                var8 *= var8;
                int var10 = AetherOptions.getSlideCoinbar() && var0.currentScreen == null ? 0 - (int)(var8 * 36.0D) : 0;
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                int var11 = var1.getCoins();
                ArrayList var12 = NotificationHandler.instance().getNotifications();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var0.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/coinbar.png"));
                drawTexturedModalRect((float)(var4 / 2 - 35), (float)var10, 0.0F, 0.0F, 71.0F, 15.0F);
                drawTexturedModalRect((float)(var4 / 2 - (var0.fontRenderer.getStringWidth("x" + String.valueOf(var11)) / 2 + 3) - 5), (float)(var10 + 1), 0.0F, 15.0F, 10.0F, 10.0F);
                var0.renderEngine.resetBoundTexture();
                var0.fontRenderer.drawStringWithShadow("x", var4 / 2 - (var0.fontRenderer.getStringWidth("x" + String.valueOf(var11)) / 2 + 2) + 6, var10 + 1, -1);
                var0.fontRenderer.drawStringWithShadow(String.valueOf(var11), var4 / 2 - (var0.fontRenderer.getStringWidth("x" + String.valueOf(var11)) / 2 + 2) + 13, var10 + 2, -1);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
    }

    public static void renderPartyHUD(Minecraft var0)
    {
        PlayerBaseAetherClient var1 = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler var2 = var1.getPlayerHandler();
        boolean var3 = AetherOptions.getMinimalPartyHUD();
        boolean var4 = AetherOptions.getRenderHead();
        boolean var5 = AetherOptions.getShowPartyHUD();
        boolean var6 = AetherOptions.getShowPartyName();
        ScaledResolution var7 = new ScaledResolution(var0.gameSettings, var0.displayWidth, var0.displayHeight);
        int var8 = var7.getScaledWidth();
        int var9 = var7.getScaledHeight();
        int var10 = NotificationHandler.instance().getNotifications().size();
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        if (var10 > 0)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var0.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/coinbar.png"));
            drawTexturedModalRect((float)(var8 / 2 + 37), 2.0F, 20.0F, 25.0F, 31.0F, 9.0F);
            var0.renderEngine.resetBoundTexture();
        }

        Party var11 = PartyController.instance().getParty(PartyController.instance().getMember((EntityPlayer)var0.thePlayer));
        Dungeon var12 = DungeonHandler.instance().getDungeon(var11);
        int var19;

        if (var11 != null && var12 != null && var12.hasStarted() && var12.hasMember(PartyController.instance().getMember((EntityPlayer)var0.thePlayer)))
        {
            byte var13 = 14;
            byte var14 = 14;
            String var15 = "x" + String.valueOf(var12.getKeyAmount(EnumKeyType.Guardian));
            String var16 = "x" + String.valueOf(var12.getKeyAmount(EnumKeyType.Host));
            String var17 = "x" + String.valueOf(var12.getKeyAmount(EnumKeyType.Eye));
            FontRenderer var18 = var0.fontRenderer;
            var19 = (var13 * 3 / 2 + (var18.getStringWidth(var15) + var18.getStringWidth(var16) + var18.getStringWidth(var17)) / 2) / 3;
            drawIcon(0.6F, (float)(var8 - 35 - 20 - var19), 10.0F, 39.0F, 0.0F, (float)var13, (float)var14);
            drawIcon(0.6F, (float)(var8 - 35 - var19), 10.0F, 53.0F, 0.0F, (float)var13, (float)var14);
            drawIcon(0.6F, (float)(var8 - 35 + 20 - var19), 10.0F, 67.0F, 0.0F, (float)var13, (float)var14);
            GL11.glPushMatrix();
            var0.renderEngine.resetBoundTexture();
            GL11.glTranslatef((float)(var8 - 35 - 12 - var19), 12.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 1.0F);
            var0.fontRenderer.drawStringWithShadow(var15, 0, 0, 15066597);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            var0.renderEngine.resetBoundTexture();
            GL11.glTranslatef((float)(var8 - 35 + 8 - var19), 12.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 1.0F);
            var0.fontRenderer.drawStringWithShadow(var16, 0, 0, 15066597);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            var0.renderEngine.resetBoundTexture();
            GL11.glTranslatef((float)(var8 - 35 + 28 - var19), 12.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 1.0F);
            var0.fontRenderer.drawStringWithShadow(var17, 0, 0, 15066597);
            GL11.glPopMatrix();
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();

        if (var5)
        {
            int var28 = var4 ? 0 : -18;
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            int var27 = var1.getCoins();
            var0.renderEngine.resetBoundTexture();
            Party var26 = PartyController.instance().getParty(var1.getPlayer());
            boolean var24 = var26 != null && var12 != null && var12.hasStarted() && var12.isQueuedParty(var26) && var12.hasMember(PartyController.instance().getMember((EntityPlayer)Minecraft.getMinecraft().thePlayer));
            ArrayList var23 = var24 ? var12.getQueuedMembers() : (var26 != null ? var26.getMembers() : null);

            if (var26 != null && var23 != null)
            {
                boolean var25 = false;
                partyAmount = var26.getMembers().size();
                GL11.glPushMatrix();
                var0.renderEngine.resetBoundTexture();
                GL11.glScalef(0.75F, 0.75F, 1.0F);
                var19 = var0.thePlayer.sendQueue.playerInfoList.size();

                if (var6 && var19 > 1)
                {
                    var0.fontRenderer.drawStringWithShadow("\u00a7n" + (var24 ? "地牢小队" : "公会") + ":\u00a7r " + var26.getName(), 2, 59, 15066597);
                }

                GL11.glPopMatrix();
                int var20 = 0;
                Iterator var21 = var23.iterator();

                while (var21.hasNext())
                {
                    PartyMember var22 = (PartyMember)var21.next();

                    if (var20 + 1 < var26.getMemberSizeLimit() && !var22.username.equalsIgnoreCase(var0.thePlayer.username))
                    {
                        drawPlayerSlot(var22.username, var28, 50 + 20 * var20 + 2 * partyAmount, var0, var3, var4);
                        ++var20;
                    }
                }

                var0.renderEngine.resetBoundTexture();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    public static void drawPlayerSlot(String var0, int var1, int var2, Minecraft var3, boolean var4, boolean var5)
    {
        PlayerClientInfo var6 = (PlayerClientInfo)Aether.proxy.getPlayerClientInfo().get(var0);

        if (var6 != null)
        {
            int var7 = var3.renderEngine.getTextureForDownloadableImage("http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(var0) + ".png", "/mob/char.png");
            scale = 1.35F - 0.025F * (float)partyAmount;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var7);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPushMatrix();

            if (var5)
            {
                float var8 = 0.125F;
                float var9 = 0.25F;
                float var10 = 0.25F;
                float var11 = 0.5F;
                GL11.glScalef(0.8F * scale, 0.8F * scale, 1.0F);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(var8, var9);
                GL11.glVertex2f((float)(var1 + 2), (float)(var2 + 2));
                GL11.glTexCoord2f(var8, var11);
                GL11.glVertex2f((float)(var1 + 2), (float)(var2 + 18));
                GL11.glTexCoord2f(var10, var11);
                GL11.glVertex2f((float)(var1 + 18), (float)(var2 + 18));
                GL11.glTexCoord2f(var10, var9);
                GL11.glVertex2f((float)(var1 + 18), (float)(var2 + 2));
                GL11.glEnd();
            }

            if (!var4)
            {
                drawHealthBar((float)(var1 + 21), (float)(var2 + 8), var6.getHalfHearts(), var6.getMaxHealth());
            }

            GL11.glPushMatrix();
            var3.renderEngine.resetBoundTexture();
            GL11.glTranslatef((float)(var1 + 22), (float)(var2 + 2), 1.0F);
            GL11.glScalef(0.5F * scale, 0.5F * scale, 1.0F);
            String var13 = String.valueOf(var6.getAetherCoins());
            int var12 = (int)((double)var6.getHunger() / 20.0D * 100.0D);
            String var15 = var6.getArmourValue() + "/" + 20;
            var3.fontRenderer.drawStringWithShadow(var0, 0, 0, PartyController.instance().isLeader(var0) ? 16763904 : 15066597);
            GL11.glPushMatrix();

            if (!var4)
            {
                GL11.glTranslatef(4.5F, 4.0F, 1.0F);
                GL11.glScalef(0.8F, 0.8F, 1.0F);
            }

            String var14 = var6.getHalfHearts() + "/" + var6.getMaxHealth();
            var3.fontRenderer.drawStringWithShadow(var14, var4 ? 12 : 44 - var3.fontRenderer.getStringWidth(var14) / 2, var4 ? 10 : 7, 15066597);
            var3.fontRenderer.drawStringWithShadow(var12 + "%", var4 ? 11 : 45, var4 ? 20 : 17, 15066597);

            if (!var4)
            {
                var3.fontRenderer.drawStringWithShadow(var15, 7, 17, 15066597);
            }

            GL11.glPopMatrix();
            var3.fontRenderer.drawStringWithShadow(var13, var4 ? 49 : var3.fontRenderer.getStringWidth(var0) + 17, var4 ? 20 : 0, 15066597);

            if (var4)
            {
                var3.fontRenderer.drawStringWithShadow(var15, 56, 10, 15066597);
            }

            if (var4)
            {
                drawIcon(1.1F, 0.0F, 10.0F, 18.0F, 0.0F, 9.0F, 9.0F);
                drawIcon(1.1F, 44.0F, 10.0F, 9.0F, 0.0F, 9.0F, 9.0F);
                drawIcon(1.1F, 0.0F, 20.0F, 0.0F, 0.0F, 9.0F, 9.0F);
                drawIcon(0.75F, 38.0F, 20.0F, 27.0F, 0.0F, 12.0F, 12.0F);
            }
            else
            {
                drawIcon(0.85F, 32.0F, 17.0F, 0.0F, 0.0F, 9.0F, 9.0F);
                drawIcon(0.85F, 0.0F, 17.0F, 9.0F, 0.0F, 9.0F, 9.0F);
                drawIcon(0.75F, (float)(var3.fontRenderer.getStringWidth(var0) + 6), 0.0F, 27.0F, 0.0F, 12.0F, 12.0F);
            }

            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }

    public static void drawIcon(float var0, float var1, float var2, float var3, float var4, float var5, float var6)
    {
        Minecraft var7 = Minecraft.getMinecraft();
        ScaledResolution var8 = new ScaledResolution(var7.gameSettings, var7.displayWidth, var7.displayHeight);
        int var9 = var8.getScaledWidth();
        int var10 = var8.getScaledHeight();
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var7.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyicons.png"));
        GL11.glTranslatef(var1, var2 - 0.5F, 1.0F);
        GL11.glScalef(var0, var0, 1.0F);
        drawTexturedModalRect(0.0F, 0.0F, var3, var4, var5, var6);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void drawHealthBar(float var0, float var1, int var2, int var3)
    {
        Minecraft var4 = Minecraft.getMinecraft();
        ScaledResolution var5 = new ScaledResolution(var4.gameSettings, var4.displayWidth, var4.displayHeight);
        int var6 = var5.getScaledWidth();
        int var7 = var5.getScaledHeight();
        float var8 = 77.0F;
        float var9 = 6.0F;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/mountHealthBar.png"));
        GL11.glTranslatef(var0, var1, 1.0F);
        GL11.glScalef(0.525F * scale, 0.525F * scale, 1.0F);
        drawTexturedModalRect(0.0F, 0.0F, 0.0F, var9, var8, var9);
        float var10 = (float)var3;
        float var11 = (float)var2;
        int var12 = (int)(var11 / var10 * var8);
        drawTexturedModalRect(0.0F, 0.0F, 0.0F, 0.0F, (float)var12, var9);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void drawArmorBar(int var0, int var1, int var2, int var3)
    {
        Minecraft var4 = Minecraft.getMinecraft();
        ScaledResolution var5 = new ScaledResolution(var4.gameSettings, var4.displayWidth, var4.displayHeight);
        int var6 = var5.getScaledWidth();
        int var7 = var5.getScaledHeight();
        float var8 = 81.0F;
        float var9 = 9.0F;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/armourBar.png"));
        GL11.glTranslatef((float)var0, (float)var1 - 0.5F, 1.0F);
        GL11.glScalef(0.315F * scale, 0.35F * scale, 1.0F);
        drawTexturedModalRect(0.0F, 0.0F, 0.0F, var9, var8, var9);
        float var10 = (float)var3;
        float var11 = (float)var2;
        int var12 = (int)(var11 / var10 * var8);
        drawTexturedModalRect(0.0F, 0.0F, 0.0F, 0.0F, (float)var12, var9);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    public static void renderBossHP(Minecraft var0)
    {
        PlayerBaseAetherClient var1 = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler var2 = var1.getPlayerHandler();
        IAetherBoss var3 = var2.getCurrentBoss();

        if (var2 != null && var3 != null && var3.getBossEntity() != null && !var3.getBossEntity().isDead && var3.getBossEntity() instanceof EntityBossMob && ((EntityBossMob)var3.getBossEntity()).getBossHP() > 0)
        {
            EntityBossMob var4 = (EntityBossMob)var3.getBossEntity();
            ScaledResolution var5 = new ScaledResolution(var0.gameSettings, var0.displayWidth, var0.displayHeight);
            int var6 = var5.getScaledWidth();
            int var7 = var5.getScaledHeight();
            String var8 = "\u00a7o" + var3.getBossTitle();
            int var9 = var0.fontRenderer.getStringWidth(var8) / 2;
            String var10 = "";
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var0.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/bossHPBar.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            float var11 = (float)var4.getBossHP() / (float)var4.getMaxHealth() * 256.0F;
            int var12 = 0;
            int var13 = 0;
            boolean var14 = false;
            int var15 = 0;
            byte var16 = 0;
            int var17 = 0;
            boolean var18 = false;
            boolean var19 = false;

            if (var3.getBossType() != null)
            {
                boolean var20 = var3.getBossType() == EnumBossType.BOSS;

                if (var20)
                {
                    drawTexturedModalRect((float)(var6 / 2 - 49), 10.0F, 1.0F, 57.0F, 96.0F, 58.0F);
                }

                var10 = "\u00a7o" + (var20 ? "最终" : "迷你") + "Boss";
                var12 = var20 ? 45 : 24;
                var13 = var20 ? -10 : 11;
                var14 = var20;
                var15 = var0.fontRenderer.getStringWidth(var10) / 2;
                var17 = var20 ? 0 : 14;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexturedModalRect((float)(var6 / 2 - 128), (float)(32 - var13), (float)var16, 28.0F, 256.0F, 14.0F);

            if (var4.getBossHP() != bossPrevHP && !dirty)
            {
                dirty = true;
                bossStaticHP = (float)bossPrevHP;
                diff = bossPrevHP - var4.getBossHP();
                linearDecrement = (float)diff;
                bossPrevHP = var4.getBossHP();
            }

            if (dirty)
            {
                float var21 = bossStaticHP / (float)var4.getMaxHealth() * 256.0F;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                drawTexturedModalRect((float)(var6 / 2 - 128), (float)(32 - var13), (float)var16, 42.0F, var21, 14.0F);

                if (init > 25)
                {
                    if (bossStaticHP > (float)bossPrevHP)
                    {
                        bossStaticHP -= linearDecrement / 50.0F;
                        linearDecrement -= (float)(diff / 5);
                        diff /= 5;
                    }
                    else
                    {
                        dirty = false;
                        diff = 0;
                        init = 0;
                    }
                }
                else
                {
                    ++init;
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexturedModalRect((float)(var6 / 2 - 128), (float)(32 - var13), (float)var16, (float)var17, var11, 14.0F);
            var0.renderEngine.resetBoundTexture();
            var0.fontRenderer.drawStringWithShadow(var8, var6 / 2 - var9, var12, 1744830463);
            var0.fontRenderer.drawStringWithShadow(var10, var6 / 2 - var15, var12 + 14, 1744830463);
            bossPrevHP = var4.getBossHP();
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    public static void renderJumps(Minecraft var0)
    {
        EntityClientPlayerMP var1 = FMLClientHandler.instance().getClient().thePlayer;
        PlayerBaseAetherClient var2 = Aether.getClientPlayer(var1);
        AetherCommonPlayerHandler var3 = var2.getPlayerHandler();

        if (var2.getPlayer() != null && var2.getPlayer().ridingEntity != null && var2.getPlayer().ridingEntity instanceof EntityMoa)
        {
            ScaledResolution var4 = new ScaledResolution(var0.gameSettings, var0.displayWidth, var0.displayHeight);
            EntityMoa var5 = (EntityMoa)((EntityMoa)var1.ridingEntity);
            int var6 = var4.getScaledWidth();
            int var7 = var4.getScaledHeight();
            GL11.glPushMatrix();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var0.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/jumps.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);

            for (int var8 = 0; var8 < var5.getColour().jumps; ++var8)
            {
                byte var9 = 18;
                int var10 = var6 / 2 + var8 * 8 - var5.getColour().jumps * 8 / 2;

                if (var8 < var5.getJumpsRemaining())
                {
                    drawTexturedModalRect((float)var10, (float)var9, 0.0F, 0.0F, 9.0F, 11.0F);
                }
                else
                {
                    drawTexturedModalRect((float)var10, (float)var9, 10.0F, 0.0F, 9.0F, 11.0F);
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public static void renderHearts(Minecraft var0, Random var1)
    {
        PlayerBaseAetherClient var2 = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler var3 = var2.getPlayerHandler();
        ScaledResolution var4 = new ScaledResolution(var0.gameSettings, var0.displayWidth, var0.displayHeight);
        int var5 = var4.getScaledWidth();
        int var6 = var4.getScaledHeight();
        EntityPlayer var7 = var2.getPlayer();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var0.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/aethericons.png"));
        int var8 = var7.getHealth() / 2;
        boolean var9 = var7.hurtResistantTime / 3 % 2 == 1;

        if (var7.hurtResistantTime < 10)
        {
            var9 = false;
        }

        int var10 = var7.getHealth() - var7.getMaxHealth();
        int var11 = var7.prevHealth - var7.getMaxHealth();
        var1.setSeed((long)(var2.updateCounter * 312871));

        if (var0.playerController.shouldDrawHUD())
        {
            for (int var12 = 0; var12 < var2.maxHealth / 2 - 10; ++var12)
            {
                int var13 = var6 - 50;

                if (ForgeHooks.getTotalArmorValue(var7) > 0)
                {
                    var13 -= 8;
                }

                byte var14 = 0;

                if (var9)
                {
                    var14 = 1;
                }

                int var15 = var5 / 2 - 91 + var12 * 8;

                if (var7.getHealth() <= 4)
                {
                    var13 += var1.nextInt(2);
                }

                drawTexturedModalRect((float)var15, (float)var13, (float)(16 + var14 * 9), 0.0F, 9.0F, 9.0F);

                if (var9)
                {
                    if (var12 * 2 + 1 < var11)
                    {
                        drawTexturedModalRect((float)var15, (float)var13, 70.0F, 0.0F, 9.0F, 9.0F);
                    }

                    if (var12 * 2 + 1 == var11)
                    {
                        drawTexturedModalRect((float)var15, (float)var13, 79.0F, 0.0F, 9.0F, 9.0F);
                    }
                }

                if (var12 * 2 + 1 < var10)
                {
                    drawTexturedModalRect((float)var15, (float)var13, 52.0F, 0.0F, 9.0F, 9.0F);
                }

                if (var12 * 2 + 1 == var10)
                {
                    drawTexturedModalRect((float)var15, (float)var13, 61.0F, 0.0F, 9.0F, 9.0F);
                }
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawTexturedModalRect(float var0, float var1, float var2, float var3, float var4, float var5)
    {
        PlayerBaseAetherClient var6 = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler var7 = var6.getPlayerHandler();
        float var8 = 0.00390625F;
        float var9 = 0.00390625F;
        Tessellator var10 = Tessellator.instance;
        var10.startDrawingQuads();
        var10.addVertexWithUV((double)(var0 + 0.0F), (double)(var1 + var5), (double)var6.zLevel, (double)((var2 + 0.0F) * var8), (double)((var3 + var5) * var9));
        var10.addVertexWithUV((double)(var0 + var4), (double)(var1 + var5), (double)var6.zLevel, (double)((var2 + var4) * var8), (double)((var3 + var5) * var9));
        var10.addVertexWithUV((double)(var0 + var4), (double)(var1 + 0.0F), (double)var6.zLevel, (double)((var2 + var4) * var8), (double)((var3 + 0.0F) * var9));
        var10.addVertexWithUV((double)(var0 + 0.0F), (double)(var1 + 0.0F), (double)var6.zLevel, (double)((var2 + 0.0F) * var8), (double)((var3 + 0.0F) * var9));
        var10.draw();
    }
}
