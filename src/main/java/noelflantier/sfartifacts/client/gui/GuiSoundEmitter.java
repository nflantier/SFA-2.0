package noelflantier.sfartifacts.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiButtonSFA;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRender;
import noelflantier.sfartifacts.client.gui.bases.GuiSFATextField;
import noelflantier.sfartifacts.client.gui.bases.GuiScrollable;
import noelflantier.sfartifacts.client.gui.bases.GuiToolTips;
import noelflantier.sfartifacts.common.container.ContainerSoundEmitter;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketSoundEmitterGui;
import noelflantier.sfartifacts.common.recipes.handler.SoundEmitterConfig;
import noelflantier.sfartifacts.common.tileentities.TileSoundEmiter;

public class GuiSoundEmitter extends GuiMachine{

	private static final ResourceLocation bground = new ResourceLocation(Ressources.MODID+":textures/gui/guiSoundEmiter.png");
	private static final ResourceLocation guiselements = new ResourceLocation(Ressources.MODID+":textures/gui/guisElements.png");
	TileSoundEmiter tile;
	private int buttonPressed = -1;
	private int tickButton = 2;
	private int currentTickButton = -1;
	private int currentTickScanning = 0;
	private int indexSD = -1;
	private boolean isScanning = false;
	
	//DATA SYNC
	private boolean isEmitting = false;
	private String lastScannedName;
	public int lastFrequency;
	public int lastVariant;
	
	public GuiScrollable listFrequency = new GuiScrollable(10);
	Map<Integer, String> allFrequency;
	
	public GuiSoundEmitter(InventoryPlayer inventory, TileSoundEmiter tile) {
		super(new ContainerSoundEmitter(inventory, tile));
		this.xSize = 176;
		this.ySize = 200;
		this.tile = tile;
		hasSidedBt[sidedButton.get(machineButtonO1)] = true;
		sidedBtHasPopUp[sidedButton.get(machineButtonO1)] = true;
		sidedBtTick[sidedButton.get(machineButtonO1)] = "List";
		sidedBtTock[sidedButton.get(machineButtonO1)] = "List";
		isEmitting = this.tile.isEmitting;
		lastVariant = this.tile.variant;
		lastScannedName = this.tile.entityNameToSpawn;
		lastFrequency = this.tile.frequencyEmited;
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void drawPopUp(int x, int y, int key){
		super.drawPopUp(x,y,key);
		if(key == machineButtonO1){
			this.drawBackgroundPopUp(guiLeft+7, guiTop+5);
			this.listFrequency.show(x,y);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(this.isAnyPopUpOpen && !byPassPopUp)
			return;
		if(byPassPopUp)
			byPassPopUp = false;
		if(button.id==0){//-
			if(this.buttonPressed==0 || this.buttonPressed==-1){
				if(this.currentTickButton<0){
					this.currentTickButton = this.tickButton;
					long i = Long.parseLong(this.componentList.get("tffreq").textFieldList.get(0).getText());
					i=i>SoundEmitterConfig.highestFrequency?SoundEmitterConfig.highestFrequency:i;
					this.componentList.get("tffreq").textFieldList.get(0).setText((i-1<SoundEmitterConfig.lowestFrequency?SoundEmitterConfig.lowestFrequency:i-1)+"");
				}else
					this.currentTickButton-=1;
			}
			this.buttonPressed = 0;
		}else if(button.id==1){//+
			if(this.buttonPressed==1 || this.buttonPressed==-1){
				if(this.currentTickButton<0){
					this.currentTickButton = this.tickButton;
					long i = Integer.parseInt(this.componentList.get("tffreq").textFieldList.get(0).getText());
					i=i<SoundEmitterConfig.lowestFrequency?SoundEmitterConfig.lowestFrequency:i;
					this.componentList.get("tffreq").textFieldList.get(0).setText((i+1>SoundEmitterConfig.highestFrequency?SoundEmitterConfig.highestFrequency:i+1)+"");
				}else
					this.currentTickButton-=1;
			}
			this.buttonPressed = 1;
		}else if(button.id==2){//SCAN
			if(this.isScanning)
				return;
			startScanning(false);
		}else if(button.id==3){//EMIT
			if(this.isScanning)
				return;
			if(!this.tile.isEmitting){
				startScanning(true);
			}else{
				//SET IS EMITING FALSE WHEN SOMEHTING IS ALLREADY EMITITNG
				PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(tile.getPos(),lastFrequency,2,lastVariant));
		
				this.componentList.get("txta").addText("stop_emitting_"+this.tile.frequencyEmited, 0, 0);				
				
				this.tile.isEmitting = false;
				isEmitting = this.tile.isEmitting;
				this.tile.frequencyEmited = 0;
				this.tile.entityNameToSpawn = null;
				this.getButtonById(3).displayString = "EMIT";
				checkVariant();
			}
		}else if(button.id==4){//CHANGE VARIANT DOWN
			if(lastVariant==-1)
				return;
			lastVariant -= 1;
			PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(tile.getPos(),lastFrequency,5,lastVariant));
			this.tile.variant = lastVariant;
			checkVariant();
		}else if(button.id==5){//CHANGE VARIANT UP
			List<String> variants = SoundEmitterConfig.getInstance().ENTITIES_VARIANTS.get(lastScannedName);
			if(variants != null && !variants.isEmpty()){
				if(lastVariant + 1 <= variants.size()-1)
					lastVariant += 1;
			}else
				lastVariant = -1;
			PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(tile.getPos(),lastFrequency,5,lastVariant));
			this.tile.variant = lastVariant;
			checkVariant();
		}
	}

	public void scanning(){
		if(this.currentTickScanning==30 || this.currentTickScanning==60)
			this.componentList.get("txta").replaceString(2, "..");
		if(this.currentTickScanning==5 || this.currentTickScanning==25 || this.currentTickScanning==35 || this.currentTickScanning==55)
			this.componentList.get("txta").replaceString(2, "....");
		if(this.currentTickScanning==10 || this.currentTickScanning==20 || this.currentTickScanning==40 || this.currentTickScanning==50)
			this.componentList.get("txta").replaceString(2, "......");
		if(this.currentTickScanning==15 || this.currentTickScanning==35 || this.currentTickScanning==45)
			this.componentList.get("txta").replaceString(2, "........");
		
		if(this.currentTickScanning==65)
			this.componentList.get("txta").replaceString(2, "_________");
		if(this.currentTickScanning==70){
			String str = "1 echo found.";
			if(this.lastScannedName==null)
				str = "0 echo found";
			this.componentList.get("txta").addText(""+str, 0, 0);
		}
		if(this.currentTickScanning==75 && this.lastScannedName!=null){
			this.componentList.get("txta").addText(SoundEmitterConfig.getInstance().getRealEntityName(this.lastScannedName), 0, 0);
			showCond("txta");
		}
		if(this.currentTickScanning==80)
			this.componentList.get("txta").addText("_________", 0, 0);
		if(this.currentTickScanning==80)
			this.componentList.get("txta").addText("end", 0, 0);
		
		if(this.currentTickScanning>=100){
			this.isScanning = false;
			this.currentTickScanning=0;
			if(this.isEmitting){
				loadListFrequency();
				this.componentList.get("txta").addText("start_emit:", 0, 0);
				if(this.lastScannedName!=null){
					this.componentList.get("txta").addText("emitting____"+this.lastFrequency, 0, 0);
					showCond("txta");
					this.getButtonById(3).displayString = "STOP";
				}else
					this.componentList.get("txta").addText("cant_emit_", 0, 0);
			}
		}
	}
	
	public void showCond(String key){
		this.componentList.get(key).addText("c:_"+this.getStrConditionsFrequency(this.lastFrequency), 0, 0);
		if(this.tile.getEnergyStored(null)<SoundEmitterConfig.getInstance().getRfForName(lastScannedName))
			this.componentList.get(key).addText("!!not_enough_rf", 0, 0);
		if(this.tile.getFluidTanks().get(0).getFluidAmount()<SoundEmitterConfig.getInstance().getFlForName(lastScannedName))
			this.componentList.get(key).addText("!!not_enough_fluid", 0, 0);
	}
		
	public String getStrConditionsFrequency(int freq){
		String s = "";
		s = SoundEmitterConfig.getInstance().getRfForFrequency(freq)+"RF_"+SoundEmitterConfig.getInstance().getFlForFrequency(freq)+"MB";
		return s;
	}
	
	public void startScanning(boolean emit){
		if(!this.isScanning){
			checkFrequency();
			this.isEmitting = false;
			this.componentList.get("txta").reset();
			this.lastFrequency = Integer.parseInt(this.componentList.get("tffreq").textFieldList.get(0).getText());
			
			//SET THE SELECETED FREQ
			PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(tile.getPos(),lastFrequency,1,lastVariant));
			this.tile.frequencySelected = this.lastFrequency;
			
			this.lastScannedName = SoundEmitterConfig.getInstance().getNameForFrequency(this.lastFrequency);
			this.isScanning = true;
			
			this.componentList.get("txta").addText("start_scan:", 0, 0);
			this.componentList.get("txta").addText("f_"+this.lastFrequency, 0, 0);
			this.componentList.get("txta").addText("..", 0, 0);
			
			if(this.lastScannedName!=null){
				if(!ModConfig.areFrequenciesShown){
					//ADD FREQUENCY AND STRING TO LIST
					PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(tile.getPos(),lastFrequency,4,lastVariant));
					if(this.tile.listScannedFrequency.containsKey(this.lastFrequency))
						this.tile.listScannedFrequency.remove(this.lastFrequency);
					this.tile.listScannedFrequency.put(this.lastFrequency, this.lastScannedName);
				}
			}
			
			if(emit){
				if(this.lastScannedName!=null && SoundEmitterConfig.getInstance().getMobsProperties(lastScannedName, this.tile.getEnergyStored(null), this.tile.getFluidTanks().get(0).getFluidAmount())!=null){

					this.isEmitting = true;
					//SET THE EMITE FREQ AND SET ISEMITING TO TRUE
					PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(tile.getPos(),lastFrequency,0,lastVariant));
					this.tile.frequencyEmited = this.lastFrequency;
					PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(tile.getPos(),lastFrequency,3,lastVariant));
					this.tile.isEmitting = true;
					
				}
			}
			checkVariant();
		}
	}
	
	public void checkFrequency(){
		long f = Long.parseLong(this.componentList.get("tffreq").textFieldList.get(0).getText());
		f=f>SoundEmitterConfig.highestFrequency?SoundEmitterConfig.highestFrequency:f;
		f=f<SoundEmitterConfig.lowestFrequency?SoundEmitterConfig.lowestFrequency:f;
		this.componentList.get("tffreq").textFieldList.get(0).setText(f+"");
	}
	
	@Override
	public void updateScreen(){
		super.updateScreen();
		if(this.isScanning){
			this.currentTickScanning+=1;
			scanning();
		}else{
			if(this.isEmitting){
				if(this.componentList.get("txta").stringList.containsKey(indexSD))
					this.componentList.get("txta").replaceString(indexSD, "spawn_delay : "+this.tile.spawnerBaseLogic.spawnDelay);
				else
					indexSD = this.componentList.get("txta").addText("spawn_delay : "+this.tile.spawnerBaseLogic.spawnDelay, 0, 0);
			}
		}
		
		if(sidedBtOpen[sidedButton.get(machineButtonO1)]){
			this.listFrequency.input();
		}
		
		for(int i =0; i<this.buttonList.size();i++){
			if(this.buttonList.get(i) instanceof GuiButtonSFA){
				if(((GuiButtonSFA)this.buttonList.get(i)).pressed && ((GuiButton)this.buttonList.get(i)).id==this.buttonPressed){
					try {
						this.actionPerformed(((GuiButtonSFA)this.buttonList.get(i)));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
    	super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		if(clickedMouseButton==0 || clickedMouseButton==1)this.buttonPressed=-1;
    }
    
	@Override
	public void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button);
		
		if(sidedBtOpen[sidedButton.get(machineButtonO1)]){
			Enumeration<Integer> enumKeyE = this.listFrequency.componentList.keys();    		
			while (enumKeyE.hasMoreElements()) {
			    int key = enumKeyE.nextElement();
		    	if(this.listFrequency.componentList.get(key).isMouseHover(x, y)){
	    		 	int i = 0;        
	    			Iterator <Map.Entry<Integer, String>>iterator;
	    			if(ModConfig.areFrequenciesShown)
	    				iterator =  allFrequency.entrySet().iterator();
	    			else
	    				iterator = this.tile.listScannedFrequency.entrySet().iterator();
	    	        while (iterator.hasNext()){
	    	        	Map.Entry<Integer, String> entry = iterator.next();
	    	        	if(i==key){
	    	        		if(sidedBtOpen[sidedButton.get(machineButtonO1)])
	    	        			sidedBtOpen[sidedButton.get(machineButtonO1)] = false;
	    	        		if(!this.isScanning){
		    	        		this.componentList.get("tffreq").textFieldList.get(0).setText(""+entry.getKey());
		    	        		byPassPopUp = true;
		    	        		actionPerformed(this.getButtonById(3));
	    	        		}
	    	        		break;
	    	        	}
	    	        	i+=1;
	    	        }
		    	}
			}
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
		this.componentList.put("se", new GuiComponent(6, 5, 100, 10){{
			addText("Sound Emiter :", 0, 0);
		}});
		this.componentList.put("in", new GuiComponent(6, 165, 100, 10){{
			addText("Inventory :", 0, 0);
		}});

		this.componentList.put("chfreq", new GuiComponent(118, 31){{
			addSfaButton(0,guiLeft,guiTop,20,20,"-");
			addSfaButton(1,guiLeft+22,guiTop,20,20,"+");
		}});

		this.componentList.put("scan", new GuiComponent(118, 52){{
			addButton(2,guiLeft,guiTop,42,20,"SCAN");
		}});

		this.componentList.put("emit", new GuiComponent(118, 73){{
			addButton(3,guiLeft,guiTop,42,20,tile.isEmitting?"STOP":"EMIT");
		}});

		this.componentList.put("txta", new GuiComponent(45, 55, 100, 10){{
			defColor = TextFormatting.DARK_GRAY;
			globalScale = 0.6F;
		}});
		if(this.tile.isEmitting){
			this.componentList.get("txta").addText("emitting____"+this.tile.frequencyEmited, 0, 0);
			this.lastFrequency = this.tile.frequencyEmited;
			showCond("txta");
		}
		
		this.componentList.put("tffreq", new GuiComponent(44, 22, 50, 10){{
			defColor = TextFormatting.DARK_GRAY;
			addText("Frequency :", 0, 0);
			addSFATextField(0,10,68,16);
		}});
		this.componentList.get("tffreq").textFieldList.get(0).setMaxStringLength(20);
		((GuiSFATextField)this.componentList.get("tffreq").textFieldList.get(0)).isOnlyNumeric = true;
		this.componentList.get("tffreq").textFieldList.get(0).setText(""+this.tile.frequencySelected);
		
		this.componentManual.put("so", new GuiComponent(guiLeft+12, guiTop+12, 100, 10){{
			globalScale = 0.6F;
			addText("This machine will emit sound on a certain", 0, 0);
			addText("frequency. Different frequency will spawn", 0, 0);
			addText("different creature so be carefull.", 0, 0);
			addText("To emit sounds, it use some RF and liquefied", 0, 0);
			addText("asgardite depending on the frequency.", 0, 0);
			addText("The majority of the creatures spawned will be", 0, 0);
			addText("attracted to the source of the sound.", 0, 0);
			addText("If you dont want to search all the frequencies,", 0, 0);
			addText("there is an option in the config file that", 0, 0);
			addText("will add all the possible frequencies and their", 0, 0);
			addText("echos to your list.", 0, 0);
		}});

		this.componentList.put("tvariant", new GuiComponent(118, 104){{
			addText("Variant:", 0, 0);
		}});
		this.componentList.put("variant", new GuiComponent(118, 104){{
			globalScale = 0.6F;
			addSfaButton(4,guiLeft,guiTop+10,20,20,"<");
			addSfaButton(5,guiLeft+22,guiTop+10,20,20,">");
			addText("Normal", 0, 56);
		}});
		
		checkVariant();
		loadListFrequency();
	}
	
	public void checkVariant(){
		if(!isEmitting || lastScannedName==null){
			componentList.get("variant").buttonList.get(0).enabled = false;
			componentList.get("variant").buttonList.get(1).enabled = false;
			componentList.get("variant").replaceString(0, "Normal");
			return;
		}
		componentList.get("variant").buttonList.get(0).enabled = true;
		componentList.get("variant").buttonList.get(1).enabled = true;
		List<String> variants = SoundEmitterConfig.getInstance().ENTITIES_VARIANTS.get(lastScannedName);
		if(variants!=null && !variants.isEmpty()){
			if(lastVariant!=-1 && variants.get(lastVariant) != null){
				componentList.get("variant").replaceString(0, variants.get(lastVariant));
			}else{
				componentList.get("variant").replaceString(0, "Normal");
				lastVariant = -1;
				componentList.get("variant").buttonList.get(0).enabled = false;
				PacketHandler.INSTANCE.sendToServer(new PacketSoundEmitterGui(tile.getPos(),lastFrequency,5,lastVariant));
				tile.variant = lastVariant;
			}
			if(lastVariant == variants.size()-1)
				componentList.get("variant").buttonList.get(1).enabled = false;
		}else{
			componentList.get("variant").buttonList.get(0).enabled = false;
			componentList.get("variant").buttonList.get(1).enabled = false;
			componentList.get("variant").replaceString(0, "Normal");
			return;
		}
		
	}
	
	public void loadListFrequency(){
		this.listFrequency = new GuiScrollable(10);
        int y = 12;
        int i = 0;

		Iterator <Map.Entry<Integer, String>>iterator;
		
		if(ModConfig.areFrequenciesShown){
			allFrequency = new LinkedHashMap<Integer, String>();
			allFrequency.putAll(SoundEmitterConfig.getInstance().getSortedFrequencyToName());
			iterator = allFrequency.entrySet().iterator();
		}else 
			iterator = this.tile.listScannedFrequency.entrySet().iterator();
		
        while (iterator.hasNext()){
        	Map.Entry<Integer, String> entry = iterator.next();
	    	GuiComponent gce = new GuiComponent(guiLeft+15, guiTop+y, 130,10);
			gce.addText(entry.getKey()+" : "+I18n.format("entity."+entry.getValue()+".name"), 0,0);
	    	gce.isLink=true;
			this.listFrequency.addComponent(i, gce);
			y += 10;
			i +=1;
        }
        this.listFrequency.setPositionSizeAndAlpha(guiLeft+11, guiTop+10, 152, 103, 0.4F);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
		GuiRender.renderEnergy(tile.storage.getMaxEnergyStored(), tile.getEnergyStored(null), guiLeft+25, guiTop+23,this.zLevel, 14, 47, 176, 0);
		GuiRender.renderFluid(tile.tank, guiLeft+8, guiTop+23, this.zLevel, 14, 47);
		super.drawGuiContainerBackgroundLayer(partialTickTime, x, y);
	}
	
	@Override
	public void updateToolTips(String key) {
		
		switch(key){
			case "tank" :
				((GuiToolTips)this.componentList.get("tank")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, String.format("%,d", this.tile.tank.getFluidAmount())+" MB");
				((GuiToolTips)this.componentList.get("tank")).addContent(this.fontRendererObj, "/ "+String.format("%,d", this.tile.tank.getCapacity())+" MB");
				break;
			case "energy" :
				((GuiToolTips)this.componentList.get("energy")).content =  new ArrayList<String>();
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, String.format("%,d", this.tile.getEnergyStored(null))+" RF");
				((GuiToolTips)this.componentList.get("energy")).addContent(this.fontRendererObj, "/ "+String.format("%,d", this.tile.getMaxEnergyStored(null))+" RF");
				break;
			default:
				break;
		}	
	}
	
}
