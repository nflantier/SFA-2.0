package noelflantier.sfartifacts.compatibilities.jei.recipe;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.recipes.RecipeInput;
import noelflantier.sfartifacts.common.recipes.RecipeMold;
import noelflantier.sfartifacts.common.recipes.RecipeOutput;

public class MoldRecipeWrapper implements IRecipeWrapper{

	private final List<ItemStack> inputItem;
	private final List<ItemStack> outputItem;
	public int[] shape;
	
	public MoldRecipeWrapper(RecipeMold recipe) {
		this.inputItem = new ArrayList<ItemStack>();
		this.outputItem = new ArrayList<ItemStack>();
		this.shape = recipe.getTabShape();
		this.outputItem.add(new ItemStack(ModItems.itemMold,0,recipe.getMoldMeta()));
	}

	@Override
	public void drawAnimations(Minecraft arg0, int arg1, int arg2) {
		
	}

	@Override
	public void drawInfo(Minecraft arg0, int arg1, int arg2, int arg3, int arg4) {
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
