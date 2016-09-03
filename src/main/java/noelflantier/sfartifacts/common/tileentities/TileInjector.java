package noelflantier.sfartifacts.common.tileentities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketInjector;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.IUseSFARecipes;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeInput;
import noelflantier.sfartifacts.common.recipes.RecipeOutput;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.InjectorRecipesHandler;

public class TileInjector extends TileAsgardianMachine implements ITileGlobalNBT, IUseSFARecipes, ITileUsingMaterials{
    	
	public EnumPillarMaterial material = EnumPillarMaterial.ASGARDITE;
	
	//PROCESSING
	public int tickToInject = 10;
	public boolean isRunning[] = new boolean[3];
	public int currentTickToInject[] = new int[3];
	public String currentRecipeName[] = new String[]{"none","none","none"};
	public int currentRecipeId[] = new int[3];

	//INVENTORY
	public ItemStack[] items = new ItemStack[13];
	
	public TileInjector(){
		super();		
		this.hasFL = true;
		this.hasRF = true;
    	this.storage.setCapacity(ModConfig.capacityInjector);
    	this.storage.setMaxReceive(ModConfig.capacityInjector/100);
    	this.storage.setMaxExtract(ModConfig.capacityInjector);
		this.tank.setCapacity(ModConfig.capacityAsgarditeInjector);
		this.tank.setFluid(new FluidStack(ModFluids.fluidLiquefiedAsgardite,0));
		for(EnumFacing f:EnumFacing.values()){
			this.receiveSides.add(f);
			this.extractSides.add(f);
		}
	}
	
	public TileInjector(EnumPillarMaterial material){
		this();
    	this.material = material;
    }

	@Override
	public void init(){
		super.init();
		fluidConnections.addAll(Arrays.asList(facing.getOpposite()));
		this.fluidAndSide =  new Hashtable<Fluid, List<EnumFacing>>();
		this.fluidAndSide.put(ModFluids.fluidLiquefiedAsgardite, Arrays.asList(facing.getOpposite()));
	}
	
	@Override
	public void processPackets() {	
		PacketHandler.sendToAllAround(new PacketFluid(this.getPos(), new int[]{tank.getFluidAmount()}, new int[]{tank.getCapacity()}, new String[]{ModFluids.fluidLiquefiedAsgardite.getName()}),this);
		PacketHandler.sendToAllAround(new PacketInjector(this),this);
	}

	@Override
	public void processMachine() {
        if(!this.isRedStoneEnable){
        	if(this.tank.getFluidAmount()<=tank.getCapacity()){
	    		/*for(EnumFacing fac : fluidAndSide.get(ModFluids.fluidLiquefiedAsgardite)){
	    			TileEntity t = worldObj.getTileEntity(getPos().add(fac.getDirectionVec()));
	    			if(t!=null){
	    				IFluidHandler ttank = FluidUtil.getFluidHandler(getWorld(), t.getPos(), fac.getOpposite());
	    				if(ttank!=null)
	    					FluidUtil.tryFluidTransfer(tank, ttank, 1000, true);
	    			}
	    		}*/
        	}
        	this.processInjecting();
        	this.processInventory();
        }
	}
	
	public boolean processInjecting(){
		for(int i=0;i<this.isRunning.length;i++){
			if(this.isRunning[i]){
				this.isRunningProcess(i);
			}else{
				this.isNotRunningProcess(i);
			}
		}
		return false;
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
	
	public void isRunningProcess(int idline){
		//this.isRunning[idline] = false;
		//this.currentRecipeName[idline] = "none";
		if(this.currentRecipeName[idline]!=null && !this.currentRecipeName[idline].equals("none")){
			ISFARecipe recipe = RecipesRegistry.instance.getRecipeForUsage(getUsageName(),this.currentRecipeName[idline]);
			if(recipe!=null && this.getEnergyStored(null)>=recipe.getEnergyCost()/this.tickToInject 
					&& this.getFluidTanks().get(0).getFluidAmount()>recipe.getFluidCost()/this.tickToInject){
				this.currentTickToInject[idline]-=1;
				if(this.getRandom(this.randomMachine)){
					this.extractEnergy(null, recipe.getEnergyCost()/this.tickToInject, false);
					this.tank.drain(recipe.getFluidCost()/this.tickToInject, true);
				}
				if(this.currentTickToInject[idline]<=0){
					if(RecipesRegistry.instance.canRecipeStackItem(recipe, getOutputStacks(idline))){
						int size = recipe.getOutputs().size();
						for(RecipeOutput ro : recipe.getOutputs()){
							if(ro.canStackWithItemStack(items[idline*2+6+1]) && size>0){
								size-=1;
								if(items[idline*2+6+1]==null){
									items[idline*2+6+1] = ro.getItemStack().copy();
								}else{
									items[idline*2+6+1].stackSize+=ro.getItemStack().stackSize;
								}
							}else if(ro.canStackWithItemStack(items[idline*2+6+1+1]) && size>0){
								size-=1;
								if(items[idline*2+6+1+1]==null){
									items[idline*2+6+1+1] = ro.getItemStack().copy();
								}else{
									items[idline*2+6+1+1].stackSize+=ro.getItemStack().stackSize;
								}
							}
							if(size<=0){
								break;
							}
						}
					}
					this.isRunning[idline] = false;
					this.currentRecipeName[idline] = "none";
				}
			}else{
				this.currentRecipeName[idline] = "none";
			}
		}
	}
	
	public void isNotRunningProcess(int idline){
		this.currentTickToInject[idline] = this.tickToInject;
		this.currentRecipeName[idline]= "none";
		if(items[idline*2+1]==null && items[idline*2+1+1]==null)
			return;
		
		List<ISFARecipe> recipes = RecipesRegistry.instance.getOrderedRecipesWithItemStacks(this, getInputStacks(idline));
		if(recipes!=null && !recipes.isEmpty()){
			for(ISFARecipe recipe : recipes){
				if(recipe!=null && recipe.getOutputs()!=null){
					if(RecipesRegistry.instance.canRecipeStackItem(recipe, getOutputStacks(idline))){
						this.currentRecipeName[idline]=recipe.getUid();
						this.isRunning[idline] = true;
						int size = recipe.getInputs().size();
						for(int i = 0 ; i < 2 ; i++){
							for(RecipeInput ri : recipe.getInputs()){
								if(RecipesRegistry.instance.getInputFromItemStack(items[idline*2+1+i]).isRecipeElementSame(ri)){
									items[idline*2+1+i].stackSize -= ri.getItemStack().stackSize;
									if(items[idline*2+1+i].stackSize<=0){
										items[idline*2+1+i]=null;
										this.worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
									}
									size-=1;
								}
							}
							if(size<=0)
								break;
						}
						break;
					}
				}
			}
		}
	}
	
	
	public List<ItemStack> getInputStacks(int idline){
		return new ArrayList<ItemStack>(){{
			add(items[idline*2+1]);
			add(items[idline*2+1+1]);
		}};
	}
	
	public List<ItemStack> getOutputStacks(int idline){
		return new ArrayList<ItemStack>(){{
			add(items[idline*2+1+6]);
			add(items[idline*2+1+1+6]);
		}};
	}
	
	@Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        for(int i =0;i<this.isRunning.length;i++)
    		isRunning[i] = nbt.getBoolean("isRunning"+i);

        for(int i =0;i<this.currentTickToInject.length;i++)
    		currentTickToInject[i] = nbt.getInteger("currentTickToInject"+i);
        
        for(int i =0;i<this.currentRecipeId.length;i++)
    		currentRecipeId[i] = nbt.getInteger("currentRecipeId"+i);
        
        for(int i =0;i<this.currentRecipeName.length;i++)
    		currentRecipeName[i] = nbt.getString("currentRecipeName"+i);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {        
	    for(int i =0;i<this.isRunning.length;i++)
	    	nbt.setBoolean("isRunning"+i, isRunning[i]);
	
	    for(int i =0;i<this.currentTickToInject.length;i++)
	    	nbt.setInteger("currentTickToInject"+i, currentTickToInject[i]);
	
	    for(int i =0;i<this.currentRecipeId.length;i++)
	    	nbt.setInteger("currentRecipeId"+i, currentRecipeId[i]);
	    
	    for(int i =0;i<this.currentRecipeName.length;i++)
	    	nbt.setString("currentRecipeName"+i, currentRecipeName[i]);
	    
    	return super.writeToNBT(nbt);
    }

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn) && Arrays.asList(new Integer[]{0,1,2,3,4,5,6}).contains((Integer)index);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return Arrays.asList(new Integer[]{7,8,9,10,11,12}).contains((Integer)index) || ( index == 0 && direction == facing.rotateY() );
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if(slot>0 && slot<7)
			return true;
		IFluidHandler fhandler = FluidUtil.getFluidHandler(stack);
		FluidStack drainfilled = null;
		if(fhandler!=null)
			drainfilled = fhandler.drain(new FluidStack(ModFluids.fluidLiquefiedAsgardite, Ressources.FLUID_MAX_TRANSFER), false);
		
		return  slot == 0 && drainfilled != null && drainfilled.amount > 0;
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

	@Override
	public String getUsageName() {
		return InjectorRecipesHandler.USAGE_INJECTOR;
	}
	@Override
	public int getEnergy() {
		return this.getEnergyStored(null);
	}
	@Override
	public int getFluid() {
		return this.getFluidTanks().get(0).getFluidAmount();
	}
	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeBase.class;
	}

	@Override
	public EnumPillarMaterial getMaterial() {
		return material;
	}

}

