package noelflantier.sfartifacts.client.render;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import noelflantier.sfartifacts.common.helpers.SFARenderHelper;
import noelflantier.sfartifacts.common.tileentities.TileInjector;
import noelflantier.sfartifacts.common.tileentities.TileLiquefier;

public class RenderLiquefier  extends TileEntitySpecialRenderer<TileLiquefier>{

	public void renderLiquids(TileLiquefier t){
		List<FluidTank> l = t.getFluidTanks();
		if(l==null || l.get(0)==null || l.get(1)==null)
			return;
		if(l.get(0).getFluid()==null || l.get(1).getFluid()==null)
			return;
		
	    TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
		TextureAtlasSprite iconasgardite = map.getTextureExtry(l.get(0).getFluid().getFluid().getStill().toString());
		TextureAtlasSprite iconwater = map.getTextureExtry(l.get(1).getFluid().getFluid().getStill().toString());
		
        GL11.glPushMatrix();
                	
    		int amount = l.get(0).getFluidAmount() > 0 ? l.get(0).getFluidAmount() : 1;
    		float ratio = (float)amount /  (float)l.get(0).getCapacity();
    		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();
            double ts = 16.0D;//size texture
            double rt = 0.0009765625D;//1/1024 texture atlas size

            VertexBuffer vertexbuffer = tessellator.getBuffer();
    		if(l.get(0).getFluidAmount()>0 && iconasgardite!=null){
                double minU = (double)iconasgardite.getInterpolatedU(0 * ts);
                double maxU = (double)iconasgardite.getInterpolatedU(1 * ts);
                double minV = (double)iconasgardite.getInterpolatedV(0 * ts);
                double maxV = (double)iconasgardite.getInterpolatedV(1 * ts);
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
    		}
    		
        	GL11.glRotatef(180, 0F, 1F, 0F);
        	
    		amount = l.get(1).getFluidAmount() > 0 ? l.get(1).getFluidAmount() : 1;
    		ratio = (float)amount /  (float)l.get(1).getCapacity();
            
    		if(l.get(1).getFluidAmount()>0 && iconwater!=null){
                double minU = (double)iconwater.getInterpolatedU(0 * ts);
                double maxU = (double)iconwater.getInterpolatedU(1 * ts);
                double minV = (double)iconwater.getInterpolatedV(0 * ts);
                double maxV = (double)iconwater.getInterpolatedV(1 * ts);
		        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		        vertexbuffer.pos(0.42, -0.233+0.4*ratio, 0.28).tex(maxU-0.64*rt, maxV-0.64*rt).endVertex();
		        vertexbuffer.pos(0.42, -0.233+0.4*ratio, -0.28).tex(maxU-0.64*rt, minV).endVertex();
		        vertexbuffer.pos(0.12, -0.233+0.4*ratio, -0.28).tex(minU, minV).endVertex();
		        vertexbuffer.pos(0.12, -0.233+0.4*ratio, 0.28).tex(minU, maxV-0.64*rt).endVertex();
		        tessellator.draw();
		        
		        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		        vertexbuffer.pos(0.42, -0.233, -0.28).tex(maxU-0.64*rt, minV+(maxV-minV)*ratio*0.4).endVertex();
		        vertexbuffer.pos(0.42, -0.233+0.4*ratio, -0.28).tex(maxU-0.64*rt, minV).endVertex();
		        vertexbuffer.pos(0.42, -0.233+0.4*ratio, 0.28).tex(minU, minV).endVertex();
		        vertexbuffer.pos(0.42, -0.233, 0.28).tex(minU, minV+(maxV-minV)*ratio*0.4).endVertex();
		        tessellator.draw();
    		}
	        
        GL11.glPopMatrix();
	}

	public void renderItems(TileLiquefier tile){
    	if(tile.items[0]!=null){
	        GL11.glPushMatrix();
				GL11.glTranslatef(0.0F, 0.41F, -0.05F);
		        GL11.glRotatef(90 ,1F, 0F, 0F);
		        GL11.glScalef(0.6F ,0.6F, 0.6F);
                Minecraft.getMinecraft().getRenderItem().renderItem(tile.items[0], ItemCameraTransforms.TransformType.GROUND);
	        GL11.glPopMatrix();
    	}
	}
	
    @Override
    public void renderTileEntityAt(TileLiquefier te, double x, double y, double z, float partialTicks, int destroyStage) {
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
