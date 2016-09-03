package noelflantier.sfartifacts.common.handlers;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.client.render.RenderEntityHammerInvoking;
import noelflantier.sfartifacts.client.render.RenderEntityHammerMining;
import noelflantier.sfartifacts.client.render.RenderEntityHoverBoard;
import noelflantier.sfartifacts.client.render.RenderHulk;
import noelflantier.sfartifacts.client.render.RenderEntityShieldThrow;
import noelflantier.sfartifacts.common.entities.EntityHammerInvoking;
import noelflantier.sfartifacts.common.entities.EntityHammerMinning;
import noelflantier.sfartifacts.common.entities.EntityHoverBoard;
import noelflantier.sfartifacts.common.entities.EntityHulk;
import noelflantier.sfartifacts.common.entities.EntityShieldThrow;

public class ModEntities {

    @SideOnly(Side.CLIENT)
	public static void preInitRenderEntities() {
        RenderingRegistry.registerEntityRenderingHandler(EntityHammerMinning.class, new IRenderFactory<EntityHammerMinning>() {
            @Override
            public Render<? super EntityHammerMinning> createRenderFor(RenderManager manager) {
                return new RenderEntityHammerMining(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityShieldThrow.class, new IRenderFactory<EntityShieldThrow>() {
            @Override
            public Render<? super EntityShieldThrow> createRenderFor(RenderManager manager) {
                return new RenderEntityShieldThrow(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityHoverBoard.class, new IRenderFactory<EntityHoverBoard>() {
            @Override
            public Render<? super EntityHoverBoard> createRenderFor(RenderManager manager) {
                return new RenderEntityHoverBoard(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityHammerInvoking.class, new IRenderFactory<EntityHammerInvoking>() {
            @Override
            public Render<? super EntityHammerInvoking> createRenderFor(RenderManager manager) {
                return new RenderEntityHammerInvoking(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityHulk.class, new IRenderFactory<EntityHulk>() {
            @Override
            public Render<? super EntityHulk> createRenderFor(RenderManager manager) {
                return new RenderHulk(manager);
            }
        });
		
	}

    @SideOnly(Side.CLIENT)
	public static void initRenderEntities() {
		
	}

	public static void preInitEntitites() {
		createEntity(EntityHammerMinning.class,"thorhammerthrow", 0, 100, 5, true);
		createEntity(EntityHammerInvoking.class,"thorhammerinvoking", 1, 100, 5, true);
		createEntity(EntityShieldThrow.class,"vibraniumshieldthrow", 4, 100, 5, true);
		createEntity(EntityHulk.class,"hulk", 5, 100, 1, true, 0x00CC00, 0x339933);
		createEntity(EntityHoverBoard.class, "hoverboard", 6, 64, 1, true);
	}

	public static void createEntity(Class <? extends Entity > entityClass, String entityName, int id, int dist, int freq, boolean velo, int c1, int c2){
		EntityRegistry.registerModEntity(entityClass, entityName, id, SFArtifacts.instance, dist, freq, velo, c1, c2);
	}
	public static void createEntity(Class <? extends Entity > entityClass, String entityName, int id, int dist, int freq, boolean velo){
		EntityRegistry.registerModEntity(entityClass, entityName, id, SFArtifacts.instance, dist, freq, velo);
	}

}
