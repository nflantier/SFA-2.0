package noelflantier.sfartifacts.common.blocks;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
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
import noelflantier.sfartifacts.common.tileentities.ATileSFA;
import noelflantier.sfartifacts.common.tileentities.TileInjector;
import noelflantier.sfartifacts.common.tileentities.TileMightyFoundry;
import noelflantier.sfartifacts.common.tileentities.TileMrFusion;

public class BlockMrFusion extends ABlockSFAContainer{
	

    protected static final AxisAlignedBB CP_UP_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.9D, 0.875D);
    protected static final AxisAlignedBB CP_DOWN_AABB = new AxisAlignedBB(0.125D, 0.1D, 0.125D, 0.875D, 1D, 0.875D);
    protected static final AxisAlignedBB CP_SOUTH_AABB = new AxisAlignedBB(0.125D, 0.125D, 0.0D, 0.875D, 0.875D, 0.9D);
    protected static final AxisAlignedBB CP_NORTH_AABB = new AxisAlignedBB(0.125D, 0.125D, 0.1D, 0.875D, 0.875D, 1D);
    protected static final AxisAlignedBB CP_EAST_AABB = new AxisAlignedBB(0.0D, 0.125D, 0.125D, 0.9D, 0.875D, 0.875D);
    protected static final AxisAlignedBB CP_WEST_AABB = new AxisAlignedBB(0.1D, 0.125D, 0.125D, 1D, 0.875D, 0.875D);
	
	public BlockMrFusion(Material materialIn) {
		super(materialIn);
		setRegistryName(Ressources.UL_NAME_MR_FUSION);
        setUnlocalizedName(Ressources.UL_NAME_MR_FUSION);
        setDefaultState(blockState.getBaseState().withProperty(H_FACING,EnumFacing.NORTH).withProperty(ALL_FACING, EnumFacing.DOWN));
		setHarvestLevel("pickaxe",1);
		this.setHardness(2.0F);
		this.setResistance(10000.0F);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}
	
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch ((EnumFacing)state.getValue(ALL_FACING))
        {
            case EAST:
                return CP_EAST_AABB;
            case WEST:
                return CP_WEST_AABB;
            case SOUTH:
                return CP_SOUTH_AABB;
            case NORTH:
                return CP_NORTH_AABB;
            case UP:
                return CP_UP_AABB;
            case DOWN:
                return CP_DOWN_AABB;
            default:
                return CP_WEST_AABB;
        }
    }

    @Override
	public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
	public boolean dropWithNBT(IBlockState state){
		return true;
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
		    			IFluidHandler fhtile = ((TileMrFusion)t).getCapabilityNoFacing(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
			    		if(fhtile!=null)
			    			return FluidUtil.tryEmptyContainerAndStow(heldItem, fhtile, null, Ressources.FLUID_MAX_TRANSFER, playerIn);
		            }
				}
	    		return true;
    		}
        }
        
		playerIn.openGui(SFArtifacts.instance, ModGUIs.guiIDMrFusion, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
    
    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(meta).withProperty(H_FACING, placer.getHorizontalFacing().getOpposite()).withProperty(ALL_FACING, facing);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(H_FACING, ((TileMrFusion)world.getTileEntity(pos)).facing);
    }
    
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	if(state instanceof IExtendedBlockState)
    		return (IExtendedBlockState) state.withProperty(H_FACING, ((TileMrFusion)world.getTileEntity(pos)).facing);
        return state;
    }
    
    public IBlockState getStateFromMeta(int meta){    
    	return this.getDefaultState().withProperty(ALL_FACING, EnumFacing.VALUES[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return (((EnumFacing) state.getValue(ALL_FACING)).getIndex());
    }
    
    @Override
    public BlockStateContainer createBlockState(){
        return new ExtendedBlockState(this, new IProperty[]{H_FACING,ALL_FACING}, new IUnlistedProperty[0]);
    }
    
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileMrFusion();
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
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }
    
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if(stack.getTagCompound()==null)
			return;
		if(stack.getTagCompound().getTag("BlockEntityTag") == null)
			return;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
			NBTTagCompound t = (NBTTagCompound) stack.getTagCompound().getTag("BlockEntityTag");
			list.add("Energy : "+t.getInteger("Energy")+" RF");
		}else{
			list.add(TextFormatting.WHITE + "" + TextFormatting.ITALIC +"<Hold Shift>");
		}
	}
}
