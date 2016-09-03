package noelflantier.sfartifacts.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.capabilities.CapabilityPlayerProvider;
import noelflantier.sfartifacts.common.handlers.capabilities.IPlayerData;

public class ItemHulkFlesh extends ItemFood{

	public static int tickHulkEffect = 1500;
	    
	public ItemHulkFlesh(){
        super(100,100,false);
		setUnlocalizedName(Ressources.UL_NAME_HULK_FLESH);
		setRegistryName(Ressources.UL_NAME_HULK_FLESH);
		setAlwaysEdible();
		setCreativeTab(SFArtifacts.sfTabs);
		MinecraftForge.EVENT_BUS.register(this);
    }
	
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack){
        return true;
    }

    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.EPIC;
    }   
    
	@SubscribeEvent
	public void LivingFallEvent(LivingFallEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(player == null || event.getEntityLiving().worldObj.isRemote)
				return;
			if(player.hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null)){
	    		if(player.getCapability(CapabilityPlayerProvider.PLAYER_DATA, null).getInt("tickHasHulkFleshEffect")>0){
					event.setDistance(0);
				}
	    	}
		}
	}
	
	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
	    	EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(player == null || !event.getEntityLiving().worldObj.isRemote)
				return;

	    	if(player.hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null)){
	    		if(player.getCapability(CapabilityPlayerProvider.PLAYER_DATA, null).getInt("tickHasHulkFleshEffect")>0){
					player.motionY += 1.45;
					player.motionX *= 10.2;
					player.motionZ *= 10.2;
				}
	    	}
		}
	}
	
    @Override
	public int getMaxItemUseDuration(ItemStack stack){
        return 64;
    }
    
    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer playerIn){
    	if(playerIn.hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null)){
    		IPlayerData ipd = playerIn.getCapability(CapabilityPlayerProvider.PLAYER_DATA, null);
    		if(ipd.getInt("tickJustEatHulkFlesh")<=0){
    			ipd.setInt("tickHasHulkFleshEffect", tickHulkEffect);
    			ipd.setInt("tickJustEatHulkFlesh", 10);
    			if (!world.isRemote){
    				playerIn.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("regeneration"), tickHulkEffect, 20));
    				playerIn.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("resistance"), tickHulkEffect, 1));
    				playerIn.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("fire_resistance"), tickHulkEffect, 1));
    				playerIn.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("strength"), tickHulkEffect, 10));
    				playerIn.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("speed"), tickHulkEffect, 6));
    				playerIn.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("health_boost"), tickHulkEffect, 9));
    	        }
    		}
    	}    	
        super.onFoodEaten(stack, world, playerIn);
    }
}
