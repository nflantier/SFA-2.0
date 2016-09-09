package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import noelflantier.sfartifacts.common.tileentities.ATileSFA;
import noelflantier.sfartifacts.common.tileentities.TileInductor;

public class ContainerInductor extends Container {

	TileInductor tile;
	
	public ContainerInductor(InventoryPlayer inventory, TileInductor tile) {
		this.tile = tile;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile != null && player != null? ((ATileSFA)tile).isUseableByPlayer(player) : false;
	}
}
