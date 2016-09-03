package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import noelflantier.sfartifacts.common.tileentities.TileControlPanel;

public class ContainerControlPanel extends Container {

	private TileControlPanel tile;
	
	public ContainerControlPanel(InventoryPlayer inventory, TileControlPanel tile) {
		this.tile = tile;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
