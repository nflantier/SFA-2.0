package noelflantier.sfartifacts.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModGUIs;

public class ItemManual extends ItemSFA{

	public ItemManual() {
		super();
		this.setMaxStackSize(1);
		setUnlocalizedName(Ressources.UL_NAME_MANUAL);
		setRegistryName(Ressources.UL_NAME_MANUAL);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
		playerIn.openGui(SFArtifacts.instance, ModGUIs.guiIDManual, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
}
