package noelflantier.sfartifacts.common.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;

public class BlockLiquefiedAsgardite extends BlockFluidClassic{
    
	public BlockLiquefiedAsgardite(Fluid fluid, Material material) {
		super(fluid, material);
		setRegistryName(Ressources.UL_NAME_LIQUEFIED_ASGARDITE);
        setUnlocalizedName(Ressources.UL_NAME_LIQUEFIED_ASGARDITE);
		this.setCreativeTab(SFArtifacts.sfTabs);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}
   
    /*@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        double r =random.nextDouble();
        if(r<0.8) {
        	return;
        }
        float nx = (float)x+0.5F;
        float ny = (float)y+0.5F;
        float nz = (float)z+0.5F; 
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
        ParticleHelper.spawnCustomParticle(ParticleHelper.Type.LIGHTNING, nx+rdx, ny+rdy, nz+rdz);
		ParticleHelper.spawnCustomParticle(ParticleHelper.Type.BOLT, nx+rdx, ny+rdy, nz+rdz);
    }*/
}
