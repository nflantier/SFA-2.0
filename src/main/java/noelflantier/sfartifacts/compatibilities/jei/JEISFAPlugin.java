package noelflantier.sfartifacts.compatibilities.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.recipes.handler.InjectorRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.MightyFoundryRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.MoldRecipesHandler;
import noelflantier.sfartifacts.compatibilities.jei.handler.InjectorRecipeJEIHandler;
import noelflantier.sfartifacts.compatibilities.jei.handler.MightyFoundryRecipeJEIHandler;
import noelflantier.sfartifacts.compatibilities.jei.handler.MoldRecipeJEIHandler;
import noelflantier.sfartifacts.compatibilities.jei.recipe.InjectorRecipeCategory;
import noelflantier.sfartifacts.compatibilities.jei.recipe.MightyFoundryRecipeCategory;
import noelflantier.sfartifacts.compatibilities.jei.recipe.MoldRecipeCategory;

@JEIPlugin
public class JEISFAPlugin implements IModPlugin{

	@Override
	public void onRuntimeAvailable(IJeiRuntime arg0) {
		
	}

	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		registry.addRecipeCategories(
			new InjectorRecipeCategory(jeiHelpers.getGuiHelper()),
			new MightyFoundryRecipeCategory(jeiHelpers.getGuiHelper()),
			new MoldRecipeCategory(jeiHelpers.getGuiHelper())
		);
		registry.addRecipeHandlers(
			new InjectorRecipeJEIHandler(),
			new MightyFoundryRecipeJEIHandler(),
			new MoldRecipeJEIHandler()
		);

		registry.addRecipes(InjectorRecipesHandler.getInstance().getRecipes());
		registry.addRecipes(MightyFoundryRecipesHandler.getInstance().getRecipes());
		registry.addRecipes(MoldRecipesHandler.getInstance().getRecipes());
		
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockInjector), InjectorRecipeCategory.UID);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockMightyFoundry), MightyFoundryRecipeCategory.UID);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModItems.itemMold), MoldRecipeCategory.UID);
		
	}

}
