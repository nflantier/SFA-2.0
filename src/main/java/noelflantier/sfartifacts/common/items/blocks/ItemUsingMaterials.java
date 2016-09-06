package noelflantier.sfartifacts.common.items.blocks;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class ItemUsingMaterials extends ItemBlockSFA{
	
	public ItemUsingMaterials(Block block, String name) {
		super(block);
		this.setRegistryName(name);
	}
	public ItemUsingMaterials(Block block, String name,  EnumOriented oriented) {
		super(block, oriented);
		this.setRegistryName(name);
	}

	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
		int meta = itemstack.getItemDamage();
    	return super.getUnlocalizedName()+"."+EnumPillarMaterial.values()[meta].getName();
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0 ; i < EnumPillarMaterial.values().length ; i++)
			list.add(new ItemStack(item, 1, i));
	}
}
