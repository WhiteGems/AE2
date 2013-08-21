package net.aetherteam.aether;

import java.util.HashMap;
import java.util.Random;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.data.PlayerClientInfo;
import net.aetherteam.playercore_api.PlayerCoreAPI;
import net.aetherteam.playercore_api.cores.PlayerCoreServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumHelper;

public class CommonProxy
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
    public static EnumArmorMaterial OBSIDIAN;

    public AetherCommonPlayerHandler getPlayerHandler(EntityPlayer entity)
    {
        if (entity instanceof EntityPlayerMP)
        {
            PlayerAetherServer playerBaseAetherServer = (PlayerAetherServer)((PlayerCoreServer)entity).getPlayerCoreObject(PlayerAetherServer.class);
            return playerBaseAetherServer.getPlayerHandler();
        }
        else
        {
            return null;
        }
    }

    public void playMusic(String music) {}

    public void registerPlayerAPI()
    {
        PlayerCoreAPI.register(PlayerCoreAPI.PlayerCoreType.SERVER, PlayerAetherServer.class);
    }

    public void registerTickHandler() {}

    public void displayMessage(EntityPlayer player, String message)
    {
        player.addChatMessage(message);
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
        return this.playerCooldownName;
    }

    public HashMap getClientCooldownName()
    {
        return this.playerMaxCooldowns;
    }

    public HashMap getClientCoins()
    {
        return this.playerCoins;
    }

    public HashMap getPlayerClientInfo()
    {
        return this.playerClientInfo;
    }

    public Minecraft getClient()
    {
        return null;
    }

    public World getClientWorld()
    {
        return null;
    }

    public EntityPlayer getClientPlayer()
    {
        return null;
    }

    public EffectRenderer getEffectRenderer()
    {
        return null;
    }

    public int addArmor(String type)
    {
        return 0;
    }

    public void registerKeyBindings() {}

    public void registerRenderers() {}

    public void spawnSwettyParticles(World world, int x, int y, int z) {}

    public void spawnAltarParticles(World world, int x, int y, int z, Random rand) {}

    public void spawnCloudSmoke(World world, double x, double y, double z, Random rand, Double radius) {}

    public void spawnDonatorMoaParticles(Entity rider, Random rand) {}

    public void spawnPortalParticles(World world, int x, int y, int z, Random random, int blockID) {}

    public void spawnRainParticles(World world, int x, int y, int z, Random random, int amount) {}

    public void loadSounds() {}

    public void registerSounds() {}

    public void registerRenderPAPI() {}

    public void renderGameOverlay(float zLevel, boolean flag, int x, int y) {}

    public void playSoundFX(String path, float volume, float pitch) {}

    public void openDungeonQueue() {}

    public void spawnCloudSmoke(World world, double x, double y, double z, Random rand, double radius, double forceX, double forceY, double forceZ) {}

    public HashMap getClientParachuting()
    {
        return this.playerParachutes;
    }

    public HashMap getClientParachuteType()
    {
        return this.playerParachuteTypes;
    }

    static
    {
        new EnumHelper();
        OBSIDIAN = EnumHelper.addArmorMaterial("OBISIDAN", 33, new int[] {2, 6, 5, 2}, 8);
    }
}
