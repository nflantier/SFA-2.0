package noelflantier.sfartifacts.common.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;

public class ItemVibraniumAlloySheet  extends ItemSFA{

	public ItemVibraniumAlloySheet() {
		super();
		setUnlocalizedName(Ressources.UL_NAME_VIBRANIUM_ALLOY_SHEET);
		setRegistryName(Ressources.UL_NAME_VIBRANIUM_ALLOY_SHEET);
	}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }
}
