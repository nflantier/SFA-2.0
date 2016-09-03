package noelflantier.sfartifacts.common.tileentities.pillar;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketRenderPillarModel;

public class TileRenderPillarModel extends TileEntity implements ITickable{

	public int isRenderingPillarModel = -1;
	
	public TileRenderPillarModel(){
	}
		
    public void setId(int id){
    	isRenderingPillarModel = id;
        PacketHandler.sendToAllAround(new PacketRenderPillarModel(this), this);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.isRenderingPillarModel = nbt.getInteger("isRenderingPillarModel");
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setInteger("isRenderingPillarModel", this.isRenderingPillarModel);
        return nbt;
    }

	@Override
	public void update() {
        if(this.worldObj.isRemote)
        	return;
        PacketHandler.sendToAllAround(new PacketRenderPillarModel(this), this);
	}
}
