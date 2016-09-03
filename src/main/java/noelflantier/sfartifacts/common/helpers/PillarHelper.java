package noelflantier.sfartifacts.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.blocks.BlockMaterials;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarBlockType;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketUnsetPillar;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig.Pillar;
import noelflantier.sfartifacts.common.tileentities.ITileMaster;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileRenderPillarModel;

public class PillarHelper {
	
	public static boolean checkStructure(World w, EntityPlayer player, BlockPos pos, boolean repass){
		Block originalb = w.getBlockState(pos).getBlock();
		for(String name : PillarsConfig.getInstance().nameOrderedBySize){
			if(PillarsConfig.getInstance().nameToPillar.containsKey(name)){
				Pillar p = PillarsConfig.getInstance().nameToPillar.get(name);
				if(p!=null){
					BlockPos npos = pos.add(-p.blockToActivate.getX(), -p.blockToActivate.getY(), -p.blockToActivate.getZ());
					if(p.checkStructure(w,npos,originalb)){
						return setupStructure(w,player,npos,EnumPillarMaterial.getMaterialFromClass(originalb.getClass())  ,name);
					}
				}
			}
		}

		return false;
	}

	public static boolean setupStructure(World w, EntityPlayer player, BlockPos pos, EnumPillarMaterial material, String structure){

		TileEntity t = w.getTileEntity(pos);
		TileMasterPillar tmp;
		if(t!=null && t instanceof TileMasterPillar){
			tmp = (TileMasterPillar)t;
		}else if(t!=null && t instanceof TileRenderPillarModel){
			w.removeTileEntity(pos);
    		w.setBlockState(pos, w.getBlockState(pos).getBlock().getDefaultState().withProperty(BlockMaterials.BLOCK_TYPE, EnumPillarBlockType.PILLAR_MASTER));
			tmp = (TileMasterPillar)w.getTileEntity(pos);
		}else{
    		w.setBlockState(pos, w.getBlockState(pos).getBlock().getDefaultState().withProperty(BlockMaterials.BLOCK_TYPE, EnumPillarBlockType.PILLAR_MASTER));
			tmp = (TileMasterPillar)w.getTileEntity(pos);
		}

		
    	tmp.isRenderingPillarModel = -1;
		tmp.structure = structure;
		tmp.setMaterial(material);
    	tmp.energyCapacity = PillarsConfig.getInstance().getPillarFromName(structure).energyCapacity;
		tmp.storage.setCapacity(tmp.energyCapacity);
		tmp.storage.setMaxTransfer(tmp.energyCapacity/tmp.ratioTransfer);
		tmp.tank.setCapacity(PillarsConfig.getInstance().getPillarFromName(structure).fluidCapacity);
    	tmp.master = pos;
    	
    	if(t!=null && t instanceof TileMasterPillar){
    		tmp.storage.setEnergyStored(tmp.getEnergyStored(null));
    	}
    	tmp.init();
    	w.scheduleUpdate(pos, tmp.getBlockType(), 0);
    	//w.notifyBlockUpdate(pos, w.getBlockState(pos), w.getBlockState(pos), 3);
    	tmp.markDirty();
    	//tmp.updateRenderTick = 2;
    	//w.markBlockRangeForRenderUpdate(pos, pos);
		
    	PillarsConfig.getInstance().getPillarFromName(structure).setupStructure(w,player,pos);

    	if(player!=null)
    		player.addChatComponentMessage(new TextComponentString(structure+" pillar structure created"));
		
		return true;
	}

	public static boolean recheckStructure(IBlockAccess w, BlockPos pos, String name){
		Block originalb = w.getBlockState(pos).getBlock();
		if(PillarsConfig.getInstance().nameToPillar.containsKey(name)){
			Pillar p = PillarsConfig.getInstance().nameToPillar.get(name);
			if(p!=null){
				return p.reCheckStructure(w,pos,originalb);
			}
		}
		return false;
	}
	
	public static boolean unsetupStructure(World w, BlockPos pos){
		TileEntity t = w.getTileEntity(pos);
		Block b = w.getBlockState(pos).getBlock();
		if(t instanceof ITileMaster && ((ITileMaster)t).getMasterPos() == null){
			return false;
		}
		
		if(t instanceof TileMasterPillar){
			((TileMasterPillar)t).master = null;
			int rf = ((TileMasterPillar)t).getEnergyStored(null);
	        w.updateComparatorOutputLevel(pos, b);
			((TileMasterPillar)t).getEnergyStorage().setEnergyStored(rf);
			PacketHandler.sendToAllAround(new PacketUnsetPillar((TileMasterPillar)t),t);
		}else{
			w.removeTileEntity(pos);
			w.setBlockState(pos, b.getDefaultState());
		}
		return false;
	}
}
