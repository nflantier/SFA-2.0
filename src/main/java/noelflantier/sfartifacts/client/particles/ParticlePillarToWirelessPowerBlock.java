package noelflantier.sfartifacts.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.handlers.ModItems;

@SideOnly(Side.CLIENT)
public class ParticlePillarToWirelessPowerBlock extends Particle{

	public double xFrom, yFrom, zFrom, xTo, yTo, zTo; 
	public float opa = 1;
	public float opapas = -0.01F;
	public float sca = 0.010F;
	public float scapas = -0.0001F;
		
	public ParticlePillarToWirelessPowerBlock(World w, double xFrom, double yFrom, double zFrom, double xTo, double yTo, double zTo, int scale, int maxAge) {
		super(w, xFrom, yFrom, zFrom, 0.0D, 0.0D, 0.0D);
		this.motionX =0.0D;
		this.motionY =0.0D;
		this.motionZ =0.0D;
		this.xFrom = xFrom;
		this.yFrom = yFrom;
		this.zFrom = zFrom;
		this.xTo = xTo;
		this.yTo = yTo;
		this.zTo = zTo;
		this.particleMaxAge=maxAge;
		this.particleScale=MathHelper.clamp_int(scale, 4, 20);
		this.particleAlpha = 1;
		this.opapas = (float) (Math.random()/30);
		this.scapas = (float) (Math.random()*0.5/1500);
		this.field_190017_n = false;
        this.setParticleTexture(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(ModItems.itemAsgardianPearl));
	}
	
    public int getFXLayer()
    {
        return 1;
    }

	@Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
    	this.opa += this.opapas;
    	if(this.opa<0.5 || this.opa>1)this.opapas *= -1;
    	
    	float f4 = this.sca*this.particleScale;
    	
    	this.sca += this.scapas;
    	if(this.sca<0.001 || this.sca>0.01)this.scapas *= -1;

    	
        float f = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
        float f1 = f + 0.015609375F;
        float f2 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
        float f3 = f2 + 0.015609375F;
        //float f4 = 0.01F * this.particleScale;

        if (this.particleTexture != null)
        {
            f = this.particleTexture.getInterpolatedU((double)(0));
            f1 = this.particleTexture.getInterpolatedU((double)(16));
            f2 = this.particleTexture.getInterpolatedV((double)(0));
            f3 = this.particleTexture.getInterpolatedV((double)(16));
        }

        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        worldRendererIn.pos((double)(f5 - rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 - rotationYZ * f4 - rotationXZ * f4)).tex((double)f, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, opa).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 - rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 - rotationYZ * f4 + rotationXZ * f4)).tex((double)f, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, opa).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 + rotationYZ * f4 + rotationXZ * f4)).tex((double)f1, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, opa).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 + rotationYZ * f4 - rotationXZ * f4)).tex((double)f1, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, opa).lightmap(j, k).endVertex();
    
    }
	
	@Override
	public void onUpdate()
	{		
		if(this.particleAge>this.particleMaxAge)this.setExpired();
		if(Minecraft.getMinecraft().gameSettings.particleSetting==2)this.setExpired();
		
		this.motionHandeler();
		this.particleAge++;
		//this.setParticleTextureIndex(64);
	}
	
	public void motionHandeler(){
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		this.motionX = (this.xTo-this.xFrom)/50;
		this.motionY = (this.yTo-this.yFrom)/50;
		this.motionZ = (this.zTo-this.zFrom)/50;
		
		double curhyp = Math.sqrt( (this.posX-this.xTo)*(this.posX-this.xTo) + (this.posY-this.yTo)*(this.posY-this.yTo) + (this.posZ-this.zTo)*(this.posZ-this.zTo) );
		
		if(curhyp<0.1)
			this.setExpired();
		
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
}
