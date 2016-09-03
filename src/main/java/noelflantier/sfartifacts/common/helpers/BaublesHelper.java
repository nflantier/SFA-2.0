package noelflantier.sfartifacts.common.helpers;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class BaublesHelper {

	public static boolean hasItemClass(Class<? extends IBauble> cla, IInventory bi){
		int size = bi.getSizeInventory();
		for(int i = 0 ; i < size ; i++){
			if(bi.getStackInSlot(i)!=null && bi.getStackInSlot(i).getItem()!=null && bi.getStackInSlot(i).getItem().getClass()==cla)
				return true;
		}
		return false;
	}
	
	public static void reloadEquippedItems(EntityPlayer player){
		IInventory bi = BaublesApi.getBaubles(player);
		if(bi==null)
			return;
		int size = bi.getSizeInventory();
		for(int i = 0 ; i < size ; i++){
			if(bi.getStackInSlot(i)!=null && bi.getStackInSlot(i).getItem()!=null && bi.getStackInSlot(i).getItem() instanceof IBauble){
				((IBauble)bi.getStackInSlot(i).getItem()).onEquipped(bi.getStackInSlot(i), player);
			}
		}
	}
	
}
