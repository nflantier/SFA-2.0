package noelflantier.sfartifacts.common.entities.ai;

import java.util.Random;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIJumpAndCollide extends EntityAIBase{
	
    EntityCreature leaper;
    int attackTick;
    
    /** The entity that the leaper is leaping towards. */
    EntityLivingBase leapTarget;
    private float leapMotionY;

    private int failedPathFindingPenalty;

    public EntityAIJumpAndCollide(EntityCreature leaper, float leapMotionY)
    {
        this.leaper = leaper;
        this.leapMotionY = leapMotionY;
        this.setMutexBits(3);
    }

	@Override
	public boolean shouldExecute() {
        leapTarget = this.leaper.getAttackTarget();

        if (leapTarget == null)
        {
            return false;
        }
        else if (!leapTarget.isEntityAlive())
        {
            return false;
        }
        else
        {
        	Path entityPathEntity = this.leaper.getNavigator().getPathToEntityLiving(leapTarget);
            return !this.leaper.onGround ? false : entityPathEntity == null ? false : this.leaper.getRNG().nextInt(5) == 0;
        }
    }
	
	public boolean continueExecuting(){
		return !this.leaper.onGround;
	}
	
    public void startExecuting(){
    	attackerJumpToEntity(leapTarget);
    }
        
    public void updateTask(){    	
        double d0 = this.leaper.getDistanceSqToEntity(leapTarget);
        double d1 = (double)(this.leaper.width * 2.0F * this.leaper.width * 2.0F + leapTarget.width);
        this.attackTick = Math.max(this.attackTick - 1, 0);
        if (d0 <= d1 && this.attackTick <= 20)
        {
            this.attackTick = 20;

            if (this.leaper.getHeldItemMainhand() != null)
            {
                this.leaper.swingArm(EnumHand.MAIN_HAND);
            }
            this.leaper.attackEntityAsMob(leapTarget);
        }
    }
    
    public void attackerJumpToEntity(EntityLivingBase entity){
    	if(!this.leaper.onGround)
    		return;
    	setAttackerPointing();
    }    
    
    public void setAttackerPointing(){
        double d0 = this.leapTarget.posX - this.leaper.posX;
        double d1 = this.leapTarget.posZ - this.leaper.posZ;
        float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        this.leaper.motionX += d0 / (double)f * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D * 25.2D;
        this.leaper.motionZ += d1 / (double)f * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D * 25.2D;
        this.leaper.motionY += (double)this.leapMotionY;
        
        float f2 = MathHelper.sqrt_double(leapTarget.posX * leapTarget.posX + leapTarget.posY * leapTarget.posY + leapTarget.posZ * leapTarget.posZ);
        double x = leapTarget.posX / (double)f2;
        double y = leapTarget.posY / (double)f2;
        double z = leapTarget.posZ / (double)f2;
        float f1 = MathHelper.sqrt_double(x * x + z * z);
        this.leaper.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        this.leaper.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        this.leaper.prevRotationYaw = this.leaper.rotationYaw;
        this.leaper.prevRotationPitch = this.leaper.rotationPitch;
    }
}
