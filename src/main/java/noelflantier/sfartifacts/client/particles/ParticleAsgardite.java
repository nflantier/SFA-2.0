package noelflantier.sfartifacts.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;

@SideOnly(Side.CLIENT)
public class ParticleAsgardite extends Particle{

	public ResourceLocation rl  = new ResourceLocation(Ressources.MODID+":particles/pt");
	public double gravity,friction,r_e,g_e,b_e,opacity_e;
	public int sens;
		
	public ParticleAsgardite(World w, double xp, double yp, double zp, double xs, double ys, double zs, int scale, int maxAge ,double gravit, double Ra,double Ga,double Ba,double opacita,double frictio)
	{
		super(w, xp, yp, zp, xs, ys, zs);
		
		float random = (float) (Math.random()*10);
		if(random>7.5F)this.sens = 0;
		else if(random>5F)this.sens = 8;
		else if(random>2.5F)this.sens =16;
		else this.sens = 24;
		this.motionX = xs;
		this.motionY = ys;
		this.motionZ = zs;
        this.friction=frictio;
        this.particleMaxAge=maxAge;
        this.particleScale=scale;
        this.gravity=gravit*0.001;
        this.r_e=Ra;
        this.g_e=Ga;
        this.b_e=Ba;
        this.opacity_e=opacita;
		this.particleAlpha = 1;
		this.field_190017_n = false;
		setTexture();
	}

	public void setTexture(){
	    this.setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(rl.toString()));
	}

    public int getFXLayer()
    {
        return 1;
    }
    
	@Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
		float textX = (float)this.particleTextureIndexX/8.0F;
		float textXP = textX + 0.125F;
		float textY = (float)this.particleTextureIndexY/8.0F;
		float textYP = textY + 0.125F;
    	float PScale = 0.01F*this.particleScale;
    	float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*partialTicks-interpPosX);
    	float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*partialTicks-interpPosY);
    	float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*partialTicks-interpPosZ);
    	
		worldRendererIn.pos((double)(x-rotationX*PScale-rotationXY*PScale), (double)(y-rotationZ*PScale), (double)(z-rotationZ*PScale-rotationXZ*PScale)).tex(textX, textY).endVertex();
		worldRendererIn.pos((double)(x-rotationX*PScale-rotationXY*PScale), (double)(y-rotationZ*PScale), (double)(z-rotationZ*PScale-rotationXZ*PScale)).tex(textXP, textY).endVertex();
		worldRendererIn.pos((double)(x-rotationX*PScale-rotationXY*PScale), (double)(y-rotationZ*PScale), (double)(z-rotationZ*PScale-rotationXZ*PScale)).tex(textXP, textYP).endVertex();
		worldRendererIn.pos((double)(x-rotationX*PScale-rotationXY*PScale), (double)(y-rotationZ*PScale), (double)(z-rotationZ*PScale-rotationXZ*PScale)).tex(textX, textYP).endVertex();

	}

	@Override
	public void onUpdate()
	{
		if(this.particleAge>this.particleMaxAge)this.setExpired();
		if(Minecraft.getMinecraft().gameSettings.particleSetting==2)this.setExpired();
		
		this.motionHandeler();
		
		this.particleAge++;
		this.nextTextureIndexX();
		//this.setParticleTextureIndex(this.particleAge+this.sens);
	}
	
	public void motionHandeler(){
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionX *= this.friction;
		this.motionY *= this.friction;
		this.motionZ *= this.friction;
		this.motionY +=this.gravity;
	}
}
