package noelflantier.sfartifacts.common.recipes.handler;

import noelflantier.sfartifacts.common.recipes.AUsageRecipes;
import noelflantier.sfartifacts.common.recipes.RecipeBase;

public class LiquefierRecipesHandler extends AUsageRecipes{

	public static final String FILE_NAME_LIQUEFIER = "LiquefierRecipes.xml";
	public static final String USAGE_LIQUEFIER = "Liquefier";
	
	public LiquefierRecipesHandler() {
		super(FILE_NAME_LIQUEFIER, USAGE_LIQUEFIER);
	}

	static final LiquefierRecipesHandler instance = new LiquefierRecipesHandler();
	public static LiquefierRecipesHandler getInstance() {
		return instance;
	}

	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeBase.class;
	}
	
	@Override
	public void loadRecipes() {
		super.loadRecipes();
	}
}
