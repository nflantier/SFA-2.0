package noelflantier.sfartifacts.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumTypeTech;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.tileentities.TileControlPanel;
import noelflantier.sfartifacts.common.tileentities.TileInjector;

public class BlockControlPanel extends ABlockSFAContainer{

    public static final PropertyBool CONNECTED = PropertyBool.create("connected");
    protected static final AxisAlignedBB CP_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 1D, 0.3D);
    protected static final AxisAlignedBB CP_NORTH_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.7F, 1F, 1F, 1F);
    protected static final AxisAlignedBB CP_EAST_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.3F, 1F, 1F);
    protected static final AxisAlignedBB CP_WEST_AABB = new AxisAlignedBB(0.7F, 0.0F, 0.0F, 1F, 1F, 1F);

	public BlockControlPanel(Material material) {
		super(material);
        setDefaultState(this.blockState.getBaseState().withProperty(H_FACING, EnumFacing.NORTH).withProperty(CONNECTED, Boolean.valueOf(false)));
		setRegistryName(Ressources.UL_NAME_CONTROL_PANEL);
        setUnlocalizedName(Ressources.UL_NAME_CONTROL_PANEL);
        setHarvestLevel("pickaxe",1);
		setHardness(1.0F);
		setResistance(10000.0F);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}


    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
    	if(world.getBlockState(neighbor) != null && world.getBlockState(neighbor).getBlock() != ModBlocks.blockControlPanel  ){
    	//if( world.getBlockState(neighbor) == null || ( world.getBlockState(neighbor) != null && world.getBlockState(neighbor).getBlock() instanceof BlockMaterials ) ){
    		TileEntity t = world.getTileEntity(pos);
	    	if(t!=null && t instanceof TileControlPanel){
	    		t.getWorld().scheduleBlockUpdate(pos, world.getBlockState(pos).getBlock(), 4, 1);
	    	}
    	}
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
    	super.updateTick(world, pos, state, rand);
    	TileEntity t = world.getTileEntity(pos);
    	if(t!=null && t instanceof TileControlPanel){
    		((TileControlPanel)t).checkMaster();
    	}
    }
    
    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(H_FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
    	if(worldIn.isRemote)
    		return;
    	
    	TileEntity t = worldIn.getTileEntity(pos);
    	if(t!=null && t instanceof TileControlPanel){
    		((TileControlPanel)t).checkMaster();
    	}
    }
    
    @Override
	public int damageDropped(IBlockState state)
    {
        return 0;
    }
    
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch ((EnumFacing)state.getValue(H_FACING))
        {
            case EAST:
                return CP_EAST_AABB;
            case WEST:
                return CP_WEST_AABB;
            case SOUTH:
                return CP_SOUTH_AABB;
            case NORTH:
                return CP_NORTH_AABB;
            default:
                return CP_WEST_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
        if(worldIn.isRemote)
    		return true;
        TileEntity t = worldIn.getTileEntity(pos);
    	if(state.getValue(CONNECTED).booleanValue())
    		playerIn.openGui(SFArtifacts.instance, ModGUIs.guiIDControlPanel, worldIn, pos.getX(), pos.getY(), pos.getZ());
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
    	return this.getDefaultState().withProperty(H_FACING, EnumFacing.HORIZONTALS[meta%4]).withProperty(CONNECTED, Boolean.valueOf((meta & 4) > 0));
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return (((EnumFacing) state.getValue(H_FACING)).getHorizontalIndex()) | (state.getValue(CONNECTED)?0:4);
    }
    
    @Override
    public BlockStateContainer createBlockState(){
        return new ExtendedBlockState(this, new IProperty[]{H_FACING,CONNECTED}, new IUnlistedProperty[0]);
    }
    
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileControlPanel();
	}
	
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state){
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }
    
    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

}
