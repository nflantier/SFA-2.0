package noelflantier.sfartifacts.common.handlers.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class CapabilityPlayerProvider  implements INBTSerializable<NBTTagCompound>,ICapabilityProvider {
	
    @CapabilityInject(IPlayerData.class)
    public static Capability<IPlayerData> PLAYER_DATA = null;

    public static class PlayerStorage implements Capability.IStorage<IPlayerData> {
    	@Override
		public NBTBase writeNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side) {
    		return instance.writeNBT();
		}
    	@Override
		public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side, NBTBase nbt) {
    		instance.readNBT(nbt);
		}
	}
    
	private IPlayerData data = new PlayerData();
    public static void register(){
        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerStorage(), PlayerData.class);
    }
    
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return PLAYER_DATA != null && capability == PLAYER_DATA;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (PLAYER_DATA != null && capability == PLAYER_DATA) 
			return (T)data != null ? (T)data : (T) new PlayerData();
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return (NBTTagCompound) PLAYER_DATA.getStorage().writeNBT(PLAYER_DATA, data, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		PLAYER_DATA.getStorage().readNBT(PLAYER_DATA, data, null, nbt);
	}

}
