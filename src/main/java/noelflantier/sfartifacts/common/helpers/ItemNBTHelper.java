package noelflantier.sfartifacts.common.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemNBTHelper {

	
	public static NBTTagCompound getCompound(ItemStack stack){
		if (stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
		return stack.getTagCompound();
	}	

	public static ItemStack setIntegerArray(ItemStack stack, String tag, int [] i)
	{
		NBTTagCompound compound = getCompound(stack);
		compound.setIntArray(tag, i);
		stack.setTagCompound(compound);
		return stack;
	}
	
	public static ItemStack setInteger(ItemStack stack, String tag, int i)
	{
		NBTTagCompound compound = getCompound(stack);
		compound.setInteger(tag, i);
		stack.setTagCompound(compound);
		return stack;
	}

	public static ItemStack setDouble(ItemStack stack, String tag, double i)
	{
		NBTTagCompound compound = getCompound(stack);
		compound.setDouble(tag, i);
		stack.setTagCompound(compound);
		return stack;
	}

	public static ItemStack setLong(ItemStack stack, String tag, long i)
	{
		NBTTagCompound compound = getCompound(stack);
		compound.setLong(tag, i);
		stack.setTagCompound(compound);
		return stack;
	}
	
	public static ItemStack setBoolean(ItemStack stack, String tag, boolean b)
	{
		NBTTagCompound compound = getCompound(stack);
		compound.setBoolean(tag, b);
		stack.setTagCompound(compound);
		return stack;
	}
	
	public static ItemStack setString(ItemStack stack, String tag, String str)
	{
		NBTTagCompound compound = getCompound(stack);
		compound.setString(tag, str);
		stack.setTagCompound(compound);
		return stack;
	}

	
	public static ItemStack setTagList(ItemStack stack, String ts, NBTTagList tag)
	{
		NBTTagCompound compound = getCompound(stack);
		compound.setTag(ts, tag);
		stack.setTagCompound(compound);
		return stack;
	}
	
	public static boolean verifyExistance(ItemStack stack, String tag) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null)
			return false;
		else
			return stack.getTagCompound().hasKey(tag);
	}

	public static int[] getIntegerArray(ItemStack stack, String tag, int []defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getTagCompound().getIntArray(tag) : defaultExpected;
	}
	
	public static int getInteger(ItemStack stack, String tag, int defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getTagCompound().getInteger(tag) : defaultExpected;
	}

	public static double getDouble(ItemStack stack, String tag, int defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getTagCompound().getDouble(tag) : defaultExpected;
	}

	public static long getLong(ItemStack stack, String tag, long defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getTagCompound().getLong(tag) : defaultExpected;
	}
	
	public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getTagCompound().getBoolean(tag) : defaultExpected;
	}
	
	public static String getString(ItemStack stack, String tag, String defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getTagCompound().getString(tag) : defaultExpected;
	}
	
	public static NBTTagList getTagList(ItemStack stack, String tag, int lvl) {
		return verifyExistance(stack, tag) ? stack.getTagCompound().getTagList(tag, lvl) :new NBTTagList();
	}
}
