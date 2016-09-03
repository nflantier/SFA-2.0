package noelflantier.sfartifacts.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;

public class BlockAsgardianGlass  extends ABlockSFA{

	public BlockAsgardianGlass(Material material) {
		super(material);

		setRegistryName(Ressources.UL_NAME_ASGARDIAN_GLASS);
        setUnlocalizedName(Ressources.UL_NAME_ASGARDIAN_GLASS);
		setHarvestLevel("pickaxe",1);
		setHardness(1.0F);
		setResistance(2000.0F);
		setSoundType(SoundType.GLASS);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}


    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity){
    	return false;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }
}