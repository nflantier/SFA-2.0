package noelflantier.sfartifacts.common.tileentities.pillar;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.tileentities.ATileSFA;
import noelflantier.sfartifacts.common.tileentities.ITileMustHaveMaster;

public class TileBlockPillar  extends ATileSFA implements ITileMustHaveMaster{

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

	/*@Override
	public void addToWaila(List<String> list) {
		if(this.hasMaster()){
			list.add("Master at : "+this.master.x+", "+this.master.y+", "+this.master.z);
			TileMasterPillar m = this.getMasterTile();
			list.add("Pillar : "+m.namePillar);
			list.add("Material : "+PillarMaterials.getMaterialFromId(m.materialId));
			list.add("Energy : "+NumberFormat.getNumberInstance().format(m.getEnergyStored(ForgeDirection.UNKNOWN))+" RF / "+NumberFormat.getNumberInstance().format(m.getMaxEnergyStored(ForgeDirection.UNKNOWN))+" RF");
			if(m.getFluidTanks().get(0)!=null)
				list.add("Liquefied Asgardite : "+NumberFormat.getNumberInstance().format(m.getFluidTanks().get(0).getFluidAmount())+" MB / "+NumberFormat.getNumberInstance().format(m.getFluidTanks().get(0).getCapacity())+" MB");
		}
	}*/

}
