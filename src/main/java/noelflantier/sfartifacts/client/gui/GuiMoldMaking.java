package noelflantier.sfartifacts.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiImage;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.common.container.ContainerMoldMaking;

public class GuiMoldMaking  extends GuiSFA{

	private static final ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiMoldMaking.png");
	private static final ResourceLocation moldFilled = new ResourceLocation(Ressources.MODID+":textures/items/mold_filled.png");
	private static final ResourceLocation moldEmpty = new ResourceLocation(Ressources.MODID+":textures/items/mold_empty.png");
	public int currentSlot;
	public int moldMeta;
	
	public GuiMoldMaking(EntityPlayer player) {
		super(new ContainerMoldMaking(player));
		this.currentSlot = player.inventory.currentItem;
		this.moldMeta = player.getHeldItemMainhand().getItemDamage();
		this.xSize = 176;
		this.ySize = 200;
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();
		this.componentList.put("im", new GuiComponent(6, 108, 100, 10){{
			addImage(new GuiImage(8+18*currentSlot, 176, 16,16 , 0F, 0F, 1F, 1F,moldMeta==0?moldEmpty:moldFilled));
			}}
		);
		//GuiRecipe gr = new GuiRecipe(new ItemStack(Blocks.furnace),GuiRecipe.TYPE.VANILLA);
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
