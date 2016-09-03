package noelflantier.sfartifacts.common.tileentities;

import java.util.List;

import net.minecraft.util.math.BlockPos;

public interface ITileCanBeMaster extends ITileMustHaveMaster{
    List<BlockPos> getChildsList();
}
