package noelflantier.sfartifacts.common.handlers.capabilities;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerData<T> {

	Map<String,Integer> getIntegerMap();
	Map<String,Float> getFloatMap();
	Map<String,Boolean> getBooleanMap();
	
	default int getInt(String key){
		return getIntegerMap().containsKey(key) ? getIntegerMap().get(key) : -666;
	}
	default float getFloat(String key){
		return getFloatMap().containsKey(key) ? getFloatMap().get(key) : -666F;
	}
	default boolean getBoolean(String key){
		return getBooleanMap().containsKey(key) ? Boolean.valueOf(getBooleanMap().get(key)) : false;
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
		for(Entry<String,Integer> e : getIntegerMap().entrySet())
			getIntegerMap().replace(e.getKey(), tag.getInteger(e.getKey()));
		for(Entry<String,Float> e : getFloatMap().entrySet())
			getFloatMap().replace(e.getKey(), tag.getFloat(e.getKey()));
		for(Entry<String,Boolean> e : getBooleanMap().entrySet())
			getBooleanMap().replace(e.getKey(), tag.getBoolean(e.getKey()));
	}
}
