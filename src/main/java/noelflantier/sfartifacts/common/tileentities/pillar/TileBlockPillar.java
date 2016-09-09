package noelflantier.sfartifacts.common.tileentities.pillar;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.tileentities.ATileSFA;
import noelflantier.sfartifacts.common.tileentities.ITileMustHaveMaster;

public class TileBlockPillar extends ATileSFA implements ITileMustHaveMaster{

	public BlockPos master;
	public int isRenderingPillarModel = -1;
    public int updateRenderTick = -1;
    
	public TileBlockPillar(){
		super();
    }

	@Override
	public void update() {
        super.update();
	}

	@Override
	public BlockPos getMasterPos() {
		return master;
	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
        writeMasterToNBT(nbt);
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        master = readMasterFromNBT(nbt);
    }

	@Override
	public World getWorldForMaster() {
		return getWorld();
	}

}
