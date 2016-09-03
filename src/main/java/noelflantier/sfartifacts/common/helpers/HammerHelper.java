package noelflantier.sfartifacts.common.helpers;

import java.lang.Thread.State;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.entities.EntityHammerInvoking;
import noelflantier.sfartifacts.common.entities.EntityHammerMinning;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.handlers.capabilities.CapabilityPlayerProvider;
import noelflantier.sfartifacts.common.handlers.capabilities.IPlayerData;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.items.baseclasses.ItemHammerTool;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;

public class HammerHelper {
	
	public static boolean breakOnImpact(ItemStack stack, BlockPos pos, int breakRadius, int breakDepth, EntityPlayer player, RayTraceResult result, Entity entityHit){
		return ((ItemHammerTool)stack.getItem()).getEnergyStored(stack) >= ItemThorHammer.energyMining && (breakRadius > 0) ? breakaBlockWithMop(stack, pos, breakRadius, breakDepth, player, result, entityHit, -1) : false;
	}
	
	public static boolean breakOnMinning(ItemStack stack, BlockPos pos, int breakRadius, int breakDepth, EntityPlayer player){
		return ((ItemHammerTool)stack.getItem()).getEnergyStored(stack) >= ItemThorHammer.energyMining ? breakaBlockWithMop(stack, pos, breakRadius, breakDepth, player, rayTraceMining(player.worldObj, player, 4.5d), null, 1) : false;
	}
	
	public static boolean breakaBlockWithMop(ItemStack stack, BlockPos pos, int breakRadius, int breakDepth, EntityPlayer player, RayTraceResult result, Entity entityHit, int rangeMining){
		if(player.worldObj.isRemote)
			return false;
		IBlockState state = player.worldObj.getBlockState(pos);
		Block block = state.getBlock();
		
		if(block==Blocks.BEDROCK)return false;
		
		boolean effective = false;
		if(block != null){
			for (String s : stack.getItem().getToolClasses(stack)){
				if (block.isToolEffective(s, state) || stack.getItem().getStrVsBlock(stack, state) > 1F) effective = true;
			}
		}
		if (!effective)	return false;
		
		float refStrength = ForgeHooks.blockStrength(state, player, player.worldObj, pos);
		if(result == null){
			updateGhostBlocks(player, player.worldObj);
			return true;
		}

		int sideHit = result.sideHit.getIndex();
		int xRange = breakRadius;
		int yRange = breakRadius;
		int zRange = breakDepth;
		int yOffset = 0;
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		if(breakRadius == 0){
			breakBlock(stack, player.worldObj, pos, sideHit, player, refStrength, true, true);
		}else{
			switch (sideHit) {
				case 0:
				case 1:
					yRange = breakDepth;
					zRange = breakRadius;
					break;
				case 2:
				case 3:
					xRange = breakRadius;
					zRange = breakDepth;
					yOffset = breakRadius - 1;
					break;
				case 4:
				case 5:
					xRange = breakDepth;
					zRange = breakRadius;
					yOffset = breakRadius - 1;
					break;
			}
			
			boolean soundOnRange = false;
			for (int xPos = x - xRange; xPos <= x + xRange; xPos++)
			{
				for (int yPos = y + yOffset - yRange; yPos <= y + yOffset + yRange; yPos++)
				{
					for (int zPos = z - zRange; zPos <= z + zRange; zPos++)
					{
						if(rangeMining<=0)soundOnRange = true;
						else soundOnRange = Math.abs(x - xPos) <= rangeMining && Math.abs(y - yPos) <= rangeMining && Math.abs(z - zPos) <= rangeMining;
						BlockPos p = new BlockPos(xPos, yPos, zPos);
						IBlockState tstate = player.worldObj.getBlockState(p);
						Block tblock = tstate.getBlock();
						if(entityHit!=null && tblock.canEntityDestroy(tstate, player.worldObj, p, entityHit))
							breakBlock(stack, player.worldObj, p, sideHit, player, refStrength, soundOnRange, false);
						else if (entityHit == null)
							breakBlock(stack, player.worldObj, p, sideHit, player, refStrength, soundOnRange, false);
					}
				}
			}
		}
		if(ItemNBTHelper.getBoolean(stack, "CanMagnet", false) && ItemNBTHelper.getBoolean(stack, "IsMagnetOn", false))
		{
			List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - xRange, y + yOffset - yRange, z - zRange, x + xRange + 1, y + yOffset + yRange + 1, z + zRange + 1));
			for (EntityItem item : items){
				if (!player.worldObj.isRemote)
				{
					item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0, 0);
					((EntityPlayerMP)player).connection.sendPacket(new SPacketEntityTeleport(item));
					item.setNoPickupDelay();
				}
			}
		}
		return true;
	}
	
	protected static void breakBlock(ItemStack stack, World world, BlockPos pos, int sidehit, EntityPlayer player, float refStrength, boolean breakSound, boolean oneblock)
	{
		if (world.isAirBlock(pos))
			return;

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		boolean effective = false;
		if(block != null){
			for (String s : stack.getItem().getToolClasses(stack)){
				if (block.isToolEffective(s, state) || stack.getItem().getStrVsBlock(stack, state) > 1F) effective = true;
			}
		}
		if (!effective)	return;

		float strength = ForgeHooks.blockStrength(state, player, world, pos);
				
		if (!player.canHarvestBlock(state) || !ForgeHooks.canHarvestBlock(block, player, world, pos)|| refStrength/strength > 10f )
			return;

		if (!world.isRemote){
			int event = ForgeHooks.onBlockBreakEvent(world, world.getWorldInfo().getGameType(), (EntityPlayerMP) player, pos);
			if (event==-1) {
				((EntityPlayerMP)player).connection.sendPacket(new SPacketBlockChange( world, pos));
				return;
			}
		}
		
		extractEnergyInHammer(stack,ItemThorHammer.energyMining );
		
		if(block.removedByPlayer(state, world, pos, player, true)){
			block.onBlockDestroyedByPlayer( world, pos, state);
			block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack);
			player.addExhaustion(-0.025F);
		}
		((EntityPlayerMP)player).connection.sendPacket(new SPacketBlockChange( world, pos));
		if (breakSound && pos.getX()%2==0 && pos.getZ()%2==0){//REDUCE THE AMOUNT OF BLOCK BREAK SOUND
			SoundHelper.playBlockSound(pos, block);
		}
	}
	
	public static void extractEnergyInHammer(ItemStack stack, int energy){
		((ItemHammerTool)stack.getItem()).extractEnergy(stack, energy, false);
	}
	
	public static RayTraceResult rayTraceMining(World world, Entity player, double range) {
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f;
		if (!world.isRemote && player instanceof EntityPlayer) d1 += 1.62D;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
		Vec3d vec3 = new Vec3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		if (player instanceof EntityPlayerMP) {
			d3 = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
		}
		Vec3d vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
		return world.rayTraceBlocks(vec3, vec31);
	}
	
	public static void updateGhostBlocks(EntityPlayer player, World world) {
		if (world.isRemote) return;
		int xPos = (int) player.posX;
		int yPos = (int) player.posY;
		int zPos = (int) player.posZ;

		for (int x = xPos - 6; x < xPos + 6; x++) {
			for (int y = yPos - 6; y < yPos + 6; y++) {
				for (int z = zPos - 6; z < zPos + 6; z++) {
					((EntityPlayerMP)player).connection.sendPacket(new SPacketBlockChange( world, new BlockPos(x,y,z)));
					//world.markBlockForUpdate(x, y, z);
				}
			}
		}
	}
	
	public static boolean startInvoking(World w, EntityPlayer player, BlockPos pos){

		TileEntity t = w.getTileEntity(pos);
		if(t!=null && t instanceof TileHammerStand){
			((TileHammerStand)t).isInvoking = true;

			EntityHammerInvoking entityh = new EntityHammerInvoking(w, player, pos);
	        entityh.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 0.0F, 0.0F);
	        w.spawnEntityInWorld(entityh);
			
			//w.spawnEntityInWorld(new EntityHammerInvoking(w, player, pos));
		}else 
			return false;
		
		return true;
	}
	
	public static boolean startLightning(World world, ItemStack stack, double x, double y, double z, EntityPlayer player){
		RayTraceResult mo = rayTraceLightning(player,(double)ModConfig.distanceLightning);
        if (mo != null){
            switch (mo.typeOfHit){
                case BLOCK:
                	world.addWeatherEffect(new EntityLightningBolt(world, mo.getBlockPos().getX(), mo.getBlockPos().getY()+1, mo.getBlockPos().getZ(), false));
            		extractEnergyInHammer(stack,ItemThorHammer.energyLightning);
                    break;
                case ENTITY:
                    world.addWeatherEffect(new EntityLightningBolt(world, mo.entityHit.posX, mo.entityHit.posY, mo.entityHit.posZ, false));
            		extractEnergyInHammer(stack,ItemThorHammer.energyLightning);
                    break;
                default:
                	break;
            }
        }
		return false;
	}
	
	public static RayTraceResult rayTraceLightning(EntityPlayer player, double distance){
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f + (double) (player.worldObj.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
		Vec3d vec3 = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        Vec3d vec31 = vec3.addVector((double) f7 * distance, (double) f6 * distance, (double) f8 * distance);
        return player.worldObj.rayTraceBlocks(vec3, vec31);
	}

	public static boolean startMoving(World w, EntityPlayer player, ItemStack stack){

    	if(player.hasCapability(CapabilityPlayerProvider.PLAYER_DATA, null)){
    		IPlayerData ipd = player.getCapability(CapabilityPlayerProvider.PLAYER_DATA, null);
    		if((ipd.getInt("tickMovingWithHammer")>0 || stack.getItem() instanceof ItemHammerTool==false))
    			return false;
    		if(!w.isRemote){
				ipd.setInt("tickMovingWithHammer", 2);
				ipd.setBoolean("isMovingWithHammer", true);
				if(!ipd.getBoolean("justStartMoving"))
					ipd.setBoolean("justStartMoving", true);
				extractEnergyInHammer(stack,ItemThorHammer.energyMoving);
				return false;
			}
    	}
		
        float f = 0.4F;
        player.motionX = (double)(-MathHelper.sin(player.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI) * f)*8;
        player.motionZ = (double)(MathHelper.cos(player.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI) * f)*8;
        player.motionY = (double)(-MathHelper.sin((player.rotationPitch + 0.0F) / 180.0F * (float)Math.PI) * f)*8;
		return true;
	}

	public static void startMiningSequence(World worldIn, EntityPlayer playerIn, ItemStack hammer) {
        EntityHammerMinning entityh = new EntityHammerMinning(worldIn, playerIn, hammer);
        entityh.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 0.0F);
        worldIn.spawnEntityInWorld(entityh);
	}

	public static void startGuiTeleport(World w, EntityPlayer player) {
		player.openGui(SFArtifacts.instance, ModGUIs.guiIDTeleport, w, (int)player.posX, (int)player.posY, (int)player.posZ);
	}
	
	public static void startTeleporting(Entity entity, String []st) {
		int dimid = Integer.parseInt(st[0]);
		double tx = (double)Integer.parseInt(st[1]);
		double ty = (double)Integer.parseInt(st[2]);
		double tz = (double)Integer.parseInt(st[3]);

		if (entity == null || entity.worldObj.isRemote) return;
		World startWorld = entity.worldObj;
		World destinationWorld = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimid);

		if (destinationWorld == null){
			return ;
		}

		Entity mount = entity.getRidingEntity();
		if (mount != null){
			entity.dismountRidingEntity();
			startTeleporting(mount, st);
		}

		boolean interDimensional = startWorld.provider.getDimension() != destinationWorld.provider.getDimension();

		startWorld.updateEntityWithOptionalForce(entity, false);//added
		if (entity instanceof EntityPlayerMP)
			((EntityPlayerMP)entity).closeScreen();
		
		if ((entity instanceof EntityPlayerMP) && interDimensional)
		{
			EntityPlayerMP player = (EntityPlayerMP)entity;
			//player.closeScreen();//added
			player.dimension = dimid;
			player.connection.sendPacket(new SPacketRespawn(player.dimension, player.worldObj.getDifficulty(), destinationWorld.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
			((WorldServer)startWorld).getPlayerChunkMap().removePlayer(player);

			startWorld.playerEntities.remove(player);
			startWorld.updateAllPlayersSleepingFlag();
			int i = entity.chunkCoordX;
			int j = entity.chunkCoordZ;
			if ((entity.addedToChunk) && (startWorld.getChunkProvider().provideChunk(i, j)!=null))
			{
				startWorld.getChunkFromChunkCoords(i, j).removeEntity(entity);
				startWorld.getChunkFromChunkCoords(i, j).setChunkModified();
			}
			startWorld.loadedEntityList.remove(entity);
			startWorld.onEntityRemoved(entity);
		}

		entity.setLocationAndAngles(tx, ty, tz, 0, 0);

		((WorldServer)destinationWorld).getChunkProvider().loadChunk((int)tx >> 4, (int)tz >> 4);

		destinationWorld.theProfiler.startSection("placing");
		if (interDimensional)
		{
			if (!(entity instanceof EntityPlayer))
			{
				NBTTagCompound entityNBT = new NBTTagCompound();
				entity.isDead = false;
				entityNBT.setString("id", EntityList.getEntityString(entity));
				entity.writeToNBT(entityNBT);
				entity.isDead = true;
				entity = EntityList.createEntityFromNBT(entityNBT, destinationWorld);
				if (entity == null)
				{
					return;
				}
				entity.dimension = destinationWorld.provider.getDimension();
			}
			destinationWorld.spawnEntityInWorld(entity);
			entity.setWorld(destinationWorld);
		}
		entity.setLocationAndAngles(tx, ty, tz, 0, entity.rotationPitch);

		destinationWorld.updateEntityWithOptionalForce(entity, false);
		entity.setLocationAndAngles(tx, ty, tz, 0, entity.rotationPitch);

		if ((entity instanceof EntityPlayerMP))
		{
			EntityPlayerMP player = (EntityPlayerMP)entity;
			if (interDimensional) {
				player.mcServer.getPlayerList().preparePlayer(player, (WorldServer) destinationWorld);
			}
			player.connection.setPlayerLocation(tx, ty, tz, 0,0);
		}

		destinationWorld.updateEntityWithOptionalForce(entity, false);

		if (((entity instanceof EntityPlayerMP)) && interDimensional)
		{
			EntityPlayerMP player = (EntityPlayerMP)entity;
			player.interactionManager.setWorld((WorldServer) destinationWorld);
			player.mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, (WorldServer) destinationWorld);
			player.mcServer.getPlayerList().syncPlayerInventory(player);

			for (PotionEffect potionEffect : (Iterable<PotionEffect>) player.getActivePotionEffects())
			{
				player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potionEffect));
			}
			
			player.connection.sendPacket(new SPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
			
			FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, startWorld.provider.getDimension(), destinationWorld.provider.getDimension());
		}
		entity.setLocationAndAngles(tx, ty, tz, 0, entity.rotationPitch);

		if (mount != null)
		{
			entity.startRiding(mount,true);
			if ((entity instanceof EntityPlayerMP)) {
				destinationWorld.updateEntityWithOptionalForce(entity, true);
			}
		}
		destinationWorld.theProfiler.endSection();
		entity.fallDistance = 0;
	}
	
	public static void breakthablock(World world, BlockPos pos, Entity entity)
	{
		if (world.isAirBlock(pos))
			return;

		IBlockState state= world.getBlockState(pos);
		Block block = state.getBlock();

		if(block==Blocks.BEDROCK || !block.canEntityDestroy(state, world, pos, entity))
			return ;
		
		if (!world.isRemote)
		{
			world.removeTileEntity(pos);
			world.setBlockToAir(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}
}
