package noelflantier.sfartifacts.client.render;

import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.MinecraftForgeClient;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.SFARenderHelper;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig.Pillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileRenderPillarModel;

public class RenderTileRenderPillar extends TileEntitySpecialRenderer<TileRenderPillarModel>{

    @Override
    public void renderTileEntityAt(TileRenderPillarModel te, double x, double y, double z, float partialTicks, int destroyStage) {
    	GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        
        GlStateManager.translate(x+0.5, y+0.5, z+0.5);
        
        GL11.glDisable(GL11.GL_LIGHTING);
        SFARenderHelper.setLigtforTESR(te.getWorld(), te.getPos(), 0, 15728640);
        renderMore(te);
        GL11.glEnable(GL11.GL_LIGHTING);  

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

	private void renderMore(TileRenderPillarModel te) {
        if(te.isRenderingPillarModel>-1 && MinecraftForgeClient.getRenderPass() == 0){
	    	int idpillar = te.isRenderingPillarModel;
	    	String name = PillarsConfig.getInstance().nameOrderedBySize.get(idpillar);
			if(name!=null && PillarsConfig.getInstance().nameToPillar.containsKey(name)){
				Pillar p = PillarsConfig.getInstance().nameToPillar.get(name);
				for(Entry<String, BlockPos> entry : p.mapStructure.entrySet()){
	    	        GL11.glPushMatrix();
			        	GlStateManager.translate(entry.getValue().getX(), entry.getValue().getY(), entry.getValue().getZ());
		                RenderHelper.enableStandardItemLighting();
			        	Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Item.getItemFromBlock(te.getBlockType())), ItemCameraTransforms.TransformType.GROUND);
		                RenderHelper.disableStandardItemLighting();
		    	    GL11.glPopMatrix();
				}
			}
	    }
    }

}
