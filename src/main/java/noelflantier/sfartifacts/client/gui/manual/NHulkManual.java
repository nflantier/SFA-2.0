package noelflantier.sfartifacts.client.gui.manual;

import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRecipe;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class NHulkManual extends ABaseCategory{

	public static final String CAT_SOUNDEMITTER = "SOUNDEMITTER";
	public static final String CAT_HULK_DESC = "HULK_DESC";
	public static final String CAT_HULK_RING = "HULK_RING";
	public static final String CAT_FREQUENCY = "FREQUENCY";
	
	public NHulkManual(String name, int x, int y) {
		super(name,x,y);
		initComponent();
	}
	
	@Override
	public void initComponent() {
		this.componentList.put(CAT_SOUNDEMITTER, new GuiComponent(this.x+10, this.y+30, 100, 10){{
			addText("Sound emitter", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_SOUNDEMITTER, new DummyCategory(CAT_SOUNDEMITTER,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("This machine will emit sound on a certain frequency. Different frequency will "
							+ "spawn different creature so be carefull. All the frequency discovered are saved in "
							+ "your list. To emit sounds, it use some RF and liquefied asgardite depending on the "
							+ "frequency. The majority of the creatures spawned will be attracted to the source "
							+ "of the sound. If you dont want to search all the frequencies, there is an option "
							+ "in the config file that will add all the possible frequencies and their echos to your list.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Sound Emitter", this.x,this.y+85,new ItemStack(ModBlocks.blockSoundEmiter,1,0),GuiRecipe.TYPE.VANILLA));
				}});
			}}
		);

		this.componentList.put(CAT_HULK_DESC, new GuiComponent(this.x+10, this.y+40, 100, 10){{
			addText("Hulk", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_HULK_DESC, new DummyCategory(CAT_HULK_DESC,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("Hulk can be lure with the sound emitter, you can't kill him but if you "
							+ "hurt him really bad he will run away. Just before running away he will drop "
							+ "some flesh, try to use a weapon with loot to have multiple flesh dropped, you "
							+ "can eat them or try to make the Mighty Hulk Ring.", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);

		this.componentList.put(CAT_HULK_RING, new GuiComponent(this.x+10, this.y+50, 100, 10){{
			addText("Hulk ring", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_HULK_RING, new DummyCategory(CAT_HULK_RING,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("The  Mighty Hulk Ring give you more health regen, more speed, step assist, "
							+ "augmented jump, reduce fall damages and your bares hand becomes stronger, you "
							+ "can mine and fight pretty efficiently with them. To equip the ring you have to "
							+ "use Baubles, and put the ring in a ring slot. However the ring as side effect on "
							+ "your hunger you'll be hungry faster.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Mighty Hulk Ring", this.x,this.y+65,new ItemStack(ModItems.itemMightyHulkRing,1,0),GuiRecipe.TYPE.INJECTOR));
				}});
			}}
		);

		this.componentList.put(CAT_FREQUENCY, new GuiComponent(this.x+10, this.y+60, 100, 10){{
			addText("Frequency", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_FREQUENCY, new DummyCategory(CAT_FREQUENCY,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("If you want all the frequencies of all the mobs presents in your game, "
							+ "there is an option in the config to show them all in the sound emiter GUI.", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);
	}
}
