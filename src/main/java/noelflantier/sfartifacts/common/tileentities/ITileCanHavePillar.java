package noelflantier.sfartifacts.common.tileentities;

import net.minecraft.util.math.BlockPos;

public interface ITileCanHavePillar extends ITileCanBeSidedToPillar{
	public void setMaster(BlockPos pos);
}
