package net.aetherteam.aether.entities.mounts_old;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.SyncDonatorList;
import net.aetherteam.aether.donator.choices.MoaChoice;
import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class RidingHandlerMoa extends RidingHandler
{
    protected EntityLiving animal = (EntityLiving)this.mount;
    Random random = new Random();

    public int jumpsRemaining = 0;
    public boolean jpress;
    public Random rand = new Random();

    public RidingHandlerMoa(EntityMoa entityMoa)
    {
        super(entityMoa);
    }

    public void update()
    {
        if (isBeingRidden())
        {
            this.rider.fallDistance = 0.0F;

            if ((this.rider.motionY < -0.2D) && (!this.rider.isSneaking()))
            {
                this.rider.motionY = -0.2D;
            }

            this.animal.tasks.taskEntries.clear();

            if ((this.rider.onGround) && (this.rider.isJumping))
            {
                this.rider.onGround = false;
                this.rider.motionY = 0.875D;
                this.jpress = true;
                this.jumpsRemaining -= 1;
            }
            else if ((this.rider.handleWaterMovement()) && (this.rider.isJumping))
            {
                this.rider.motionY = 0.5D;
                this.jpress = true;
                this.jumpsRemaining -= 1;
            }
            else if ((this.jumpsRemaining > 0) && (!this.jpress) && (this.rider.isJumping))
            {
                this.rider.motionY = 0.75D;
                this.jpress = true;
                this.jumpsRemaining -= 1;
            }

            if ((this.jpress) && (!this.rider.isJumping))
            {
                this.jpress = false;
            }

            if ((this.rider.onGround) || (this.rider.worldObj.getBlockId(MathHelper.floor_double(this.rider.posX), MathHelper.floor_double(this.rider.posY - 1.0D), MathHelper.floor_double(this.rider.posZ)) != 0))
            {
                this.jpress = false;
                this.jumpsRemaining = ((EntityMoa)this.animal).getColour().jumps;
            }

            if ((this.rider instanceof EntityPlayer))
            {
                String username = ((EntityPlayer)this.rider).username;

                if (Aether.syncDonatorList.isDonator(username))
                {
                    Aether.getInstance();
                    Donator donator = Aether.syncDonatorList.getDonator(username);
                    boolean hasChoice = donator.containsChoiceType(EnumChoiceType.MOA);
                    DonatorChoice choice = null;

                    if (hasChoice)
                    {
                        choice = (MoaChoice)donator.getChoiceFromType(EnumChoiceType.MOA);
                        ((MoaChoice)choice).spawnParticleEffects(this.random, (EntityPlayer)this.rider);
                    }
                }
            }

            this.rider.stepHeight = 1.0F;
        }

        super.update();
    }

    public float getSpeedModifier()
    {
        return 1.65F;
    }

    public void onUnMount()
    {
        this.rider.stepHeight = 0.5F;
        this.animal.tasks.addTask(0, new EntityAISwimming(this.animal));
        this.animal.tasks.addTask(1, new EntityAIPanic((EntityCreature)this.animal, 0.38F));
        this.animal.tasks.addTask(2, new EntityAIWander((EntityCreature)this.animal, 0.3F));
        this.animal.tasks.addTask(4, new EntityAIWatchClosest(this.animal, EntityPlayer.class, 6.0F));
        this.animal.tasks.addTask(5, new EntityAILookIdle(this.animal));
        super.onUnMount();
    }

    public boolean shouldBeSitting()
    {
        return isBeingRidden();
    }

    public double getRiderYOffset()
    {
        return 0.85D;
    }

    public void onMount()
    {
        if ((this.rider != null) && ((this.rider instanceof EntityPlayer)))
        {
            String username = ((EntityPlayer)this.rider).username;

            if (Aether.syncDonatorList.isDonator(username))
            {
                Aether.getInstance();
                Donator donator = Aether.syncDonatorList.getDonator(username);
                boolean hasChoice = donator.containsChoiceType(EnumChoiceType.MOA);
                DonatorChoice choice = null;

                if (hasChoice)
                {
                    for (int j = 0; j < 10; j++)
                    {
                        double d = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        double d3 = 5.0D;
                        this.rider.worldObj.spawnParticle("flame", this.rider.posX + this.random.nextFloat() * this.rider.width * 2.0F - this.rider.width - d * d3, this.rider.posY + this.random.nextFloat() * (this.rider.height - 0.6F) - d1 * d3, this.rider.posZ + this.random.nextFloat() * this.rider.width * 2.0F - this.rider.width - d2 * d3, d, d1, d2);
                        this.rider.worldObj.spawnParticle("largeexplode", this.rider.posX + this.random.nextFloat() * this.rider.width * 2.0F - this.rider.width - d * d3, this.rider.posY + this.random.nextFloat() * (this.rider.height - 0.6F) - d1 * d3, this.rider.posZ + this.random.nextFloat() * this.rider.width * 2.0F - this.rider.width - d2 * d3, d, d1, d2);
                    }
                }
            }
        }
    }
}

