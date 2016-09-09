package noelflantier.sfartifacts.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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
					BlockPos masterpos = pos.add(-p.blockToActivate.getX(), -p.blockToActivate.getY(), -p.blockToActivate.getZ());
					if(p!=null && PillarsConfig.checkStructure(w,masterpos,originalb,p)){
						return setupStructure(w,player,masterpos,EnumPillarMaterial.getMaterialFromClass(originalb.getClass())  ,name);
					}
				}
			}
		}

		return false;
	}

	public static boolean setupStructure(World w, EntityPlayer player, BlockPos masterpos, EnumPillarMaterial material, String structure){
		player.getServer().addScheduledTask(new Runnable(){
			@Override
			public void run() {
		    	Pillar p = PillarsConfig.getInstance().getPillarFromName(structure);
				if(p!=null)
					PillarsConfig.setupStructure(w,player,masterpos,p, material, structure);
		
		    	if(player!=null)
		    		player.addChatComponentMessage(new TextComponentString(structure+" pillar structure created"));
			}}
		);
		return true;
	}

	public static boolean recheckStructure(IBlockAccess w, BlockPos pos, String name){
		Block originalb = w.getBlockState(pos).getBlock();
		if(PillarsConfig.getInstance().nameToPillar.containsKey(name)){
			Pillar p = PillarsConfig.getInstance().nameToPillar.get(name);
			if(p!=null)
				return PillarsConfig.reCheckStructure(w,pos,originalb,p);
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
