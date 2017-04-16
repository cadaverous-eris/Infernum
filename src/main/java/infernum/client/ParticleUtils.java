package infernum.client;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleUtils {

	public static void spawnParticleBeam(World world, EnumParticleTypes type, double x1, double y1, double z1, double x2, double y2, double z2, double step) {
		
		double distance = MathHelper.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
		int numParticles = (int) Math.ceil(distance / step);
		
		double dx = (x2 - x1) / numParticles;
		double dy = (y2 - y1) / numParticles;
		double dz = (z2 - z1) / numParticles;
		
		for (int i = 0; i < numParticles; i++) {
			world.spawnParticle(type, true, x1 + (dx * i), y1 + (dy * i), z1 + (dz * i), 0, 0, 0);
		}
		
	}
	
}
