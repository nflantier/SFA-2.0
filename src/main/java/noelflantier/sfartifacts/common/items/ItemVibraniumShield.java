package noelflantier.sfartifacts.common.items;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.ShieldHelper;

public class ItemVibraniumShield  extends ItemSFA{

	public static ToolMaterial SHIELD_1 = EnumHelper.addToolMaterial("SHIELD_1", 0, -1, 280.0F, 55.0F, -1);
    public static final AttributeModifier knockbackModifier = (new AttributeModifier( "KnockBack modifier", 100D, 0)).setSaved(false);
	
	public ItemVibraniumShield() {
		super();
		setUnlocalizedName(Ressources.UL_NAME_VIBRANIUM_SHIELD);
		setRegistryName(Ressources.UL_NAME_VIBRANIUM_SHIELD);
		setMaxStackSize(1);
        setHasSubtypes(true);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
		if(hand.equals(EnumHand.OFF_HAND))
			return new ActionResult(EnumActionResult.FAIL, itemStackIn);
		
		if (!ItemNBTHelper.getBoolean(itemStackIn, "IsThrown", false)){
			if(playerIn.isSneaking()){
				return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
			}			
			if(!worldIn.isRemote){
				ItemNBTHelper.setBoolean(itemStackIn, "IsThrown", true);
				int t = itemStackIn.getItemDamage();
				itemStackIn.setItemDamage(15+t);				
				ShieldHelper.startThrowing(worldIn, playerIn, itemStackIn, t);
			}
		}
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

	@Override
    public void onUpdate(ItemStack stack, World w, Entity entity, int p_77663_4_, boolean p_77663_5_) {
		if (ItemNBTHelper.getBoolean(stack, "IsThrown", false))
			return;
		if(!ModConfig.isShieldBlockOnlyWhenShift && !ItemNBTHelper.getBoolean(stack, "CanBlock", false))
			ItemNBTHelper.setBoolean(stack, "CanBlock", true);
		
        if (entity instanceof EntityPlayer && ModConfig.isShieldBlockOnlyWhenShift){
            EntityPlayer player = (EntityPlayer)entity;
			if(player.isSneaking()){
				ItemNBTHelper.setBoolean(stack, "CanBlock", true);
			}else
				if(ItemNBTHelper.getBoolean(stack, "CanBlock", false))
					ItemNBTHelper.setBoolean(stack, "CanBlock", false);
        }
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack it = new ItemStack(item, 1, 0);
		if(!ModConfig.isShieldBlockOnlyWhenShift)
			it = ItemNBTHelper.setBoolean(it, "CanBlock", true);
		else
			it = ItemNBTHelper.setBoolean(it, "CanBlock", false);
		it = ItemNBTHelper.setBoolean(it, "IsThrown", false);
		

		ItemStack itca = new ItemStack(item, 1, 1);
		if(!ModConfig.isShieldBlockOnlyWhenShift)
			itca = ItemNBTHelper.setBoolean(itca, "CanBlock", true);
		else
			itca = ItemNBTHelper.setBoolean(itca, "CanBlock", false);
		itca = ItemNBTHelper.setBoolean(itca, "IsThrown", false);
		
		list.add(it);
		list.add(itca);
	}

	@Override
    public int getMetadata(int meta){
        return meta;
    }

	@Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase el, EntityLivingBase el2){
        return true;
    }
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if(entity instanceof EntityItem==false)
			entity.attackEntityFrom(DamageSource.causePlayerDamage(player), SHIELD_1.getDamageVsEntity());
		return true;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player){
        return !ItemNBTHelper.getBoolean(stack, "IsThrown", false);
    }

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack){
		if(ItemNBTHelper.getBoolean(stack, "IsThrown", false) || !ItemNBTHelper.getBoolean(stack, "CanBlock", false))
			return super.getAttributeModifiers(slot,stack);
		
        Multimap multimap = super.getAttributeModifiers(slot,stack);
        multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getAttributeUnlocalizedName(), knockbackModifier);
        return multimap;
	}


	@Override
    public String getUnlocalizedName(ItemStack itemstack){
    	return super.getUnlocalizedName(itemstack)+"."+Ressources.UL_NAME_TYPE_SHIELD[itemstack.getItemDamage()>1?0:itemstack.getItemDamage()];
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		super.addInformation(stack, player, list, par4);
		
		if(ModConfig.isShieldBlockOnlyWhenShift)
			list.add(String.format("You have to press shift to block damages."));
	}
}
