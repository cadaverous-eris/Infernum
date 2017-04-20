package infernum.common.blocks;

import infernum.Infernum;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class InfernumBlocks {
	
	public static BlockPedestal PEDESTAL;
	
	public static void init() {
		PEDESTAL = new BlockPedestal();
		
		GameRegistry.registerTileEntity(TilePedestal.class, Infernum.MODID + "_pedestal");
	}
	
	public static void initModels() {
		PEDESTAL.initModel();
	}
	
}
