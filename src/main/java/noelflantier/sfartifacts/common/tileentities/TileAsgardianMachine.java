package noelflantier.sfartifacts.common.tileentities;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import noelflantier.sfartifacts.common.handlers.ModConfig;

public abstract class TileAsgardianMachine extends TileMachine implements ITileCanHavePillar, ITileCanTakeRFonlyFromPillars{
	
	//STRUCTURE
	public BlockPos master;

	public TileAsgardianMachine(){
		super();
	}
	
	@Override
	public void update(){
		super.update();
    }

	@Override
    public void setMaster(BlockPos pos){
    	master = pos;
    }

	@Override
	public void init(){
		super.init();
	}
	
	@Override
	public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate) {	
		if(ModConfig.isAMachinesWorksOnlyWithPillar){
			return 0;
		}
		return isRedStoneEnable ? 0 : storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy(EnumFacing facing, int maxExtract, boolean simulate) {	
		if(ModConfig.isAMachinesWorksOnlyWithPillar){
			return 0;
		}	
		return isRedStoneEnable ? 0 : storage.extractEnergy(maxExtract, simulate);
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
        //STRUCTURE
        master = readMasterFromNBT(nbt);
    }

	@Override
	public BlockPos getMasterPos() {
		return master;
	}
	
	@Override
	public int receiveOnlyFromPillars(int maxReceive, boolean simulate) {
		return this.isRedStoneEnable ? 0 : storage.receiveEnergy(maxReceive, simulate);
	}
	@Override
	public int extractOnlyFromPillars(int maxExtract, boolean simulate) {
		return isRedStoneEnable ? 0 : storage.extractEnergy(maxExtract, simulate);
	}

}
