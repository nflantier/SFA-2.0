package noelflantier.sfartifacts.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.container.ContainerLightningRodStand;
import noelflantier.sfartifacts.common.tileentities.TileLightningRodStand;

public class GuiLightningRodStand extends GuiMachine{

	private static final ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiLightningRodStand.png");
	TileLightningRodStand tile;
	
	public GuiLightningRodStand(InventoryPlayer inventory, TileLightningRodStand tile) {
		super(new ContainerLightningRodStand(inventory, tile));
		this.xSize = 176;
		this.ySize = 160;
		this.tile = tile;
	}

	@Override
	public void loadComponents(){
		super.loadComponents();
		
		this.componentList.put("energy", new GuiToolTips(guiLeft+60, guiTop+15, 14, 47, this.width));
		
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Lightning Rod Stand :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 68, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		this.componentManual.put("so", new GuiComponent(guiLeft+12, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine will passively generate RF if there is", 0, 0);
			addText("a lightning rod in it.", 0, 0);
		}});
	}


	public void updateToolTips(String key){
		switch(key){
			case "energy" :
				((GuiToolTips)this.componentList.get("energy")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, String.format("%,d", this.tile.getEnergyStored(null))+" RF");
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, "/ "+String.format("%,d", this.tile.getMaxEnergyStored(null))+" RF");
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {

		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
		GuiRender.renderEnergy(tile.storage.getMaxEnergyStored(), tile.getEnergyStored(null), guiLeft+61, guiTop+16,this.zLevel, 14, 47, 176, 0);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}

}
