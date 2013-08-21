package net.aetherteam.aether.overlays;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.client.PlayerAetherClient;
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
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class AetherOverlays
{
    private static final ResourceLocation TEXTURE_JUMPS = new ResourceLocation("aether", "textures/gui/jumps.png");
    private static final ResourceLocation TEXTURE_BOSS_HP_BAR = new ResourceLocation("aether", "textures/gui/bossHPBar.png");
    private static final ResourceLocation TEXTURE_ARMOUR_BAR = new ResourceLocation("aether", "textures/gui/armourBar.png");
    private static final ResourceLocation TEXTURE_PARTYICONS = new ResourceLocation("aether", "textures/gui/partyicons.png");
    private static final ResourceLocation TEXTURE_COINBAR = new ResourceLocation("aether", "textures/gui/coinbar.png");
    private static final ResourceLocation TEXTURE_MOUNT_HEALTH_BAR = new ResourceLocation("aether", "textures/gui/mountHealthBar.png");
    private static final ResourceLocation TEXTURE_COOLDOWN_BAR = new ResourceLocation("aether", "textures/gui/cooldownBar.png");
    private static final ResourceLocation TEXTURE_AETHERICONS = new ResourceLocation("aether", "textures/gui/aethericons.png");
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
        PlayerAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();
        EntityPlayer player = base.getPlayer();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        mc.renderEngine.func_110577_a(TEXTURE_AETHERICONS);
        int bubbleAmount = base.getAccessoryCount(AetherItems.IronBubble.itemID);

        if (mc.playerController.shouldDrawHUD() && player.isInWater() && player.isInsideOfMaterial(Material.water))
        {
            for (int i = 0; i < bubbleAmount; ++i)
            {
                drawTexturedModalRect((float)(width / 2 - 8 * i + 81), (float)(height - 49), 16.0F, 18.0F, 9.0F, 9.0F);
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderCooldown(Minecraft mc)
    {
        PlayerAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();

        if (base.generalcooldown != 0)
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();
            EntityPlayer player = base.getPlayer();
            mc.fontRenderer.drawStringWithShadow(Aether.proxy.getClientCooldownName().get(player.username) + " Cooldown", width / 2 - mc.fontRenderer.getStringWidth(Aether.proxy.getClientCooldownName().get(player.username) + " Cooldown") / 2, 32 + (handler.getCurrentBoss() != null ? 20 : 0), -1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            mc.renderEngine.func_110577_a(TEXTURE_COOLDOWN_BAR);
            drawTexturedModalRect((float)(width / 2 - 64), (float)(42 + (handler.getCurrentBoss() != null ? 20 : 0)), 0.0F, 8.0F, 128.0F, 8.0F);
            int w = (int)((float)base.generalcooldown / (float)base.generalcooldownmax * 128.0F);
            drawTexturedModalRect((float)(width / 2 - 64), (float)(42 + (handler.getCurrentBoss() != null ? 20 : 0)), 0.0F, 0.0F, (float)w, 8.0F);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public static void renderDungeonQueue(Minecraft mc)
    {
        PartyMember member = PartyController.instance().getMember(mc.thePlayer.username);
        Party party = PartyController.instance().getParty(member);
        Dungeon dungeon = DungeonHandler.instance().getDungeon(party);
        EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;

        if (dungeon != null && party != null && !dungeon.hasStarted())
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();
            String dungeonQueue = "Dungeon Queue: " + dungeon.getAmountQueued() + "/" + party.getMembers().size();
            mc.fontRenderer.drawStringWithShadow(dungeonQueue, width / 2 - mc.fontRenderer.getStringWidth(dungeonQueue) / 2, 16 + (player.ridingEntity instanceof Mount ? 16 : 0), -1);
        }
    }

    public static void renderDungeonTimer(Minecraft mc)
    {
        PartyMember member = PartyController.instance().getMember(mc.thePlayer.username);
        Party party = PartyController.instance().getParty(member);
        Dungeon dungeon = DungeonHandler.instance().getDungeon(party);
        EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;

        if (dungeon != null && party != null && dungeon.timerStarted() && dungeon.hasMember(PartyController.instance().getMember((EntityPlayer)mc.thePlayer)))
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();

            if (!dungeon.timerFinished())
            {
                int minutes = (dungeon.getTimerLength() - dungeon.getTimerSeconds()) / 60;
                int seconds = dungeon.getTimerLength() - dungeon.getTimerSeconds() - minutes * 60;
                String timer = String.valueOf(minutes + ":" + String.format("%02d", new Object[] {Integer.valueOf(seconds)}));
                String dungeonTimer = "Dungeon Ends In: " + timer;
                mc.fontRenderer.drawStringWithShadow(dungeonTimer, width / 2 - mc.fontRenderer.getStringWidth(dungeonTimer) / 2, 16 + (player.ridingEntity instanceof Mount ? 16 : 0), -1);
            }
        }
    }

    public static void renderMountHealth(Minecraft mc)
    {
        PlayerAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();
        EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;

        if (player.ridingEntity instanceof Mount && player.ridingEntity instanceof EntityLiving)
        {
            EntityLiving mount = (EntityLiving)player.ridingEntity;

            if (mount != null)
            {
                ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
                int width = scaledresolution.getScaledWidth();
                int height = scaledresolution.getScaledHeight();
                float textureWidth = 77.0F;
                float textureHeight = 6.0F;
                byte healthBarYOffset = 16;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                mc.renderEngine.func_110577_a(TEXTURE_MOUNT_HEALTH_BAR);
                drawTexturedModalRect((float)(width / 2) - textureWidth / 2.0F, (float)healthBarYOffset, 0.0F, textureHeight, textureWidth, textureHeight);
                float mountMaxHealth = mount.func_110138_aP();
                float mountHealth = (float)((Mount)mount).getHealthTracked();
                int healthProgress = (int)(mountHealth / mountMaxHealth * textureWidth);
                drawTexturedModalRect((float)(width / 2) - textureWidth / 2.0F, (float)healthBarYOffset, 0.0F, 0.0F, (float)healthProgress, textureHeight);
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

    public static void renderCoinbar(Minecraft mc)
    {
        PlayerAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();

        if (slideTime != 0L)
        {
            double d0 = (double)(Minecraft.getSystemTime() - slideTime) / 3000.0D;

            if (!hasSlided && (d0 < 0.0D || d0 > 1.0D))
            {
                slideTime = 0L;
            }
            else
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
                int dynamicY = AetherOptions.getSlideCoinbar() && mc.currentScreen == null ? 0 - (int)(d1 * 36.0D) : 0;
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                int coinAmount = base.getCoins();
                ArrayList notificationList = NotificationHandler.instance().getNotifications();
                mc.renderEngine.func_110577_a(TEXTURE_COINBAR);
                drawTexturedModalRect((float)(width / 2 - 35), (float)dynamicY, 0.0F, 0.0F, 71.0F, 15.0F);
                drawTexturedModalRect((float)(width / 2 - (mc.fontRenderer.getStringWidth("x" + String.valueOf(coinAmount)) / 2 + 3) - 5), (float)(dynamicY + 1), 0.0F, 15.0F, 10.0F, 10.0F);
                mc.fontRenderer.drawStringWithShadow("x", width / 2 - (mc.fontRenderer.getStringWidth("x" + String.valueOf(coinAmount)) / 2 + 2) + 6, dynamicY + 1, -1);
                mc.fontRenderer.drawStringWithShadow(String.valueOf(coinAmount), width / 2 - (mc.fontRenderer.getStringWidth("x" + String.valueOf(coinAmount)) / 2 + 2) + 13, dynamicY + 2, -1);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
    }

    public static void renderPartyHUD(Minecraft mc)
    {
        PlayerAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
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
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        if (notificationSize > 0)
        {
            mc.renderEngine.func_110577_a(TEXTURE_COINBAR);
            drawTexturedModalRect((float)(width / 2 + 37), 2.0F, 20.0F, 25.0F, 31.0F, 9.0F);
        }

        Party possibleParty = PartyController.instance().getParty(PartyController.instance().getMember((EntityPlayer)mc.thePlayer));
        Dungeon dungeon = DungeonHandler.instance().getDungeon(possibleParty);
        int serverPlayerAmount;

        if (possibleParty != null && dungeon != null && dungeon.hasStarted() && dungeon.hasMember(PartyController.instance().getMember((EntityPlayer)mc.thePlayer)))
        {
            byte xNegOffset = 14;
            byte coinAmount = 14;
            String party = "x" + String.valueOf(dungeon.getKeyAmount(EnumKeyType.Guardian));
            String inDungeon = "x" + String.valueOf(dungeon.getKeyAmount(EnumKeyType.Host));
            String members = "x" + String.valueOf(dungeon.getKeyAmount(EnumKeyType.Eye));
            FontRenderer j = mc.fontRenderer;
            serverPlayerAmount = (xNegOffset * 3 / 2 + (j.getStringWidth(party) + j.getStringWidth(inDungeon) + j.getStringWidth(members)) / 2) / 3;
            drawIcon(0.6F, (float)(width - 35 - 20 - serverPlayerAmount), 10.0F, 39.0F, 0.0F, (float)xNegOffset, (float)coinAmount);
            drawIcon(0.6F, (float)(width - 35 - serverPlayerAmount), 10.0F, 53.0F, 0.0F, (float)xNegOffset, (float)coinAmount);
            drawIcon(0.6F, (float)(width - 35 + 20 - serverPlayerAmount), 10.0F, 67.0F, 0.0F, (float)xNegOffset, (float)coinAmount);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(width - 35 - 12 - serverPlayerAmount), 12.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 1.0F);
            mc.fontRenderer.drawStringWithShadow(party, 0, 0, 15066597);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(width - 35 + 8 - serverPlayerAmount), 12.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 1.0F);
            mc.fontRenderer.drawStringWithShadow(inDungeon, 0, 0, 15066597);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(width - 35 + 28 - serverPlayerAmount), 12.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 1.0F);
            mc.fontRenderer.drawStringWithShadow(members, 0, 0, 15066597);
            GL11.glPopMatrix();
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();

        if (showHUD)
        {
            int var23 = renderHead ? 0 : -18;
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            int var25 = base.getCoins();
            Party var24 = PartyController.instance().getParty(base.getPlayer());
            boolean var26 = var24 != null && dungeon != null && dungeon.hasStarted() && dungeon.isQueuedParty(var24) && dungeon.hasMember(PartyController.instance().getMember((EntityPlayer)Minecraft.getMinecraft().thePlayer));
            ArrayList var27 = var26 ? dungeon.getQueuedMembers() : (var24 != null ? var24.getMembers() : null);

            if (var24 != null && var27 != null)
            {
                boolean var28 = false;
                partyAmount = var24.getMembers().size();
                GL11.glPushMatrix();
                GL11.glScalef(0.75F, 0.75F, 1.0F);
                serverPlayerAmount = mc.thePlayer.sendQueue.playerInfoList.size();

                if (showName && serverPlayerAmount > 1)
                {
                    mc.fontRenderer.drawStringWithShadow("\u00a7n" + (var26 ? "Dungeon Group" : "Party") + ":\u00a7r " + var24.getName(), 2, 59, 15066597);
                }

                GL11.glPopMatrix();
                int count = 0;
                Iterator i$ = var27.iterator();

                while (i$.hasNext())
                {
                    PartyMember member = (PartyMember)i$.next();

                    if (count + 1 < var24.getMemberSizeLimit() && !member.username.equalsIgnoreCase(mc.thePlayer.username))
                    {
                        drawPlayerSlot(member.username, var23, 50 + 20 * count + 2 * partyAmount, mc, minimalistic, renderHead);
                        ++count;
                    }
                }
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    public static void drawPlayerSlot(String playername, int x, int y, Minecraft mc, boolean minimalistic, boolean renderHead)
    {
        PlayerClientInfo playerClientInfo = (PlayerClientInfo)Aether.proxy.getPlayerClientInfo().get(playername);

        if (playerClientInfo != null)
        {
            AbstractClientPlayer player = (AbstractClientPlayer)mc.theWorld.getPlayerEntityByName(playername);
            scale = 1.35F - 0.025F * (float)partyAmount;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.func_110577_a(player.func_110306_p());
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPushMatrix();

            if (renderHead)
            {
                float coins = 0.125F;
                float hungerPercent = 0.25F;
                float armour = 0.25F;
                float hearts = 0.5F;
                GL11.glScalef(0.8F * scale, 0.8F * scale, 1.0F);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(coins, hungerPercent);
                GL11.glVertex2f((float)(x + 2), (float)(y + 2));
                GL11.glTexCoord2f(coins, hearts);
                GL11.glVertex2f((float)(x + 2), (float)(y + 18));
                GL11.glTexCoord2f(armour, hearts);
                GL11.glVertex2f((float)(x + 18), (float)(y + 18));
                GL11.glTexCoord2f(armour, hungerPercent);
                GL11.glVertex2f((float)(x + 18), (float)(y + 2));
                GL11.glEnd();
            }

            if (!minimalistic)
            {
                drawHealthBar((float)(x + 21), (float)(y + 8), playerClientInfo.getHalfHearts(), playerClientInfo.getMaxHealth());
            }

            GL11.glPushMatrix();
            GL11.glTranslatef((float)(x + 22), (float)(y + 2), 1.0F);
            GL11.glScalef(0.5F * scale, 0.5F * scale, 1.0F);
            String coins1 = String.valueOf(playerClientInfo.getAetherCoins());
            int hungerPercent1 = (int)((double)playerClientInfo.getHunger() / 20.0D * 100.0D);
            String armour1 = playerClientInfo.getArmourValue() + "/" + 20;
            mc.fontRenderer.drawStringWithShadow(playername, 0, 0, PartyController.instance().isLeader(playername) ? 16763904 : 15066597);
            GL11.glPushMatrix();

            if (!minimalistic)
            {
                GL11.glTranslatef(4.5F, 4.0F, 1.0F);
                GL11.glScalef(0.8F, 0.8F, 1.0F);
            }

            String hearts1 = playerClientInfo.getHalfHearts() + "/" + playerClientInfo.getMaxHealth();
            mc.fontRenderer.drawStringWithShadow(hearts1, minimalistic ? 12 : 44 - mc.fontRenderer.getStringWidth(hearts1) / 2, minimalistic ? 10 : 7, 15066597);
            mc.fontRenderer.drawStringWithShadow(hungerPercent1 + "%", minimalistic ? 11 : 45, minimalistic ? 20 : 17, 15066597);

            if (!minimalistic)
            {
                mc.fontRenderer.drawStringWithShadow(armour1, 7, 17, 15066597);
            }

            GL11.glPopMatrix();
            mc.fontRenderer.drawStringWithShadow(coins1, minimalistic ? 49 : mc.fontRenderer.getStringWidth(playername) + 17, minimalistic ? 20 : 0, 15066597);

            if (minimalistic)
            {
                mc.fontRenderer.drawStringWithShadow(armour1, 56, 10, 15066597);
            }

            if (minimalistic)
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
                drawIcon(0.75F, (float)(mc.fontRenderer.getStringWidth(playername) + 6), 0.0F, 27.0F, 0.0F, 12.0F, 12.0F);
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
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        mc.renderEngine.func_110577_a(TEXTURE_PARTYICONS);
        GL11.glTranslatef(x, y - 0.5F, 1.0F);
        GL11.glScalef(scale, scale, 1.0F);
        drawTexturedModalRect(0.0F, 0.0F, u, v, width, height);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
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
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        mc.renderEngine.func_110577_a(TEXTURE_MOUNT_HEALTH_BAR);
        GL11.glTranslatef(x, y, 1.0F);
        GL11.glScalef(0.525F * scale, 0.525F * scale, 1.0F);
        drawTexturedModalRect(0.0F, 0.0F, 0.0F, textureHeight, textureWidth, textureHeight);
        float mountMaxHealth = (float)maxHealth;
        float mountHealth = (float)health;
        int healthProgress = (int)(mountHealth / mountMaxHealth * textureWidth);
        drawTexturedModalRect(0.0F, 0.0F, 0.0F, 0.0F, (float)healthProgress, textureHeight);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
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
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        mc.renderEngine.func_110577_a(TEXTURE_ARMOUR_BAR);
        GL11.glTranslatef((float)x, (float)y - 0.5F, 1.0F);
        GL11.glScalef(0.315F * scale, 0.35F * scale, 1.0F);
        drawTexturedModalRect(0.0F, 0.0F, 0.0F, textureHeight, textureWidth, textureHeight);
        float playerMaxArmour = (float)maxArmour;
        float playerArmour = (float)armour;
        int healthProgress = (int)(playerArmour / playerMaxArmour * textureWidth);
        drawTexturedModalRect(0.0F, 0.0F, 0.0F, 0.0F, (float)healthProgress, textureHeight);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    public static void renderBossHP(Minecraft mc)
    {
        PlayerAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();
        IAetherBoss boss = handler.getCurrentBoss();

        if (handler != null && boss != null && boss.getBossEntity() != null && !boss.getBossEntity().isDead && boss.getBossEntity() instanceof EntityBossMob && ((EntityBossMob)boss.getBossEntity()).getBossHP() > 0)
        {
            EntityBossMob bossMob = (EntityBossMob)boss.getBossEntity();
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();
            String bossTitle = "\u00a7o" + boss.getBossTitle();
            int nameOffset = mc.fontRenderer.getStringWidth(bossTitle) / 2;
            String bossTypeString = "";
            mc.renderEngine.func_110577_a(TEXTURE_BOSS_HP_BAR);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            float health = (float)bossMob.getBossHP() / bossMob.func_110138_aP() * 256.0F;
            int offset1 = 0;
            int offset2 = 0;
            boolean offset3 = false;
            int offset4 = 0;
            byte x = 0;
            int y = 0;
            boolean u = false;
            boolean v = false;

            if (boss.getBossType() != null)
            {
                boolean staticHealth = boss.getBossType() == EnumBossType.BOSS;

                if (staticHealth)
                {
                    drawTexturedModalRect((float)(width / 2 - 49), 10.0F, 1.0F, 57.0F, 96.0F, 58.0F);
                }

                bossTypeString = "\u00a7o" + (staticHealth ? "Final" : "Mini") + " Boss";
                offset1 = staticHealth ? 45 : 24;
                offset2 = staticHealth ? -10 : 11;
                offset3 = staticHealth;
                offset4 = mc.fontRenderer.getStringWidth(bossTypeString) / 2;
                y = staticHealth ? 0 : 14;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexturedModalRect((float)(width / 2 - 128), (float)(32 - offset2), (float)x, 28.0F, 256.0F, 14.0F);

            if (bossMob.getBossHP() != bossPrevHP && !dirty)
            {
                dirty = true;
                bossStaticHP = (float)bossPrevHP;
                diff = bossPrevHP - bossMob.getBossHP();
                linearDecrement = (float)diff;
                bossPrevHP = bossMob.getBossHP();
            }

            if (dirty)
            {
                float var21 = bossStaticHP / bossMob.func_110138_aP() * 256.0F;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                drawTexturedModalRect((float)(width / 2 - 128), (float)(32 - offset2), (float)x, 42.0F, var21, 14.0F);

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
            drawTexturedModalRect((float)(width / 2 - 128), (float)(32 - offset2), (float)x, (float)y, health, 14.0F);
            mc.fontRenderer.drawStringWithShadow(bossTitle, width / 2 - nameOffset, offset1, 1744830463);
            mc.fontRenderer.drawStringWithShadow(bossTypeString, width / 2 - offset4, offset1 + 14, 1744830463);
            bossPrevHP = bossMob.getBossHP();
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    public static void renderJumps(Minecraft mc)
    {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;
        PlayerAetherClient base = Aether.getClientPlayer(player);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();

        if (base.getPlayer() != null && base.getPlayer().ridingEntity != null && base.getPlayer().ridingEntity instanceof EntityMoa)
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            EntityMoa moa = (EntityMoa)((EntityMoa)player.ridingEntity);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();
            GL11.glPushMatrix();
            mc.renderEngine.func_110577_a(TEXTURE_JUMPS);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);

            for (int jump = 0; jump < moa.getColour().jumps; ++jump)
            {
                byte yPos = 18;
                int xPos = width / 2 + jump * 8 - moa.getColour().jumps * 8 / 2;

                if (jump < moa.getJumpsRemaining())
                {
                    drawTexturedModalRect((float)xPos, (float)yPos, 0.0F, 0.0F, 9.0F, 11.0F);
                }
                else
                {
                    drawTexturedModalRect((float)xPos, (float)yPos, 10.0F, 0.0F, 9.0F, 11.0F);
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public static void renderHearts(Minecraft mc, Random rand)
    {
        PlayerAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        EntityPlayer player = base.getPlayer();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        int heartsLife = (int)(player.func_110143_aJ() / 2.0F);
        boolean flag1 = player.hurtResistantTime / 3 % 2 == 1;

        if (player.hurtResistantTime < 10)
        {
            flag1 = false;
        }

        int halfHearts = (int)(player.func_110143_aJ() - player.func_110138_aP());
        int prevHalfHearts = (int)(player.prevHealth - player.func_110138_aP());
        rand.setSeed((long)(base.updateCounter * 312871));

        if (mc.playerController.shouldDrawHUD())
        {
            for (int heart = 0; (float)heart < player.func_110138_aP() / 2.0F - 10.0F; ++heart)
            {
                int yPos = height - 50;

                if (ForgeHooks.getTotalArmorValue(player) > 0)
                {
                    yPos -= 8;
                }

                boolean k5 = false;

                if (flag1)
                {
                    k5 = true;
                }

                int xPos = width / 2 - 91 + heart * 8;

                if (player.func_110143_aJ() <= 4.0F)
                {
                    int var10000 = yPos + rand.nextInt(2);
                }

                if (flag1)
                {
                    if (heart * 2 + 1 < prevHalfHearts)
                    {
                        ;
                    }

                    if (heart * 2 + 1 == prevHalfHearts)
                    {
                        ;
                    }
                }

                if (heart * 2 + 1 < halfHearts)
                {
                    ;
                }

                if (heart * 2 + 1 == halfHearts)
                {
                    ;
                }
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawTexturedModalRect(float x, float y, float u, float v, float width, float height)
    {
        PlayerAetherClient base = Aether.getClientPlayer(FMLClientHandler.instance().getClient().thePlayer);
        AetherCommonPlayerHandler handler = base.getPlayerHandler();
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV((double)(x + 0.0F), (double)(y + height), (double)base.zLevel, (double)((u + 0.0F) * var7), (double)((v + height) * var8));
        var9.addVertexWithUV((double)(x + width), (double)(y + height), (double)base.zLevel, (double)((u + width) * var7), (double)((v + height) * var8));
        var9.addVertexWithUV((double)(x + width), (double)(y + 0.0F), (double)base.zLevel, (double)((u + width) * var7), (double)((v + 0.0F) * var8));
        var9.addVertexWithUV((double)(x + 0.0F), (double)(y + 0.0F), (double)base.zLevel, (double)((u + 0.0F) * var7), (double)((v + 0.0F) * var8));
        var9.draw();
    }
}
