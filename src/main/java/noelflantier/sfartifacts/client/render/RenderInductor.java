package noelflantier.sfartifacts.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import noelflantier.sfartifacts.common.tileentities.TileInductor;

public class RenderInductor extends TileEntitySpecialRenderer<TileInductor>{
    @Override
    public void renderTileEntityAt(TileInductor te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);

        if(te.canWirelesslySend || te.canWirelesslyRecieve){
        	System.out.println("gg");
        	long angle = (System.currentTimeMillis() / 10) % 360;
        	GlStateManager.rotate(angle, 0, 1, 0);
        }
        
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }
}
