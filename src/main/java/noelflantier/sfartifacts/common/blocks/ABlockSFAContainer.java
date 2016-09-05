package noelflantier.sfartifacts.common.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.tileentities.ATileSFA;
import noelflantier.sfartifacts.common.tileentities.ITileGlobalNBT;
import noelflantier.sfartifacts.common.tileentities.TileMrFusion;

public abstract class ABlockSFAContainer extends BlockContainer {

    public static final PropertyDirection H_FACING = PropertyDirection.create("facing",EnumFacing.Plane.HORIZONTAL);
    public static final PropertyDirection ALL_FACING = PropertyDirection.create("all_facing");
	
	protected ABlockSFAContainer(Material materialIn) {
		super(materialIn);
		this.setCreativeTab(SFArtifacts.sfTabs);
	}

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity){
    	return false;
    }
    
	public boolean dropWithNBT(IBlockState state){
		return false;
	}

    @Override
	public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

	@Override 
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
    	TileEntity te = worldIn.getTileEntity(pos);
    	if(te!=null && te instanceof ATileSFA){
    		if(state.getProperties().containsKey(H_FACING))
    			((ATileSFA)te).facing = state.getValue(H_FACING);
        	((ATileSFA)te).initAfterFacing();
    	}
    }
    
	@Override 
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
		if(!dropWithNBT(state))
			return super.removedByPlayer(state, world, pos, player, willHarvest);

		TileEntity te = world.getTileEntity(pos);
		if (te instanceof ITileGlobalNBT)
        {
            this.onBlockDestroyedByPlayer(world, pos, state);
            this.harvestBlock(world, player, pos, state, te, player.getHeldItemMainhand());
            super.removedByPlayer(state, world, pos, player, willHarvest);
			return false;
        }
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack)
    {
        if (te instanceof ITileGlobalNBT)
        {
        	ItemStack itemstack = new ItemStack(this.getItemDropped(state, null, 0), 1, this.damageDropped(state));
		    ((ITileGlobalNBT) te).writeToItem(itemstack);
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, (TileEntity)null, stack);
        }
    }
	
	@Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		if(dropWithNBT(state)){
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof ITileGlobalNBT){
				ItemStack itemStack = new ItemStack(this.getItemDropped(state, null, 0), 1, this.damageDropped(state));
			    ((ITileGlobalNBT) te).writeToItem(itemStack);
				return new ArrayList<ItemStack>(){{
					add(itemStack);
				}};
			}
			return super.getDrops(world, pos, state, fortune);
		}
		return super.getDrops(world, pos, state, fortune);
	}
	
	/*@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
	    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	    if(state.getBlock() instanceof ABlockSFAContainer && !((ABlockSFAContainer)state.getBlock()).dropWithNBT(state))
	    	return;
	    
		TileEntity te = worldIn.getTileEntity(pos);
	    if(te instanceof ITileGlobalNBT) {
	    	((ITileGlobalNBT)te).readFromItem(stack);
			te.setPos(new BlockPos(pos.getX(),pos.getY(),pos.getZ()));
		}
	}*/
	
    @Override
    public boolean hasTileEntity(IBlockState state){
        return true;
    }
    public abstract TileEntity createNewTileEntity(World worldIn, int meta);
    
}
