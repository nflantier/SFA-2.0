package noelflantier.sfartifacts.common.recipes;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.recipes.handler.SoundEmitterConfig;

public class MobsPropertiesForSpawning {
	public Class entityClass;
	public String entityName;
	
	public int frequency = SoundEmitterConfig.highestFrequency+1;
	public int customX = 0;
	public int customY = 0;
	public int customZ = 0;
	public boolean isAttractedToSpawner = true;
	public boolean isSpawningOnce = false;
	public int nbMaxSpawning = -1;
	
	public int energyneeded = 0;
	public FluidStack fluidneeded = new FluidStack(ModFluids.fluidLiquefiedAsgardite,0);
	public List<ItemStack> itemsneeded = null;
	
	public MobsPropertiesForSpawning(){
		
	}
}
