package noelflantier.sfartifacts.common.entities;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import noelflantier.sfartifacts.common.blocks.BlockHammerStand;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;

public class EntityHammerInvoking extends EntityThrowable implements IEntityAdditionalSpawnData {

	private BlockPos posOr;
	private static double decY = 100;
	private boolean canFall = false;
	private Random rand = new Random();
	private int rf = 0;
	private int tickEnergy = 0;
	private TileHammerStand hs;

	public EntityHammerInvoking(World worldIn)
    {
        super(worldIn);
    }

    public EntityHammerInvoking(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public EntityHammerInvoking(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }
    	
	public EntityHammerInvoking(World w, EntityPlayer player, BlockPos pos) {
		super(w, pos.getX()+0.5, pos.getY() + decY >= 255 ? 255 : pos.getY() + decY, pos.getZ()+0.5);
		isImmuneToFire = true;
		noClip = true;
		posOr = pos;
		TileEntity t = worldObj.getTileEntity(posOr);
		if(t!=null && t instanceof TileHammerStand){
			hs = (TileHammerStand)t;
		}/*else
			setDead();*/
	}
	
	@Override
    public void onUpdate()
    {
		super.onUpdate();
		if(ticksExisted>=400)
			setDead();

		if(!worldObj.isRemote){
			if(!canFall){
				motionY = 0;
				if(hs != null){
					TileMasterPillar tp = hs.getMasterTile();
					if(tp != null && tp.getEnergyStored(null) + rf >= ModConfig.rfNeededThorHammer){
						
						if(tickEnergy<1){
							tickEnergy = 2;
							if(rf>=ModConfig.rfNeededThorHammer)
								canFall = true;
							int maxAvailable = tp.extractEnergy(null, ModConfig.rfNeededThorHammer-rf < 10000 ? ModConfig.rfNeededThorHammer-rf : 10000, true);
							rf += tp.extractEnergyWireless(maxAvailable, false, hs.getPos());
		           		}
						tickEnergy--;
						
					}else{
						return;
					}
				}/*else
					setDead();*/
			}else{
				if(rand.nextFloat()<0.2F){
					worldObj.addWeatherEffect(new EntityLightningBolt(worldObj, posOr.getX(), posOr.getY(), posOr.getZ(), true));
				}
				if(posY < posOr.getY())
					breakHammerStand();
			}
		}
    }

	public void breakHammerStand(){
		
        worldObj.createExplosion(this, posOr.getX(), posOr.getY()-1, posOr.getZ(), 4.0F, true);

		if(this.worldObj.isRemote){
			setDead();
			return;
		}
		TileEntity t = worldObj.getTileEntity(posOr);
		if(t!=null && t instanceof TileHammerStand){
			((TileHammerStand)t).hasInvoked = true;
			((TileHammerStand)t).isInvoking = false;
			ItemStack it = new ItemStack(ModItems.itemThorHammer, 1, 1);
			it = ItemNBTHelper.setInteger(it, "Energy", ModConfig.rfThorhammer);
			it = ItemNBTHelper.setInteger(it, "AddedCapacityLevel", 0);
			it = ItemNBTHelper.setInteger(it, "Radius", 0);
			it = ItemNBTHelper.setBoolean(it, "CanMagnet", false);
			it = ItemNBTHelper.setBoolean(it, "IsMagnetOn", false);
			it = ItemNBTHelper.setBoolean(it, "IsMoving", false);
			it = ItemNBTHelper.setBoolean(it, "IsThrown", false);
			it = ItemNBTHelper.setBoolean(it, "CanThrowLightning", false);
			it = ItemNBTHelper.setBoolean(it, "CanThrowToMove", false);
			it = ItemNBTHelper.setBoolean(it, "CanBeConfigByHand", false);
			it = ItemNBTHelper.setBoolean(it, "CanTeleport", false);
			it = ItemNBTHelper.setInteger(it, "Mode", 0);
			it = ItemNBTHelper.setTagList(it,"EnchStored", new NBTTagList());
			((TileHammerStand)t).items[0] = it;
			worldObj.setBlockState(posOr, worldObj.getBlockState(posOr).withProperty(BlockHammerStand.BROKEN, true));
			worldObj.notifyBlockUpdate(posOr, worldObj.getBlockState(posOr), worldObj.getBlockState(posOr), 3);
		}
		setDead();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setDouble("oreX", posOr.getX());
		compound.setDouble("oreY", posOr.getY());
		compound.setDouble("oreZ", posOr.getZ());
		compound.setInteger("rf", rf);
		compound.setBoolean("canFall", canFall);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		posOr = new BlockPos( compound.getInteger("oreX"), compound.getInteger("oreY"), compound.getInteger("oreZ"));
		rf = compound.getInteger("rf");
		canFall = compound.getBoolean("canFall");
	}

	@Override
	public void writeSpawnData(ByteBuf buf) {
		buf.writeInt(posOr.getX());
		buf.writeInt(posOr.getY());
		buf.writeInt(posOr.getZ());
	    buf.writeInt(rf);
	    buf.writeBoolean(canFall);
	    
	}

	@Override
	public void readSpawnData(ByteBuf buf) {
		posOr = new BlockPos( buf.readInt(), buf.readInt(), buf.readInt());
	    rf = buf.readInt();
	    canFall = buf.readBoolean();
		
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if(worldObj.isRemote)return;
		
		if(result.typeOfHit==RayTraceResult.Type.BLOCK){
			if(worldObj.getBlockState(result.getBlockPos()).getBlock() != ModBlocks.blockHammerStand)
				HammerHelper.breakthablock(worldObj, result.getBlockPos(), this);
		}else if(result.typeOfHit==RayTraceResult.Type.ENTITY && result.entityHit!=null && result.entityHit instanceof EntityLivingBase)
			result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, null), ((ItemThorHammer) ModItems.itemThorHammer).getToolMaterial().getDamageVsEntity());		
	}
}
