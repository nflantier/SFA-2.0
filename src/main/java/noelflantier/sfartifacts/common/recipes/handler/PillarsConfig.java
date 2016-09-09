package noelflantier.sfartifacts.common.recipes.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.blocks.BlockMaterials;
import noelflantier.sfartifacts.common.blocks.SFAProperties;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarBlockType;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.helpers.Utils;
import noelflantier.sfartifacts.common.tileentities.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileInterfacePillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileRenderPillarModel;

public class PillarsConfig {
	static final PillarsConfig instance = new PillarsConfig();
	private static final String CORE_FILE_NAME = "pillarsConfig.json";

	private static final String KEY_PILLARS = "pillars";
	private static final String KEY_ENERGY_CAPACITY = "energycapacity";
	private static final String KEY_FLUID_CAPACITY = "fluidcapacity";
	private static final String KEY_NATURAL_RATIO = "naturalratio";
	private static final String KEY_STRUCTURE = "structure";
	
	
	private static final String P_NORMAL_BLOCK = "block";
	private static final String P_INTERFACE = "interface";
	private static final String P_BLOCK_ACTIVATE = "blockactivate";
	private static final String P_MASTER = "master";
	
	public static PillarsConfig getInstance() {
		return instance;
	}
	
	public Map<String, Pillar> nameToPillar = new HashMap<String, Pillar>();
	public List<String> nameOrderedBySize;

	private PillarsConfig(){
		String configText;
		JsonElement root;
		JsonObject rootObj;
		JsonObject pillarsObj;

		try {
			configText = Utils.getFileFromConfig(CORE_FILE_NAME, false);
			root = new JsonParser().parse(configText);
			rootObj = root.getAsJsonObject();
			pillarsObj = rootObj.getAsJsonObject(KEY_PILLARS);

			for (Entry<String, JsonElement> entry : pillarsObj.entrySet()) {
				String name = entry.getKey();
				if(name==null || name.equals("")){
					System.out.println("Pillar name cant be null");
					continue;
				}
				if(nameToPillar.containsKey(name)){
					System.out.println("Pillar "+name+" allready registered");
					continue;
				}
				processPillar(name, entry.getValue().getAsJsonObject());
			}
			nameOrderedBySize = new ArrayList<String>(nameToPillar.size());
			for(Entry<String, Pillar> entry : nameToPillar.entrySet()){
				orderMap(entry.getValue());
			}
			//for(String s : nameOrderedBySize){
			//	System.out.println("........................ "+s);
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void orderMap( Pillar el){
		int idx = 0;
		if(nameOrderedBySize.size()<=0){
			nameOrderedBySize.add(el.name);
			return;
		}
		for(String s : nameOrderedBySize){
			if(el.mapStructure.size()<nameToPillar.get(s).mapStructure.size())
				idx+=1;
		}
		nameOrderedBySize.add(idx, el.name);
	}
	
	private void processPillar(String name, JsonObject pi) {
		boolean flag = false;
		Pillar p = new Pillar(name);
		if(pi.has(KEY_ENERGY_CAPACITY))
			p.energyCapacity = pi.get(KEY_ENERGY_CAPACITY).getAsInt();
		else
			flag = true;
		if(pi.has(KEY_FLUID_CAPACITY))
			p.fluidCapacity = pi.get(KEY_FLUID_CAPACITY).getAsInt();
		else
			flag = true;
		if(pi.has(KEY_NATURAL_RATIO))
			p.naturalRatio = pi.get(KEY_NATURAL_RATIO).getAsFloat();
		else
			flag = true;
		if(!flag){
			boolean havemaster = false;
			boolean haveblocktoactivate = false;
			int lx = 0;
			int gx = 0;
			int ly = 0;
			int gy = 0;
			int lz = 0;
			int gz = 0;
			List<BlockPos> ms = new ArrayList<BlockPos>();
			Map<String,String> inte = new HashMap<String,String>();
			for (Entry<String, JsonElement> entry : pi.get(KEY_STRUCTURE).getAsJsonObject().entrySet()) {
				int[] c = getXYZ(entry.getKey(), "_");
				lx = c[0] < lx ? c[0] : lx;
				ly = c[1] < ly ? c[1] : ly;
				lz = c[2] < lz ? c[2] : lz;
				gx = c[0] > gx ? c[0] : gx;
				gy = c[1] > gy ? c[1] : gy;
				gz = c[2] > gz ? c[2] : gz;
				BlockPos co = new BlockPos(c[0],c[1],c[2]);
				ms.add(co);
				if(P_BLOCK_ACTIVATE.equals(entry.getValue().getAsString())){
					p.blockToActivate = co;
					haveblocktoactivate = true;
				}else if(P_MASTER.equals(entry.getValue().getAsString())){
					p.blockMaster = co;
					havemaster = true;
				}else if(P_NORMAL_BLOCK.equals(entry.getValue().getAsString())){
					
				}else if(entry.getValue().getAsString().startsWith(P_INTERFACE)){
					String[] separated = entry.getValue().getAsString().split("[:]");
					if(separated!=null && separated.length>1)
						inte.put(entry.getKey(), separated[1]);
				}
			}
			
			p.width = ( lx < 0 ? lx * -1 + gx : gx ) + 1;
			p.height = ( ly < 0 ? ly * -1 + gy : gy ) + 1;
			p.length = ( lz < 0 ? lz * -1 + gz : gz ) + 1;
			
			if(!havemaster){
				System.out.println("Pillar "+name+" dont have any master");
				flag = true;
			}else{
				if(!haveblocktoactivate){
					haveblocktoactivate = true;
					p.blockToActivate = new BlockPos(p.blockMaster.getX(), p.blockMaster.getY(), p.blockMaster.getZ());
				}
			}
			if(!flag && haveblocktoactivate){
				for(BlockPos co : ms){
					int nx = co.getX() + p.blockMaster.getX()*-1;
					int ny = co.getY() + p.blockMaster.getY()*-1;
					int nz = co.getZ() + p.blockMaster.getZ()*-1;
					p.mapStructure.put(nx+"_"+ny+"_"+nz, new BlockPos(nx, ny, nz));
				}
				for(Entry<String, String> entry : inte.entrySet()){
					int[] c = getXYZ(entry.getKey(), "_");
					int ix = c[0] + p.blockMaster.getX()*-1;
					int iy = c[1] + p.blockMaster.getY()*-1;
					int iz = c[2] + p.blockMaster.getZ()*-1;
					String[] t = entry.getValue().split("[,]");
					if(t!=null && t.length>=1){
						int[] ta = new int[t.length];
						for(int i = 0;i<t.length;i++)
							ta[i] = Integer.parseInt(t[i]);
						p.interfaces.put(ix+"_"+iy+"_"+iz,ta );
					}
				}
				p.blockToActivate.add(p.blockMaster.getX()*-1, p.blockMaster.getY()*-1, p.blockMaster.getZ()*-1);
				p.blockMaster.add(-p.blockMaster.getX(), -p.blockMaster.getY(), -p.blockMaster.getZ());
			}
			//dd
			if(!flag)
				nameToPillar.put(name, p);
		}
	}
	
	public int[] getXYZ(String str, String sep){
		String[] separated = str.split("["+sep+"]");
		return new int[]{Integer.parseInt(separated[0]),Integer.parseInt(separated[1]),Integer.parseInt(separated[2])};
	}
	
	public Pillar getPillarFromName(String name){
		return nameToPillar.containsKey(name)?nameToPillar.get(name):null;
	}
	
	public static class Pillar{
		public final String name;//UID
		public int ID;
		public int energyCapacity = 0;
		public int fluidCapacity = 0;
		public float naturalRatio = 1;
		public Map<String,BlockPos> mapStructure = new HashMap<String, BlockPos>();
		public Map<String,int[]> interfaces = new HashMap<String, int[]>();
		public BlockPos blockToActivate;
		public BlockPos blockMaster;
		public int width =-1;
		public int height =-1;
		public int length =-1;
		
		public Pillar(String name){
			this.name = name;
		}

	}

	public static boolean checkStructure(World w, BlockPos pos, Block originalb, Pillar p) {
		
		int size = p.mapStructure.size();
		for(Entry<String, BlockPos> entry : p.mapStructure.entrySet()){
			BlockPos npos = entry.getValue().add(pos.getX(), pos.getY(), pos.getZ());
	    	IBlockState state = w.getBlockState(npos);
			Block b = state.getBlock();
			TileEntity t = w.getTileEntity(npos);
			if(b==null || b.getClass()!=originalb.getClass()){
				return false;
			}
			if(t!=null){
				if( ( t instanceof TileMasterPillar && ((TileMasterPillar)t).hasMaster() ) ){
					return false;
				}
				if( t instanceof TileMasterPillar ==false && t instanceof TileRenderPillarModel==false){
					return false;
				}
			}
		}
		return true;
	}	
	
	public static boolean reCheckStructure(IBlockAccess w, BlockPos pos, Block originalb, Pillar p) {
		for(Entry<String, BlockPos> entry : p.mapStructure.entrySet()){
			BlockPos npos = entry.getValue().add(pos.getX(), pos.getY(), pos.getZ());
	    	IBlockState state = w.getBlockState(npos);
			Block b = state.getBlock();
	    	if(b==Blocks.AIR)
	    		return false;
			TileEntity t = w.getTileEntity(npos);
			if(b==null || b.getClass()!=originalb.getClass()){
				return false;
			}
			if(t!=null){
				//if( ( t instanceof TileMasterPillar && ((TileMasterPillar)t).hasMaster() ) ){
				//	return false;
				//}
				if( t instanceof TileMasterPillar ==false && t instanceof TileRenderPillarModel==false && t instanceof TileBlockPillar==false){
					return false;
				}
			}
		}
		return true;
	}
	
	public static void setupStructure(World world, EntityPlayer player, BlockPos masterpos, Pillar p,EnumPillarMaterial material, String structure){
		TileEntity t = world.getTileEntity(masterpos);
		TileMasterPillar tmp;
		if(t!=null && t instanceof TileMasterPillar){
			tmp = (TileMasterPillar)t;
		}else if(t!=null && t instanceof TileRenderPillarModel){
			world.removeTileEntity(masterpos);
			world.setBlockState(masterpos, world.getBlockState(masterpos).getBlock().getDefaultState().withProperty(BlockMaterials.BLOCK_TYPE, EnumPillarBlockType.PILLAR_MASTER),3);
			tmp = (TileMasterPillar)world.getTileEntity(masterpos);
		}else{
			world.setBlockState(masterpos, world.getBlockState(masterpos).getBlock().getDefaultState().withProperty(BlockMaterials.BLOCK_TYPE, EnumPillarBlockType.PILLAR_MASTER),3);
			tmp = (TileMasterPillar)world.getTileEntity(masterpos);
		}

		
    	tmp.isRenderingPillarModel = -1;
		tmp.structure = structure;
		tmp.setMaterial(material);
    	tmp.energyCapacity = PillarsConfig.getInstance().getPillarFromName(structure).energyCapacity;
		tmp.storage.setCapacity(tmp.energyCapacity);
		tmp.storage.setMaxTransfer(tmp.energyCapacity/tmp.ratioTransfer);
		tmp.tank.setCapacity(PillarsConfig.getInstance().getPillarFromName(structure).fluidCapacity);
    	tmp.master = masterpos;
    	
    	if(t!=null && t instanceof TileMasterPillar){
    		tmp.storage.setEnergyStored(tmp.getEnergyStored(null));
    	}
    	world.notifyBlockUpdate(masterpos, world.getBlockState(masterpos), world.getBlockState(masterpos), 3);
		
		for(Entry<String, BlockPos> entry : p.mapStructure.entrySet()){
			BlockPos npos = entry.getValue().add(masterpos.getX(), masterpos.getY(), masterpos.getZ());
	    	IBlockState state = world.getBlockState(npos);
			Block b = state.getBlock();
			TileEntity tb = world.getTileEntity(npos);
			
			if(tb!=null && tb instanceof TileMasterPillar && ((TileMasterPillar)tb).hasMaster()){
				continue;
			}
			
	    	if(tb!=null && tb instanceof TileRenderPillarModel){
				world.removeTileEntity(npos);
				world.setBlockState(npos, state.withProperty(BlockMaterials.BLOCK_TYPE, SFAProperties.EnumPillarBlockType.NO_PILLAR_NORMAL),3);
				state = world.getBlockState(npos);
				b = state.getBlock();
	    	}
	    	
	    	if(p.interfaces.containsKey(entry.getKey())){
	    		world.setBlockState(npos, state.withProperty(BlockMaterials.BLOCK_TYPE, SFAProperties.EnumPillarBlockType.PILLAR_INTERFACE),3);
	    		TileEntity te = (TileEntity)world.getTileEntity(npos);
	    		TileInterfacePillar tip;
	        	if(te!=null && te instanceof TileInterfacePillar){
	        		tip = (TileInterfacePillar)te;
	    	        tip.master = masterpos;
	    	        for(int k = 0 ; k<p.interfaces.get(entry.getKey()).length; k++){
		            	tip.extractSides.add(EnumFacing.getFront(p.interfaces.get(entry.getKey())[k]));
	 	        		tip.recieveSides.add(EnumFacing.getFront(p.interfaces.get(entry.getKey())[k]));
		            }
		            tip.initInterface();
	        	}
	    	}else{
				world.setBlockState(npos, state.withProperty(BlockMaterials.BLOCK_TYPE, SFAProperties.EnumPillarBlockType.PILLAR_NORMAL),3);
	    		TileEntity te = (TileEntity)world.getTileEntity(npos);
	    	   	TileBlockPillar tbp;
	        	if(te!=null && te instanceof TileBlockPillar){
	        		tbp = (TileBlockPillar)te;
		    		tbp.master = masterpos;
	    		}
	    	}
	    	world.notifyBlockUpdate(npos, state, world.getBlockState(npos), 3);
	    	world.scheduleUpdate(npos, b, 0);
	    	world.markBlockRangeForRenderUpdate(npos, npos);
	        //world.updateComparatorOutputLevel(npos, b);
	    	//w.scheduleUpdate(npos, b, 0);
	    	//w.notifyBlockUpdate(pos, w.getBlockState(pos), w.getBlockState(pos), 3);
	    	//w.notifyNeighborsOfStateChange(npos, b);
		}
	}
}
