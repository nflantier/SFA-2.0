package noelflantier.sfartifacts.common.items;


import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;

public class ItemLightningRod extends ItemSFA{
	
	public ItemLightningRod(){
		super();
		setUnlocalizedName(Ressources.UL_NAME_LIGHTNING_ROD);
		setRegistryName(Ressources.UL_NAME_LIGHTNING_ROD);
        setHasSubtypes(true);
	}
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack){
    	int i = itemstack.getItemDamage();
    	if (i < 0 || i >= Ressources.UL_NAME_TYPE_LIGHTNINGROD.length){
        	i = 0;
        }
        return super.getUnlocalizedName()+"."+Ressources.UL_NAME_TYPE_LIGHTNINGROD[i];
    }
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems){
        for (int i = 0; i < Ressources.UL_NAME_TYPE_LIGHTNINGROD.length; ++i)
        {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }
}
