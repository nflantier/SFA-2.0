package noelflantier.sfartifacts.common.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.recipes.IUseSFARecipes;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeHammerUpgrades;
import noelflantier.sfartifacts.common.recipes.RecipeOnHammerStand;
import noelflantier.sfartifacts.common.recipes.handler.HammerUpgradesRecipesHandler;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;

public class TileHammerStand extends ATileSFA implements IInventory, ITileCanHavePillar, IUseSFARecipes{
	
	public BlockPos master;

	//ENERGY
	public int tmpRF;
	public int previousTmpRf;
	
	//INVOKING
	public boolean hasInvoked;
	public boolean isInvoking = false;
	
	//UPGRADES
	public RecipeOnHammerStand curentRecipe;
	public int soundTick = 50;
		
	//SLOTS
	public ItemStack[] items = new ItemStack[1];
	
	public TileHammerStand(){
		super();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
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
	public int getSizeInventory() {
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return items[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack it = getStackInSlot(index);
		if(it!=null){
			if(it.stackSize < count)
				setInventorySlotContents(index, null);
			else{
				it = it.splitStack(count);
				markDirty();
			}
		}
		return it;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack s = this.getStackInSlot(index);
		setInventorySlotContents(index, null);
		return s;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if(stack!=null && stack.stackSize > getInventoryStackLimit())
			stack.stackSize = getInventoryStackLimit();
		items[index] = stack;
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return stack.getItem() instanceof ItemThorHammer;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
        if(!hasMaster())
        	return;
        TileMasterPillar t = this.getMasterTile();
		if(t!=null){
			if(this.curentRecipe!=null){
				if(!this.curentRecipe.itemStillHere())
					this.curentRecipe=null;
			}
				        
	        if(this.items[0] != null && this.items[0].getItem() instanceof ItemThorHammer){
	        	this.items[0].setItemDamage(0);
	        	if(ItemNBTHelper.getBoolean(this.items[0], "IsThrown", true))
	        		ItemNBTHelper.setBoolean(this.items[0], "IsThrown", false);//Set the hammer back to reinit if it stuck to thrown
	        	if(ItemNBTHelper.getBoolean(this.items[0], "IsMoving", true))
					ItemNBTHelper.setBoolean(this.items[0], "IsMoving", false);//Set the hammer back to reinit if it stuck to moving
	        	
	        	ItemThorHammer it = (ItemThorHammer) this.items[0].getItem();
	        	int en = it.getEnergyStored(this.items[0]);
	        	int cap = it.getCapacity(this.items[0]);
	        	if(en<cap){
                	if(t.getEnergyStored(null)>0){
	                	int maxAvailable = t.extractEnergy(null, 100000, true);
	            		int energyTransferred = 0;
	        			energyTransferred = it.receiveEnergy(this.items[0], maxAvailable, false);
	            		t.extractEnergyWireless(energyTransferred, false, this.getPos());
                	}
	        	}
	        }
	        
	        this.tmpRF = t.getEnergyStored(null);
	        
	        if(this.previousTmpRf!=this.tmpRF)
	        	this.previousTmpRf = this.tmpRF;
        }
	}

	public boolean isPillarHasEnoughEnergy(){
    	return getMasterTile()!=null && getMasterTile().getEnergyStored(null)>=ModConfig.rfNeededThorHammer;
    }

    @Override
    public void setMaster(BlockPos pos){
    	master = pos;
    }
    
	@Override
	public BlockPos getMasterPos() {
		return master;
	}

	@Override
	public String getUsageName() {
		return HammerUpgradesRecipesHandler.USAGE_HAMMER_UPGRADES;
	}

	@Override
	public int getEnergy() {
		return 0;
	}

	@Override
	public int getFluid() {
		return 0;
	}

	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeHammerUpgrades.class;
	}

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("hasInvoked", hasInvoked);
        nbt.setBoolean("isInvoking", isInvoking);

        writeMasterToNBT(nbt);
        
        NBTTagCompound[] tag = new NBTTagCompound[items.length];
		for (int i = 0; i < items.length; i++)
		{
			tag[i] = new NBTTagCompound();
			if (this.items[i] != null)
				tag[i] = items[i].writeToNBT(tag[i]);
			nbt.setTag("Items" + i, tag[i]);
		}
		return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        hasInvoked = nbt.getBoolean("hasInvoked");
        isInvoking = nbt.getBoolean("isInvoking");

        master = readMasterFromNBT(nbt);
    
		NBTTagCompound[] tag = new NBTTagCompound[this.items.length];
		for (int i = 0; i < this.items.length; i++)
		{
			tag[i] = nbt.getCompoundTag("Items" + i);
			this.items[i] = ItemStack.loadItemStackFromNBT(tag[i]);
		}
    }

	public World getWorldForMaster() {
		return getWorld();
	}
}
