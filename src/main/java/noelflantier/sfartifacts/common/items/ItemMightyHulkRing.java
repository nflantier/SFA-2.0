package noelflantier.sfartifacts.common.items;

import java.util.List;

import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.handlers.capabilities.CapabilityPlayerProvider;
import noelflantier.sfartifacts.common.handlers.capabilities.IPlayerData;
import noelflantier.sfartifacts.common.helpers.BaublesHelper;
import noelflantier.sfartifacts.common.helpers.Utils;
import noelflantier.sfartifacts.common.items.baseclasses.ItemBaubles;

public class ItemMightyHulkRing  extends ItemBaubles implements IBauble{
		
	public ItemMightyHulkRing() {
		super();
		setUnlocalizedName(Ressources.UL_NAME_MIGHTY_HULK_RING);
		setRegistryName(Ressources.UL_NAME_MIGHTY_HULK_RING);
		this.setMaxStackSize(1);
		if(Loader.isModLoaded("Baubles"))
			MinecraftForge.EVENT_BUS.register(this);
		
	}

	@SubscribeEvent
	public void onHarvestCheck(PlayerEvent.HarvestCheck event) {
    	EntityPlayer player = event.getEntityPlayer();
		if(player == null || event.getTargetBlock()==Blocks.BEDROCK || event.getEntityPlayer().getHeldItemMainhand()!=null)
			return;
		IInventory ip = BaublesApi.getBaubles(player);
		if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
			event.setCanHarvest(true);
		}
	}

	@SubscribeEvent
	public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
    	EntityPlayer player = event.getEntityPlayer();
		if(player == null || event.getState().getBlock()==Blocks.BEDROCK || event.getEntityPlayer().getHeldItemMainhand()!=null)
			return;
		IInventory ip = BaublesApi.getBaubles(player);
		if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
			if(event.getState().getMaterial().isToolNotRequired()){
				player.getFoodStats().addExhaustion(0.05F);
			}else
				player.getFoodStats().addExhaustion(0.075F);
			float h = event.getState().getBlockHardness(event.getEntityPlayer().worldObj, event.getPos());
			event.setNewSpeed( h*8.5F<15?15:h*8.5F );
		}
	}

	@SubscribeEvent
	public void LivingFallEvent(LivingFallEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(player == null)
				return;
			
			IInventory ip = BaublesApi.getBaubles(player);
			if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
				event.setDistance( event.getDistance()>14F?(event.getDistance()-14F)/2.5F:0F );
				if(event.getDistance()>0)
					player.getFoodStats().addExhaustion(0.05F);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
	    	EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(player == null || player.isSneaking())
				return;

	    	if(player.hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null)){
	    		IPlayerData ipd = player.getCapability(CapabilityPlayerProvider.PLAYER_DATA, null);
		    	
				if(ipd.getInt("tickHasHulkFleshEffect")>0)
					return;
				IInventory ip = BaublesApi.getBaubles(player);
				if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
					player.getFoodStats().addExhaustion(0.01F);
					player.motionY += 0.5;
					player.motionX *= 4.3;
					player.motionZ *= 4.3;
				}
	    	}
		}
	}	
	
	@SubscribeEvent
	public void onLiving(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
	    	EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(player == null)
				return;
			

	    	if(player.hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null)){
	    		IPlayerData ipd = player.getCapability(CapabilityPlayerProvider.PLAYER_DATA, null);

				if(ipd.getInt("tickHasHulkFleshEffect")>0)
					return;
				IInventory ip = BaublesApi.getBaubles(player);
				if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
					player.addExhaustion(0.02F);

					if (player.getHealth() < player.getMaxHealth()){//REGEN
						if(player.getFoodStats().getFoodLevel()<=0)
							player.heal(0.07F);
						else
							player.heal(0.17F);
					}
						
			    	float f = Utils.getSpeedHoverFluid(player,1.22F);
			    	if(f>0){
			    		player.motionX *= f;
						player.motionZ *= f;
			    	}
				}
	    	}
		}
	}
	
	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
    	EntityPlayer player = event.getEntityPlayer();
		if(player == null || event.getTarget() instanceof EntityLiving==false || event.getEntityPlayer().getHeldItemMainhand()!=null)
			return;
		IInventory ip = BaublesApi.getBaubles(player);
		if(ip!=null && BaublesHelper.hasItemClass(ItemMightyHulkRing.class	, ip)){
			event.getTarget().hitByEntity(event.getEntity());
            float damages = (float)event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            damages = damages>1?damages*5:10;
            int knockback = 4;
            event.getTarget().attackEntityFrom(DamageSource.causePlayerDamage(event.getEntityPlayer()), damages);
            event.getTarget().addVelocity((double)(-MathHelper.sin(event.getEntity().rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F), 0.3D, (double)(MathHelper.cos(event.getEntity().rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F));
			player.getFoodStats().addExhaustion(0.3F);
			event.setCanceled(true);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);
		if(player.stepHeight <1)//STEP
			player.stepHeight = 1F;
		
	}
	
	@Override
	public void onEquippedLoad(ItemStack stack, EntityLivingBase player) {
		attributes.clear();
		fillModifiers(attributes, stack);
		player.getAttributeMap().applyAttributeModifiers(attributes);
	}
	
	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		attributes.clear();
		fillModifiers(attributes, stack);
		player.getAttributeMap().removeAttributeModifiers(attributes);
		if(player instanceof EntityPlayer){
	    	if(player.hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null)){
	    		IPlayerData ipd = player.getCapability(CapabilityPlayerProvider.PLAYER_DATA, null);
	    		ipd.setFloat("changeStep", 0.5F);
	    	}
		}
		player.stepHeight = 0.5F;
		if(player.getHealth()>player.getMaxHealth())
			player.setHealth(player.getMaxHealth());
	}
	   
	void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {
		
		attributes.put(SharedMonsterAttributes.MOVEMENT_SPEED.getAttributeUnlocalizedName(), 
				new AttributeModifier(getUUID(stack),"SFA modifier", 0.13, 0));
		attributes.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), 
				new AttributeModifier(getUUID(stack),"SFA modifier", 8, 0));
	}
	
	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}
}
