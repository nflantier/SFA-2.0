package noelflantier.sfartifacts.client.gui.manual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import noelflantier.sfartifacts.client.gui.bases.GuiComponent;
import noelflantier.sfartifacts.client.gui.bases.GuiRecipe;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModItems;

public class NBlocksAndItems  extends ABaseCategory{

	public static final String PRE_IB = "PRE_IB";
	
	public NBlocksAndItems(String name, int x, int y) {
		super(name,x,y);
		initComponent();
	}

	@Override
	public void initComponent() {
		
		Comparator<ItemStack> comparator = (i1,i2)-> (int)I18n.format(i1.getItem().getUnlocalizedName(i1)+".name").charAt(0)-(int)I18n.format(i2.getItem().getUnlocalizedName(i2)+".name").charAt(0);
		List<ItemStack> vanilla = new ArrayList<ItemStack>();
		vanilla.add(new ItemStack(ModBlocks.blockAsgardianBronze,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockAsgardianSteel,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockAsgardite,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockControlPanel,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockHammerStand,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockInjector,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockLightningRodStand,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockLiquefier,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockMightyFoundry,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockMrFusion,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockRecharger,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockSoundEmiter,1,0));
		vanilla.add(new ItemStack(ModItems.itemAsgardianRing,1,0));
		vanilla.add(new ItemStack(ModItems.itemBasicHammer,1,0));
		vanilla.add(new ItemStack(ModItems.itemEnergeticConvector,1,0));
		vanilla.add(new ItemStack(ModItems.itemEnergyModule,1,0));
		vanilla.add(new ItemStack(ModItems.itemFluidModule,1,0));
		vanilla.add(new ItemStack(ModItems.itemFusionCore,1,0));
		vanilla.add(new ItemStack(ModItems.itemCable,1,0));
		vanilla.add(new ItemStack(ModItems.itemCable,1,1));
		vanilla.add(new ItemStack(ModItems.itemCable,1,2));
		vanilla.add(new ItemStack(ModItems.itemCable,1,3));
		vanilla.add(new ItemStack(ModBlocks.blockInductor,1,0));
		vanilla.add(new ItemStack(ModBlocks.blockInductor,1,1));
		vanilla.add(new ItemStack(ModItems.itemCircuitBoard,1,0));
		vanilla.add(new ItemStack(ModItems.itemCircuitBoard,1,1));
		vanilla.add(new ItemStack(ModItems.itemCircuitBoard,1,2));
		vanilla.add(new ItemStack(ModItems.itemGlassCutter,1,0));
		vanilla.add(new ItemStack(ModItems.itemHoverboard,1,0));
		vanilla.add(new ItemStack(ModItems.itemHoverboard,1,2));
		vanilla.add(new ItemStack(ModItems.itemLightningRod,1,0));
		vanilla.add(new ItemStack(ModItems.itemLightningRod,1,1));
		vanilla.add(new ItemStack(ModItems.itemLightningRod,1,2));
		vanilla.add(new ItemStack(ModItems.itemLightningRod,1,3));
		vanilla.add(new ItemStack(ModItems.itemMagnet,1,0));
		vanilla.add(new ItemStack(ModItems.itemManual,1,0));
		vanilla.add(new ItemStack(ModItems.itemMicroChip,1,0));
		vanilla.add(new ItemStack(ModItems.itemMicroChip,1,1));
		vanilla.add(new ItemStack(ModItems.itemMicroChip,1,2));
		vanilla.add(new ItemStack(ModItems.itemMicroChip,1,3));
		vanilla.add(new ItemStack(ModItems.itemMold,1,0));
		vanilla.add(new ItemStack(ModItems.itemSilicon,1,0));
		vanilla.add(new ItemStack(ModItems.itemStabilizer,1,0));
		vanilla.add(new ItemStack(ModItems.itemThruster,1,0));
		vanilla.add(new ItemStack(ModItems.itemUberMightyFeather,1,0));
		vanilla.add(new ItemStack(ModItems.itemVibraniumAlloySheet,1,0));
		vanilla.add(new ItemStack(ModItems.itemVibraniumShield,1,1));
		vanilla.sort(comparator);
		
		int dec = 0 ;
		float scale = 0.6F;
		this.componentList.put("vanilla", new GuiComponent(this.x+10+100*(int)((dec)/25), this.y+30+(int)(10*scale)*((dec)%25), 90, (int)(10*scale)){{
			globalScale = scale;
			addText("VANILLA RECIPES :", 0, 0);
		}});
		dec+=1;
		for(int i = 0 ; i < vanilla.size() ; i++){
			ItemStack it = vanilla.get(i);
			String strID = PRE_IB+it.getItem().getUnlocalizedName(it);
			String translated = I18n.format(it.getItem().getUnlocalizedName(it)+".name");
			this.componentList.put(strID, new GuiComponent(this.x+10+100*(int)((i+dec)/25), this.y+30+(int)(10*scale)*((i+dec)%25), 90, (int)(10*scale)){{
				globalScale = scale;
				addText(translated, 0, 0);
				isLink = true;
			}});
			listCategory.put(strID, new DummyCategory(strID,this.x, this.y){{
				addComponent("in",
					new GuiComponent(x+10, y+30, 100, 10){{
						addRecipe(new GuiRecipe(translated, this.x,this.y,it,GuiRecipe.TYPE.VANILLA));
					}});
				}}
			);
		}
		
		dec = dec+vanilla.size()+1;
		this.componentList.put("injector", new GuiComponent(this.x+10+100*(int)((dec)/25), this.y+30+(int)(10*scale)*((dec)%25), 90, (int)(10*scale)){{
			globalScale = scale;
			addText("INJECTOR RECIPES :", 0, 0);
		}});
		dec+=1;
		List<ItemStack> injector = new ArrayList<ItemStack>();
		injector.add(new ItemStack(ModBlocks.blockAsgardianGlass,1,0));
		injector.add(new ItemStack(ModBlocks.blockInductor,1,2));
		injector.add(new ItemStack(ModBlocks.blockInductor,1,3));
		injector.add(new ItemStack(ModItems.itemAsgardianBronzeIngot,1,0));
		injector.add(new ItemStack(ModItems.itemAsgardianSteelIngot,1,0));
		injector.add(new ItemStack(ModItems.itemAsgardianPearl,1,0));
		injector.add(new ItemStack(ModItems.itemMightyHulkRing,1,0));
		injector.add(new ItemStack(ModItems.itemVibraniumAlloy,1,0));
		injector.sort(comparator);
		for(int i = 0 ; i < injector.size() ; i++){
			ItemStack it = injector.get(i);
			String strID = PRE_IB+it.getItem().getUnlocalizedName(it);
			String translated = I18n.format(it.getItem().getUnlocalizedName(it)+".name");
			this.componentList.put(strID, new GuiComponent(this.x+10+100*(int)((i+dec)/25), this.y+30+(int)(10*scale)*((i+dec)%25), 90, (int)(10*scale)){{
				globalScale = scale;
				addText(translated, 0, 0);
				isLink = true;
			}});
			listCategory.put(strID, new DummyCategory(strID,this.x, this.y){{
				addComponent("in",
					new GuiComponent(x+10, y+30, 100, 10){{
						addRecipe(new GuiRecipe(translated, this.x,this.y,it,GuiRecipe.TYPE.INJECTOR));
					}});
				}}
			);
		}	
		
		dec = dec+injector.size()+1;
		this.componentList.put("foundry", new GuiComponent(this.x+10+100*(int)((dec)/25), this.y+30+(int)(10*scale)*((dec)%25), 90, (int)(10*scale)){{
			globalScale = scale;
			addText("MIGHTY FOUNDRY RECIPES :", 0, 0);
		}});
		dec+=1;
		List<ItemStack> foundry = new ArrayList<ItemStack>();
		foundry.add(new ItemStack(ModItems.itemMuonBoosterCasing,1,0));
		foundry.add(new ItemStack(ModItems.itemVibraniumShield,1,0));
		foundry.add(new ItemStack(ModItems.itemFusionCasing,1,0));
		foundry.sort(comparator);
		for(int i = 0 ; i < foundry.size() ; i++){
			ItemStack it = foundry.get(i);
			String strID = PRE_IB+it.getItem().getUnlocalizedName(it);
			String translated = I18n.format(it.getItem().getUnlocalizedName(it)+".name");
			this.componentList.put(strID, new GuiComponent(this.x+10+100*(int)((i+dec)/25), this.y+30+(int)(10*scale)*((i+dec)%25), 90, (int)(10*scale)){{
				globalScale = scale;
				addText(translated, 0, 0);
				isLink = true;
			}});
			listCategory.put(strID, new DummyCategory(strID,this.x, this.y){{
				addComponent("in",
					new GuiComponent(x+10, y+30, 100, 10){{
						addRecipe(new GuiRecipe(translated, this.x,this.y,it,GuiRecipe.TYPE.MIGHTYFOUNDRY));
					}});
				}}
			);
		}
		
	}

}
