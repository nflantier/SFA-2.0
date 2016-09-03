package noelflantier.sfartifacts.common.handlers;

import java.util.Map;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.helpers.RegisterHelper;
import noelflantier.sfartifacts.common.items.ItemAsgardianBronzeIngot;
import noelflantier.sfartifacts.common.items.ItemAsgardianPearl;
import noelflantier.sfartifacts.common.items.ItemAsgardianRing;
import noelflantier.sfartifacts.common.items.ItemAsgardianSteelIngot;
import noelflantier.sfartifacts.common.items.ItemAsgardite;
import noelflantier.sfartifacts.common.items.ItemBasicHammer;
import noelflantier.sfartifacts.common.items.ItemCable;
import noelflantier.sfartifacts.common.items.ItemCircuitBoard;
import noelflantier.sfartifacts.common.items.ItemEnergeticConvector;
import noelflantier.sfartifacts.common.items.ItemEnergyModule;
import noelflantier.sfartifacts.common.items.ItemFluidModule;
import noelflantier.sfartifacts.common.items.ItemFusionCasing;
import noelflantier.sfartifacts.common.items.ItemFusionCore;
import noelflantier.sfartifacts.common.items.ItemGlassCutter;
import noelflantier.sfartifacts.common.items.ItemHoverBoard;
import noelflantier.sfartifacts.common.items.ItemHulkFlesh;
import noelflantier.sfartifacts.common.items.ItemLightningRod;
import noelflantier.sfartifacts.common.items.ItemMagnet;
import noelflantier.sfartifacts.common.items.ItemManual;
import noelflantier.sfartifacts.common.items.ItemMicroChip;
import noelflantier.sfartifacts.common.items.ItemMightyFeather;
import noelflantier.sfartifacts.common.items.ItemMightyHulkRing;
import noelflantier.sfartifacts.common.items.ItemMold;
import noelflantier.sfartifacts.common.items.ItemMuonBoosterCasing;
import noelflantier.sfartifacts.common.items.ItemSilicon;
import noelflantier.sfartifacts.common.items.ItemStabilizer;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.items.ItemThruster;
import noelflantier.sfartifacts.common.items.ItemUberMightyFeather;
import noelflantier.sfartifacts.common.items.ItemVibraniumAlloy;
import noelflantier.sfartifacts.common.items.ItemVibraniumAlloySheet;
import noelflantier.sfartifacts.common.items.ItemVibraniumDust;
import noelflantier.sfartifacts.common.items.ItemVibraniumShield;
import noelflantier.sfartifacts.common.items.blocks.ItemBlockSFA.EnumOriented;
import noelflantier.sfartifacts.common.items.blocks.ItemHammerStand;
import noelflantier.sfartifacts.common.items.blocks.ItemInductor;
import noelflantier.sfartifacts.common.items.blocks.ItemOreVibranium;
import noelflantier.sfartifacts.common.items.blocks.ItemUsingMaterials;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;
import noelflantier.sfartifacts.common.recipes.RecipeMold;
import noelflantier.sfartifacts.common.recipes.RecipesRegistry;
import noelflantier.sfartifacts.common.recipes.handler.MoldRecipesHandler;

public class ModItems {

	public static Item itemAsgardite;
	public static Item itemThorHammer;
	public static Item itemAsgardianBronzeIngot;
	public static Item itemAsgardianSteelIngot;
	public static Item itemAsgardianRing;
	public static Item itemAsgardianPearl;
	public static Item itemEnergyModule;
	public static Item itemFluidModule;
	public static Item itemFusionCasing;
	public static Item itemGlassCutter;
	public static Item itemFusionCore;
	public static Item itemEnergeticConvector;
	public static Item itemMagnet;
	public static Item itemMightyFeather;
	public static Item itemMuonBoosterCasing;
	public static Item itemStabilizer;
	public static Item itemThruster;
	public static Item itemUberMightyFeather;
	public static Item itemVibraniumDust;
	public static Item itemVibraniumAlloy;
	public static Item itemVibraniumAlloySheet;
	public static Item itemLightningRod;
	public static Item itemMold;
	public static Item itemHulkFlesh;
	public static Item itemCable;
	public static Item itemMicroChip;
	public static Item itemCircuitBoard;
	public static Item itemSilicon;
	public static Item itemVibraniumShield;
	public static Item itemBasicHammer;
	public static Item itemHoverboard;
	public static Item itemManual;
	
	public static Item itemMightyHulkRing;

	public static ItemBlock itemOreVibranium;
	public static ItemBlock itemHammerStand;
	public static ItemBlock itemInjector;
	public static ItemBlock itemLightningRodStand;
	public static ItemBlock itemLiquefier;
	public static ItemBlock itemInductor;

    @SideOnly(Side.CLIENT)
	public static void preInitRenderItems() {
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemThorHammer, 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_THOR_HAMMER, "thor_hammer"));
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemThorHammer, 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_THOR_HAMMER, "thor_hammer"));
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemThorHammer, 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_THOR_HAMMER, "thor_hammer_throw"));
    	
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemVibraniumShield, 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_VIBRANIUM_SHIELD, "shield_vibranium"));
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemVibraniumShield, 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_VIBRANIUM_SHIELD, "shield_captainamerica"));
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemVibraniumShield, 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_VIBRANIUM_SHIELD, "shield_block"));
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemVibraniumShield, 15, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_VIBRANIUM_SHIELD, "shield_throw_vibranium"));
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemVibraniumShield, 16, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_VIBRANIUM_SHIELD, "shield_throw_captainamerica"));
    	
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemLightningRod, 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIGHTNING_ROD, "l_"+Ressources.UL_NAME_TYPE_LIGHTNINGROD[0]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemLightningRod, 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIGHTNING_ROD, "l_"+Ressources.UL_NAME_TYPE_LIGHTNINGROD[1]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemLightningRod, 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIGHTNING_ROD, "l_"+Ressources.UL_NAME_TYPE_LIGHTNINGROD[2]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemLightningRod, 3, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIGHTNING_ROD, "l_"+Ressources.UL_NAME_TYPE_LIGHTNINGROD[3]));
		
		ModelLoader.setCustomModelResourceLocation(ModItems.itemHoverboard, 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_HOVERBOARD, Ressources.UL_NAME_TYPE_HOVERBOARD[0]));
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemHoverboard, 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_HOVERBOARD, Ressources.UL_NAME_TYPE_HOVERBOARD[0]));
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemHoverboard, 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_HOVERBOARD, Ressources.UL_NAME_TYPE_HOVERBOARD[1]));
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemHoverboard, 3, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_HOVERBOARD, Ressources.UL_NAME_TYPE_HOVERBOARD[1]));
    	ModelLoader.setCustomModelResourceLocation(ModItems.itemHoverboard, 15, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_HOVERBOARD, "in_use"));
    	
		ModelLoader.setCustomModelResourceLocation(ModItems.itemMold, 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_MOLD, "mold_"+Ressources.UL_NAME_TYPE_MOLDS[0]));
		for(Map.Entry<String, ISFARecipe> entry : RecipesRegistry.instance.getRecipesForUsage(MoldRecipesHandler.USAGE_MOLD).entrySet()){
			int m = RecipeMold.class.cast(entry.getValue()).getMoldMeta();
			ModelLoader.setCustomModelResourceLocation(ModItems.itemMold, m, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_MOLD, "mold_"+Ressources.UL_NAME_TYPE_MOLDS[1]));
		}
		
		ModelLoader.setCustomModelResourceLocation(ModItems.itemCable, 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_CABLE, Ressources.UL_NAME_TYPE_CABLE[0]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemCable, 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_CABLE, Ressources.UL_NAME_TYPE_CABLE[1]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemCable, 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_CABLE, Ressources.UL_NAME_TYPE_CABLE[2]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemCable, 3, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_CABLE, Ressources.UL_NAME_TYPE_CABLE[3]));
		
		ModelLoader.setCustomModelResourceLocation(ModItems.itemMicroChip, 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_MICRO_CHIP, Ressources.UL_NAME_TYPE_MICRO_CHIP[0]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemMicroChip, 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_MICRO_CHIP, Ressources.UL_NAME_TYPE_MICRO_CHIP[1]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemMicroChip, 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_MICRO_CHIP, Ressources.UL_NAME_TYPE_MICRO_CHIP[2]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemMicroChip, 3, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_MICRO_CHIP, Ressources.UL_NAME_TYPE_MICRO_CHIP[3]));
		
		ModelLoader.setCustomModelResourceLocation(ModItems.itemCircuitBoard, 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_CIRCUIT_BOARD, Ressources.UL_NAME_TYPE_CIRCUIT_BOARD[0]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemCircuitBoard, 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_CIRCUIT_BOARD, Ressources.UL_NAME_TYPE_CIRCUIT_BOARD[1]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemCircuitBoard, 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_CIRCUIT_BOARD, Ressources.UL_NAME_TYPE_CIRCUIT_BOARD[2]));

		ModelLoader.setCustomModelResourceLocation(ModItems.itemSilicon, 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_SILICON, Ressources.UL_NAME_TYPE_SILICON[0]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemSilicon, 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_SILICON, Ressources.UL_NAME_TYPE_SILICON[1]));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemSilicon, 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_SILICON, Ressources.UL_NAME_TYPE_SILICON[2]));
	
	}

    @SideOnly(Side.CLIENT)
	public static void initRenderItems() {
    	RegisterHelper.registerInventoryVariantItemRender(itemAsgardite,0,Ressources.UL_NAME_ASGARDITE_DUST);
    	RegisterHelper.registerInventoryVariantItemRender(itemAsgardianBronzeIngot,0,Ressources.UL_NAME_ASGARDIAN_BRONZE_INGOT);
    	RegisterHelper.registerInventoryVariantItemRender(itemAsgardianSteelIngot,0,Ressources.UL_NAME_ASGARDIAN_STEEL_INGOT);
    	RegisterHelper.registerInventoryVariantItemRender(itemAsgardianRing,0,Ressources.UL_NAME_ASGARDIAN_RING);
    	RegisterHelper.registerInventoryVariantItemRender(itemAsgardianPearl,0,Ressources.UL_NAME_ASGARDIAN_PEARL);
    	RegisterHelper.registerInventoryVariantItemRender(itemEnergyModule,0,Ressources.UL_NAME_ENERGY_MODULE);
    	RegisterHelper.registerInventoryVariantItemRender(itemFluidModule,0,Ressources.UL_NAME_FLUID_MODULE);
    	RegisterHelper.registerInventoryVariantItemRender(itemFusionCasing,0,Ressources.UL_NAME_FUSION_CASING);
    	RegisterHelper.registerInventoryVariantItemRender(itemGlassCutter,0,Ressources.UL_NAME_GLASS_CUTTER);
    	RegisterHelper.registerInventoryVariantItemRender(itemFusionCore,0,Ressources.UL_NAME_FUSION_CORE);
    	RegisterHelper.registerInventoryVariantItemRender(itemEnergeticConvector,0,Ressources.UL_NAME_ENERGETIC_CONVECTOR);
    	RegisterHelper.registerInventoryVariantItemRender(itemMagnet,0,Ressources.UL_NAME_MAGNET);
    	RegisterHelper.registerInventoryVariantItemRender(itemMightyFeather,0,Ressources.UL_NAME_MIGHTY_FEATHER);
    	RegisterHelper.registerInventoryVariantItemRender(itemMuonBoosterCasing,0,Ressources.UL_NAME_MUON_BOOSTER_CASING);
    	RegisterHelper.registerInventoryVariantItemRender(itemStabilizer,0,Ressources.UL_NAME_STABILIZER);
    	RegisterHelper.registerInventoryVariantItemRender(itemThruster,0,Ressources.UL_NAME_THRUSTER);
    	RegisterHelper.registerInventoryVariantItemRender(itemUberMightyFeather,0,Ressources.UL_NAME_UBBER_MIGHTY_FEATHER);
    	RegisterHelper.registerInventoryVariantItemRender(itemVibraniumDust,0,Ressources.UL_NAME_VIBRANIUM_DUST);
    	RegisterHelper.registerInventoryVariantItemRender(itemVibraniumAlloy,0,Ressources.UL_NAME_VIBRANIUM_ALLOY);
    	RegisterHelper.registerInventoryVariantItemRender(itemVibraniumAlloySheet,0,Ressources.UL_NAME_VIBRANIUM_ALLOY_SHEET);
    	RegisterHelper.registerInventoryVariantItemRender(itemBasicHammer,0,Ressources.UL_NAME_BASIC_HAMMER);
    	RegisterHelper.registerInventoryVariantItemRender(itemManual,0,Ressources.UL_NAME_MANUAL);
    	RegisterHelper.registerInventoryVariantItemRender(itemHulkFlesh,0,Ressources.UL_NAME_HULK_FLESH);
    	RegisterHelper.registerInventoryVariantItemRender(itemMightyHulkRing,0,Ressources.UL_NAME_MIGHTY_HULK_RING);
	}

	public static void preInitItems() {
		
		/* ITEMS */
		
		itemAsgardite = new ItemAsgardite();
		RegisterHelper.registerItem(itemAsgardite);
		itemThorHammer = new ItemThorHammer();
		RegisterHelper.registerItem(itemThorHammer);
		itemAsgardianBronzeIngot = new ItemAsgardianBronzeIngot();
		RegisterHelper.registerItem(itemAsgardianBronzeIngot);
		itemAsgardianSteelIngot = new ItemAsgardianSteelIngot();
		RegisterHelper.registerItem(itemAsgardianSteelIngot);
		itemAsgardianRing = new ItemAsgardianRing();
		RegisterHelper.registerItem(itemAsgardianRing);
		itemAsgardianPearl = new ItemAsgardianPearl();
		RegisterHelper.registerItem(itemAsgardianPearl);
		itemEnergyModule = new ItemEnergyModule();
		RegisterHelper.registerItem(itemEnergyModule);
		itemFluidModule = new ItemFluidModule();
		RegisterHelper.registerItem(itemFluidModule);
		itemFusionCasing = new ItemFusionCasing();
		RegisterHelper.registerItem(itemFusionCasing);
		itemGlassCutter = new ItemGlassCutter();
		RegisterHelper.registerItem(itemGlassCutter);
		itemFusionCore = new ItemFusionCore();
		RegisterHelper.registerItem(itemFusionCore);
		itemEnergeticConvector = new ItemEnergeticConvector();
		RegisterHelper.registerItem(itemEnergeticConvector);
		itemMagnet = new ItemMagnet();
		RegisterHelper.registerItem(itemMagnet);
		itemMightyFeather = new ItemMightyFeather();
		RegisterHelper.registerItem(itemMightyFeather);
		itemMuonBoosterCasing = new ItemMuonBoosterCasing();
		RegisterHelper.registerItem(itemMuonBoosterCasing);
		itemStabilizer = new ItemStabilizer();
		RegisterHelper.registerItem(itemStabilizer);
		itemThruster = new ItemThruster();
		RegisterHelper.registerItem(itemThruster);
		itemUberMightyFeather = new ItemUberMightyFeather();
		RegisterHelper.registerItem(itemUberMightyFeather);
		itemVibraniumDust = new ItemVibraniumDust();
		RegisterHelper.registerItem(itemVibraniumDust);
		itemVibraniumAlloy = new ItemVibraniumAlloy();
		RegisterHelper.registerItem(itemVibraniumAlloy);
		itemVibraniumAlloySheet = new ItemVibraniumAlloySheet();
		RegisterHelper.registerItem(itemVibraniumAlloySheet);
		itemLightningRod = new ItemLightningRod();
		RegisterHelper.registerItem(itemLightningRod);
		itemBasicHammer = new ItemBasicHammer();
		RegisterHelper.registerItem(itemBasicHammer);
		itemManual = new ItemManual();
		RegisterHelper.registerItem(itemManual);
		itemMold = new ItemMold();
		RegisterHelper.registerItem(itemMold);
		itemHulkFlesh = new ItemHulkFlesh();
		RegisterHelper.registerItem(itemHulkFlesh);
		itemCable = new ItemCable();
		RegisterHelper.registerItem(itemCable);
		itemMicroChip = new ItemMicroChip();
		RegisterHelper.registerItem(itemMicroChip);
		itemCircuitBoard = new ItemCircuitBoard();
		RegisterHelper.registerItem(itemCircuitBoard);
		itemSilicon = new ItemSilicon();
		RegisterHelper.registerItem(itemSilicon);
		itemVibraniumShield = new ItemVibraniumShield();
		RegisterHelper.registerItem(itemVibraniumShield);
		
		itemMightyHulkRing = new ItemMightyHulkRing();
		RegisterHelper.registerItem(itemMightyHulkRing);
		
		itemHoverboard = new ItemHoverBoard();
		RegisterHelper.registerItem(itemHoverboard);
		
		/* ITEMS BLOCKS */
		
		itemOreVibranium = new ItemOreVibranium(ModBlocks.blockOreVibranium);
    	RegisterHelper.registerItem(itemOreVibranium);
    	itemHammerStand = new ItemHammerStand(ModBlocks.blockHammerStand);
    	RegisterHelper.registerItem(itemHammerStand);
    	itemInductor = new ItemInductor(ModBlocks.blockInductor);
    	RegisterHelper.registerItem(itemInductor);
    	
    	itemLiquefier = new ItemUsingMaterials(ModBlocks.blockLiquefier, Ressources.UL_NAME_LIQUEFIER);
    	RegisterHelper.registerItem(itemLiquefier);
       	itemInjector = new ItemUsingMaterials(ModBlocks.blockInjector, Ressources.UL_NAME_INJECTOR);
    	RegisterHelper.registerItem(itemInjector);
    	itemLightningRodStand = new ItemUsingMaterials(ModBlocks.blockLightningRodStand, Ressources.UL_NAME_LIGHTNINGROD_STAND);
    	RegisterHelper.registerItem(itemLightningRodStand);
		
	}

	public static void postInitRenderItems() {	
		
	}

}
