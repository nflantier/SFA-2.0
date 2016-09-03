package noelflantier.sfartifacts.common.entities;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.entities.ai.EntityAIJumpAndCollide;
import noelflantier.sfartifacts.common.entities.ai.EntityAITargetBlock;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class EntityHulk extends EntityMob implements IRangedAttackMob{

    private boolean isRunningAway = false;
    private int runningAwayTimer = -1;
    private int flingAttackTimer = 0;
    private int destroyAroundTimer = 0;
    private int randomDropTimer = 0;
    private int jumpAndDestroyAroundTimer = 0;
    private Random rdm = new Random();
    private EntityAINearestAttackableTarget aiNearest;
    private EntityAIHurtByTarget aiHurt;
    private EntityAITargetBlock aiTargetAway;
    public int countFleshDropped = 0;
    private int animationSmash = 0;
    private int lastLoot = 0;
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS));
    
    
    public EntityHulk(World world){
        super(world);
        this.setHealth(this.getMaxHealth());
        this.setSize(2.5F, 4.5F);
        ((PathNavigateGround)this.getNavigator()).setCanSwim(true);
        this.isImmuneToFire = true;
        this.experienceValue = 50;
        this.stepHeight = 2.0F;
    }

    @Override
    protected void initEntityAI(){
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIJumpAndCollide(this, 1.1F));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 0.8D, true));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 100.0F));
        aiNearest = new EntityAINearestAttackableTarget(this, EntityPlayer.class, true);
        aiHurt = new EntityAIHurtByTarget(this, false, new Class[0]);
        aiTargetAway = new EntityAITargetBlock(this, 0,true,false,(int)this.posX+1000,(int)this.posY+100,(int)this.posZ+1000);
        this.targetTasks.addTask(1, aiNearest);
        this.targetTasks.addTask(2, aiHurt);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt){
        super.writeEntityToNBT(nbt);
		nbt.setBoolean("isRunningAway", isRunningAway);
		nbt.setInteger("flingAttackTimer", flingAttackTimer);
		nbt.setInteger("destroyAroundTimer", destroyAroundTimer);
		nbt.setInteger("randomDropTimer", randomDropTimer);
		nbt.setInteger("jumpAndDestroyAroundTimer", jumpAndDestroyAroundTimer);		
		nbt.setInteger("lastLoot", lastLoot);
		nbt.setInteger("countFleshDropped", countFleshDropped);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt){
        super.readEntityFromNBT(nbt);
		isRunningAway = nbt.getBoolean("isRunningAway");
		flingAttackTimer = nbt.getInteger("flingAttackTimer");
		destroyAroundTimer = nbt.getInteger("destroyAroundTimer");
		randomDropTimer = nbt.getInteger("randomDropTimer");
		jumpAndDestroyAroundTimer = nbt.getInteger("jumpAndDestroyAroundTimer");
		lastLoot = nbt.getInteger("lastLoot");
		countFleshDropped = nbt.getInteger("countFleshDropped");
    }

    @Override
    public void fall(float distance, float damageMultiplier){
    
    }
    
    @Override
    public boolean canBreatheUnderwater(){
        return true;
    }

    @Override
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000.0D);//1000
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.9D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(12.0D);//12
    }

    @Override
    protected void entityInit(){
        super.entityInit();
        /*this.dataManager.register(STATE, Integer.valueOf(-1));
        this.dataManager.register(POWERED, Boolean.valueOf(false));
        this.dataManager.register(IGNITED, Boolean.valueOf(false));*/
    }

    @Override
    protected void collideWithEntity(Entity p_82167_1_){
        this.setAttackTarget((EntityLivingBase)p_82167_1_);
        super.collideWithEntity(p_82167_1_);
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int loot){
    	dropWithCurrentLoot();
    }

    public void dropWithCurrentLoot(){
    	int nb = 1;
    	if (lastLoot > 0){
            nb += this.rand.nextInt(lastLoot);
        }
        this.dropItem(ModItems.itemHulkFlesh, nb);
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource ds, float par2){
        if (isEntityInvulnerable(ds)){
            return false;
        }else{
            Entity entity = ds.getEntity();
            int loot = 0;
        	if (entity instanceof EntityPlayer){
        		loot = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
	        }
        	lastLoot = loot;
        	if(!this.worldObj.isRemote && this.countFleshDropped<3 && this.randomDropTimer<=0 && this.rdm.nextFloat()<=0.1F && par2>10){
        		this.randomDropTimer = 500;
        		this.countFleshDropped +=1;
                this.dropItem(ModItems.itemHulkFlesh, 1);
        	}
            return super.attackEntityFrom(ds, par2);
        }
    }

    /*protected void attackEntity(Entity p_70785_1_, float p_70785_2_){
    	super.attackEntity(p_70785_1_, p_70785_2_);
        if (this.attackTime <= 0 && p_70785_2_ < 2.0F && p_70785_1_.boundingBox.maxY > this.boundingBox.minY && p_70785_1_.boundingBox.minY < this.boundingBox.maxY){
            this.attackTime = 20;
            this.attackEntityAsMob(p_70785_1_);
        }
    }*/

    @Override
    public boolean attackEntityAsMob(Entity entity){
    	int pref = flingAttackTimer;
    	this.flingAttackTimer = 10;
    	
    	if(pref == 0)
    		this.worldObj.setEntityState(this, (byte)4);

    	boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 
    			(float)(this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (flag){
        	entity.motionY += 0.6000000059604645D;
        }
        return flag;
    }
    
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte p_70103_1_){
        if (p_70103_1_ == 4)
        {
            this.flingAttackTimer = 10;
        }else if (p_70103_1_ == 18)
        {
            this.animationSmash = 10;
        }
        else
        {
            super.handleStatusUpdate(p_70103_1_);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getAnimationSmash(){
        return this.animationSmash;
    }
    
    @SideOnly(Side.CLIENT)
    public int getAttackFlingTimer(){
        return this.flingAttackTimer;
    }
    
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
       
        //this.setDead();
        
        if(this.randomDropTimer>0)
        	this.randomDropTimer-=1;
        
        if(this.flingAttackTimer>0)
        	this.flingAttackTimer-=1;
        
        if(this.animationSmash>0)
        	this.animationSmash-=1;
        
        if(this.worldObj.isRemote)
        	return;
        
        if(this.getHealth()-1<this.getMaxHealth()/2 && !this.isRunningAway){
        	this.isRunningAway = true;
        	this.targetTasks.removeTask(aiNearest);
        	this.targetTasks.removeTask(aiHurt);
        	this.targetTasks.addTask(0,aiTargetAway);
        	runningAwayTimer = 100;
        }
        if(this.runningAwayTimer>0)
        	this.runningAwayTimer-=1;

        if(this.runningAwayTimer==50){
        	this.noClip = true;
        	dropWithCurrentLoot();
        	this.motionX *= 15;
        	this.motionZ *= 15;
        	this.motionY+=8.0D;
    	}
        
        if(this.runningAwayTimer==0)
        	this.setDead();
        
        if(this.destroyAroundTimer>0)
        	this.destroyAroundTimer-=1;

        if(this.jumpAndDestroyAroundTimer>0)
        	this.jumpAndDestroyAroundTimer-=1;
        

        if(this.jumpAndDestroyAroundTimer==1)
        	this.smashAround();
        
        if(this.destroyAroundTimer==0){
	        if(this.getAttackTarget()!=null){
	        	if(this.getNavigator().getPath()==null || this.getNavigator().getPathToEntityLiving(this.getAttackTarget())==null){
	        		hulkSmash(true);
	        	}
	        }else if(this.getAttackTarget()==null){
	        	//if(this.getNavigator().getPath()==null)
	        	hulkSmash(false);
	        }
        }
        /*if (this.getDataWatcher().getWatchableObjectInt(17) > 0){
        	this.getDataWatcher().updateObject(17, this.getDataWatcher().getWatchableObjectInt(17)-1);
            
        }*/
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public void smashAround(){
        this.worldObj.setEntityState(this, (byte)18);
        animationSmash = 10;
        int hulkx = MathHelper.floor_double(this.posX);
        int hulky = MathHelper.floor_double(this.posY);
        int hulkz = MathHelper.floor_double(this.posZ);
        
        boolean flag = false;

        for (int by = 0; by <= 4+rdm.nextInt(3); ++by)
        {
            for (int bz = -4-rdm.nextInt(3); bz <= 4+rdm.nextInt(3); ++bz)
            {
                for (int bx = -4-rdm.nextInt(3); bx <= 4+rdm.nextInt(3); ++bx)
                {
                	BlockPos bpos = this.getPosition().add(bx, by, bz);
                	IBlockState bstate = worldObj.getBlockState(bpos);
                    Block block = bstate.getBlock();

                    if (!worldObj.isAirBlock(bpos) && block.canEntityDestroy(bstate, worldObj, bpos, this))
                    {
                        flag = this.worldObj.destroyBlock(bpos, true) || flag;
                    }
                }
            }
        }
    }
    
    public void jumpAndSmashAround(){
    	this.motionY+=0.6000000059604645D;
    	jumpAndDestroyAroundTimer = 20;
    }
    
    public void hulkSmash(boolean hasentity){
		if(hasentity){
			if(this.getAttackTarget().posY>this.posY){
				jumpAndSmashAround();
			}else if(this.getAttackTarget().posY<this.posY){
				jumpAndSmashAround();
			}else{
				smashAround();
			}
	    	this.destroyAroundTimer = 400;
		}else{
			smashAround();
	    	this.destroyAroundTimer = 110;
		}
    }

	@Override
    public boolean isEntityInvulnerable(DamageSource source){
    	if(this.runningAwayTimer>0)
    		return true;
        return super.isEntityInvulnerable(source);
    }
	@Override
    protected boolean canDespawn(){
        return this.runningAwayTimer<=0?true:false;
    }
	@Override
    protected void despawnEntity(){
        this.entityAge = 0;
    }
	@Override
    protected void updateAITasks(){
        super.updateAITasks();
    }
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
		
	}
	@Override
    public boolean isNonBoss(){
        return false;
    }
	@Override
    protected boolean canBeRidden(Entity entityIn){
        return false;
    }
	@Override
    public void addTrackingPlayer(EntityPlayerMP player){
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }
	@Override
    public void removeTrackingPlayer(EntityPlayerMP player){
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
}
