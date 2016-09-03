package noelflantier.sfartifacts.common.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;

public class ItemFusionCore extends ItemSFA{
	public ItemFusionCore() {
		super();
		setUnlocalizedName(Ressources.UL_NAME_FUSION_CORE);
		setRegistryName(Ressources.UL_NAME_FUSION_CORE);
	}

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack p_77636_1_){
        return true;
    }
}

