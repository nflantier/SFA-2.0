package noelflantier.sfartifacts.client.gui;

import java.io.IOException;
import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiButtonImage;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiImage;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.container.ContainerMightyFoundry;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketMightyFoundryGui;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.tileentities.TileMightyFoundry;

public class GuiMightyFoundry extends GuiMachine{
	
	private static final ResourceLocation guiselements = new ResourceLocation(Ressources.MODID+":textures/gui/guisElements.png");
	private static final ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiMightyFoundry.png");
	TileMightyFoundry tile;
	public int stId = 36;
	public boolean hasMold = false;
	public final GuiImage guiElements = new GuiImage(0, 0, 128,128 , 0F, 0F, 1F, 1F,guiselements);
	
	public GuiMightyFoundry(InventoryPlayer inventory, TileMightyFoundry tile) {
		super(new ContainerMightyFoundry(inventory, tile));
		this.xSize = 176;
		this.ySize = 200;
		this.tile = tile;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.hasMold = this.inventorySlots.getSlot(this.stId+1).getHasStack();
		
		if(this.hasMold){
			this.getButtonById(0).enabled = true;
			int mid = ItemNBTHelper.getInteger(this.inventorySlots.getSlot(this.stId+1).getStack(), "idmold", 0);
			ISFARecipe r = RecipesRegistry.instance.getRecipeWithMoldMeta(mid);
			if(r!=null)
				this.componentList.get("wca").replaceString(0, ""+r.getUid());
			
			DecimalFormat f = new DecimalFormat();
			f.setMaximumFractionDigits(2);
			this.componentList.get("wpr").replaceString(0, ""+f.format(tile.progression*100)+"%");
		}else{
			this.getButtonById(0).enabled =false;
			this.componentList.get("wca").replaceString(0, "Nothing...");
			this.componentList.get("wpr").replaceString(0, "0%");
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		this.hasMold = this.inventorySlots.getSlot(this.stId+1).getHasStack();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(button.id==0){
			if(this.tile.isLocked){
				PacketHandler.INSTANCE.sendToServer(new PacketMightyFoundryGui(this.tile, 2));
				this.tile.isLocked = false;
			}else{
				PacketHandler.INSTANCE.sendToServer(new PacketMightyFoundryGui(this.tile, 1));
				this.tile.isLocked = true;
			}
		}
	}
	
	@Override
	public void updateToolTips(String key) {		
		switch(key){
			case "tank" :
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, String.format("%,d", this.tile.tank.getFluidAmount())+" MB");
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, "/ "+String.format("%,d", this.tile.tank.getCapacity())+" MB");
				break;
			case "energy" :
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, String.format("%,d", this.tile.getEnergyStored(null))+" RF");
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, "/ "+String.format("%,d", this.tile.getMaxEnergyStored(null))+" RF");
				break;
			default:
				break;
		}
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
		
		this.componentList.put("btblock", new GuiComponent(0,0){{
			addImageButton(new GuiButtonImage(0,guiLeft+68,guiTop+22, 22, 20, new GuiImage(64, 16, 32,32 , 0F, 0F, 1F, 1F,guiselements)), 0,0,4,4, !tile.isLocked);
		}});
		
		this.componentList.put("mf", new GuiComponent(6, 5, 100, 10){{
			addText("Mighty Foundry :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 108, 100, 10){{
			addText("Inventory :", 0, 0);
		}});
		
		this.componentList.put("cast", new GuiComponent(42, 45, 100, 10){{
			addText("Casting :", 0, 0);
		}});
		this.componentList.put("wca", new GuiComponent(42, 55, 100, 10){{
			defColor = TextFormatting.GRAY;
			addText("Nothing...", 0, 0);
		}});
		this.componentList.put("prog", new GuiComponent(42, 75, 100, 10){{
			addText("Progression :", 0, 0);
		}});
		this.componentList.put("wpr", new GuiComponent(42, 85, 100, 10){{
			defColor = TextFormatting.GRAY;
			addText("0%", 0, 0);
		}});

		this.componentManual.put("so", new GuiComponent(guiLeft+12, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine is used to forge items.", 0, 0);
			addText("To do so you need a valid mold and the materials", 0, 0);
			addText("needed to forge the item linked to the mold.", 0, 0);
			addText("The mold need to be locked for the foundry to", 0, 0);
			addText("start forging.", 0, 0);
			addText("Adding lava will make the foundry really faster.", 0, 0);
		}});
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
		GuiRender.renderEnergy(tile.storage.getMaxEnergyStored(), tile.getEnergyStored(null), guiLeft+25, guiTop+23,this.zLevel, 14, 47, 176, 0);
		if(tile.progression>0)
			GuiRender.renderTask(100, 100-(int)(tile.progression*100), guiLeft+119, guiTop+76, this.zLevel, 22, 16, 176, 47);
		GuiRender.renderFluid(tile.tank, guiLeft+8, guiTop+23, this.zLevel, 14, 47);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);

	}


}
