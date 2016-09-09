package noelflantier.sfartifacts.client.particles;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class ParticleLightning extends Particle
{

	private static final Random RANDOM_GENERATOR = new Random();
	final int steps = this.getSteps();
	final double[][] Steps;
	final double[] I = new double[3];
	final double[] K = new double[3];
	float currentPoint = 0;
	boolean hasData = false;

	public ParticleLightning( World w, double x, double y, double z, double r, double g, double b )
	{
		this( w, x, y, z, r, g, b, 6 );
		this.regen();
	}

	public ParticleLightning( World w, double x, double y, double z, double r, double g, double b , float alpha)
	{
		this( w, x, y, z, r, g, b, 6 );
		this.particleAlpha = alpha;
		this.regen();
	}
	protected ParticleLightning( World w, double x, double y, double z, double r, double g, double b, int maxAge )
	{
		super( w, x, y, z, 0,0,0);
		this.particleRed = (float)r;
		this.particleGreen = (float)g;
		this.particleBlue = (float)b;
		this.Steps = new double[this.steps][3];
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.particleMaxAge = maxAge;
	}

	protected void regen()
	{
		double lastDirectionX = ( RANDOM_GENERATOR.nextDouble() - 0.5 ) * 0.9;
		double lastDirectionY = ( RANDOM_GENERATOR.nextDouble() - 0.5 ) * 0.9;
		double lastDirectionZ = ( RANDOM_GENERATOR.nextDouble() - 0.5 ) * 0.9;
		for( int s = 0; s < this.steps; s++ )
		{
			this.Steps[s][0] = lastDirectionX = ( lastDirectionX + ( RANDOM_GENERATOR.nextDouble() - 0.5 ) * 0.9 ) / 2.0;
			this.Steps[s][1] = lastDirectionY = ( lastDirectionY + ( RANDOM_GENERATOR.nextDouble() - 0.5 ) * 0.9 ) / 2.0;
			this.Steps[s][2] = lastDirectionZ = ( lastDirectionZ + ( RANDOM_GENERATOR.nextDouble() - 0.5 ) * 0.9 ) / 2.0;
		}
	}

	private int getSteps()
	{
		return 2;
	}

	@Override
	public int getBrightnessForRender( float par1 )
	{
		int j1 = 13;
		return j1 << 20 | j1 << 4;
	}

	@Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
		float j = 1.0f;
		//tess.setColorRGBA_F( this.particleRed,this.particleGreen,this.particleBlue, this.particleAlpha );
		if( this.particleAge == 3 )
		{
			this.regen();
		}
		double f6 = this.particleTextureIndexX / 16.0;
		double f7 = f6 + 0.0324375F;
		double f8 = this.particleTextureIndexY / 16.0;
		double f9 = f8 + 0.0324375F;

		f6 = f7;
		f8 = f9;

		double scale = 0.02;// 0.02F * this.particleScale;

		double[] a = new double[3];
		double[] b = new double[3];

		double ox = 0;
		double oy = 0;
		double oz = 0;

		EntityPlayer p = Minecraft.getMinecraft().thePlayer;
		double offX = -rotationZ;
		double offY = MathHelper.cos( (float) ( Math.PI / 2.0f + p.rotationPitch * 0.017453292F ) );
		double offZ = rotationX;

		for( int layer = 0; layer < 2; layer++ )
		{
			if( layer == 0 )
			{
				scale = 0.04;
				offX *= 0.001;
				offY *= 0.001;
				offZ *= 0.001;
			}
			else
			{
				offX = 0;
				offY = 0;
				offZ = 0;
				scale = 0.02;
			}

			for( int cycle = 0; cycle < 3; cycle++ )
			{
				this.clear();

				double x = ( this.prevPosX + ( this.posX - this.prevPosX ) * partialTicks - interpPosX ) - offX;
				double y = ( this.prevPosY + ( this.posY - this.prevPosY ) * partialTicks - interpPosY ) - offY;
				double z = ( this.prevPosZ + ( this.posZ - this.prevPosZ ) * partialTicks - interpPosZ ) - offZ;

				for( int s = 0; s < this.steps; s++ )
				{
					double xN = x + this.Steps[s][0];
					double yN = y + this.Steps[s][1];
					double zN = z + this.Steps[s][2];

					double xD = xN - x;
					double yD = yN - y;
					double zD = zN - z;

					if( cycle == 0 )
					{
						ox = ( yD * 0 ) - ( 1 * zD );
						oy = ( zD * 0 ) - ( 0 * xD );
						oz = ( xD * 1 ) - ( 0 * yD );
					}
					if( cycle == 1 )
					{
						ox = ( yD * 1 ) - ( 0 * zD );
						oy = ( zD * 0 ) - ( 1 * xD );
						oz = ( xD * 0 ) - ( 0 * yD );
					}
					if( cycle == 2 )
					{
						ox = ( yD * 0 ) - ( 0 * zD );
						oy = ( zD * 1 ) - ( 0 * xD );
						oz = ( xD * 0 ) - ( 1 * yD );
					}

					double ss = Math.sqrt( ox * ox + oy * oy + oz * oz ) / ( ( ( (double) this.steps - (double) s ) / this.steps ) * scale );
					ox /= ss;
					oy /= ss;
					oz /= ss;

					a[0] = x + ox;
					a[1] = y + oy;
					a[2] = z + oz;

					b[0] = x;
					b[1] = y;
					b[2] = z;

					this.draw( worldRendererIn, a, b, f6, f8, partialTicks);

					x = xN;
					y = yN;
					z = zN;
				}
			}
		}
	}

	private void clear()
	{
		this.hasData = false;
	}

	private void draw( VertexBuffer worldRendererIn, double[] a, double[] b, double f6, double f8, float partialTicks)
	{
		if( this.hasData )
		{
			int i = this.getBrightnessForRender(partialTicks);
	        int j = i >> 16 & 65535;
	        int k = i & 65535;
			worldRendererIn.pos(a[0], a[1], a[2]).tex(f6, f8).color( this.particleRed,this.particleGreen,this.particleBlue, this.particleAlpha ).lightmap(j, k).endVertex();
			worldRendererIn.pos(this.I[0], this.I[1], this.I[2]).tex(f6, f8).color( this.particleRed,this.particleGreen,this.particleBlue, this.particleAlpha ).lightmap(j, k).endVertex();
			worldRendererIn.pos(this.K[0], this.K[1], this.K[2]).tex(f6, f8).color( this.particleRed,this.particleGreen,this.particleBlue, this.particleAlpha ).lightmap(j, k).endVertex();
			worldRendererIn.pos(b[0], b[1], b[2]).tex(f6, f8).color( this.particleRed,this.particleGreen,this.particleBlue, this.particleAlpha ).lightmap(j, k).endVertex();
		}
		this.hasData = true;
		for( int x = 0; x < 3; x++ )
		{
			this.I[x] = a[x];
			this.K[x] = b[x];
		}
	}
}
