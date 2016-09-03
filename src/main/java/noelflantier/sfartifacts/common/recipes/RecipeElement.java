package noelflantier.sfartifacts.common.recipes;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class RecipeElement {

	protected ItemStack inputItem;
	protected FluidStack inputFluid;
	protected boolean isOreDict = false;

	protected String oreName;
	
	public RecipeElement(){
    	this.inputFluid = null;
    	this.inputItem = null;
	}
	public RecipeElement(ItemStack st){
    	this.inputItem = st;
    	this.inputFluid = null;
    }
    public RecipeElement(FluidStack ft){
    	this.inputFluid = ft;
    	this.inputItem = null;
    }

    public boolean isFluid(){
    	return this.getFluidStack()!=null;
    }
    public boolean isItem(){
    	return this.getItemStack()!=null;
    }

	public boolean isOreDict() {
		return isOreDict;
	}
	public void setIsOreDict(boolean isOreDict) {
		this.isOreDict = isOreDict;
	}
	public String getOreName() {
		return oreName;
	}
	public void setOreName(String oreName) {
		this.setIsOreDict(true);
		this.oreName = oreName;
	}
    public ItemStack getItemStack() {
		return inputItem;
	}
	public void setItemStack(ItemStack inputItem) {
		this.inputItem = inputItem;
	}
    public FluidStack getFluidStack() {
		return inputFluid;
	}
	public void setFluidStack(FluidStack inputFluid) {
		this.inputFluid = inputFluid;
	}
    public int getStackSize() {
    	if(getItemStack()!=null){
    		return getItemStack().stackSize;
		}
		if(getFluidStack()!=null){
    		return getFluidStack().amount;
		}
		return 0;
	}
    public String getName() {
    	if(getItemStack()!=null){
    		return getItemStack().getUnlocalizedName();
		}
		if(getFluidStack()!=null){
    		return getFluidStack().getUnlocalizedName();
		}
		return null;
	}	
	public boolean isItemStackSame(ItemStack stack){
		return stack.getItem()!=null && getItemStack().getItem()!=null && getItemStack().getItem()==stack.getItem() 
				&& getItemStack().getItemDamage()==stack.getItemDamage() && getItemStack().stackSize>=stack.stackSize;
	}
	public boolean isItemStackSameNoSize(ItemStack stack){
		return stack.getItem()!=null && getItemStack().getItem()!=null && getItemStack().getItem()==stack.getItem() 
				&& getItemStack().getItemDamage()==stack.getItemDamage();
	}
	public boolean isFluidStackSame(FluidStack stack){
		return stack.getFluid()==getFluidStack().getFluid();
	}


    public boolean canStackWithItemStack(ItemStack i){
    	boolean flag = false;
		if(getItemStack()!=null){
			if(i==null || (i!=null && i.stackSize<i.getMaxStackSize() 
					&& i.stackSize+getItemStack().stackSize<=i.getMaxStackSize() 
					&& i.getItem()==getItemStack().getItem()
					&& i.getItemDamage()==getItemStack().getItemDamage())){
				flag = true;
			}
		}
		return flag;
    }
    
    public boolean isSameItem(ItemStack i){
    	boolean flag = false;
		if(getItemStack()!=null){
			if(i!=null && i.getItem()==getItemStack().getItem()
					&& i.getItemDamage()==getItemStack().getItemDamage()){
				flag = true;
			}
		}
		return flag;
    }
    
    public List<ItemStack> canStackWithElement(List<ItemStack> it){
		if(getItemStack()!=null && it!=null && !it.isEmpty()){
			Predicate<ItemStack> predicateItem = (i)->i==null || (i!=null && i.stackSize<i.getMaxStackSize() 
					&& i.stackSize+getItemStack().stackSize<=i.getMaxStackSize() 
					&& i.getItem()==getItemStack().getItem()
					&& i.getItemDamage()==getItemStack().getItemDamage());
			it.removeIf(predicateItem.negate());
			return it;
		}
    	return it;
    }

    public boolean isRecipeElementSameNoSize(RecipeElement ri){//the object should always be the potential input or output / ri should awlays be the recipeelement of the choosen recipe
		if(ri!=null && ri.getItemStack()!=null){
			return getItemStack()!=null && isItemStackSameNoSize(ri.getItemStack());
		}
		if(ri!=null && ri.getFluidStack()!=null){
			return getFluidStack()!=null && isFluidStackSame(ri.getFluidStack());
		}
		return false;
	}
    
	public boolean isRecipeElementSame(RecipeElement ri){//the object should always be the potential input or output / ri should awlays be the recipeelement of the choosen recipe
		if(ri!=null && ri.getItemStack()!=null){
			return getItemStack()!=null && isItemStackSame(ri.getItemStack());
		}
		if(ri!=null && ri.getFluidStack()!=null){
			return getFluidStack()!=null && isFluidStackSame(ri.getFluidStack());
		}
		return false;
	}
	
	public void debug(){
		String str = "";
		if(this.getItemStack()!=null)
			str += this.inputItem.getItem()+"  "+this.inputItem.stackSize+"  ";
		if(this.getFluidStack()!=null)
			str += this.inputFluid.getFluid().getName()+"  "+this.inputFluid.amount;
		System.out.println(str);
	}
}
