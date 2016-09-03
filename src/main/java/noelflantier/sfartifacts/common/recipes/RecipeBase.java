package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeBase implements ISFARecipe{

	private int energyCost = 0;
	private int fluidCost = 0;
	private List<RecipeInput> inputs = new ArrayList<RecipeInput>();
	private List<RecipeOutput> outputs = new ArrayList<RecipeOutput>();
	private final String UID;
	private String label = null;
	private boolean hasOuputs = true;
    
	public RecipeBase(String uid){
    	this.UID = uid;
    }
	
    public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getFluidCost() {
		return fluidCost;
	}
	public void setFluidCost(int fluidCost) {
		this.fluidCost = fluidCost;
	}
	public int getEnergyCost() {
		return energyCost;
	}
	public void setEnergyCost(int energyCost) {
		this.energyCost = energyCost;
	}
    public boolean hasOuputs() {
		return hasOuputs;
	}
	public void setHasOuputs(boolean hasOuputs) {
		this.hasOuputs = hasOuputs;
	}
	public List<RecipeInput> getInputs() {
		return inputs;
	}
	public void setInputs(List<RecipeInput> inputs) {
		this.inputs = inputs;
	}
	public List<RecipeOutput> getOutputs() {
		return outputs;
	}
	public void setOutputs(List<RecipeOutput> outputs) {
		this.outputs = outputs;
	}
	
	@Override
	public String getUid() {
		return UID;
	}

	public void addInputStackOreDict(String ore) {
		RecipeInput r = new RecipeInput();
		r.setOreName(ore);
	}
	public void addInputStack(ItemStack stack) {
		this.inputs.add(new RecipeInput(stack));
	}
	public void addOutputStack(ItemStack stack) {
		this.outputs.add(new RecipeOutput(stack));
	}
	public void addInputFluidStack(FluidStack fluidStack) {
		this.inputs.add(new RecipeInput(fluidStack));
	}
	public void addOutputFluidStack(FluidStack fluidStack) {
		this.outputs.add(new RecipeOutput(fluidStack));
	}
}