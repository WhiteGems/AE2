package net.aetherteam.aether.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.aetherteam.aether.PlayerAetherServer;
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
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.data.PlayerClientInfo;
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
import net.aetherteam.playercore_api.cores.PlayerCoreClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.PlayBackgroundMusicEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    private HashMap<String, InventoryAether> playerInventories = new HashMap();
    private HashMap<String, Integer> playerExtraHearts = new HashMap();
    private HashMap<String, Integer> playerCooldowns = new HashMap();
    private HashMap<String, Integer> playerMaxCooldowns = new HashMap();
    private HashMap<String, String> playerCooldownName = new HashMap();
    private HashMap<String, Integer> playerCoins = new HashMap();
    private HashMap<String, Boolean> playerParachutes = new HashMap();
    private HashMap<String, Integer> playerParachuteTypes = new HashMap();
    private HashMap<String, PlayerClientInfo> playerClientInfo = new HashMap();
    private Minecraft mc = Minecraft.getMinecraft();
    public Random rand = new Random();

    @SideOnly(Side.CLIENT)
    public AetherCommonPlayerHandler getPlayerHandler(EntityPlayer entity)
    {
        if (entity instanceof EntityPlayerSP)
        {
            PlayerAetherClient playerBaseAetherClient = (PlayerAetherClient)((PlayerCoreClient)entity).getPlayerCoreObject(PlayerAetherClient.class);
            return playerBaseAetherClient.getPlayerHandler();
        }
        else
        {
            return super.getPlayerHandler(entity);
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
        PlayerCoreAPI.register(PlayerCoreAPI.PlayerCoreType.CLIENT, PlayerAetherClient.class);
        PlayerCoreAPI.register(PlayerCoreAPI.PlayerCoreType.SERVER, PlayerAetherServer.class);
    }

    public void playMusic(String music)
    {
        this.mc.sndManager.sndSystem.stop("BgMusic");
        SoundPoolEntry soundpoolentry = this.mc.sndManager.soundPoolMusic.getRandomSoundFromSoundPool(music);
        soundpoolentry = SoundEvent.getResult(new PlayBackgroundMusicEvent(this.mc.sndManager, soundpoolentry));

        if (soundpoolentry != null)
        {
            this.mc.sndManager.sndSystem.backgroundMusic("BgMusic", soundpoolentry.func_110457_b(), soundpoolentry.func_110458_a(), false);
            this.mc.sndManager.sndSystem.setVolume("BgMusic", Minecraft.getMinecraft().gameSettings.musicVolume);
            this.mc.sndManager.sndSystem.play("BgMusic");
        }
    }

    public void registerTickHandler()
    {
        MinecraftForge.EVENT_BUS.register(new GuiAetherIngame(Minecraft.getMinecraft()));
        TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
    }

    public void playSoundFX(String path, float volume, float pitch)
    {
        this.mc.sndManager.playSoundFX(path, volume, pitch);
    }

    public int addArmor(String type)
    {
        return RenderingRegistry.addNewArmourRendererPrefix(type);
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

    public void registerSounds()
    {
        MinecraftForge.EVENT_BUS.register(new AetherSoundLoader());
    }

    public void openDungeonQueue()
    {
        this.mc.displayGuiScreen(new GuiDungeonScreen(this.mc));
    }

    @SideOnly(Side.CLIENT)
    public void renderGameOverlay(float zLevel, boolean flag, int x, int y)
    {
        if (Aether.getClientPlayer(this.getClientPlayer()) != null)
        {
            ClientNotificationHandler.updateNotifications();
            AetherPoison.displayCureEffect();
            AetherPoison.displayPoisonEffect();
        }
    }

    public void displayMessage(EntityPlayer player, String message)
    {
        player.addChatMessage(message);
    }

    public void registerRenderPAPI()
    {
        PlayerCoreAPI.register(PlayerCoreAPI.PlayerCoreType.RENDER, RenderPlayerAether.class);
    }

    public void spawnSwettyParticles(World world, int x, int y, int z)
    {
        for (int count = 0; count < 5; ++count)
        {
            EntityAetherBreakingFX particles = new EntityAetherBreakingFX(world, (double)x + this.rand.nextDouble(), (double)y + this.rand.nextDouble(), (double)z + this.rand.nextDouble(), AetherItems.SwettyBall);
            particles.renderDistanceWeight = 10.0D;
            particles.setParticleTextureIndex(143);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(particles);
        }
    }

    public void spawnAltarParticles(World world, int x, int y, int z, Random rand)
    {
        byte particleAmount = 50;

        for (int count = 0; count < particleAmount; ++count)
        {
            EntityGoldenFX particles = new EntityGoldenFX(world, (double)((float)x + rand.nextFloat()), (double)((float)y + (count > particleAmount / 2 ? 0.3F : 0.5F)), (double)((float)z + rand.nextFloat()), 0.0D, 1.0D, 0.0D, true);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(particles);
        }
    }

    public void spawnCloudSmoke(World world, double x, double y, double z, Random rand, double radius)
    {
        double xOffset = x + rand.nextDouble() * radius * 2.0D - radius;
        double yOffset = y + rand.nextDouble() * radius * 2.0D - radius;
        double zOffset = z + rand.nextDouble() * radius * 2.0D - radius;
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCloudSmokeFX(world, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D, 2.5F, 1.0F, 1.0F, 1.0F));
    }

    public void spawnCloudSmoke(World world, double x, double y, double z, Random rand, double radius, double forceX, double forceY, double forceZ)
    {
        double xOffset = x + rand.nextDouble() * radius * 2.0D - radius;
        double yOffset = y + rand.nextDouble() * radius * 2.0D - radius;
        double zOffset = z + rand.nextDouble() * radius * 2.0D - radius;
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCloudSmokeFX(world, xOffset, yOffset, zOffset, forceX, forceY, forceZ, 2.5F, 1.0F, 1.0F, 1.0F));
    }

    public void spawnDonatorMoaParticles(Entity rider, Random rand)
    {
        for (int count = 0; count < 4; ++count)
        {
            double xOffset = rider.posX + ((double)rand.nextFloat() - 0.5D) * 4.0D;
            double yOffset = rider.posY + ((double)rand.nextFloat() - 0.5D) * 4.0D;
            double zOffset = rider.posZ + ((double)rand.nextFloat() - 0.5D) * 4.0D;
            double motionX = 0.0D;
            double motionY = 0.0D;
            double motionZ = 0.0D;
            motionX = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            motionY = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            motionZ = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            EntityGoldenFX particles = new EntityGoldenFX(rider.worldObj, xOffset, yOffset, zOffset, motionX, motionY, motionZ, false);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(particles);
        }
    }

    public void spawnPortalParticles(World world, int x, int y, int z, Random random, int blockID)
    {
        for (int l = 0; l < 4; ++l)
        {
            double xOffset = (double)((float)x + random.nextFloat());
            double yOffset = (double)((float)y + random.nextFloat());
            double zOffset = (double)((float)z + random.nextFloat());
            double motionX = 0.0D;
            double motionY = 0.0D;
            double motionZ = 0.0D;
            int randMotionFactor = random.nextInt(2) * 2 - 1;
            motionX = ((double)random.nextFloat() - 0.5D) * 0.5D;
            motionY = ((double)random.nextFloat() - 0.5D) * 0.5D;
            motionZ = ((double)random.nextFloat() - 0.5D) * 0.5D;

            if (world.getBlockId(x - 1, y, z) != blockID && world.getBlockId(x + 1, y, z) != blockID)
            {
                xOffset = (double)x + 0.5D + 0.25D * (double)randMotionFactor;
                motionX = (double)(random.nextFloat() * 2.0F * (float)randMotionFactor);
            }
            else
            {
                zOffset = (double)z + 0.5D + 0.25D * (double)randMotionFactor;
                motionZ = (double)(random.nextFloat() * 2.0F * (float)randMotionFactor);
            }

            EntityAetherPortalFX obj = new EntityAetherPortalFX(world, xOffset, yOffset, zOffset, motionX, motionY, motionZ);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(obj);
        }
    }

    public void spawnRainParticles(World world, int x, int y, int z, Random rand, int amount)
    {
        for (int count = 0; count < amount; ++count)
        {
            double xOffset = (double)x + rand.nextDouble();
            double yOffset = (double)y + rand.nextDouble();
            double zOffset = (double)z + rand.nextDouble();
            world.spawnParticle("splash", xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D);
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
