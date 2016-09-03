package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.List;

import noelflantier.sfartifacts.common.handlers.RecipeHandler;

public abstract class AUsageRecipes {
	private final List<ISFARecipe> recipes = new ArrayList<ISFARecipe>();
	private final String fileName;
	private final String usageName;
	private CustomHandler customHandler;
	  
	public AUsageRecipes(String fileName, String usageName) {  
		this.fileName = fileName;  
		this.usageName = usageName;
		this.customHandler = createCustomHandler();
	}
	
	public CustomHandler createCustomHandler(){
		return null;
	}
	
	public void loadRecipes() {
	    RecipeHandler rh = RecipeHandler.loadRecipeConfig(fileName, customHandler, getClassOfRecipe());
	    if(rh != null) {
	    	recipes.addAll(rh.recipes);
			RecipesRegistry.instance.registerRecipes(usageName,this);
	    } else {
	    	System.out.println("Could not load recipes for "+usageName);
	    }
	}
	
	public List<ISFARecipe> getRecipes() {
		return recipes;
	}

	public abstract Class<? extends RecipeBase> getClassOfRecipe();
}
