package noelflantier.sfartifacts.common.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public interface ITileGlobalNBT {
	default public void writeToItem(ItemStack stack){
	    if(stack == null) {
	        return;
	    }
	    if(!stack.hasTagCompound()) {
	        stack.setTagCompound(new NBTTagCompound());
	    }
	    NBTTagCompound root = writeToNBT(new NBTTagCompound());
	    root.removeTag("x");
	    root.removeTag("y");
	    root.removeTag("z");
        root.removeTag("id");
        root.removeTag("facing");
        //stack.getTagCompound().merge(root);
	    stack.setTagInfo("BlockEntityTag", root);
	}

	default public void readFromItem(ItemStack stack){
	    if(stack == null || !stack.hasTagCompound()) {
	        return;
	    }
	    readFromNBT(stack.getTagCompound());
	}
	
	NBTTagCompound writeToNBT(NBTTagCompound nbt);
	void readFromNBT(NBTTagCompound nbt);
}
