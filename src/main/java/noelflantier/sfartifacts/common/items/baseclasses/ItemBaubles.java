package noelflantier.sfartifacts.common.items.baseclasses;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemSFA;

public class ItemBaubles extends ItemSFA implements IBauble{

	private static final String TAG_HASHCODE = "playerHashcode";
	private static final String TAG_UUID_MOST = "sfaUUIDMost";
	private static final String TAG_UUID_LEAST = "sfaUUIDLeast";
	protected Multimap<String, AttributeModifier> attributes = HashMultimap.create();
	
	public ItemBaubles(){
		super();
	}

	public static int getLastPlayerHashcode(ItemStack stack) {
		return ItemNBTHelper.getInteger(stack, TAG_HASHCODE, 0);
	}

	public static void setLastPlayerHashcode(ItemStack stack, int hash) {
		ItemNBTHelper.setInteger(stack, TAG_HASHCODE, hash);
	}
	
	public static UUID getUUID(ItemStack stack) {
		long most = ItemNBTHelper.getLong(stack, TAG_UUID_MOST, 0);
		if(most == 0) {
			UUID uuid = UUID.randomUUID();
			ItemNBTHelper.setLong(stack, TAG_UUID_MOST, uuid.getMostSignificantBits());
			ItemNBTHelper.setLong(stack, TAG_UUID_LEAST, uuid.getLeastSignificantBits());
			return getUUID(stack);
		}

		long least = ItemNBTHelper.getLong(stack, TAG_UUID_LEAST, 0);
		return new UUID(most, least);
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {		
		if(getLastPlayerHashcode(stack) != player.hashCode()) {
			onEquippedLoad(stack,player);
			setLastPlayerHashcode(stack, player.hashCode());
		}
	}

	public void onEquippedLoad(ItemStack stack, EntityLivingBase player) {
		
	}
	
	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		onEquippedLoad(stack,player);
		setLastPlayerHashcode(stack, player.hashCode());
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		// TODO Auto-generated method stub
		return false;
	}

}
