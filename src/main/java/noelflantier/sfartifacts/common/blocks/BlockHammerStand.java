package noelflantier.sfartifacts.common.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemBasicHammer;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketSoundEvent;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.IUseSFARecipes;
import noelflantier.sfartifacts.common.recipes.RecipeOnHammerStand;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;

public class BlockHammerStand extends ABlockSFAContainer {


    public static final PropertyBool BROKEN = PropertyBool.create("broken");
    
	public BlockHammerStand(Material material) {
		super(material);

		setRegistryName(Ressources.UL_NAME_HAMMER_STAND);
        setUnlocalizedName(Ressources.UL_NAME_HAMMER_STAND);
        setDefaultState(blockState.getBaseState().withProperty(H_FACING,EnumFacing.NORTH).withProperty(BROKEN, false));
		setHarvestLevel("pickaxe",1);
		setHardness(2.0F);
		setResistance(10000.0F);
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabz, List list){
    	list.add(new ItemStack(item, 1, 0));
    	list.add(new ItemStack(item, 1, 1));
    }	

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(meta).withProperty(H_FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta){
    	return getDefaultState().withProperty(BROKEN, meta != 0);
    	//return getDefaultState().withProperty(H_FACING, EnumFacing.HORIZONTALS[meta % 4]).withProperty(BROKEN, meta>3);
    }
        
    @Override
    public int getMetaFromState(IBlockState state){
    	return state.getValue(BROKEN) ? 1 : 0 ;
    	//return ((EnumFacing) state.getValue(H_FACING)).ordinal() + 4 * ( state.getValue(BROKEN) ? 1 : 0 );
    }

    @Override
    protected BlockStateContainer createBlockState() {
    	return new ExtendedBlockState(this, new IProperty[]{H_FACING,BROKEN}, new IUnlistedProperty[0]);
    }


    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(H_FACING, ((TileHammerStand)world.getTileEntity(pos)).facing);
    }
    
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	if(state instanceof IExtendedBlockState)
    		return (IExtendedBlockState) state.withProperty(H_FACING, ((TileHammerStand)world.getTileEntity(pos)).facing);
        return state;
    }  
    
	@Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side){
		if(side==EnumFacing.UP)return false;
		return true;
    }
	
    @Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileHammerStand();
	}
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if(playerIn.isSneaking()) {
    		return false;
    	}
    	
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile != null && tile instanceof TileHammerStand){
				    	
	    	if(side==EnumFacing.UP && heldItem != null && heldItem.getItem() instanceof ItemBasicHammer && ItemNBTHelper.getInteger(heldItem, "Mode", 1)==2){
	    		TileHammerStand ths= (TileHammerStand)tile;
	    		if(ths.items[0]!=null){
	    			if(!worldIn.isRemote && ths.curentRecipe!=null){
	    				if(ths.curentRecipe.itemStillHere()){
		    				ths.curentRecipe.age++;
			    			if(ths.curentRecipe.isDone()){
			    				ths.curentRecipe.end(ths.items[0]);				
			    				ths.curentRecipe = null;
			    				worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, pos.getX(), pos.getY()+1, pos.getZ(), false));	
			    				PacketHandler.sendToTargetPoint(new PacketSoundEvent(pos, 1002, 0),worldIn, pos);
								worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 3);
			    			}
			    			
		    			}else{
		    				ths.curentRecipe = null;
		    			}
			    		return true;
	    			}
	    			if (!worldIn.isRemote){
	    				List<EntityItem> items = worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos,pos.add(1, 2, 1)));
	    				if(items!=null && items.size()>0){
			    			ISFARecipe re = RecipesRegistry.instance.getBestRecipe((IUseSFARecipes)ths,RecipesRegistry.instance.getInputFromEntityItem(items) );
		    				if(re!=null){
		    	    			ths.curentRecipe = new RecipeOnHammerStand(re, items, ths.items[0]);
		    					if(!ths.curentRecipe.isValid)
			    					ths.curentRecipe = null;
		    				}else
		    					ths.curentRecipe = null;
	    				}
	    			}
	    		}
	    		return true;
	    	}
			
    		if(worldIn.getBlockState(pos).getValue(BROKEN)) playerIn.openGui(SFArtifacts.instance, ModGUIs.guiIDHammerStandInvoked, worldIn, pos.getX(), pos.getY(), pos.getZ());
    		else playerIn.openGui(SFArtifacts.instance, ModGUIs.guiIDHammerStandNonInvoked, worldIn, pos.getX(), pos.getY(), pos.getZ());
    		
		}
		
		return true;
    }
    

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
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
