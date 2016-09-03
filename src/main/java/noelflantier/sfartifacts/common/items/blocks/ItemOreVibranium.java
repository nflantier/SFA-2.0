package noelflantier.sfartifacts.common.items.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.items.blocks.ItemBlockSFA.EnumOriented;

public class ItemOreVibranium extends ItemBlockSFA{

	public ItemOreVibranium(Block block) {
		super(block, EnumOriented.NONE);
		setRegistryName(Ressources.UL_NAME_ORE_VIBRANIUM);
	}

	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
		int meta = itemstack.getItemDamage();
    	return super.getUnlocalizedName()+"."+Ressources.UL_NAME_TYPE_VIBRANIUM[meta<15?meta<8?meta<1?0:1:2:3];
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1,15 ));
	}
}
