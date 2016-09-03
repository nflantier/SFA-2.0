package noelflantier.sfartifacts.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public abstract class ATileSFA extends TileEntity implements ITickable{

	public boolean isRedStoneEnable = false;
    public boolean init = false;
    public boolean preinit = false;
    public EnumFacing facing = EnumFacing.NORTH;
    
	@Override
	public void update() {
        if(!preinit)
        	preinit();
        if(!init)
        	init();
        if(worldObj.isRemote)
        	return;
        isRedStoneEnable = worldObj.isBlockPowered(this.getPos()) ;
	}

    public void preinit(){
    	preinit = true;
    }
    
    public void init(){
    	init = true;
    }
    
	@Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), writeToNBT(new NBTTagCompound()));
    }
	
    public NBTTagCompound getUpdateTag(){
        return writeToNBT(new NBTTagCompound());
    }
    
	@Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        readFromNBT(pkt.getNbtCompound());
    }

	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(pos.getX()+0.5F, pos.getY()+0.5F, pos.getZ()+0.5F)<=64;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
	    facing = EnumFacing.getFront(compound.getInteger("facing"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	super.writeToNBT(compound);
    	compound.setInteger("facing", facing.getIndex());
        return compound;
    }
    
}
