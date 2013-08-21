package net.aetherteam.aether;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.util.Random;
import net.aetherteam.aether.client.PlayerAetherClient;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class AetherCommonPlayerHandler
{
    public int timeUntilPortal;
    public boolean inPortal;
    public float timeInPortal;
    public float prevTimeInPortal;
    public EntityPlayer player;
    protected int teleportDirection;
    public Random rand;
    public World worldObj;
    public int jrem;
    public static MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
    private IAetherBoss currentBoss;
    public RidingHandler riddenBy;
    public boolean verifiedOutOfPortal;
    private int verifiedOutOfPortalTime;
    private boolean beenInPortal;
    private double dungeonPosX;
    private double dungeonPosY;
    private double dungeonPosZ;
    private boolean isParachuting;

    public AetherCommonPlayerHandler(EntityPlayer Player)
    {
        this.timeUntilPortal = 0;
        this.inPortal = false;
        this.rand = new Random();
        this.dungeonPosX = 0.0D;
        this.dungeonPosY = 0.0D;
        this.dungeonPosZ = 0.0D;
        this.player = Player;
        this.worldObj = this.player.worldObj;
    }

    public AetherCommonPlayerHandler(PlayerAetherClient playerBaseAetherClient)
    {
        this(playerBaseAetherClient.getPlayer());
    }

    public AetherCommonPlayerHandler(PlayerAetherServer playerBaseAether)
    {
        this(playerBaseAether.getPlayer());
    }

    public void afterOnUpdate()
    {
        this.timeUntilPortal = this.player.timeUntilPortal;
        this.beforeOnLivingUpdate();

        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer)
        {
            MinecraftServer bossMob = ((WorldServer)this.worldObj).getMinecraftServer();
            int i = this.player.getMaxInPortalTime();

            if (this.inPortal)
            {
                if (this.player.ridingEntity == null && this.timeInPortal++ >= (float)i)
                {
                    this.timeInPortal = (float)i;
                    this.player.timeUntilPortal = this.player.getPortalCooldown();
                    Aether.teleportPlayerToAether((EntityPlayerMP)this.player, false);
                }

                this.inPortal = false;
            }
            else
            {
                if (this.timeInPortal > 0.0F)
                {
                    this.timeInPortal -= 4.0F;
                }

                if (this.timeInPortal < 0.0F)
                {
                    this.timeInPortal = 0.0F;
                }
            }

            if (this.timeUntilPortal > 0)
            {
                --this.timeUntilPortal;
            }
        }

        if (this.currentBoss != null && this.currentBoss.getBossEntity() instanceof EntityLiving)
        {
            EntityLiving var4 = (EntityLiving)this.currentBoss;

            if (var4.isDead || this.currentBoss.getBossHP() <= 0)
            {
                this.setCurrentBoss((IAetherBoss)null);
            }
        }
    }

    public void playPortalSFX()
    {
        if (this.timeInPortal == 0.0F)
        {
            this.player.worldObj.playSoundAtEntity(this.player, "portal.trigger", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
        }
    }

    public float getSpeedModifier()
    {
        return this.riddenBy != null ? this.riddenBy.getSpeedModifier() : -1.0F;
    }

    public void beforeOnLivingUpdate()
    {
        if (this.riddenBy != null)
        {
            this.riddenBy.update();
        }

        AetherPoison.poisonTick(this.player);
    }

    public boolean jump()
    {
        return this.riddenBy != null ? this.riddenBy.jump(this, this.player) : true;
    }

    public void rideEntity(Entity animal, RidingHandler ridingHandler)
    {
        this.riddenBy = ridingHandler;

        if (animal == null)
        {
            this.riddenBy = null;
        }

        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            EntityPlayerMP playerr = (EntityPlayerMP)this.player;
            PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendRidingPacket(animal), (Player)playerr);
        }
        else if (side.isClient())
        {
            EntityClientPlayerMP playerr1 = (EntityClientPlayerMP)this.player;
        }
    }

    public void setCurrentBoss(IAetherBoss boss)
    {
        this.currentBoss = boss;
        PartyMember member = PartyController.instance().getMember(this.player.username);
        Party party = PartyController.instance().getParty(member);

        if (party != null)
        {
            ;
        }
    }

    public boolean getParachuting()
    {
        return this.isParachuting;
    }

    public void setParachuting(boolean b)
    {
        this.isParachuting = b;
    }

    public IAetherBoss getCurrentBoss()
    {
        return this.currentBoss;
    }
}
