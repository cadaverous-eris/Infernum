package infernum.common.network;

import java.util.Random;

import infernum.client.ParticleUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSoulDrainFX implements IMessage {

	public static Random rand = new Random();
	double posX = 0, posY = 0, posZ = 0;
	double castX = 0, castY = 0, castZ = 0;

	public MessageSoulDrainFX() {
		super();
	}

	public MessageSoulDrainFX(double x1, double y1, double z1, double x2, double y2, double z2) {
		super();
		this.posX = x1;
		this.posY = y1;
		this.posZ = z1;
		this.castX = x2;
		this.castY = y2;
		this.castZ = z2;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		castX = buf.readDouble();
		castY = buf.readDouble();
		castZ = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeDouble(castX);
		buf.writeDouble(castY);
		buf.writeDouble(castZ);
	}

	public static class MessageHolder implements IMessageHandler<MessageSoulDrainFX, IMessage> {
		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(final MessageSoulDrainFX message, final MessageContext ctx) {
			World world = Minecraft.getMinecraft().world;
			if (world.isRemote) {
				for (double i = 0; i < 8; i++) {
					double xCoord = message.posX + (rand.nextDouble() - 0.5);
					double yCoord = message.posY + (rand.nextDouble() - 0.5);
					double zCoord = message.posZ + (rand.nextDouble() - 0.5);
					world.spawnParticle(EnumParticleTypes.CRIT, true, xCoord, yCoord, zCoord, (rand.nextDouble() - 0.5),
							(rand.nextDouble() - 0.5), (rand.nextDouble() - 0.5));
				}
				ParticleUtils.spawnParticleBeam(world, EnumParticleTypes.CRIT, message.posX, message.posY, message.posZ,
						message.castX, message.castY, message.castZ, 0.2);
			}
			return null;
		}
	}
}
