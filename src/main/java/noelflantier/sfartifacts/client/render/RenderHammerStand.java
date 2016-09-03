package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import noelflantier.sfartifacts.common.helpers.SFARenderHelper;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;
import noelflantier.sfartifacts.common.tileentities.TileLiquefier;

public class RenderHammerStand  extends TileEntitySpecialRenderer<TileHammerStand>{
	public void renderItems(TileHammerStand tile){
    	if(tile.items[0]!=null){
	        GL11.glPushMatrix();
				GL11.glTranslatef(-0.05F, 0.75F, 0.0F);
		        GL11.glRotatef(90 ,0F, 1F, 0F);
		        GL11.glRotatef(-5 ,1F, 0F, 1F);
		        GL11.glScalef(0.9F ,0.9F, 0.9F);
                Minecraft.getMinecraft().getRenderItem().renderItem(tile.items[0], ItemCameraTransforms.TransformType.GROUND);
	        GL11.glPopMatrix();
    	}
	}
	
    @Override
    public void renderTileEntityAt(TileHammerStand te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

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
        renderItems(te);
        GL11.glEnable(GL11.GL_LIGHTING); 
        
                
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }
}
