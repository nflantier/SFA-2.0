package noelflantier.sfartifacts.common.tileentities;

import java.util.Random;

import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;

public interface ITileUsingMaterials {
	EnumPillarMaterial getMaterial();
	default float getEnergyRatio(){
		return getMaterial().energyRatio;
	};
	default void setNaturalEnergy(float ne, int id){
		ne = getEnergyRatio();
	};
	default boolean getRandom(Random r){
		return r.nextInt(100) < getEnergyRatio();
	}
}
