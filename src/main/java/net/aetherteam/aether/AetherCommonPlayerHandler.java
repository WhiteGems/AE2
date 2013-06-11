package net.aetherteam.aether;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

import java.util.Random;

import net.aetherteam.aether.client.PlayerBaseAetherClient;
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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class AetherCommonPlayerHandler
{
    public int timeUntilPortal = 0;
    public boolean inPortal = false;
    public float timeInPortal;
    public float prevTimeInPortal;
    public EntityPlayer player;
    protected int field_82152_aq;
    public Random rand = new Random();
    public World worldObj;
    public int jrem;
    public static MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
    private IAetherBoss currentBoss;
    public RidingHandler riddenBy;
    public boolean verifiedOutOfPortal;
    private int verifiedOutOfPortalTime;
    private boolean beenInPortal;
    private double dungeonPosX = 0.0D;
    private double dungeonPosY = 0.0D;
    private double dungeonPosZ = 0.0D;
    private int frostBiteTimer;
    private int frostBiteTime;

    public AetherCommonPlayerHandler(EntityPlayer Player)
    {
        this.player = Player;
        this.worldObj = this.player.worldObj;
    }

    public AetherCommonPlayerHandler(PlayerBaseAetherClient playerBaseAetherClient)
    {
        this(playerBaseAetherClient.getPlayer());
    }

    public AetherCommonPlayerHandler(PlayerBaseAetherServer playerBaseAether)
    {
        this(playerBaseAether.getPlayer());
    }

    public void afterOnUpdate()
    {
        this.timeUntilPortal = this.player.timeUntilPortal;
        beforeOnLivingUpdate();

        if ((!this.worldObj.isRemote) && ((this.worldObj instanceof WorldServer)))
        {
            MinecraftServer minecraftserver = ((WorldServer) this.worldObj).getMinecraftServer();
            int i = this.player.getMaxInPortalTime();

            if (this.inPortal)
            {
                if ((this.player.ridingEntity == null) && (this.timeInPortal++ >= i))
                {
                    this.timeInPortal = i;
                    this.player.timeUntilPortal = this.player.getPortalCooldown();

                    Aether.teleportPlayerToAether((EntityPlayerMP) this.player, false);
                }

                this.inPortal = false;
            } else
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
                this.timeUntilPortal -= 1;
            }
        }

        if ((this.currentBoss != null) && ((this.currentBoss.getBossEntity() instanceof EntityLiving)))
        {
            EntityLiving bossMob = (EntityLiving) this.currentBoss;

            if ((bossMob.isDead) || (this.currentBoss.getBossHP() <= 0))
            {
                setCurrentBoss(null);
            }
        }
        if (this.frostBiteTimer > 0)
        {
            if (this.rand.nextInt(8) == 1)
            {
                this.player.attackEntityFrom(new FrostDamageSource(), this.rand.nextInt(4));
                this.frostBiteTime += 1;
            }
            this.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 3, 2));
            this.frostBiteTimer -= 1;
        }
        if (this.player.isDead) this.frostBiteTimer = 0;
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
        if (this.riddenBy != null)
        {
            return this.riddenBy.jump(this, this.player);
        }

        return true;
    }

    public void rideEntity(Entity animal, RidingHandler ridingHandler)
    {
        this.riddenBy = ridingHandler;

        if (animal == null)
        {
            this.riddenBy = null;
        }
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        EntityClientPlayerMP playerr;
        if (side.isServer())
        {
            EntityPlayerMP playerrs = (EntityPlayerMP) this.player;
            PacketDispatcher.sendPacketToPlayer(AetherPacketHandler.sendRidingPacket(animal), (Player) playerrs);
        } else if (side.isClient())
        {
            playerr = (EntityClientPlayerMP) this.player;
        }
    }

    public void setCurrentBoss(IAetherBoss boss)
    {
        this.currentBoss = boss;

        PartyMember member = PartyController.instance().getMember(this.player.username);
        Party party = PartyController.instance().getParty(member);

        if (party != null) ;
    }

    public int getFrostBiteTime()
    {
        return this.frostBiteTime;
    }

    public IAetherBoss getCurrentBoss()
    {
        return this.currentBoss;
    }

    public void setInIce()
    {
        int time = 100 + this.rand.nextInt(20 * MathHelper.clamp_int(this.rand.nextInt(13), 5, 13));
        this.frostBiteTimer = time;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherCommonPlayerHandler
 * JD-Core Version:    0.6.2
 */