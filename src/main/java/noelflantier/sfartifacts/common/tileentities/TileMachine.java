package noelflantier.sfartifacts.common.tileentities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.compatibilities.IC2Handler;
import noelflantier.sfartifacts.compatibilities.InterMods;

public abstract class TileMachine extends ATileSFA implements ISFAFluid,ISFAEnergyHandler,ISidedInventory{
	
	//CONTROL
	public boolean isManualyEnable = true;
	public boolean customCH = false;
	public boolean hasRF = false;
	public boolean hasFL = false;
	
	//ENERGY
    public EnergyStorage storage = new EnergyStorage(0,0,0);
    public List<EnumFacing> receiveSides = new ArrayList<>();
    public List<EnumFacing> extractSides = new ArrayList<>();
	public int lastEnergyStoredAmount = -1;
	
	//FLUID
   	public FluidTank tank = new FluidTank(0);
   	public Hashtable<Fluid, List<EnumFacing>> fluidAndSide = new Hashtable<Fluid, List<EnumFacing>>();
	public List<EnumFacing> fluidConnections = new ArrayList<EnumFacing>();
	
	//MACHINE
	public Random randomMachine = new Random();
	
	public TileMachine(){
		super();
	}
	
	@Override
	public void update(){
		super.update();
		
		if(processGlobalUpdate())
			return;
		
        if(worldObj.isRemote){
            if(!isRedStoneEnable)
            	processClientUpdate();
            if(isManualyEnable && !isRedStoneEnable)
            	processClientMachine();
        	return;
        }
        if(!isRedStoneEnable)
        	processUpdate();
        if(isManualyEnable && !isRedStoneEnable)
        	processMachine();
        if(randomMachine.nextFloat()<getRandomTickChance())
        	processAtRandomTicks();

        processPackets();	
        if(this.storage.getEnergyStored() != this.lastEnergyStoredAmount)
			PacketHandler.sendToAllAround(new PacketEnergy(this.getPos(), this.getEnergyStored(null), this.lastEnergyStoredAmount),this);
	
		this.lastEnergyStoredAmount = this.getEnergyStored(null);
    }
	
	public void extractEnergyToSides(boolean rf, boolean eu){
		if(this.extractSides.isEmpty() || this.getEnergyStored(null)<=0)
			return;
		for(EnumFacing fd : this.extractSides){
    		int maxAvailable = this.extractEnergy(fd, this.getEnergyStored(fd), true);
    		double energyTransferred = 0;
			TileEntity tile = worldObj.getTileEntity(this.getPos().offset(fd));
			
			if(rf && tile!=null && tile instanceof IEnergyReceiver){
				energyTransferred = ((IEnergyReceiver) tile).receiveEnergy(fd.getOpposite(), maxAvailable, false);
				this.extractEnergy(fd, (int)energyTransferred, false);
			}else if(eu && tile!=null && InterMods.hasIc2 && IC2Handler.isEnergyStorage(tile) ){
				energyTransferred = IC2Handler.injectEnergy(tile, IC2Handler.convertRFtoEU(maxAvailable,5), false);
    			this.extractEnergy(fd, IC2Handler.convertEUtoRF(IC2Handler.convertRFtoEU(maxAvailable,5)-energyTransferred), false);
			}
		}
	}
	
    public float getRandomTickChance(){
    	return 0F;
    }
    public void processAtRandomTicks(){}
	public abstract void processPackets();
	public abstract void processMachine();

	public boolean processGlobalUpdate(){return false;}
	public void processUpdate(){}
    @SideOnly(Side.CLIENT)
	public void processClientUpdate(){}
    @SideOnly(Side.CLIENT)
	public void processClientMachine(){}

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        
        //FLUID
        tank.writeToNBT(nbt);
        
        //INVENTORY
        NBTTagCompound[] tag = new NBTTagCompound[this.getItems().length];
		for (int i = 0; i < this.getItems().length; i++)
		{
			tag[i] = new NBTTagCompound();
			if (this.getItems()[i] != null)
				tag[i] = this.getItems()[i].writeToNBT(tag[i]);
			nbt.setTag("Items" + i, tag[i]);
		}

		//ENERGY
		storage.writeToNBT(nbt);
        nbt.setInteger("lastEnergyStoredAmount", this.lastEnergyStoredAmount);
    	
    	//Control
    	nbt.setBoolean("manualyEnabled", this.isManualyEnable);
    	
    	
    	//HACK FOR THE FLUIDSTACK WHEN BLOCK SAVES NBT ON DROPPED
        nbt.removeTag("Empty");
        
    	return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        
    	//FLUID
        tank.readFromNBT(nbt);
        
		//INVENTORY
		NBTTagCompound[] tag = new NBTTagCompound[this.getItems().length];
		for (int i = 0; i < this.getItems().length; i++)
		{
			tag[i] = nbt.getCompoundTag("Items" + i);
			this.getItems()[i] = ItemStack.loadItemStackFromNBT(tag[i]);
		}
        
		//ENERGY
		storage.readFromNBT(nbt);
		this.lastEnergyStoredAmount = nbt.getInteger("lastEnergyStoredAmount");
		        
        //Control
        this.isManualyEnable = nbt.getBoolean("manualyEnabled");
    }

	@Override
	public EnergyStorage getEnergyStorage() {
		return storage;
	}

	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		lastEnergyStoredAmount = lastEnergyStored;
	}
	
    public abstract ItemStack[] getItems();
    
	@Override
	public List<FluidTank> getFluidTanks() {
		return new ArrayList<FluidTank>(){{add(tank);}};
	}
	
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing){
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getCapabilityNoFacing(Capability<T> capability, EnumFacing facing){
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasFL){
            return (T) getFluidTanks().get(0);
        }
        return super.getCapability(capability, facing);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
    	if(customCH)
            return super.getCapability(capability, facing);
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasFL){
        	if(!fluidConnections.contains(facing))
                return super.getCapability(capability, facing);
            return (T) getFluidTanks().get(0);
        }
        return super.getCapability(capability, facing);
    }
	
	@Override
	public boolean canConnectEnergy(EnumFacing facing) {
		for(EnumFacing direction : receiveSides)
			if(direction == facing)
				return true;
		for(EnumFacing direction : extractSides)
			if(direction == facing)
				return true;
		return false;
	}
	
	@Override
	public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate) {
		return this.isRedStoneEnable?0:this.storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy(EnumFacing facing, int maxExtract, boolean simulate) {
		return this.isRedStoneEnable?0:this.storage.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int getEnergyStored(EnumFacing facing) {
		return this.storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing facing) {
		return this.storage.getMaxEnergyStored();
	}
	
	@Override
	public int getSizeInventory() {
		return this.getItems().length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.getItems()[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int i) {
		ItemStack it = this.getStackInSlot(slot);
		if(it!=null){
			if(it.stackSize < i)
				this.setInventorySlotContents(slot, null);
			else{
				int s = it.stackSize;
				it = it.splitStack(i);
				if(s-i<=0)
					this.setInventorySlotContents(slot, null);
				this.markDirty();
			}
		}
		return it;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if(stack!=null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		this.getItems()[slot] = stack;
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}
	
}
