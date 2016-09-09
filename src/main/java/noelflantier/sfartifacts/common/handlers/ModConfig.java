package noelflantier.sfartifacts.common.handlers;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import noelflantier.sfartifacts.Ressources;;

public class ModConfig {
	

	public static boolean useOldRegistration;

	public static boolean useTESR;
	public static boolean isAMachinesWorksOnlyWithPillar;
	public static int capacityLiquefier;
	public static int capacityWaterLiquefier;
	public static int capacityAsgarditeLiquefier;
	public static int capacityInjector;
	public static int capacityAsgarditeInjector;
	public static int capacityMightyFoundry;
	public static int capacityLavaMightyFoundry;
	public static int capacitySoundEmiter;
	public static int capacityLightningRodStand;
	public static int capacityAsgarditeSoundEmitter;
	public static int capacityMrFusion;
	public static int capacityLiquidMrFusion;
	public static int transfertCapacityInductorBasic;
	public static int transfertCapacityInductorAdvanced;
	public static int transfertCapacityInductorBasicEnergized;
	public static int transfertCapacityInductorAdvancedEnergized;
	public static int pureSiliconLifeSpan;
	public static int rfNeededThorHammer;
	public static int rfMining;
	public static int rfLightning;
	public static int rfMoving;
	public static int rfEntityHit;
	public static int rfThorhammer;
	public static int rfTeleporting;
	public static int rfAddedPerCapacityUpgrade;
	public static int distanceLightning;
	public static int shieldProtection;
	public static boolean isManualSpawning;
	public static boolean isOresEmitParticles;
	public static double chanceToSpawnMightyFeather;
	public static boolean isShieldBlockOnlyWhenShift;
	public static boolean isAsgarditeOreSpawnEnable;
	public static boolean isVibraniumOreSpawnEnable;
	public static int tickToCookVibraniumOres;
	public static int minYVibranium;
	public static int maxYVibranium;
	public static int minVainSizeVibranium;
	public static int maxVainSizeVibranium;
	public static int chanceVibranium;
	public static int minYAsgardite;
	public static int maxYAsgardite;
	public static int minVainSizeAsgardite;
	public static int maxVainSizeAsgardite;
	public static int chanceAsgardite;
	public static int capacityHoverBoardMarty;
	public static int rfPerSecondHoverBoardMarty;
	public static int capacityHoverBoardBiff;
	public static int rfPerSecondHoverBoardBiff;
	public static int rfAddedPerCapacityUpgradeOnHoverboards;
	public static boolean areFrequenciesShown;
	public static int maxAmountPillarCanExtract;
	public static int rangeOfRecharger;
	public static int transferRecharger;
	public static int oneEuToRf;

	public final static String CAT_UTILS = "Utils";
	public final static String CAT_ORES = "Ores";
	public final static String CAT_THOR_HAMMER = "Thor s Hammer";
	public final static String CAT_HOVERBOARD = "HoverBoard";
	public final static String CAT_SOUND_EMITTER = "Sound Emiter";
	public final static String CAT_VIBRANIUM_SHIELD = "Vibranium Shield";
	public final static String CAT_MACHINES = "Machines";
	
	public static Configuration config;
	public static File configDirectory;

	
	public static void init(FMLPreInitializationEvent event) {
		if( configDirectory == null){
			configDirectory = new File(event.getModConfigurationDirectory(), Ressources.MODID.toLowerCase());
		    if(!configDirectory.exists()) {
		    	configDirectory.mkdir();
		    }
		}
		if (config == null) {
			config = new Configuration(new File(configDirectory, Ressources.NAME+".cfg"));
			syncConfig();
		}
	}
	
	public static void syncConfig() {
		try {
			
			distanceLightning = config.get(CAT_THOR_HAMMER, "block/distance", 60, "The distance in block at which you can throw lightnings").getInt();
			rfNeededThorHammer = config.get(CAT_THOR_HAMMER, "rf/invok", 1000000, "The amount of RF energy needed to invok thor's hammer").getInt();
			rfMining = config.get(CAT_THOR_HAMMER, "rf/block mined", 50, "The amount of RF used by thor's hammer for each block mined").getInt();
			rfTeleporting = config.get(CAT_THOR_HAMMER, "rf/teleport", 500, "The amount of RF used by thor's hammer when you use the teleport mod").getInt();
			rfLightning = config.get(CAT_THOR_HAMMER, "rf/lightning", 50, "The amount of RF used by thor's hammer when you throw a lightning").getInt();
			rfMoving = config.get(CAT_THOR_HAMMER, "rf/throw", 150, "The amount of RF used by thor's hammer when you are using the moving mod").getInt();
			rfEntityHit = config.get(CAT_THOR_HAMMER, "rf/entity hit", 20, "The amount of RF used by thor's hammer when you hit an entity").getInt();
			rfThorhammer = config.get(CAT_THOR_HAMMER, "capacity", 100000, "Thor's Hammer RF capacity").getInt();
			rfAddedPerCapacityUpgrade = config.get(CAT_THOR_HAMMER, "capacity added/capacity upgrade", 10000, "The amount of RF capacity upgrade add to the hammer").getInt();
			
			shieldProtection = config.get(CAT_VIBRANIUM_SHIELD, "angle", 90, "The Angle in degrees your shield deflect damages around you ex:90 = 45 to your left 45 to your right").getInt();
			isShieldBlockOnlyWhenShift = config.get(CAT_VIBRANIUM_SHIELD, "shift", true, "Is the shield block damages only when you shift with it").getBoolean();
			
			areFrequenciesShown = config.get(CAT_SOUND_EMITTER, "show frequencies", true, "If set to true you'll have all the frequencies for each mobs shown in your list").getBoolean();
			useTESR = config.get(Configuration.CATEGORY_GENERAL, "tile entity renderer", true, "Is the tile entities of machines (injector, liquefier ...) use a special renderer to render fluids or items inside their block ( setting this to false will probably maximize your fps)").getBoolean();
			isOresEmitParticles = config.get(Configuration.CATEGORY_GENERAL, "ores particles", true, "Is ores emit particles").getBoolean();
			
			isManualSpawning = config.get(Configuration.CATEGORY_GENERAL, "manual spawn", true, "Is the manual spawn at player on new log in").getBoolean();
			chanceToSpawnMightyFeather = config.get(Configuration.CATEGORY_GENERAL, "chance to drop mighty feather", 0.25, "Chance that chickens hit by lightning drop mighty feather [0-1]").getDouble();
			
			maxAmountPillarCanExtract = config.get(Configuration.CATEGORY_GENERAL, "max MB", 5000, "Maximun amount of MB pillar can consume to produce energy").getInt();
			
			tickToCookVibraniumOres = config.get(CAT_ORES, "ticks", 6000, "The number of ticks needed to vibranium ores to be cooked 20 tick = 1 s").getInt();
			isAsgarditeOreSpawnEnable = config.get(CAT_ORES, "asgardite ore spawn enable", true, "Is asgardite ore spawn enable").getBoolean();
			isVibraniumOreSpawnEnable = config.get(CAT_ORES, "vibranium ore spawn enable", true, "Is vibranium ore spawn enable").getBoolean();
			minYVibranium = config.get(CAT_ORES, "min Y level for vibranium", 3, "The min Y level").getInt();
			maxYVibranium = config.get(CAT_ORES, "max Y level for vibranium", 18, "The max Y level").getInt();
			minVainSizeVibranium = config.get(CAT_ORES, "min vain size for vibranium", 2, "The min vain size").getInt();
			maxVainSizeVibranium = config.get(CAT_ORES, "max vain size for vibranium", 4, "The max vain size").getInt();
			chanceVibranium = config.get(CAT_ORES, "chance to spawn vibranium", 6, "The chance to spawn").getInt();
			minYAsgardite = config.get(CAT_ORES, "min Y level for asgardite", 40, "The min Y level").getInt();
			maxYAsgardite = config.get(CAT_ORES, "max Y level for asgardite", 120, "The max Y level").getInt();
			minVainSizeAsgardite = config.get(CAT_ORES, "min vain size for asgardite", 5, "The min vain size").getInt();
			maxVainSizeAsgardite = config.get(CAT_ORES, "max vain size for asgardite", 12, "The max vain size").getInt();
			chanceAsgardite = config.get(CAT_ORES, "chance to spawn asgardite", 10, "The chance to spawn").getInt();
			
			isAMachinesWorksOnlyWithPillar = config.get(CAT_MACHINES, "machines accept Rf only from pillars", false, "True or false if the machines should accept RF energy only from pillars").getBoolean();
			
			capacityLiquefier = config.get(CAT_MACHINES, "liquefier rf capacity", 20000, "The liquefier energy capacity").getInt();
			capacityWaterLiquefier = config.get(CAT_MACHINES, "liquefier water capacity", 20000, "The liquefier water capacity").getInt();
			capacityAsgarditeLiquefier = config.get(CAT_MACHINES, "liquefier liquefied asgardite capacity", 20000, "The liquefier liquefied asgardite capacity").getInt();
			
			capacityInjector = config.get(CAT_MACHINES, "injector rf capacity", 20000, "The injector energy capacity").getInt();
			capacityAsgarditeInjector = config.get(CAT_MACHINES, "injector liquefied asgardite capacity", 20000, "The injector liquefied asgardite capacity").getInt();
			
			capacityMightyFoundry = config.get(CAT_MACHINES, "mighty foundry rf capacity", 50000, "The mightyfoundry energy capacity").getInt();
			capacityLavaMightyFoundry = config.get(CAT_MACHINES, "mightyFoundry lava capacity", 20000, "The mightyfoundry lava capacity").getInt();
			
			capacitySoundEmiter = config.get(CAT_MACHINES, "soundEmitter rf capacity", 500000, "The soundemitter energy capacity").getInt();
			capacityAsgarditeSoundEmitter = config.get(CAT_MACHINES, "soundEmitter liquefied asgardite capacity", 200000, "The soundemitter liquefied asgardite capacity").getInt();

			capacityMrFusion = config.get(CAT_MACHINES, "mr fusion rf capacity", 1210000000, "Mr fudion energy capacity").getInt();
			capacityLiquidMrFusion = config.get(CAT_MACHINES, "mr fusion liquid capacity", 1000000, "Mr fudion liquid capacity").getInt();
			
			capacityLightningRodStand = config.get(CAT_MACHINES, "lightningrod stand rf capacity", 100000, "Lightningrod stand energy capacity").getInt();

			transfertCapacityInductorBasic = config.get(Configuration.CATEGORY_GENERAL, "basic inductor transfert RF/T", 300, "Transfert rate RF/T").getInt();
			transfertCapacityInductorAdvanced = config.get(Configuration.CATEGORY_GENERAL, "advanced inductor transfert RF/T", 2000, "Transfert rate RF/T").getInt();
			transfertCapacityInductorBasicEnergized = config.get(Configuration.CATEGORY_GENERAL, "basic energized inductor transfert RF/T", 10000, "Transfert rate RF/T").getInt();
			transfertCapacityInductorAdvancedEnergized = config.get(Configuration.CATEGORY_GENERAL, "advanced energized inductor transfert RF/T", 20000, "Transfert rate RF/T").getInt();
			
			pureSiliconLifeSpan = config.get(Configuration.CATEGORY_GENERAL, "lifespan silicon", 150, "Lifespan of pure silicon before it decay in ticks 20 tick = 1 s").getInt();
			
			capacityHoverBoardMarty = config.get(CAT_HOVERBOARD, "mattel hoverboard rf capacity", 30000, "Mattel hoverBoard energy capacity").getInt();
			rfPerSecondHoverBoardMarty = config.get(CAT_HOVERBOARD, "mattel rf/second hoverboard", 10, "The amount of rf used by the mattel overboard each second you are using it").getInt();
			capacityHoverBoardBiff= config.get(CAT_HOVERBOARD, "pitbull hoverboard rf capacity", 60000, "PitBull hoverBoard energy capacity").getInt();
			rfPerSecondHoverBoardBiff = config.get(CAT_HOVERBOARD, "pitbull rf/second hoverboard", 20, "The amount of rf used by the pitbull overboard each second you are using it").getInt();
			rfAddedPerCapacityUpgradeOnHoverboards = config.get(CAT_HOVERBOARD, "capacity added on hoveboard/capacity upgrade", 10000, "The amount of RF capacity upgrade add to the hoveboard").getInt();
			
			useOldRegistration = config.get(CAT_UTILS, "Use old registration", false, "In the newer version registered name have changed so if you have an id error set this to true").getBoolean();
			oneEuToRf = config.get(CAT_UTILS, "Eu conversion", 4, "The amount of RF that equal 1 EU. 4 by default, so 4RF == 1EU").getInt();
			
			rangeOfRecharger = config.get(Configuration.CATEGORY_GENERAL, "block range recharger", 10, "The range in block where the recharger can recharge energy items in the player, armor and bauble inventory").getInt();
			transferRecharger= config.get(Configuration.CATEGORY_GENERAL, "transfer rate", 20000, "The transfer in rf / tick that the recharger recharge energy items").getInt();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (config.hasChanged()) config.save();
		}
	}
}
