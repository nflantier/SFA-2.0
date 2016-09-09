package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.helpers.SFARenderHelper;
import noelflantier.sfartifacts.common.tileentities.TileRecharger;

public class RenderRecharger extends TileEntitySpecialRenderer<TileRecharger>{
	private IModel model;
    private IBakedModel bakedModel;

    private IBakedModel getBakedModel() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedModel == null) {
            try {
                model = ModelLoaderRegistry.getModel(new ResourceLocation(Ressources.MODID, "block/recharger-pilon.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        return bakedModel;
    }
    
    private void renderPilon(TileRecharger te) {
        GlStateManager.pushMatrix();
        	float speed = 0F;
			if(te.isRecharging)
				speed = 10F;
			
	        long angle = (System.currentTimeMillis() / 10) % 360  * (long)speed ;
	        GlStateManager.rotate(angle, 0, 1, 0);
		    
	        RenderHelper.disableStandardItemLighting();
	        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	        if (Minecraft.isAmbientOcclusionEnabled()) {
	            GlStateManager.shadeModel(GL11.GL_SMOOTH);
	        } else {
	            GlStateManager.shadeModel(GL11.GL_FLAT);
	        }

	        World world = te.getWorld();
	        GlStateManager.translate(-te.getPos().getX()-0.5, -te.getPos().getY()-0.5, -te.getPos().getZ()-0.5);
	        Tessellator tessellator = Tessellator.getInstance();
	        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
	        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
	                world,
	                getBakedModel(),
	                world.getBlockState(te.getPos()),
	                te.getPos(),
	                Tessellator.getInstance().getBuffer(),false);
	        tessellator.draw();
	
	        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
	
    @Override
    public void renderTileEntityAt(TileRecharger te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x+0.5, y+0.5, z+0.5);
        GlStateManager.disableRescaleNormal();

        
        GL11.glDisable(GL11.GL_LIGHTING);
        SFARenderHelper.setLigtforTESR(te.getWorld(), te.getPos(), 0, 15728640);
        renderPilon(te);
        GL11.glEnable(GL11.GL_LIGHTING); 
        
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }
}
