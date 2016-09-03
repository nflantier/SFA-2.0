package noelflantier.sfartifacts.common.entities;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemThorHammer;

public class EntityHammerMinning extends EntityThrowable implements IEntityAdditionalSpawnData{
	
	private double orX;
	private double orY;
	private double orZ;
	private double curhyp;
	private boolean isCommingBack;
	private ItemStack hammer;
    
	public EntityHammerMinning(World worldIn)
    {
        super(worldIn);
    }

    public EntityHammerMinning(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public EntityHammerMinning(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }
    public EntityHammerMinning(World worldIn, EntityPlayer throwerIn, ItemStack stack) {
        super(worldIn, throwerIn);
		isImmuneToFire = true;
		noClip = true;
		orX = throwerIn.posX;
		orY = throwerIn.posY;
		orZ = throwerIn.posZ;
		hammer = stack;
	}

	public static void func_189662_a(DataFixer p_189662_0_)
    {
        EntityThrowable.func_189661_a(p_189662_0_, "Snowball");
    }
    	
	protected float getGravityVelocity(){
        return 0.0F;
    }
	
	@Override
    public void onUpdate(){
		super.onUpdate();
		
		if(ticksExisted>=400)
			setHammerBack();
		
		if(!worldObj.isRemote){
			
			float decy = 0.8F;
			float decx = -0.5F;
			
			if(!isCommingBack){
				curhyp = Math.sqrt( (posX-orX)*(posX-orX) + (posY-orY)*(posY-orY) + (posZ-orZ)*(posZ-orZ) );
				if(curhyp > 20)isCommingBack = true;
			}
			
			if(isCommingBack){
				if(getThrower()!=null){	
					double xth = getThrower().posX;
					double yth = getThrower().posY+0.5;
					double zth = getThrower().posZ;
					curhyp = Math.sqrt( (posX-xth)*(posX-xth) + (posY-yth)*(posY-yth) + (posZ-zth)*(posZ-zth) );
			        setThrowableHeading(xth-posX,yth-posY,zth-posZ, 1.5F, 1.0F);

					if(curhyp<1.5){
						if(isEntityAlive())setHammerBack();
					}
				}
			}
	    }
    }
	
	public void setHammerBack(){
		if(hammer == null){
			setDead();
			return;
		}
		hammer.setItemDamage(0);
		ItemNBTHelper.setBoolean(hammer, "IsThrown", false);
		isCommingBack = false;
		setDead();
	}

	@Override
	public void onImpact(RayTraceResult result) {
		if(worldObj.isRemote)return;
		if(getThrower() == null)return;
		
		if(result.entityHit!=null){
			if(result.entityHit==this.getThrower()){
				if(this.isEntityAlive())
					this.setHammerBack();
			}else if(result.entityHit instanceof EntityLivingBase){
				if(hammer != null)	
					result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setProjectile(), ((ItemThorHammer)hammer.getItem()).getToolMaterial().getDamageVsEntity());	
					hammer.hitEntity((EntityLivingBase)result.entityHit, (EntityPlayer) this.getThrower());
			}else if(result.entityHit != null){
				if(hammer != null)	
					result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setProjectile(), ((ItemThorHammer)hammer.getItem()).getToolMaterial().getDamageVsEntity());
			}
			return;
		}
		if(hammer != null)
			if(!HammerHelper.breakOnImpact(hammer,  result.getBlockPos(), 1, 1,(EntityPlayer)this.getThrower(), result, this))this.isCommingBack = true;
		
	}
	
	@Override
	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		return false;
	}
	
	@Override
	public void writeSpawnData(ByteBuf data) {
	}

	@Override
	public void readSpawnData(ByteBuf data) {
	}

}
