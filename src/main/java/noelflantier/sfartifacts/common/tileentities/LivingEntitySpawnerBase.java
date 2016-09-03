package noelflantier.sfartifacts.common.tileentities;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.monster.ZombieType;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.recipes.handler.SoundEmitterConfig;

public abstract class LivingEntitySpawnerBase {
    /** The delay to spawn. */
	public int spawnDelay = 20;
    //public final List<WeightedSpawnerEntity> minecartToSpawn = Lists.<WeightedSpawnerEntity>newArrayList();
    public WeightedSpawnerEntity theEntity = new WeightedSpawnerEntity();
    /** The rotation of the mob inside the mob spawner */
    public double mobRotation;
    /** the previous rotation of the mob inside the mob spawner */
    public double prevMobRotation;
    public int minSpawnDelay = 200;
    public int maxSpawnDelay = 600;
	public int spawnCount = 1;
	public int maxSpawnCount = 5;
	public int minSpawnCount = 1;
	public boolean spawnEntityOnce = false;
    /** Cached instance of the entity to render inside the spawner. */
    public Entity cachedEntity;
    public int maxNearbyEntities = 100;
    /** The distance from which a player activates the spawner. */
    public int activatingRangeFromPlayer = 25;
    /** The range coefficient for spawning entities around. */
	public int spawnRange = 4;
	public int minSpawnRange = 0;
	public int customX = 0;
	public int customY = 0;
	public int customZ = 0;
	
	public boolean attractedToSpawner = false;
	public boolean requiresPlayer = false;
	public boolean followVanillaSpawnRules = false;

    /**
     * Gets the entity name that should be spawned.
     */
    public String getEntityNameToSpawn()
    {
        return this.theEntity.getNbt().getString("id");
    }

    public void setEntityName(String name)
    {
        this.theEntity.getNbt().setString("id", name);
    }

    /**
     * Returns true if there's a player close enough to this mob spawner to activate it.
     */
    public boolean isActivated()
    {
    	if(!requiresPlayer)
    		return true;
        BlockPos blockpos = this.getSpawnerPosition();
        return this.getSpawnerWorld().isAnyPlayerWithinRangeAt((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, (double)this.activatingRangeFromPlayer);
    }

    public void updateSpawner()
    {
        if (!this.isActivated())
        {
            this.prevMobRotation = this.mobRotation;
        }
        else
        {
            BlockPos blockpos = this.getSpawnerPosition();

            if (this.getSpawnerWorld().isRemote)
            {
                double d3 = (double)((float)blockpos.getX() + this.getSpawnerWorld().rand.nextFloat());
                double d4 = (double)((float)blockpos.getY() + this.getSpawnerWorld().rand.nextFloat());
                double d5 = (double)((float)blockpos.getZ() + this.getSpawnerWorld().rand.nextFloat());
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                }

                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + (double)(1000.0F / ((float)this.spawnDelay + 2000.0F))) % 360.0D;
            }
            else
            {
                if (this.spawnDelay == -1)
                {
                    this.resetTimer();
                }
                
                if(this.spawnDelay==0){
					if(!spawnConditions()){
						this.resetTimer();
						return;
					}
				}
                
                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < this.spawnCount; ++i)
                {
                    NBTTagCompound nbttagcompound = this.theEntity.getNbt();
                    NBTTagList nbttaglist = nbttagcompound.getTagList("Pos", 6);
                    World world = this.getSpawnerWorld();
                    int j = nbttaglist.tagCount();
                    double d0 = j >= 1 ? nbttaglist.getDoubleAt(0) : (double)blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    double d1 = j >= 2 ? nbttaglist.getDoubleAt(1) : (double)(blockpos.getY() + world.rand.nextInt(3) - 1);
                    double d2 = j >= 3 ? nbttaglist.getDoubleAt(2) : (double)blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, false);

                    if (entity == null)
                    {
                        return;
                    }
                    
                    entityJustCreated(entity);
                    
                    int k = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), (double)(blockpos.getX() + 1), (double)(blockpos.getY() + 1), (double)(blockpos.getZ() + 1))).expandXyz((double)this.spawnRange)).size();

                    if (k >= this.maxNearbyEntities)
                    {
                        this.resetTimer();
                        return;
                    }

                    EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                    entity.setLocationAndAngles(entity.posX+customX, entity.posY+customY, entity.posZ+customZ, world.rand.nextFloat() * 360.0F, 0.0F);

                    if ( ( entityliving == null || net.minecraftforge.event.ForgeEventFactory.canEntitySpawnSpawner(entityliving, getSpawnerWorld(), (float)entity.posX, (float)entity.posY, (float)entity.posZ) || !followVanillaSpawnRules ) && getSpawnerWorld().isAirBlock(entity.getPosition()))
                    {
                        if (this.theEntity.getNbt().getSize() == 1 && this.theEntity.getNbt().hasKey("id", 8) && entity instanceof EntityLiving)
                        {
                            if (!net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(entityliving, this.getSpawnerWorld(), (float)entity.posX, (float)entity.posY, (float)entity.posZ))
                            ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);
                        }
                        
                        List<String> variants = SoundEmitterConfig.getInstance().ENTITIES_VARIANTS.get(getEntityNameToSpawn());
                    	if(entity.getClass()==EntitySkeleton.class){
                    		if(getVariant() == -1){
                    			((EntitySkeleton)entity).func_189768_a(SkeletonType.NORMAL);
                    		}else if(variants!=null && !variants.isEmpty()){
                                String var = variants.get(getVariant());
                    			if(var!=null && var.equals("WitherSkeleton")){
    	                			((EntitySkeleton)entity).tasks.addTask(4, new EntityAIAttackMelee(((EntitySkeleton)entity), 1.2D, false));
    	                			((EntitySkeleton)entity).func_189768_a(SkeletonType.WITHER);
    	                			((EntitySkeleton)entity).setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
    	                			((EntitySkeleton)entity).getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    	                		}else if(var!=null && var.equals("Stray")){
    	                			((EntitySkeleton)entity).func_189768_a(SkeletonType.STRAY);
    	                		}
                    		}
                    	}else if(entity.getClass()==EntityZombie.class){
                    		if(getVariant() == -1){
                    			((EntityZombie)entity).func_189778_a(ZombieType.NORMAL);
                    		}else if(variants!=null && !variants.isEmpty()){
                                String var = variants.get(getVariant());
                        		if(var == "Husk"){
                        			((EntityZombie)entity).func_189778_a(ZombieType.HUSK);
                        		}
                    		}
                    	}else if(entity.getClass()==EntityHorse.class){
                    		if(getVariant() == -1){
                    			((EntityHorse)entity).setType(HorseType.HORSE);
                    			((EntityHorse)entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0F + (float)(this.getSpawnerWorld().rand.nextInt(8) + (float)this.getSpawnerWorld().rand.nextInt(9)));
                    			((EntityHorse)entity).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((0.44999998807907104D + this.getSpawnerWorld().rand.nextDouble() * 0.3D + this.getSpawnerWorld().rand.nextDouble() * 0.3D + this.getSpawnerWorld().rand.nextDouble() * 0.3D) * 0.25D);
                    		}else if(variants!=null && !variants.isEmpty()){
                                String var = variants.get(getVariant());
                        		if(var == "Donkey"){
                        			((EntityHorse)entity).setType(HorseType.DONKEY);
                        			((EntityHorse)entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0F + (float)this.getSpawnerWorld().rand.nextInt(8) + (float)this.getSpawnerWorld().rand.nextInt(9));
                        			((EntityHorse)entity).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.17499999701976776D);
                        		}else if(var == "Mule"){
                        			((EntityHorse)entity).setType(HorseType.MULE);
                        			((EntityHorse)entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0F + (float)this.getSpawnerWorld().rand.nextInt(8) + (float)this.getSpawnerWorld().rand.nextInt(9));
                        			((EntityHorse)entity).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.17499999701976776D);
                        		}else if(var == "ZombieHorse"){
                        			((EntityHorse)entity).setType(HorseType.ZOMBIE);
                        			((EntityHorse)entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
                        			((EntityHorse)entity).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
                        		}else if(var == "SkeletonHorse"){
                        			((EntityHorse)entity).setType(HorseType.SKELETON);
                        			((EntityHorse)entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
                        			((EntityHorse)entity).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
                        		}
                    		}
                    	}
                        
                        AnvilChunkLoader.spawnEntity(entity, world);
                        entityJustSpawned(entityliving!=null?entityliving:entity);
                        world.playEvent(2004, blockpos, 0);

                        if (entityliving != null)
                        {
                            entityliving.spawnExplosionParticle();
                        }

                        flag = true;
                    }
                }

                this.customX = 0;
                this.customY = 0;
                this.customZ = 0;
                if (flag)
                {
                    this.resetTimer();
                }
                finishSpawning();
            }
        }
    }

    public void resetTimer()
    {
        if (this.maxSpawnDelay <= this.minSpawnDelay)
        {
            this.spawnDelay = this.minSpawnDelay;
        }
        else
        {
            int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(i);
        }
        
        newSpawning();
        /*if (!this.minecartToSpawn.isEmpty())
        {
            this.setNextSpawnData((WeightedSpawnerEntity)WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.minecartToSpawn));
        }*/

        this.broadcastEvent(1);
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        this.spawnDelay = nbt.getShort("Delay");
        //this.minecartToSpawn.clear();
		requiresPlayer = nbt.getBoolean("RequiresPlayer");
		
        /*if (nbt.hasKey("SpawnPotentials", 9))
        {
            NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                this.minecartToSpawn.add(new WeightedSpawnerEntity(nbttaglist.getCompoundTagAt(i)));
            }
        }*/

        NBTTagCompound nbttagcompound = nbt.getCompoundTag("SpawnData");

        if (!nbttagcompound.hasKey("id", 8))
        {
            nbttagcompound.setString("id", "Pig");
        }

        //this.setNextSpawnData(new WeightedSpawnerEntity(1, nbttagcompound));

        if (nbt.hasKey("MinSpawnDelay", 99))
        {
            this.minSpawnDelay = nbt.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
            this.spawnCount = nbt.getShort("SpawnCount");
        }

        if (nbt.hasKey("MaxNearbyEntities", 99))
        {
            this.maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
            this.activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
        }

        if (nbt.hasKey("SpawnRange", 99))
        {
            this.spawnRange = nbt.getShort("SpawnRange");
        }

        if (this.getSpawnerWorld() != null)
        {
            this.cachedEntity = null;
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound p_189530_1_)
    {
        String s = this.getEntityNameToSpawn();

        if (StringUtils.isNullOrEmpty(s))
        {
            return p_189530_1_;
        }
        else
        {
        	p_189530_1_.setBoolean("RequiresPlayer", requiresPlayer);
            p_189530_1_.setShort("Delay", (short)this.spawnDelay);
            p_189530_1_.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
            p_189530_1_.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
            p_189530_1_.setShort("SpawnCount", (short)this.spawnCount);
            p_189530_1_.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
            p_189530_1_.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
            p_189530_1_.setShort("SpawnRange", (short)this.spawnRange);
            p_189530_1_.setTag("SpawnData", this.theEntity.getNbt().copy());
            /*NBTTagList nbttaglist = new NBTTagList();

            if (this.minecartToSpawn.isEmpty())
            {
                nbttaglist.appendTag(this.theEntity.toCompoundTag());
            }
            else
            {
                for (WeightedSpawnerEntity weightedspawnerentity : this.minecartToSpawn)
                {
                    nbttaglist.appendTag(weightedspawnerentity.toCompoundTag());
                }
            }

            p_189530_1_.setTag("SpawnPotentials", nbttaglist);*/
            return p_189530_1_;
        }
    }

    /**
     * Sets the delay to minDelay if parameter given is 1, else return false.
     */
    public boolean setDelayToMin(int delay)
    {
        if (delay == 1 && this.getSpawnerWorld().isRemote)
        {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        }
        else
        {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    public Entity getCachedEntity()
    {
        if (this.cachedEntity == null)
        {
            this.cachedEntity = AnvilChunkLoader.readWorldEntity(this.theEntity.getNbt(), this.getSpawnerWorld(), false);

            if (this.theEntity.getNbt().getSize() == 1 && this.theEntity.getNbt().hasKey("id", 8) && this.cachedEntity instanceof EntityLiving)
            {
                ((EntityLiving)this.cachedEntity).onInitialSpawn(this.getSpawnerWorld().getDifficultyForLocation(new BlockPos(this.cachedEntity)), (IEntityLivingData)null);
            }
        }

        return this.cachedEntity;
    }

    public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_)
    {
        this.theEntity = p_184993_1_;
    }
    
    public abstract int getVariant();
    public abstract boolean spawnConditions();
    public abstract void broadcastEvent(int id);
    public abstract World getSpawnerWorld();
    public abstract BlockPos getSpawnerPosition();
    public abstract void entityJustCreated(Entity entity);
    public abstract void entityJustSpawned(Entity entity);
    public abstract void finishSpawning();
    public abstract void newSpawning();

    @SideOnly(Side.CLIENT)
    public double getMobRotation()
    {
        return this.mobRotation;
    }

    @SideOnly(Side.CLIENT)
    public double getPrevMobRotation()
    {
        return this.prevMobRotation;
    }
}