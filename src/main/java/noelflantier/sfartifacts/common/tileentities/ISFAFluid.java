package noelflantier.sfartifacts.common.tileentities;

import java.util.List;

import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface ISFAFluid{
	
	List<FluidTank> getFluidTanks();
}
