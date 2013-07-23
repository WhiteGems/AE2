package net.aetherteam.aether;

import java.util.HashMap;
import java.util.Random;
import net.aetherteam.playercore_api.PlayerCoreAPI;
import net.aetherteam.playercore_api.PlayerCoreAPI.PlayerCoreType;
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
    private HashMap playerInventories = new HashMap();
    private HashMap playerExtraHearts = new HashMap();
    private HashMap playerCooldowns = new HashMap();
    private HashMap playerMaxCooldowns = new HashMap();
    private HashMap playerCooldownName = new HashMap();
    private HashMap playerCoins = new HashMap();
    private HashMap playerParachutes = new HashMap();
    private HashMap playerParachuteTypes = new HashMap();
    private HashMap playerClientInfo = new HashMap();
    public static EnumArmorMaterial OBSIDIAN;

    public AetherCommonPlayerHandler getPlayerHandler(EntityPlayer var1)
    {
        if (var1 instanceof EntityPlayerMP)
        {
            PlayerBaseAetherServer var2 = (PlayerBaseAetherServer)((PlayerCoreServer)var1).getPlayerCoreObject(PlayerBaseAetherServer.class);
            return var2.getPlayerHandler();
        }
        else
        {
            return null;
        }
    }

    public void registerPlayerAPI()
    {
        PlayerCoreAPI.register(PlayerCoreType.SERVER, PlayerBaseAetherServer.class);
    }

    public void registerTickHandler() {}

    public void displayMessage(EntityPlayer var1, String var2)
    {
        var1.addChatMessage(var2);
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

    public int addArmor(String var1)
    {
        return 0;
    }

    public void registerKeyBindings() {}

    public void registerRenderers() {}

    public void registerMainMenu() {}

    public void spawnSwettyParticles(World var1, int var2, int var3, int var4) {}

    public void spawnAltarParticles(World var1, int var2, int var3, int var4, Random var5) {}

    public void spawnCloudSmoke(World var1, double var2, double var4, double var6, Random var8, Double var9) {}

    public void spawnDonatorMoaParticles(Entity var1, Random var2) {}

    public void spawnPortalParticles(World var1, int var2, int var3, int var4, Random var5, int var6) {}

    public void spawnRainParticles(World var1, int var2, int var3, int var4, Random var5, int var6) {}

    public void loadSounds() {}

    public void registerSounds() {}

    public void registerRenderPAPI() {}

    public void renderGameOverlay(float var1, boolean var2, int var3, int var4) {}

    public void playSoundFX(String var1, float var2, float var3) {}

    public void openDungeonQueue() {}

    public void spawnCloudSmoke(World var1, double var2, double var4, double var6, Random var8, double var9, double var11, double var13, double var15) {}

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
