package noelflantier.sfartifacts.common.recipes;

import net.minecraft.item.ItemStack;

public class RecipeMightyFoundry extends RecipeBase{

	private int setTickPerItem = 0;
	private int itemQuantity = 0;
	private ItemStack mold = null;

	public RecipeMightyFoundry(String uid) {
		super(uid);
	}

	public int getItemQuantity() {
		return itemQuantity;
	}
	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}
	public ItemStack getMold() {
		return mold;
	}
	public void setMold(ItemStack mold) {
		this.mold = mold;
	}
    public int getTickPerItem() {
		return setTickPerItem;
	}
	public void setTickPerItem(int tickPerItem) {
		this.setTickPerItem = tickPerItem;
	}
}
