package noelflantier.sfartifacts.common.recipes.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.Utils;
import noelflantier.sfartifacts.common.recipes.MobsPropertiesForSpawning;

public class SoundEmitterConfig {

	public static Map<String,List<String>> ENTITIES_VARIANTS = new HashMap<String,List<String>>();
	static final SoundEmitterConfig instance = new SoundEmitterConfig();
	private static final String CORE_FILE_NAME = "coreMobConfig.json";
	private static final String OTHER_FILE_NAME = "otherMobConfig.json";

	private static final String KEY_DEFAULT = "default";
	private static final String KEY_MOBS = "mobs";
	private static final String KEY_FREQUENCY = "frequency";
	private static final String KEY_ENERGY_COST = "energyCost";
	private static final String KEY_FLUID_COST = "fluidCost";
	private static final String KEY_CUSTOM_XYZ = "customXYZ";
	private static final String KEY_ATTRACTED = "isAttractedToSpawner";
	private static final String KEY_ONCE_SPAWNING = "isSpawningOnce";
	private static final String KEY_NB_SPAWNING = "nbMaxSpawning";
	  
	public static SoundEmitterConfig getInstance() {
		return instance;
	}

	private Map<String, MobsPropertiesForSpawning> nameToMobsProperties = new HashMap<String, MobsPropertiesForSpawning>();
	private Map<Integer,String> sortedFrequencyToName = new TreeMap<Integer,String>();
	private Map<Integer,String> frequencyToName = new HashMap<Integer,String>();
	private Map<String,Integer> nameToFrequency = new HashMap<String,Integer>();

	private int defaultCustomX = 0;
	private int defaultCustomY = 0;
	private int defaultCustomZ = 0;
	private boolean defaultAttractedToSpawner = true;
	private boolean defaultSpawningOnce = false;
	private int defaultNbMaxSpawning  = 10;
	
	public static final int lowestFrequency = -1000;
	public static final int highestFrequency = 1000;
	private int clampFrequency = 0;
	public Random seedingFrequency = new Random(133769);
	public final static String KEY_SPAWN = "SpawnedBySoundEmitter";

	private SoundEmitterConfig(){
		ENTITIES_VARIANTS.put("Skeleton", new ArrayList<String>(){{add("WitherSkeleton");add("Stray");}});
		ENTITIES_VARIANTS.put("Zombie", new ArrayList<String>(){{add("Husk");}});
		ENTITIES_VARIANTS.put("EntityHorse", new ArrayList<String>(){{add("Donkey");add("Mule");add("ZombieHorse");add("SkeletonHorse");}});
		
		String configText;
		JsonElement root;
		JsonObject rootObj;
		JsonObject defaultObj;
		JsonObject mobsObj;
		try {
			configText = Utils.getFileFromConfig(CORE_FILE_NAME, false);
			root = new JsonParser().parse(configText);
			rootObj = root.getAsJsonObject();
		  
			defaultObj = rootObj.getAsJsonObject(KEY_DEFAULT);
			if(!defaultObj.isJsonNull()){
				String[] separated = defaultObj.get(KEY_CUSTOM_XYZ).getAsString().split("[,]");
				defaultCustomX = Integer.parseInt(separated[0]);
				defaultCustomY = Integer.parseInt(separated[1]);
				defaultCustomZ = Integer.parseInt(separated[2]);
				defaultAttractedToSpawner = defaultObj.get(KEY_ATTRACTED).getAsBoolean();
				defaultSpawningOnce = defaultObj.get(KEY_ONCE_SPAWNING).getAsBoolean();
				defaultNbMaxSpawning = defaultObj.get(KEY_NB_SPAWNING).getAsInt();
			}
			mobsObj = rootObj.getAsJsonObject(KEY_MOBS);
			for (Entry<String, JsonElement> entry : mobsObj.entrySet()) {
				if(entry.getKey()==null || entry.getKey().equals("")){
					System.out.println("Entity name cant be null");
					continue;
				}
				if(!EntityList.isStringValidEntityName(entry.getKey())){
					System.out.println("Entity "+entry.getKey()+" does not exist or is not registered at the right place");
					continue;
				}
				if(nameToMobsProperties.containsKey(entry.getKey())){
					System.out.println("Entity " + entry.getKey() +" found duplicate");
					continue;
				}
				
				processMobsElement(entry.getKey(), entry.getValue().getAsJsonObject());
			  
			}
			//for(Entry<String, MobsPropertiesForSpawning> entry : nameToMobsProperties.entrySet()){
				//System.out.println("............................................ "+entry.getKey()+"   "+entry.getValue().nbMaxSpawning+"    "+entry.getValue().customY);
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			configText = getMobConfig(OTHER_FILE_NAME);
			root = new JsonParser().parse(configText);
			rootObj = root.getAsJsonObject();
			mobsObj = rootObj.getAsJsonObject(KEY_MOBS);
			for (Entry<String, JsonElement> entry : mobsObj.entrySet()) {
				if(entry.getKey()==null || entry.getKey().equals("")){
					System.out.println("Entity name cant be null");
					continue;
				}
				if(!EntityList.isStringValidEntityName(entry.getKey())){
					System.out.println("Entity "+entry.getKey()+" does not exist or is not registered at the right place");
					continue;
				}
				if(nameToMobsProperties.containsKey(entry.getKey())){
					System.out.println("Entity " + entry.getKey() +" found duplicate");
					continue;
				}
				
				processMobsElement(entry.getKey(), entry.getValue().getAsJsonObject());
			  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sortedFrequencyToName.putAll(frequencyToName);
		
	}
	
	private String getMobConfig(String fileName) throws IOException {    
		if(!Utils.getConfigFile(fileName).exists()) {
			return generateFileWithCustomMobs(fileName);
		}            
		return IOUtils.toString(new FileReader(Utils.getConfigFile(fileName)));    
	}
	
	private String generateFileWithCustomMobs(String name)  throws IOException {
		JsonObject rootObj = new JsonObject();
		rootObj.addProperty("howto", "Here are the mobs added by others mods, check "+CORE_FILE_NAME+" to see any properties you want to add.");
		JsonObject mobsObj = new JsonObject();
		Iterator <Entry<String, Class<? extends Entity>>>iterator = EntityList.NAME_TO_CLASS.entrySet().iterator();
        while (iterator.hasNext()){
        	Map.Entry<String,Class<? extends Entity>> entry = iterator.next();
        	if(!nameToMobsProperties.containsKey(entry.getKey()) 
        			&& Utils.getAllSuperclasses(entry.getValue()).contains(EntityLivingBase.class) 
        			&& !Modifier.isAbstract( entry.getValue().getModifiers() )){
        		JsonObject tmp = new JsonObject();
        		tmp.addProperty(KEY_ENERGY_COST, 100);
        		tmp.addProperty(KEY_FLUID_COST, 100);
        		mobsObj.add(entry.getKey(),tmp);
        	}
        }
        rootObj.add(KEY_MOBS, mobsObj);

	    BufferedWriter writer = null;
	    try {
	    	writer = new BufferedWriter(new FileWriter(new File(ModConfig.configDirectory, name), false));
	    	writer.write(rootObj.toString());
	    } finally {
	    	IOUtils.closeQuietly(writer);
	    }
	    
	    return rootObj.toString();
	}

	public void processMobsElement(String name, JsonObject m){
		boolean flag = false;
		MobsPropertiesForSpawning mpfs = new MobsPropertiesForSpawning();
		mpfs.entityName = name;
		mpfs.entityClass = (Class) EntityList.NAME_TO_CLASS.getOrDefault(name, null);
		
		if(m.has(KEY_ENERGY_COST))
			mpfs.energyneeded = m.get(KEY_ENERGY_COST).getAsInt();
		else
			flag = true;
		if(m.has(KEY_FLUID_COST))
			mpfs.fluidneeded.amount = m.get(KEY_FLUID_COST).getAsInt();
		else
			flag = true;
		
		if(m.has(KEY_CUSTOM_XYZ)){
			String[] separated = m.get(KEY_CUSTOM_XYZ).getAsString().split("[,]");
			mpfs.customX = Integer.parseInt(separated[0]);
			mpfs.customY = Integer.parseInt(separated[1]);
			mpfs.customZ = Integer.parseInt(separated[2]);
		}else{
			mpfs.customX = defaultCustomX;
			mpfs.customY = defaultCustomY;
			mpfs.customZ = defaultCustomZ;
		}

		if(m.has(KEY_ATTRACTED))
			mpfs.isAttractedToSpawner = m.get(KEY_ATTRACTED).getAsBoolean();
		else
			mpfs.isAttractedToSpawner = defaultAttractedToSpawner;
		if(m.has(KEY_ONCE_SPAWNING))
			mpfs.isSpawningOnce = m.get(KEY_ONCE_SPAWNING).getAsBoolean();
		else
			mpfs.isSpawningOnce = defaultSpawningOnce;
		if(m.has(KEY_NB_SPAWNING))
			mpfs.nbMaxSpawning = m.get(KEY_NB_SPAWNING).getAsInt();
		else
			mpfs.nbMaxSpawning = defaultNbMaxSpawning;
		if(m.has(KEY_FREQUENCY))
			mpfs.frequency = m.get(KEY_FREQUENCY).getAsInt();
		else{
			mpfs.frequency = setRandomFrequencyForId();
		}
		
		if(!flag){
			nameToMobsProperties.put(name, mpfs);
			frequencyToName.put(mpfs.frequency, name);
			nameToFrequency.put(name, mpfs.frequency);
		}
	}
	
	public int setRandomFrequencyForId(){
		Integer r = seedingFrequency.nextInt(lowestFrequency*-1+highestFrequency)-((lowestFrequency*-1+highestFrequency)/2);
		if(frequencyToName!=null && frequencyToName.size()>0 && frequencyToName.containsKey(r))
			return setRandomFrequencyForId();
		return r;
	}
	
	public Map<String, MobsPropertiesForSpawning> getNameToMobsProperties() {
		return nameToMobsProperties;
	}
	public Map<Integer, String> getSortedFrequencyToName() {
		return sortedFrequencyToName;
	}
	public Map<Integer, String> getFrequencyToName() {
		return frequencyToName;
	}
	public Map<String, Integer> getNameToFrequency() {
		return nameToFrequency;
	}
	
	public String getNameForFrequency(int frequency){
		return getFrequencyToName().containsKey(frequency)?getFrequencyToName().get(frequency):null;
	}
	public String getRealEntityName(int frequency){
		String n = getNameForFrequency(frequency);
		return n!=null?getNameToMobsProperties().containsKey(n)?getNameToMobsProperties().get(n).entityName:null:null;
	}
	public String getRealEntityName(String name){
		return getNameToMobsProperties().containsKey(name)?getNameToMobsProperties().get(name).entityName:"null";
	}
	public int getRfForName(String name){
		return getNameToMobsProperties().containsKey(name)?getNameToMobsProperties().get(name).energyneeded:0;
	}
	public int getFlForName(String name){
		return getNameToMobsProperties().containsKey(name)?getNameToMobsProperties().get(name).fluidneeded.amount:0;
	}
	public int getRfForFrequency(int freq) {
		String n = getNameForFrequency(freq);
		return n!=null?getNameToMobsProperties().get(n).energyneeded:0;
	}
	public int getFlForFrequency(int freq) {
		String n = getNameForFrequency(freq);
		return n!=null?getNameToMobsProperties().get(n).fluidneeded.amount:0;
	}

	public MobsPropertiesForSpawning getMobsProperties(String name, int energyStored, int fluidAmount) {
		return getNameToMobsProperties().containsKey(name)?getNameToMobsProperties().get(name).energyneeded<=energyStored && getNameToMobsProperties().get(name).fluidneeded.amount<=fluidAmount?getNameToMobsProperties().get(name):null:null;
	}
}
