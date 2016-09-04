package noelflantier.sfartifacts.common.tileentities.pillar;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.tileentities.ISFAEnergyHandler;
import noelflantier.sfartifacts.common.tileentities.ISFAFluid;
import noelflantier.sfartifacts.compatibilities.IC2Handler;
import noelflantier.sfartifacts.compatibilities.InterMods;

public class TileInterfacePillar extends TileBlockPillar implements ISFAFluid,ISFAEnergyHandler{
	//ENERGY
    public List<EnumFacing> recieveSides = new ArrayList<EnumFacing>();
    public List<EnumFacing> extractSides = new ArrayList<EnumFacing>();
    
    //FLUID
	public Hashtable<Fluid, List<EnumFacing>> fluidAndSide =  new Hashtable<Fluid, List<EnumFacing>>();

	public TileInterfacePillar(){
		super();
    }
	
	@Override
	public void init(){
		super.init();
		this.fluidAndSide.put(ModFluids.fluidLiquefiedAsgardite, recieveSides);
	}
	
    @Override
    public void update() {
        super.update();
        if(this.worldObj.isRemote)
        	return;
        if(this.extractSides.isEmpty())
        	return;

		for(EnumFacing fd : this.extractSides){
    		int maxExtract = this.getEnergyStored(fd);
    		int maxAvailable = this.extractEnergy(fd, maxExtract, true);
    		double energyTransferred = 0;
			TileEntity tile = worldObj.getTileEntity(this.getPos().add(fd.getDirectionVec()));
			if(tile!=null && tile instanceof IEnergyReceiver){
				energyTransferred = ((IEnergyReceiver) tile).receiveEnergy(fd.getOpposite(), maxAvailable, false);
				this.extractEnergy(fd, (int)energyTransferred, false);
			}else if(tile!=null && InterMods.hasIc2 && IC2Handler.isEnergyStorage(tile)){
				energyTransferred = IC2Handler.injectEnergy(tile, IC2Handler.convertRFtoEU(maxAvailable,5), false);
    			this.extractEnergy(fd, IC2Handler.convertEUtoRF(IC2Handler.convertRFtoEU(maxAvailable,5)-energyTransferred), false);
			}
		}
    	
    }

	@Override
	public List<FluidTank> getFluidTanks() {
		return getMasterTile().getFluidTanks();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		for(EnumFacing direction : recieveSides)
			if(direction == from)
				return true;
		for(EnumFacing direction : extractSides)
			if(direction == from)
				return true;
		return false;
	}

	@Override
	public EnergyStorage getEnergyStorage() {
		return getMasterTile().getEnergyStorage();
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return getMasterTile().getEnergyStorage().getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return getMasterTile().getEnergyStorage().getMaxEnergyStored();
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {	
		return this.isRedStoneEnable || getMasterTile()==null ? 0 : getMasterTile().getEnergyStorage().extractEnergy(maxExtract, simulate);
	
	}
	
	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return this.isRedStoneEnable || getMasterTile()==null ? 0 : getMasterTile().getEnergyStorage().receiveEnergy(maxReceive, simulate);
	}

    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);		
        int[] dirext = tag.getIntArray("tabextract");
        for(int i = 0 ; i < dirext.length ; i++){
        	extractSides.add(EnumFacing.getFront(dirext[i]));
        }

        int[] dirrec = tag.getIntArray("tabrecieve");
        for(int i = 0 ; i < dirrec.length ; i++){
        	recieveSides.add(EnumFacing.getFront(dirrec[i]));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);

        if(!extractSides.isEmpty()){	        
	    	tag.setIntArray("tabextract", extractSides.stream().mapToInt((e)->e.getIndex()).toArray());
        }

        if(!recieveSides.isEmpty()){
	    	tag.setIntArray("tabrecieve", recieveSides.stream().mapToInt((e)->e.getIndex()).toArray());
        }
        return tag;
    }
	
    
    
    /* CAPABILITIES */
	
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing){
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
        	if(!fluidAndSide.get(ModFluids.fluidLiquefiedAsgardite).contains(facing))
                return super.getCapability(capability, facing);
            return (T) getFluidTanks().get(0);
        }
        return super.getCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    public <T> T getCapabilityNoFacing(Capability<T> capability, EnumFacing facing){
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return (T) getFluidTanks().get(0);
        }
        return super.getCapability(capability, facing);
    }

	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
	}
}
