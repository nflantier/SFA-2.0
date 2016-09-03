package noelflantier.sfartifacts.common.handlers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;

public class ModRecipes {
	
	public static void loadRecipes() {
		loadOreDictionnaryRecipe();
		
		//GENERAL
		
		//THOR
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockAsgardianBronze,1,0),  "BBB", "BBB", "BBB", 'B', ModItems.itemAsgardianBronzeIngot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockAsgardianSteel,1,0),  "SSS", "SSS", "SSS", 'S', ModItems.itemAsgardianSteelIngot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockAsgardite,1,0),  "AAA", "AAA", "AAA", 'A', ModItems.itemAsgardite));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemAsgardite,9,0), Item.getItemFromBlock(ModBlocks.blockAsgardite)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemFluidModule,1,0),  "GGG", "GTG", "GPG", 'P', Item.getItemFromBlock(Blocks.PISTON), 'G', Item.getItemFromBlock(Blocks.GLASS), 'T', Items.CAULDRON));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemEnergyModule,1,0),  "BBB", "BRB", "BDB", 'A', ModItems.itemAsgardite, 'B', ModItems.itemAsgardite, 'R', Item.getItemFromBlock(Blocks.REDSTONE_BLOCK), 'D', Items.DIAMOND));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemMagnet,1,0),  "B B", "S S", "SDS", 'S',ModItems.itemAsgardianSteelIngot,'B',Items.IRON_INGOT,'D',ModItems.itemEnergyModule));
		
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockControlPanel,1,0), ModItems.itemEnergyModule, Item.getItemFromBlock(Blocks.REDSTONE_TORCH), Items.REPEATER));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemBasicHammer,1,0),  "BBB", "BBB", " S ", 'B', Item.getItemFromBlock(ModBlocks.blockAsgardite), 'S', Items.STICK));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockHammerStand,1,0),  "III", "BSB", "SSS", 'B', Item.getItemFromBlock(ModBlocks.blockAsgardianBronze), 'S', Item.getItemFromBlock(ModBlocks.blockAsgardianSteel), 'I', ModItems.itemAsgardianSteelIngot));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemVibraniumShield,1,1),  "RRR", "BTB", "RRR", 'B', new ItemStack(Items.DYE,1,4), 'R', new ItemStack(Items.DYE,1,1), 'T', new ItemStack(ModItems.itemVibraniumShield,1,0)));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemLightningRod,1,0),  "D", "S", "S", 'D', Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 'S', Item.getItemFromBlock(ModBlocks.blockAsgardianSteel)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemLightningRod,1,1),  " D ", " S ", "SSS", 'D', Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 'S', Item.getItemFromBlock(ModBlocks.blockAsgardianSteel)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemLightningRod,1,2),  " D ", "SSS", "SSS", 'D', Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 'S', Item.getItemFromBlock(ModBlocks.blockAsgardianSteel)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemLightningRod,1,3),  " DS", "SSS", "SSS", 'D', Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 'S', Item.getItemFromBlock(ModBlocks.blockAsgardianSteel)));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemManual,1,0), ModItems.itemAsgardite, Items.BOOK));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemUberMightyFeather,1,0),  "FFF", "FMF", "FFF", 'F', ModItems.itemMightyFeather, 'M', ModItems.itemEnergyModule));
		
    	for(EnumPillarMaterial pm :EnumPillarMaterial.values()){
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockLiquefier,1,pm.ordinal()),  "MPM", "TET", "MPM", 'E', ModItems.itemEnergyModule, 'M',pm.item , 'T',ModItems.itemFluidModule, 'P', Item.getItemFromBlock(Blocks.PISTON)));
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockInjector,1,pm.ordinal()),  "MTM", "EEE", "MPM", 'E', ModItems.itemEnergyModule, 'M',pm.item , 'T',ModItems.itemFluidModule, 'P',Item.getItemFromBlock(Blocks.PISTON)));
    		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockLightningRodStand,1,pm.ordinal()),  "M M", "M M", "MEM", 'E', ModItems.itemEnergyModule, 'M',pm.item ));
    	}
    	
    	
    	//CAPTAINAMERICA
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMightyFoundry,1,0),  "APA", "ATA", "AEA", 'E', ModItems.itemEnergyModule,'T',ModItems.itemFluidModule, 'A',Item.getItemFromBlock(ModBlocks.blockAsgardianSteel), 'P',Item.getItemFromBlock(Blocks.PISTON)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemVibraniumAlloySheet,1,0),  "AAA", "AAA", "AAA", 'A', ModItems.itemVibraniumAlloy));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemMold,1,0), Items.PAPER, new ItemStack(Blocks.SAND,1,0), new ItemStack(Blocks.SAND,1,0), new ItemStack(Blocks.SAND,1,0), new ItemStack(Blocks.SAND,1,0), new ItemStack(Blocks.SAND,1,0), new ItemStack(Blocks.SAND,1,0), new ItemStack(Blocks.SAND,1,0), new ItemStack(Blocks.SAND,1,0)));
		
		
		//HULK
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockSoundEmiter,1,0),  "GGG", "BMB", "BEB", 'G', Item.getItemFromBlock(ModBlocks.blockAsgardianGlass),'M',ModItems.itemMagnet,'B',Item.getItemFromBlock(ModBlocks.blockAsgardianSteel), 'E',ModItems.itemEnergyModule));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemAsgardianRing,1,0),  " S ", "S S", " S ", 'S', ModItems.itemAsgardianSteelIngot));
		
		
		//BACKTOTHEFUTURE
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMrFusion,1,0),  "III", "ICI", "OOO", 'I', Items.IRON_INGOT,'C',ModItems.itemFusionCore, 'O',Item.getItemFromBlock(Blocks.OBSIDIAN), 'E', ModItems.itemEnergyModule));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemFusionCore,1,0),  "PIP", "CNC", "PIP", 'I', Items.IRON_INGOT,'C', ModItems.itemFusionCasing, 'N', Items.NETHER_STAR, 'I', ModItems.itemEnergyModule, 'P', new ItemStack(ModItems.itemCircuitBoard,1,2)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCable,8,0),  "RRR", "III", "RRR", 'I', Items.IRON_INGOT,'R',Items.REDSTONE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCable,8,1),  "RRR", "III", "RRR", 'I', Items.GOLD_INGOT,'R',Items.REDSTONE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCable,8,2),  "RRR", "III", "RRR", 'I', Items.IRON_INGOT,'R',ModItems.itemAsgardite));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCable,8,3),  "RRR", "III", "RRR", 'I', Items.GOLD_INGOT,'R',ModItems.itemAsgardite));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockInductor,2,0),new ItemStack(ModItems.itemCable,1,0),new ItemStack(ModItems.itemCable,1,0),new ItemStack(ModItems.itemCable,1,0),new ItemStack(ModItems.itemCable,1,0), new ItemStack(ModItems.itemMagnet,1,0)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockInductor,2,1),new ItemStack(ModItems.itemCable,1,1),new ItemStack(ModItems.itemCable,1,1),new ItemStack(ModItems.itemCable,1,1),new ItemStack(ModItems.itemCable,1,1), new ItemStack(ModItems.itemMagnet,1,0)));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemGlassCutter,1,0),  "DI ", "II ", "  I", 'I', Items.IRON_INGOT,'D',Items.DIAMOND));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemSilicon,3,0),new ItemStack(Items.WATER_BUCKET,1,0),new ItemStack(Items.QUARTZ,1,0),new ItemStack(Items.QUARTZ,1,0),new ItemStack(Blocks.SAND,1,0),new ItemStack(Blocks.SAND,1,0),new ItemStack(Blocks.SAND,1,0),new ItemStack(Blocks.SAND,1,0),new ItemStack(Blocks.SAND,1,0), new ItemStack(Blocks.SAND,1,0)));
		GameRegistry.addSmelting(new ItemStack(ModItems.itemSilicon,1,0), new ItemStack(ModItems.itemSilicon,1,1), 0);
		GameRegistry.addSmelting(new ItemStack(ModItems.itemSilicon,1,1), new ItemStack(ModItems.itemSilicon,1,2), 0);
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemMicroChip,2,0),new ItemStack(ModItems.itemSilicon,1,2),new ItemStack(ModItems.itemGlassCutter,1,0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemMicroChip,1,1),  "RQR", "RCR", "RRR", 'Q', Items.QUARTZ,'C',new ItemStack(ModItems.itemMicroChip,1,0), 'R', Items.REDSTONE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemMicroChip,1,2),  "QCQ", "QRQ", "QCQ", 'Q', Items.REDSTONE,'C',new ItemStack(ModItems.itemMicroChip,1,1), 'R', Items.DIAMOND));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemMicroChip,1,3),  "CQC", "QRQ", "CQC", 'Q', Items.DIAMOND,'C',new ItemStack(ModItems.itemMicroChip,1,1), 'R', Items.REDSTONE));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCircuitBoard,1,0),  "RRR", "CPC", "RRR",'P',new ItemStack(ModItems.itemMicroChip,1,1) , 'C', new ItemStack(ModItems.itemCable,1,0),'R',Items.REDSTONE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCircuitBoard,1,1),  "RRR", "CPC", "RRR",'P',new ItemStack(ModItems.itemMicroChip,1,2) , 'C', new ItemStack(ModItems.itemCable,1,1),'R',Items.REDSTONE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCircuitBoard,1,2),  "CCC", "RPR", "CCC",'P',new ItemStack(ModItems.itemMicroChip,1,3) , 'C', new ItemStack(ModItems.itemCable,1,1),'R',Items.REDSTONE));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemStabilizer,1,0),  "CCC", "RPR", "CCC",'P',ModItems.itemMagnet , 'C', Items.IRON_INGOT,'R', new ItemStack(ModItems.itemCircuitBoard,1,1)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemHoverboard,1,0),  "PSP", "QEQ", "PSP",'P', Items.EMERALD , 'S', ModItems.itemStabilizer,'Q',new ItemStack(ModItems.itemCircuitBoard,1,2),'E',ModItems.itemEnergeticConvector));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemHoverboard,1,2),  "PSP", "QEQ", "CSC",'C', ModItems.itemThruster,'P', Items.EMERALD , 'S', ModItems.itemStabilizer,'Q',new ItemStack(ModItems.itemCircuitBoard,1,2),'E',ModItems.itemEnergeticConvector));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemThruster,1,0),  "VVV", "VFV", "VCV",'C', Items.FIRE_CHARGE,'F', ModItems.itemEnergeticConvector , 'V', ModItems.itemVibraniumAlloySheet));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemEnergeticConvector,1,0),  "DDD", "ECE", "OSO",'C', ModItems.itemMuonBoosterCasing,'D', Items.DIAMOND , 'E', ModItems.itemEnergyModule, 'O',new ItemStack(ModItems.itemCircuitBoard,1,0),'S',ModItems.itemMagnet ));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockRecharger,1,0),  " I ", "RER", "RCR",'C', new ItemStack(ModItems.itemCircuitBoard,1,0),'R', Items.IRON_INGOT , 'E', ModItems.itemEnergyModule, 'I',new ItemStack(ModBlocks.blockInductor,1,1)));
		
	}
	
	public static void loadOreDictionnaryRecipe(){

	}

}
