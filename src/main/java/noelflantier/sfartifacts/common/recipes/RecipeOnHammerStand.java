package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.recipes.RecipeHammerUpgrades.NbtTagToAdd;

public class RecipeOnHammerStand {

	public int age;
	public int maxAge;
	public ISFARecipe recipe;
	public List<EntityItem> itemOnSpot = new ArrayList<EntityItem>();
	public ItemStack hammer;
	public boolean isEnchantBook;
	public boolean isValid = true;

	public RecipeOnHammerStand(ISFARecipe recipe, List<EntityItem> list, ItemStack hammer){
		this.age = 0;
		this.maxAge = 1;
		this.recipe = recipe;
		isEnchantBook = RecipeHammerUpgrades.class.cast(this.recipe).isEnchantBook();
		this.hammer = hammer;
		isValid = addItemSpot(list);
	}
	
	public boolean isDone(){
		return this.age>=this.maxAge;
	}
		
	public boolean itemStillHere(){
		boolean flag = true;
		for (EntityItem item : itemOnSpot){
			if(!item.isEntityAlive())
				flag = false;
		}
		if(!flag)
			itemOnSpot.clear();
		return flag;
	}

	public void end(ItemStack hammer) {
		if(this.isEnchantBook){
			for (RecipeInput ri : recipe.getInputs()){
				for(EntityItem ei:itemOnSpot){
					if(ri.getItemStack().getItem()==ei.getEntityItem().getItem()){
						NBTTagList list = ei.getEntityItem().getTagCompound().getTagList("StoredEnchantments", 10);
						hammer.addEnchantment(Enchantment.getEnchantmentByID(list.getCompoundTagAt(0).getInteger("id")), list.getCompoundTagAt(0).getInteger("lvl"));
						
						if (!hammer.getTagCompound().hasKey("EnchStored", 9))
							hammer.getTagCompound().setTag("EnchStored", new NBTTagList());
						NBTTagList nbttaglist = hammer.getTagCompound().getTagList("EnchStored", 10);
				        NBTTagCompound nbttagcompound = new NBTTagCompound();
				        nbttagcompound.setShort("id", (short)list.getCompoundTagAt(0).getInteger("id"));
				        nbttagcompound.setShort("lvl", (short)((byte)list.getCompoundTagAt(0).getInteger("lvl")));
				        nbttagcompound.setBoolean("enable", true);
				        nbttaglist.appendTag(nbttagcompound);
						ei.getEntityItem().stackSize-=ri.getItemStack().stackSize;
						break;
					}
				}
			}
		}else{
			for(NbtTagToAdd tag :RecipeHammerUpgrades.class.cast(recipe).getNbtTagList()){
				addTagToHammer(tag, hammer);
			}
		}

		for (RecipeInput ri : recipe.getInputs()){
			this.itemOnSpot.stream()
				.filter((i)->i.getEntityItem()!=null 
				&& i.getEntityItem().getItem()==ri.getItemStack().getItem() 
				&& i.getEntityItem().getItemDamage()==ri.getItemStack().getItemDamage())
				.forEach((s)->s.getEntityItem().stackSize -= ri.getItemStack().stackSize);
		}
		itemOnSpot.clear();
		this.age = 0;
	}
	
	public static void addTagToHammer(NbtTagToAdd tag, ItemStack stack){
		if(tag.type.equals("boolean")){
			stack = ItemNBTHelper.setBoolean(stack, tag.name, tag.value.equals("true"));
		}else if(tag.type.equals("int")){
			if(tag.process.equals("adding"))
				stack = ItemNBTHelper.setInteger(stack, tag.name, ItemNBTHelper.getInteger(stack, tag.name, 0)+Integer.parseInt(tag.value));
			else
				stack = ItemNBTHelper.setInteger(stack, tag.name, Integer.parseInt(tag.value));
		}else if(tag.type.equals("double")){
			if(tag.process.equals("adding"))
				stack = ItemNBTHelper.setDouble(stack, tag.name, ItemNBTHelper.getDouble(stack, tag.name, 0)+Double.parseDouble(tag.value));
			else
				stack = ItemNBTHelper.setDouble(stack, tag.name, Double.parseDouble(tag.value));
		}else if(tag.type.equals("long")){
			if(tag.process.equals("adding"))
				stack = ItemNBTHelper.setLong(stack, tag.name, Long.parseLong(tag.value));
			else
				stack = ItemNBTHelper.setLong(stack, tag.name, ItemNBTHelper.getLong(stack, tag.name, 0)+Long.parseLong(tag.value));
		}
	}
	
	public boolean addItemSpot(List<EntityItem> items){
		List<RecipeInput> listr = recipe.getInputs();
		boolean flag = false;
		if(this.isEnchantBook){
			for(EntityItem it:items){
				if(it.getEntityItem().hasTagCompound()){
					NBTTagList list = it.getEntityItem().getTagCompound().getTagList("StoredEnchantments", 10);
					if (list.tagCount() < 1) continue;
					Enchantment enchant = Enchantment.getEnchantmentByID(list.getCompoundTagAt(0).getInteger("id"));
					if(!((ItemThorHammer)hammer.getItem()).isEnchantValid(enchant))continue;
					NBTTagList tl = ItemNBTHelper.getTagList(hammer, "EnchStored", 10);
			        LinkedHashMap linkedhashmap = new LinkedHashMap();
			        if (tl != null){
			    		for (int i = 0; i < tl.tagCount(); ++i)
			    	        {
			    	            short short1 = tl.getCompoundTagAt(i).getShort("id");
			    	            short short2 = tl.getCompoundTagAt(i).getShort("lvl");
			    	            linkedhashmap.put(Integer.valueOf(short1), Integer.valueOf(short2));
			    	        }
			            }
					if(linkedhashmap.containsKey(list.getCompoundTagAt(0).getInteger("id")))continue;
					itemOnSpot.add(it);
					break;
				}
			}
			return itemOnSpot.size()>0;
		}
		
		for (RecipeInput ri : listr){
			items.stream()
				.filter((i)->i.getEntityItem()!=null 
					&& i.getEntityItem().getItem()==ri.getItemStack().getItem() 
					&& i.getEntityItem().getItemDamage()==ri.getItemStack().getItemDamage()
					&& i.getEntityItem().stackSize>=ri.getItemStack().stackSize)
				.forEach(itemOnSpot::add);			
		}
		if(itemOnSpot.size()<=0)
			return false;

		for(NbtTagToAdd tag :RecipeHammerUpgrades.class.cast(recipe).getNbtTagList()){
			if (hammerHasTag(tag, hammer))
				return false;
		}
		
		return itemOnSpot.size()>0;
	}
	
	public boolean hammerHasTag(NbtTagToAdd tag, ItemStack stack){
		if(tag.process.equals("adding"))
			return false;
		if(tag.type.equals("boolean")){
			return ItemNBTHelper.getBoolean(stack, tag.name, false);
		}
		return ItemNBTHelper.verifyExistance(hammer, tag.name);
	}
}
