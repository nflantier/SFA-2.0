package noelflantier.sfartifacts.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.items.baseclasses.ItemParticle;

public class ItemMightyFeather  extends ItemParticle{
	public ItemMightyFeather() {
		super();
		setUnlocalizedName(Ressources.UL_NAME_MIGHTY_FEATHER);
		setRegistryName(Ressources.UL_NAME_MIGHTY_FEATHER);
	}
}
