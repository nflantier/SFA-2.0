package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.entities.EntityHammerMinning;
import noelflantier.sfartifacts.common.handlers.ModItems;

@SideOnly(Side.CLIENT)
public class RenderEntityHammerMining<T extends EntityHammerMinning>  extends Render<T>{
	
	public RenderEntityHammerMining(RenderManager renderManager) {
		super(renderManager);
	}

	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
		GL11.glRotatef(entity.rotationYaw,0F, 1F,0F);
		GL11.glRotatef(-entity.rotationPitch+90+180,1F, 0F,0F);
		GL11.glRotatef(90,0F, 1F,0F);
		GL11.glScalef(1.2F, 1.2F,1.2F);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        Minecraft.getMinecraft().getRenderItem().renderItem(this.getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);

        if (this.renderOutlines)
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
        return new ItemStack(ModItems.itemThorHammer);
    }


	@Override
	protected ResourceLocation getEntityTexture(T entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}
