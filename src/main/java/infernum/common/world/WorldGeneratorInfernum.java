package infernum.common.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGeneratorInfernum implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		
		if (world.provider.getDimension() == -1) {
			
			BlockPos chunkCenter = new BlockPos((16 * chunkX) + 8, 64, (16 * chunkZ) + 8);
			
			new PigmanMageTowerGenerator().generate(world, world.rand, chunkCenter);
			
		}

	}

}
