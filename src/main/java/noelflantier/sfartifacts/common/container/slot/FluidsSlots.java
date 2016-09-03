package noelflantier.sfartifacts.common.container.slot;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.items.ItemAsgardite;

public class FluidsSlots extends Slot{

	public boolean filled;
	public Fluid theFluid;
	
	public FluidsSlots(IInventory inv, int id,int x, int y, boolean filled, Fluid fluid) {
		super(inv, id, x, y);
		this.filled = filled;
		this.theFluid = fluid;
	}
	
	@Override
    public boolean isItemValid(ItemStack stack)
    {
		IFluidHandler fhandler = FluidUtil.getFluidHandler(stack);
		if(fhandler!=null){
			FluidStack drainfilled = fhandler.drain(new FluidStack(theFluid, Ressources.FLUID_MAX_TRANSFER), false);
			int amountfilled = fhandler.fill(new FluidStack(theFluid, Ressources.FLUID_MAX_TRANSFER), false);
			return ( filled && drainfilled != null && drainfilled.amount > 0 ) || ( !filled && amountfilled > 0 );
		}
		return false;
    }     

	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
}