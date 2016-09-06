package noelflantier.sfartifacts.compatibilities.jei.recipe;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.recipes.RecipeInput;
import noelflantier.sfartifacts.common.recipes.RecipeMightyFoundry;
import noelflantier.sfartifacts.common.recipes.RecipeOutput;

public class MightyFoundryRecipeWrapper implements IRecipeWrapper{

	private final List<ItemStack> inputItem;
	private final List<ItemStack> outputItem;
	public int energy = 0;
	
	public MightyFoundryRecipeWrapper(RecipeMightyFoundry recipe) {
		this.inputItem = new ArrayList<ItemStack>();
		this.outputItem = new ArrayList<ItemStack>();
		this.energy = recipe.getEnergyCost();
		
		this.inputItem.add(recipe.getMold());
		
		for(RecipeInput ri :recipe.getInputs()){
	    	if(ri.isItem()){
	    		ItemStack s = new ItemStack(ri.getItemStack().getItem(), recipe.getItemQuantity());
	    		this.inputItem.add(s);
	    	}
	    }
		for(RecipeOutput ri :recipe.getOutputs()){
	    	if(ri.isItem())
	    		this.outputItem.add(ri.getItemStack());
	    }
	}

	@Override
	public void drawAnimations(Minecraft arg0, int arg1, int arg2) {
		
	}

	@Override
	public void drawInfo(Minecraft minecraft, int arg1, int arg2, int arg3, int arg4) {
		minecraft.fontRendererObj.drawString("Energy per one : "+this.energy+" RF", 2, 80, TextFormatting.GRAY.getColorIndex());
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return ImmutableList.of();
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return ImmutableList.of();
	}

	@Override
	public List getInputs() {
		return inputItem;
	}

	@Override
	public List getOutputs() {
		return outputItem;
	}

	@Override
	public List<String> getTooltipStrings(int arg0, int arg1) {
		return ImmutableList.of();
	}

	@Override
	public boolean handleClick(Minecraft arg0, int arg1, int arg2, int arg3) {
		return false;
	}

}
