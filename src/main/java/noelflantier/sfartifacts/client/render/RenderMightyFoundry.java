package noelflantier.sfartifacts.client.render;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidTank;
import noelflantier.sfartifacts.common.helpers.SFARenderHelper;
import noelflantier.sfartifacts.common.tileentities.TileMightyFoundry;

public class RenderMightyFoundry  extends TileEntitySpecialRenderer<TileMightyFoundry>{
	
	public void renderLiquids(TileMightyFoundry t){
		List<FluidTank> l = t.getFluidTanks();
		if(l==null || l.get(0)==null)
			return;
		if(l.get(0).getFluid()==null)
			return;
		
	    TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
		TextureAtlasSprite icon = map.getTextureExtry(l.get(0).getFluid().getFluid().getStill().toString());
		
        GL11.glPushMatrix();
                	
    		int amount = l.get(0).getFluidAmount() > 0 ? l.get(0).getFluidAmount() : 1;
    		float ratio = (float)amount /  (float)l.get(0).getCapacity();
    		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();
            double ts = 16.0D;//size texture
            double rt = 0.0009765625D;//1/1024 texture atlas size
    		
        	GL11.glRotatef(180, 0F, 1F, 0F);
        	
            VertexBuffer vertexbuffer = tessellator.getBuffer();
    		if(l.get(0).getFluidAmount()>0 && icon!=null){
                double minU = (double)icon.getInterpolatedU(0 * ts);
                double maxU = (double)icon.getInterpolatedU(1 * ts);
                double minV = (double)icon.getInterpolatedV(0 * ts);
                double maxV = (double)icon.getInterpolatedV(1 * ts);
    	        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
    	        vertexbuffer.pos(0.42, -0.39+0.5*ratio, 0.47).tex(maxU-0.1*rt, maxV-0.1*rt).endVertex();
    	        vertexbuffer.pos(0.42, -0.39+0.5*ratio, -0.47).tex(maxU-0.1*rt, minV).endVertex();
    	        vertexbuffer.pos(-0.48, -0.39+0.5*ratio, -0.47).tex(minU, minV).endVertex();
    	        vertexbuffer.pos(-0.48, -0.39+0.5*ratio, 0.47).tex(minU, maxV-0.1*rt).endVertex();
    	        tessellator.draw();
    	        
    	        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
    	        vertexbuffer.pos(0.42, -0.39, -0.47).tex(maxU-0.1*rt, minV+(maxV-minV)*ratio*0.5).endVertex();
    	        vertexbuffer.pos(0.42, -0.39+0.5*ratio, -0.47).tex(maxU-0.1*rt, minV).endVertex();
    	        vertexbuffer.pos(0.42, -0.39+0.5*ratio, 0.47).tex(minU, minV).endVertex();
    	        vertexbuffer.pos(0.42, -0.39, 0.47).tex(minU, minV+(maxV-minV)*ratio*0.5).endVertex();
    	        tessellator.draw();
    		}
	        
        GL11.glPopMatrix();
	}

	public void renderItems(TileMightyFoundry tile){
		
    	for(int i =0;i<4;i++){
        	if(tile.items[i+2]!=null){
    	        GL11.glPushMatrix();
					GL11.glTranslatef(-0.2F, 0.62F, 0F);
			        GL11.glRotatef(90 ,0F, 1F, 0F);
			        GL11.glScalef(0.4F ,0.4F, 0.4F);
                	Minecraft.getMinecraft().getRenderItem().renderItem(tile.items[i+2], ItemCameraTransforms.TransformType.GROUND);
    	        GL11.glPopMatrix();
        	}
    	}
		
    	if(tile.items[6]!=null){
	        GL11.glPushMatrix();
				GL11.glTranslatef(0F, -0.28F, 0.62F);
		        GL11.glScalef(0.4F ,0.4F, 0.4F);
		        long angle = (System.currentTimeMillis() / 10) % 360;
		        GlStateManager.rotate(angle, 0, 1, 0);
                Minecraft.getMinecraft().getRenderItem().renderItem(tile.items[6], ItemCameraTransforms.TransformType.GROUND);
	        GL11.glPopMatrix();
    	}
	}
	
    @Override
    public void renderTileEntityAt(TileMightyFoundry te, double x, double y, double z, float partialTicks, int destroyStage) {
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
