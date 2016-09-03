package noelflantier.sfartifacts.client.gui;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiSFA;
import noelflantier.sfartifacts.common.container.ContainerMachine;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketMachine;
import noelflantier.sfartifacts.common.tileentities.TileMachine;

public abstract class GuiMachine extends GuiSFA{

	private static final ResourceLocation bgroundMachine = new ResourceLocation(Ressources.MODID+":textures/gui/guiBaseMachineManual.png");
	public int xSizeBM = 160;
	public int ySizeBm = 115;
	public Hashtable<String, GuiComponent> componentManual = new Hashtable<String, GuiComponent>();
	private static final ResourceLocation guisLeftBar = new ResourceLocation(Ressources.MODID+":textures/gui/guiMachineLeftBar.png");
	public TileMachine tmachine;
	
	public boolean isAnyPopUpOpen = false;
	public boolean byPassPopUp = false;
	public int machineButtonON = 150;
	public int machineButtonMA = 151;
	public int machineButtonO1 = 152;
	public int machineButtonO2 = 153;
	public HashMap<Integer, Integer> sidedButton= new HashMap<Integer,Integer>(){{
		put(machineButtonON,0);
		put(machineButtonMA,1);
		put(machineButtonO1,2);
		put(machineButtonO2,3);
	}};
	public boolean[] hasSidedBt = new boolean[]{true,true,false,false};
	public boolean[] sidedBtHasPopUp = new boolean[]{false,true,false,false};
	public boolean[] sidedBtOpen = new boolean[]{false,false,false,false};
	public String[] sidedBtTick = new String[]{"OFF","?","",""};
	public String[] sidedBtTock = new String[]{"ON","?","",""};
	
	//button enable
	
	public GuiMachine(ContainerMachine container) {
		super(container);
		this.tmachine = container.tmachine;
	}
	

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		if(sidedButton.containsKey(button.id) && sidedBtHasPopUp[sidedButton.get(button.id)])
			sidedBtOpen[sidedButton.get(button.id)] = sidedBtOpen[sidedButton.get(button.id)]?false:true;
		
		if(button.id==machineButtonON){
			this.tmachine.isManualyEnable = this.tmachine.isManualyEnable?false:true;
			PacketHandler.INSTANCE.sendToServer(new PacketMachine(this.tmachine));
			this.getButtonById(machineButtonON).displayString = this.tmachine.isManualyEnable?"OFF":"ON";
		}
	}
	
	@Override
	public void loadComponents(){
		super.loadComponents();
		this.componentManual.put("ma", new GuiComponent(guiLeft+12, guiTop+85, 100, 10){{
			globalScale = 0.6F;
			addText("Machine processing task can be turned", 0, 0);
			addText("on and off by clicking the appropriate button.", 0, 0);
			addText("If you want to stop their extract and recieve", 0, 0);
			addText("task of fluid and rf, you have to apply a", 0, 0);
			addText("redstone signal.", 0, 0);
		}});
		Iterator <Map.Entry<Integer, Integer>>iterator = sidedButton.entrySet().iterator();
        while (iterator.hasNext()){
        	Entry<Integer, Integer> entry = iterator.next();
        	if(hasSidedBt[entry.getValue()]){
    			this.componentList.put("bt"+entry.getKey(), new GuiComponent(-22, 10+entry.getValue()*21){{
    				addButton(entry.getKey(),guiLeft,guiTop,25,20,sidedBtTick[entry.getValue()]);
    			}});
        	}
        }
	}
	
	public void drawOver(int x, int y){
		Iterator <Map.Entry<Integer, Integer>>iterator = sidedButton.entrySet().iterator();
        while (iterator.hasNext()){
        	Entry<Integer, Integer> entry = iterator.next();
        	if(hasSidedBt[entry.getValue()] && sidedBtOpen[entry.getValue()]){
        		GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_LIGHTING);
        		GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
        		this.drawPopUp(x,y,entry.getKey());
        		GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
        		GL11.glPopMatrix();
        	}
        }
	}
	
	public void drawPopUp(int x, int y, int key){
		if(!sidedBtHasPopUp[sidedButton.get(key)])
			return;
		if(key == machineButtonMA){
			this.drawBackgroundPopUp(guiLeft+7, guiTop+5);
			Enumeration<String> enumKey = this.componentManual.keys();
			while (enumKey.hasMoreElements()) {
			    String tkey = enumKey.nextElement();
			    this.componentManual.get(tkey).draw(x,y);
			}
		}
		
	}
	
	public void drawBackgroundPopUp(int x, int y){
		GL11.glPushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(bgroundMachine);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		this.drawTexturedModalRect(x, y, 0, 0, this.xSizeBM, this.ySizeBm);
		GL11.glPopMatrix();		
	}
	
	@Override
	public void updateScreen(){
		super.updateScreen();
		isAnyPopUpOpen = (sidedBtHasPopUp[sidedButton.get(machineButtonON)] && hasSidedBt[sidedButton.get(machineButtonON)] && sidedBtOpen[sidedButton.get(machineButtonON)]) ||
				(sidedBtHasPopUp[sidedButton.get(machineButtonMA)] && hasSidedBt[sidedButton.get(machineButtonMA)] && sidedBtOpen[sidedButton.get(machineButtonMA)]) ||
				(sidedBtHasPopUp[sidedButton.get(machineButtonO1)] && hasSidedBt[sidedButton.get(machineButtonO1)] && sidedBtOpen[sidedButton.get(machineButtonO1)]) ||
				(sidedBtHasPopUp[sidedButton.get(machineButtonO2)] && hasSidedBt[sidedButton.get(machineButtonO2)] && sidedBtOpen[sidedButton.get(machineButtonO2)]);
	}
	
	@Override
    public void drawScreen(int x, int y, float f){
    	if(isAnyPopUpOpen)
    		this.curentToolTipComponent = "";
    	super.drawScreen(x, y, f);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		if(hasSidedBt[sidedButton.get(machineButtonON)])
			this.getButtonById(machineButtonON).displayString = this.tmachine.isManualyEnable?sidedBtTick[sidedButton.get(machineButtonON)]:sidedBtTock[sidedButton.get(machineButtonON)];
	}

	@Override
    public void drawGuiContainerForegroundLayer(int x, int y){
		super.drawGuiContainerForegroundLayer(x, y);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(guisLeftBar);
		this.drawTexturedModalRect(guiLeft-26, guiTop+5, 0, 0, this.xSize, this.ySize);
	}

}
