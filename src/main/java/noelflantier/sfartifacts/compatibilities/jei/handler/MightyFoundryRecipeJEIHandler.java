package noelflantier.sfartifacts.compatibilities.jei.handler;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeMightyFoundry;
import noelflantier.sfartifacts.compatibilities.jei.recipe.InjectorRecipeWrapper;
import noelflantier.sfartifacts.compatibilities.jei.recipe.MightyFoundryRecipeWrapper;

public class MightyFoundryRecipeJEIHandler implements IRecipeHandler<RecipeMightyFoundry> {

	@Override
	public String getRecipeCategoryUid() {
		return Ressources.MODID+"mightyfoundry";
	}

	@Override
	public String getRecipeCategoryUid(RecipeMightyFoundry recipe) {
		return getRecipeCategoryUid();
	}

	@Override
	public Class getRecipeClass() {
		return RecipeMightyFoundry.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(RecipeMightyFoundry recipe) {
		return new MightyFoundryRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(RecipeMightyFoundry recipe) {
		return true;
	}

}
