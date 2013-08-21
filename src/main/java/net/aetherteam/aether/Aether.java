package net.aetherteam.aether;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import net.aetherteam.aether.achievements.AetherAchievements;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.client.PlayerAetherClient;
import net.aetherteam.aether.commands.CommandClearInventoryAether;
import net.aetherteam.aether.commands.CommandTeleport;
import net.aetherteam.aether.creative_tabs.AetherAccessoryTab;
import net.aetherteam.aether.creative_tabs.AetherArmourTab;
import net.aetherteam.aether.creative_tabs.AetherBlockTab;
import net.aetherteam.aether.creative_tabs.AetherCapeTab;
import net.aetherteam.aether.creative_tabs.AetherFoodTab;
import net.aetherteam.aether.creative_tabs.AetherMaterialsTab;
import net.aetherteam.aether.creative_tabs.AetherMiscTab;
import net.aetherteam.aether.creative_tabs.AetherToolsTab;
import net.aetherteam.aether.creative_tabs.AetherWeaponsTab;
import net.aetherteam.aether.data.SerialDataHandler;
import net.aetherteam.aether.donator.Base58;
import net.aetherteam.aether.donator.DonatorChoices;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.AetherEntities;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.recipes.AetherEnchantments;
import net.aetherteam.aether.recipes.AetherFreezables;
import net.aetherteam.aether.recipes.AetherRecipes;
import net.aetherteam.aether.tile_entities.AetherTileEntities;
import net.aetherteam.aether.worldgen.BiomeGenAether;
import net.aetherteam.aether.worldgen.TeleporterAether;
import net.aetherteam.aether.worldgen.WorldProviderAether;
import net.aetherteam.playercore_api.cores.PlayerCoreClient;
import net.aetherteam.playercore_api.cores.PlayerCoreServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.stats.Achievement;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

@Mod(
    modid = "Aether II",
    name = "Aether II",
    version = "Alpha v1.0.1"
)
@NetworkMod(
    clientSideRequired = true,
    serverSideRequired = true,
    channels = {"Aether"},
    packetHandler = AetherPacketHandler.class,
    connectionHandler = AetherConnectionHandler.class
)
public class Aether
{
    @Mod.Instance("Aether II")
    public static Aether instance;
    private static String version = "Alpha v1.0.1";
    public static Configuration config;
    public static BiomeGenBase biome = new BiomeGenAether();
    public static final int DIMENSION_ID = 3;
    public static AetherAchievements achievements = new AetherAchievements();
    public static AchievementPage page;
    public static AetherGuiHandler aetherGuiHandler = new AetherGuiHandler();
    public static AetherCraftingHandler aetherCraftingHandler = new AetherCraftingHandler();
    public static DonatorChoices donatorChoices = new DonatorChoices();
    public static CreativeTabs blocks = new AetherBlockTab();
    public static CreativeTabs tools = new AetherToolsTab();
    public static CreativeTabs weapons = new AetherWeaponsTab();
    public static CreativeTabs armour = new AetherArmourTab();
    public static CreativeTabs capes = new AetherCapeTab();
    public static CreativeTabs accessories = new AetherAccessoryTab();
    public static CreativeTabs materials = new AetherMaterialsTab();
    public static CreativeTabs food = new AetherFoodTab();
    public static CreativeTabs misc = new AetherMiscTab();
    public static String PubKey = "2TuPVgMCHJy5atawrsADEzjP7MCVbyyCA89UW6Wvjp9HrAMRqHBLXxCWbeqpaSUQqP1orskhtvckgmQLUCYNSoWouoBW6qgWVMExtsCKnsftqQQb2JGtk7NWQvt6p818d9NzwjP1N2Pvicmy8MpktG1HfwURcR4LQXAGBriu1Ti5XzbVRreoUj8DifbuwfcftgmCPzN8vqdEmTCardSsWNSNkkjKucy1CnvACewXESjp5vCXtjLpPm35kxWWjMvgf5tEjvFFrDMPzYtjM8St6FwNfeBv2GW4uayfxeGHdhQt2c5GTyusVK53wRo1eP2Zy5MALTZerCUoKJpJaLnxyQ5NautXGKKNVWVtdj5LE1uWAvvv3ySbEtyZrzT8DiX6Ng3K6TBXJzA36ZWaHN";
    private static PublicKey publicKey;
    public static SyncDonatorList syncDonatorList = new SyncDonatorList();
    @SidedProxy(
        clientSide = "net.aetherteam.aether.client.ClientProxy",
        serverSide = "net.aetherteam.aether.CommonProxy"
    )
    public static CommonProxy proxy;
    public static ArrayList<String> developers = new ArrayList();
    public static ArrayList<String> helper = new ArrayList();
    public static ArrayList<String> translator = new ArrayList();

    public static Configuration getConfig()
    {
        return config;
    }

    public static String getVersion()
    {
        return version;
    }

    public static Aether getInstance()
    {
        return instance;
    }

    @Mod.Init
    public void load(FMLInitializationEvent event)
    {
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base58.decode(PubKey));

        try
        {
            KeyFactory e = KeyFactory.getInstance("RSA");
            publicKey = e.generatePublic(publicKeySpec);
        }
        catch (NoSuchAlgorithmException var4)
        {
            var4.printStackTrace();
        }
        catch (InvalidKeySpecException var5)
        {
            var5.printStackTrace();
        }

        DimensionManager.registerProviderType(3, WorldProviderAether.class, true);
        DimensionManager.registerDimension(3, 3);
        proxy.loadSounds();
        proxy.registerRenderers();
        proxy.registerPlayerAPI();
        config.load();
        AetherEntities.init(this);
        AetherTileEntities.init();
        AetherBlocks.init();
        AetherItems.init();
        AetherRecipes.init();
        AetherFreezables.init();
        AetherEnchantments.init();
        AetherRecipes.init();
        AetherAchievements var10000 = achievements;
        AetherAchievements.init();
        NetworkRegistry.instance().registerGuiHandler(this, aetherGuiHandler);
        GameRegistry.registerCraftingHandler(aetherCraftingHandler);
        MinecraftForge.EVENT_BUS.register(new AetherHooks());
        MinecraftForge.EVENT_BUS.register(new AetherEvents());
        config.save();
    }

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        AetherRanks.addAllRanks();
        proxy.registerRenderPAPI();
        proxy.registerSounds();
        config = new Configuration(event.getSuggestedConfigurationFile());
        proxy.registerTickHandler();
        GameRegistry.registerPlayerTracker(new AetherPlayerTracker());
        new ArrayList();
    }

    @Mod.ServerStarting
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandClearInventoryAether());
        event.registerServerCommand(new CommandTeleport());
        TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
    }

    @Mod.ServerStarted
    public void serverStarted(FMLServerStartedEvent event)
    {
        SerialDataHandler dungeonDataHandler = new SerialDataHandler("dungeons.dat");
        ArrayList dungeonObjects = dungeonDataHandler.deserializeObjects();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        ServerConfigurationManager configManager = server.getConfigurationManager();

        if (configManager.playerEntityList.size() <= 1)
        {
            SerialDataHandler partyDataHandler = new SerialDataHandler("parties.dat");
            ArrayList partyObjects = partyDataHandler.deserializeObjects();

            if (partyObjects != null)
            {
                PartyController.instance().setParties(partyObjects);
            }
        }

        if (dungeonObjects != null)
        {
            DungeonHandler.instance().loadInstances(dungeonObjects);
        }
    }

    @Mod.ServerStopping
    public void serverStopping(FMLServerStoppingEvent event)
    {
        SerialDataHandler dungeonDataHandler = new SerialDataHandler("dungeons.dat");
        ArrayList dungeonObjects = DungeonHandler.instance().getInstances();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        ServerConfigurationManager configManager = server.getConfigurationManager();

        if (configManager.playerEntityList.size() <= 1)
        {
            SerialDataHandler partyDataHandler = new SerialDataHandler("parties.dat");
            ArrayList partyObjects = PartyController.instance().getParties();
            partyDataHandler.serializeObjects(partyObjects);
        }

        dungeonDataHandler.serializeObjects(dungeonObjects);
    }

    public static void teleportPlayerToAether(EntityPlayerMP player, boolean aboveWorld)
    {
        getServerPlayer(player);
        PlayerAetherServer.keepInventory = true;
        TeleporterAether aetherTeleporter;
        ServerConfigurationManager scm;

        if (player.dimension == 0)
        {
            aetherTeleporter = new TeleporterAether(MinecraftServer.getServer().worldServerForDimension(3));
            scm = MinecraftServer.getServer().getConfigurationManager();
            scm.transferPlayerToDimension(player, 3, aetherTeleporter);
        }
        else if (player.dimension == 3 && !aboveWorld)
        {
            aetherTeleporter = new TeleporterAether(MinecraftServer.getServer().worldServerForDimension(0));
            scm = MinecraftServer.getServer().getConfigurationManager();
            scm.transferPlayerToDimension(player, 0, aetherTeleporter);
        }
        else if (player.dimension == 3 && aboveWorld)
        {
            double aetherTeleporter2 = player.posX;
            double posZ = player.posZ;
            TeleporterAether aetherTeleporter1 = new TeleporterAether(MinecraftServer.getServer().worldServerForDimension(0), false);
            ServerConfigurationManager scm1 = MinecraftServer.getServer().getConfigurationManager();
            scm1.transferPlayerToDimension(player, 0, aetherTeleporter1);
            player.setPositionAndUpdate(aetherTeleporter2, 256.0D, posZ);
        }
        else
        {
            aetherTeleporter = new TeleporterAether(MinecraftServer.getServer().worldServerForDimension(3));
            scm = MinecraftServer.getServer().getConfigurationManager();
            scm.transferPlayerToDimension(player, 3, aetherTeleporter);
        }

        player.timeUntilPortal = player.getPortalCooldown();
    }

    public static void teleportEntityToAether(Entity entity)
    {
        if (!entity.worldObj.isRemote && !entity.isDead)
        {
            boolean newDim = false;
            int j = entity.dimension;
            byte newDim1;

            if (j == 0)
            {
                newDim1 = 3;
            }
            else if (j == 3)
            {
                newDim1 = 0;
            }
            else
            {
                newDim1 = 3;
            }

            entity.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            WorldServer worldserver = minecraftserver.worldServerForDimension(j);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(newDim1);
            TeleporterAether aetherTeleporter = new TeleporterAether(MinecraftServer.getServer().worldServerForDimension(newDim1));
            entity.dimension = newDim1;
            entity.worldObj.removeEntity(entity);
            entity.isDead = false;
            entity.worldObj.theProfiler.startSection("reposition");
            minecraftserver.getConfigurationManager().transferEntityToWorld(entity, j, worldserver, worldserver1, aetherTeleporter);
            entity.worldObj.theProfiler.endStartSection("reloading");
            Entity entity1 = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);

            if (entity1 != null)
            {
                entity1.copyDataFrom(entity, true);
                entity1.timeUntilPortal = entity.getPortalCooldown();
                worldserver1.spawnEntityInWorld(entity1);
            }

            entity.isDead = true;
            entity.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            entity.worldObj.theProfiler.endSection();
        }
    }

    public static void giveAchievement(Achievement achievement, EntityPlayer player)
    {
        Minecraft mc = proxy.getClient();
        boolean isRemote = player.worldObj.isRemote;

        if (!isRemote && mc != null)
        {
            if (mc.statFileWriter.canUnlockAchievement(achievement) && !mc.statFileWriter.hasAchievementUnlocked(achievement))
            {
                ;
            }
        }
    }

    public String getMyKey(String name)
    {
        File Me = new File(Minecraft.getMinecraft().mcDataDir, name.toLowerCase() + ".key");

        if (Me.exists())
        {
            try
            {
                BufferedReader url = new BufferedReader(new FileReader(Me));
                String e = url.readLine();
                System.out.print("Key !! :" + e);
                System.out.println("Reading name...");

                if (!e.equalsIgnoreCase("false") && this.isLegit(name.toLowerCase(), e))
                {
                    System.out.println("Read name is correct.");
                    return e;
                }
            }
            catch (IOException var8)
            {
                var8.printStackTrace();
            }
        }

        try
        {
            URL url1 = new URL("http://www.aethermod.net/signature.php?name=" + name.toLowerCase());
            BufferedReader e1 = new BufferedReader(new InputStreamReader(url1.openStream()));
            String str;

            if ((str = e1.readLine()) != null && name != null)
            {
                e1.close();
                System.out.println(str);
                System.out.println(this.isLegit(name.toLowerCase(), str));

                if (str.equalsIgnoreCase("false"))
                {
                    return null;
                }

                if (this.isLegit(name.toLowerCase(), str))
                {
                    System.out.println("Downloaded name is legit, should save now.");
                    return str;
                }

                System.out.println("Downloaded name is not legit, weird!");
            }

            e1.close();
        }
        catch (MalformedURLException var6)
        {
            var6.printStackTrace();
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }

        return null;
    }

    public String getKey(String name)
    {
        try
        {
            URL url = new URL("http://www.aethermod.net/signature.php?name=" + name.toLowerCase());
            BufferedReader e = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;

            if ((str = e.readLine()) != null)
            {
                e.close();

                if (str.equalsIgnoreCase("false"))
                {
                    return null;
                }

                if (this.isLegit(name.toLowerCase(), str))
                {
                    System.out.println("Downloaded name is legit, should save now.");
                    return str;
                }

                System.out.println("Downloaded name is not legit, weird!");
            }

            e.close();
        }
        catch (MalformedURLException var5)
        {
            var5.printStackTrace();
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        return null;
    }

    public boolean isLegit(String username, String proof)
    {
        if (proof != null && username != null)
        {
            try
            {
                Signature sig = Signature.getInstance("MD5WithRSA");
                sig.initVerify(publicKey);
                sig.update(username.toLowerCase().getBytes("UTF8"));
                return Base58.decode(proof) != null ? sig.verify(Base58.decode(proof)) : false;
            }
            catch (NoSuchAlgorithmException var5)
            {
                var5.printStackTrace();
            }
            catch (InvalidKeyException var6)
            {
                var6.printStackTrace();
            }
            catch (SignatureException var7)
            {
                var7.printStackTrace();
            }
            catch (UnsupportedEncodingException var8)
            {
                var8.printStackTrace();
            }

            return false;
        }
        else
        {
            return false;
        }
    }

    public static AetherCommonPlayerHandler getPlayerBase(EntityPlayer entity)
    {
        return proxy.getPlayerHandler(entity);
    }

    public static PlayerAetherServer getServerPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            PlayerCoreServer playerCoreServer = (PlayerCoreServer)player;
            return (PlayerAetherServer)playerCoreServer.getPlayerCoreObject(PlayerAetherServer.class);
        }
        else
        {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static PlayerAetherClient getClientPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerSP)
        {
            PlayerCoreClient playerCoreClient = (PlayerCoreClient)player;
            return (PlayerAetherClient)playerCoreClient.getPlayerCoreObject(PlayerAetherClient.class);
        }
        else
        {
            return null;
        }
    }

    public static boolean isGamePaused()
    {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.isSingleplayer() && mc.currentScreen != null && mc.currentScreen.doesGuiPauseGame() && !mc.getIntegratedServer().getPublic();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent event) {}
}
