package noelflantier.sfartifacts.client.gui.manual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.NIndexManual;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiSFAScreen;

public class NGuiManual extends GuiSFAScreen{

	public ResourceLocation bgroundL = new ResourceLocation(Ressources.MODID+":textures/gui/guiManualsLeft.png");
	public ResourceLocation bgroundR = new ResourceLocation(Ressources.MODID+":textures/gui/guiManualsRight.png");
	private static final ResourceLocation guiselements = new ResourceLocation(Ressources.MODID+":textures/gui/guisElements.png");
	public Hashtable<String,ABaseCategory> listLink = new Hashtable<String,ABaseCategory>();
	public static final String CAT_INDEX = "INDEX";
	public static final String CAT_PREVIOUS = "PREVIOUS";
	public List<String> history = new ArrayList<String>();
	public int maxHistorySize = 10;
	public static int maxStringWidth = 340;
	public static ABaseCategory index;
	public String currentKey = "";
	public ABaseCategory currentCategory;
	
	public NGuiManual(){
		super();
		this.xSize = 360;
		this.ySize = 190;
	}
	public NGuiManual(EntityPlayer player){
		this();
	}
	public void loadComponents(){
		if(this.componentloaded)return;
		super.loadComponents();
		index = new NIndexManual(CAT_INDEX,this.guiLeft, this.guiTop);
		listLink.put(CAT_INDEX, index);
		initLink(index);
		currentKey = CAT_INDEX;
		setCategory(false);
	}
	
	public void initLink(ABaseCategory c){
		if(c.listCategory.isEmpty())
			return;
		c.addLink(listLink);
		for (Map.Entry<String,ABaseCategory> entry : c.listCategory.entrySet()){
			initLink(entry.getValue());
		}
	}
	
	public void addMenu(){
		this.fullComponentList.put(CAT_INDEX, new GuiComponent(guiLeft+180-15, guiTop+10, 30, 10){{
			addText("HOME", 0, 0);
			isLink = true;
		}});
		if(this.history.size()>1){
			this.fullComponentList.put(CAT_PREVIOUS, new GuiComponent(guiLeft+10, guiTop+10, 50, 10){{
				addText("previous", 0, 0);
				isLink = true;
			}});
			if(this.listLink.containsKey(CAT_PREVIOUS))
				this.listLink.remove(CAT_PREVIOUS);
			if(history.get(history.size()-2)!=null && listLink.get(history.get(history.size()-2))!=null){
				this.listLink.put(CAT_PREVIOUS, listLink.get(history.get(history.size()-2)));
			}
		}
	}
	
	public void setCategory(boolean init){
		if(!listLink.containsKey(currentKey))
			return;
		if(CAT_PREVIOUS.equals(currentKey)){
			int r = this.history.size()-1;
			this.history.remove(r);
		}else
			addHistory(currentKey);
		currentCategory = listLink.get(currentKey);
		this.fullComponentList.clear();
		this.fullComponentList.putAll(currentCategory.getComponents());
		addMenu();
		if(init)
			this.initGui();
	}
	
	public void addHistory(String key){
		if(this.history.size()>=this.maxHistorySize)
			this.history.remove(0);
		this.history.add(this.history.size(),key);
	} 
	
	@Override
    protected void actionPerformed(GuiButton button) {
		this.fullComponentList.forEach((s,g)->g.handleButton(button));
    }
    
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button);
		Enumeration<String> enumKey = this.fullComponentList.keys();
		while (enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    ItemStack st = this.fullComponentList.get(key).getItemStackHovered(x, y);
		    if(st!=null && listLink.containsKey(NBlocksAndItems.PRE_IB+st.getUnlocalizedName())){
		    	currentKey = NBlocksAndItems.PRE_IB+st.getUnlocalizedName();
		    	setCategory(true);
	    		break;
		    }
		    if(this.fullComponentList.get(key).clicked(x, y)){
		    	currentKey = key;
		    	setCategory(true);
	    		break;
		    }
		}
	}
	
	@Override
	public boolean doesGuiPauseGame(){
        return false;
    }
	
	@Override
    public void drawScreen(int x, int y, float f){
		Minecraft.getMinecraft().getTextureManager().bindTexture(bgroundL);
		GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 180, this.ySize);
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(bgroundR);
		GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			this.drawTexturedModalRect(guiLeft+180, guiTop, 0, 0, 180, this.ySize);
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		 
    	super.drawScreen(x, y, f);
    	
    	ItemStack io = null;
		Enumeration<String> enumKey = this.fullComponentList.keys();
		while (enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    this.fullComponentList.get(key).draw(x, y);
		    if(io==null)io = this.fullComponentList.get(key).getItemStackHovered(x, y);
		}
    	drawImageButtons(x,y);
    	renderToolTip(io, x, y);
    }
}
