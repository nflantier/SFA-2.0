package noelflantier.sfartifacts.common.tileentities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

public interface ISFAEnergyHandler extends IEnergyProvider,IEnergyReceiver{

	public EnergyStorage getEnergyStorage();
	public void setLastEnergyStored(int lastEnergyStored);
}
