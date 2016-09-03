package noelflantier.sfartifacts.common.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class FluidContainerSlot  extends Slot{
	
	public FluidContainerSlot(IInventory inv, int id,int x, int y) {
		super(inv, id, x, y);
	}
	
	@Override
    public boolean isItemValid(ItemStack stack)
    {
        return FluidUtil.getFluidHandler(stack) != null;
    }     

	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
}