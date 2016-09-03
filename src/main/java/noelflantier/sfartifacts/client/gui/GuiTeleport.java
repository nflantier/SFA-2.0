package noelflantier.sfartifacts.client.gui;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.client.gui.bases.GuiScrollable;
import noelflantier.sfartifacts.common.container.ContainerTeleport;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketTeleport;

public class GuiTeleport extends GuiSFA{
	
	public ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiTeleport.png");
	public EntityPlayer player;
	public GuiScrollable coord = new GuiScrollable(10);
	public ItemStack hammer;
	public int selectedCoord = -1;
	public int tmpsc = -1;
	public int tickInput = 0;
	public int tmpti = -1;
	public int tickUpdateList = 0;
	
	public GuiTeleport(InventoryPlayer inventory){
		super(new ContainerTeleport(inventory));
		this.xSize = 198;
		this.ySize = 179;
	}

	public GuiTeleport(EntityPlayer player){
		this(player.inventory);
		this.player = player;
		this.hammer = this.player.inventory.getCurrentItem();
	}

	@Override
	public void loadComponents(){
		super.loadComponents();
		
		this.componentList.put("tfname", new GuiComponent(10, 15, 50, 10){{
			defColor = TextFormatting.DARK_GRAY;
			addText("Name :", 0, 0);
			addTextField(35,-5,95,16);
		}});
		
		this.componentList.put("tfcoord", new GuiComponent(10, 35, 50, 10){{
			defColor = TextFormatting.DARK_GRAY;
			addText("D,X,Y,Z :", 0, 0);
			addTextField(41,-5,95,16);
		}});
		this.componentList.get("tfcoord").textFieldList.get(0).setMaxStringLength(20);
		this.componentList.get("tfcoord").textFieldList.get(0).setText(this.player.dimension+","+((int)this.player.posX)+","+((int)this.player.posY)+","+((int)this.player.posZ));
		this.componentList.get("tfcoord").textFieldReadOnly.add(this.componentList.get("tfcoord").textFieldList.get(0));
		
		this.componentList.put("li", new GuiComponent(10, 55, 50, 10){{
			defColor = TextFormatting.DARK_GRAY;
			addText("Stored :", 0, 0);
		}});
		this.componentList.put("btadd", new GuiComponent(this.xSize/2-83, 15, 10, 10){{
			addSfaButton(0,guiLeft+134,guiTop-7,20,20,"+");
		}});
		this.componentList.put("btsupp", new GuiComponent(this.xSize/2-62, 15, 10, 10){{
			addSfaButton(1,guiLeft+134,guiTop-7,20,20,"-");
		}});
		this.componentList.put("bttp", new GuiComponent(this.xSize/2-83, 36, 10, 10){{
			addSfaButton(2,guiLeft+134,guiTop-7,42,20,"GO");
		}});

		if (!hammer.getTagCompound().hasKey("TeleportCoord", 9))
			hammer.getTagCompound().setTag("TeleportCoord", new NBTTagList());

		setList();
	}
	
	public void setList(){
		this.coord.componentList.clear();
		NBTTagList nbttaglist = this.hammer.getTagCompound().getTagList("TeleportCoord", 10);
        int y = 10;
        for(int i = 0 ; i<nbttaglist.tagCount() ; i++){
			String name = nbttaglist.getCompoundTagAt(i).getString("name");
			String c = nbttaglist.getCompoundTagAt(i).getString("coord");
			String[] st = c.split(",");
			int[] ct = new int[st.length];
			for(int k=0;k<st.length;k++)
				ct[k] = Integer.parseInt(st[k]);
	    	GuiComponent gce = new GuiComponent(10, y+58, 150,10);
			gce.defColor = TextFormatting.DARK_GRAY;
	    	gce.isLink=true;
	    	String dname = Minecraft.getMinecraft().theWorld.provider.getDimensionType().getById(ct[0]).getName();
			gce.addText(name+" "+dname+" "+ct[1]+" "+ct[2]+" "+ct[3], 0,0);
			this.coord.addComponent(i, gce);
			y += 10;
        }
        this.coord.setPositionSizeAndAlpha(guiLeft+8, guiTop+65, 182, 102, 0.4F);
	}

	public void drawOver(int x, int y){
		this.coord.showTheArrows(x, y);
		this.coord.showTheBorders(x,y);
	}
	
	@Override
    public void drawGuiContainerForegroundLayer(int x, int y){
		super.drawGuiContainerForegroundLayer(x-guiLeft,y-guiTop);
		for(int i = this.coord.currentIndex ; i < this.coord.currentIndex + this.coord.maxComponent ; i++){
			if(this.coord.componentList.get(i)==null)break;
	    	this.coord.componentList.get(i).scrollingYMarge = this.coord.currentIndex*10;
		    this.coord.componentList.get(i).draw(x-guiLeft,y-guiTop);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button);
		Enumeration<Integer> enumKeyE = this.coord.componentList.keys();		
		while (enumKeyE.hasMoreElements()) {
		    int key = enumKeyE.nextElement();
	    	if(this.coord.componentList.get(key).isMouseHover(x-guiLeft, y-guiTop)){
	    			if(this.selectedCoord == key)
	    				this.selectedCoord = -1;
	    			else
	    				this.selectedCoord = key;
	    		}
	    	}
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.coord.input();
		
		if(this.selectedCoord!=-1 && this.coord.componentList.get(this.selectedCoord)!=null){
			this.coord.componentList.get(this.selectedCoord).defColor = TextFormatting.GREEN;
			this.getButtonById(1).enabled = true;
			this.getButtonById(2).enabled = true;
		}else{
			this.getButtonById(1).enabled = false;
			this.getButtonById(2).enabled = false;
		}
		if(this.tmpsc!=-1  && this.coord.componentList.get(this.tmpsc)!=null && ( this.selectedCoord==-1 || this.tmpsc!=this.selectedCoord))
			this.coord.componentList.get(this.tmpsc).defColor = TextFormatting.DARK_GRAY;
		this.tmpsc = this.selectedCoord;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 0 && this.componentList.get("tfcoord").textFieldList.get(0).getText()!="" && this.componentList.get("tfname").textFieldList.get(0).getText()!=""){//ADDING COORD
			String[] st = this.componentList.get("tfcoord").textFieldList.get(0).getText().split(",");
			if(st.length==4){
				if (st[0].matches("[+-]?[0-9]+") && st[1].matches("[+-]?[0-9]+") && st[2].matches("[+-]?[0-9]+") && st[3].matches("[+-]?[0-9]+")){
					if(!DimensionManager.isDimensionRegistered(Integer.parseInt(st[0])))
						return;
					if (!hammer.getTagCompound().hasKey("TeleportCoord", 9))
						hammer.getTagCompound().setTag("TeleportCoord", new NBTTagList());
					NBTTagList nbttaglist = hammer.getTagCompound().getTagList("TeleportCoord", 10);
			        NBTTagCompound nbttagcompound = new NBTTagCompound();
			        nbttagcompound.setString("name", this.componentList.get("tfname").textFieldList.get(0).getText());
			        nbttagcompound.setString("coord", this.componentList.get("tfcoord").textFieldList.get(0).getText());
			        nbttaglist.appendTag(nbttagcompound);
			        setList();
					PacketHandler.INSTANCE.sendToServer(new PacketTeleport(0, this.componentList.get("tfname").textFieldList.get(0).getText(),this.componentList.get("tfcoord").textFieldList.get(0).getText()));
					
				}
			}
		}else if (button.id == 1 && this.selectedCoord!=-1){//DELETING COORD
			
			NBTTagList nbttaglist1 = hammer.getTagCompound().getTagList("TeleportCoord", 10);
			if(nbttaglist1.getCompoundTagAt(this.selectedCoord)!=null)
				nbttaglist1.removeTag(this.selectedCoord);
			setList();
			PacketHandler.INSTANCE.sendToServer(new PacketTeleport(1, this.selectedCoord));
			this.tmpsc = -1;
			this.selectedCoord = -1;
			this.coord.currentIndex = 0;
		}else if (button.id == 2 && this.selectedCoord!=-1){//TELEPORTING
            //this.player.travelToDimension(0);
			NBTTagList nbttaglist = hammer.getTagCompound().getTagList("TeleportCoord", 10);
			if(nbttaglist.getCompoundTagAt(this.selectedCoord)!=null){
				PacketHandler.INSTANCE.sendToServer(new PacketTeleport(2, nbttaglist.getCompoundTagAt(this.selectedCoord).getString("coord")));
			}
		}
	}
	
	@Override
	public void initGui() {
		this.componentList = new Hashtable<String, GuiComponent>();
		super.initGui();

		this.getButtonById(1).enabled = false;
		this.getButtonById(2).enabled = false;
		
	}
	
	@Override
	public boolean doesGuiPauseGame(){
        return false;
    }

	@Override
	public void updateToolTips(String key) {		
	}
}
