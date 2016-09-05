package noelflantier.sfartifacts.compatibilities.jei.handler;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.recipes.RecipeMold;
import noelflantier.sfartifacts.compatibilities.jei.recipe.MoldRecipeWrapper;

public class MoldRecipeJEIHandler implements IRecipeHandler<RecipeMold> {

	@Override
	public String getRecipeCategoryUid() {
		return Ressources.MODID+"mold";
	}

	@Override
	public String getRecipeCategoryUid(RecipeMold recipe) {
		return getRecipeCategoryUid();
	}

	@Override
	public Class getRecipeClass() {
		return RecipeMold.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(RecipeMold recipe) {
		return new MoldRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(RecipeMold recipe) {
		return true;
	}

}
