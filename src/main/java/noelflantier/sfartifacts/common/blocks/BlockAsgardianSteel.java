package noelflantier.sfartifacts.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarBlockType;

public class BlockAsgardianSteel  extends BlockMaterials implements IBlockMaterials{

	public BlockAsgardianSteel(Material materialIn) {
		super(materialIn);
		setRegistryName(Ressources.UL_NAME_ASGARDIAN_STEEL);
        setUnlocalizedName(Ressources.UL_NAME_ASGARDIAN_STEEL);
        setDefaultState(blockState.getBaseState().withProperty(BLOCK_TYPE,EnumPillarBlockType.NO_PILLAR_NORMAL));
		setHarvestLevel("pickaxe",2);
		setHardness(3.0F);
		setResistance(10000.0F);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}
}

