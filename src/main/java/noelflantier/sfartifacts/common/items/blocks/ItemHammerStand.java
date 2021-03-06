package noelflantier.sfartifacts.common.items.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;

public class ItemHammerStand extends ItemBlockSFA{

	public ItemHammerStand(Block block) {
		super(block);
		this.setRegistryName(Ressources.UL_NAME_HAMMER_STAND);
	}

	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
		int meta = itemstack.getItemDamage();
    	return super.getUnlocalizedName()+"."+Ressources.UL_NAME_TYPE_HAMMERSTAND[meta];
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
	}

}
