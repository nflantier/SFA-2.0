package noelflantier.sfartifacts.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarBlockType;
import noelflantier.sfartifacts.common.blocks.SFAProperties.PropertyPillarBlockType;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.PillarHelper;
import noelflantier.sfartifacts.common.items.ItemBasicHammer;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;
import noelflantier.sfartifacts.common.tileentities.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileInterfacePillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileRenderPillarModel;

public class BlockMaterials extends ABlockSFAContainer implements IBlockHasHammerInteractions{
    public static final PropertyPillarBlockType BLOCK_TYPE = PropertyPillarBlockType.create("block_type");
	
	public BlockMaterials(Material materialIn) {
		super(materialIn);
	}

    @Override
	public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
        
    	if(hand==EnumHand.MAIN_HAND){
    		ItemStack cei = playerIn.getHeldItemMainhand();
			if(playerIn!=null && cei!=null && cei.getItem() instanceof ItemBasicHammer && !worldIn.isRemote){
				int mode = 1;
				if(cei!=null)
					mode = ItemNBTHelper.getInteger(playerIn.getHeldItemMainhand(), "Mode", 1);
				if(mode==0){//BASIC MODE
					PillarHelper.checkStructure(worldIn, playerIn, pos, false);
				}
				if(mode==1){//CONSTRUCTION MODE  
		    		TileEntity t = worldIn.getTileEntity(pos);
	    			IBlockState st = state.getBlock().getDefaultState().withProperty(BLOCK_TYPE,EnumPillarBlockType.PILLAR_RENDER);
		    		if(t==null){
		    			worldIn.setBlockState(pos, st, 2);
		    		} 
		    		t = worldIn.getTileEntity(pos);
					if(t!=null && t instanceof TileRenderPillarModel){
						TileRenderPillarModel teap = (TileRenderPillarModel)t;
						teap.setId(teap.isRenderingPillarModel==-1?0:teap.isRenderingPillarModel+1<PillarsConfig.getInstance().nameOrderedBySize.size()?teap.isRenderingPillarModel+1:-1);
		    			worldIn.markBlockRangeForRenderUpdate(pos, pos);
						return true;
					}
				}
			}
    	}
    	if(heldItem!=null){
	        IFluidHandler fhitem = FluidUtil.getFluidHandler(heldItem);
			if(fhitem!=null){
		    	if(!worldIn.isRemote){
		    		TileEntity t = worldIn.getTileEntity(pos);
		    		if(t!=null && t instanceof TileBlockPillar){
		    			TileMasterPillar tmp = ((TileBlockPillar)t).getMasterTile();
			    		if (tmp != null && tmp.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side))
			            {
			    			IFluidHandler fhtile = ((TileMasterPillar)tmp).getCapabilityNoFacing(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);

				    		if(fhtile!=null)
				    			return FluidUtil.interactWithFluidHandler(heldItem, fhtile, playerIn);
			            }
		    			
		    		}
				}
	    		return true;
    		}
        }
    	return false;
    }
        
    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
        super.onNeighborChange(world, pos, neighbor);
		if(((World)world).isRemote)
            return ;

		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileBlockPillar){
			TileBlockPillar tbp = (TileBlockPillar)tile;
			if(tbp.hasMaster()){
				if(tbp.isMasterHere()){
					if(!PillarHelper.recheckStructure(world, tbp.getMasterPos(), tbp.getMasterTile().structure)){
						PillarHelper.unsetupStructure((World)world, pos);
					}
				}else{
					PillarHelper.unsetupStructure((World)world, pos);
				}
			}else{
				PillarHelper.unsetupStructure((World)world, pos);
			}
			/*if(tbp.hasMaster() && tbp.isMasterHere()){
				if(!PillarHelper.recheckStructure(world, tbp.getMasterPos(), tbp.getMasterTile().structure)){
					PillarHelper.unsetupStructure((World) world, pos);
				}
			}else{
				PillarHelper.unsetupStructure((World) world, pos);
			}*/
		}
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
    	return new ExtendedBlockState(this, new IProperty[]{BLOCK_TYPE}, new IUnlistedProperty[0]);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta){    	
    	/* 0 pillar=false interface=false  normal block
    	 * 1 pillar=false interface=true   normal block from pillar
    	 * 2 pillar=true interface=false   master block
    	 * 3 pillar=true interface)true	   interface block
    	 * */
    	//return getDefaultState().withProperty(PILLAR, (meta >> 1) > 1  ).withProperty(INTERFACE, (meta & 1) == 1);
    	return getDefaultState().withProperty(BLOCK_TYPE, EnumPillarBlockType.values()[meta]);
    }
        
    @Override
    public int getMetaFromState(IBlockState state){
    	/*int i = 0;
    	i += state.getValue(PILLAR).booleanValue() ? 2 : 0;
        i += state.getValue(INTERFACE).booleanValue() ? 1 : 0;
        return i;*/
    	return ((EnumPillarBlockType) state.getValue(BLOCK_TYPE)).ordinal();
    }
    
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	if(state instanceof IExtendedBlockState)
    		return (IExtendedBlockState) state;
        return state;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state){
        return EnumBlockRenderType.MODEL;
    }
    
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if(meta==1)
			return new TileBlockPillar();
		else if(meta==2)
			return new TileInterfacePillar();
		else if(meta==3)
			return new TileMasterPillar();
		else if(meta==4)
			return new TileRenderPillarModel();
		return null;
	}
	
    @Override
    public boolean isOpaqueCube(IBlockState state){
        return true;
    }
}
