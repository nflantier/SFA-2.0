package noelflantier.sfartifacts.common.tileentities;

import net.minecraft.item.ItemStack;

public interface ISFAInventory {

	boolean isInventoryDroppedOnBreaking();
	ItemStack[] getInventory();
}
