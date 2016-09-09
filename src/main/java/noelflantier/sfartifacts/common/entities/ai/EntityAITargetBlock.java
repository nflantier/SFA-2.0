package noelflantier.sfartifacts.common.entities.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;

public class EntityAITargetBlock extends EntityAITargetSFA{
	
    private final int targetChance;
    private int xTarget;
    private int yTarget;
    private int zTarget;


    public EntityAITargetBlock(EntityLiving entity, int chance, boolean sight, boolean nearby, int x, int y, int z)
    {
        super(entity, sight, nearby);
        this.targetChance = chance;
        this.xTarget = x;
        this.yTarget = y;
        this.zTarget = z;
        this.setMutexBits(1);
    }
    
    public EntityAITargetBlock(EntityLiving entity, int chance, boolean sight, boolean nearby, BlockPos pos)
    {
        this(entity,chance,sight,nearby,pos.getX(),pos.getY(),pos.getZ());
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
        {
            return false;
        }
        else
        {
        	return true;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    	this.taskOwner.getNavigator().tryMoveToXYZ(this.xTarget, this.yTarget, this.zTarget, 1.0D);
        super.startExecuting();
    }
}
