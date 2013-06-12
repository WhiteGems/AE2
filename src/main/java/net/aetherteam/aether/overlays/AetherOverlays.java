package net.aetherteam.aether.overlays;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.CommonProxy;
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
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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

    public static void renderIronBubbles(Minecraft mc, Random rand)
    {
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();

        PlayerBaseAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();

        EntityPlayer player = base.getPlayer();

        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/aethericons.png"));

        int bubbleAmount = base.getAccessoryCount(AetherItems.IronBubble.itemID);

        if ((mc.playerController.shouldDrawHUD()) && (player.isInWater()) && (player.isInsideOfMaterial(Material.water)))
        {
            for (int i = 0; i < bubbleAmount; i++)
            {
                drawTexturedModalRect(width / 2 - 8 * i + 81, height - 49, 16.0F, 18.0F, 9.0F, 9.0F);
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderCooldown(Minecraft mc)
    {
        PlayerBaseAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();

        if (base.generalcooldown != 0)
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();

            EntityPlayer player = base.getPlayer();

            mc.renderEngine.resetBoundTexture();

            mc.fontRenderer.drawStringWithShadow(Aether.proxy.getClientCooldownName().get(player.username) + " Cooldown", width / 2 - mc.fontRenderer.getStringWidth(Aether.proxy.getClientCooldownName().get(player.username) + " Cooldown") / 2, 32 + (handler.getCurrentBoss() != null ? 20 : 0), -1);

            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3008);
            GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/cooldownBar.png"));

            drawTexturedModalRect(width / 2 - 64, 42 + (handler.getCurrentBoss() != null ? 20 : 0), 0.0F, 8.0F, 128.0F, 8.0F);
            int w = (int) (base.generalcooldown / base.generalcooldownmax * 128.0F);
            drawTexturedModalRect(width / 2 - 64, 42 + (handler.getCurrentBoss() != null ? 20 : 0), 0.0F, 0.0F, w, 8.0F);

            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glEnable(3008);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public static void renderDungeonQueue(Minecraft mc)
    {
        PartyMember member = PartyController.instance().getMember(mc.thePlayer.username);
        Party party = PartyController.instance().getParty(member);
        Dungeon dungeon = DungeonHandler.instance().getDungeon(party);

        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        if ((dungeon != null) && (party != null) && (!dungeon.hasStarted()))
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();

            mc.renderEngine.resetBoundTexture();

            String dungeonQueue = "地牢队列: " + dungeon.getAmountQueued() + "/" + party.getMembers().size();

            mc.fontRenderer.drawStringWithShadow(dungeonQueue, width / 2 - mc.fontRenderer.getStringWidth(dungeonQueue) / 2, 16 + ((player.ridingEntity instanceof Mount) ? 16 : 0), -1);
        }
    }

    public static void renderDungeonTimer(Minecraft mc)
    {
        PartyMember member = PartyController.instance().getMember(mc.thePlayer.username);
        Party party = PartyController.instance().getParty(member);
        Dungeon dungeon = DungeonHandler.instance().getDungeon(party);

        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        if ((dungeon != null) && (party != null) && (dungeon.timerStarted()) && (dungeon.hasMember(PartyController.instance().getMember(mc.thePlayer))))
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();

            if (!dungeon.timerFinished())
            {
                mc.renderEngine.resetBoundTexture();

                int minutes = (dungeon.getTimerLength() - dungeon.getTimerSeconds()) / 60;
                int seconds = dungeon.getTimerLength() - dungeon.getTimerSeconds() - minutes * 60;

                String timer = String.valueOf(minutes + ":" + String.format("%02d", new Object[]{Integer.valueOf(seconds)}));
                String dungeonTimer = "地牢队列将结束: " + timer;

                mc.fontRenderer.drawStringWithShadow(dungeonTimer, width / 2 - mc.fontRenderer.getStringWidth(dungeonTimer) / 2, 16 + ((player.ridingEntity instanceof Mount) ? 16 : 0), -1);
            }
        }
    }

    public static void renderMountHealth(Minecraft mc)
    {
        PlayerBaseAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();

        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        if ((!(player.ridingEntity instanceof Mount)) || (!(player.ridingEntity instanceof EntityLiving)))
        {
            return;
        }

        EntityLiving mount = (EntityLiving) player.ridingEntity;

        if (mount != null)
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();

            float textureWidth = 77.0F;
            float textureHeight = 6.0F;

            int healthBarYOffset = 16;

            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3008);
            GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/mountHealthBar.png"));

            drawTexturedModalRect(width / 2 - textureWidth / 2.0F, healthBarYOffset, 0.0F, textureHeight, textureWidth, textureHeight);

            float mountMaxHealth = mount.getMaxHealth();
            float mountHealth = ((Mount) mount).getHealthTracked();

            int healthProgress = (int) (mountHealth / mountMaxHealth * textureWidth);

            drawTexturedModalRect(width / 2 - textureWidth / 2.0F, healthBarYOffset, 0.0F, 0.0F, healthProgress, textureHeight);

            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glEnable(3008);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public static void queueCoinbarSlide()
    {
        slideTime = Minecraft.getSystemTime();
        hasSlided = false;
    }

    public static void renderCoinbar(Minecraft mc)
    {
        PlayerBaseAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();

        if (slideTime != 0L)
        {
            double d0 = (Minecraft.getSystemTime() - slideTime) / 3000.0D;

            if ((!hasSlided) && ((d0 < 0.0D) || (d0 > 1.0D)))
            {
                slideTime = 0L;
            } else
            {
                hasSlided = true;
                double d1 = d0 * 2.0D;

                if (d1 > 1.0D)
                {
                    d1 = 2.0D - d1;
                }

                d1 *= 4.0D;
                d1 = 1.0D - d1;

                if (d1 < 0.0D)
                {
                    d1 = 0.0D;
                }

                d1 *= d1;
                d1 *= d1;

                int dynamicY = (AetherOptions.getSlideCoinbar()) && (mc.currentScreen == null) ? 0 - (int) (d1 * 36.0D) : 0;

                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(770, 771);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(3008);

                int coinAmount = base.getCoins();
                ArrayList notificationList = NotificationHandler.instance().getNotifications();

                GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/coinbar.png"));
                drawTexturedModalRect(width / 2 - 35, dynamicY, 0.0F, 0.0F, 71.0F, 15.0F);
                drawTexturedModalRect(width / 2 - (mc.fontRenderer.getStringWidth("x" + String.valueOf(coinAmount)) / 2 + 3) - 5, dynamicY + 1, 0.0F, 15.0F, 10.0F, 10.0F);

                mc.renderEngine.resetBoundTexture();

                mc.fontRenderer.drawStringWithShadow("x", width / 2 - (mc.fontRenderer.getStringWidth("x" + String.valueOf(coinAmount)) / 2 + 2) + 6, dynamicY + 1, -1);
                mc.fontRenderer.drawStringWithShadow(String.valueOf(coinAmount), width / 2 - (mc.fontRenderer.getStringWidth("x" + String.valueOf(coinAmount)) / 2 + 2) + 13, dynamicY + 2, -1);

                GL11.glDepthMask(true);
                GL11.glEnable(2929);
                GL11.glEnable(3008);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
    }

    public static void renderPartyHUD(Minecraft mc)
    {
        PlayerBaseAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();

        boolean minimalistic = AetherOptions.getMinimalPartyHUD();
        boolean renderHead = AetherOptions.getRenderHead();
        boolean showHUD = AetherOptions.getShowPartyHUD();
        boolean showName = AetherOptions.getShowPartyName();

        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();

        int notificationSize = NotificationHandler.instance().getNotifications().size();

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);

        if (notificationSize > 0)
        {
            GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/coinbar.png"));
            drawTexturedModalRect(width / 2 + 37, 2.0F, 20.0F, 25.0F, 31.0F, 9.0F);
            mc.renderEngine.resetBoundTexture();
        }

        Party possibleParty = PartyController.instance().getParty(PartyController.instance().getMember(mc.thePlayer));
        Dungeon dungeon = DungeonHandler.instance().getDungeon(possibleParty);

        if ((possibleParty != null) && (dungeon != null) && (dungeon.hasStarted()) && (dungeon.hasMember(PartyController.instance().getMember(mc.thePlayer))))
        {
            int keyWidth = 14;
            int keyLength = 14;

            String guardianAmount = "x" + String.valueOf(dungeon.getKeyAmount(EnumKeyType.Guardian));
            String hostAmount = "x" + String.valueOf(dungeon.getKeyAmount(EnumKeyType.Host));
            String eyeAmount = "x" + String.valueOf(dungeon.getKeyAmount(EnumKeyType.Eye));

            FontRenderer font = mc.fontRenderer;

            int centerOffset = (keyWidth * 3 / 2 + (font.getStringWidth(guardianAmount) + font.getStringWidth(hostAmount) + font.getStringWidth(eyeAmount)) / 2) / 3;

            drawIcon(0.6F, width - 35 - 20 - centerOffset, 10.0F, 39.0F, 0.0F, keyWidth, keyLength);
            drawIcon(0.6F, width - 35 - centerOffset, 10.0F, 53.0F, 0.0F, keyWidth, keyLength);
            drawIcon(0.6F, width - 35 + 20 - centerOffset, 10.0F, 67.0F, 0.0F, keyWidth, keyLength);

            GL11.glPushMatrix();
            mc.renderEngine.resetBoundTexture();

            GL11.glTranslatef(width - 35 - 12 - centerOffset, 12.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 1.0F);

            mc.fontRenderer.drawStringWithShadow(guardianAmount, 0, 0, 15066597);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            mc.renderEngine.resetBoundTexture();

            GL11.glTranslatef(width - 35 + 8 - centerOffset, 12.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 1.0F);

            mc.fontRenderer.drawStringWithShadow(hostAmount, 0, 0, 15066597);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            mc.renderEngine.resetBoundTexture();

            GL11.glTranslatef(width - 35 + 28 - centerOffset, 12.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 1.0F);

            mc.fontRenderer.drawStringWithShadow(eyeAmount, 0, 0, 15066597);
            GL11.glPopMatrix();
        }

        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();

        if (showHUD)
        {
            int xNegOffset = renderHead ? 0 : -18;

            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3008);

            int coinAmount = base.getCoins();

            mc.renderEngine.resetBoundTexture();

            Party party = PartyController.instance().getParty(base.getPlayer());

            boolean inDungeon = (party != null) && (dungeon != null) && (dungeon.hasStarted()) && (dungeon.isQueuedParty(party)) && (dungeon.hasMember(PartyController.instance().getMember(Minecraft.getMinecraft().thePlayer)));

            ArrayList<PartyMember> members = party != null ? party.getMembers() : inDungeon ? dungeon.getQueuedMembers() : null;

            if ((party != null) && (members != null))
            {
                int j = 0;

                partyAmount = party.getMembers().size();

                GL11.glPushMatrix();
                mc.renderEngine.resetBoundTexture();

                GL11.glScalef(0.75F, 0.75F, 1.0F);

                int serverPlayerAmount = mc.thePlayer.sendQueue.playerInfoList.size();

                if ((showName) && (serverPlayerAmount > 1))
                    mc.fontRenderer.drawStringWithShadow("§n" + (inDungeon ? "地牢小队" : "公会") + ":§r " + party.getName(), 2, 59, 15066597);
                GL11.glPopMatrix();

                int count = 0;

                for (PartyMember member : members)
                {
                    if ((count + 1 < party.getMemberSizeLimit()) && (!member.username.equalsIgnoreCase(mc.thePlayer.username)))
                    {
                        drawPlayerSlot(member.username, xNegOffset, 50 + 20 * count + 2 * partyAmount, mc, minimalistic, renderHead);

                        count++;
                    }
                }
                mc.renderEngine.resetBoundTexture();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glEnable(3008);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    public static void drawPlayerSlot(String playername, int x, int y, Minecraft mc, boolean minimalistic, boolean renderHead)
    {
        PlayerClientInfo playerClientInfo = (PlayerClientInfo) Aether.proxy.getPlayerClientInfo().get(playername);

        if (playerClientInfo != null)
        {
            int icon = mc.renderEngine.getTextureForDownloadableImage("http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(playername) + ".png", "/mob/char.png");

            scale = 1.35F - 0.025F * partyAmount;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(3553, icon);
            GL11.glEnable(3553);

            GL11.glPushMatrix();

            if (renderHead)
            {
                float u = 0.125F;
                float v = 0.25F;
                float u1 = 0.25F;
                float v1 = 0.5F;
                GL11.glScalef(0.8F * scale, 0.8F * scale, 1.0F);
                GL11.glBegin(7);
                GL11.glTexCoord2f(u, v);
                GL11.glVertex2f(x + 2, y + 2);
                GL11.glTexCoord2f(u, v1);
                GL11.glVertex2f(x + 2, y + 18);
                GL11.glTexCoord2f(u1, v1);
                GL11.glVertex2f(x + 18, y + 18);
                GL11.glTexCoord2f(u1, v);
                GL11.glVertex2f(x + 18, y + 2);
                GL11.glEnd();
            }

            if (!minimalistic)
            {
                drawHealthBar(x + 21, y + 8, playerClientInfo.getHalfHearts(), playerClientInfo.getMaxHealth());
            }

            GL11.glPushMatrix();
            mc.renderEngine.resetBoundTexture();

            GL11.glTranslatef(x + 22, y + 2, 1.0F);
            GL11.glScalef(0.5F * scale, 0.5F * scale, 1.0F);

            String coins = String.valueOf(playerClientInfo.getAetherCoins());
            int hungerPercent = (int) (playerClientInfo.getHunger() / 20.0D * 100.0D);
            String armour = playerClientInfo.getArmourValue() + "/" + 20;

            mc.fontRenderer.drawStringWithShadow(playername, 0, 0, PartyController.instance().isLeader(playername) ? 16763904 : 15066597);

            GL11.glPushMatrix();
            if (!minimalistic)
            {
                GL11.glTranslatef(4.5F, 4.0F, 1.0F);
                GL11.glScalef(0.8F, 0.8F, 1.0F);
            }

            String hearts = playerClientInfo.getHalfHearts() + "/" + playerClientInfo.getMaxHealth();

            mc.fontRenderer.drawStringWithShadow(hearts, minimalistic ? 12 : 44 - mc.fontRenderer.getStringWidth(hearts) / 2, minimalistic ? 10 : 7, 15066597);
            mc.fontRenderer.drawStringWithShadow(String.valueOf(hungerPercent) + "%", minimalistic ? 11 : 45, minimalistic ? 20 : 17, 15066597);

            if (!minimalistic) mc.fontRenderer.drawStringWithShadow(armour, 7, 17, 15066597);
            GL11.glPopMatrix();

            mc.fontRenderer.drawStringWithShadow(coins, minimalistic ? 49 : mc.fontRenderer.getStringWidth(playername) + 17, minimalistic ? 20 : 0, 15066597);

            if (minimalistic) mc.fontRenderer.drawStringWithShadow(armour, 56, 10, 15066597);

            if (minimalistic)
            {
                drawIcon(1.1F, 0.0F, 10.0F, 18.0F, 0.0F, 9.0F, 9.0F);
                drawIcon(1.1F, 44.0F, 10.0F, 9.0F, 0.0F, 9.0F, 9.0F);
                drawIcon(1.1F, 0.0F, 20.0F, 0.0F, 0.0F, 9.0F, 9.0F);

                drawIcon(0.75F, 38.0F, 20.0F, 27.0F, 0.0F, 12.0F, 12.0F);
            } else
            {
                drawIcon(0.85F, 32.0F, 17.0F, 0.0F, 0.0F, 9.0F, 9.0F);
                drawIcon(0.85F, 0.0F, 17.0F, 9.0F, 0.0F, 9.0F, 9.0F);
                drawIcon(0.75F, mc.fontRenderer.getStringWidth(playername) + 6, 0.0F, 27.0F, 0.0F, 12.0F, 12.0F);
            }

            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }

    public static void drawIcon(float scale, float x, float y, float u, float v, float width, float height)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

        int scaledWidth = scaledresolution.getScaledWidth();
        int scaledHeight = scaledresolution.getScaledHeight();

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/partyicons.png"));

        GL11.glTranslatef(x, y - 0.5F, 1.0F);
        GL11.glScalef(scale, scale, 1.0F);

        drawTexturedModalRect(0.0F, 0.0F, u, v, width, height);

        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void drawHealthBar(float x, float y, int health, int maxHealth)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

        int scaledWidth = scaledresolution.getScaledWidth();
        int scaledHeight = scaledresolution.getScaledHeight();

        float textureWidth = 77.0F;
        float textureHeight = 6.0F;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/mountHealthBar.png"));

        GL11.glTranslatef(x, y, 1.0F);
        GL11.glScalef(0.525F * scale, 0.525F * scale, 1.0F);

        drawTexturedModalRect(0.0F, 0.0F, 0.0F, textureHeight, textureWidth, textureHeight);

        float mountMaxHealth = maxHealth;
        float mountHealth = health;

        int healthProgress = (int) (mountHealth / mountMaxHealth * textureWidth);

        drawTexturedModalRect(0.0F, 0.0F, 0.0F, 0.0F, healthProgress, textureHeight);

        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void drawArmorBar(int x, int y, int armour, int maxArmour)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

        int scaledWidth = scaledresolution.getScaledWidth();
        int scaledHeight = scaledresolution.getScaledHeight();

        float textureWidth = 81.0F;
        float textureHeight = 9.0F;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/armourBar.png"));

        GL11.glTranslatef(x, y - 0.5F, 1.0F);
        GL11.glScalef(0.315F * scale, 0.35F * scale, 1.0F);

        drawTexturedModalRect(0.0F, 0.0F, 0.0F, textureHeight, textureWidth, textureHeight);

        float playerMaxArmour = maxArmour;
        float playerArmour = armour;

        int healthProgress = (int) (playerArmour / playerMaxArmour * textureWidth);

        drawTexturedModalRect(0.0F, 0.0F, 0.0F, 0.0F, healthProgress, textureHeight);

        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    public static void renderBossHP(Minecraft mc)
    {
        PlayerBaseAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();

        IAetherBoss boss = handler.getCurrentBoss();

        if ((handler == null) || (boss == null) || (boss.getBossEntity() == null) || (boss.getBossEntity().isDead) || (!(boss.getBossEntity() instanceof EntityBossMob)) || (((EntityBossMob) boss.getBossEntity()).getBossHP() <= 0))
        {
            return;
        }

        EntityBossMob bossMob = (EntityBossMob) boss.getBossEntity();

        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();

        String bossTitle = "§o" + boss.getBossTitle();
        int nameOffset = mc.fontRenderer.getStringWidth(bossTitle) / 2;
        String bossTypeString = "";

        GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/bossHPBar.png"));

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        float health = bossMob.getBossHP() / bossMob.getMaxHealth() * 256.0F;

        int offset1 = 0;
        int offset2 = 0;
        int offset3 = 0;
        int offset4 = 0;

        int x = 0;
        int y = 0;
        int u = 0;
        int v = 0;

        if (boss.getBossType() != null)
        {
            boolean isFinal = boss.getBossType() == EnumBossType.BOSS;

            if (isFinal)
            {
                drawTexturedModalRect(width / 2 - 49, 10.0F, 1.0F, 57.0F, 96.0F, 58.0F);
            }

            bossTypeString = "§o" + (isFinal ? "最终" : "迷你") + "Boss";

            offset1 = isFinal ? 45 : 24;
            offset2 = isFinal ? -10 : 11;
            offset3 = isFinal ? 7 : 0;
            offset4 = mc.fontRenderer.getStringWidth(bossTypeString) / 2;

            y = isFinal ? 0 : 14;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        drawTexturedModalRect(width / 2 - 128, 32 - offset2, x, 28.0F, 256.0F, 14.0F);

        if ((bossMob.getBossHP() != bossPrevHP) && (!dirty))
        {
            dirty = true;
            bossStaticHP = bossPrevHP;
            diff = bossPrevHP - bossMob.getBossHP();
            linearDecrement = diff;
            bossPrevHP = bossMob.getBossHP();
        }

        if (dirty)
        {
            float staticHealth = bossStaticHP / bossMob.getMaxHealth() * 256.0F;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            drawTexturedModalRect(width / 2 - 128, 32 - offset2, x, 42.0F, staticHealth, 14.0F);

            if (init > 25)
            {
                if (bossStaticHP > bossPrevHP)
                {
                    bossStaticHP -= linearDecrement / 50.0F;
                    linearDecrement -= diff / 5;
                    diff /= 5;
                } else
                {
                    dirty = false;
                    diff = 0;
                    init = 0;
                }
            } else init += 1;

        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(width / 2 - 128, 32 - offset2, x, y, health, 14.0F);

        mc.renderEngine.resetBoundTexture();

        mc.fontRenderer.drawStringWithShadow(bossTitle, width / 2 - nameOffset, offset1, 1744830463);
        mc.fontRenderer.drawStringWithShadow(bossTypeString, width / 2 - offset4, offset1 + 14, 1744830463);

        bossPrevHP = bossMob.getBossHP();

        GL11.glDisable(3042);
    }

    public static void renderJumps(Minecraft mc)
    {
        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        PlayerBaseAetherClient base = Aether.getClientPlayer(player);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();

        if ((base.getPlayer() == null) || (base.getPlayer().ridingEntity == null) || (!(base.getPlayer().ridingEntity instanceof EntityMoa)))
        {
            return;
        }
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

        EntityMoa moa = (EntityMoa) player.ridingEntity;
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();

        GL11.glPushMatrix();
        GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/jumps.png"));
        GL11.glEnable(3042);
        GL11.glBlendFunc(775, 769);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glDisable(3042);

        for (int jump = 0; jump < moa.getColour().jumps; jump++)
        {
            int yPos = 18;
            int xPos = width / 2 + jump * 8 - moa.getColour().jumps * 8 / 2;

            if (jump < moa.getJumpsRemaining())
            {
                drawTexturedModalRect(xPos, yPos, 0.0F, 0.0F, 9.0F, 11.0F);
            } else
            {
                drawTexturedModalRect(xPos, yPos, 10.0F, 0.0F, 9.0F, 11.0F);
            }
        }

        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void renderHearts(Minecraft mc, Random rand)
    {
        PlayerBaseAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();

        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();

        EntityPlayer player = base.getPlayer();

        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/aethericons.png"));

        int heartsLife = player.getHealth() / 2;
        boolean flag1 = player.hurtResistantTime / 3 % 2 == 1;

        if (player.hurtResistantTime < 10)
        {
            flag1 = false;
        }

        int halfHearts = player.getHealth() - player.getMaxHealth();
        int prevHalfHearts = player.prevHealth - player.getMaxHealth();
        rand.setSeed(base.updateCounter * 312871);

        if (mc.playerController.shouldDrawHUD())
        {
            for (int heart = 0; heart < base.maxHealth / 2 - 10; heart++)
            {
                int yPos = height - 50;

                if (ForgeHooks.getTotalArmorValue(player) > 0)
                {
                    yPos -= 8;
                }

                int k5 = 0;

                if (flag1)
                {
                    k5 = 1;
                }

                int xPos = width / 2 - 91 + heart * 8;

                if (player.getHealth() <= 4)
                {
                    yPos += rand.nextInt(2);
                }

                drawTexturedModalRect(xPos, yPos, 16 + k5 * 9, 0.0F, 9.0F, 9.0F);

                if (flag1)
                {
                    if (heart * 2 + 1 < prevHalfHearts)
                    {
                        drawTexturedModalRect(xPos, yPos, 70.0F, 0.0F, 9.0F, 9.0F);
                    }

                    if (heart * 2 + 1 == prevHalfHearts)
                    {
                        drawTexturedModalRect(xPos, yPos, 79.0F, 0.0F, 9.0F, 9.0F);
                    }
                }

                if (heart * 2 + 1 < halfHearts)
                {
                    drawTexturedModalRect(xPos, yPos, 52.0F, 0.0F, 9.0F, 9.0F);
                }

                if (heart * 2 + 1 == halfHearts)
                {
                    drawTexturedModalRect(xPos, yPos, 61.0F, 0.0F, 9.0F, 9.0F);
                }
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawTexturedModalRect(float x, float y, float u, float v, float width, float height)
    {
        PlayerBaseAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();

        float var7 = 0.0039063F;
        float var8 = 0.0039063F;
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV(x + 0.0F, y + height, base.zLevel, (u + 0.0F) * var7, (v + height) * var8);
        var9.addVertexWithUV(x + width, y + height, base.zLevel, (u + width) * var7, (v + height) * var8);
        var9.addVertexWithUV(x + width, y + 0.0F, base.zLevel, (u + width) * var7, (v + 0.0F) * var8);
        var9.addVertexWithUV(x + 0.0F, y + 0.0F, base.zLevel, (u + 0.0F) * var7, (v + 0.0F) * var8);
        var9.draw();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.overlays.AetherOverlays
 * JD-Core Version:    0.6.2
 */
