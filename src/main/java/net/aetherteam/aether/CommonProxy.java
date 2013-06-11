package net.aetherteam.aether;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumHelper;

public class CommonProxy
{
    private HashMap playerInventories = new HashMap();
    private HashMap playerExtraHearts = new HashMap();
    private HashMap playerCooldowns = new HashMap();
    private HashMap playerMaxCooldowns = new HashMap();
    private HashMap playerCooldownName = new HashMap();
    private HashMap playerCoins = new HashMap();

    private HashMap playerClientInfo = new HashMap();

    public static EnumArmorMaterial OBSIDIAN = EnumHelper.addArmorMaterial("OBISIDAN", 33, new int[]{2, 6, 5, 2}, 8);

    public AetherCommonPlayerHandler getPlayerHandler(EntityPlayer entity)
    {
        if ((entity instanceof EntityPlayerMP))
        {
            return ((PlayerBaseAetherServer) ((EntityPlayerMP) entity).getServerPlayerBase("Aether II")).getPlayerHandler();
        }

        return null;
    }

    public void registerPlayerAPI()
    {
        ServerPlayerAPI.register("Aether II", PlayerBaseAetherServer.class);
    }

    public void registerTickHandler()
    {
    }

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

    public void registerKeyBindings()
    {
    }

    public void registerRenderers()
    {
    }

    public void registerMainMenu()
    {
    }

    public void spawnSwettyParticles(World world, int x, int y, int z)
    {
    }

    public void spawnAltarParticles(World world, int x, int y, int z, Random rand)
    {
    }

    public void spawnCloudSmoke(World world, double x, double y, double z, Random rand, Double radius)
    {
    }

    public void spawnDonatorMoaParticles(Entity rider, Random rand)
    {
    }

    public void spawnPortalParticles(World world, int x, int y, int z, Random random, int blockID)
    {
    }

    public void spawnRainParticles(World world, int x, int y, int z, Random random, int amount)
    {
    }

    public void loadSounds()
    {
    }

    public void registerSounds()
    {
    }

    public void registerRenderPAPI()
    {
    }

    public void renderGameOverlay(float zLevel, boolean flag, int x, int y)
    {
    }

    public void playSoundFX(String path, float volume, float pitch)
    {
    }

    public void openDungeonQueue()
    {
    }

    static
    {
        new EnumHelper();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.CommonProxy
 * JD-Core Version:    0.6.2
 */