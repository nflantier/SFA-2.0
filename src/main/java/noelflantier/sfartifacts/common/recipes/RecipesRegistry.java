package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import noelflantier.sfartifacts.common.items.ItemMold;
import noelflantier.sfartifacts.common.recipes.handler.MightyFoundryRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.MoldRecipesHandler;

public class RecipesRegistry {
	public static final RecipesRegistry instance = new RecipesRegistry();
	private final Map<String, Map<String, ISFARecipe>> recipes = new HashMap<String, Map<String, ISFARecipe>>();// name machine => name recipe -> recipe

	public void registerRecipe(String usageName, ISFARecipe recipe){
		getRecipesForUsage(usageName).put(recipe.getUid(), recipe);
	}

	public void registerRecipes(String usageName, AUsageRecipes container){
		for(ISFARecipe ir: container.getRecipes()){
			registerRecipe(usageName, ir);
		}
	}

	public ISFARecipe getRecipeForUsage(String usageName, String recipe) {
		ISFARecipe re = null;
		if(recipes.get(usageName)!=null)
			re = recipes.get(usageName).get(recipe);
		return re;
	}
	
	public Map<String, ISFARecipe> getRecipesForUsage(String usageName) {
		Map<String, ISFARecipe> res = recipes.get(usageName);
		if(res == null) {
			res = new HashMap<String, ISFARecipe>();
			recipes.put(usageName, res);
		}
		return res;
	}
	
	public List<RecipeInput> getInputFromEntityItem(List<EntityItem> items){
		List<RecipeInput> ri = null;
		if(items.size()>0){
			ri = new ArrayList<RecipeInput>();
			for(EntityItem ei: items){
				ri.add(new RecipeInput(ei.getEntityItem()));
			}
		}
		return ri;
	}
	public RecipeInput getInputFromItemStack(ItemStack it){
		RecipeInput ri = new RecipeInput();
		if(it == null)
			return ri;
		ri.setItemStack(it);
		return ri;
	}
	public List<RecipeOutput> getOutputFromItemStacks(List<ItemStack> list){
		List<RecipeOutput> ro = null;
		if(list==null || list.isEmpty())
			return ro;
		
		ro = new ArrayList<RecipeOutput>();
		for(ItemStack ei: list){
			if(ei!=null)
				ro.add(new RecipeOutput(ei));
		}
		return ro;
	}
	public List<RecipeInput> getInputFromItemStacks(List<ItemStack> list){
		List<RecipeInput> ri = null;
		if(list==null || list.isEmpty())
			return ri;
		
		ri = new ArrayList<RecipeInput>();
		for(ItemStack ei: list){
			if(ei!=null)
				ri.add(new RecipeInput(ei));
		}
		return ri;
	}	
	public List<RecipeInput> getInputFromFluidStacks(List<FluidStack> list){
		List<RecipeInput> ri = null;
		if(list==null || list.isEmpty())
			return ri;
		
		ri = new ArrayList<RecipeInput>();
		for(FluidStack fl: list){
			if(fl!=null){
				ri.add(new RecipeInput(fl.copy()));
			}
		}
		return ri;
	}
	
	public ISFARecipe getBestRecipeWithItemStacks(IUseSFARecipes iuse, List<ItemStack> inputs){
		return inputs!=null && inputs.size()>0?getBestRecipe(iuse, getInputFromItemStacks(inputs)):null;
	}
	public ISFARecipe getBestRecipe(IUseSFARecipes iuse, List<RecipeInput> inputs){
		return inputs!=null && inputs.size()>0?getBestRecipe(iuse.getUsageName(), inputs, iuse.getEnergy(), iuse.getFluid()):null;
	}
	public List<ISFARecipe> getRecipesWithItemStacks(IUseSFARecipes iuse, List<ItemStack> inputs){
		return inputs!=null && inputs.size()>0?getRecipes(iuse, getInputFromItemStacks(inputs)):null;
	}
	public List<ISFARecipe> getRecipes(IUseSFARecipes iuse, List<RecipeInput> inputs){
		return inputs!=null && inputs.size()>0?getRecipes(iuse.getUsageName(), inputs, iuse.getEnergy(), iuse.getFluid()):null;
	}
	public List<ISFARecipe> getOrderedRecipes(IUseSFARecipes iuse, List<RecipeInput> inputs){
		return inputs!=null && inputs.size()>0?getOrderedRecipes(iuse.getUsageName(), inputs, iuse.getEnergy(), iuse.getFluid()):null;
	}
	public List<ISFARecipe> getOrderedRecipesWithItemStacks(IUseSFARecipes iuse, List<ItemStack> inputs) {
		return inputs!=null && inputs.size()>0?getOrderedRecipes(iuse, getInputFromItemStacks(inputs)):null;
	}
	public List<ISFARecipe> getRecipesWithItemStacksAndFluidStacks(IUseSFARecipes iuse, List<ItemStack> inputs, List<FluidStack> fluids) {
		List<RecipeInput> in = getInputFromItemStacks(inputs);
		in.addAll(getInputFromFluidStacks(fluids));
		return getRecipes(iuse.getUsageName(), in, iuse.getEnergy(), iuse.getFluid());
	}
	public ISFARecipe getBestRecipesWithItemStacksAndFluidStacks(IUseSFARecipes iuse, List<ItemStack> inputs, List<FluidStack> fluids) {
		List<ISFARecipe> r = getRecipesWithItemStacksAndFluidStacks(iuse, inputs, fluids);
		return r!=null?!r.isEmpty()?r.get(0):null:null;
	}
	
	public List<ISFARecipe> getOrderedRecipes(String usageName, List<RecipeInput> inputs, int energy, int fluid){
		if(inputs==null || inputs.isEmpty())
			return null;
		
		List<ISFARecipe> r = getRecipes(usageName, inputs, energy, fluid);
		if(r!=null && !r.isEmpty()){
			Comparator<ISFARecipe> comparator = (i1,i2)-> i1.getInputs().size()-i2.getInputs().size();
			r.sort(comparator.reversed());
		}
		return r;
	}

	public boolean isRecipeCanBeDone(ISFARecipe recipe, List<ItemStack> inputs, IUseSFARecipes iuse){
		return isRecipeCanBeDone(recipe,  getInputFromItemStacks(inputs), iuse.getEnergy(),iuse.getFluid());
	}
	public boolean isRecipeCanBeDone(ISFARecipe recipe, List<RecipeInput> inputs, int energy, int fluid){
		return recipe.isStacksInputs(inputs) && recipe.getEnergyCost()<=energy && recipe.getFluidCost()<=fluid;
	}
	
	public List<ISFARecipe> getRecipes(String usageName, List<RecipeInput> inputs, int energy, int fluid){
		if(inputs==null || inputs.isEmpty())
			return null;
		List<ISFARecipe> list = getRecipesForUsageAndInputs(usageName, inputs);
		if(list == null || list.size()<=0)
			return null;
		Predicate<ISFARecipe> predicate = (r) -> r.getEnergyCost() > energy || r.getFluidCost() > fluid;
		list.removeIf(predicate);
		if(list.isEmpty())
			list = null;
		return list;
	}
	
	public ISFARecipe getBestRecipe(String usageName, List<RecipeInput> inputs, int energy, int fluid){
		if(inputs==null || inputs.isEmpty())
			return null;
		ISFARecipe daRecipe =null;
		List<ISFARecipe> list = getRecipes(usageName, inputs, energy, fluid);
		if(list==null || list.size()<=0)
			return daRecipe;
		int size = 0;
		for(int i = 0;i < list.size();i++){
			int psize = list.get(i).getInputs().size();
			if(psize>size){
				size = psize;
				daRecipe = list.get(i);
			}
		}
		return daRecipe;
	}

	public ISFARecipe getFirstRecipeForUsageAndOutputsItemStacks(String usageName,List<ItemStack> outputs){
		return getFirstRecipeForUsageAndOutputs(usageName, getOutputFromItemStacks(outputs));
	}
	public ISFARecipe getFirstRecipeForUsageAndOutputs(String usageName,List<RecipeOutput> outputs){
		if(outputs==null)
			return null;
		List<ISFARecipe> r = getRecipesForUsageAndOutputs(usageName, outputs);
		return r!=null?r.get(0):null;
	}
	public List<ISFARecipe> getRecipeForUsageAndOutputsItemStacks(String usageName,List<ItemStack> outputs){
		return getRecipesForUsageAndOutputs(usageName, getOutputFromItemStacks(outputs));
	}
	public List<ISFARecipe> getRecipesForUsageAndOutputs(String usageName,List<RecipeOutput> outputs){
		ArrayList<ISFARecipe> flag = new ArrayList<ISFARecipe>();
		for (Map.Entry<String, ISFARecipe> entry : instance.getRecipesForUsage(usageName).entrySet()){
			if(entry.getValue().isStacksOutputs(outputs)){
				flag.add(entry.getValue());
			}
		}
		return flag.size()>0?flag:null;
	}
	public static List<ISFARecipe> getRecipesForUsageAndInputs(String usageName, List<RecipeInput> inputs){
		ArrayList<ISFARecipe> flag = new ArrayList<ISFARecipe>();
		for (Map.Entry<String, ISFARecipe> entry : instance.getRecipesForUsage(usageName).entrySet()){
			if(entry.getValue().isStacksInputs(inputs)){
				flag.add(entry.getValue());
			}
		}
		return flag.size()>0?flag:null;
	}

	public boolean canRecipeStackItem(ISFARecipe recipe, List<ItemStack> outputStacks) {
		int sizeitem = 0;
		for(RecipeOutput ro : recipe.getOutputs()){
			if(ro.isItem()){
				sizeitem+=1;
				ro.canStackWithElement(outputStacks);
			}
		}
		return outputStacks!=null && !outputStacks.isEmpty() && sizeitem<=outputStacks.size();
	}

	public FluidStack canRecipeStackTank(ISFARecipe recipe, FluidTank tank) {
		for(RecipeOutput ro : recipe.getOutputs()){
			if(ro.isFluid() && ro.getFluidStack().getFluid()!=null && (tank.getFluid()==null || (ro.getFluidStack().isFluidEqual(tank.getFluid()) && ro.getFluidStack().amount+tank.getFluidAmount()<=tank.getCapacity()))){
				return ro.getFluidStack();
			}
		}
		return null;
	}
	
	//MOLD
	public RecipeMold getMoldWithShap(int[] shape){
		for (Map.Entry<String, ISFARecipe> entry : instance.getRecipesForUsage(MoldRecipesHandler.USAGE_MOLD).entrySet()){
			if(RecipeMold.class.cast(entry.getValue()).isShapeEquals(shape))
				return RecipeMold.class.cast(entry.getValue());
		}
		return null;
	}
	public String getNameWithMeta(int meta){
		for (Map.Entry<String, ISFARecipe> entry : instance.getRecipesForUsage(MoldRecipesHandler.USAGE_MOLD).entrySet()){
			if(RecipeMold.class.cast(entry.getValue()).getMoldMeta()==meta)
				return entry.getValue().getUid();
		}
		return "";
	}
	
	//MIGHTYFOUNDRY
	public ISFARecipe getRecipeWithMoldMeta(int meta){
		for (Map.Entry<String, ISFARecipe> entry : instance.getRecipesForUsage(MightyFoundryRecipesHandler.USAGE_MIGHTY_FOUNDRY).entrySet()){
			if(entry.getValue() instanceof RecipeMightyFoundry){
				if(RecipeMightyFoundry.class.cast(entry.getValue()).getMold()!=null)
					if(RecipeMightyFoundry.class.cast(entry.getValue()).getMold().getItemDamage()==meta)
						return entry.getValue();
			}
		}
		return null;
	}
	public ItemStack getIngredientsForRecipe(ISFARecipe recipe){
		if(recipe!=null){
			for(RecipeInput ri : recipe.getInputs()){
				if(ri.isItem() && ri.getItemStack().getItem() instanceof ItemMold == false)
					return ri.getItemStack();
			}
		}
		return null;
	}
	public ItemStack getResultForRecipe(ISFARecipe recipe){
		if(recipe!=null){
			for(RecipeOutput ri : recipe.getOutputs()){
				return ri.getItemStack().copy();
			}
		}
		return null;
	}
}
