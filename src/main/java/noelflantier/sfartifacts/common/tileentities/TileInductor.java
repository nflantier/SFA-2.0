package noelflantier.sfartifacts.common.tileentities;

import java.util.ArrayList;
import java.util.List;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumTypeTech;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketParticleMoving;
import noelflantier.sfartifacts.compatibilities.IC2Handler;
import noelflantier.sfartifacts.compatibilities.InterMods;

@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2")
public class TileInductor extends ATileSFA implements ic2.api.energy.tile.IEnergySink,ITileCanBeMaster,ITileWirelessEnergy,ISFAEnergyHandler{
    	
    public EnumTypeTech type = EnumTypeTech.BASIC;
	public EnumFacing facing = EnumFacing.NORTH;

	public BlockPos master;
    public List<BlockPos> energyChild = new ArrayList<BlockPos>();
    public boolean canWirelesslySend = true;
    public boolean canWirelesslyRecieve = true;
    public boolean canSend = true;
    public boolean canRecieve = true;
    public int energyCapacity = 0;
    public static List<Integer> energyCapacityConfig = new ArrayList(){{
    	add(0,ModConfig.transfertCapacityInductorBasic);
    	add(1,ModConfig.transfertCapacityInductorAdvanced);
    	add(2,ModConfig.transfertCapacityInductorBasicEnergized);
    	add(3,ModConfig.transfertCapacityInductorAdvancedEnergized);
    }};
    public EnergyStorage storage = new EnergyStorage(0,0,0);
	public int lastEnergyStoredAmount = -1;
	
	public TileInductor(){
    	super();
    }

	public TileInductor(EnumTypeTech enumTypeTech) {
		this();
		this.type = enumTypeTech;
    	this.energyCapacity = energyCapacityConfig.get(enumTypeTech.ordinal())!=null?energyCapacityConfig.get(enumTypeTech.ordinal()):0;
    	this.storage.setCapacity(this.energyCapacity);
    	this.storage.setMaxReceive(this.energyCapacity);
    	this.storage.setMaxExtract(this.energyCapacity);
	}

	public void preinit(){
		super.preinit();
		if(InterMods.hasIc2 && !FMLCommonHandler.instance().getEffectiveSide().isClient())
			IC2Handler.loadIC2Tile(this);
	}
	@Override
	public void invalidate(){
		super.invalidate();
		unload();
	}
	void unload(){
		if(InterMods.hasIc2){
			IC2Handler.unloadIC2Tile(this);
		}
	}
	@Override
	public void onChunkUnload(){
		unload();
	}
	
	@Override
	public void update() {
		super.update();
		if(this.worldObj.isRemote)
			return;

		TileEntity tile = worldObj.getTileEntity(getPos().add(facing.getOpposite().getDirectionVec()));
		if(tile!=null && this.canSend){
			int maxAvailable = this.extractEnergy(facing, this.getEnergyStored(null), true);
			if(tile instanceof IEnergyReceiver){
				int energyTransferred = ((IEnergyReceiver) tile).receiveEnergy(facing, maxAvailable, false);
				this.extractEnergy(facing.getOpposite(), energyTransferred, false);
			}else if(InterMods.hasIc2 && IC2Handler.isEnergySink(tile)){
				double energyTransferred = IC2Handler.injectEnergy(tile, facing, IC2Handler.convertRFtoEU(maxAvailable,5), false);
				this.extractEnergy(facing.getOpposite(), IC2Handler.convertEUtoRF(IC2Handler.convertRFtoEU(maxAvailable,5)-energyTransferred), false);
			}
		}

    	if(!this.energyChild.isEmpty() && this.canWirelesslySend){
    		this.energyChild.removeIf((d)->worldObj.getTileEntity(d)==null || worldObj.getTileEntity(d) instanceof TileInductor == false);
        	if(!this.energyChild.isEmpty()){
        		for(BlockPos c : this.energyChild){
        			TileEntity te = worldObj.getTileEntity(c);
        			if(te instanceof TileInductor && ((TileInductor)te).getEnergyStored(null)<((TileInductor)te).getMaxEnergyStored(null) && ((TileInductor)te).canWirelesslyRecieve){
	                	int maxAc = this.storage.extractEnergy(this.getEnergyStored(null)/this.energyChild.size(), true);
	                	int energyTc = ((TileInductor) te).storage.receiveEnergy( maxAc, true);
	                	if(energyTc!=0){
	                		energyTc = ((TileInductor) te).storage.receiveEnergy( maxAc, false);
            				this.extractEnergyWireless(energyTc, false, te.getPos());
        				}
        			}
        		}
        	}
    	}
    	
    	if(this.storage.getEnergyStored()!=this.lastEnergyStoredAmount)
    		PacketHandler.sendToAllAround(new PacketEnergy(this.getPos(), this.getEnergyStored(null), this.lastEnergyStoredAmount),this);
 	
    	this.lastEnergyStoredAmount = this.getEnergyStored(null);
	}

	@Override
	public BlockPos getMasterPos() {
		return this.master;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return this.getEnergyStorage().getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return this.getEnergyStorage().getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		if(from == facing.getOpposite())
			return true;
		return false;
	}

	@Override
	public EnergyStorage getEnergyStorage() {
		return this.storage;
	}

	@Override
	public void setLastEnergyStored(int lastEnergyStored) {
		this.lastEnergyStoredAmount = lastEnergyStored;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return this.isRedStoneEnable || !this.canConnectEnergy(from) || !this.canRecieve ? 0 : this.storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return this.isRedStoneEnable || !this.canConnectEnergy(from)?0:this.storage.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int receiveEnergyWireless(int maxReceive, boolean simulate, BlockPos posTo) {
		return 0;
	}

	@Override
	public int extractEnergyWireless(int maxExtract, boolean simulate, BlockPos posTo) {
		int ex = this.storage.extractEnergy(maxExtract, true);
		if(maxExtract>0 && ex>0 && !simulate && this.worldObj.rand.nextFloat()<0.1){
            PacketHandler.sendToAllAround(new PacketParticleMoving(posTo,getPos()),this);
		}
		return this.isRedStoneEnable?0:this.storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public List<BlockPos> getChildsList() {
		return this.energyChild;
	}

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setInteger("facing", facing.getIndex());
    	
        nbt.setBoolean("canWirelesslySend", canWirelesslySend);
        nbt.setBoolean("canWirelesslyRecieve", canWirelesslyRecieve);
        nbt.setBoolean("canSend", canSend);
        nbt.setBoolean("canRecieve", canRecieve);
        nbt.setInteger("energyCapacity", energyCapacity);
        this.storage.writeToNBT(nbt);
        
       	if(this.energyChild.size()>0){
	        int[] tabch = new int[this.energyChild.size()*3];
	        int k = 0;
	        for(BlockPos ch : this.energyChild){
	        	tabch[k] = ch.getX();
	        	k+=1;
	        	tabch[k] = ch.getY();
	        	k+=1;
	        	tabch[k] = ch.getZ();
	        	k+=1;
	        }
	    	nbt.setIntArray("tabch", tabch);
    	}
       	return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

	    facing = EnumFacing.getFront(nbt.getInteger("facing"));
	    
        this.canWirelesslySend = nbt.getBoolean("canWirelesslySend");
        this.canWirelesslyRecieve = nbt.getBoolean("canWirelesslyRecieve");
        this.canSend = nbt.getBoolean("canSend");
        this.canRecieve = nbt.getBoolean("canRecieve");
        this.energyCapacity = nbt.getInteger("energyCapacity");
    	this.storage.setCapacity(this.energyCapacity);
    	this.storage.setMaxReceive(this.energyCapacity);
    	this.storage.setMaxExtract(this.energyCapacity);
		this.storage.readFromNBT(nbt);

        int[] tabch = nbt.getIntArray("tabch");
        if(tabch != null && tabch.length>0){
            int k = 0;
	        for(int i = 0 ; i < tabch.length/3 ; i++){
	        	this.energyChild.add(new BlockPos(tabch[k],tabch[k+1],tabch[k+2]));
	        	k = k+3;
	        }
        }
    }
	
	@Override
	@Optional.Method(modid = "IC2")
	public double getDemandedEnergy() {
		return InterMods.hasIc2?IC2Handler.convertRFtoEU(this.getMaxEnergyStored(null),5):0;
	}

	@Override
	@Optional.Method(modid = "IC2")
	public int getSinkTier() {
		return 5;
	}

	@Override
	@Optional.Method(modid = "IC2")
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		if(!InterMods.hasIc2)
			return 0;
		int c = this.receiveEnergy(directionFrom, IC2Handler.convertEUtoRF(amount), false);
		return amount-IC2Handler.convertRFtoEU(c,5);
	}

	@Override
	@Optional.Method(modid = "IC2")
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return InterMods.hasIc2 && this.canConnectEnergy(side);
	}
		

}

