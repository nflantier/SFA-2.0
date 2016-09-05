package noelflantier.sfartifacts.common.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;

public class BlockLiquefiedAsgardite extends BlockFluidClassic{
    
	public BlockLiquefiedAsgardite(Fluid fluid, Material material) {
		super(fluid, material);
		setRegistryName(Ressources.UL_NAME_LIQUEFIED_ASGARDITE);
        setUnlocalizedName(Ressources.UL_NAME_LIQUEFIED_ASGARDITE);
		this.setCreativeTab(SFArtifacts.sfTabs);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}
   
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
        double r =random.nextDouble();
        if(r<0.8) {
        	return;
        }
        float nx = (float)pos.getX()+0.5F;
        float ny = (float)pos.getY()+0.5F;
        float nz = (float)pos.getZ()+0.5F; 
    	float rdx =	0;
        float rdy = random.nextFloat()*2-1F;
    	float rdz = 0;
        if(rdy>0.5F || rdy<-0.5F){
        	rdx = random.nextFloat()*2-1F;
        	rdz = random.nextFloat()*2-1F;
        }else{
        	float tx = random.nextFloat();
        	if(tx>=0.5F){
        		rdx = random.nextFloat()*-1;
        	}else{
        		rdx = random.nextFloat();
        	}
        	
        	float tz = random.nextFloat();
        	if(tz>=0.5F){
        		rdz = random.nextFloat()*-1;
        	}else{
        		rdz = random.nextFloat();
        	}
        }
		ParticleHelper.spawnAsgardianParticles(pos.getX(),pos.getY(),pos.getZ(),random,0.5F);
    }
}
