package noelflantier.sfartifacts.common.handlers.capabilities;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import noelflantier.sfartifacts.common.recipes.ISFARecipe;

public interface IPlayerData<T> {

	Map<String,Integer> getIntegerMap();
	Map<String,Float> getFloatMap();
	Map<String,Boolean> getBooleanMap();
	
	default int getInt(String key){
		return getIntegerMap().containsKey(key) ? getIntegerMap().get(key) : null;
	}
	default float getFloat(String key){
		return getFloatMap().containsKey(key) ? getFloatMap().get(key) : null;
	}
	default boolean getBoolean(String key){
		return getBooleanMap().containsKey(key) ? Boolean.valueOf(getBooleanMap().get(key)) : null;
	}
	
	default IPlayerData setInt(String key, int value){
		if( getIntegerMap().containsKey(key) )
			getIntegerMap().put(key, value);
		return this;
	}
	default IPlayerData setFloat(String key, float value){
		if( getFloatMap().containsKey(key) )
			getFloatMap().put(key, value);
		return this;
	}
	default IPlayerData setBoolean(String key, boolean value){
		if( getBooleanMap().containsKey(key) )
			getBooleanMap().put(key, Boolean.valueOf(value));
		return this;
	}
	
	default NBTBase writeNBT() {
        NBTTagCompound itemTag = new NBTTagCompound();
        getIntegerMap().forEach((s,i)->itemTag.setInteger(s, i));
        getFloatMap().forEach((s,f)->itemTag.setFloat(s, f));
        getBooleanMap().forEach((s,b)->itemTag.setBoolean(s, b));
		return itemTag;
	}
	default void readNBT(NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
        getIntegerMap().forEach((s,i)->i = tag.getInteger(s));
        getFloatMap().forEach((s,f)->f = tag.getFloat(s));
        getBooleanMap().forEach((s,b)->b = tag.getBoolean(s));
	}
}
