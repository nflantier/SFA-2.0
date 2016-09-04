package noelflantier.sfartifacts.client.render;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.BlockInductor;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumTypeTech;
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
