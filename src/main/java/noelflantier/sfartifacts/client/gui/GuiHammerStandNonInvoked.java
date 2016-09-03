package noelflantier.sfartifacts.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.common.container.ContainerHammerStandNonInvoked;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketInvokStarting;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;

public class GuiHammerStandNonInvoked  extends GuiSFA{

	public TileHammerStand tile;
	public boolean statusStandConnected;
	public static final ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiDefaultBackground.png");
	
	public GuiHammerStandNonInvoked(InventoryPlayer inventory, TileHammerStand tile) {
		super(new ContainerHammerStandNonInvoked(inventory, tile));
		this.tile = tile;
		this.statusStandConnected = tile.hasMaster();
		this.xSize = 220;
		this.ySize = 100;
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();

		if(this.statusStandConnected){

			if(!this.tile.isPillarHasEnoughEnergy()){
				this.componentList.put("txt", new GuiComponent(10 , 15){{
					addText("To invok an hammer you need",0,0);
					addText("at least "+String.format("%,d", ModConfig.rfNeededThorHammer)+" RF stored",0,0);
					addText("in the pillar maybe build a bigger pillar!",0,0);
				}});
			}else{
				this.componentList.put("txt", new GuiComponent(10 , 15){{
					addText("Before invoking the hammer you",0,0);
					addText("should know that it will may produce",0,0);
					addText("some explosion around the stand, dont",0,0);
					addText("worry about asgardian stuff they are",0,0);
					addText("very stronk.",0,0);
				}});
			}
			this.componentList.put("invok", new GuiComponent(0 , 0){{
				addSfaButton(0,guiLeft+10,guiTop+65,100,20,"Invok the Hammer!");
			}});
		}else{

			this.componentList.put("txt", new GuiComponent(10 , 25){{
				addText("This hammer stand is not connected,",0,0);
				addText("you need to connect it to a pillar by",0,0);
				addText("right clicking on both of the structures",0,0);
				addText("with a basic hammer, then you will be",0,0);
				addText("able to invok an hammer from it.",0,0);
			}});
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0){
			if(this.statusStandConnected && this.tile.isPillarHasEnoughEnergy()){
				PacketHandler.INSTANCE.sendToServer(new PacketInvokStarting(this.tile));
				this.tile.isInvoking = true;
			}
        }
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		if(!this.statusStandConnected)return;

		if( tile.hasInvoked || !this.tile.isPillarHasEnoughEnergy() || tile.isInvoking )
			this.getButtonById(0).enabled = false;
		else
			this.getButtonById(0).enabled = true;
	}
		
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void updateToolTips(String key) {		
	}

}
