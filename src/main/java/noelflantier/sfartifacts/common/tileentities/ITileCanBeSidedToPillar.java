package noelflantier.sfartifacts.common.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;

public interface ITileCanBeSidedToPillar extends ITileMaster{

    default public TileMasterPillar isSidedToPillar(BlockPos pos){
    	TileEntity t = getWorld().getTileEntity(pos);
    	if(t!=null && t instanceof ITileMustHaveMaster){
    		ITileMustHaveMaster itm = (ITileMustHaveMaster)t;
    		return itm.isMasterStillMaster()?itm.getMasterTile():null;
    	}
    	return null;
    }
}
