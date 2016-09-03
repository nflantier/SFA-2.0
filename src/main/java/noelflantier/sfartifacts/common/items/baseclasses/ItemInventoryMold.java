package noelflantier.sfartifacts.common.items.baseclasses;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.recipes.RecipeMold;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;

public class ItemInventoryMold implements IInventory{
	
	public ItemStack[] items = new ItemStack[81];
	public EntityPlayer player;
	
	public ItemInventoryMold(EntityPlayer player){
		this.player = player;
		this.setInv();
	}

	public ItemInventoryMold(EntityPlayer player, int id){
		this(player);
	}
	public void setInv(){
		if(this.player.getHeldItemMainhand()==null)
			return;
		int[] inv = ItemNBTHelper.getIntegerArray(this.player.getHeldItemMainhand(), "moldstructure", new int[]{});
		for(int i=0;i<inv.length;i++){
			String bin = Integer.toBinaryString(inv[i]);
			int l = 9-bin.length();
			for(int j=0;j<l;j++)
				bin = "0"+bin;
			for(int k=0;k<bin.length();k++){
				if(bin.substring(k, k+1).equals("1")){
					this.setInventorySlotContentsN(k+9*i, new ItemStack(Blocks.SAND,1,0));
				}
			}
		}
	}
	
	public void setInventorySlotContentsN(int slot, ItemStack stack) {
		if(stack!=null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		this.items[slot] = stack;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString("Mold");
	}

	@Override
	public int getSizeInventory() {
		return this.items.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack it = this.getStackInSlot(index);
		if(it!=null){
			if(it.stackSize < count)
				this.setInventorySlotContents(index, null);
			else{
				it = it.splitStack(count);
				this.markDirty();
			}
		}
		return it;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if(stack!=null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		this.items[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		int[] tab = new int[9];
		for(int i =0;i<9;i++){
			String bin="";
			for(int j =0;j<9;j++){
				if(this.getStackInSlot(j+9*i)!=null)
					bin=bin+"1";
				else
					bin=bin+"0";
			}
			tab[i]=Integer.parseInt(bin, 2);
		}
		RecipeMold rm = RecipesRegistry.instance.getMoldWithShap(tab);
		if(rm!=null){
			this.player.getHeldItemMainhand().setItemDamage(rm.getMoldMeta());
			ItemNBTHelper.setInteger(this.player.getHeldItemMainhand(), "idmold", rm.getMoldMeta());
			ItemNBTHelper.setIntegerArray(this.player.getHeldItemMainhand(), "moldstructure", rm.getTabShape());
		}else{
			this.player.getHeldItemMainhand().setItemDamage(0);
			ItemNBTHelper.setInteger(this.player.getHeldItemMainhand(), "idmold", -1);
			ItemNBTHelper.setIntegerArray(this.player.getHeldItemMainhand(), "moldstructure", tab);
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return stack.getItem()==Item.getItemFromBlock(Blocks.SAND);
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}
