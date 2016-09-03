package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.items.baseclasses.ItemInventoryMold;

public class ContainerMoldMaking extends Container{

	private int slotId = -1;
	public int currentSlot;
	public IInventory itemInv;
	
	public ContainerMoldMaking(EntityPlayer player){
		this.currentSlot = player.inventory.currentItem;
		this.itemInv = new ItemInventoryMold(player);
		for(int x = 0 ; x < 9 ; x++){
			if(x!=currentSlot)
				this.addSlotToContainer(new Slot(player.inventory,x,8+18*x,176));
		}
		for(int y = 0 ; y < 9 ; y++)
			for(int x = 0 ; x < 9 ; x++)
				this.addSlotToContainer(new MoldMakingSlots(itemInv,nextId(),8+18*x,8+18*y));
		
	}

	private int nextId(){
		this.slotId++;
		return this.slotId;
	}
	
	private class MoldMakingSlots extends Slot{
		
		public MoldMakingSlots(IInventory inv, int id,int x, int y) {
			super(inv, id, x, y);
		}
		
		@Override
	    public boolean isItemValid(ItemStack stack)
	    {
	        return stack.getItem()==Item.getItemFromBlock(Blocks.SAND);
	    }    
	
		@Override
		public int getSlotStackLimit()
	    {
	        return 1;
	    }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
    public void detectAndSendChanges(){
    	super.detectAndSendChanges();
    	//if(!this.getSlot(currentSlot).getHasStack())
    }
    
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
		Slot slot = getSlot(index);
		if(!this.getSlot(currentSlot).getHasStack())
			return null;
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();
			if (index >= 9){ //To player
				if (!mergeItemStack(stack, 0, 9, false)){
					return null;
				}
			}else{
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

}
