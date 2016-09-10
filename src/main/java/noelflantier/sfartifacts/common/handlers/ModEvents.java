package noelflantier.sfartifacts.common.handlers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.entities.EntityHoverBoard;
import noelflantier.sfartifacts.common.entities.EntityItemStronk;
import noelflantier.sfartifacts.common.handlers.capabilities.CapabilityPlayerProvider;
import noelflantier.sfartifacts.common.handlers.capabilities.IPlayerData;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.Utils;
import noelflantier.sfartifacts.common.items.ItemEnergyModule;
import noelflantier.sfartifacts.common.items.ItemHoverBoard;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.items.ItemVibraniumShield;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketCapabilityPlayerData;

public class ModEvents {

	public static ModEvents INSTANCE;
	protected long serverTickCount = 0;
	protected long clientTickCount = 0;
	public List<EntityChicken> listChick = new ArrayList<EntityChicken>();
	
	public static void init(){
		INSTANCE = new ModEvents() ;
	}
    
	public long getServerTick(){return this.serverTickCount;}
	public long getClientTick(){return this.clientTickCount;}	
    @SubscribeEvent
    public void onTick(ServerTickEvent evt) {if(evt.phase == Phase.END)serverTickCount++;}
    @SubscribeEvent
    public void onTick(ClientTickEvent evt) {if(evt.phase == Phase.END)clientTickCount++;}
	
    
    @SubscribeEvent
    public void entityLighted(EntityStruckByLightningEvent evt){
    	if(evt.getEntity() instanceof EntityChicken){
    		if(!this.listChick.contains((EntityChicken)evt.getEntity()))
    			this.listChick.add((EntityChicken)evt.getEntity());
    	}
    }
    
    public void chikenIsDad(EntityChicken ec, TickEvent.WorldTickEvent event){
    	if(event.world.rand.nextFloat()<(float)ModConfig.chanceToSpawnMightyFeather){
			float f1 = event.world.rand.nextFloat() * 0.8F+0.1F;
			float f2 = event.world.rand.nextFloat() * 0.8F+0.1F;
			float f3 = event.world.rand.nextFloat() * 0.8F+0.1F;
			event.world.spawnEntityInWorld(new EntityItemStronk(event.world, ec.posX+f1, ec.posY+f2, ec.posZ+f3, new ItemStack(ModItems.itemMightyFeather,1,0)));
		}
    }
    
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
    	if(event.phase == Phase.END) {
	    	if(this.listChick.isEmpty())
	    		return;
	    	this.listChick.stream().filter((c)->c.isDead).forEach((c)->chikenIsDad(c,event));
	    	this.listChick.removeIf((c)->c.isDead || c.getAge()>6000);
    	}
    }
    
	@SubscribeEvent
	public void onAnvilSwag(AnvilUpdateEvent event) {
	    if(event.getLeft() == null || event.getRight() == null) {
	        return;
	    }
		if(event.getLeft().getItem() instanceof ItemHoverBoard){
			if(event.getRight().getItem() instanceof ItemEnergyModule){
				event.setCost(5);
				event.setMaterialCost(1);
				int r = ItemNBTHelper.getInteger(event.getLeft(),"AddedCapacityLevel",0)+1;
				ItemStack o = event.getLeft().copy();
				if(!event.getName().isEmpty())
					o.setStackDisplayName(event.getName());
				event.setOutput(ItemNBTHelper.setInteger(o, "AddedCapacityLevel", r));
			}
		}
	}
    
	public void handleShieldDamages(ItemStack stack, LivingAttackEvent event, EntityPlayer player){
		if( ItemNBTHelper.getBoolean(stack, "CanBlock", false) && !ItemNBTHelper.getBoolean(stack, "IsThrown", false)){
			if(event.getSource()!=null && event.getSource().getSourceOfDamage()!=null){
				double cop = (double)player.posZ - (double)event.getSource().getSourceOfDamage().posZ;
				double coa = (double)player.posX - (double)event.getSource().getSourceOfDamage().posX;
				double a = 360-(Math.toDegrees(Math.atan2(coa,cop))+180);
				float pr = player.rotationYaw < 0 ? 360 + player.rotationYaw : player.rotationYaw;
				float ang = (float)ModConfig.shieldProtection/2;
				if( ( event.getSource()==event.getSource().outOfWorld || event.getSource()==event.getSource().lava || event.getSource()==event.getSource().starve 
						|| event.getSource()==event.getSource().drown || event.getSource()==event.getSource().inFire || event.getSource()==event.getSource().onFire 
						|| event.getSource()==event.getSource().fall ) && !event.getSource().isExplosion()){
					
				}else if(a < pr + ang && a > pr - ang)
					event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void LivingAttackEvent(LivingAttackEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			
			if(player.getHeldItemMainhand()!=null && player.getHeldItemMainhand().getItem() instanceof ItemVibraniumShield)
				handleShieldDamages(player.getHeldItemMainhand(),event, player);
			else if(player.getHeldItemOffhand()!=null && player.getHeldItemOffhand().getItem() instanceof ItemVibraniumShield)
				handleShieldDamages(player.getHeldItemOffhand(),event,player);
			
		}else if(event.getEntity() instanceof EntityItemStronk){
			if(event.getSource()!=null && event.getSource().getSourceOfDamage()!=null && event.getSource()==event.getSource().onFire || event.getSource()==event.getSource().inFire)
				event.setCanceled(true);
		}
	}

	public void handleShieldFall(ItemStack stack, LivingFallEvent event, EntityPlayer player){
		if(ItemNBTHelper.getBoolean(stack, "CanBlock", false) && !ItemNBTHelper.getBoolean(stack, "IsThrown", false)){
			if(player.rotationPitch>80){
				event.setDistance((float)event.getDistance()/4<1?0:(float)event.getDistance()/4);
			}
		}
	}
	
	@SubscribeEvent
	public void LivingFallEvent(LivingFallEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(player == null)
				return;
			
			if(player.getHeldItemMainhand()!=null && player.getHeldItemMainhand().getItem() instanceof ItemVibraniumShield)
				handleShieldFall(player.getHeldItemMainhand(),event, player);
			else if(player.getHeldItemOffhand()!=null && player.getHeldItemOffhand().getItem() instanceof ItemVibraniumShield)
				handleShieldFall(player.getHeldItemOffhand(),event,player);
			
		}
	}
	
	public boolean handleShieldExplosions(ItemStack stack, ExplosionEvent.Detonate event, EntityPlayer player){
		if(ItemNBTHelper.getBoolean(stack, "CanBlock", false) && !ItemNBTHelper.getBoolean(stack, "IsThrown", false)){
			double a = 360-(Math.toDegrees(Math.atan2( (double)player.posX - (double)event.getExplosion().getPosition().xCoord , (double)player.posZ - (double)event.getExplosion().getPosition().zCoord ))+180);
			float pr = player.rotationYaw<0?360+player.rotationYaw:player.rotationYaw;
			float ang = (float)ModConfig.shieldProtection/2;
			return a<pr+ang && a>pr-ang;
		}
		return false;
	}
	
	@SubscribeEvent
	public void ExplosionEventDetonate(ExplosionEvent.Detonate event) {
		List<Entity> toRemove = new ArrayList<Entity>();
		for(Entity e : event.getAffectedEntities()){
			if(e instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer)e;
				if(player.getHeldItemMainhand()!=null && player.getHeldItemMainhand().getItem() instanceof ItemVibraniumShield)
					if( handleShieldExplosions(player.getHeldItemMainhand(),event, player) )
						toRemove.add(e);
				else if(player.getHeldItemOffhand()!=null && player.getHeldItemOffhand().getItem() instanceof ItemVibraniumShield)
					if( handleShieldExplosions(player.getHeldItemOffhand(),event,player) )
						toRemove.add(e);
			}
		}
		if(toRemove.size()>0)
			event.getAffectedEntities().removeAll(toRemove);
	}
	
	@SubscribeEvent
    public void PlayerLoggedInEvent (PlayerLoggedInEvent event){
    	if(ModConfig.isManualSpawning){
    		if(event.player instanceof EntityPlayerMP){
    			EntityPlayerMP mp = (EntityPlayerMP)event.player;
        		if(!mp.getStatFile().hasAchievementUnlocked(ModAchievements.GETTING_MANUAL)){
    	    		ItemStack manual = new ItemStack(ModItems.itemManual);
        	        if (!event.player.inventory.addItemStackToInventory(manual)){
        				float f1 = event.player.getEntityWorld().rand.nextFloat() * 0.8F+0.1F;
        				float f2 = event.player.getEntityWorld().rand.nextFloat() * 0.8F+0.1F;
        				float f3 = event.player.getEntityWorld().rand.nextFloat() * 0.8F+0.1F;
        				EntityItem it = new EntityItem(event.player.worldObj, event.player.posX+f1, event.player.posY+f2, event.player.posZ+f3, manual);
        				event.player.worldObj.spawnEntityInWorld(it);
        	        }
    				event.player.addStat(ModAchievements.GETTING_MANUAL);
        		}
    		}
    	}
    }
	
	@SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    	if(event.phase == Phase.END) {
			
	    	EntityPlayer player = event.player;
			if(player == null)
				return;

	    	if(player.hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null)){
	    		IPlayerData ipd = player.getCapability(CapabilityPlayerProvider.PLAYER_DATA, null);
	    		if(ipd.getFloat("changeStep")>0F){
					player.stepHeight = ipd.getFloat("changeStep");
					ipd.setFloat("changeStep", 0F);
				}

				if(ipd.getInt("tickJustEatHulkFlesh")>0)
					ipd.setInt("tickJustEatHulkFlesh", ipd.getInt("tickJustEatHulkFlesh")-1);
				if(ipd.getInt("tickHasHulkFleshEffect")>0){
					ipd.setInt("tickHasHulkFleshEffect", ipd.getInt("tickHasHulkFleshEffect")-1);
					player.stepHeight = 2F;
					Utils.handleEntityInMaterial(player, true);
				}else if(ipd.getInt("tickHasHulkFleshEffect")==0){
					ipd.setInt("tickHasHulkFleshEffect", -1);
					player.stepHeight = 0.5F;
				}
				
				if(event.side==Side.CLIENT)
					return;
				
				if(ipd.getInt("justBlockedAttack")>0){
					ipd.setInt("justBlockedAttack",ipd.getInt("justBlockedAttack")-1);
				}
				
				if(ipd.getBoolean("isMovingWithHammer")){
					player.fallDistance = 0.0F;
					ipd.setBoolean("justStopMoving",false);
				}else if(ipd.getBoolean("justStopMoving")){
					ipd.setBoolean("justStopMoving",false);
					ItemStack it = player.getHeldItemMainhand();
					if(it!=null && it.getItem()!=null && it.getItem() instanceof ItemThorHammer){
						ItemNBTHelper.setBoolean(player.getHeldItemMainhand(), "IsMoving", false);
					}else{
						it = player.getHeldItemOffhand();
						if(it!=null && it.getItem()!=null && it.getItem() instanceof ItemThorHammer)
							ItemNBTHelper.setBoolean(player.getHeldItemOffhand(), "IsMoving", false);
					}
				}
				
				if(ipd.getBoolean("isMovingWithHammer")){
					if(!ipd.getBoolean("justStartMoving")){
						ipd.setBoolean("isMovingWithHammer",!player.onGround);
		    			if(!ipd.getBoolean("isMovingWithHammer"))
							ipd.setBoolean("justStopMoving",true);
					}
					ipd.setBoolean("justStartMoving",false);
					ipd.setInt("tickMovingWithHammer",ipd.getInt("tickMovingWithHammer")-1);
	    		}else
					ipd.setInt("tickMovingWithHammer",0);
	    	}
    	}
    }
	
    @SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			if(event.getEntityPlayer() == null)
				return;
			if(!EntityHoverBoard.isHoverBoardDeployed(event.getEntityPlayer())){
				if(EntityHoverBoard.getHoverBoardType(event.getEntityPlayer())==ItemHoverBoard.PITBULL_HOVERBOARD)
					GL11.glTranslatef(0, 0.125F, 0);
				else
					GL11.glTranslatef(0, 0.065F, 0);
			}
		}
	}

    @SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderPlayerPost(RenderPlayerEvent.Post event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			if(event.getEntityPlayer() == null)
				return;
			if(!EntityHoverBoard.isHoverBoardDeployed(event.getEntityPlayer())){
				if(EntityHoverBoard.getHoverBoardType(event.getEntityPlayer())==ItemHoverBoard.PITBULL_HOVERBOARD)
					GL11.glTranslatef(0, -0.125F, 0);
				else
					GL11.glTranslatef(0, -0.065F, 0);
			}
		}
	}
	
	public void syncCapabilityPlayerData(Entity entity ,World world){
		if(!world.isRemote && entity instanceof EntityPlayer && entity instanceof EntityPlayerMP && entity.hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null))
			PacketHandler.sendToPlayerMP(new PacketCapabilityPlayerData((EntityPlayer)entity),(EntityPlayerMP)entity);
	}
	
	public void syncAttributes(Entity entity, World world){
		if(!world.isRemote && entity instanceof EntityPlayerMP && entity instanceof EntityPlayer)
			((EntityPlayerMP)entity).connection.sendPacket(new SPacketEntityProperties(entity.getEntityId(), ((EntityPlayer)entity).getAttributeMap().getAllAttributes()));
	}

	
	@SubscribeEvent
    public void attachCapability (AttachCapabilitiesEvent.Entity event){
        if(!event.getEntity().hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null) && event.getEntity() instanceof EntityPlayer)
        	event.addCapability(new ResourceLocation(Ressources.MODID), new CapabilityPlayerProvider());
	}
    @SubscribeEvent
    public void onEntityJoining (EntityJoinWorldEvent event){
		if(!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer){
			syncCapabilityPlayerData(event.getEntity(), event.getWorld());
			syncAttributes(event.getEntity(), event.getWorld());
		}
    }
    @SubscribeEvent
    public void onClonePlayer(PlayerEvent.Clone event) {
    	if(!event.getEntity().getEntityWorld().isRemote && event.getEntity().hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null)){
	    	NBTTagCompound compound = (NBTTagCompound) CapabilityPlayerProvider.PLAYER_DATA.getStorage().writeNBT(CapabilityPlayerProvider.PLAYER_DATA, event.getOriginal().getCapability(CapabilityPlayerProvider.PLAYER_DATA, null), null);
	    	CapabilityPlayerProvider.PLAYER_DATA.getStorage().readNBT(CapabilityPlayerProvider.PLAYER_DATA, event.getEntityPlayer().getCapability(CapabilityPlayerProvider.PLAYER_DATA, null), null, compound);

	    	syncCapabilityPlayerData(event.getEntityPlayer(), event.getEntityPlayer().getEntityWorld());
	    	syncAttributes(event.getEntityPlayer(), event.getEntityPlayer().getEntityWorld());
    	}
    }	    	
}
