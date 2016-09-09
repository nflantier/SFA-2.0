package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.container.slot.FluidContainerSlot;
import noelflantier.sfartifacts.common.tileentities.TileMrFusion;

public class ContainerMrFusion extends ContainerMachine{

	private int slotId = -1;
	
	public ContainerMrFusion(InventoryPlayer inventory, TileMrFusion tile) {
		super(inventory, tile);
		for(int x = 0 ; x < 9 ; x++){
			this.addSlotToContainer(new Slot(inventory,x,36+18*x,216));
		}
		
		
		for(int x = 0 ; x < 9 ; x++)
			for(int y = 0 ; y < 3 ; y++)
				this.addSlotToContainer(new Slot(inventory,x+y*9+9,36+18*x,158+18*y));
		
		this.addSlotToContainer(new FluidContainerSlot(tile,nextId(),179,124));

		for(int x = 0 ; x < 9 ; x++)
			for(int y = 0 ; y < 7 ; y++)
				this.addSlotToContainer(new Slot(tile,nextId(),9+18*x,16+18*y));
		
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

                    if (l <= stack.getMaxStackSize())
                    {
                    	stack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < stack.getMaxStackSize())
                    {
                    	stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = stack.getMaxStackSize();
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
