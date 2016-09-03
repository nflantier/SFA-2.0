package noelflantier.sfartifacts.common.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianBronze;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianGlass;
import noelflantier.sfartifacts.common.blocks.BlockAsgardianSteel;
import noelflantier.sfartifacts.common.blocks.BlockAsgardite;
import noelflantier.sfartifacts.common.blocks.BlockControlPanel;
import noelflantier.sfartifacts.common.blocks.BlockHammerStand;
import noelflantier.sfartifacts.common.blocks.BlockInductor;
import noelflantier.sfartifacts.common.blocks.BlockInjector;
import noelflantier.sfartifacts.common.blocks.BlockLightningRodStand;
import noelflantier.sfartifacts.common.blocks.BlockLiquefier;
import noelflantier.sfartifacts.common.blocks.BlockMightyFoundry;
import noelflantier.sfartifacts.common.blocks.BlockMrFusion;
import noelflantier.sfartifacts.common.blocks.BlockOreAsgardite;
import noelflantier.sfartifacts.common.blocks.BlockOreVibranium;
import noelflantier.sfartifacts.common.blocks.BlockRecharger;
import noelflantier.sfartifacts.common.blocks.BlockSoundEmiter;
import noelflantier.sfartifacts.common.helpers.RegisterHelper;

public class ModBlocks {

	public static Block blockOreVibranium;
	public static Block blockOreAsgardite;
	public static Block blockAsgardite;
	public static Block blockAsgardianBronze;
	public static Block blockAsgardianSteel;
	public static Block blockAsgardianGlass;
	public static Block blockInjector;
	public static Block blockControlPanel;
	public static Block blockHammerStand;
	public static Block blockSoundEmiter;
	public static Block blockMrFusion;
	
	public static Block blockLiquefier;
	public static Block blockLightningRodStand;
	public static Block blockMightyFoundry;
	public static Block blockRecharger;
	
	public static Block blockInductor;
	
	public static Block bakedModelBlock;
	
	public static void preInitBlocks() {
		
		/* ORES */
		
		blockOreAsgardite = new BlockOreAsgardite(Material.ROCK);
    	RegisterHelper.registerBlock(blockOreAsgardite);
    	
    	blockOreVibranium = new BlockOreVibranium(Material.ROCK);
    	RegisterHelper.registerBlock(blockOreVibranium);
    	
        /*bakedModelBlock = new BakedModelBlock();
    	RegisterHelper.registerBlock(bakedModelBlock);*/
    	
    	/* MATERIAL */
    	
    	blockAsgardite = new BlockAsgardite(Material.ROCK);
    	RegisterHelper.registerBlock(blockAsgardite);
    	
    	blockAsgardianBronze = new BlockAsgardianBronze(Material.ROCK);
    	RegisterHelper.registerBlock(blockAsgardianBronze);
    	
    	blockAsgardianSteel = new BlockAsgardianSteel(Material.ROCK);
    	RegisterHelper.registerBlock(blockAsgardianSteel);

    	/* GENERAL */
    	
    	blockAsgardianGlass = new BlockAsgardianGlass(Material.GLASS);
    	RegisterHelper.registerBlock(blockAsgardianGlass);

    	blockHammerStand = new BlockHammerStand(Material.ANVIL);
    	RegisterHelper.registerBlock(blockHammerStand);
    	
    	blockControlPanel = new BlockControlPanel(Material.ROCK);
    	RegisterHelper.registerBlock(blockControlPanel);
    	
    	/* MACHINE */
    	
    	blockInductor = new BlockInductor(Material.ROCK);
    	RegisterHelper.registerBlock(blockInductor);
    	
    	blockInjector = new BlockInjector(Material.ROCK);
    	RegisterHelper.registerBlock(blockInjector);
    	
    	blockLightningRodStand = new BlockLightningRodStand(Material.ROCK);
    	RegisterHelper.registerBlock(blockLightningRodStand);

    	blockLiquefier = new BlockLiquefier(Material.ROCK);
    	RegisterHelper.registerBlock(blockLiquefier);
    	
    	blockMightyFoundry = new BlockMightyFoundry(Material.ROCK);
    	RegisterHelper.registerBlock(blockMightyFoundry);
    	
    	blockSoundEmiter = new BlockSoundEmiter(Material.ROCK);
    	RegisterHelper.registerBlock(blockSoundEmiter);
    	
    	blockRecharger = new BlockRecharger(Material.ROCK);
    	RegisterHelper.registerBlock(blockRecharger);
    	
    	blockMrFusion = new BlockMrFusion(Material.ROCK);
    	RegisterHelper.registerBlock(blockMrFusion);
    	
    	
	}
	
    @SideOnly(Side.CLIENT)
	public static void preInitRenderBlocks() {
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockInjector), 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_INJECTOR, "item_a"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockInjector), 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_INJECTOR, "item_s"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockInjector), 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_INJECTOR, "item_b"));

    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockLightningRodStand), 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIGHTNINGROD_STAND, "item_a"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockLightningRodStand), 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIGHTNINGROD_STAND, "item_s"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockLightningRodStand), 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIGHTNINGROD_STAND, "item_b"));
    	
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockLiquefier), 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIQUEFIER, "item_a"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockLiquefier), 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIQUEFIER, "item_s"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockLiquefier), 2, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIQUEFIER, "item_b"));
    	
    	//ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockHammerStand), 0, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_HAMMER_STAND, "broken=false"));
    	//ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockHammerStand), 1, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_HAMMER_STAND, "broken=true"));
    	
    	ModFluids.preInitRenderFluids();
    }

    
    @SideOnly(Side.CLIENT)
	public static void initRenderBlocks() {
		//RegisterHelper.registerInventoryVariantBlockRender(bakedModelBlock,0,"bakedmodelblock");

    	RegisterHelper.registerInventoryVariantBlockRender(blockOreAsgardite,0,Ressources.UL_NAME_ORE_ASGARDITE);
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,0,Ressources.UL_NAME_ORE_VIBRANIUM,"status=0");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,1,Ressources.UL_NAME_ORE_VIBRANIUM,"status=6");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,2,Ressources.UL_NAME_ORE_VIBRANIUM,"status=12");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,3,Ressources.UL_NAME_ORE_VIBRANIUM,"status=18");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,4,Ressources.UL_NAME_ORE_VIBRANIUM,"status=24");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,5,Ressources.UL_NAME_ORE_VIBRANIUM,"status=30");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,6,Ressources.UL_NAME_ORE_VIBRANIUM,"status=36");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,7,Ressources.UL_NAME_ORE_VIBRANIUM,"status=42");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,8,Ressources.UL_NAME_ORE_VIBRANIUM,"status=56");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,9,Ressources.UL_NAME_ORE_VIBRANIUM,"status=62");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,10,Ressources.UL_NAME_ORE_VIBRANIUM,"status=68");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,11,Ressources.UL_NAME_ORE_VIBRANIUM,"status=74");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,12,Ressources.UL_NAME_ORE_VIBRANIUM,"status=80");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,13,Ressources.UL_NAME_ORE_VIBRANIUM,"status=86");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,14,Ressources.UL_NAME_ORE_VIBRANIUM,"status=92");
    	RegisterHelper.registerVariantBlockRender(blockOreVibranium,15,Ressources.UL_NAME_ORE_VIBRANIUM,"status=100");

    	RegisterHelper.registerInventoryVariantBlockRender(blockAsgardite,0,Ressources.UL_NAME_ASGARDITE_BLOCK);
    	RegisterHelper.registerInventoryVariantBlockRender(blockAsgardianBronze,0,Ressources.UL_NAME_ASGARDIAN_BRONZE);
    	RegisterHelper.registerInventoryVariantBlockRender(blockAsgardianSteel,0,Ressources.UL_NAME_ASGARDIAN_STEEL);
    	RegisterHelper.registerInventoryVariantBlockRender(blockAsgardianGlass,0,Ressources.UL_NAME_ASGARDIAN_GLASS);
    	
    	RegisterHelper.registerVariantBlockRender(blockHammerStand,0,Ressources.UL_NAME_HAMMER_STAND,"broken=false,facing=north");
    	RegisterHelper.registerVariantBlockRender(blockHammerStand,1,Ressources.UL_NAME_HAMMER_STAND,"broken=true,facing=north");

    	RegisterHelper.registerInventoryVariantBlockRender(blockControlPanel,0,Ressources.UL_NAME_CONTROL_PANEL);
    	RegisterHelper.registerInventoryVariantBlockRender(blockMightyFoundry,0,Ressources.UL_NAME_MIGHTYFOUNDRY);
    	RegisterHelper.registerInventoryVariantBlockRender(blockSoundEmiter,0,Ressources.UL_NAME_SOUND_EMITTER);
    	RegisterHelper.registerInventoryVariantBlockRender(blockRecharger,0,Ressources.UL_NAME_RECHARGER);
    	RegisterHelper.registerInventoryVariantBlockRender(blockMrFusion,0,Ressources.UL_NAME_MR_FUSION);

    	RegisterHelper.registerVariantBlockRender(blockInductor,0,Ressources.UL_NAME_INDUCTOR, "all_facing=up,type_tech=basic");	
    	RegisterHelper.registerVariantBlockRender(blockInductor,1,Ressources.UL_NAME_INDUCTOR, "all_facing=up,type_tech=advanced");	
    	RegisterHelper.registerVariantBlockRender(blockInductor,2,Ressources.UL_NAME_INDUCTOR, "all_facing=up,type_tech=basicenergized");	
    	RegisterHelper.registerVariantBlockRender(blockInductor,3,Ressources.UL_NAME_INDUCTOR, "all_facing=up,type_tech=advancedenergized");    	
	}

}
