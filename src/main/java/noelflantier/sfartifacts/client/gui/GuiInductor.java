package noelflantier.sfartifacts.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiButtonImage;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiImage;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.common.container.ContainerInductor;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketInductorGui;
import noelflantier.sfartifacts.common.tileentities.TileInductor;

public class GuiInductor  extends GuiSFA{

	private static final ResourceLocation guiselements = new ResourceLocation(Ressources.MODID+":textures/gui/guisElements.png");
	private static final ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiInductor.png");
	TileInductor tile;
	
	public GuiInductor(InventoryPlayer inventory, TileInductor tile) {
		super(new ContainerInductor(inventory, tile));
		this.xSize = 197;
		this.ySize = 135;
		this.tile = tile;
	}
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(button.id==0){
			this.tile.canWirelesslySend = !tile.canWirelesslySend;
			PacketHandler.INSTANCE.sendToServer(new PacketInductorGui(tile,0,tile.canWirelesslySend));
		}else if(button.id==1){
			this.tile.canWirelesslyRecieve = !tile.canWirelesslyRecieve;
			PacketHandler.INSTANCE.sendToServer(new PacketInductorGui(tile,1,tile.canWirelesslyRecieve));
		}else if(button.id==2){
			this.tile.canSend = !tile.canSend;
			PacketHandler.INSTANCE.sendToServer(new PacketInductorGui(tile,2,tile.canSend));
		}else if(button.id==3){
			this.tile.canRecieve = !tile.canRecieve;
			PacketHandler.INSTANCE.sendToServer(new PacketInductorGui(tile,3,tile.canRecieve));
		}
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Inductor :", 0, 0);
		}});
		
		this.componentList.put("btws", new GuiComponent(0,0){{
			addImageButton(new GuiButtonImage(0,guiLeft+50,guiTop+35, 22, 20, new GuiImage(45, 29, 32,32 , 0F, 0F, 1F, 1F,guiselements)), 0.5F,0.25F,4,4, tile.canWirelesslySend);
		}});

		this.componentList.put("btwr", new GuiComponent(0,0){{
			addImageButton(new GuiButtonImage(1,guiLeft+73,guiTop+35, 22, 20, new GuiImage(68, 29, 32,32 , 0F, 0F, 1F, 1F,guiselements)), 0.5F,0F,4,4, tile.canWirelesslyRecieve);
		}});
		
		this.componentList.put("btds", new GuiComponent(0,0){{
			addImageButton(new GuiButtonImage(2,guiLeft+96,guiTop+35, 22, 20, new GuiImage(91, 29, 32,32 , 0F, 0F, 1F, 1F,guiselements)), 0F,0.5F,4,4, tile.canSend);
		}});
		
		this.componentList.put("btdr", new GuiComponent(0,0){{
			addImageButton(new GuiButtonImage(3,guiLeft+119,guiTop+35, 22, 20, new GuiImage(114, 29, 32,32 , 0F, 0F, 1F, 1F,guiselements)), 0.5F,0.5F,4,4, tile.canRecieve);
		}});

		this.componentList.put("t0", new GuiComponent(6, 15){{
			addText("Max transfer rate :", 10, 0);
			addText(tile.energyCapacity+" RF/T or "+(tile.energyCapacity/4)+" EU/T", 10, 0);
		}});
		this.componentList.put("t", new GuiComponent(6, 60){{
			addText("1.Send wireless energy", 10, 0);
			addText("2.Recieve wireless energy",10, 0);
			addText("3.Extract energy to the "+tile.facing.getOpposite().name(), 10, 0);
			addText("block", 10, 0);
			addText("4.Recieve energy from the "+tile.facing.getOpposite().name(), 10, 0);
			addText("block", 10, 0);
		}});
	}

	
	@Override
	public void updateToolTips(String key) {
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
	}
}