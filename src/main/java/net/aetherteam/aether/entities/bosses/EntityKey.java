package net.aetherteam.aether.entities.bosses;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.dungeons.keys.DungeonKey;
import net.aetherteam.aether.dungeons.keys.EnumKeyType;
import net.aetherteam.aether.party.PartyController;
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

    public EntityKey(World var1, double var2, double var4, double var6)
    {
        super(var1);
        this.age = 0;
        this.health = 5;
        this.hoverStart = (float) (Math.random() * Math.PI * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(var2, var4, var6);
        this.rotationYaw = 0.0F;
    }

    public EntityKey(World var1, double var2, double var4, double var6, ItemStack var8)
    {
        this(var1, var2, var4, var6);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityKey(World var1)
    {
        super(var1);
        this.age = 0;
        this.health = 5;
        this.hoverStart = (float) (Math.random() * Math.PI * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
    }

    protected void entityInit()
    {
        this.getDataWatcher().addObjectByDataType(10, 5);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.noClip = true;
        ++this.age;

        if (this.getDungeon() == null || this.getDungeon() != null && !this.getDungeon().isActive())
        {
            this.setDead();
        }
    }

    public void setAgeToCreativeDespawnTime()
    {
        this.age = 4800;
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return false;
    }

    /**
     * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    protected void dealFireDamage(int var1)
    {}

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setShort("Health", (short) ((byte) this.health));
        var1.setShort("Age", (short) this.age);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        this.setDead();
        this.health = var1.getShort("Health") & 255;
        this.age = var1.getShort("Age");
        this.setKeyName(var1.getString("KeyName"));
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer var1)
    {
        DungeonHandler.instance().getDungeon(PartyController.instance().getParty(var1)).addKey(new DungeonKey(EnumKeyType.getEnumFromItem(this)));
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }

    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int var1)
    {}

    public void setKeyName(String var1)
    {
        this.getDataWatcher().updateObject(10, var1);
        this.getDataWatcher().setObjectWatched(10);
    }

    public String getKeyName()
    {
        return this.getDataWatcher().getWatchableObjectString(10);
    }

    public void setDungeon(Dungeon var1)
    {
        this.dungeon = var1;
    }

    public Dungeon getDungeon()
    {
        return this.dungeon;
    }
}
