package noelflantier.sfartifacts.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import noelflantier.sfartifacts.SFArtifacts;

public abstract class ABlockSFA extends Block{

	public ABlockSFA(Material materialIn) {
		super(materialIn);
		this.setCreativeTab(SFArtifacts.sfTabs);
	}

}
