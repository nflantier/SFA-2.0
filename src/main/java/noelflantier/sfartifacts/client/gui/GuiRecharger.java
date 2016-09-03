package noelflantier.sfartifacts.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.container.ContainerRecharger;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketRechargerGui;
import noelflantier.sfartifacts.common.tileentities.TileRecharger;

public class GuiRecharger  extends GuiMachine{
	private static final ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiRecharger.png");
	TileRecharger tile;
	
	public GuiRecharger(InventoryPlayer inventory, TileRecharger tile) {
		super(new ContainerRecharger(inventory, tile));
		this.xSize = 176;
		this.ySize = 160;
		this.tile = tile;
		hasSidedBt[sidedButton.get(machineButtonO1)] = true;
		sidedBtHasPopUp[sidedButton.get(machineButtonO1)] = true;
		sidedBtTick[sidedButton.get(machineButtonO1)] = "W";
		sidedBtTock[sidedButton.get(machineButtonO1)] = "W";
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);	
		if(button.id==machineButtonO1){
			this.tile.wirelessRechargingEnable = !this.tile.wirelessRechargingEnable;
			PacketHandler.INSTANCE.sendToServer(new PacketRechargerGui(this.tile));
		}
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();

		GuiComponent gc = new GuiToolTips(guiLeft+26, guiTop+15, 14, 70, this.width);
		this.componentList.put("energy", gc);
		
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Recharger :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 67, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		this.componentManual.put("so", new GuiComponent(guiLeft+12, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine can recharge all your energy items", 0, 0);//f
			addText("(RF and EU), if you are close enough it will", 0, 0);
			addText("recharge them wirelessly directly from player,", 0, 0);
			addText("armor or bauble inventories. To disable the", 0, 0);
			addText("wireless recharging click on the W button.", 0, 0);
		}});
	}
	
	@Override
	public void updateToolTips(String key) {
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
		GuiRender.renderEnergy(tile.storage.getMaxEnergyStored(), tile.getEnergyStored(null), guiLeft+27, guiTop+16,this.zLevel, 14, 47, 176, 0);
		if(this.tile.wirelessRechargingEnable)
			GuiRender.renderVerticalRectangle(100, 100, guiLeft+48, guiTop+23, this.zLevel, 18, 16, 176, 47);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}
}
