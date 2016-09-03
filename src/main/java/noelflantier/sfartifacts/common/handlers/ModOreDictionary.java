package noelflantier.sfartifacts.common.handlers;

import net.minecraftforge.oredict.OreDictionary;

public class ModOreDictionary {

	public static String copperIngotName = "ingotCopper";
	public static boolean hasCopperIngot = false;
	
	public static boolean isRegistered(String name) {
		if(!OreDictionary.getOres(name).isEmpty()) {
			return true;
		}
		return false;
	}

	public static void loadOres(){
		 OreDictionary.registerOre("gemQuartz", ModItems.itemAsgardite);
	}
	
	public static void checkOreDictionary(){
		hasCopperIngot =  isRegistered(copperIngotName);
	}
}
