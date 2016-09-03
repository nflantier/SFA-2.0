package noelflantier.sfartifacts.common.tileentities;

import net.minecraft.util.math.BlockPos;

public interface ITileWirelessEnergy {
	int receiveEnergyWireless(int maxReceive, boolean simulate, BlockPos pos);
	int extractEnergyWireless(int maxExtract, boolean simulate, BlockPos pos);
}
