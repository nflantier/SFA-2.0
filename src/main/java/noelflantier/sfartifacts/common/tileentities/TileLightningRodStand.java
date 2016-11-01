package noelflantier.sfartifacts.common.tileentities;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.BlockInjector;
import noelflantier.sfartifacts.common.blocks.BlockLightningRodStand;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.items.ItemLightningRod;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketLightningRodStand;

public class TileLightningRodStand extends TileMachine implements ITileUsingMaterials{

	//INVENTORY
	public ItemStack[] items = new ItemStack[1];
	
	//ENERGY
	public int lightningRodEnergy = 0;
	public static int energyTick = 10;	    
	public int currentEnergyTick = 0;
	
	//LIGHTNING
	public int lightningTick = 20;
	public int currentLightningTick = 0;
	
	public TileLightningRodStand(){
		super();
		this.hasFL = false;
		this.hasRF = true;
    	this.storage.setCapacity(ModConfig.capacityLightningRodStand);
    	this.storage.setMaxReceive(ModConfig.capacityLightningRodStand/100);
    	this.storage.setMaxExtract(ModConfig.capacityLightningRodStand);
    	this.extractSides.add(EnumFacing.DOWN);
	}
	
	@Override
	public void processPackets() {
		PacketHandler.sendToAllAround(new PacketLightningRodStand(this), this);
	}

	@Override
	public void processMachine() {        
        if(this.items[0] != null && this.items[0].getItem() instanceof ItemLightningRod){
        	this.lightningRodEnergy = (int) (Ressources.EFFICIENCY_LIGHTNING_ROD[this.items[0].getItemDamage()] * this.getEnergyRatio());
        	this.receiveEnergy(null,this.lightningRodEnergy/this.energyTick, false);
        	
        	this.currentLightningTick--;
        	if(this.currentLightningTick<=0){
        		this.currentLightningTick = (int)Math.random()*(200-50)+50;
        		int lc = -1;
	        	switch(this.items[0].getItemDamage()){
	        		case 0: lc = 2;
	        			break;
	        		case 1: lc = 5;
	        			break;
	        		case 2: lc = 10;
	        			break;
	        		case 3: lc = 20;
	        			break;
	        		default:
	        			break;
	        	}
            	if(this.randomMachine.nextInt(100)<lc){
    				this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.getPos().getX(), this.getPos().getY()+6, this.getPos().getZ(), false));
    	    		this.receiveEnergy(null,100, false);
            	}        	
            }
        }
    	
		TileEntity tile = worldObj.getTileEntity(this.getPos().add(0, -1, 0));
		if(tile!=null && tile instanceof IEnergyReceiver){
    		int maxExtract = this.storage.getMaxExtract();
    		int maxAvailable = this.storage.extractEnergy(maxExtract, true);
    		int energyTransferred = 0;
			energyTransferred = ((IEnergyReceiver) tile).receiveEnergy(EnumFacing.UP, maxAvailable, false);
    		this.extractEnergy(null,energyTransferred, false);
		}
	}
	
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("energyTickTmp", this.currentEnergyTick);
        nbt.setInteger("currentLightningTick", this.currentLightningTick);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.currentEnergyTick = nbt.getInteger("energyTickTmp");
        this.currentLightningTick = nbt.getInteger("currentLightningTick");
    }
    
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[]{0};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof ItemLightningRod;
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

	@Override
	public EnumPillarMaterial getMaterial() {
		return getWorld().getBlockState(getPos()).getValue(BlockLightningRodStand.MATERIAL);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(pos.getX()+0.5F, pos.getY()+0.5F, pos.getZ()+0.5F)<=64;
	}

}
