package noelflantier.sfartifacts.common.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;

public interface ITileMaster {
	default int getMasterX(){
		return getMasterPos().getX();
	};
	default int getMasterY(){
		return getMasterPos().getY();
	};
	default int getMasterZ(){
		return getMasterPos().getZ();
	};
    BlockPos getMasterPos();
    World getWorldForMaster();
    
    default boolean hasMaster(){
    	return getMasterPos()!=null;
    };
    default boolean isMasterHere(){
    	return getMasterTile()!=null;
    };
    default boolean isMaster(BlockPos pos){
    	return getMasterPos().equals(pos);
    };
    default boolean isMasterStillMaster(){
    	return isMasterHere() && getMasterTile().hasMaster();
    };
    
    default public TileMasterPillar getMasterTile(){
    	if(getWorldForMaster()==null || getMasterPos()==null)
    		return null;
    	TileEntity t = getWorldForMaster().getTileEntity(getMasterPos());
    	if(t!=null && t instanceof TileMasterPillar){
    		return (TileMasterPillar)t;
    	}
    	return null;
    }
    
    default NBTTagCompound writeMasterToNBT(NBTTagCompound nbt){
    	if(hasMaster()){
	        nbt.setInteger("masterx", getMasterX());
	        nbt.setInteger("mastery", getMasterY());
	        nbt.setInteger("masterz", getMasterZ());
        	nbt.setBoolean("hasmaster", true);
        }else
        	nbt.setBoolean("hasmaster", false);
    	return nbt;
    }
    
    default BlockPos readMasterFromNBT(NBTTagCompound nbt){
    	if(nbt.getBoolean("hasmaster"))
    		return new BlockPos(nbt.getInteger("masterx"),nbt.getInteger("mastery"),nbt.getInteger("masterz"));	
    	return null;
    }
}
