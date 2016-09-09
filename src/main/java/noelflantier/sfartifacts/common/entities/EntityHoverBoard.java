package noelflantier.sfartifacts.common.entities;

import java.util.Map;

import com.google.common.collect.MapMaker;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.handlers.ModEvents;
import noelflantier.sfartifacts.common.helpers.Utils;
import noelflantier.sfartifacts.common.items.ItemHoverBoard;

public class EntityHoverBoard  extends Entity implements IEntityAdditionalSpawnData {

	private EntityPlayer player;
	private int slot;
	private int currentTickEnergy = 20;
	private int tickEnergy = 20;
	private int typeHoverBoard = 0;
	
	
	private static Map<EntityPlayer, EntityHoverBoard> hoverBoardMap = new MapMaker().weakKeys().weakValues().makeMap();
	private long tickSwing = 0;
	
	public static boolean isHoverBoardDeployed(Entity player) {
		EntityHoverBoard hoverb = hoverBoardMap.get(player);
		return hoverb == null || hoverb.isDeployed();
	}
	public static int getHoverBoardType(Entity player) {
		EntityHoverBoard hoverb = hoverBoardMap.get(player);
		return hoverb == null?0:hoverb.getTypeHoverBoard();
	}
	private static boolean isHoverBoardValid(EntityPlayer player, EntityHoverBoard hoverboard) {
		if (player == null || player.isDead || hoverboard == null || hoverboard.isDead) return false;
		if (player.worldObj.provider.getDimension() != hoverboard.worldObj.provider.getDimension()) return false;
		if(player.inventory.getStackInSlot(hoverboard.getSlot())!=null){
			if (player.inventory.getStackInSlot(hoverboard.getSlot()).getItem() instanceof ItemHoverBoard ==false)
				return false;
			if (player.inventory.getStackInSlot(hoverboard.getSlot()).getItemDamage()>>1!=hoverboard.getTypeHoverBoard())
				return false;
			if (((ItemHoverBoard)player.inventory.getStackInSlot(hoverboard.getSlot()).getItem()).getEnergyStored(player.inventory.getStackInSlot(hoverboard.getSlot()))<=0)
				return false;
		}else
			return false;
		
		return true;
	}
	
	public EntityHoverBoard(World world) {
		super(world);
	}
	
	public EntityHoverBoard(World world, EntityPlayer player) {
		this(world);
		this.player = player;
		this.slot = player.inventory.currentItem;
		this.typeHoverBoard = player.inventory.getStackInSlot(slot).getItemDamage()>>1;
	}
	
	@Override
	protected void entityInit() {
		//this.dataWatcher.addObject(17, (byte)1);
	}

	public boolean isDeployed() {
		return false;
		//return this.dataWatcher.getWatchableObjectByte(17) == 1;
	}

	@SideOnly(Side.CLIENT)
	public static void updateHoverboards(World worldObj) {
		for (Map.Entry<EntityPlayer, EntityHoverBoard> e : hoverBoardMap.entrySet()) {
			EntityPlayer player = e.getKey();
			EntityHoverBoard hoverboard = e.getValue();
			if (isHoverBoardValid(player, hoverboard)) hoverboard.fixPositions(player, player instanceof EntityPlayerSP);
			else hoverboard.setDead();
		}
	}
	
	@Override
	public void onUpdate() {
		//setDead();
		if (!isHoverBoardValid(player, this)) {
			setDead();
		}

		if (isDead) {
			hoverBoardMap.remove(player);
			return;
		}

		boolean isDeployed = false;

		if (!worldObj.isRemote) {
			//this.dataWatcher.updateObject(17, (byte)(isDeployed? 1 : 0));
			fixPositions(player, false);
		}
		
		boolean isInFluid = Utils.isPlayerHoverFuid(player, 0, 0, 0) || player.isInWater();
		
		if (this.typeHoverBoard==ItemHoverBoard.MATTEL_HOVERBOARD && isInFluid)
			return;
		
		double horizontalSpeed = 0.1;
		if (player.isSneaking()) {
			horizontalSpeed = 0;
		} else {
			if(this.typeHoverBoard==ItemHoverBoard.MATTEL_HOVERBOARD)
				horizontalSpeed = 0.2;
			else if(this.typeHoverBoard==ItemHoverBoard.PITBULL_HOVERBOARD)
				horizontalSpeed = 0.3;
		}
		
		double x = Math.cos(Math.toRadians(player.rotationYawHead + 90)) * horizontalSpeed;
		double z = Math.sin(Math.toRadians(player.rotationYawHead + 90)) * horizontalSpeed;

		if(player.motionX!=0){
			if(player.motionX>-1 && player.motionX<1)
				player.motionX += x;
		}
		if(player.motionZ!=0){
			if(player.motionZ>-1 && player.motionZ<1)
				player.motionZ += z;
		}
		
		if(worldObj.isRemote && horizontalSpeed>0 && ModEvents.INSTANCE.getClientTick()%15==0){
			tickSwing = ModEvents.INSTANCE.getServerTick()+8;
			player.limbSwing = 0.5f;
			player.prevLimbSwingAmount = 0.5f;
			player.limbSwingAmount = 0.5f;
		}
		if(worldObj.isRemote && (tickSwing==0 || (tickSwing>0 && tickSwing==ModEvents.INSTANCE.getClientTick()) )){
			player.limbSwing = 0f;
			player.prevLimbSwingAmount = 0f;
			player.limbSwingAmount = 0f;
			tickSwing=0;
		}
		
		if(currentTickEnergy<=0 && horizontalSpeed>0 ){
			if(player.inventory.getStackInSlot(slot)!=null)
				((ItemHoverBoard)player.inventory.getStackInSlot(slot).getItem()).extractEnergy(player.inventory.getStackInSlot(slot), ItemHoverBoard.rfPerSecongHoverboard[this.typeHoverBoard], false);
			currentTickEnergy = tickEnergy;
		}
		currentTickEnergy-=1;
		
		/*if(player.motionY>5)
			player.motionY = 5;*/
		if(player.fallDistance>5)
			player.fallDistance = 5;
		
		if(worldObj.isRemote && this.typeHoverBoard==ItemHoverBoard.PITBULL_HOVERBOARD && ModEvents.INSTANCE.getClientTick()%2==0){
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX-Math.cos(Math.toRadians(rotationYaw + 140))*0.4, this.posY-1.5, this.posZ-Math.sin(Math.toRadians(rotationYaw + 140))*0.4, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX-Math.cos(Math.toRadians(rotationYaw + 40))*0.4, this.posY-1.5, this.posZ-Math.sin(Math.toRadians(rotationYaw + 40))*0.4, 0.0D, 0.0D, 0.0D);
		}
		
		if(this.typeHoverBoard==ItemHoverBoard.PITBULL_HOVERBOARD && (Utils.isPlayerHoverFuid(player, 0, -1, 0) || isInFluid )){
			player.motionY+=0.08;
			player.onGround = true;
		}
		
		player.stepHeight = 2;

	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	public int getSlot() {
		return slot;
	}
	public int getTypeHoverBoard() {
		return typeHoverBoard;
	}
	
	@Override
	public void setDead() {
		super.setDead();
		player.stepHeight = 0.5F;
		hoverBoardMap.remove(player);
	}

	private void fixPositions(EntityPlayer thePlayer, boolean localPlayer) {
		this.lastTickPosX = prevPosX = player.prevPosX;
		this.lastTickPosY = prevPosY = player.prevPosY;
		this.lastTickPosZ = prevPosZ = player.prevPosZ;

		this.posX = player.posX;
		this.posY = player.posY;
		this.posZ = player.posZ;

		setPosition(posX, posY, posZ);
		this.prevRotationYaw = player.prevRenderYawOffset;
		this.rotationYaw = player.renderYawOffset;

		this.prevRotationPitch = player.prevRotationPitch;
		this.rotationPitch = player.rotationPitch;

		if (!localPlayer) {
			this.posY += 1.2;
			this.prevPosY += 1.2;
			this.lastTickPosY += 1.2;
		}

		this.motionX = this.posX - this.prevPosX;
		this.motionY = this.posY - this.prevPosY;
		this.motionZ = this.posZ - this.prevPosZ;
	}
	
	@Override
	public void writeSpawnData(ByteBuf data) {
		if (player == null) {
			data.writeInt(-42);
		} else {
			data.writeInt(player.getEntityId());
		}
		data.writeInt(slot);
		data.writeInt(typeHoverBoard);
	}

	@Override
	public void readSpawnData(ByteBuf data) {		
		int playerId = data.readInt();
		Entity e = worldObj.getEntityByID(playerId);
	
		if (e instanceof EntityPlayer) {
			player = (EntityPlayer)e;
			hoverBoardMap.put(player, this);
			slot = data.readInt();
			typeHoverBoard = data.readInt();
		} else {
			setDead();
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}
	
	@Override
	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		return false;
	}

}
