package noelflantier.sfartifacts.client.render;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.SFARenderHelper;
import noelflantier.sfartifacts.common.tileentities.TileInjector;

public class RenderInjector extends TileEntitySpecialRenderer<TileInjector>{

	public void renderLiquids(TileInjector t){
		List<FluidTank> l = t.getFluidTanks();
		if(l==null || l.get(0)==null )
			return;
		if((l.get(0)!=null && l.get(0).getFluid()==null))
			return;
		if((l.get(0)!=null && l.get(0).getFluid()!=null && l.get(0).getFluidAmount()<=0))
			return;
		
	    TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
		TextureAtlasSprite icon = map.getTextureExtry(l.get(0).getFluid().getFluid().getStill().toString());
		if(icon == null){
			return;
		}
		FluidStack fs = l.get(0).getFluid();
        GL11.glPushMatrix();
                	
    		int amount = l.get(0).getFluidAmount() > 0 ? l.get(0).getFluidAmount() : 1;
    		float ratio = (float)amount /  (float)l.get(0).getCapacity();
    		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            Tessellator tessellator = Tessellator.getInstance();
            double ts = 16.0D;//size texture
            double minU = (double)icon.getInterpolatedU(0 * ts);
            double maxU = (double)icon.getInterpolatedU(1 * ts);
            double minV = (double)icon.getInterpolatedV(0 * ts);
            double maxV = (double)icon.getInterpolatedV(1 * ts);
            double rt = 0.0009765625D;//1/1024 texture atlas size
            
            VertexBuffer vertexbuffer = tessellator.getBuffer();
	        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
	        vertexbuffer.pos(0.42, -0.233+0.4*ratio, 0.28).tex(maxU-ts*0.3*rt, maxV-ts*0.3*rt).endVertex();
	        vertexbuffer.pos(0.42, -0.233+0.4*ratio, -0.28).tex(maxU-ts*0.3*rt, minV).endVertex();
	        vertexbuffer.pos(0.12, -0.233+0.4*ratio, -0.28).tex(minU, minV).endVertex();
	        vertexbuffer.pos(0.12, -0.233+0.4*ratio, 0.28).tex(minU, maxV-ts*0.3*rt).endVertex();
	        tessellator.draw();
	        
	        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
	        vertexbuffer.pos(0.42, -0.233, -0.28).tex(maxU-ts*0.3*rt, minV+(maxV-minV)*ratio*0.4).endVertex();
	        vertexbuffer.pos(0.42, -0.233+0.4*ratio, -0.28).tex(maxU-ts*0.3*rt, minV).endVertex();
	        vertexbuffer.pos(0.42, -0.233+0.4*ratio, 0.28).tex(minU, minV).endVertex();
	        vertexbuffer.pos(0.42, -0.233, 0.28).tex(minU, minV+(maxV-minV)*ratio*0.4).endVertex();
	        tessellator.draw();

        	GL11.glRotatef(180, 0F, 1F, 0F);
	        
	        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
	        vertexbuffer.pos(0.415, -0.41+0.4*ratio, 0.40).tex(maxU-ts*0.465*rt, maxV-ts*0.20*rt).endVertex();
	        vertexbuffer.pos(0.415, -0.41+0.4*ratio, -0.40).tex(maxU-ts*0.465*rt, minV).endVertex();
	        vertexbuffer.pos(-0.2, -0.41+0.4*ratio, -0.40).tex(minU, minV).endVertex();
	        vertexbuffer.pos(-0.2, -0.41+0.4*ratio, 0.40).tex(minU, maxV-ts*0.20*rt).endVertex();
	        tessellator.draw();

	        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);    	        
	        vertexbuffer.pos(0.415, -0.41, -0.40).tex(maxU-ts*0.20*rt, minV+(maxV-minV)*ratio*0.4).endVertex();
	        vertexbuffer.pos(0.415, -0.41+0.4*ratio, -0.40).tex(maxU-ts*0.20*rt, minV).endVertex();
	        vertexbuffer.pos(0.415, -0.41+0.4*ratio, 0.40).tex(minU, minV).endVertex();
	        vertexbuffer.pos(0.415, -0.41, 0.40).tex(minU, minV+(maxV-minV)*ratio*0.4).endVertex();
	        tessellator.draw();
	        
        GL11.glPopMatrix();
	}

	public void renderItems(TileInjector tile){
		
		float tz[] = new float[]{-0.25F,-0.25F,-0F,-0F,+0.25F,+0.25F};
    	float tx[] = new float[]{-0.1F,-0.30F,-0.1F,-0.30F,-0.1F,-0.30F};
    	
    	double yit = -0.133F;
    	List<FluidTank> l = tile.getFluidTanks();
    	int amount = l.get(0).getFluidAmount()>0?l.get(0).getFluidAmount():1;
		float ratio = (float)amount /  (float)l.get(0).getCapacity();
		if(amount>0)yit = -0.41+0.4*ratio;

    	for(int i =0;i<6;i++){
        	if(tile.items[i+1]!=null){
    	        GL11.glPushMatrix();
	    	        GL11.glTranslatef((float)tx[i], (float)yit-0.08F, (float)tz[i]);
			        GL11.glRotatef(45 ,0F, 1F, 0F);
			        GL11.glScalef(0.6F ,0.6F, 0.6F);
	                Minecraft.getMinecraft().getRenderItem().renderItem(tile.items[i+1], ItemCameraTransforms.TransformType.GROUND);
    	        GL11.glPopMatrix();
        	}
    	}
	}
	
    @Override
    public void renderTileEntityAt(TileInjector te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x+0.5, y+0.5, z+0.5);
        switch(te.facing.getIndex()){
			case 2:GL11.glRotatef(270, 0F, 1F, 0F);
				break;
			case 3:GL11.glRotatef(90, 0F, 1F, 0F);
				break;
			case 5:GL11.glRotatef(180, 0F, 1F, 0F);
				break;
			default:
				break;
		}
        
        GL11.glDisable(GL11.GL_LIGHTING);
        SFARenderHelper.setLigtforTESR(te.getWorld(), te.getPos(), 0, 15728640);
        renderLiquids(te);
        renderItems(te);
        GL11.glEnable(GL11.GL_LIGHTING); 
        
                
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

}
