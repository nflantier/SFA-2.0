package noelflantier.sfartifacts.common.handlers;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.client.render.RenderHammerStand;
import noelflantier.sfartifacts.client.render.RenderInjector;
import noelflantier.sfartifacts.client.render.RenderLightningRodStand;
import noelflantier.sfartifacts.client.render.RenderLiquefier;
import noelflantier.sfartifacts.client.render.RenderMightyFoundry;
import noelflantier.sfartifacts.client.render.RenderRecharger;
import noelflantier.sfartifacts.client.render.RenderSoundEmiter;
import noelflantier.sfartifacts.client.render.RenderTileRenderPillar;
import noelflantier.sfartifacts.common.helpers.RegisterHelper;
import noelflantier.sfartifacts.common.tileentities.TileControlPanel;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;
import noelflantier.sfartifacts.common.tileentities.TileInductor;
import noelflantier.sfartifacts.common.tileentities.TileInjector;
import noelflantier.sfartifacts.common.tileentities.TileLightningRodStand;
import noelflantier.sfartifacts.common.tileentities.TileLiquefier;
import noelflantier.sfartifacts.common.tileentities.TileMightyFoundry;
import noelflantier.sfartifacts.common.tileentities.TileMrFusion;
import noelflantier.sfartifacts.common.tileentities.TileRecharger;
import noelflantier.sfartifacts.common.tileentities.TileSoundEmiter;
import noelflantier.sfartifacts.common.tileentities.pillar.TileBlockPillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileInterfacePillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;
import noelflantier.sfartifacts.common.tileentities.pillar.TileRenderPillarModel;

public class ModTileEntities {

    @SideOnly(Side.CLIENT)
	public static void initRenderTileEntities() {
		
	}

	public static void preInitTileEntitites() {
    	RegisterHelper.registerTileEntity(TileBlockPillar.class,"TileBlockPillar");
    	RegisterHelper.registerTileEntity(TileInterfacePillar.class,"TileInterfacePillar");
    	RegisterHelper.registerTileEntity(TileMasterPillar.class,"TileMasterPillar");
    	RegisterHelper.registerTileEntity(TileRenderPillarModel.class,"TileRenderPillarModel");
    	RegisterHelper.registerTileEntity(TileInductor.class,"TileInductor");
    	RegisterHelper.registerTileEntity(TileHammerStand.class,"TileHammerStand");
    	RegisterHelper.registerTileEntity(TileInjector.class,"TileInjector");
    	RegisterHelper.registerTileEntity(TileControlPanel.class,"TileControlPanel");
    	RegisterHelper.registerTileEntity(TileLiquefier.class,"TileLiquefier");
    	RegisterHelper.registerTileEntity(TileMightyFoundry.class,"TileMightyFoundry");
    	RegisterHelper.registerTileEntity(TileMrFusion.class,"TileMrFusion");
    	RegisterHelper.registerTileEntity(TileRecharger.class,"TileRecharger");
    	RegisterHelper.registerTileEntity(TileLightningRodStand.class,"TileLightningRodStand");
    	RegisterHelper.registerTileEntity(TileSoundEmiter.class,"TileSoundEmiter");
	}

    @SideOnly(Side.CLIENT)
	public static void preInitRenderTileEntities() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileRenderPillarModel.class, new RenderTileRenderPillar());
		if(ModConfig.useTESR){
			//ClientRegistry.bindTileEntitySpecialRenderer(TileInductor.class, new RenderInductor());
			ClientRegistry.bindTileEntitySpecialRenderer(TileInjector.class, new RenderInjector());
			ClientRegistry.bindTileEntitySpecialRenderer(TileLiquefier.class, new RenderLiquefier());
			ClientRegistry.bindTileEntitySpecialRenderer(TileMightyFoundry.class, new RenderMightyFoundry());
			ClientRegistry.bindTileEntitySpecialRenderer(TileLightningRodStand.class, new RenderLightningRodStand());
			ClientRegistry.bindTileEntitySpecialRenderer(TileHammerStand.class, new RenderHammerStand());
		}
		ClientRegistry.bindTileEntitySpecialRenderer(TileRecharger.class, new RenderRecharger());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSoundEmiter.class, new RenderSoundEmiter());
	}

}
