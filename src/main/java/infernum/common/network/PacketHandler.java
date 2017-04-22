package infernum.common.network;

import infernum.Infernum;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Infernum.MODID);

    private static int id = 0;
    
    public static void registerMessages(){
    	INSTANCE.registerMessage(MessageSoulDrainFX.MessageHolder.class, MessageSoulDrainFX.class, id++, Side.CLIENT);
    }

}
