package noelflantier.sfartifacts.common.items.baseclasses;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.items.ItemSFA;

public class ItemHammerBase extends ItemSFA{
	
	public ItemHammerBase(){
		super();
		this.setMaxStackSize(1);
	}
	
    @SideOnly(Side.CLIENT)
    public boolean isFull3D(){
        return true;
    }
}
