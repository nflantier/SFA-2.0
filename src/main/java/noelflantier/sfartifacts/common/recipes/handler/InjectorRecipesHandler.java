package noelflantier.sfartifacts.common.recipes.handler;

import noelflantier.sfartifacts.common.recipes.AUsageRecipes;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeDummyInjector;

public class InjectorRecipesHandler extends AUsageRecipes{
	public static final String FILE_NAME_INJECTOR = "InjectorRecipes.xml";
	public static final String USAGE_INJECTOR = "Injector";
		
	public InjectorRecipesHandler() {
		super(FILE_NAME_INJECTOR, USAGE_INJECTOR);
	}
	
	public static final InjectorRecipesHandler instance = new InjectorRecipesHandler();
	public static InjectorRecipesHandler getInstance() {
		return instance;
	}

	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeDummyInjector.class;
	}
	
	@Override
	public void loadRecipes() {
		super.loadRecipes();
	}
	
}
