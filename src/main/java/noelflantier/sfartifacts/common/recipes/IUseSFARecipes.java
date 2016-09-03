package noelflantier.sfartifacts.common.recipes;

public interface IUseSFARecipes {
	String getUsageName();
	int getEnergy();
	int getFluid();
	Class<? extends RecipeBase> getClassOfRecipe();
}
