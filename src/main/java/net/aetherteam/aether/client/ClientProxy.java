package net.aetherteam.aether.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.AetherEventReceiver;
import net.aetherteam.aether.AetherKeyHandler;
import net.aetherteam.aether.AetherPoison;
import net.aetherteam.aether.AetherSoundLoader;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.MenuBaseAetherII;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.client.gui.AetherGuis;
import net.aetherteam.aether.client.gui.GuiAetherIngame;
import net.aetherteam.aether.client.gui.social.GuiDungeonScreen;
import net.aetherteam.aether.client.renders.AetherEntityRenderers;
import net.aetherteam.aether.client.renders.RenderAetherTallGrass;
import net.aetherteam.aether.client.renders.RenderBerryBush;
import net.aetherteam.aether.client.renders.RenderHandlerAltar;
import net.aetherteam.aether.client.renders.RenderHandlerSkyrootChest;
import net.aetherteam.aether.client.renders.RenderHandlerTreasureChest;
import net.aetherteam.aether.entities.EntityAetherBreakingFX;
import net.aetherteam.aether.entities.EntityAetherPortalFX;
import net.aetherteam.aether.entities.EntityCloudSmokeFX;
import net.aetherteam.aether.entities.EntityGoldenFX;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.notifications.client.ClientNotificationHandler;
import net.aetherteam.aether.tile_entities.TileEntityAltar;
import net.aetherteam.aether.tile_entities.TileEntityAltarRenderer;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.aetherteam.aether.tile_entities.TileEntityEntranceRenderer;
import net.aetherteam.aether.tile_entities.TileEntitySkyrootChest;
import net.aetherteam.aether.tile_entities.TileEntitySkyrootChestRenderer;
import net.aetherteam.aether.tile_entities.TileEntityTreasureChest;
import net.aetherteam.aether.tile_entities.TileEntityTreasureChestRenderer;
import net.aetherteam.mainmenu_api.MainMenuAPI;
import net.aetherteam.playercore_api.PlayerCoreAPI;
import net.aetherteam.playercore_api.PlayerCoreAPI.PlayerCoreType;
import net.aetherteam.playercore_api.cores.PlayerCoreClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    private HashMap playerInventories = new HashMap();
    private HashMap playerExtraHearts = new HashMap();
    private HashMap playerCooldowns = new HashMap();
    private HashMap playerMaxCooldowns = new HashMap();
    private HashMap playerCooldownName = new HashMap();
    private HashMap playerCoins = new HashMap();
    private HashMap playerParachutes = new HashMap();
    private HashMap playerParachuteTypes = new HashMap();
    private HashMap playerClientInfo = new HashMap();
    private static String soundZipPath = "/resources/";
    private Minecraft mc = Minecraft.getMinecraft();
    public Random rand = new Random();

    @SideOnly(Side.CLIENT)
    public AetherCommonPlayerHandler getPlayerHandler(EntityPlayer var1)
    {
        if (var1 instanceof EntityPlayerSP)
        {
            PlayerBaseAetherClient var2 = (PlayerBaseAetherClient)((PlayerCoreClient)var1).getPlayerCoreObject(PlayerBaseAetherClient.class);
            return var2.getPlayerHandler();
        }
        else
        {
            return super.getPlayerHandler(var1);
        }
    }

    public void registerRenderers()
    {
        Aether var10000 = Aether.instance;
        Aether.syncDonatorList.initialVerification(this.mc);
        AetherGuis.init();
        AetherEntityRenderers.registerEntityRenderers();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAltar.class, new TileEntityAltarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChest.class, new TileEntityTreasureChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyrootChest.class, new TileEntitySkyrootChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEntranceController.class, new TileEntityEntranceRenderer());
        AetherBlocks.altarRenderId = RenderingRegistry.getNextAvailableRenderId();
        AetherBlocks.treasureChestRenderId = RenderingRegistry.getNextAvailableRenderId();
        AetherBlocks.skyrootChestRenderId = RenderingRegistry.getNextAvailableRenderId();
        AetherBlocks.entranceRenderId = RenderingRegistry.getNextAvailableRenderId();
        AetherBlocks.berryBushRenderId = RenderingRegistry.getNextAvailableRenderId();
        AetherBlocks.tallAetherGrassRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderBerryBush());
        RenderingRegistry.registerBlockHandler(new RenderAetherTallGrass());
        RenderingRegistry.registerBlockHandler(new RenderHandlerAltar());
        RenderingRegistry.registerBlockHandler(new RenderHandlerTreasureChest());
        RenderingRegistry.registerBlockHandler(new RenderHandlerSkyrootChest());
        MinecraftForge.EVENT_BUS.register(new AetherEventReceiver());
        MainMenuAPI.registerMenu("Aether II", MenuBaseAetherII.class);
    }

    public void registerPlayerAPI()
    {
        PlayerCoreAPI.register(PlayerCoreType.CLIENT, PlayerBaseAetherClient.class);
        PlayerCoreAPI.register(PlayerCoreType.SERVER, PlayerBaseAetherServer.class);
    }

    public void registerMainMenu() {}

    public void registerTickHandler()
    {
        MinecraftForge.EVENT_BUS.register(new GuiAetherIngame(Minecraft.getMinecraft()));
        TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
    }

    public void playSoundFX(String var1, float var2, float var3)
    {
        this.mc.sndManager.playSoundFX(var1, var2, var3);
    }

    public int addArmor(String var1)
    {
        return RenderingRegistry.addNewArmourRendererPrefix(var1);
    }

    public void registerKeyBindings()
    {
        KeyBindingRegistry.registerKeyBinding(new AetherKeyHandler());
    }

    public Minecraft getClient()
    {
        return FMLClientHandler.instance().getClient();
    }

    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    public EntityPlayer getClientPlayer()
    {
        return FMLClientHandler.instance().getClient().thePlayer;
    }

    public void loadSounds()
    {
        if (this.mc.sndManager != null)
        {
            this.installSound("music/aether1.ogg");
            this.installSound("music/aether2.ogg");
            this.installSound("music/aether3.ogg");
            this.installSound("music/aether4.ogg");
            this.installSound("music/aether5.ogg");
            this.installSound("music/Approaches.ogg");
            this.installSound("streaming/Aether Menu.ogg");
            this.installSound("streaming/Aether Menu Two.wav");
            this.installSound("streaming/Spectrum.ogg");
            this.installSound("streaming/Aether Day 1.ogg");
            this.installSound("streaming/Aether Night 1.ogg");
            this.installSound("streaming/Aether Night 2.ogg");
            this.installSound("streaming/Dungeon Background.ogg");
            this.installSound("streaming/Approaches.ogg");
            this.installSound("streaming/Demise.ogg");
            this.installSound("streaming/Aerwhale.ogg");
            this.installSound("streaming/Aether Tune.ogg");
            this.installSound("streaming/Ascending Dawn.ogg");
            this.installSound("streaming/Slider Battle.ogg");
            this.installSound("streaming/Slider Finish.ogg");
            this.installSound("newsound/aeboss/slider/awake.ogg");
            this.installSound("newsound/aeboss/slider/collide.ogg");
            this.installSound("newsound/aeboss/slider/die.ogg");
            this.installSound("newsound/aeboss/slider/move.ogg");
            this.installSound("newsound/aeboss/slider/unlock.ogg");
            this.installSound("newsound/aemob/aerbunny/die.ogg");
            this.installSound("newsound/aemob/aerbunny/hurt1.ogg");
            this.installSound("newsound/aemob/aerbunny/hurt2.ogg");
            this.installSound("newsound/aemob/aerbunny/land.ogg");
            this.installSound("newsound/aemob/aerbunny/lift.ogg");
            this.installSound("newsound/aemob/aerwhale/say.wav");
            this.installSound("newsound/aemob/aerwhale/die.wav");
            this.installSound("newsound/aemob/moa/say.wav");
            this.installSound("newsound/aemob/zephyr/say1.wav");
            this.installSound("newsound/aemob/zephyr/say2.wav");
            this.installSound("newsound/aemob/zephyr/shoot.ogg");
            this.installSound("newsound/aemisc/achieveGen.ogg");
            this.installSound("newsound/aemisc/achieveBronze.ogg");
            this.installSound("newsound/aemisc/achieveSilver.ogg");
            this.installSound("newsound/aemisc/achieveBronzeNew.ogg");
            this.installSound("newsound/aemisc/activateTrap.ogg");
            this.installSound("newsound/aemisc/shootDart.ogg");
            this.installSound("newsound/aemob/sentryGolem/seenEnemy.ogg");
            this.installSound("newsound/aemob/sentryGolem/creepySeen.wav");
            this.installSound("newsound/aemob/sentryGolem/say1.wav");
            this.installSound("newsound/aemob/sentryGolem/say2.wav");
            this.installSound("newsound/aemob/sentryGolem/death.wav");
            this.installSound("newsound/aemob/sentryGolem/hit1.wav");
            this.installSound("newsound/aemob/sentryGolem/hit2.wav");
            this.installSound("newsound/aemob/sentryGuardian/death.wav");
            this.installSound("newsound/aemob/sentryGuardian/spawn.ogg");
            this.installSound("newsound/aemob/sentryGuardian/hit.ogg");
            this.installSound("newsound/aemob/sentryGuardian/living.ogg");
            this.installSound("newsound/aemisc/coin.ogg");
            this.installSound("newsound/aemob/cog/wall.wav");
            this.installSound("newsound/aemob/cog/wall1.wav");
            this.installSound("newsound/aemob/cog/wall2.wav");
            this.installSound("newsound/aemob/cog/wallFinal.ogg");
            this.installSound("newsound/aeportal/aeportal.wav");
            this.installSound("newsound/aeportal/aetravel.wav");
            this.installSound("newsound/aeportal/aetrigger.wav");
            this.installSound("newsound/aemob/labyrinthsEye/cogloss.ogg");
            this.installSound("newsound/aemob/labyrinthsEye/eyedeath.ogg");
            this.installSound("newsound/aemob/labyrinthsEye/move_1.ogg");
            this.installSound("newsound/aemob/labyrinthsEye/move_2.ogg");
        }
    }

    public void registerSounds()
    {
        MinecraftForge.EVENT_BUS.register(new AetherSoundLoader());
    }

    private void installSound(String var1)
    {
        File var2 = new File(this.mc.mcDataDir, "resources/" + var1);

        if (!var2.exists())
        {
            try
            {
                String var3 = soundZipPath + var1;
                InputStream var4 = Aether.class.getResourceAsStream(var3);

                if (var4 == null)
                {
                    throw new IOException();
                }

                if (!var2.getParentFile().exists())
                {
                    var2.getParentFile().mkdirs();
                }

                BufferedInputStream var5 = new BufferedInputStream(var4);
                BufferedOutputStream var6 = new BufferedOutputStream(new FileOutputStream(var2));
                byte[] var7 = new byte[1024];
                boolean var8 = false;
                int var10;

                while (-1 != (var10 = var5.read(var7)))
                {
                    var6.write(var7, 0, var10);
                }

                var5.close();
                var6.close();
            }
            catch (IOException var9)
            {
                ;
            }
        }

        if (var2.canRead() && var2.isFile())
        {
            this.mc.installResource(var1, var2);
        }
        else
        {
            System.err.println("Could not load file: " + var2);
        }
    }

    public void openDungeonQueue()
    {
        this.mc.displayGuiScreen(new GuiDungeonScreen(this.mc));
    }

    @SideOnly(Side.CLIENT)
    public void renderGameOverlay(float var1, boolean var2, int var3, int var4)
    {
        if (Aether.getClientPlayer(this.getClientPlayer()) != null)
        {
            ClientNotificationHandler.updateNotifications();
            AetherPoison.displayCureEffect();
            AetherPoison.displayPoisonEffect();
        }
    }

    public void displayMessage(EntityPlayer var1, String var2)
    {
        var1.addChatMessage(var2);
    }

    public void registerRenderPAPI()
    {
        PlayerCoreAPI.register(PlayerCoreType.RENDER, RenderPlayerBaseAether.class);
    }

    public void spawnSwettyParticles(World var1, int var2, int var3, int var4)
    {
        for (int var5 = 0; var5 < 5; ++var5)
        {
            EntityAetherBreakingFX var6 = new EntityAetherBreakingFX(var1, (double)var2 + this.rand.nextDouble(), (double)var3 + this.rand.nextDouble(), (double)var4 + this.rand.nextDouble(), AetherItems.SwettyBall);
            var6.renderDistanceWeight = 10.0D;
            var6.setParticleTextureIndex(143);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(var6);
        }
    }

    public void spawnAltarParticles(World var1, int var2, int var3, int var4, Random var5)
    {
        byte var6 = 50;

        for (int var7 = 0; var7 < var6; ++var7)
        {
            EntityGoldenFX var8 = new EntityGoldenFX(var1, (double)((float)var2 + var5.nextFloat()), (double)((float)var3 + (var7 > var6 / 2 ? 0.3F : 0.5F)), (double)((float)var4 + var5.nextFloat()), 0.0D, 1.0D, 0.0D, true);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(var8);
        }
    }

    public void spawnCloudSmoke(World var1, double var2, double var4, double var6, Random var8, double var9)
    {
        double var11 = var2 + var8.nextDouble() * var9 * 2.0D - var9;
        double var13 = var4 + var8.nextDouble() * var9 * 2.0D - var9;
        double var15 = var6 + var8.nextDouble() * var9 * 2.0D - var9;
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCloudSmokeFX(var1, var11, var13, var15, 0.0D, 0.0D, 0.0D, 2.5F, 1.0F, 1.0F, 1.0F));
    }

    public void spawnCloudSmoke(World var1, double var2, double var4, double var6, Random var8, double var9, double var11, double var13, double var15)
    {
        double var17 = var2 + var8.nextDouble() * var9 * 2.0D - var9;
        double var19 = var4 + var8.nextDouble() * var9 * 2.0D - var9;
        double var21 = var6 + var8.nextDouble() * var9 * 2.0D - var9;
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCloudSmokeFX(var1, var17, var19, var21, var11, var13, var15, 2.5F, 1.0F, 1.0F, 1.0F));
    }

    public void spawnDonatorMoaParticles(Entity var1, Random var2)
    {
        for (int var3 = 0; var3 < 4; ++var3)
        {
            double var4 = var1.posX + ((double)var2.nextFloat() - 0.5D) * 4.0D;
            double var6 = var1.posY + ((double)var2.nextFloat() - 0.5D) * 4.0D;
            double var8 = var1.posZ + ((double)var2.nextFloat() - 0.5D) * 4.0D;
            double var10 = 0.0D;
            double var12 = 0.0D;
            double var14 = 0.0D;
            var10 = ((double)var2.nextFloat() - 0.5D) * 0.5D;
            var12 = ((double)var2.nextFloat() - 0.5D) * 0.5D;
            var14 = ((double)var2.nextFloat() - 0.5D) * 0.5D;
            EntityGoldenFX var16 = new EntityGoldenFX(var1.worldObj, var4, var6, var8, var10, var12, var14, false);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(var16);
        }
    }

    public void spawnPortalParticles(World var1, int var2, int var3, int var4, Random var5, int var6)
    {
        for (int var7 = 0; var7 < 4; ++var7)
        {
            double var8 = (double)((float)var2 + var5.nextFloat());
            double var10 = (double)((float)var3 + var5.nextFloat());
            double var12 = (double)((float)var4 + var5.nextFloat());
            double var14 = 0.0D;
            double var16 = 0.0D;
            double var18 = 0.0D;
            int var20 = var5.nextInt(2) * 2 - 1;
            var14 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
            var16 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
            var18 = ((double)var5.nextFloat() - 0.5D) * 0.5D;

            if (var1.getBlockId(var2 - 1, var3, var4) != var6 && var1.getBlockId(var2 + 1, var3, var4) != var6)
            {
                var8 = (double)var2 + 0.5D + 0.25D * (double)var20;
                var14 = (double)(var5.nextFloat() * 2.0F * (float)var20);
            }
            else
            {
                var12 = (double)var4 + 0.5D + 0.25D * (double)var20;
                var18 = (double)(var5.nextFloat() * 2.0F * (float)var20);
            }

            EntityAetherPortalFX var21 = new EntityAetherPortalFX(var1, var8, var10, var12, var14, var16, var18);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(var21);
        }
    }

    public void spawnRainParticles(World var1, int var2, int var3, int var4, Random var5, int var6)
    {
        for (int var7 = 0; var7 < var6; ++var7)
        {
            double var8 = (double)var2 + var5.nextDouble();
            double var10 = (double)var3 + var5.nextDouble();
            double var12 = (double)var4 + var5.nextDouble();
            var1.spawnParticle("splash", var8, var10, var12, 0.0D, 0.0D, 0.0D);
        }
    }

    public HashMap getClientInventories()
    {
        return this.playerInventories;
    }

    public HashMap getClientExtraHearts()
    {
        return this.playerExtraHearts;
    }

    public HashMap getClientCooldown()
    {
        return this.playerCooldowns;
    }

    public HashMap getClientMaxCooldown()
    {
        return this.playerMaxCooldowns;
    }

    public HashMap getClientCooldownName()
    {
        return this.playerCooldownName;
    }

    public HashMap getClientCoins()
    {
        return this.playerCoins;
    }

    public HashMap getPlayerClientInfo()
    {
        return this.playerClientInfo;
    }

    public HashMap getClientParachuting()
    {
        return this.playerParachutes;
    }

    public HashMap getClientParachuteType()
    {
        return this.playerParachuteTypes;
    }
}
