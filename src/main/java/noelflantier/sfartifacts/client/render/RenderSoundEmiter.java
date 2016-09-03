package noelflantier.sfartifacts.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import noelflantier.sfartifacts.common.helpers.SFARenderHelper;
import noelflantier.sfartifacts.common.tileentities.LivingEntitySpawnerBase;
import noelflantier.sfartifacts.common.tileentities.TileSoundEmiter;

public class RenderSoundEmiter extends TileEntitySpecialRenderer<TileSoundEmiter>{

    @Override
    public void renderTileEntityAt(TileSoundEmiter te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5F, (float)y+1, (float)z + 0.5F);
        if(te.isEmitting){
            SFARenderHelper.setLigtforTESR(te.getWorld(), te.getPos(), 0, 15728640);
        	renderMob(te.spawnerBaseLogic, x, y, z, partialTicks);
        }
        GlStateManager.popMatrix();
    }

    /**
     * Render the mob inside the mob spawner.
     */
    public static void renderMob(LivingEntitySpawnerBase mobSpawnerLogic, double posX, double posY, double posZ, float partialTicks)
    {
        Entity entity = mobSpawnerLogic.getCachedEntity();

        if (entity != null)
        {
            float f = 0.53125F;
            float f1 = Math.max(entity.width, entity.height);

            if ((double)f1 > 1.0D)
            {
                f /= f1;
            }

            GlStateManager.translate(0.0F, 0.4F, 0.0F);
            GlStateManager.rotate((float)(mobSpawnerLogic.getPrevMobRotation() + (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * (double)partialTicks) * 10.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.2F, 0.0F);
            GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(f, f, f);
            entity.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
            Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
        }
    }

}
