package noelflantier.sfartifacts.common.entities;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemVibraniumShield;

public class EntityShieldThrow extends EntityThrowable implements IEntityAdditionalSpawnData{

	private int typeShield;
	private double orX,orY,orZ,curhyp;
	private ItemStack shield;
	public int tickTravel = 20;
	public int currentTickTravel = 0;
	public boolean isCommingBack = false;
	public float angleYaw = 0;
	public float anglePitch = 0;
	public boolean blockMop = false;
	
	public EntityShieldThrow(World w) {
		super(w);
		this.isImmuneToFire = true;
		this.noClip = false;
	}
	
	public EntityShieldThrow(World w, EntityLivingBase p){
		super(w,p);
		this.orX = p.posX;
		this.orY = p.posY;
		this.orZ = p.posZ;
		this.isImmuneToFire = true;
		this.noClip = false;
		if(w.isRemote)
			this.setVelocity(this.motionX*1.5, this.motionY*1.5,this.motionZ*1.5);
	}
	
	public EntityShieldThrow(World w, EntityLivingBase p, ItemStack stack){
		this(w,p);
		this.shield = stack;
	}
	
	public EntityShieldThrow(World w, EntityLivingBase p, ItemStack stack, int type){
		this(w,p,stack);
		this.typeShield = type;
	}
	
	public void setV(double vx, double vy, double vz){
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(vx * vx + vz * vz);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(vx, vz) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(vy, (double)f) * 180.0D / Math.PI);
        }
	}

	public int getTypeShield() {
		return typeShield;
	}
	
    public boolean canBeCollidedWith(){
        return true;
    }
	
	protected float getGravityVelocity(){
        return 0.0F;
    }

	@Override
    public void onUpdate(){
		super.onUpdate();
		
		if(ticksExisted>=400)
			setShieldBack();
		
		if(!worldObj.isRemote){
			currentTickTravel+=1;
			if(currentTickTravel>tickTravel)
				isCommingBack=true;
			if(isCommingBack){
				if(getThrower()!=null){	
					double xth = getThrower().posX;
					double yth = getThrower().posY+0.5;
					double zth = getThrower().posZ;
					curhyp = Math.sqrt( (posX-xth)*(posX-xth) + (posY-yth)*(posY-yth) + (posZ-zth)*(posZ-zth) );
			        setThrowableHeading(xth-posX,yth-posY,zth-posZ, 1.5F, 1.0F);
					if(curhyp<1.5){
						if(isEntityAlive())
							setShieldBack();
					}
				}
			}
		}
    }
	
	@Override
	protected void onImpact(RayTraceResult mop) {
		if(this.worldObj.isRemote)return;
		if(this.getThrower() == null)return;

		if(mop.typeOfHit==RayTraceResult.Type.ENTITY && mop.entityHit!=null){
			if(mop.entityHit==this.getThrower()){
				if(this.isEntityAlive())
					this.setShieldBack();
			}else if(mop.entityHit instanceof EntityLivingBase){
				if(shield != null)
					mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setProjectile(), ((ItemVibraniumShield)shield.getItem()).SHIELD_1.getDamageVsEntity());
				shield.hitEntity((EntityLivingBase)mop.entityHit, (EntityPlayer) this.getThrower());
				
			}else if(mop.entityHit != null){
				if(shield != null)
					mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setProjectile(), ((ItemVibraniumShield)shield.getItem()).SHIELD_1.getDamageVsEntity());
			}
			return;
		}else if(mop.typeOfHit==RayTraceResult.Type.BLOCK){
			this.isCommingBack = true;
			this.blockMop = true;
		}
	}
	
	public void setBounce(int side, RayTraceResult mop){
		this.angleYaw = this.rotationYaw;
		this.anglePitch= this.rotationPitch;
		if(side==0 || side==1)
			this.anglePitch = this.rotationPitch*-1;

		switch(side){
			 case 2:this.angleYaw=this.rotationYaw<0?(-180-this.rotationYaw):180-this.rotationYaw;
				 break;
			 case 3:this.angleYaw=this.rotationYaw<0?(-180-this.rotationYaw):180-this.rotationYaw;
			 	break;
			 case 4:this.angleYaw=this.rotationYaw*-1;
			 	break;
			 case 5:this.angleYaw=this.rotationYaw*-1;
			 	break;
			 default:
				 break;
		}
		this.prevRotationPitch = this.rotationPitch;
		this.rotationPitch = this.anglePitch;
		this.prevRotationYaw = this.rotationYaw;
		this.rotationYaw = this.angleYaw;
		
        float f = 0.4F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionY = (double)(-MathHelper.sin((this.rotationPitch + 0F) / 180.0F * (float)Math.PI) * f);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}
	
	public void setShieldBack(){
		if(shield==null){
			setDead();
			return;
		}
		shield.setItemDamage(getTypeShield());
		ItemNBTHelper.setBoolean(shield, "IsThrown", false);
		this.isCommingBack = false;
		this.setDead();
	}
		
	@Override
	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		return false;
	}

	@Override
	public void writeSpawnData(ByteBuf data) {
		data.writeInt(typeShield);
	}

	@Override
	public void readSpawnData(ByteBuf data) {
		typeShield = data.readInt();
	}
}
