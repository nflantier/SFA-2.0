package noelflantier.sfartifacts.client.gui;

import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.manual.ABaseCategory;
import noelflantier.sfartifacts.client.gui.manual.NBTTFManual;
import noelflantier.sfartifacts.client.gui.manual.NBlocksAndItems;
import noelflantier.sfartifacts.client.gui.manual.NCaptainManual;
import noelflantier.sfartifacts.client.gui.manual.NHulkManual;
import noelflantier.sfartifacts.client.gui.manual.NThorManual;

public class NIndexManual extends ABaseCategory{
	

	public static final String CAT_THOR = "THOR";
	public static final String CAT_HULK = "HULK";
	public static final String CAT_CAPTAIN = "CAPTAIN";
	public static final String CAT_BTTF = "BTTF";
	public static final String CAT_ITEMS_AND_BLOCKS = "ITEMS_AND_BLOCKS";
	public static NThorManual THOR_MANUAL;
	public static NHulkManual HULK_MANUAL;
	public static NCaptainManual CAPTAIN_MANUAL;
	public static NBTTFManual BTTF_MANUAL;
	public static NBlocksAndItems IAB_MANUAL;
	
	public NIndexManual(String name, int x, int y) {
		super(name,x,y);
		THOR_MANUAL = new NThorManual(CAT_THOR,this.x, this.y);
		HULK_MANUAL = new NHulkManual(CAT_HULK,this.x, this.y);
		CAPTAIN_MANUAL = new NCaptainManual(CAT_CAPTAIN,this.x, this.y);
		BTTF_MANUAL = new NBTTFManual(CAT_BTTF,this.x, this.y);
		IAB_MANUAL = new NBlocksAndItems(CAT_ITEMS_AND_BLOCKS,this.x, this.y);
		initComponent();
	}
	
	@Override
	public void initComponent() {
		this.componentList.put(CAT_THOR, new GuiComponent(this.x+10, this.y+30, 100, 10){{
			addText("Thor", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_THOR, THOR_MANUAL);
		
		this.componentList.put(CAT_HULK, new GuiComponent(this.x+10, this.y+40, 100, 10){{
			addText("Hulk", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_HULK, HULK_MANUAL);
		
		this.componentList.put(CAT_CAPTAIN, new GuiComponent(this.x+10, this.y+50, 100, 10){{
			addText("Captain america", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_CAPTAIN, CAPTAIN_MANUAL);
		
		this.componentList.put(CAT_BTTF, new GuiComponent(this.x+10, this.y+60, 100, 10){{
			addText("Back to the Future", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_BTTF, BTTF_MANUAL);
		
		this.componentList.put(CAT_ITEMS_AND_BLOCKS, new GuiComponent(this.x+10, this.y+70, 100, 10){{
			addText("Items and Blocks", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_ITEMS_AND_BLOCKS, IAB_MANUAL);
	}
}
