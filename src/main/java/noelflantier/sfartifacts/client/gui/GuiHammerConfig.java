package noelflantier.sfartifacts.client.gui;

import java.io.IOException;
import java.util.Enumeration;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.client.gui.bases.GuiScrollable;
import noelflantier.sfartifacts.common.container.ContainerHammerConfig;
import noelflantier.sfartifacts.common.container.ContainerHammerStandInvoked;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEnchantHammer;
import noelflantier.sfartifacts.common.network.messages.PacketUpgradeHammer;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;

public class GuiHammerConfig extends GuiSFA{
	
	private static final ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiHammerConfig.png");
	private static final ResourceLocation bgroundInStand = new ResourceLocation(Ressources.MODID+":textures/gui/guiHammerStandInvoked.png");
	public InventoryPlayer inv;
	public boolean inHammerStand;
	public TileHammerStand tile;
	public ItemStack hammer;
	public int globalXMarge = 0;
	public int globalYMarge = 0;
	public int tickInput = 0;
	public GuiScrollable enchantSc = new GuiScrollable(10);
	
	public int xR = 0;
	public int yR = -6;
	public boolean componentloaded = false;
	public static int globalBtId = -1;
	public boolean enchantOpen = false;
	
	public GuiHammerConfig(InventoryPlayer inventory, TileHammerStand tile) {
		super(new ContainerHammerStandInvoked(inventory, tile));
		this.tile = tile;
		this.xSize = 222;
		this.ySize = 194;
		this.inv = inventory;
		this.inHammerStand = true;
		if(this.tile.items[0]!= null)
			this.hammer = this.tile.items[0];
		this.globalXMarge = 30;
		this.globalYMarge = 0;
	}
	
	public GuiHammerConfig(InventoryPlayer inventory) {
		super(new ContainerHammerConfig(inventory));
		this.xSize = 222;
		this.ySize = 194;
		this.inv = inventory;
		this.inHammerStand = false;
		this.hammer = this.inv.getCurrentItem();
		this.tile = null;
		this.globalXMarge = 5;
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		if(this.inHammerStand){
			if(this.hammer!=null && this.tile.items[0]==null){
				this.componentList.clear();
				this.enchantSc.componentList.clear();
				this.buttonList.clear();
				this.enchantOpen = false;
			}
			if(this.hammer==null && this.tile.items[0]!=null){
				this.hammer = this.tile.items[0];
				this.componentloaded  = false;
				this.setWorldAndResolution(mc, this.width, this.height);
			}
			this.hammer = this.tile.items[0];
		}
		
		if(this.hammer==null) return;

		if(this.enchantOpen)
			this.enchantSc.input();
		
		if(this.hammer.getItem() instanceof ItemThorHammer){
			this.componentList.get("radius").replaceString(0, "Radius : "+ItemNBTHelper.getInteger(this.hammer, "Radius", 0));
			((GuiButton)this.buttonList.get(0)).visible = true;
			((GuiButton)this.buttonList.get(1)).visible = true;

			if(!ItemNBTHelper.getBoolean(this.hammer, "CanMagnet",false)){
				((GuiButton)this.buttonList.get(2)).visible = true;
				((GuiButton)this.buttonList.get(2)).enabled = false;
				((GuiButton)this.buttonList.get(3)).visible = false;
			}else{
				if(ItemNBTHelper.getBoolean(this.hammer, "IsMagnetOn", false)){
					((GuiButton)this.buttonList.get(2)).visible = false;
					((GuiButton)this.buttonList.get(3)).visible = true;
					((GuiButton)this.buttonList.get(3)).enabled = true;
				}else{
					((GuiButton)this.buttonList.get(2)).enabled = true;
					((GuiButton)this.buttonList.get(2)).visible = true;
					((GuiButton)this.buttonList.get(3)).visible = false;
				}
			}

			if(ItemNBTHelper.getTagList(this.hammer, "EnchStored", 10).tagCount()>0){
				((GuiButton)this.buttonList.get(4)).visible = true;
			}
		}
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(this.hammer==null) return;

		if(this.hammer.getItem() instanceof ItemThorHammer){
			if(!this.enchantOpen){
				if (button.id == 0){
					PacketHandler.INSTANCE.sendToServer(new PacketUpgradeHammer(this.tile, 1, this.inHammerStand));
					int r = ItemNBTHelper.getInteger(this.hammer, "Radius", 0)-1;
					if(!this.inHammerStand){
						ItemNBTHelper.setInteger(this.hammer, "Radius", r<0?0:r);
					}
				}else if (button.id == 1){
					PacketHandler.INSTANCE.sendToServer(new PacketUpgradeHammer(this.tile, 2, this.inHammerStand));
					int r1 = ItemNBTHelper.getInteger(this.hammer, "Radius", 0)+1;
					if(!this.inHammerStand){
						ItemNBTHelper.setInteger(this.hammer, "Radius", r1>5?5:r1);
					}
				}else if (button.id == 2){
					PacketHandler.INSTANCE.sendToServer(new PacketUpgradeHammer(this.tile, 3, this.inHammerStand));	
					if(!this.inHammerStand)ItemNBTHelper.setBoolean(this.hammer, "IsMagnetOn", true);
				}else if (button.id == 3){
					PacketHandler.INSTANCE.sendToServer(new PacketUpgradeHammer(this.tile, 4, this.inHammerStand));
					if(!this.inHammerStand)ItemNBTHelper.setBoolean(this.hammer, "IsMagnetOn", false);
				}
			}
			if(button.id == 4){
				this.enchantOpen = this.enchantOpen?false:true;
			}
		}
	}
	
	public int nextY(){
		this.yR += 22;
		return this.yR;
	}
	
	public void loadComponents(){
		super.loadComponents();
		this.componentList.put("title", new GuiComponent(this.globalXMarge, 6, 100, 10){{
			addText("Equipment :", 0, 0);
		}});
		
		if(this.hammer==null) return;
		int globalid = 0;
		this.yR = 0;
		if(this.hammer.getItem() instanceof ItemThorHammer){
			GuiComponent gc = new GuiComponent(this.globalXMarge, this.nextY());
			gc.addButton(globalid++,guiLeft+65,guiTop+0,20,20,"-");
			gc.addButton(globalid++,guiLeft+86,guiTop+0,20,20,"+");
			gc.addText("Radius : "+ItemNBTHelper.getInteger(this.hammer, "Radius", 5), 5,8);
	    	gc.defColor=TextFormatting.DARK_GRAY;
			this.componentList.put("radius", gc);

			GuiComponent gc2 = new GuiComponent(this.globalXMarge, this.nextY());
			int magnetmarge = this.hammer!=null && ItemNBTHelper.getBoolean(this.hammer, "CanMagnet",false)?0:15;
			gc2.addButton(globalid++,guiLeft+110+magnetmarge,guiTop+0,35,20,"ON");
			gc2.addButton(globalid++,guiLeft+110+magnetmarge,guiTop+0,35,20,"OFF");
			String magnet = ItemNBTHelper.getBoolean(this.hammer, "CanMagnet",false)?"Installed":"Not installed";
			gc2.addText("Magnet : "+magnet, 5,8);
	    	gc2.defColor=TextFormatting.DARK_GRAY;
			this.componentList.put("magnet", gc2);

			GuiComponent gc3 = new GuiComponent(this.globalXMarge, this.nextY());
			String weapon = ItemNBTHelper.getBoolean(this.hammer, "CanThrowLightning",false)?"Installed":"Not installed";
			gc3.addText("Lightning weapon : "+weapon, 5,8);
	    	gc3.defColor=TextFormatting.DARK_GRAY;
			this.componentList.put("weapon", gc3);

			GuiComponent gc5 = new GuiComponent(this.globalXMarge, this.nextY());
			String ttm = ItemNBTHelper.getBoolean(this.hammer, "CanThrowToMove",false)?"Installed":"Not installed";
			gc5.addText("Moving upgrade : "+ttm, 5,8);
	    	gc5.defColor=TextFormatting.DARK_GRAY;
			this.componentList.put("throwtomove", gc5);
			
			int enchadd = 0;
			if(!this.inHammerStand)
				enchadd = 46;
			GuiComponent gc4 = new GuiComponent(this.globalXMarge,0);
			gc4.addButton(globalid++,guiLeft+113+enchadd,guiTop+5,50,20,"Enchant");
	    	gc4.defColor=TextFormatting.DARK_GRAY;
			this.componentList.put("enchant", gc4);
			
			NBTTagList nbttaglist = this.hammer.getTagCompound().getTagList("EnchStored", 10);
	        int y = 10;
	        for(int i = 0 ; i<nbttaglist.tagCount() ; i++){
				Enchantment enchant = Enchantment.getEnchantmentByID(nbttaglist.getCompoundTagAt(i).getInteger("id"));
				String n = enchant.getTranslatedName(nbttaglist.getCompoundTagAt(i).getInteger("lvl"));
		    	GuiComponent gce = new GuiComponent(36, y+26, 150,10);
		    	gce.isLink=true;
		    	gce.defColor=TextFormatting.DARK_GRAY;
				gce.addText(n, 0,0);
				this.enchantSc.addComponent(i, gce);
				y += 10;
	        }
	        this.enchantSc.setPositionSizeAndAlpha(guiLeft+34, guiTop+34, 154, 98, 0.4F);
		}
	}

	@Override
	public void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button);
		if(this.hammer==null)return;
		
		if(this.enchantOpen){
			Enumeration<Integer> enumKeyE = this.enchantSc.componentList.keys();
    		NBTTagList nbttaglist = this.hammer.getTagCompound().getTagList("EnchStored", 10);
    		
			while (enumKeyE.hasMoreElements()) {
			    int key = enumKeyE.nextElement();
		    	if(this.enchantSc.componentList.get(key).isMouseHover(x-guiLeft, y-guiTop)){
		    		if(nbttaglist.getCompoundTagAt(key).getBoolean("enable")){
						PacketHandler.INSTANCE.sendToServer(new PacketEnchantHammer(this.tile, nbttaglist.getCompoundTagAt(key).getInteger("id"), false, this.inHammerStand));
						if(!this.inHammerStand)nbttaglist.getCompoundTagAt(key).setBoolean("enable", false);
		    		}else{
						PacketHandler.INSTANCE.sendToServer(new PacketEnchantHammer(this.tile, nbttaglist.getCompoundTagAt(key).getInteger("id"), true, this.inHammerStand));
						if(!this.inHammerStand)nbttaglist.getCompoundTagAt(key).setBoolean("enable", true);
		    		}
		    	}
			}
		}
	}

	public void drawOver(int x, int y){
		if(this.enchantOpen){
			this.enchantSc.showTheArrows(x, y);
			this.enchantSc.showTheBorders(x, y);
		}
	}
	@Override
    public void drawGuiContainerForegroundLayer(int x, int y){
		super.drawGuiContainerForegroundLayer(x-guiLeft,y-guiTop);

		if(this.hammer==null) return;
		if(this.enchantOpen){
			GL11.glPushMatrix();
				Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
				GL11.glColor4f(1f, 1f, 1f, 1f);
				drawTexturedModalRect(30, 30,0 , 0, 150,100);
				drawTexturedModalRect(174, 30,this.xSize-20 , 0, 20,100);
				drawTexturedModalRect(30, 120,0 , this.ySize-20, 150,20);
				drawTexturedModalRect(174, 120,this.xSize-20 , this.ySize-20, 20,20);
			GL11.glPopMatrix();

			NBTTagList nbttaglist = this.hammer.getTagCompound().getTagList("EnchStored", 10);
			
			for(int i = this.enchantSc.currentIndex ; i < this.enchantSc.currentIndex + this.enchantSc.maxComponent ; i++){

				if(this.enchantSc.componentList.get(i)==null)break;
				
		    	TextFormatting color = TextFormatting.DARK_GRAY;
		    	if(!nbttaglist.getCompoundTagAt(i).getBoolean("enable")){
		    		color=TextFormatting.STRIKETHROUGH;
			    	if(this.enchantSc.componentList.get(i).isMouseHover(x-this.guiLeft, y-this.guiTop)){
			    		color =  TextFormatting.DARK_GRAY;
			    	}
		    	}else{
			    	if(this.enchantSc.componentList.get(i).isMouseHover(x-this.guiLeft, y-this.guiTop)){
			    		color =  TextFormatting.GRAY;
			    	}
		    	}
		    	this.enchantSc.componentList.get(i).defColor = color;
		    	this.enchantSc.componentList.get(i).scrollingYMarge = this.enchantSc.currentIndex*10;
			    this.enchantSc.componentList.get(i).draw(x-guiLeft,y-guiTop);
		    
			}
		}
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY) {

		if(this.inHammerStand)
			Minecraft.getMinecraft().getTextureManager().bindTexture(bgroundInStand);
		else
			Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
			
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void updateToolTips(String key) {
	}

}
