package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import noelflantier.sfartifacts.common.tileentities.ATileSFA;
import noelflantier.sfartifacts.common.tileentities.TileMachine;

public class ContainerMachine  extends Container{
	public TileMachine tmachine;

	public ContainerMachine(InventoryPlayer inventory,TileMachine tile){
		this.tmachine = tile;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tmachine != null && player != null? ((ATileSFA)tmachine).isUseableByPlayer(player) : false;
	}
}
