package noelflantier.sfartifacts.compatibilities.jei.handler;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.recipes.RecipeDummyInjector;
import noelflantier.sfartifacts.compatibilities.jei.recipe.InjectorRecipeWrapper;

public class InjectorRecipeJEIHandler implements IRecipeHandler<RecipeDummyInjector> {

	@Override
	public String getRecipeCategoryUid() {
		return Ressources.MODID+"injector";
	}

	@Override
	public String getRecipeCategoryUid(RecipeDummyInjector recipe) {
		return getRecipeCategoryUid();
	}

	@Override
	public Class getRecipeClass() {
		return RecipeDummyInjector.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(RecipeDummyInjector recipe) {
		return new InjectorRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(RecipeDummyInjector recipe) {
		return true;
	}

}
