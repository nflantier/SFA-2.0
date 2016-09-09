package noelflantier.sfartifacts.common.blocks;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.blocks.SFAProperties.PropertyMaterial;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.tileentities.TileInjector;

public class BlockInjector extends ABlockSFAContainer implements IBlockUsingMaterials{

	public static final PropertyMaterial MATERIAL = PropertyMaterial.create("material");

	public BlockInjector(Material materialIn) {
		super(materialIn);
		setRegistryName(Ressources.UL_NAME_INJECTOR);
        setUnlocalizedName(Ressources.UL_NAME_INJECTOR);
		setHarvestLevel("pickaxe",1);
        setDefaultState(blockState.getBaseState().withProperty(MATERIAL, EnumPillarMaterial.ASGARDITE).withProperty(H_FACING, EnumFacing.NORTH));
		setHardness(2.0F);
		setResistance(10000.0F);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(meta).withProperty(H_FACING, placer.getHorizontalFacing().getOpposite());
    }
	
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
        if(playerIn.isSneaking())
    		return true;
        
        if(heldItem!=null){
	        IFluidHandler fhitem = FluidUtil.getFluidHandler(heldItem);
			if(fhitem!=null){
		    	if(!worldIn.isRemote){
		    		TileEntity t = worldIn.getTileEntity(pos);
		    		if (t != null && t.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side))
		            {
		    			IFluidHandler fhtile = ((TileInjector)t).getCapabilityNoFacing(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
			    		if(fhtile!=null)
			    			return FluidUtil.tryEmptyContainerAndStow(heldItem, fhtile, null, Ressources.FLUID_MAX_TRANSFER, playerIn);
		            }
				}
	    		return true;
    		}
        }
        
        playerIn.openGui(SFArtifacts.instance, ModGUIs.guiIDInjector, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
        
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileInjector();
	}

    @Override
    protected BlockStateContainer createBlockState() {
    	return new ExtendedBlockState(this, new IProperty[]{MATERIAL,H_FACING}, new IUnlistedProperty[0]);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(H_FACING, ((TileInjector)world.getTileEntity(pos)).facing);
    }
    
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	if(state instanceof IExtendedBlockState)
    		return (IExtendedBlockState) state.withProperty(H_FACING, ((TileInjector)world.getTileEntity(pos)).facing);
        return state;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta){
    	/* 0 material = asgardite
    	 * 1 material = bronze
    	 * 2 material = steel*/
    	return getDefaultState().withProperty(MATERIAL, EnumPillarMaterial.values()[meta]);
    }
        
    @Override
    public int getMetaFromState(IBlockState state){
        return ((EnumPillarMaterial) state.getValue(MATERIAL)).ordinal();
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list){
		for(int i = 0 ; i < EnumPillarMaterial.values().length ; i++)
			list.add(new ItemStack(itemIn, 1, i));
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
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
    	EnumFacing sf = state.getValue(H_FACING);
		if(sf==EnumFacing.EAST || sf==EnumFacing.WEST)
			return face==EnumFacing.NORTH || face==EnumFacing.SOUTH;
		if(sf==EnumFacing.NORTH || sf==EnumFacing.SOUTH)
			return face==EnumFacing.EAST || face==EnumFacing.WEST;
		return false;
    }
    
    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
		return true;
    	
    }

    @Override
	public boolean dropWithNBT(IBlockState state){
		return true;
	}
	
    @Override
    public boolean isOpaqueCube(IBlockState state){
        return true;
    }
    
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if(stack.getTagCompound()==null)
			return;
		if(stack.getTagCompound().getTag("BlockEntityTag") == null)
			return;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
			NBTTagCompound t = (NBTTagCompound) stack.getTagCompound().getTag("BlockEntityTag");
			list.add("Energy : "+t.getInteger("Energy")+" RF");
			list.add("Liquid Asgardite : "+t.getInteger("Amount")+" MB");
			list.add("Recipes : "+t.getString("currentRecipeName0")+" | "+t.getString("currentRecipeName1")+" | "+t.getString("currentRecipeName2"));
		}else{
			list.add(TextFormatting.WHITE + "" + TextFormatting.ITALIC +"<Hold Shift>");
		}
	}
}
