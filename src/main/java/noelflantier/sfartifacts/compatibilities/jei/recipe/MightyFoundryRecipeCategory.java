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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.handlers.ModConfig;

public class MightyFoundryRecipeCategory implements IRecipeCategory {
	
	public static final String UID = Ressources.MODID+"mightyfoundry";
	private final IDrawableStatic background;
	private IDrawable energy;
	private IGuiHelper guiH;
	private int energyValue = 0;

	public MightyFoundryRecipeCategory(IGuiHelper guiHelper) {
		guiH = guiHelper;
		background = guiHelper.createDrawable(new ResourceLocation(Ressources.MODID,"textures/gui/jei/guiMightyFoundry.png"), 0, 0, 166, 80, 0, 0, 0, 0);
	}
	
	@Override
	public void drawAnimations(Minecraft arg0) {

	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		energy.draw(minecraft, 0, 0);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public String getTitle() {
		return "Mighty Foundry";
	}

	@Override
	public String getUid() {
		return this.UID;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
		if(!(recipeWrapper instanceof MightyFoundryRecipeWrapper))
			return;
		MightyFoundryRecipeWrapper wrapper = ((MightyFoundryRecipeWrapper) recipeWrapper);
		energyValue = wrapper.energy;
		int h = (int)Math.ceil(((double)energyValue/(double)ModConfig.capacityMightyFoundry)*47);
		energy = guiH.createDrawable(new ResourceLocation(Ressources.MODID,"textures/gui/jei/guiMightyFoundry.png"),
				166, 0, 14, h, 3+47-h, 0, 19, 0);

		List inputs = wrapper.getInputs();
		recipeLayout.getItemStacks().init(0, true, 37, 2);
		recipeLayout.getItemStacks().set(0, ((ItemStack) wrapper.getInputs().get(0)));
		System.out.println("fdf "+((ItemStack)wrapper.getInputs().get(0)).getMetadata());
		
		int index = 1;
		for(int i = 1; i < wrapper.getInputs().size(); i++) {			
			Object o = wrapper.getInputs().get(i);
			recipeLayout.getItemStacks().init(index, true, 91+(i-1)*18, 2);
			if(o instanceof ItemStack) {
				recipeLayout.getItemStacks().set(index, ((ItemStack) o));
			}
			index++;
		}
		
		List outputs = wrapper.getOutputs();
		for(int k = 0; k < wrapper.getOutputs().size(); k++) {			
			Object o = wrapper.getOutputs().get(k);
			recipeLayout.getItemStacks().init(index, true, 141, 54);
			if(o instanceof ItemStack) {
				recipeLayout.getItemStacks().set(index, ((ItemStack) o));
			}
			index++;
		}
	}

}
