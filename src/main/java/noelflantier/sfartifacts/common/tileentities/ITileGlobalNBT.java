package noelflantier.sfartifacts.common.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ITileGlobalNBT {
	default public void writeToItem(ItemStack stack){
	    if(stack == null) {
	        return;
	    }
	    if(!stack.hasTagCompound()) {
	        stack.setTagCompound(new NBTTagCompound());
	    }
	    NBTTagCompound root = writeToNBTItem(new NBTTagCompound());
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
	    readFromNBTItem(stack.getTagCompound());
	}
	
	NBTTagCompound writeToNBTItem(NBTTagCompound nbt);
	void readFromNBTItem(NBTTagCompound nbt);
}
