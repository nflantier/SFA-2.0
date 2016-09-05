package noelflantier.sfartifacts.common.tileentities.pillar;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketParticleMoving;
import noelflantier.sfartifacts.common.network.messages.PacketPillar;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;
import noelflantier.sfartifacts.common.tileentities.ITileCanBeMaster;
import noelflantier.sfartifacts.common.tileentities.ITileCanHavePillar;
import noelflantier.sfartifacts.common.tileentities.ITileCanTakeRFonlyFromPillars;
import noelflantier.sfartifacts.common.tileentities.ITileWirelessEnergy;
import noelflantier.sfartifacts.compatibilities.IC2Handler;
import noelflantier.sfartifacts.compatibilities.InterMods;

public class TileMasterPillar extends TileInterfacePillar implements ITileCanBeMaster,ITileWirelessEnergy{

	//STRUCTURE & MATERIAL
    public String structure = PillarsConfig.getInstance().nameOrderedBySize.get(PillarsConfig.getInstance().nameOrderedBySize.size()-1);
    public EnumPillarMaterial material = EnumPillarMaterial.ASGARDITE;
    public Random random = new Random();

	//ENERGY
	public boolean isTransferCaped = false;
    public int energyCapacity = 0;
    public EnergyStorage storage = new EnergyStorage(0,0,0);
    public int ratioTransfer = 100;
	public int lastEnergyStoredAmount = -1;
    public boolean onlySender = false;
	public int passiveEnergy = 0;
	public int fluidEnergy = 0;
	public int amountToExtract = 0;

	//ENERGY CHILD
    public int[] energyChildInit;
    public List<BlockPos> energyChild = new ArrayList<>();
    	
    //FLUID
   	public FluidTank tank = new FluidTank(new FluidStack(ModFluids.fluidLiquefiedAsgardite, 0),0);
	public Hashtable<Fluid, List<Integer>> fluidAndSide;
    
    public TileMasterPillar(){
    	super();
    }
    
	@Override
	public void update() {
        super.update();
        if(worldObj.isRemote)
        	return;

        if(!hasMaster() || !init)
        	return;
        
    	if(!energyChild.isEmpty()){
    		energyChild.removeIf((d)->worldObj.getTileEntity(d)==null 
    				||  ( worldObj.getTileEntity(d) instanceof IEnergyReceiver == false  
    	    		&&   worldObj.getTileEntity(d) instanceof ITileCanHavePillar == false 
    				&& ( InterMods.hasIc2 && IC2Handler.isEnergyStorage(worldObj.getTileEntity(d))== false) ) );
        	if(!this.energyChild.isEmpty()){
        		for(BlockPos p : this.energyChild){
        			TileEntity te = worldObj.getTileEntity(p);
        			int maxAc = extractEnergy(null, getEnergyStored(null)/energyChild.size(), true);

        			if(te!=null && te instanceof IEnergyReceiver && ((IEnergyReceiver)te).getEnergyStored(null)<((IEnergyReceiver)te).getMaxEnergyStored(null)){
        				for(EnumFacing fd : EnumFacing.values()){
	        				int energyTc = 0;
	        				if(ModConfig.isAMachinesWorksOnlyWithPillar && te instanceof ITileCanTakeRFonlyFromPillars){
	    						ITileCanTakeRFonlyFromPillars iop = (ITileCanTakeRFonlyFromPillars)te;
	    						energyTc = iop.receiveOnlyFromPillars( maxAc, true);
	    						if(energyTc!=0){
	    							energyTc = iop.receiveOnlyFromPillars(maxAc, false);
		            				extractEnergyWireless(energyTc, false, te.getPos());
		        				}
	    					}else{
			                	energyTc = ((IEnergyReceiver) te).receiveEnergy(fd.getOpposite(), maxAc, false);
		            			extractEnergyWireless(energyTc, false, te.getPos());
	    					}
	        				if(energyTc>0)
	            				break;
	    				}
        			}else if(te!=null && InterMods.hasIc2 && IC2Handler.isEnergyStorage(te) ){
        				double energyTc = IC2Handler.injectEnergy(te, IC2Handler.convertRFtoEU(maxAc,5), false);
        				this.extractEnergyWireless(IC2Handler.convertEUtoRF(IC2Handler.convertRFtoEU(maxAc,5)-energyTc), false, te.getPos());
        			}
        		}
        	}
    	}
    	
    	float structureratio =  (PillarsConfig.getInstance().getPillarFromName(structure)!=null) ? PillarsConfig.getInstance().getPillarFromName(structure).naturalRatio : 1;
    	float ratioHeight = (getPos().getY()<material.heightRatio)?(float)getPos().getY()/(float)material.heightRatio:1;
    	float ratioRaining = (worldObj.isRaining() && worldObj.getBiomeGenForCoords(getPos()).getTemperature()<2)?material.rainRatio:0;
    	passiveEnergy = (int)( Math.pow( structureratio ,2.2 ) * Math.pow( material.energyRatio + Math.pow( ratioHeight * 2 ,4 ) + ratioRaining ,1.5 ) );
    	
    	if(getEnergyStored(null)<energyCapacity){
    		fluidEnergy = 0;
        	FluidStack ds = tank.drain(amountToExtract, false);
        	if(ds!=null && ds.amount>=amountToExtract && amountToExtract!=0){
            	fluidEnergy = (int) Math.pow( Math.pow(structureratio, 1.1) * Math.pow(ds.amount/20+1,1.5) *  Math.pow(material.energyRatio, 1.1) , 0.85 );
        		tank.drain(amountToExtract, true);
        	}
    	}else
    		fluidEnergy = 0;
    	receiveEnergy(null,(int)passiveEnergy+(int)fluidEnergy, false);
    	
    	PacketHandler.sendToAllAround(new PacketEnergy(getPos(), getEnergyStored(null), lastEnergyStoredAmount),this);
        PacketHandler.sendToAllAround(new PacketFluid(getPos(), new int[]{tank.getFluidAmount()}, new int[]{tank.getCapacity()}, new String[]{ModFluids.fluidLiquefiedAsgardite.getName()}),this);
        PacketHandler.sendToAllAround(new PacketPillar(this), this);
    	lastEnergyStoredAmount = getEnergyStored(null);
    	
	}
	
	public void setMaterial(EnumPillarMaterial material){
		this.material = material;
	}
	
	@Override
	public int receiveEnergyWireless(int maxReceive, boolean simulate, BlockPos posFrom) {
		int re = getEnergyStorage().receiveEnergy(maxReceive, true);
		if(maxReceive>0 && re>0 && !simulate && random.nextFloat()<0.5F){
			PacketHandler.sendToAllAround(new PacketParticleMoving(getPos(),posFrom),this);
		}
		return isRedStoneEnable ? 0 : storage.receiveEnergy(maxReceive, simulate);
	
	}

	@Override
	public int extractEnergyWireless(int maxExtract, boolean simulate, BlockPos posTo) {
		int ex = getEnergyStorage().extractEnergy(maxExtract, true);
		if(maxExtract>0 && ex>0 && !simulate && random.nextFloat()<0.3F){
            PacketHandler.sendToAllAround(new PacketParticleMoving(posTo,getPos()),this);
		}
		return this.isRedStoneEnable ? 0 : storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return isRedStoneEnable ? 0 : getEnergyStorage().receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return isRedStoneEnable ? 0 : getEnergyStorage().extractEnergy(maxExtract, simulate);
	}
	@Override
	public List<BlockPos> getChildsList() {
		return energyChild;
	}

	@Override
	public List<FluidTank> getFluidTanks() {
		return new ArrayList<FluidTank>(){{
			add(tank);
		}};
	}
	
	@Override
	public EnergyStorage getEnergyStorage(){
		return this.storage;
	}
	
	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		this.lastEnergyStoredAmount = lastEnergyStored;
	}
	
	@Override
	public int getEnergyStored(EnumFacing from) {
		return getEnergyStorage().getEnergyStored();
	}

    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        
        structure = tag.getString("structure");
        material = EnumPillarMaterial.getMaterialFromId(tag.getInteger("material"));
		energyCapacity = PillarsConfig.getInstance().getPillarFromName(structure).energyCapacity;
		
		storage.setCapacity(energyCapacity);
		storage.readFromNBT(tag);
		storage.setMaxTransfer(energyCapacity/ratioTransfer);
		lastEnergyStoredAmount = tag.getInteger("lastEnergyStoredAmount");
		
		tank.readFromNBT(tag);
		tank.setCapacity(PillarsConfig.getInstance().getPillarFromName(structure).fluidCapacity);
        this.amountToExtract = tag.getInteger("amountToExtract");

        int[] tabch = tag.getIntArray("tabch");
        if(tabch != null && tabch.length>0){
	        int k = 0;
	        for(int i = 0 ; i < tabch.length/3 ; i++){
	        	this.energyChild.add(new BlockPos(tabch[k],tabch[k+1],tabch[k+2]));
	        	k = k+3;
	        }
	    }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setString("structure", structure);
        tag.setInteger("material", material.ID);
        tag.setInteger("amountToExtract", amountToExtract);

        storage.writeToNBT(tag);
        tank.writeToNBT(tag);
        
       	if(this.energyChild.size()>0){
	        int[] tabch = new int[energyChild.size()*3];
	        int k = 0;
	        for(BlockPos pos : this.energyChild){
	        	tabch[k] = pos.getX();
	        	k+=1;
	        	tabch[k] = pos.getY();
	        	k+=1;
	        	tabch[k] = pos.getZ();
	        	k+=1;
	        }
	    	tag.setIntArray("tabch", tabch);
    	}
        return tag;
    }
}
