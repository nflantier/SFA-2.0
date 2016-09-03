package noelflantier.sfartifacts.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.Ressources;

public class ItemGlassCutter extends ItemSFA{

	public ItemGlassCutter() {
		super();
		setUnlocalizedName(Ressources.UL_NAME_GLASS_CUTTER);
		setRegistryName(Ressources.UL_NAME_GLASS_CUTTER);
		setMaxStackSize(1);
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack){
        return new ItemStack(this);
    }

	@Override
	public boolean hasContainerItem(ItemStack stack){
        return true;
    }
}
