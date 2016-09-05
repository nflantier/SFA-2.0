package noelflantier.sfartifacts.compatibilities.jei.recipe;

import java.util.Collection;
import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.handlers.ModConfig;

public class InjectorRecipeCategory implements IRecipeCategory {
	
	public static final String UID = Ressources.MODID+"injector";
	private final IDrawableStatic background;

	public InjectorRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(new ResourceLocation(Ressources.MODID,"textures/gui/jei/guiInjector.png"), 0, 0, 166, 65, 0, 0, 0, 0);
	}
	
	@Override
	public void drawAnimations(Minecraft arg0) {

	}

	@Override
	public void drawExtras(Minecraft arg0) {

	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public String getTitle() {
		return "Injector";
	}

	@Override
	public String getUid() {
		return this.UID;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
		if(!(recipeWrapper instanceof InjectorRecipeWrapper))
			return;
		InjectorRecipeWrapper wrapper = ((InjectorRecipeWrapper) recipeWrapper);

		List inputs = wrapper.getInputs();
		int index = 0;
		for(int i = 0; i < wrapper.getInputs().size(); i++) {			
			Object o = wrapper.getInputs().get(i);
			recipeLayout.getItemStacks().init(index, true, 48+i*18, 16);
			if(o instanceof ItemStack) {
				recipeLayout.getItemStacks().set(index, ((ItemStack) o));
			}
			index++;
		}
		
		List outputs = wrapper.getOutputs();
		for(int k = 0; k < wrapper.getOutputs().size(); k++) {			
			Object o = wrapper.getOutputs().get(k);
			recipeLayout.getItemStacks().init(index, true, 118+k*18, 16);
			if(o instanceof ItemStack) {
				recipeLayout.getItemStacks().set(index, ((ItemStack) o));
			}
			index++;
		}
		
		List inputsfl = wrapper.getFluidInputs();
		index = 0;
		for(int i = 0; i < wrapper.getFluidInputs().size(); i++) {			
			Object o = wrapper.getFluidInputs().get(i);
			recipeLayout.getFluidStacks().init(index, true, 5, 1, 14, 48, ModConfig.capacityAsgarditeInjector , true, null);
			recipeLayout.getFluidStacks().set(index, wrapper.getFluidInputs().get(i));
			index++;
		}
	}

}
