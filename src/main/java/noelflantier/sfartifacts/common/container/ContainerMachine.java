package noelflantier.sfartifacts.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import noelflantier.sfartifacts.common.tileentities.TileMachine;

public abstract class ContainerMachine  extends Container{
	public TileMachine tmachine;

	public ContainerMachine(InventoryPlayer inventory,TileMachine tile){
		this.tmachine = tile;
	}
	
	public abstract boolean canInteractWith(EntityPlayer player);
}
