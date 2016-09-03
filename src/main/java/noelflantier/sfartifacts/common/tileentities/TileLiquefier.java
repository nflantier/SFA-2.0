package noelflantier.sfartifacts.common.tileentities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketLiquefier;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.IUseSFARecipes;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeInput;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.LiquefierRecipesHandler;

public class TileLiquefier extends TileAsgardianMachine implements ITileGlobalNBT, IUseSFARecipes, ITileUsingMaterials{

	public EnumPillarMaterial material = EnumPillarMaterial.ASGARDITE;

	//INVENTORY
	public ItemStack[] items = new ItemStack[3];
	
	//PROCESSING
	public boolean isRunning;
	public String currentRecipeName = "none";
	public int tickToMelt = 10;
	public int currentTickToMelt;
	public FluidTank tankWater = new FluidTank(ModConfig.capacityWaterLiquefier);
	public static int[][] offsetWater = new int[][]{{0,-1,0},{1,-1,0},{-1,-1,0},{0,-1,1},{1,-1,1},{-1,-1,1},{0,-1,-1},{1,-1,-1},{-1,-1,-1}};
	
	/*public class FluidTankLiquefier extends FluidTank{

	    protected FluidStack fluidAsgardite;
	    protected FluidStack fluidWater;
	    
		public FluidTankLiquefier(int capacity)
	    {
	        super((FluidStack)null, capacity);
	    }

	    @Override
	    public FluidTank readFromNBT(NBTTagCompound nbt)
	    {
	        if (!nbt.hasKey("Empty"))
	        {
	            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
	            setFluid(fluid);
	        }
	        else
	        {
	            setFluid(null);
	        }
	        return this;
	    }

	    @Override
	    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	    {
	        if (fluid != null)
	        {
	            fluid.writeToNBT(nbt);
	        }
	        else
	        {
	            nbt.setString("Empty", "");
	        }
	        return nbt;
	    }
		
	}*/
	
	public TileLiquefier(){
		super();		
		this.hasFL = true;
		this.hasRF = true;
		
    	this.storage.setCapacity(ModConfig.capacityLiquefier);
    	this.storage.setMaxReceive(ModConfig.capacityLiquefier/100);
    	this.storage.setMaxExtract(ModConfig.capacityLiquefier);
		this.tank.setCapacity(ModConfig.capacityAsgarditeLiquefier);
		this.tank.setFluid(new FluidStack(ModFluids.fluidLiquefiedAsgardite,0));
		this.tankWater.setFluid(new FluidStack(FluidRegistry.WATER,0));
		for(EnumFacing f:EnumFacing.values()){
			this.receiveSides.add(f);
			this.extractSides.add(f);
		}
	}
	
	public TileLiquefier(EnumPillarMaterial material){
		this();
    	this.material = material;
    }

	@Override
	public void init(){
		super.init();
		fluidConnections.addAll(Arrays.asList(EnumFacing.VALUES));
		this.fluidAndSide =  new Hashtable<Fluid, List<EnumFacing>>();
		this.fluidAndSide.put(ModFluids.fluidLiquefiedAsgardite, Arrays.asList(facing.getOpposite()));
		this.fluidAndSide.put(FluidRegistry.WATER, Arrays.asList(EnumFacing.VALUES).stream().filter((e)->e!=facing.getOpposite()).collect(Collectors.toList()));
	}
	
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasFL){
        	if(fluidAndSide.get(FluidRegistry.WATER).contains(facing))
                return (T) getFluidTanks().get(1);
        	else if(fluidAndSide.get(ModFluids.fluidLiquefiedAsgardite).contains(facing))
                return (T) getFluidTanks().get(0);
            return super.getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }
    
	@Override
	public void processPackets() {	
		PacketHandler.sendToAllAround(new PacketFluid(this.getPos(), new int[]{this.tank.getFluidAmount(), this.tankWater.getFluidAmount()}, new int[]{this.tank.getCapacity(), this.tankWater.getCapacity()}, new String[]{ModFluids.fluidLiquefiedAsgardite.getName(), FluidRegistry.WATER.getName()}),this);
	    PacketHandler.sendToAllAround(new PacketLiquefier(this),this);
	}
	
	@Override
    public void processAtRandomTicks(){
		if(getFluidTanks().get(1).getFluidAmount()>=getFluidTanks().get(1).getCapacity())
			return;
		int wateramount = 0;
		if(!getRandom(randomMachine)){
			wateramount += 15;
		}
    	for(int i = 0;i<this.offsetWater.length;i++){
    		if(getWorld().getBlockState(getPos().add(offsetWater[i][0], offsetWater[i][1], offsetWater[i][2])).getBlock()==Blocks.WATER)
    			wateramount+=1;
    	}

		tankWater.fill(new FluidStack(FluidRegistry.WATER,wateramount), true);
    }

	@Override
    public float getRandomTickChance(){
    	return 0.18F;
    }
	
	@Override
	public void processMachine() {
        if(!isRedStoneEnable){
        	if(tank.getFluidAmount()>0){
				for(EnumFacing or : fluidAndSide.get(ModFluids.fluidLiquefiedAsgardite)){
					TileEntity t = worldObj.getTileEntity(getPos().add(or.getDirectionVec()));
					if(t!=null){
	    				IFluidHandler ttank = FluidUtil.getFluidHandler(getWorld(), t.getPos(), or.getOpposite());
	    				if(ttank!=null){
	    					FluidUtil.tryFluidTransfer(ttank, tank, 1000, true);
	    				}
	    			}
				}
        	}
	    	this.processInventory();
	    	this.processLiquefying();
        }
	}
	
	public boolean processInventory(){
		if(this.items[1]!=null){
			if(this.tankWater.getFluidAmount()<this.tankWater.getCapacity()){
				IFluidHandler fh = FluidUtil.getFluidHandler(items[1]);
				if(fh!=null){
    				FluidStack f = FluidUtil.tryFluidTransfer(tankWater, fh, 1000, true);
				}
			}
		}
		if(this.items[2]!=null){
			IFluidHandler fh = FluidUtil.getFluidHandler(items[2]);
			if(fh!=null){
				FluidUtil.tryFluidTransfer(fh, tank, 1000, true);
			}
		}
		return true;
	}

	@Override
	public List<FluidTank> getFluidTanks() {
		return new ArrayList<FluidTank>(){{add(tank);add(tankWater);}};
	}
	
	public List<ItemStack> getInputStacks(){
		return new ArrayList<ItemStack>(){{
			add(items[0]);
		}};
	}
	public boolean processLiquefying(){
		if(!this.isRunning){
			currentTickToMelt = tickToMelt;
			ISFARecipe recipe = RecipesRegistry.instance.getBestRecipeWithItemStacks(this, getInputStacks());
			if(recipe!=null){
				FluidStack fs = RecipesRegistry.instance.canRecipeStackTank(recipe, tank);
				if(fs!=null && fs.getFluid()!=null){
					isRunning = true;
					currentRecipeName=recipe.getUid();
					for(RecipeInput ri : recipe.getInputs()){
						if(ri.isItem()){
							items[0].stackSize -= ri.getItemStack().stackSize;
							if(items[0].stackSize<=0){
								items[0]=null;
								worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
							}
						}
					}
				}else
					return false;
			}
		}else{
			if(this.currentRecipeName!=null && !this.currentRecipeName.equals("none")){
				ISFARecipe recipe = RecipesRegistry.instance.getRecipeForUsage(getUsageName(),currentRecipeName);
				if(recipe!=null && this.getEnergyStored(null)>=recipe.getEnergyCost()/tickToMelt 
						&& tankWater.getFluidAmount()>recipe.getFluidCost()/tickToMelt){
					currentTickToMelt -= 1;
					if(getRandom(randomMachine)){
						tankWater.drain(recipe.getFluidCost()/this.tickToMelt, true);
						extractEnergy(null, recipe.getEnergyCost()/this.tickToMelt, false);
					}
					if(this.currentTickToMelt<=0){
						tank.fill(RecipesRegistry.instance.canRecipeStackTank(recipe, this.tank), true);
						isRunning = false;
						currentRecipeName="none";
						return true;
					}
				}
			}else{
				isRunning = false;
				currentRecipeName="none";
			}
		}
		return true;
	}
	
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("tankmelt", tankWater.writeToNBT(new NBTTagCompound()) );

        nbt.setString("currentRecipeName", currentRecipeName);
		nbt.setBoolean("isRunning", isRunning);
		nbt.setInteger("currentTickToMelt", currentTickToMelt);
		return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        tankWater.readFromNBT(nbt.getCompoundTag("tankmelt"));
        
        currentRecipeName = nbt.getString("currentRecipeName");
		isRunning = nbt.getBoolean("isRunning");
		currentTickToMelt = nbt.getInteger("currentTickToMelt");
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getCapabilityNoFacing(Capability<T> capability, EnumFacing facing){
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return (T) getFluidTanks().get(1);
        }
        return super.getCapability(capability, facing);
    }
    
	@Override
	public int[] getSlotsForFace(EnumFacing side) {	
		return new int[]{0,1,2};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn) && Arrays.asList(new Integer[]{0,1}).contains((Integer)index);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return Arrays.asList(new Integer[]{2}).contains((Integer)index);
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if(slot == 0)
			return true;
		IFluidHandler fhandler = FluidUtil.getFluidHandler(stack);
		FluidStack drainfilled = null;
		int amountfilled = 0;
		if(fhandler!=null){
			drainfilled = fhandler.drain(new FluidStack(FluidRegistry.WATER, Ressources.FLUID_MAX_TRANSFER), false);
			amountfilled = fhandler.fill(new FluidStack(ModFluids.fluidLiquefiedAsgardite, Ressources.FLUID_MAX_TRANSFER), false);
		}
		return ( slot == 1 && drainfilled != null && drainfilled.amount > 0) || ( slot == 2 && amountfilled > 0 );
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
	public EnumPillarMaterial getMaterial() {
		return this.material;
	}

	@Override
	public String getUsageName() {
		return LiquefierRecipesHandler.USAGE_LIQUEFIER;
	}

	@Override
	public int getEnergy() {
		return getEnergyStored(null);
	}

	@Override
	public int getFluid() {
		return tankWater.getFluidAmount();
	}

	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeBase.class;
	}

}
