package noelflantier.sfartifacts.common.items;

import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.items.baseclasses.ItemParticle;

public class ItemAsgardianPearl extends ItemParticle{
	public ItemAsgardianPearl() {
		super();
		setUnlocalizedName(Ressources.UL_NAME_ASGARDIAN_PEARL);
		setRegistryName(Ressources.UL_NAME_ASGARDIAN_PEARL);
		setMaxStackSize(16);
	}
}
