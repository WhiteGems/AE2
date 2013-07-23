package net.aetherteam.aether.entities.bosses;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.party.PartyController;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityKey extends Entity
{
    public int age;
    public int delayBeforeCanPickup;
    private int health;
    public float hoverStart;
    private Dungeon dungeon;

    public EntityKey(World par1World, double par2, double par4, double par6)
    {
        super(par1World);
        this.age = 0;
        this.health = 5;
        this.hoverStart = ((float)(Math.random() * Math.PI * 2.0D));
        setSize(0.25F, 0.25F);
        this.yOffset = (this.height / 2.0F);
        setPosition(par2, par4, par6);
        this.rotationYaw = 0.0F;
    }

    public EntityKey(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack)
    {
        this(par1World, par2, par4, par6);
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityKey(World par1World)
    {
        super(par1World);
        this.age = 0;
        this.health = 5;
        this.hoverStart = ((float)(Math.random() * Math.PI * 2.0D));
        setSize(0.25F, 0.25F);
        this.yOffset = (this.height / 2.0F);
    }

    protected void entityInit()
    {
        getDataWatcher().addObjectByDataType(10, 5);
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.noClip = true;
        this.age += 1;

        if ((getDungeon() == null) || ((getDungeon() != null) && (!getDungeon().isActive())))
        {
            setDead();
        }
    }

    public void setAgeToCreativeDespawnTime()
    {
        this.age = 4800;
    }

    public boolean handleWaterMovement()
    {
        return false;
    }

    protected void dealFireDamage(int par1)
    {
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("Health", (short)(byte)this.health);
        par1NBTTagCompound.setShort("Age", (short)this.age);
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        setDead();
        this.health = (par1NBTTagCompound.getShort("Health") & 0xFF);
        this.age = par1NBTTagCompound.getShort("Age");
        setKeyName(par1NBTTagCompound.getString("KeyName"));
    }

    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        DungeonHandler.instance().getDungeon(PartyController.instance().getParty(par1EntityPlayer)).addKey(new DungeonKey(EnumKeyType.getEnumFromItem(this)));
    }

    public boolean canAttackWithItem()
    {
        return false;
    }

    public void travelToDimension(int par1)
    {
    }

    public void setKeyName(String keyName)
    {
        getDataWatcher().updateObject(10, keyName);
        getDataWatcher().setObjectWatched(10);
    }

    public String getKeyName()
    {
        return getDataWatcher().getWatchableObjectString(10);
    }

    public void setDungeon(Dungeon dungeon)
    {
        this.dungeon = dungeon;
    }

    public Dungeon getDungeon()
    {
        return this.dungeon;
    }
}

