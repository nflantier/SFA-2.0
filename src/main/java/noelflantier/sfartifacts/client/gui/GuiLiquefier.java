package noelflantier.sfartifacts.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.container.ContainerLiquefier;
import noelflantier.sfartifacts.common.tileentities.TileLiquefier;

public class GuiLiquefier extends GuiMachine{

	private static final ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiLiquefier.png");
	TileLiquefier tile;
	
	public GuiLiquefier(InventoryPlayer inventory, TileLiquefier tile) {
		super(new ContainerLiquefier(inventory, tile));
		this.xSize = 176;
		this.ySize = 200;
		this.tile = tile;
	}

	@Override
	public void loadComponents(){
		super.loadComponents();
		GuiComponent gc = new GuiToolTips(guiLeft+8, guiTop+23, 14, 47, this.width);
		this.componentList.put("tankmelt", gc);
		
		GuiComponent gc2 = new GuiToolTips(guiLeft+25, guiTop+23, 14, 47, this.width);
		this.componentList.put("energy", gc2);
		
		GuiComponent gc3 = new GuiToolTips(guiLeft+135, guiTop+23, 27, 47, this.width);
		this.componentList.put("tank", gc3);
		
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Liquefier :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 108, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		
		this.componentManual.put("so", new GuiComponent(guiLeft+12, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine will turn asgardite into liquefied", 0, 0);
			addText("asgardite, it will use RF an water to do so.", 0, 0);
			addText("If you put water source under this machine it will", 0, 0);
			addText("passively drain from it, it can drain from a 3x3", 0, 0);
			addText("zone right under it.", 0, 0);
		}});
	}
	
	public void updateToolTips(String key){
		switch(key){
			case "tankmelt" :
				((GuiToolTips)this.componentList.get("tankmelt")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("tankmelt")).addContent(this.fontRendererObj, String.format("%,d", this.tile.tankWater.getFluidAmount())+" MB");
				((GuiToolTips)this.componentList.get("tankmelt")).addContent(this.fontRendererObj, "/ "+String.format("%,d", this.tile.tankWater.getCapacity())+" MB");
				break;
			case "energy" :
				((GuiToolTips)this.componentList.get("energy")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, String.format("%,d", this.tile.getEnergyStored(null))+" RF");
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, "/ "+String.format("%,d", this.tile.getMaxEnergyStored(null))+" RF");
				break;
			case "tank" :
				((GuiToolTips)this.componentList.get("tank")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, String.format("%,d", this.tile.tank.getFluidAmount())+" MB");
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, "/ "+String.format("%,d", this.tile.tank.getCapacity())+" MB");
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
		if(tile.isRunning)
			GuiRender.renderTask(tile.tickToMelt, tile.currentTickToMelt, guiLeft+92, guiTop+37, this.zLevel, 22, 16, 176, 47);
		GuiRender.renderFluid(tile.tank, guiLeft+135, guiTop+23, this.zLevel, 27, 47);
		GuiRender.renderFluid(tile.tankWater, guiLeft+8, guiTop+23, this.zLevel, 14, 47);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}

}
