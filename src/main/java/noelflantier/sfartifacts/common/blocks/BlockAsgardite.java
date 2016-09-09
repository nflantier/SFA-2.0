package noelflantier.sfartifacts.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarBlockType;

public class BlockAsgardite extends BlockMaterials implements IBlockMaterials{

	public BlockAsgardite(Material materialIn) {
		super(materialIn);
		setRegistryName(Ressources.UL_NAME_ASGARDITE_BLOCK);
        setUnlocalizedName(Ressources.UL_NAME_ASGARDITE_BLOCK);
        setDefaultState(blockState.getBaseState().withProperty(BLOCK_TYPE,EnumPillarBlockType.NO_PILLAR_NORMAL));
		setHarvestLevel("pickaxe",2);
		setHardness(2.0F);
		setLightLevel(0.5F);
		setResistance(20.0F);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}
}
