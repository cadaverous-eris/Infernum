package infernum;

import infernum.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Infernum.MODID, name = Infernum.NAME, version = Infernum.VERSION, useMetadata = true)
public class Infernum {

	public static final String MODID = "infernum";
	public static final String NAME = "Infernum";
	public static final String VERSION = "0.1";
	
	@SidedProxy(clientSide = "infernum.client.ClientProxy", serverSide = "infernum.common.CommonProxy")
	public static CommonProxy proxy;
	
	@Mod.Instance
	public static Infernum instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
}
