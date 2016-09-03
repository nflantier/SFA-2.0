package noelflantier.sfartifacts.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeOutput extends RecipeElement{
    public RecipeOutput(){
    	super();
    }
	public RecipeOutput(ItemStack st){
    	super(st);
    }
    public RecipeOutput(FluidStack ft){
    	super(ft);
    }
}
