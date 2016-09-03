package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import noelflantier.sfartifacts.common.tileentities.TileInductor;

public class ContainerInductor extends Container {

	public ContainerInductor(InventoryPlayer inventory, TileInductor tile) {
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
