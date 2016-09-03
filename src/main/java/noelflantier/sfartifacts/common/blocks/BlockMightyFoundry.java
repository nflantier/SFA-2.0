package noelflantier.sfartifacts.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.tileentities.TileMightyFoundry;

public class BlockMightyFoundry extends ABlockSFAContainer{

	public BlockMightyFoundry(Material material) {
		super(material);
        setDefaultState(this.blockState.getBaseState().withProperty(H_FACING, EnumFacing.NORTH));
		setRegistryName(Ressources.UL_NAME_MIGHTYFOUNDRY);
        setUnlocalizedName(Ressources.UL_NAME_MIGHTYFOUNDRY);
		this.setHarvestLevel("pickaxe",1);
		this.setHardness(2.0F);
		this.setResistance(10000.0F);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(meta).withProperty(H_FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
	public int damageDropped(IBlockState state)
    {
        return 0;
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
        if(worldIn.isRemote)
    		return true;
        
        if(heldItem!=null){
	        IFluidHandler fhitem = FluidUtil.getFluidHandler(heldItem);
			if(fhitem!=null){
		    	if(!worldIn.isRemote){
		    		TileEntity t = worldIn.getTileEntity(pos);
		    		if (t != null && t.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side))
		            {
		    			IFluidHandler fhtile = ((TileMightyFoundry)t).getCapabilityNoFacing(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
			    		if(fhtile!=null)
			    			return FluidUtil.tryEmptyContainerAndStow(heldItem, fhtile, null, Ressources.FLUID_MAX_TRANSFER, playerIn);
		            }
				}
	    		return true;
    		}
        }
        
		playerIn.openGui(SFArtifacts.instance, ModGUIs.guiIDMightyFoundry, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state;
    }
    
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	if(state instanceof IExtendedBlockState)
    		return (IExtendedBlockState) state;
        return state;
    }
    
    public IBlockState getStateFromMeta(int meta){    
    	return this.getDefaultState().withProperty(H_FACING, EnumFacing.HORIZONTALS[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return (((EnumFacing) state.getValue(H_FACING)).getHorizontalIndex());
    }
    
    @Override
    public BlockStateContainer createBlockState(){
        return new ExtendedBlockState(this, new IProperty[]{H_FACING}, new IUnlistedProperty[0]);
    }
    
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileMightyFoundry();
	}
	
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state){
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
		return side != EnumFacing.UP;
    	
    }

    @Override
	public boolean dropWithNBT(IBlockState state){
		return true;
	}
    
    @Override
    public boolean isOpaqueCube(IBlockState state){
        return true;
    }

}
