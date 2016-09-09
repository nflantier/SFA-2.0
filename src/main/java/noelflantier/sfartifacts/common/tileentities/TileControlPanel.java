package noelflantier.sfartifacts.common.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.blocks.ABlockSFAContainer;
import noelflantier.sfartifacts.common.blocks.BlockControlPanel;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;

public class TileControlPanel extends ATileSFA implements ITileCanBeSidedToPillar{

	BlockPos master;
	
	public TileControlPanel(){
		super();
    }

	@Override
	public BlockPos getMasterPos() {
		return master;
	}

	public void checkMaster(){
		BlockPos sided = getPos().add(getWorld().getBlockState(getPos()).getValue(ABlockSFAContainer.H_FACING).getOpposite().getDirectionVec());
		TileMasterPillar t = isSidedToPillar(sided);
		if(t!=null)
			master = t.getPos();
		else
			master = null;
		getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockControlPanel.CONNECTED, master!=null));
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
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

	public World getWorldForMaster() {
		return getWorld();
	}
}
