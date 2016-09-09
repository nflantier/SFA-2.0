package noelflantier.sfartifacts.client.gui.manual;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiItemStack;
import noelflantier.sfartifacts.client.gui.bases.GuiRecipe;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeMightyFoundry;
import noelflantier.sfartifacts.common.recipes.RecipeMold;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.MightyFoundryRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.MoldRecipesHandler;

public class NCaptainManual  extends ABaseCategory{

	public static final String CAT_VIBRANIUM_ORE = "VIBRANIUM_ORE";
	public static final String CAT_MOLDS = "MOLDS";
	public static final String CAT_FOUNDRY = "FOUNDRY";
	public static final String CAT_FOUNDRY_RECIPES = "FOUNDRY_RECIPES";
	public static final String CAT_MOLD_PATTERN = "MOLD_PATTERN";
	public static final String CAT_SHIELD = "SHIELD";
	
	public NCaptainManual(String name, int x, int y) {
		super(name,x,y);
		initComponent();
	}
	
	@Override
	public void initComponent() {

		this.componentList.put(CAT_VIBRANIUM_ORE, new GuiComponent(this.x+10, this.y+30, 100, 10){{
			addText("Vibranium ore", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_VIBRANIUM_ORE, new DummyCategory(CAT_VIBRANIUM_ORE,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addItemStack(new GuiItemStack(x,y+60,new ItemStack(ModBlocks.blockOreVibranium,1,0)));
					addItemStack(new GuiItemStack(x+17,y+60,new ItemStack(ModBlocks.blockOreVibranium,1,6)));
					addItemStack(new GuiItemStack(x+34,y+60,new ItemStack(ModBlocks.blockOreVibranium,1,12)));
					addItemStack(new GuiItemStack(x+51,y+60,new ItemStack(ModBlocks.blockOreVibranium,1,15)));
					addItemStack(new GuiItemStack(x+68,y+60,new ItemStack(ModItems.itemVibraniumDust,1,0)));
					addTextAutoWitdh("Vibranium ores are found only under Savanna biomes (Savanna, "
							+ "Savanna M, Savanna Plateau, Savanna Plateau M). Vibranium ores need "
							+ "to be 'cooked' for you to be able to mine their dust. The only way to "
							+ "cook them it's to surround them with lava or any really hot liquid, "
							+ "they will start heating and become cooked after some time. You can "
							+ "find them between the Y levels 3 to 18.", 0, 0, NGuiManual.maxStringWidth);
				}});
			}}
		);
		
		this.componentList.put(CAT_MOLDS, new GuiComponent(this.x+10, this.y+40, 100, 10){{
			addText("Molds", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_MOLDS, new DummyCategory(CAT_MOLDS,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("Molds are used in the Mighty Foundry. To fill a mold right click it and put in it inventory "
							+ "sand in the shape of the mold you want. If the mold is valid you can see the name of the item to forge "
							+ "on the mold annotations.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Mold", this.x,this.y+50,new ItemStack(ModItems.itemMold,1,0),GuiRecipe.TYPE.VANILLA));
				}});
			}}
		);
		
		this.componentList.put(CAT_FOUNDRY, new GuiComponent(this.x+10, this.y+50, 100, 10){{
			addText("Mighty foundry", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_FOUNDRY, new DummyCategory(CAT_FOUNDRY,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("To use the foundry you need Molds. Once you have a valid mold put it "
							+ "in the foundry and lock it. The foundry works only if the mold is locked, if "
							+ "you unlock it and get the mold out, the progress on the mold will be deleted. "
							+ "The foundry works with RF energy but if you provide it lava, the process will "
							+ "be accelerate significantly. You can put items to melt once the mold is locked.", 0, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Mighty Foundry", this.x,this.y+70,new ItemStack(ModBlocks.blockMightyFoundry,1,0),GuiRecipe.TYPE.VANILLA));
				}});
			}}
		);
		
		this.componentList.put(CAT_FOUNDRY_RECIPES, new GuiComponent(this.x+10, this.y+60, 100, 10){{
			addText("Mighty foundry recipes", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_FOUNDRY_RECIPES, new DummyCategory(CAT_FOUNDRY_RECIPES,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					nbGuiRecipeVertical = 2;
					nbGuiRecipeHorizontal = 5;
					yButtonRecipes = 100;
					handleRecipesGroup = true;
					for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(MightyFoundryRecipesHandler.USAGE_MIGHTY_FOUNDRY).entrySet()){
						List<ItemStack> it = new ArrayList<ItemStack>();
						entry.getValue().getInputs().forEach((ge)->it.add(new ItemStack(ge.getItemStack().getItem(),RecipeMightyFoundry.class.cast(entry.getValue()).getItemQuantity(),ge.getItemStack().getItemDamage())));
						List<ItemStack> it2 = new ArrayList<ItemStack>();
						entry.getValue().getOutputs().forEach((ge)->it2.add(ge.getItemStack()));
						addRecipe(new GuiRecipe(entry.getValue().getUid(),x,y,GuiRecipe.getGuiItemStack(it2),GuiRecipe.TYPE.MIGHTYFOUNDRY,GuiRecipe.getGuiItemStack(it)));
					}
				}});
			}}
		);
		
		this.componentList.put(CAT_MOLD_PATTERN, new GuiComponent(this.x+10, this.y+70, 100, 10){{
			addText("Mold pattern", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_MOLD_PATTERN, new DummyCategory(CAT_MOLD_PATTERN,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+100, y+25, 100, 10){{
					nbGuiRecipeVertical = 1;
					nbGuiRecipeHorizontal = 1;
					yButtonRecipes = 130;
					xButtonRecipes = -50;
					handleRecipesGroup = true;
					for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(MoldRecipesHandler.USAGE_MOLD).entrySet()){
						int[] s = RecipeMold.class.cast(entry.getValue()).getTabShape();
						List<ItemStack> it = new ArrayList<ItemStack>();	
						for(int i=0;i<s.length;i++){
							String bin = Integer.toBinaryString(s[i]);
							int l = 9-bin.length();
							for(int j=0;j<l;j++)
								bin = "0"+bin;
							for(int k=0;k<bin.length();k++){
								if(bin.substring(k, k+1).equals("1"))
									it.add(new ItemStack(Blocks.SAND));
								else
									it.add(null);
								
							}
						}	
						List<ItemStack> it2 = new ArrayList<ItemStack>();	
						int m = RecipeMold.class.cast(entry.getValue()).getMoldMeta();
						ItemStack it0 = new ItemStack(ModItems.itemMold, 1, m);
						it0.setItemDamage(m);
						it0 = ItemNBTHelper.setInteger(it0, "idmold", m);
						it0 = ItemNBTHelper.setIntegerArray(it0, "moldstructure", s.clone());
						it2.add(it0);
						addRecipe(new GuiRecipe(entry.getValue().getUid(),x,y,GuiRecipe.getGuiItemStack(it2),GuiRecipe.TYPE.MOLD,GuiRecipe.getGuiItemStack(it)));
					}
				}});
			}}
		);
		
		this.componentList.put(CAT_SHIELD, new GuiComponent(this.x+10, this.y+80, 100, 10){{
			addText("Shields", 0, 0);
			isLink = true;
		}});
		listCategory.put(CAT_SHIELD, new DummyCategory(CAT_SHIELD,this.x, this.y){{
			addComponent("in",
				new GuiComponent(x+10, y+30, 100, 10){{
					addTextAutoWitdh("Vibranium shield can block damages from ennemies. Its a powerfull "
							+ "combat tool it can be use as a melee weapons or ranged if you throw it at "
							+ "any entities.", 0, 0, NGuiManual.maxStringWidth);
					addTextAutoWitdh("Depending on the config : ", 0, 0, NGuiManual.maxStringWidth);
					addTextAutoWitdh("Damages will only be blocked on a certain angle around you.", 10, 0, NGuiManual.maxStringWidth);
					addTextAutoWitdh("You have to press SHIFT to shield you and block damages.", 10, 0, NGuiManual.maxStringWidth);
					addRecipe(new GuiRecipe("Vibranium shield", this.x,this.y+65,new ItemStack(ModItems.itemVibraniumShield,1,0),GuiRecipe.TYPE.MIGHTYFOUNDRY));
					addRecipe(new GuiRecipe("Captain America shield", this.x+80,this.y+65,new ItemStack(ModItems.itemVibraniumShield,1,1),GuiRecipe.TYPE.VANILLA));
				}});
			}}
		);
	}
}
