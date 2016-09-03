package noelflantier.sfartifacts.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumTypeTech;
import noelflantier.sfartifacts.common.blocks.SFAProperties.PropertyTypeTech;
import noelflantier.sfartifacts.common.blocks.SFAProperties.UnlistedPropertyInductor;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.tileentities.ATileSFA;
import noelflantier.sfartifacts.common.tileentities.TileInductor;
import noelflantier.sfartifacts.common.tileentities.TileMrFusion;

public class BlockInductor extends ABlockSFAContainer{

    public static final PropertyTypeTech TYPETECH = PropertyTypeTech.create("type_tech");

    protected static final AxisAlignedBB NORTHBB = new AxisAlignedBB(0.2F, 0.2F, 0.4F, 0.8F, 0.8F, 1F);
	protected static final AxisAlignedBB SOUTHBB = new AxisAlignedBB(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.6F);
	protected static final AxisAlignedBB EASTBB = new AxisAlignedBB(0.0F, 0.2F, 0.2F, 0.6F, 0.8F, 0.8F);
	protected static final AxisAlignedBB WESTBB = new AxisAlignedBB(0.4F, 0.2F, 0.2F, 1F, 0.8F, 0.8F);
	protected static final AxisAlignedBB UPBB = new AxisAlignedBB(0.2F, 0.0F, 0.2F, 0.8F, 0.6F, 0.8F);
	protected static final AxisAlignedBB DOWNBB = new AxisAlignedBB(0.2F, 0.4F, 0.2F, 0.8F, 1.0F, 0.8F);
	protected static final AxisAlignedBB FULLBB = new AxisAlignedBB(1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
	
	public BlockInductor(Material materialIn) {
		super(materialIn);
		setRegistryName(Ressources.UL_NAME_INDUCTOR);
        setUnlocalizedName(Ressources.UL_NAME_INDUCTOR);
		setHarvestLevel("pickaxe",1);
        this.setDefaultState(blockState.getBaseState().withProperty(ALL_FACING, EnumFacing.NORTH).withProperty(TYPETECH, EnumTypeTech.BASIC));
		setHardness(1.0F);
		setResistance(100.0F);
	}
	
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
        if(worldIn.isRemote)
    		return true;
        
		playerIn.openGui(SFArtifacts.instance, ModGUIs.guiIDInductor, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
    
    @Override 
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
    	TileEntity te = worldIn.getTileEntity(pos);
    	if( te instanceof TileInductor){
    		((TileInductor)te).facing = state.getValue(ALL_FACING);
    	}
    }
    
    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(meta).withProperty(ALL_FACING, facing);
    }

    
    public IBlockState getStateFromMeta(int meta){    
    	return this.getDefaultState().withProperty(TYPETECH, EnumTypeTech.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return ((EnumTypeTech) state.getValue(TYPETECH)).ordinal();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{ALL_FACING,TYPETECH}, new IUnlistedProperty[0]);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(ALL_FACING, ((TileInductor)world.getTileEntity(pos)).facing);
    }
    
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	if(state instanceof IExtendedBlockState)
    		return (IExtendedBlockState) state.withProperty(ALL_FACING, ((TileInductor)world.getTileEntity(pos)).facing);
        return state;
    }
    
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileInductor(getStateFromMeta(meta).getValue(TYPETECH));
	}

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
    	if(source.getTileEntity(pos)==null || ( source.getTileEntity(pos)!=null && !(source.getTileEntity(pos) instanceof TileInductor)) )
    		return FULLBB;
    	
    	EnumFacing enumfacing = ((TileInductor)source.getTileEntity(pos)).facing ;
    	if (enumfacing==EnumFacing.NORTH){
        	return NORTHBB;
        }else if (enumfacing==EnumFacing.SOUTH){
        	return SOUTHBB;
        }else if (enumfacing==EnumFacing.EAST){
        	return EASTBB;
        }else if (enumfacing==EnumFacing.WEST){
        	return WESTBB;
        }else if (enumfacing==EnumFacing.UP){
        	return UPBB;
        }else if (enumfacing==EnumFacing.DOWN){
        	return DOWNBB;
        }
    	return FULLBB;
    }
    
    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return false;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state){
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }
}
