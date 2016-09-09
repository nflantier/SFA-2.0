package noelflantier.sfartifacts.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.container.ContainerInjector;
import noelflantier.sfartifacts.common.tileentities.TileInjector;

public class GuiInjector extends GuiMachine{

	private static final ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiInjector.png");
	public TileInjector tile;
	
	public GuiInjector(InventoryPlayer inventory, TileInjector tile) {
		super(new ContainerInjector(inventory, tile));
		this.xSize = 176;
		this.ySize = 200;
		this.tile = tile;
	}
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void drawPopUp(int x, int y, int key){
		super.drawPopUp(x,y,key);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	public void loadComponents(){
		super.loadComponents();
		this.componentList.put("tank", 
				new GuiToolTips(guiLeft+8, guiTop+23, 14, 47, this.width)
				);

		this.componentList.put("energy", 
				new GuiToolTips(guiLeft+25, guiTop+23, 14, 47, this.width)
				);
		
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Injector :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 108, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		
		this.componentManual.put("so", new GuiComponent(guiLeft+12, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine is used to inject liquefied asgardite", 0, 0);
			addText("into items, it use RF and liquefied asgardite.", 0, 0);
		}});
	}


	public void updateToolTips(String key){
		switch(key){
			case "tank" :
				((GuiToolTips)this.componentList.get("tank")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, String.format("%,d", tile.tank.getFluidAmount())+" MB");
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, "/ "+String.format("%,d", this.tile.tank.getCapacity())+" MB");
				break;
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
		GuiRender.renderEnergy(tile.storage.getMaxEnergyStored(), tile.getEnergyStored(null), guiLeft+25, guiTop+23,this.zLevel, 14, 47, 176, 0);
		for(int i =0;i<tile.isRunning.length;i++){
			if(tile.isRunning[i]){
				GuiRender.renderTask(tile.tickToInject, tile.currentTickToInject[i], guiLeft+94, guiTop+23+i*25, this.zLevel, 22, 16, 176, 47);
			}
		}
		GuiRender.renderFluid(tile.tank, guiLeft+8, guiTop+23, this.zLevel, 14, 47);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}

}
