package noelflantier.sfartifacts.common.tileentities;

import java.util.ArrayList;
import java.util.List;

import baubles.api.BaublesApi;
import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Loader;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketRecharger;
import noelflantier.sfartifacts.compatibilities.IC2Handler;
import noelflantier.sfartifacts.compatibilities.InterMods;

public class TileRecharger extends TileMachine implements ITileGlobalNBT{
	
	//INVENTORY
	public ItemStack[] items = new ItemStack[8];

	public boolean wirelessRechargingEnable = true;
	public boolean isRecharging = false;
	public boolean tmpRecharging = false;
	
	public TileRecharger(){
		super();		
		hasRF = true;
		hasFL = false;
		this.storage.setCapacity(500000);
		this.storage.setMaxTransfer(ModConfig.transferRecharger);
	}
	
	@Override
	public void processMachine() {
		if(this.getEnergyStored(null)<=0)
			return;

		tmpRecharging = false;
		for(int i=0;i<this.items.length;i++){
			if(this.items[i]!=null && ( this.items[i].getItem() instanceof IEnergyContainerItem 
					|| (InterMods.hasIc2 && IC2Handler.isElectricItem(this.items[i])) )){
				this.rechargeItemStack(this.items[i]);
			}
		}
		if(wirelessRechargingEnable)
			getAllPlayerAround(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(),ModConfig.rangeOfRecharger*10).forEach((p)->rechargePlayerInv(p));
		if(this.isRecharging != tmpRecharging){
			this.isRecharging = tmpRecharging;
			PacketHandler.sendToAllAround(new PacketRecharger(this),this);
		}
		
	}

	public void rechargeItemStack(ItemStack stack){
		if(stack.getItem() instanceof IEnergyContainerItem){
			int s = ((IEnergyContainerItem)stack.getItem()).getEnergyStored(stack);
			int c = ((IEnergyContainerItem)stack.getItem()).getMaxEnergyStored(stack);
			if(s<c){
	    		int maxAvailable = this.extractEnergy(null, this.getEnergyStored(null), true);
	    		int energyTransferred = ((IEnergyContainerItem)stack.getItem()).receiveEnergy(stack, maxAvailable, true);
	    		if(energyTransferred!=0){
	    			this.tmpRecharging = true;
					energyTransferred = ((IEnergyContainerItem)stack.getItem()).receiveEnergy(stack, maxAvailable, false);
					this.extractEnergy(null, energyTransferred, false);
				}
			}
		}else if(InterMods.hasIc2 && IC2Handler.isElectricItem(stack)){
			double s = IC2Handler.getCharge(stack);
			double c = IC2Handler.getMaxCharge(stack);
			if(s<c){
	    		int maxAvailable = this.extractEnergy(null, this.getEnergyStored(null), true);
	    		double energyTransferred = IC2Handler.charge(stack, IC2Handler.convertRFtoEU(maxAvailable,5), 5, true, true);
	    		if(energyTransferred!=0){
	    			this.tmpRecharging = true;
					energyTransferred = IC2Handler.charge(stack, IC2Handler.convertRFtoEU(maxAvailable,5), 5, true, false);
					this.extractEnergy(null, IC2Handler.convertEUtoRF(energyTransferred), false);
				}
			}
			
		}
	}
	
	public void rechargePlayerInv(EntityPlayer p){
		for(int i = 0;i<p.inventory.armorInventory.length;i++){
			if(this.getEnergyStored(null)<=0)
				break;
			if(p.inventory.armorInventory[i]!=null && ( p.inventory.armorInventory[i].getItem() instanceof IEnergyContainerItem 
					|| (InterMods.hasIc2 && IC2Handler.isElectricItem(p.inventory.armorInventory[i]) )) )
				this.rechargeItemStack(p.inventory.armorInventory[i]);
		}
		for(int i = 0;i<p.inventory.mainInventory.length;i++){
			if(this.getEnergyStored(null)<=0)
				break;
			if(p.inventory.mainInventory[i]!=null && ( p.inventory.mainInventory[i].getItem() instanceof IEnergyContainerItem 
					|| (InterMods.hasIc2 && IC2Handler.isElectricItem(p.inventory.mainInventory[i]))))
				this.rechargeItemStack(p.inventory.mainInventory[i]);	
		}

		if(Loader.isModLoaded("Baubles")){
			if(BaublesApi.getBaubles(p)!=null){
				for(int i = 0;i<BaublesApi.getBaubles(p).getSizeInventory();i++){
					if(this.getEnergyStored(null)<=0)
						break;
					if(BaublesApi.getBaubles(p).getStackInSlot(i)!=null && ( BaublesApi.getBaubles(p).getStackInSlot(i).getItem() instanceof IEnergyContainerItem 
							|| (InterMods.hasIc2 && IC2Handler.isElectricItem(BaublesApi.getBaubles(p).getStackInSlot(i)))))
						this.rechargeItemStack(BaublesApi.getBaubles(p).getStackInSlot(i));	
				}
			}
		}
	}
	
	public List<EntityPlayer> getAllPlayerAround(int x, int y, int z, int r){
		ArrayList<EntityPlayer> a = new ArrayList<EntityPlayer>();	
        for (int i = 0; i < this.worldObj.playerEntities.size(); ++i){
	        EntityPlayer entityplayer1 = (EntityPlayer)this.worldObj.playerEntities.get(i);
	        if(entityplayer1.getDistanceSq(x, y, z)<r)
	        	a.add(entityplayer1);
        }
		return a;
	}
	
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("isRecharging", this.isRecharging);
        nbt.setBoolean("wirelessRechargingEnable", this.wirelessRechargingEnable);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.isRecharging = nbt.getBoolean("isRecharging");
        this.wirelessRechargingEnable = nbt.getBoolean("wirelessRechargingEnable");
    }
    
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[]{0,1,2,3,4,5,6,7};
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof IEnergyContainerItem 
				|| ItemNBTHelper.verifyExistance(stack, "Energy") 
				|| (InterMods.hasIc2 && IC2Handler.isElectricItem(stack));
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
	public void processPackets() {
	}


	@Override
	public ItemStack[] getItems() {
		return items;
	}
	
	@Override
	public NBTTagCompound writeToNBTItem(NBTTagCompound nbt) {
		return this.writeToNBT(nbt);
	}

	@Override
	public void readFromNBTItem(NBTTagCompound nbt) {
		this.readFromNBT(nbt);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(pos.getX()+0.5F, pos.getY()+0.5F, pos.getZ()+0.5F)<=64;
	}

}
