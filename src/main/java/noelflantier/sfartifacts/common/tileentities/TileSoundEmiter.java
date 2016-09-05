package noelflantier.sfartifacts.common.tileentities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.entities.ai.EntityAITargetBlock;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnergy;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketSoundEmitter;
import noelflantier.sfartifacts.common.recipes.MobsPropertiesForSpawning;
import noelflantier.sfartifacts.common.recipes.handler.SoundEmitterConfig;

public class TileSoundEmiter extends TileAsgardianMachine implements ITileGlobalNBT{

	//INVENTORY
	public ItemStack[] items = new ItemStack[1];
	
	//PROCESSING
	public boolean isEmitting = false;
	public int frequencySelected = 0;
	public int frequencyEmited = 0;
	public int variant = -1;
    public String entityNameToSpawn;
	public Map<Integer, String> listScannedFrequency = new HashMap<Integer, String>();

	//SPAWNER
	public LivingEntitySpawnerBase spawnerBaseLogic = new LivingEntitySpawnerBase(){

		@Override
		public void broadcastEvent(int id) {
			TileSoundEmiter.this.worldObj.addBlockEvent(TileSoundEmiter.this.pos, Blocks.MOB_SPAWNER, id, 0);
		}

		@Override
		public World getSpawnerWorld() {
			return TileSoundEmiter.this.worldObj;
		}

		@Override
		public BlockPos getSpawnerPosition() {
			return TileSoundEmiter.this.pos;
		}

		@Override
		public boolean spawnConditions() {
			if(TileSoundEmiter.this.getEnergyStored(null)<=0 && TileSoundEmiter.this.getFluidTanks().get(0).getFluidAmount()<=0)
				return false;
			
			MobsPropertiesForSpawning mpfs = SoundEmitterConfig.getInstance().getMobsProperties(getEntityNameToSpawn(), TileSoundEmiter.this.getEnergyStored(null), TileSoundEmiter.this.getFluidTanks().get(0).getFluidAmount());
			if(mpfs!=null){
				int nbmax = mpfs.nbMaxSpawning==-1?this.maxSpawnCount:mpfs.nbMaxSpawning;
				int spx = 0;
				for(int i = nbmax;i>=this.minSpawnCount;i--){
					if(TileSoundEmiter.this.getFluidTanks().get(0).getFluidAmount()>=mpfs.fluidneeded.amount * nbmax
							&& TileSoundEmiter.this.getEnergyStored(null)>=mpfs.energyneeded * nbmax){
						spx = i;
						break;
					}
				}
				this.spawnCount = spx;
				this.attractedToSpawner = mpfs.isAttractedToSpawner;
				this.spawnEntityOnce = mpfs.isSpawningOnce;
				this.customX = mpfs.customX;
				this.customY = mpfs.customY;
				this.customZ = mpfs.customZ;
				if(spx<=0){
					return false;
				}else{
					FluidStack st = mpfs.fluidneeded.copy();
					st.amount = st.amount*spx;
					TileSoundEmiter.this.extractEnergy(null, mpfs.energyneeded*spx, false);
					TileSoundEmiter.this.tank.drainInternal(st, true);
					this.minSpawnRange = 5;
					this.spawnRange = 10;
					return true;
				}
			}
			return false;
		}

		@Override
		public void entityJustCreated(Entity entity) {
            entity.getEntityData().setIntArray(SoundEmitterConfig.KEY_SPAWN, new int[]{getSpawnerPosition().getX(),getSpawnerPosition().getY(),getSpawnerPosition().getZ()});
		}

		@Override
		public void entityJustSpawned(Entity entity) {
			if(entity instanceof EntityLiving && entity instanceof EntityCreature && this.attractedToSpawner){
				((EntityLiving)entity).targetTasks.addTask(0, new EntityAITargetBlock((EntityCreature) entity, 0,true,false,getSpawnerPosition()));
			}
		}

		@Override
		public void finishSpawning() {
			if(this.spawnEntityOnce){
				this.spawnEntityOnce = false;
				TileSoundEmiter.this.isEmitting = false;
			}
		}

		@Override
		public void newSpawning() {
            IBlockState iblockstate = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());
	        this.getSpawnerWorld().notifyBlockUpdate(TileSoundEmiter.this.pos, iblockstate, iblockstate, 4);
		}

		@Override
		public int getVariant() {
			return TileSoundEmiter.this.variant;
		}
		
	};
	
    public boolean receiveClientEvent(int id, int type)
    {
        return spawnerBaseLogic.setDelayToMin(id) ? true : super.receiveClientEvent(id, type);
    }
    
	public TileSoundEmiter(){
		super();		
		this.hasFL = true;
		this.hasRF = true;
    	this.storage.setCapacity(ModConfig.capacitySoundEmiter);
    	this.storage.setMaxReceive(ModConfig.capacitySoundEmiter/100);
    	this.storage.setMaxExtract(ModConfig.capacitySoundEmiter);
		this.tank.setCapacity(ModConfig.capacityAsgarditeSoundEmitter);
		this.tank.setFluid(new FluidStack(ModFluids.fluidLiquefiedAsgardite,0));
		for(EnumFacing f:EnumFacing.values()){
			this.receiveSides.add(f);
			this.extractSides.add(f);
		}
	}	
	
	@Override
	public void processPackets() {
		PacketHandler.sendToAllAround(new PacketFluid(this.getPos(), new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new String[]{ModFluids.fluidLiquefiedAsgardite.getName()}),this);
		PacketHandler.sendToAllAround(new PacketSoundEmitter(this),this);
	}

	@Override
	public boolean processGlobalUpdate(){
        if(isManualyEnable && !isRedStoneEnable){
        	processMachine();
        }

		if(this.getWorld().isRemote)
			return true;
        processPackets();	
        if(this.storage.getEnergyStored() != this.lastEnergyStoredAmount)
			PacketHandler.sendToAllAround(new PacketEnergy(this.getPos(), this.getEnergyStored(null), this.lastEnergyStoredAmount),this);
	
		this.lastEnergyStoredAmount = this.getEnergyStored(null);
		return true;
	}
	
	@Override
	public void processMachine(){
		if(!this.getWorld().isRemote)
			this.processInventory();
		if(this.isEmitting){
			if(this.entityNameToSpawn==null){
				spawnerBaseLogic.cachedEntity = null;
				this.entityNameToSpawn = SoundEmitterConfig.getInstance().getNameForFrequency(this.frequencyEmited);
				if(entityNameToSpawn!=null)
					spawnerBaseLogic.setEntityName(entityNameToSpawn);
			}else{
				spawnerBaseLogic.updateSpawner();
			}
		}
	}

	public boolean processInventory(){
		if(this.items[0]!=null){
			if(this.tank.getFluidAmount()<this.tank.getCapacity()){
				IFluidHandler fh = FluidUtil.getFluidHandler(items[0]);
				if(fh!=null){
    				FluidStack f = FluidUtil.tryFluidTransfer(tank, fh, 1000, true);
				}
			}
		}
		return true;
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

		spawnerBaseLogic.writeToNBT(nbt);
		nbt.setBoolean("isEmitting", this.isEmitting);
		nbt.setInteger("frequencySelected", this.frequencySelected);
		nbt.setInteger("frequencyEmited", this.frequencyEmited);
		nbt.setInteger("variant", this.variant);
		nbt.setInteger("sizefrequencyscanned", this.listScannedFrequency.size());
		if(this.listScannedFrequency.size()>0){
			int k = 0;
			for (Map.Entry<Integer, String> entry : this.listScannedFrequency.entrySet()){
				nbt.setInteger("frequency"+k, entry.getKey());
				nbt.setString("frequencystring"+k, entry.getValue());
				k++;
			}
		}
		return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

		spawnerBaseLogic.readFromNBT(nbt);
		this.isEmitting = nbt.getBoolean("isEmitting");
		this.frequencySelected = nbt.getInteger("frequencySelected");
		this.frequencySelected = nbt.getInteger("frequencySelected");
		this.variant = nbt.getInteger("variant");
		
		int size = nbt.getInteger("sizefrequencyscanned");
		if(size>0){
			for(int i = 0 ; i<size ; i++){
				this.listScannedFrequency.put(nbt.getInteger("frequency"+i), nbt.getString("frequencystring"+i));
			}
		}
    }
    
	@Override
    public void initAfterFacing(){
		fluidConnections.addAll(Arrays.asList(EnumFacing.VALUES));
		fluidAndSide.put(ModFluids.fluidLiquefiedAsgardite, Arrays.asList(EnumFacing.VALUES));
	}
    
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[]{0};
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		IFluidHandler fhandler = FluidUtil.getFluidHandler(stack);
		FluidStack drainfilled = null;
		if(fhandler!=null){
			drainfilled = fhandler.drain(new FluidStack(ModFluids.fluidLiquefiedAsgardite, Ressources.FLUID_MAX_TRANSFER), false);
		}
		return slot == 0 && drainfilled != null && drainfilled.amount > 0;
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
