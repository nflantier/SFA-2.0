package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.tileentities.ATileSFA;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;

public class ContainerHammerStandInvoked extends Container {

	private int slotId = -1;
	private TileHammerStand tile;
	
	public ContainerHammerStandInvoked(InventoryPlayer inventory,TileHammerStand tile) {
		
		this.tile = tile;
		
		for(int x = 0 ; x < 9 ; x++){
			this.addSlotToContainer(new Slot(inventory,x,31+18*x,156));
		}
		/*for(int x = 0 ; x < 9 ; x++)
			for(int y = 0 ; y < 3 ; y++)
				this.addSlotToContainer(new Slot(inventory,x+y*9+9,31+18*x,98+18*y));
		*/
		this.addSlotToContainer(new HammerStandSlots(tile, nextId(),6,21));//REAL ID 36

	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile != null && player != null? ((ATileSFA)tile).isUseableByPlayer(player) : false;
	}
	
	private int nextId(){
		this.slotId++;
		return this.slotId;
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
		Slot slot = getSlot(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();

			if (index >= 9){ //To player
				if (!mergeItemStack(stack, 0, 9, false)){
					return null;
				}
			}else {
				boolean success = false;
				if(tile.getStackInSlot(0)==null){
					if(!tile.isItemValidForSlot(0, stack) || !mergeItemStack(stack, 9, 10, false))
						success = false;
					else{
						success = true;
					}
				}
				if(!success)
					return null;
				
			}

			if (stack.stackSize == 0) {
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}

			slot.onPickupFromSlot(player, stack);

			return result;
		}

		return null;
    }
	private class HammerStandSlots extends Slot {
		public HammerStandSlots(IInventory inv, int id,int x, int y) {
			super(inv, id, x, y);
		}

		@Override
	    public boolean isItemValid(ItemStack stack)
	    {
	        return stack.getItem() instanceof ItemThorHammer;
	    }    

		@Override
		public int getSlotStackLimit()
	    {
	        return 1;
	    }
    
	}
}

