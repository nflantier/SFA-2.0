package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import noelflantier.sfartifacts.common.container.slot.FluidsSlots;
import noelflantier.sfartifacts.common.tileentities.TileMightyFoundry;

public class ContainerMightyFoundry extends ContainerMachine{

	private int slotId = -1;
	
	public ContainerMightyFoundry(InventoryPlayer inventory,TileMightyFoundry tile){
		super(inventory,tile);
		
		for(int x = 0 ; x < 9 ; x++){
			this.addSlotToContainer(new Slot(inventory,x,8+18*x,176));
		}
		for(int x = 0 ; x < 9 ; x++)
			for(int y = 0 ; y < 3 ; y++)
				this.addSlotToContainer(new Slot(inventory,x+y*9+9,8+18*x,118+18*y));
		
		this.addSlotToContainer(new FluidsSlots(tile, nextId(),15,75,true,FluidRegistry.LAVA));

		this.addSlotToContainer(new MoldSlots(tile, nextId(),44,23));

		for(int i=0;i<4;i++)
			this.addSlotToContainer(new MightyFoundrySlots(tile, nextId(),98+18*i,23, false));
		
		this.addSlotToContainer(new MightyFoundrySlots(tile, nextId(),148,75, true));
	}

	private int nextId(){
		this.slotId++;
		return this.slotId;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tmachine.isUseableByPlayer(player);
	}

	private class MightyFoundrySlots extends Slot{
	
		public boolean isResult;
		
		public MightyFoundrySlots(IInventory inv, int id,int x, int y, boolean isr) {
			super(inv, id, x, y);
			this.isResult = isr;
		}
		
		@Override
	    public boolean isItemValid(ItemStack stack)
	    {
	        return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
	    }    
	
		@Override
		public int getSlotStackLimit()
	    {
	        return 64;
	    }
	}
	
	private class MoldSlots extends Slot{
		
		
		public MoldSlots(IInventory inv, int id,int x, int y) {
			super(inv, id, x, y);
		}
		
		@Override
	    public boolean isItemValid(ItemStack stack)
	    {
	        return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
	    }    
	
		@Override
		public int getSlotStackLimit()
	    {
	        return 1;
	    }
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
		Slot slot = getSlot(index);
		if(slot instanceof MoldSlots && ((TileMightyFoundry)this.tmachine).isLocked)
			return null;
			
		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();
			if (index >= 36){ //To player
				if (!mergeItemStack(stack, 0, 36, false)){
					return null;
				}
			}else{
				boolean success = false;
				for(int i = 0 ; i <= this.slotId ; i++){
					if(this.tmachine.getStackInSlot(i)==null){
						if(!this.tmachine.isItemValidForSlot(i, stack) || !mergeItemStack(stack, 36+i, 37+i, false))
							success = false;
						else{
							success = true;
							break;
						}
					}else if(this.tmachine.getStackInSlot(i).getItem()==slot.getStack().getItem()){
						if(!mergeItemStack(stack, 36+i, 37+i, false))
							success = false;
						else{
							success = true;
							break;
						}
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

	/*@Override
    protected boolean mergeItemStack(ItemStack stack, int p_75135_2_, int p_75135_3_, boolean p_75135_4_)
    {
        boolean flag1 = false;
        int k = p_75135_2_;

        if (p_75135_4_)
        {
            k = p_75135_3_ - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (stack.isStackable())
        {
            while (stack.stackSize > 0 && (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_))
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 != null && itemstack1.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, itemstack1))
                {
                    int l = itemstack1.stackSize + stack.stackSize;

                    if (l <= stack.getMaxStackSize() && l<=slot.getSlotStackLimit())
                    {
                    	stack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < stack.getMaxStackSize() && itemstack1.stackSize< slot.getSlotStackLimit())
                    {
                    	int m = stack.getMaxStackSize() - itemstack1.stackSize;
                    	if(m>slot.getSlotStackLimit())
                    		m = slot.getSlotStackLimit() - itemstack1.stackSize;
                    	int d = Math.min(m, stack.stackSize);
                    	stack.stackSize -= d;
                        itemstack1.stackSize += d;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (p_75135_4_)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        if (stack.stackSize > 0)
        {
            if (p_75135_4_)
            {
                k = p_75135_3_ - 1;
            }
            else
            {
                k = p_75135_2_;
            }

            while (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_)
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 == null)
                {	
                	ItemStack t = stack.copy();
                	t.stackSize = Math.min(t.stackSize, slot.getSlotStackLimit());
                    slot.putStack(t.copy());
                    slot.onSlotChanged();
                    stack.stackSize -= t.stackSize;
                    flag1 = true;
                    break;
                }

                if (p_75135_4_)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return flag1;
    }*/
}
