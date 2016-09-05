package noelflantier.sfartifacts.common.tileentities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemMold;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketFluid;
import noelflantier.sfartifacts.common.network.messages.PacketMightyFoundry;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.IUseSFARecipes;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeInput;
import noelflantier.sfartifacts.common.recipes.RecipeMightyFoundry;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.MightyFoundryRecipesHandler;

public class TileMightyFoundry extends TileAsgardianMachine implements ITileGlobalNBT, IUseSFARecipes{

	//PROCESSING
	public boolean isLocked = false;
	public boolean isRunning = false;
	public String currentRecipeName = "none";
	public double progression = 0.0D;
	public int numberofItemAllreadyCasted = 0;
	public int initialTickToMelt = 1000;
	public int tickToMelt = 1000;
	public int currentTickToMelt = 0;
	public FluidStack fluidNeededPerOneItem = new FluidStack(FluidRegistry.LAVA, 200);
	
	//INVENTORY
	public ItemStack[] items = new ItemStack[7];
	
	public TileMightyFoundry(){
		super();		
		this.hasFL = true;
		this.hasRF = true;
    	this.storage.setCapacity(ModConfig.capacityMightyFoundry);
    	this.storage.setMaxReceive(ModConfig.capacityMightyFoundry/100);
    	this.storage.setMaxExtract(ModConfig.capacityMightyFoundry);
		this.tank.setCapacity(ModConfig.capacityLavaMightyFoundry);
		this.tank.setFluid(new FluidStack(FluidRegistry.LAVA,0));
		for(EnumFacing f:EnumFacing.values()){
			this.receiveSides.add(f);
			this.extractSides.add(f);
		}	
		if(this.currentTickToMelt==0)
			this.currentTickToMelt = this.tickToMelt;
	}
	
	public TileMightyFoundry(EnumPillarMaterial material){
		this();
    }
	
	@Override
	public void processPackets() {
		PacketHandler.sendToAllAround(new PacketFluid(this.getPos(), new int[]{this.tank.getFluidAmount()}, new int[]{this.tank.getCapacity()}, new String[]{FluidRegistry.LAVA.getName()}),this);
		PacketHandler.sendToAllAround(new PacketMightyFoundry(this),this);  
	}

	@Override
	public void processMachine() {        
        this.processInventory();
        
        if(this.isLocked && this.getStackInSlot(1)!=null)
        	this.processFoundry();
        if(this.isRunning && this.getStackInSlot(1)==null){
        	resetFoundry();
        } 
	}

	@Override
    @SideOnly(Side.CLIENT)
	public void processClientMachine(){
		if(this.isRunning && this.randomMachine.nextFloat()<0.1F){
			float nx = 0.5F+facing.getFrontOffsetX();
			float ny = 0.5F+facing.getFrontOffsetY();
			float nz = 0.5F+facing.getFrontOffsetZ();
			nx = nx<0?nx+0.4F:nx>0.5F?nx-0.4F:nx;
			ny = ny<0?ny+0.4F:ny>0.5F?ny-0.4F:ny;
			nz = nz<0?nz+0.4F:nz>0.5F?nz-0.4F:nz;
			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX()+nx, this.getPos().getY()+ny+0.1F, this.getPos().getZ()+nz, 0.0D, 0.0D, 0.0D);
		}
	}
	
	public void resetFoundry(){
		this.isRunning = false;
		this.progression = 0.0D;
		this.currentRecipeName = "none";
		this.numberofItemAllreadyCasted = 0;
	}

	public List<ItemStack> getInputStacks(){
		return new ArrayList<ItemStack>(){{
			add(getStackInSlot(2));
			add(getStackInSlot(3));
			add(getStackInSlot(4));
			add(getStackInSlot(5));
		}};
	}
	
	public boolean processFoundry(){
		ItemStack mold = this.getStackInSlot(1);
		if(this.currentRecipeName==null || this.currentRecipeName.equals("none")){
			ISFARecipe recipe = RecipesRegistry.instance.getRecipeWithMoldMeta(mold.getItemDamage());
			if(recipe!=null){
				this.currentRecipeName = recipe.getUid();
				this.initialTickToMelt = RecipeMightyFoundry.class.cast(recipe).getTickPerItem();
				this.tickToMelt = this.initialTickToMelt;
				this.currentTickToMelt = this.initialTickToMelt;
			}
		}
		ISFARecipe recipe = RecipesRegistry.instance.getRecipeForUsage(getUsageName(),this.currentRecipeName);
		if(recipe!=null){
			int itemQ = RecipeMightyFoundry.class.cast(recipe).getItemQuantity();
			this.progression = (double)this.numberofItemAllreadyCasted/(double)itemQ;
			if(this.isRunning){
				if(this.currentTickToMelt<this.tickToMelt){
					this.currentTickToMelt+=1;
				}else{
					if(this.numberofItemAllreadyCasted>=itemQ){
						this.setInventorySlotContents(6, RecipesRegistry.instance.getResultForRecipe(recipe));
						this.setInventorySlotContents(1, null);
						this.isLocked = false;
						worldObj.notifyBlockUpdate(getPos(), worldObj.getBlockState(getPos()), worldObj.getBlockState(getPos()), 3);
						resetFoundry();
					}else{
						if(RecipesRegistry.instance.isRecipeCanBeDone(recipe, getInputStacks(), this)){
							this.extractEnergy(null, recipe.getEnergyCost(), false);
							if(this.tank.getFluidAmount()>=this.fluidNeededPerOneItem.amount){
								this.tank.drain(this.fluidNeededPerOneItem.amount, true);
								this.tickToMelt = this.initialTickToMelt/4;
							}else{
								this.tickToMelt = this.initialTickToMelt;
							}
							this.numberofItemAllreadyCasted+=1;
							this.currentTickToMelt = 0;
							int size = recipe.getInputs().size();
							for(RecipeInput ri: recipe.getInputs()){
								for(int i = 0;i < 4; i++){
									if(RecipesRegistry.instance.getInputFromItemStack(items[2+i]).isRecipeElementSame(ri)){
										items[2+i].stackSize -= ri.getItemStack().stackSize;
										if(items[2+i].stackSize<=0){
											items[2+i]=null;
										}
										size-=1;
									}
									if(size<=0)
										break;
								}
								if(size<=0)
									break;
							}
						}
					}
				}
			}else{
				this.isRunning = true;
				//this.currentRecipeName = recipe.getUid();
				this.numberofItemAllreadyCasted = 0;
			}
		}else{
			this.currentRecipeName = "none";
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
	
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
		nbt.setBoolean("isLocked", this.isLocked);
		nbt.setBoolean("isRunning", this.isRunning);
		nbt.setInteger("tickToMelt",this.tickToMelt);
		nbt.setInteger("currentTickToMelt",this.currentTickToMelt);
		nbt.setString("currentRecipeName", this.currentRecipeName);
		nbt.setInteger("numberofItemAllreadyCasted", this.numberofItemAllreadyCasted);
		return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
		this.isLocked = nbt.getBoolean("isLocked");
		this.isRunning = nbt.getBoolean("isRunning");
		this.tickToMelt = nbt.getInteger("tickToMelt");
		this.currentTickToMelt = nbt.getInteger("currentTickToMelt");
		this.currentRecipeName = nbt.getString("currentRecipeName");
		this.numberofItemAllreadyCasted = nbt.getInteger("numberofItemAllreadyCasted");
		
    }

	@Override
    public void initAfterFacing(){
		fluidConnections.addAll(Arrays.asList(EnumFacing.VALUES));
		fluidAndSide.put(FluidRegistry.LAVA, Arrays.asList(EnumFacing.VALUES));
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		IFluidHandler fhandler = FluidUtil.getFluidHandler(stack);
		FluidStack drainfilled = null;
		if(fhandler!=null)
			drainfilled = fhandler.drain(new FluidStack(FluidRegistry.LAVA, Ressources.FLUID_MAX_TRANSFER), false);
		
		return  ( slot == 0 && drainfilled != null && drainfilled.amount > 0 ) || 
				( slot == 1 && stack.getItem() instanceof ItemMold && ItemNBTHelper.getInteger(stack, "idmold", 0) > 0 ) ||
				( slot > 1 && slot < 6 && this.getStackInSlot(1) != null && isItemValidForSlot(1,this.getStackInSlot(1)) && this.isLocked );
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[]{0,1,2,3,4,5,6};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn) && Arrays.asList(new Integer[]{0,1,2,3,4,5}).contains((Integer)index);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return Arrays.asList(new Integer[]{6}).contains((Integer)index);
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
		return MightyFoundryRecipesHandler.USAGE_MIGHTY_FOUNDRY;
	}

	@Override
	public int getEnergy() {
		return this.getEnergyStored(null);
	}

	@Override
	public int getFluid() {
		return 0;
	}

	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeMightyFoundry.class;
	}

}
