package noelflantier.sfartifacts.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.tileentities.TileInjector;
import noelflantier.sfartifacts.common.tileentities.TileMrFusion;
import noelflantier.sfartifacts.common.tileentities.TileSoundEmiter;

public class BlockSoundEmiter extends ABlockSFAContainer{
	
	public BlockSoundEmiter(Material materialIn) {
		super(materialIn);
		setRegistryName(Ressources.UL_NAME_SOUND_EMITTER);
        setUnlocalizedName(Ressources.UL_NAME_SOUND_EMITTER);
		this.setHarvestLevel("pickaxe",1);
		this.setHardness(2.0F);
		this.setResistance(10000.0F);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
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
		    			IFluidHandler fhtile = ((TileSoundEmiter)t).getCapabilityNoFacing(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
			    		if(fhtile!=null)
			    			return FluidUtil.tryEmptyContainerAndStow(heldItem, fhtile, null, Ressources.FLUID_MAX_TRANSFER, playerIn);
		            }
				}
	    		return true;
    		}
        }
        
    	playerIn.openGui(SFArtifacts.instance, ModGUIs.guiIDSoundEmiter, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
    
    @Override
	public boolean dropWithNBT(IBlockState state){
		return true;
	}
    
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSoundEmiter();
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
        return true;
    }

}