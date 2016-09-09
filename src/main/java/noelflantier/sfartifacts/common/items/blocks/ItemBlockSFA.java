package noelflantier.sfartifacts.common.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.tileentities.ATileSFA;

public class ItemBlockSFA extends ItemBlock{

	public static enum EnumOriented{
		NONE,
		HORIZONTAL,
		GLOBAL;
	}
	
	public EnumOriented oriented = EnumOriented.NONE;
	public ItemBlockSFA(Block block) {
		super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
	}
	public ItemBlockSFA(Block block,  EnumOriented oriented) {
		this(block);
		this.oriented = oriented;
	}
    public int getMetadata(int damage)
    {
        return damage;
    }
	
    @Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState){
    	boolean flag = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
    	if(oriented == EnumOriented.NONE)
    		return flag;
    	
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof ATileSFA){
			
			switch (oriented)
	        {
	            case NONE:
	            	break;
	            case HORIZONTAL:
	            	((ATileSFA)te).facing = player.getHorizontalFacing().getOpposite();
	            	break;
	            case GLOBAL:
	            	((ATileSFA)te).facing = side;
	            	break;
	            default:
	            	break;
	        }
			((ATileSFA)te).markDirty();
		}
		return flag;
    }

}
