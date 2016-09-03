package noelflantier.sfartifacts.common.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketParticleGlobal;

public class BlockOreAsgardite extends ABlockSFA{

	public BlockOreAsgardite(Material materialIn) {
		super(materialIn);
		setRegistryName(Ressources.UL_NAME_ORE_ASGARDITE);
		setUnlocalizedName(Ressources.UL_NAME_ORE_ASGARDITE);
		setHarvestLevel("pickaxe",2);
		setHardness(3.0F);
		setLightLevel(0.5F);
		setResistance(1.0F);
		setTickRandomly(true);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}
	
	@Override
	protected boolean canSilkHarvest(){
		return true;
	}

	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return ModItems.itemAsgardite;
    }
    
    @Override
    public int quantityDroppedWithBonus(int fortunelevel, Random random){
        return this.quantityDropped(random) + random.nextInt(fortunelevel + 1);
    }
    
    @Override
    public int quantityDropped(Random random){
        return 4 + random.nextInt(2);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    	if(ModConfig.isOresEmitParticles)
    		ParticleHelper.spawnAsgardianParticles(pos.getX(),pos.getY(),pos.getZ(),rand,0.5F);
		return;
    }
}
