package noelflantier.sfartifacts.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModAchievements;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModEntities;
import noelflantier.sfartifacts.common.handlers.ModEvents;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.handlers.ModGUIs;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.handlers.ModNetworkMessages;
import noelflantier.sfartifacts.common.handlers.ModOreDictionary;
import noelflantier.sfartifacts.common.handlers.ModRecipes;
import noelflantier.sfartifacts.common.handlers.ModTileEntities;
import noelflantier.sfartifacts.common.handlers.capabilities.CapabilityPlayerProvider;
import noelflantier.sfartifacts.common.helpers.SoundHelper;
import noelflantier.sfartifacts.common.recipes.handler.HammerUpgradesRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.InjectorRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.LiquefierRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.MightyFoundryRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.MoldRecipesHandler;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;
import noelflantier.sfartifacts.common.recipes.handler.SoundEmitterConfig;
import noelflantier.sfartifacts.common.world.ModWorldGenOre;
import noelflantier.sfartifacts.common.world.village.ComponentPillar;
import noelflantier.sfartifacts.common.world.village.VillagePillarHandler;
import noelflantier.sfartifacts.compatibilities.InterMods;

public class CommonProxy {
	
	static {
		FluidRegistry.enableUniversalBucket(); // Must be called before preInit
	}
	
	public void preInit(FMLPreInitializationEvent event) {
		CapabilityPlayerProvider.register();
		ModConfig.init(event);
		ModEvents.init();
        MinecraftForge.EVENT_BUS.register(ModEvents.INSTANCE);

    	ModBlocks.preInitBlocks();
    	ModItems.preInitItems();
    	ModFluids.preInitFluids();
		SoundHelper.registerSounds();
    	ModTileEntities.preInitTileEntitites();
		ModEntities.preInitEntitites();
    	GameRegistry.registerWorldGenerator(new ModWorldGenOre(), 0);
    	MoldRecipesHandler.getInstance().loadRecipes();
    	ModNetworkMessages.loadMessages();
		ModAchievements.addModAchievements();
		VillagerRegistry.instance().registerVillageCreationHandler(new VillagePillarHandler());
		MapGenStructureIO.registerStructureComponent(ComponentPillar.class, Ressources.MODID+":AsgardianPillarStructure");
	}
	
	public void init(FMLInitializationEvent event) {

    	NetworkRegistry.INSTANCE.registerGuiHandler(SFArtifacts.instance, new ModGUIs());
	}

	public void postinit(FMLPostInitializationEvent event) {
    	InterMods.loadConfig();
    	ModRecipes.loadRecipes();
		ModAchievements.registerAchievementPane();
    	InjectorRecipesHandler.getInstance().loadRecipes();
    	HammerUpgradesRecipesHandler.getInstance().loadRecipes();
    	MoldRecipesHandler.getInstance().loadRecipes();
    	LiquefierRecipesHandler.getInstance().loadRecipes();
    	MightyFoundryRecipesHandler.getInstance().loadRecipes();
    	ModOreDictionary.checkOreDictionary();
    	ModOreDictionary.loadOres();
    	SoundEmitterConfig.getInstance();
    	PillarsConfig.getInstance();
	}

	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}
}
