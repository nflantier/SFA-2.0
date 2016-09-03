package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.common.entities.EntityHoverBoard;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.items.ItemHoverBoard;

public class RenderEntityHoverBoard<T extends EntityHoverBoard>  extends Render<T>{
	
	public RenderEntityHoverBoard(RenderManager renderManager) {
		super(renderManager);
	}
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        
        final float rotation = interpolateRotation(entity.prevRotationYaw, entity.rotationYaw, 0);
		GL11.glRotatef(180.0F - rotation+90, 0.0F, 1.0F, 0.0F);
		if(entity.getTypeHoverBoard()==ItemHoverBoard.MATTEL_HOVERBOARD)
			GL11.glTranslatef(0, 0.08F, 0);
		else if(entity.getTypeHoverBoard()==ItemHoverBoard.PITBULL_HOVERBOARD)
			GL11.glTranslatef(+0.3F, 0.15F, 0);
		
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        Minecraft.getMinecraft().getRenderItem().renderItem(this.getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);

        if (renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public ItemStack getStackToRender(T entityIn)
    {
        return new ItemStack(ModItems.itemHoverboard,1,entityIn.getTypeHoverBoard()*2);
    }
	
	private static float interpolateRotation(float prevRotation, float nextRotation, float modifier) {
		float rotation = nextRotation - prevRotation;

		while (rotation < -180.0F)
			rotation += 360.0F;

		while (rotation >= 180.0F) {
			rotation -= 360.0F;
		}

		return prevRotation + modifier * rotation;
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
