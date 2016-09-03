package noelflantier.sfartifacts.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeInput extends RecipeElement{
	public RecipeInput(){
		super();
	}
	public RecipeInput(ItemStack st){
		super(st);
    }
    public RecipeInput(FluidStack ft){
    	super(ft);
    }
}
