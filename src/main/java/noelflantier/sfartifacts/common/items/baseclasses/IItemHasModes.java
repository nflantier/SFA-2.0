package noelflantier.sfartifacts.common.items.baseclasses;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public interface IItemHasModes{
	
	List<ItemMode> getModes();
	
	default void addMode(String nameNbt, String name, String desc){
		getModes().add(new ItemMode(nameNbt, name, desc));
	}	
	
	default void changeMode(ItemStack stack, EntityPlayer player){
		int m = ItemNBTHelper.getInteger(stack, "Mode", 0);
		int nme = m;
		if(m+1>=getModes().size())
			nme = 0;
		else
			nme = m+1;
		
		player.addChatComponentMessage(new TextComponentString(this.getStringChat()+""+getModes().get(nme).name));
		ItemNBTHelper.setInteger(stack, "Mode", nme);
	}
	ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand);
	double getDistanceRay();
	boolean shouldSneak();
	String getStringChat();
	void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4);
}
