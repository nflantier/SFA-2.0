package noelflantier.sfartifacts.client.gui.manual;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiImage;
import noelflantier.sfartifacts.client.gui.bases.GuiItemStack;
import noelflantier.sfartifacts.client.gui.bases.GuiRecipe;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeHammerUpgrades;
import noelflantier.sfartifacts.common.recipes.RecipeHammerUpgrades.NbtTagToAdd;
import noelflantier.sfartifacts.common.recipes.RecipeOnHammerStand;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.HammerUpgradesRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.InjectorRecipesHandler;

public class NThorManual extends ABaseCategory{

	public static final String CAT_MATERIALS = "MATERIALS";
	public static final String CAT_PILLARS = "PILLARS";
	public static final String CAT_BASIC_HAMMER = "BASIC_HAMMER";
	public static final String CAT_LIQUEFIER = "LIQUEFIER";
	public static final String CAT_INJECTOR = "INJECTOR";
	public static final String CAT_HAMMERSTAND = "HAMMER_STAND";
	public static final String CAT_THOR_HAMMER = "THOR_HAMMER";
	public static final String CAT_UBER_MIGHTY_FEATHER = "UBER_MIGHTY_FEATHER";
	public static final String CAT_HOWTO = "HOW_TO";
	public static final String CAT_TIPS = "TIPS";

	public static final String CAT_PILLARS_STRUCTURES = "PILLARS_STRUCTURES";
	public static final String CAT_PILLARS_PASSIVE_ENERGY = "PILLARS_PASSIVE_ENERGY";
	public static final String CAT_PILLARS_FLUID_ENERGY = "PILLARS_FLUID_ENERGY";
	public static final String CAT_HAMMER_UPGRADES = "HAMMER_UPRGADES";
	public static final String CAT_HAMMER_UPGRADES_RECIPES = "HAMMER_UPRGADES_RECIPES";
	public static final String CAT_HAMMER_CONFIG = "HAMMER_CONFIG";
	public static final String CAT_INJECTOR_RECIPES = "INJECTOR_RECIPES";
	
	public NThorManual(String name, int x, int y) {
		super(name,x,y);
		initComponent();
	}
	
	@Override
	public void initComponent() {
		this.componentList.put(CAT_MATERIALS, new GuiComponent(this.x+10, this.y+30, 100, 10){{
			addText("Materials", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_MATERIALS, new DummyCategory(CAT_MATERIALS,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addItemStack(new GuiItemStack(x,y+15,new ItemStack(ModBlocks.blockAsgardite)));
					addItemStack(new GuiItemStack(x,y+35,new ItemStack(ModBlocks.blockAsgardianSteel)));
					addItemStack(new GuiItemStack(x,y+55,new ItemStack(ModBlocks.blockAsgardianBronze)));
					addText("There is 3 types of materials :",0,0);
					addText("Agardite : basic asgardian material (Energy ratio : "+EnumPillarMaterial.values()[2].energyRatio+")",20,10);
					addText("Agardian Steel : iron in the injector (Energy ratio : "+EnumPillarMaterial.values()[1].energyRatio+")",20,10);
					addText("Agardian Bronze : gold in the injector (Energy ratio : "+EnumPillarMaterial.values()[0].energyRatio+")",20,10);
					addText("",0,0);
					addTextAutoWitdh("They all handle energy and fluids with their proper efficiency ratio."
							+ " So it will affect the structure or the block efficency that your willing to "
							+ "make with the said material.", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);
		
		this.componentList.put(CAT_PILLARS, new GuiComponent(this.x+10, this.y+40, 100, 10){{
			addText("Pillars", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_PILLARS, new DummyCategory(CAT_PILLARS,this.x, this.y){{
			addComponent("in2",
				new GuiComponent(x+10, y+30, 100, 10){{
					addImage(new GuiImage(this.x, this.y, 85, 105, 
							new ResourceLocation(Ressources.MODID+":textures/misc/all_crop.png"))
					);
				}});
			addComponent("in3",
				new GuiComponent(x+185, y+130, 100, 10){{
					addImage(new GuiImage(this.x, this.y, 16, 16, 
							new ResourceLocation(Ressources.MODID+":textures/blocks/asgardite_full_interface.png"))
					);
				}});
			addComponent("in",
				new GuiComponent(x+100, y+30, 100, 10){{
					addTextAutoWitdh("Pillars are multiblock structures, you can build them with asgardian materials."
							+ " To aquire those materials you can mine, produce them or find villages, they "
							+ "often have some pillar ruins.", 0, 0, NGuiManual.maxStringWidth-100);
					addTextAutoWitdh("Pillars can be linked wirelessly, with the basic hammer, to any asgardian machine "
							+ "or any other mod machines which accept RF or EU. Once linked it will provide energy in "
							+ "equal share to all the connected machines. Or pillars can directly recieve /transmit "
							+ "fluid and energy by their interface :",0,0, NGuiManual.maxStringWidth-100);
					addText("",0,0);
					addTextAutoWitdh("You can change the structures of a pillar by rebuilding the new structure on "
							+ "top of the master block, which is normaly find in the center bottom layer of the pillar.", -90, 00, NGuiManual.maxStringWidth);
				}});
			}}
		);	
		this.componentList.put(CAT_BASIC_HAMMER, new GuiComponent(this.x+10, this.y+50, 100, 10){{
			addText("Basic Hammer", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_BASIC_HAMMER, new DummyCategory(CAT_BASIC_HAMMER,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("The Basic hammer has modes you can change them by shift right "
							+ "clicking it in on an empty space.", 0, 0, NGuiManual.maxStringWidth-80);
					addText("Construction mode :",0,0);
					addTextAutoWitdh("Show pillars structures : shift right click an asgardian block "
							+ "of any materials to see the pillars structures you can do.", 5, 0, NGuiManual.maxStringWidth-80);

					addText("Basic mode :",0,0);
					addTextAutoWitdh("Build pillars : activate a pillar by clicking on the highest block of the pillar and it will "
							+ "setup the structure if you did it right.", 5, 0, NGuiManual.maxStringWidth-5);
					addTextAutoWitdh("Link pillars and machines : right click your working pillar and shift right click "
							+ "on any asgardian, RF or EU machine or EU machine even from other mods.", 5, 0, NGuiManual.maxStringWidth-5);
					addText("Forge mode :",0,0);
					addTextAutoWitdh("Apply upgrade : go read about ", 5, 0, NGuiManual.maxStringWidth-5);
					
					addRecipe(new GuiRecipe("Basic Hammer", this.x+270,this.y,new ItemStack(ModItems.itemBasicHammer),GuiRecipe.TYPE.VANILLA));
				}});
			addComponent(CAT_HAMMER_UPGRADES,
				new GuiComponent(x+175, y+160, 100, 10){{
					addText("Hammer upgrades", 0, 0);
					isLink = true;
				}});
			}}
		);
		
		this.componentList.put(CAT_LIQUEFIER, new GuiComponent(this.x+10, this.y+60, 100, 10){{
			addText("Liquefier", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_LIQUEFIER, new DummyCategory(CAT_LIQUEFIER,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("The liquefier will need water, power and some asgardite to "
							+ "create liquefied asgardite needed for the injector or producing more "
							+ "energy in your pillar. You can place a liquefier directly sided to a "
							+ "pillar interface, link it wirelessly with the basic hammer or use "
							+ "cables, pipes from other mods. Water can be inserted from all sides. "
							+ "Asgardite can be inserted from all sides. Liquefied asgardite can only be "
							+ "extracted from the back. If there is water in a 3x3 area right under "
							+ "it the liquefier will passively gain water.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Liquefier", this.x,this.y+90,new ItemStack(ModBlocks.blockLiquefier,1,2),GuiRecipe.TYPE.VANILLA));
					addRecipe(new GuiRecipe("Liquefier", this.x+80,this.y+90,new ItemStack(ModBlocks.blockLiquefier,1,1),GuiRecipe.TYPE.VANILLA));
					addRecipe(new GuiRecipe("Liquefier", this.x+160,this.y+90,new ItemStack(ModBlocks.blockLiquefier,1,0),GuiRecipe.TYPE.VANILLA));
				}});
			}}
		);	
		
		this.componentList.put(CAT_INJECTOR, new GuiComponent(this.x+10, this.y+70, 100, 10){{
			addText("Injector", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_INJECTOR, new DummyCategory(CAT_INJECTOR,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("The injector will need liquefied asgardite and power to inject "
							+ "liquefied asgardite into items. You can place an injector directly sided "
							+ "to a pillar interface, link it wirelessly with the basic hammer or use "
							+ "cables, pipes from other mods. Liquefied asgardite can only be inserted "
							+ "from the back. Recipes materials can be inserted from all sides.", 0, 0, NGuiManual.maxStringWidth);					
					addRecipe(new GuiRecipe("Injector", this.x,this.y+70,new ItemStack(ModBlocks.blockInjector,1,2),GuiRecipe.TYPE.VANILLA));
					addRecipe(new GuiRecipe("Injector", this.x+80,this.y+70,new ItemStack(ModBlocks.blockInjector,1,1),GuiRecipe.TYPE.VANILLA));
					addRecipe(new GuiRecipe("Injector", this.x+160,this.y+70,new ItemStack(ModBlocks.blockInjector,1,0),GuiRecipe.TYPE.VANILLA));
				}});
			}}
		);	
		
		this.componentList.put(CAT_HAMMERSTAND, new GuiComponent(this.x+10, this.y+80, 100, 10){{
			addText("Hammer stand", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_HAMMERSTAND, new DummyCategory(CAT_HAMMERSTAND,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("You will need an hammer stand to invok Thor's hammer, just link "
							+ "it with the basic hammer to a pillar, if the pillar has sufficient "
							+ "energy (at least "+ModConfig.rfNeededThorHammer+" RF) you will be "
							+ "able to invok the hammer. The hammer stand act like an inventory "
							+ "only holding one hammer, once in the inventory the hammer will recharge "
							+ "itself if the hammer stand is connected to a pillar with power.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Hammer Stand", this.x,this.y+70,new ItemStack(ModBlocks.blockHammerStand,1,0),GuiRecipe.TYPE.VANILLA));
				}});
			}}
		);	
		
		this.componentList.put(CAT_THOR_HAMMER, new GuiComponent(this.x+10, this.y+90, 100, 10){{
			addText("Thor's Hammer", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_THOR_HAMMER, new DummyCategory(CAT_THOR_HAMMER,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("Thor's Hammer has 4 modes (right click), you can change them by shift "
							+ "right clicking it :", 0, 0, NGuiManual.maxStringWidth);
					addTextAutoWitdh("Mining (default) : you can throw your hammer it will mine everything on its way.", 10, 0, NGuiManual.maxStringWidth-20);
					addTextAutoWitdh("Lightning : you can strike block or entity, from a distance, with lightning it will cause "
							+ "damage and fire.", 10, 0, NGuiManual.maxStringWidth-20);
					addTextAutoWitdh("Moving : you can throw your hammer and move with it.", 10, 0, NGuiManual.maxStringWidth-20);
					addTextAutoWitdh("Teleporting : you can set teleport point everywhere.", 10, 0, NGuiManual.maxStringWidth-20);
					addText("",0,0);
					addTextAutoWitdh("At any moment you can use (left click) your hammer as a multitool, it act like a pickaxe, "
							+ "a shovel and a sword.", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);	
		
		this.componentList.put(CAT_UBER_MIGHTY_FEATHER, new GuiComponent(this.x+10, this.y+100, 110, 10){{
			addText("(Uber) Mighty Feather", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_UBER_MIGHTY_FEATHER, new DummyCategory(CAT_UBER_MIGHTY_FEATHER,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("Mighty feather are dropped by chiken, dead of a lightning strike, "
							+ "the easyest way to get some its to install the lightning mode on your hammer "
							+ "and kill some chicken with it. Then you will be able to craft Uber mighty feather.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Uber Mighty Feather", this.x,this.y+50,new ItemStack(ModItems.itemUberMightyFeather,1,0),GuiRecipe.TYPE.VANILLA));
				}});
			}}
		);	
		
		this.componentList.put(CAT_HOWTO, new GuiComponent(this.x+10, this.y+120, 100, 10){{
			addText("How to progress", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_HOWTO, new DummyCategory(CAT_HOWTO,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("To start you will need to build a pillar, you can go mine "
							+ "asgardite or find villages and salvage their ruins to gather materials. "
							+ "You will need to make an hammer stand and connect it to a pillar, if you "
							+ "don't have enough energy stored or you don't want to wait the pillar to "
							+ "slowly fill itself, maybe you'll need a liquefier and an injector to have "
							+ "better materials to build better pillars to be able to invok Thor's hammer. "
							+ "After invoking enjoy your hammer upgrade it and be OP.", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);	
		
		this.componentList.put(CAT_TIPS, new GuiComponent(this.x+10, this.y+130, 100, 10){{
			addText("Tips", 0, 0);
			isLink = true;
		}});
		this.listCategory.put(CAT_TIPS, new DummyCategory(CAT_TIPS,this.x, this.y){{
			addComponent("in",
				new GuiComponent(this.x+10, this.y+30, 100, 10){{
					addTextAutoWitdh("To find a lot of asgardite ore easily, find an extreme hills biome, "
							+ "you'll see ores on the surface. Steel or bronze pillars are worth building instead "
							+ "of waiting your asgardite pillar to fill.", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);
		
		
		this.componentList.put(CAT_PILLARS_STRUCTURES, new GuiComponent(this.x+180, this.y+30, 100, 10){{
			addText("Pillars structures", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_PILLARS_STRUCTURES, new DummyCategory(CAT_PILLARS_STRUCTURES,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{			
					addTextAutoWitdh("To see the structure in 3d right click a material block with a basic hammer set in construction mode.", 0, 0, NGuiManual.maxStringWidth);
					addText("",0,0);
					/*for(Map.Entry<String, Pillar> entry :PillarsConfig.getInstance().nameToPillar.entrySet()){
			
						addTextAutoWitdh(entry.getKey()+" : "+entry.getValue().mapStructure.size()+" blocks; "+
								NumberFormat.getNumberInstance().format(entry.getValue().energyCapacity)+" RF; "+
								NumberFormat.getNumberInstance().format(entry.getValue().fluidCapacity)+" FL; ratio : "+
								entry.getValue().naturalRatio+".", 0, 0, NGuiManual.maxStringWidth);
					}*/
				}});
			}}
		);	
		
		this.componentList.put(CAT_PILLARS_PASSIVE_ENERGY, new GuiComponent(this.x+180, this.y+40, 120, 10){{
			addText("Pillars passive energy", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_PILLARS_PASSIVE_ENERGY, new DummyCategory(CAT_PILLARS_PASSIVE_ENERGY,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("Pillar will passively produce energy depending on : their material ratio, "
							+ "their structural ratio, the height there at (higher will produce more) and if it "
							+ "is raining or not (it will produce more while raining ).", 0, 0, NGuiManual.maxStringWidth);
					addTextAutoWitdh("Formula :  Math.pow( structureratio ,2.2 ) * Math.pow( materialEnergyRatio + "
							+ "Math.pow( ratioHeight * 2 ,4 ) + ratioRaining ,1.5 ) ", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);	
		
		this.componentList.put(CAT_PILLARS_FLUID_ENERGY, new GuiComponent(this.x+180, this.y+50, 120, 10){{
			addText("Pillars fluid energy", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_PILLARS_FLUID_ENERGY, new DummyCategory(CAT_PILLARS_FLUID_ENERGY,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("An other way to produce tons of energy its to use liquified asgardite "
							+ "inside the pillar, if you connect a control panel to the pillar you will be able "
							+ "to control the amount of fluid per tick your pillar will consume to produce additional "
							+ "energy.", 0, 0, NGuiManual.maxStringWidth);
					addTextAutoWitdh("Formula : Math.pow( Math.pow(structureratio, 1.1) * Math.pow(ds.amount/20+1,1.5) "
							+ "*  Math.pow(materialEnergyRatio, 1.1) , 0.85 )", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);	
		
		this.componentList.put(CAT_HAMMER_UPGRADES, new GuiComponent(this.x+180, this.y+60, 100, 10){{
			addText("Hammer upgrades", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_HAMMER_UPGRADES, new DummyCategory(CAT_HAMMER_UPGRADES,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("To apply upgrade to your hammer place it in an hammer stand, "
							+ "drop the items needed for the upgrade on top of the stand and right click "
							+ "the top of the stand with a basic hammer set on Forge mode.", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);	
		
		this.componentList.put(CAT_HAMMER_UPGRADES_RECIPES, new GuiComponent(this.x+180, this.y+70, 130, 10){{
			addText("Hammer upgrades recipes", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_HAMMER_UPGRADES_RECIPES, new DummyCategory(CAT_HAMMER_UPGRADES_RECIPES,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					nbGuiRecipeVertical = 2;
					nbGuiRecipeHorizontal = 5;
					yButtonRecipes = 100;
					handleRecipesGroup = true;
					for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(HammerUpgradesRecipesHandler.USAGE_HAMMER_UPGRADES).entrySet()){
						List<ItemStack> it = new ArrayList<ItemStack>();
						entry.getValue().getInputs().forEach((ge)->it.add(ge.getItemStack()));
						ItemStack hammer = new ItemStack(ModItems.itemThorHammer);
						for(NbtTagToAdd tag :RecipeHammerUpgrades.class.cast(entry.getValue()).getNbtTagList()){
							RecipeOnHammerStand.addTagToHammer(tag, hammer);
						}
						addRecipe(new GuiRecipe(entry.getValue().getUid(),x+10,y,GuiRecipe.getGuiItemStack(new ArrayList<ItemStack>(){{add(hammer);}}),GuiRecipe.TYPE.HAMMERSTAND,GuiRecipe.getGuiItemStack(it)));
					}
				}});
			}}
		);	
		
		this.componentList.put(CAT_HAMMER_CONFIG, new GuiComponent(this.x+180, this.y+80, 100, 10){{
			addText("Hammer config", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_HAMMER_CONFIG, new DummyCategory(CAT_HAMMER_CONFIG,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("You can configure your hammer when it is in an hammer stand or if you have the config "
							+ "upgrade installed, you can press H (by default) when your are holding it. You can configure "
							+ "the enchants your hammer has. Just click on enchants in the config GUI and disable or enable "
							+ "enchants you allready have installed.",0,0, NGuiManual.maxStringWidth);
				}});
			}}
		);		
		
		this.componentList.put(CAT_INJECTOR_RECIPES, new GuiComponent(this.x+180, this.y+90, 100, 10){{
			addText("Injector recipes", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_INJECTOR_RECIPES, new DummyCategory(CAT_INJECTOR_RECIPES,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{	
					nbGuiRecipeVertical = 2;
					nbGuiRecipeHorizontal = 5;
					yButtonRecipes = 100;
					handleRecipesGroup = true;
					for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(InjectorRecipesHandler.USAGE_INJECTOR).entrySet()){
						List<ItemStack> it = new ArrayList<ItemStack>();
						entry.getValue().getInputs().forEach((ge)->it.add(ge.getItemStack()));
						List<ItemStack> it2 = new ArrayList<ItemStack>();
						entry.getValue().getOutputs().forEach((ge)->it2.add(ge.getItemStack()));
						addRecipe(new GuiRecipe(entry.getValue().getUid(),x,y,GuiRecipe.getGuiItemStack(it2),GuiRecipe.TYPE.INJECTOR,GuiRecipe.getGuiItemStack(it)));
					}
				}});
		}});

	}
}
