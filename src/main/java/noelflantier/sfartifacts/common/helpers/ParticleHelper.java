package noelflantier.sfartifacts.common.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.client.particles.ParticleAsgardite;
import noelflantier.sfartifacts.client.particles.ParticleLightning;
import noelflantier.sfartifacts.client.particles.ParticlePillarToWirelessPowerBlock;

@SideOnly(Side.CLIENT)
public class ParticleHelper {
	
	public enum Type{
		LIGHTNING,
		BOLT,
		ENERGYFLOW;
	}
	
	private static Minecraft mc = Minecraft.getMinecraft();
	private static World theWorld = mc.theWorld;
	private static Map<String, ResourceLocation> boundTextures = new HashMap();
	private static ResourceLocation defaultParticles;
	
	
	public static void spawnCustomParticle(Type pt, double x, double y, double z, Object... data) {
		spawnCustomParticle(pt, x, y, z, 64.0D, data);
	}
	public static void spawnCustomParticle(Type pt, double x, double y, double z, double viewRange,  Object... data){
		
		Entity entity = mc.getRenderViewEntity();

        if (mc != null && entity != null && mc.effectRenderer != null)
        {
            int i = mc.gameSettings.particleSetting;

            if (i == 1 && theWorld.rand.nextInt(3) == 0)
            {
                i = 2;
            }
            if(i==2)
            	return;
            
			switch(pt){
				case LIGHTNING :
					mc.effectRenderer.addEffect((new ParticleLightning(mc.theWorld, x, y, z, 0.6f, 1f, 1f )));
					break;
				case BOLT :
					mc.effectRenderer.addEffect((new ParticleAsgardite(mc.theWorld, x, y, z, 0, 0.1, 0, 20, 6, -2, 0.6, 1, 1, 1, 0)));
					break;
				case ENERGYFLOW :
					mc.effectRenderer.addEffect((new ParticlePillarToWirelessPowerBlock(mc.theWorld, (double)x, (double)y, (double)z, (double)data[0], (double)data[1], (double)data[2], (int)data[3], 70)));
					break;
				default : break;
			}
        }
	}
	
	public static void spawnAsgardianParticles(int x, int y, int z, Random random, float ch){
		if(random.nextFloat()>ch)
			return;
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
	    spawnCustomParticle(ParticleHelper.Type.LIGHTNING, nx + rdx, ny + rdy, nz + rdz);
	}
}
