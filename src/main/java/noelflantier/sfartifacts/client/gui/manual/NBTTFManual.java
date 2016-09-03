package noelflantier.sfartifacts.client.gui.manual;

import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiItemStack;
import noelflantier.sfartifacts.client.gui.bases.GuiRecipe;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class NBTTFManual  extends ABaseCategory{

	public static final String CAT_MRFUSION = "MR_FUSION";
	public static final String CAT_INDUCTOR = "INDUCTOR";
	public static final String CAT_SILICON = "SILICON";
	public static final String CAT_HOVERBOARDS = "HOVERBOARDS";
	public static final String CAT_RECHARGER = "RECHARGER";
	
	public NBTTFManual(String name, int x, int y) {
		super(name,x,y);
		initComponent();
	}
	
	@Override
	public void initComponent() {

		this.componentList.put(CAT_MRFUSION, new GuiComponent(this.x+10, this.y+30, 100, 10){{
			addText("Mr Fusion", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_MRFUSION, new DummyCategory(CAT_MRFUSION,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("Mr Fusion is a powerfull generator it can provide 1.21 GW or GRF/T using "
							+ "fusion of everything you can find. However it will be more efficient with organic "
							+ "materials like foods and with any kind of liquids.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Mr Fusion", this.x,this.y+50,new ItemStack(ModBlocks.blockMrFusion),GuiRecipe.TYPE.VANILLA));
				}});
			}}
		);

		this.componentList.put(CAT_INDUCTOR, new GuiComponent(this.x+10, this.y+40, 100, 10){{
			addText("Inductors", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_INDUCTOR, new DummyCategory(CAT_INDUCTOR,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					nbGuiRecipeVertical = 2;
					nbGuiRecipeHorizontal = 5;
					addTextAutoWitdh("Inductor can transmit power wirelessly. To associate inductors you "
							+ "need to right click them with the basic hammer set in basic mode. The first "
							+ "inductor right clicked will provide energy to the other one. You can setup "
							+ "the inductor transmission behaviour in it gui. The inductor will share all "
							+ "its power equally, to all the other connected inductors.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Basic Inductor", this.x,this.y+65,new ItemStack(ModBlocks.blockInductor,1,0),GuiRecipe.TYPE.VANILLA));
					addRecipe(new GuiRecipe("Advanced Inductor", this.x+80,this.y+65,new ItemStack(ModBlocks.blockInductor,1,1),GuiRecipe.TYPE.VANILLA));
					addRecipe(new GuiRecipe("Energized Basic Inductor", this.x+160,this.y+65,new ItemStack(ModBlocks.blockInductor,1,2),GuiRecipe.TYPE.INJECTOR));
					addRecipe(new GuiRecipe("Energized Advanced Inductor", this.x+230,this.y+65,new ItemStack(ModBlocks.blockInductor,1,3),GuiRecipe.TYPE.INJECTOR));
				}});
			}}
		);

		this.componentList.put(CAT_SILICON, new GuiComponent(this.x+10, this.y+50, 100, 10){{
			addText("Silicon", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_SILICON, new DummyCategory(CAT_SILICON,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("To makes chips you will need some pure silicon. Pure silicon "
							+ "is made by smelting clear silicon in a furnace. Pure silicon will "
							+ "naturaly decay back to clear silicon after a while, so be quick at "
							+ "crafting chips. Clear silicon is made by smelting raw silicon in a furnace.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Raw Silicon", this.x,this.y+50,new ItemStack(ModItems.itemSilicon,1,0),GuiRecipe.TYPE.VANILLA));
					addItemStack(new GuiItemStack(x+80,y+50,new ItemStack(ModItems.itemSilicon,1,1)));
					addItemStack(new GuiItemStack(x+100,y+50,new ItemStack(ModItems.itemSilicon,1,2)));
				}});
			}}
		);

		this.componentList.put(CAT_HOVERBOARDS, new GuiComponent(this.x+10, this.y+60, 100, 10){{
			addText("Hoverboards", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_HOVERBOARDS, new DummyCategory(CAT_HOVERBOARDS,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("HoverBoards make you move easily on terrain, you can go faster and "
							+ "climb tall step, it prevent fall damages and depending on the model can make "
							+ "you hover fluids ! You can upgrade their energy capacity in the anvil with "
							+ "energy module. To start moving with the hoverboard right click it then you'll "
							+ "be able to do everything you do normaly except you are on an hoverboard. To stop "
							+ "moving with the hoverboard press shift or right click it.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Mattel Hoverboard", this.x,this.y+80,new ItemStack(ModItems.itemHoverboard,1,0),GuiRecipe.TYPE.VANILLA));
					addRecipe(new GuiRecipe("Pitbull Hoverboard", this.x+80,this.y+80,new ItemStack(ModItems.itemHoverboard,1,2),GuiRecipe.TYPE.VANILLA));
				}});
			}}
		);

		this.componentList.put(CAT_RECHARGER, new GuiComponent(this.x+10, this.y+70, 100, 10){{
			addText("Recharger", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_RECHARGER, new DummyCategory(CAT_RECHARGER,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("The Recharger can recharge your energy RF or EU items. If you are close enough : "
					+ModConfig.rangeOfRecharger+" blocks around, it can recharge them wirelessly.", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);
	}
}