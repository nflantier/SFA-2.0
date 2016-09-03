package noelflantier.sfartifacts.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.entities.EntityHulk;

@SideOnly(Side.CLIENT)
public class RenderHulk extends RenderLiving<EntityHulk>{

    private static final ResourceLocation hulkTexture = new ResourceLocation(Ressources.MODID+":textures/entities/hulk.png");
    
    public RenderHulk(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelHulk(), 1.0F);
    }
    
    protected void preRenderCallback(EntityHulk p_77041_1_, float p_77041_2_){
        GL11.glScalef(1F, 1F, 1F);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityHulk entity) {
		return hulkTexture;
	}
}
