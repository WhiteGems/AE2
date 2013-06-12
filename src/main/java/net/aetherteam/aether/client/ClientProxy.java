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
import java.io.PrintStream;
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
import net.aetherteam.aether.client.gui.AetherGuis;
import net.aetherteam.aether.client.gui.GuiAetherIngame;
import net.aetherteam.aether.client.gui.social.GuiDungeonScreen;
import net.aetherteam.aether.client.renders.AetherEntityRenderers;
import net.aetherteam.aether.client.renders.RenderAetherTallGrass;
import net.aetherteam.aether.client.renders.RenderBerryBush;
import net.aetherteam.aether.client.renders.RenderHandlerAltar;
import net.aetherteam.aether.client.renders.RenderHandlerSkyrootChest;
import net.aetherteam.aether.client.renders.RenderHandlerTreasureChest;
import net.aetherteam.aether.donator.SyncDonatorList;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventBus;

public class ClientProxy extends CommonProxy
{
    private HashMap playerInventories = new HashMap();
    private HashMap playerExtraHearts = new HashMap();
    private HashMap playerCooldowns = new HashMap();
    private HashMap playerMaxCooldowns = new HashMap();
    private HashMap playerCooldownName = new HashMap();
    private HashMap playerCoins = new HashMap();

    private HashMap playerClientInfo = new HashMap();

    private static String soundZipPath = "/resources/";
    private Minecraft mc = Minecraft.getMinecraft();
    public Random rand = new Random();

    public AetherCommonPlayerHandler getPlayerHandler(EntityPlayer entity)
    {
        if ((entity instanceof EntityPlayerSP))
        {
            return ((PlayerBaseAetherClient) ((EntityPlayerSP) entity).getPlayerBase("Aether II")).getPlayerHandler();
        }

        return super.getPlayerHandler(entity);
    }

    public void registerRenderers()
    {
        Aether.syncDonatorList.initialVerification(this.mc);

        AetherGuis.init();
        AetherEntityRenderers.registerEntityRenderers();

        RenderingRegistry.registerBlockHandler(new RenderBerryBush());
        RenderingRegistry.registerBlockHandler(new RenderAetherTallGrass());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAltar.class, new TileEntityAltarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChest.class, new TileEntityTreasureChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyrootChest.class, new TileEntitySkyrootChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEntranceController.class, new TileEntityEntranceRenderer());

        net.aetherteam.aether.blocks.AetherBlocks.altarRenderId = RenderingRegistry.getNextAvailableRenderId();
        net.aetherteam.aether.blocks.AetherBlocks.treasureChestRenderId = RenderingRegistry.getNextAvailableRenderId();
        net.aetherteam.aether.blocks.AetherBlocks.skyrootChestRenderId = RenderingRegistry.getNextAvailableRenderId();
        net.aetherteam.aether.blocks.AetherBlocks.entranceRenderId = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(new RenderHandlerAltar());
        RenderingRegistry.registerBlockHandler(new RenderHandlerTreasureChest());
        RenderingRegistry.registerBlockHandler(new RenderHandlerSkyrootChest());

        MinecraftForge.EVENT_BUS.register(new AetherEventReceiver());

        MainMenuAPI.registerMenu("Aether II", MenuBaseAetherII.class);
    }

    public void registerPlayerAPI()
    {
        PlayerAPI.register("Aether II", PlayerBaseAetherClient.class);
        ServerPlayerAPI.register("Aether II", PlayerBaseAetherServer.class);
    }

    public void registerMainMenu()
    {
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

    public void loadSounds()
    {
        if (this.mc.sndManager == null)
        {
            return;
        }

        installSound("music/aether1.ogg");
        installSound("music/aether2.ogg");
        installSound("music/aether3.ogg");
        installSound("music/aether4.ogg");
        installSound("music/aether5.ogg");
        
        installSound("streaming/Approaches.ogg");
        installSound("streaming/Aether Menu.ogg");
        installSound("streaming/Aether Menu Two.wav");
        installSound("streaming/Spectrum.ogg");
        installSound("streaming/Aether Day 1.ogg");
        installSound("streaming/Aether Night 1.ogg");
        installSound("streaming/Aether Night 2.ogg");
        installSound("streaming/Dungeon Background.ogg");
        installSound("streaming/Approaches.ogg");
        installSound("streaming/Demise.ogg");

        installSound("streaming/Aether Tune.ogg");
        installSound("streaming/Ascending Dawn.ogg");

        installSound("streaming/Slider Battle.ogg");
        installSound("streaming/Slider Finish.ogg");

        installSound("newsound/aeboss/slider/awake.ogg");
        installSound("newsound/aeboss/slider/collide.ogg");
        installSound("newsound/aeboss/slider/die.ogg");
        installSound("newsound/aeboss/slider/move.ogg");
        installSound("newsound/aeboss/slider/unlock.ogg");

        installSound("newsound/aemob/aerbunny/die.ogg");
        installSound("newsound/aemob/aerbunny/hurt1.ogg");
        installSound("newsound/aemob/aerbunny/hurt2.ogg");
        installSound("newsound/aemob/aerbunny/land.ogg");
        installSound("newsound/aemob/aerbunny/lift.ogg");

        installSound("newsound/aemob/aerwhale/say.wav");
        installSound("newsound/aemob/aerwhale/die.wav");

        installSound("newsound/aemob/moa/say.wav");

        installSound("newsound/aemob/zephyr/say1.wav");
        installSound("newsound/aemob/zephyr/say2.wav");
        installSound("newsound/aemob/zephyr/shoot.ogg");

        installSound("newsound/aemisc/achieveGen.ogg");
        installSound("newsound/aemisc/achieveBronze.ogg");
        installSound("newsound/aemisc/achieveSilver.ogg");
        installSound("newsound/aemisc/achieveBronzeNew.ogg");

        installSound("newsound/aemisc/activateTrap.ogg");

        installSound("newsound/aemisc/shootDart.ogg");

        installSound("newsound/aemob/sentryGolem/seenEnemy.ogg");
        installSound("newsound/aemob/sentryGolem/creepySeen.wav");
        installSound("newsound/aemob/sentryGolem/say1.wav");
        installSound("newsound/aemob/sentryGolem/say2.wav");
        installSound("newsound/aemob/sentryGolem/death.wav");
        installSound("newsound/aemob/sentryGolem/hit1.wav");
        installSound("newsound/aemob/sentryGolem/hit2.wav");

        installSound("newsound/aemob/sentryGuardian/death.ogg");
        installSound("newsound/aemob/sentryGuardian/spawn.ogg");
        installSound("newsound/aemob/sentryGuardian/hit.ogg");
        installSound("newsound/aemob/sentryGuardian/living.ogg");

        installSound("newsound/aemisc/coin.ogg");

        installSound("newsound/aemob/cog/wall.wav");
        installSound("newsound/aemob/cog/wall1.wav");
        installSound("newsound/aemob/cog/wall2.wav");
        installSound("newsound/aemob/cog/wallFinal.ogg");

        installSound("newsound/aeportal/aeportal.wav");
        installSound("newsound/aeportal/aetravel.wav");
        installSound("newsound/aeportal/aetrigger.wav");
    }

    public void registerSounds()
    {
        MinecraftForge.EVENT_BUS.register(new AetherSoundLoader());
    }

    private void installSound(String filename)
    {
        File soundFile = new File(this.mc.mcDataDir, "resources/" + filename);

        if (!soundFile.exists()) try
        {
            String srcPath = soundZipPath + filename;
            InputStream inStream = Aether.class.getResourceAsStream(srcPath);

            if (inStream == null)
            {
                throw new IOException();
            }

            if (!soundFile.getParentFile().exists())
            {
                soundFile.getParentFile().mkdirs();
            }

            BufferedInputStream fileIn = new BufferedInputStream(inStream);
            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(soundFile));

            byte[] buffer = new byte[1024];

            int n = 0;

            while (-1 != (n = fileIn.read(buffer)))
            {
                fileOut.write(buffer, 0, n);
            }

            fileIn.close();
            fileOut.close();
        } catch (IOException ex)
        {
        }
        if ((soundFile.canRead()) && (soundFile.isFile())) this.mc.installResource(filename, soundFile);
        else System.err.println("Could not load file: " + soundFile);
    }

    public void openDungeonQueue()
    {
        this.mc.displayGuiScreen(new GuiDungeonScreen(this.mc));
    }

    @SideOnly(Side.CLIENT)
    public void renderGameOverlay(float zLevel, boolean flag, int x, int y)
    {
        if (Aether.getClientPlayer(getClientPlayer()) == null)
        {
            return;
        }
        ClientNotificationHandler.updateNotifications();

        AetherPoison.displayCureEffect();
        AetherPoison.displayPoisonEffect();
    }

    public void displayMessage(EntityPlayer player, String message)
    {
        player.addChatMessage(message);
    }

    public void registerRenderPAPI()
    {
        RenderPlayerAPI.register("Aether II", RenderPlayerBaseAether.class);
    }

    public void spawnSwettyParticles(World world, int x, int y, int z)
    {
        for (int count = 0; count < 5; count++)
        {
            EntityFX particles = new EntityAetherBreakingFX(world, x + this.rand.nextDouble(), y + this.rand.nextDouble(), z + this.rand.nextDouble(), AetherItems.SwettyBall);

            particles.renderDistanceWeight = 10.0D;
            particles.setParticleTextureIndex(143);

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(particles);
        }
    }

    public void spawnAltarParticles(World world, int x, int y, int z, Random rand)
    {
        int particleAmount = 50;

        for (int count = 0; count < particleAmount; count++)
        {
            EntityFX particles = new EntityGoldenFX(world, x + rand.nextFloat(), y + (count > particleAmount / 2 ? 0.3F : 0.5F), z + rand.nextFloat(), 0.0D, 1.0D, 0.0D, true);

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(particles);
        }
    }

    public void spawnCloudSmoke(World world, double x, double y, double z, Random rand, Double radius)
    {
        double xOffset = x + rand.nextDouble() * radius.doubleValue() * 2.0D - radius.doubleValue();
        double yOffset = y + rand.nextDouble() * radius.doubleValue() * 2.0D - radius.doubleValue();
        double zOffset = z + rand.nextDouble() * radius.doubleValue() * 2.0D - radius.doubleValue();

        FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCloudSmokeFX(world, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D, 2.5F, 1.0F, 1.0F, 1.0F));
    }

    public void spawnDonatorMoaParticles(Entity rider, Random rand)
    {
        for (int count = 0; count < 4; count++)
        {
            double xOffset = rider.posX + (rand.nextFloat() - 0.5D) * 4.0D;
            double yOffset = rider.posY + (rand.nextFloat() - 0.5D) * 4.0D;
            double zOffset = rider.posZ + (rand.nextFloat() - 0.5D) * 4.0D;

            double motionX = 0.0D;
            double motionY = 0.0D;
            double motionZ = 0.0D;

            motionX = (rand.nextFloat() - 0.5D) * 0.5D;
            motionY = (rand.nextFloat() - 0.5D) * 0.5D;
            motionZ = (rand.nextFloat() - 0.5D) * 0.5D;

            EntityFX particles = new EntityGoldenFX(rider.worldObj, xOffset, yOffset, zOffset, motionX, motionY, motionZ, false);

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(particles);
        }
    }

    public void spawnPortalParticles(World world, int x, int y, int z, Random random, int blockID)
    {
        for (int l = 0; l < 4; l++)
        {
            double xOffset = x + random.nextFloat();
            double yOffset = y + random.nextFloat();
            double zOffset = z + random.nextFloat();

            double motionX = 0.0D;
            double motionY = 0.0D;
            double motionZ = 0.0D;

            int randMotionFactor = random.nextInt(2) * 2 - 1;

            motionX = (random.nextFloat() - 0.5D) * 0.5D;
            motionY = (random.nextFloat() - 0.5D) * 0.5D;
            motionZ = (random.nextFloat() - 0.5D) * 0.5D;

            if ((world.getBlockId(x - 1, y, z) == blockID) || (world.getBlockId(x + 1, y, z) == blockID))
            {
                zOffset = z + 0.5D + 0.25D * randMotionFactor;
                motionZ = random.nextFloat() * 2.0F * randMotionFactor;
            } else
            {
                xOffset = x + 0.5D + 0.25D * randMotionFactor;
                motionX = random.nextFloat() * 2.0F * randMotionFactor;
            }

            EntityFX obj = new EntityAetherPortalFX(world, xOffset, yOffset, zOffset, motionX, motionY, motionZ);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(obj);
        }
    }

    public void spawnRainParticles(World world, int x, int y, int z, Random rand, int amount)
    {
        for (int count = 0; count < amount; count++)
        {
            double xOffset = x + rand.nextDouble();
            double yOffset = y + rand.nextDouble();
            double zOffset = z + rand.nextDouble();

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
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.ClientProxy
 * JD-Core Version:    0.6.2
 */