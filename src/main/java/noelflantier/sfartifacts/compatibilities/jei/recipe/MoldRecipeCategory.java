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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.handlers.ModConfig;

public class MoldRecipeCategory implements IRecipeCategory {
	
	public static final String UID = Ressources.MODID+"mold";
	private final IDrawableStatic background;

	public MoldRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(new ResourceLocation(Ressources.MODID,"textures/gui/jei/guiMoldMaking.png"), 0, 0, 166, 166, 0, 0, 0, 0);
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
		return "Mold";
	}

	@Override
	public String getUid() {
		return this.UID;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
		if(!(recipeWrapper instanceof MoldRecipeWrapper))
			return;
		MoldRecipeWrapper wrapper = ((MoldRecipeWrapper) recipeWrapper);

		int[] inv = wrapper.shape;
		int index = 0;
		for(int i = 0 ; i < inv.length ; i++){
			String bin = Integer.toBinaryString(inv[i]);
			int l = 9-bin.length();
			for(int j=0;j<l;j++)
				bin = "0"+bin;
			for(int k=0;k<bin.length();k++){
				if(bin.substring(k, k+1).equals("1")){
					recipeLayout.getItemStacks().init(index, true, 3+k*18, 3+i*18);
					recipeLayout.getItemStacks().set(index, new ItemStack(Blocks.SAND,1));
					index++;
				}
			}
		}
	}

}
