package noelflantier.sfartifacts.common.tileentities;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.BlockMrFusion;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.helpers.Utils;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;

public class TileMrFusion extends TileMachine{

	//RATIO
	public static float ratioFood = 3;
	public static float ratioLiquid = 2.5F;
	public static float ratioDefault= 1;
	
	//RF
	public static int rf = 5;
	
	//INVENTORY
	public ItemStack[] items = new ItemStack[64];
	
	public TileMrFusion(){
		super();		
		this.hasFL = true;
		this.hasRF = true;
    	this.storage.setCapacity(ModConfig.capacityMrFusion);
    	this.storage.setMaxReceive(ModConfig.capacityMrFusion);
    	this.storage.setMaxExtract(ModConfig.capacityMrFusion);
		this.tank.setCapacity(ModConfig.capacityLiquidMrFusion);
	}
	
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasFL){
        	return (T) getFluidTanks().get(0);
        }
        return super.getCapability(capability, facing);
    }
    
	@Override
	public void processPackets() {
		PacketHandler.sendToAllAround(new PacketFluid(getPos(), new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new String[]{ModFluids.fluidLiquefiedAsgardite.getName()}),this);	
	}
	
	@Override
	public void processMachine() {
		processRfGain();
		extractEnergyToSides(true,true);
	}

	@Override
	public void processUpdate(){
		processInventory();
	}
	
	public boolean processInventory(){
		if(this.items[0]!=null){
			if(this.tank.getFluidAmount()<this.tank.getCapacity()){
				IFluidHandler fluid = FluidUtil.getFluidHandler(this.items[0]);
				if(fluid!=null && fluid.drain(Ressources.FLUID_MAX_TRANSFER, false) != null && fluid.drain(Ressources.FLUID_MAX_TRANSFER, false).amount > 0 ){
					this.items[0] = FluidUtil.tryEmptyContainer(this.items[0], this.tank, this.tank.getCapacity(), null, true);
					return true;
				}
			}
		}
		return false;
	}
	
	public void processRfGain(){
		if(this.getEnergyStored(null)>=this.storage.getMaxEnergyStored())
			return;
		float rfg = (float)0;
		rfg += (float)this.tank.getFluidAmount()*this.ratioLiquid;
		int m  = (int)(this.tank.getFluidAmount()/this.tank.getCapacity())*10;
		m = m<=0?1:m;
		this.tank.drain(this.tank.getCapacity(), true);
		this.storage.receiveEnergy((int)rfg, false);
		if(this.getEnergyStored(null)>=this.storage.getMaxEnergyStored())
			return;

		for(int i = 1 ; i < this.items.length ; i++){
			if(this.items[i]==null)
				continue;
			rfg = 0;
			if(Utils.getAllSuperclasses(this.items[i].getItem().getClass()).contains(ItemFood.class)){//FOOD
				rfg+=this.ratioFood*(float)this.rf*(float)this.items[i].stackSize*m;
			}else{
				rfg+=this.ratioDefault*(float)this.rf*(float)this.items[i].stackSize;
			}
			this.items[i] = null;
			this.storage.receiveEnergy((int)rfg*m, false);
			if(this.getEnergyStored(null)>=this.storage.getMaxEnergyStored())
				return;
		}
	}

	@Override
    public void initAfterFacing(){
		extractSides.add(this.worldObj.getBlockState(getPos()).getValue(BlockMrFusion.ALL_FACING).getOpposite());
		fluidConnections.addAll(Arrays.asList(EnumFacing.VALUES));
		fluidAndSide.put(ModFluids.fluidLiquefiedAsgardite, Arrays.asList(EnumFacing.VALUES));
	}
    
	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return isRedStoneEnable || !canConnectEnergy(from) ? 0 : storage.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63};
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return (slot == 0 && FluidUtil.getFluidHandler(stack) != null ) || slot > 0 && slot < 63;
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack s = this.getStackInSlot(index);
		setInventorySlotContents(index, null);
		return s;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
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
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ItemStack[] getItems() {
		return items;
	}

}
