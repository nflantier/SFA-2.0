package noelflantier.sfartifacts.common.world;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModConfig;

public class ModWorldGenOre implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case 0:
			generateSurface(random, chunkX * 16, chunkZ * 16, world);
			break;
		case 1:
			generateEnd(random, chunkX * 16, chunkZ * 16, world);
			break;
		case -1:
			generateNether(random, chunkX * 16, chunkZ * 16, world);
			break;
		default:
			generateSurface(random, chunkX * 16, chunkZ * 16, world);
			break;
		}
	}

	private void generateNether(Random random, int x, int z, World world) {
		
	}

	private void generateEnd(Random random, int x, int z, World world) {
		
	}

	private void generateSurface(Random random, int x, int z, World world) {
		if(ModConfig.isAsgarditeOreSpawnEnable)
			addOreSpawn(ModBlocks.blockOreAsgardite, Blocks.STONE, world, random, x, z, ModConfig.minVainSizeAsgardite, ModConfig.maxVainSizeAsgardite, ModConfig.chanceAsgardite, ModConfig.minYAsgardite, ModConfig.maxYAsgardite);

		if(ModConfig.isVibraniumOreSpawnEnable){
			Biome b = world.getBiomeGenForCoords(new BlockPos(x, 70, z));
			int bid = Biome.getIdForBiome(b);
			if(bid==35 || bid==36 || bid==163 || bid==164){
				addOreSpawn(ModBlocks.blockOreVibranium, Blocks.STONE, world, random, x, z, ModConfig.minVainSizeVibranium, ModConfig.maxVainSizeVibranium, ModConfig.chanceVibranium, ModConfig.minYVibranium, ModConfig.maxYVibranium);
			}
		}
	}
	public static Predicate<IBlockState> isBlockCanBeReplaced(Class<? extends Block> cl) {
        return p -> p.getBlock().getClass().equals(cl);
    }
	
	public void addOreSpawn(final Block block, final Block blockToReplace, final World world, final Random random, final int chunkXPos, final int chunkZPos, final int minVainSize, final int maxVainSize, final int chancesToSpawn, final int minY, final int maxY) {
		for (int i = 0; i < chancesToSpawn; i++) {
			final int posX = chunkXPos + random.nextInt(16);
			final int posY = minY + random.nextInt(maxY - minY);
			final int posZ = chunkZPos + random.nextInt(16);
			new WorldGenMinable(block.getDefaultState(), minVainSize+random.nextInt(maxVainSize - minVainSize), isBlockCanBeReplaced(blockToReplace.getClass())).generate(world, random, new BlockPos(posX, posY, posZ));
		}
	}
}
