package noelflantier.sfartifacts.common.items;

import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeMold;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.MoldRecipesHandler;

public class ItemMold extends ItemSFA{
	
	public ItemMold() {
		setUnlocalizedName(Ressources.UL_NAME_MOLD);
		setRegistryName(Ressources.UL_NAME_MOLD);
		this.setMaxStackSize(1);
		this.hasSubtypes = true;
	}
	
	@Override
    public int getMetadata(int meta){
		return meta;
    }

	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
		if(hand.equals(EnumHand.OFF_HAND))
			return new ActionResult(EnumActionResult.FAIL, itemStackIn);
		if(!worldIn.isRemote)
			playerIn.openGui(SFArtifacts.instance, ModGUIs.guiIDMold, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		
		ItemStack it1 = new ItemStack(item, 1, 0);
		it1.setItemDamage(0);
		it1 = ItemNBTHelper.setInteger(it1, "idmold", 0);//0 nothing   -1 invalid    other>0 valid
		it1 = ItemNBTHelper.setIntegerArray(it1, "moldstructure", new int[]{0,0,0,0,0,0,0,0,0});
		list.add(it1);
		for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(MoldRecipesHandler.USAGE_MOLD).entrySet()){
			int m = RecipeMold.class.cast(entry.getValue()).getMoldMeta();
			ItemStack it0 = new ItemStack(item, 1, m);
			it0.setItemDamage(m);
			it0 = ItemNBTHelper.setInteger(it0, "idmold", m);
			it0 = ItemNBTHelper.setIntegerArray(it0, "moldstructure", RecipeMold.class.cast(entry.getValue()).getTabShape().clone());
			list.add(it0);
		}
	}


	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player){
		if(player.openContainer.windowId==ModGUIs.guiIDMold){
			return false;
		}
		return true;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		super.addInformation(stack, player, list, par4);
		int tid = ItemNBTHelper.getInteger(stack, "idmold", 0);
		if(tid==-1)
			list.add(String.format("Mold : Invalid"));
		else if(tid==0)
			list.add(String.format("Mold : Empty"));
		else if(tid>0)
			list.add(String.format("Mold : "+RecipesRegistry.instance.getNameWithMeta(tid)));
	}

}
