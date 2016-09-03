package noelflantier.sfartifacts.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarBlockType;

public class BlockAsgardianBronze extends BlockMaterials implements IBlockMaterials{

	public BlockAsgardianBronze(Material materialIn) {
		super(materialIn);
		setRegistryName(Ressources.UL_NAME_ASGARDIAN_BRONZE);
        setUnlocalizedName(Ressources.UL_NAME_ASGARDIAN_BRONZE);
		setHarvestLevel("pickaxe",2);
        setDefaultState(blockState.getBaseState().withProperty(BLOCK_TYPE,EnumPillarBlockType.NO_PILLAR_NORMAL));
		setHardness(3.0F);
		setResistance(10000.0F);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

}
