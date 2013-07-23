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
import net.aetherteam.aether.client.PlayerBaseAetherClient;
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
    public static ArrayList developers = new ArrayList();
    public static ArrayList helper = new ArrayList();

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
    public void load(FMLInitializationEvent var1)
    {
        X509EncodedKeySpec var2 = new X509EncodedKeySpec(Base58.decode(PubKey));

        try
        {
            KeyFactory var3 = KeyFactory.getInstance("RSA");
            publicKey = var3.generatePublic(var2);
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
        proxy.registerMainMenu();
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
    public void preInit(FMLPreInitializationEvent var1)
    {
        AetherRanks.addAllRanks();
        proxy.registerRenderPAPI();
        proxy.registerSounds();
        config = new Configuration(var1.getSuggestedConfigurationFile());
        proxy.registerTickHandler();
        GameRegistry.registerPlayerTracker(new AetherPlayerTracker());
        new ArrayList();
    }

    @Mod.ServerStarting
    public void serverStarting(FMLServerStartingEvent var1)
    {
        var1.registerServerCommand(new CommandClearInventoryAether());
        var1.registerServerCommand(new CommandTeleport());
        TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
    }

    @Mod.ServerStarted
    public void serverStarted(FMLServerStartedEvent var1)
    {
        SerialDataHandler var2 = new SerialDataHandler("dungeons.dat");
        ArrayList var3 = var2.deserializeObjects();
        MinecraftServer var4 = FMLCommonHandler.instance().getMinecraftServerInstance();
        ServerConfigurationManager var5 = var4.getConfigurationManager();

        if (var5.playerEntityList.size() <= 1)
        {
            SerialDataHandler var6 = new SerialDataHandler("parties.dat");
            ArrayList var7 = var6.deserializeObjects();

            if (var7 != null)
            {
                PartyController.instance().setParties(var7);
            }
        }

        if (var3 != null)
        {
            DungeonHandler.instance().loadInstances(var3);
        }
    }

    @Mod.ServerStopping
    public void serverStopping(FMLServerStoppingEvent var1)
    {
        SerialDataHandler var2 = new SerialDataHandler("dungeons.dat");
        ArrayList var3 = DungeonHandler.instance().getInstances();
        MinecraftServer var4 = FMLCommonHandler.instance().getMinecraftServerInstance();
        ServerConfigurationManager var5 = var4.getConfigurationManager();

        if (var5.playerEntityList.size() <= 1)
        {
            SerialDataHandler var6 = new SerialDataHandler("parties.dat");
            ArrayList var7 = PartyController.instance().getParties();
            var6.serializeObjects(var7);
        }

        var2.serializeObjects(var3);
    }

    public static void teleportPlayerToAether(EntityPlayerMP var0, boolean var1)
    {
        getServerPlayer(var0);
        PlayerBaseAetherServer.keepInventory = true;
        TeleporterAether var2;
        ServerConfigurationManager var3;

        if (var0.dimension == 0)
        {
            var2 = new TeleporterAether(MinecraftServer.getServer().worldServerForDimension(3));
            var3 = MinecraftServer.getServer().getConfigurationManager();
            var3.transferPlayerToDimension(var0, 3, var2);
        }
        else if (var0.dimension == 3 && !var1)
        {
            var2 = new TeleporterAether(MinecraftServer.getServer().worldServerForDimension(0));
            var3 = MinecraftServer.getServer().getConfigurationManager();
            var3.transferPlayerToDimension(var0, 0, var2);
        }
        else if (var0.dimension == 3 && var1)
        {
            double var8 = var0.posX;
            double var4 = var0.posZ;
            TeleporterAether var6 = new TeleporterAether(MinecraftServer.getServer().worldServerForDimension(0), false);
            ServerConfigurationManager var7 = MinecraftServer.getServer().getConfigurationManager();
            var7.transferPlayerToDimension(var0, 0, var6);
            var0.setPositionAndUpdate(var8, 256.0D, var4);
        }
        else
        {
            var2 = new TeleporterAether(MinecraftServer.getServer().worldServerForDimension(3));
            var3 = MinecraftServer.getServer().getConfigurationManager();
            var3.transferPlayerToDimension(var0, 3, var2);
        }

        var0.timeUntilPortal = var0.getPortalCooldown();
    }

    public static void teleportEntityToAether(Entity var0)
    {
        if (!var0.worldObj.isRemote && !var0.isDead)
        {
            boolean var1 = false;
            int var2 = var0.dimension;
            byte var8;

            if (var2 == 0)
            {
                var8 = 3;
            }
            else if (var2 == 3)
            {
                var8 = 0;
            }
            else
            {
                var8 = 3;
            }

            var0.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer var3 = MinecraftServer.getServer();
            WorldServer var4 = var3.worldServerForDimension(var2);
            WorldServer var5 = var3.worldServerForDimension(var8);
            TeleporterAether var6 = new TeleporterAether(MinecraftServer.getServer().worldServerForDimension(var8));
            var0.dimension = var8;
            var0.worldObj.removeEntity(var0);
            var0.isDead = false;
            var0.worldObj.theProfiler.startSection("reposition");
            var3.getConfigurationManager().transferEntityToWorld(var0, var2, var4, var5, var6);
            var0.worldObj.theProfiler.endStartSection("reloading");
            Entity var7 = EntityList.createEntityByName(EntityList.getEntityString(var0), var5);

            if (var7 != null)
            {
                var7.copyDataFrom(var0, true);
                var7.timeUntilPortal = var0.getPortalCooldown();
                var5.spawnEntityInWorld(var7);
            }

            var0.isDead = true;
            var0.worldObj.theProfiler.endSection();
            var4.resetUpdateEntityTick();
            var5.resetUpdateEntityTick();
            var0.worldObj.theProfiler.endSection();
        }
    }

    public static void giveAchievement(Achievement var0, EntityPlayer var1)
    {
        Minecraft var2 = proxy.getClient();
        boolean var3 = var1.worldObj.isRemote;

        if (!var3 && var2 != null)
        {
            if (var2.statFileWriter.canUnlockAchievement(var0) && !var2.statFileWriter.hasAchievementUnlocked(var0))
            {
                ;
            }
        }
    }

    public String getMyKey(String var1)
    {
        Minecraft.getMinecraft();
        File var2 = new File(Minecraft.getMinecraftDir(), var1.toLowerCase() + ".key");

        if (var2.exists())
        {
            try
            {
                BufferedReader var3 = new BufferedReader(new FileReader(var2));
                String var4 = var3.readLine();
                System.out.print("Key !! :" + var4);
                System.out.println("Reading name...");

                if (!var4.equalsIgnoreCase("false") && this.isLegit(var1.toLowerCase(), var4))
                {
                    System.out.println("Read name is correct.");
                    return var4;
                }
            }
            catch (IOException var8)
            {
                var8.printStackTrace();
            }
        }

        try
        {
            URL var9 = new URL("http://www.aethermod.net/signature.php?name=" + var1.toLowerCase());
            BufferedReader var10 = new BufferedReader(new InputStreamReader(var9.openStream()));
            String var5;

            if ((var5 = var10.readLine()) != null && var1 != null)
            {
                var10.close();
                System.out.println(var5);
                System.out.println(this.isLegit(var1.toLowerCase(), var5));

                if (var5.equalsIgnoreCase("false"))
                {
                    return null;
                }

                if (this.isLegit(var1.toLowerCase(), var5))
                {
                    System.out.println("Downloaded name is legit, should save now.");
                    return var5;
                }

                System.out.println("Downloaded name is not legit, weird!");
            }

            var10.close();
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

    public String getKey(String var1)
    {
        try
        {
            URL var2 = new URL("http://www.aethermod.net/signature.php?name=" + var1.toLowerCase());
            BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.openStream()));
            String var4;

            if ((var4 = var3.readLine()) != null)
            {
                var3.close();

                if (var4.equalsIgnoreCase("false"))
                {
                    return null;
                }

                if (this.isLegit(var1.toLowerCase(), var4))
                {
                    System.out.println("Downloaded name is legit, should save now.");
                    return var4;
                }

                System.out.println("Downloaded name is not legit, weird!");
            }

            var3.close();
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

    public boolean isLegit(String var1, String var2)
    {
        if (var2 != null && var1 != null)
        {
            try
            {
                Signature var3 = Signature.getInstance("MD5WithRSA");
                var3.initVerify(publicKey);
                var3.update(var1.toLowerCase().getBytes("UTF8"));
                return Base58.decode(var2) != null ? var3.verify(Base58.decode(var2)) : false;
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

    public static AetherCommonPlayerHandler getPlayerBase(EntityPlayer var0)
    {
        return proxy.getPlayerHandler(var0);
    }

    public static PlayerBaseAetherServer getServerPlayer(EntityPlayer var0)
    {
        if (var0 instanceof EntityPlayerMP)
        {
            PlayerCoreServer var1 = (PlayerCoreServer)var0;
            return (PlayerBaseAetherServer)var1.getPlayerCoreObject(PlayerBaseAetherServer.class);
        }
        else
        {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static PlayerBaseAetherClient getClientPlayer(EntityPlayer var0)
    {
        if (var0 instanceof EntityPlayerSP)
        {
            PlayerCoreClient var1 = (PlayerCoreClient)var0;
            return (PlayerBaseAetherClient)var1.getPlayerCoreObject(PlayerBaseAetherClient.class);
        }
        else
        {
            return null;
        }
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1) {}
}
