package infernum.client;

import infernum.common.CommonProxy;
import infernum.common.blocks.InfernumBlocks;
import infernum.common.entities.InfernumEntities;
import infernum.common.items.InfernumItems;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	
	public void preInit() {
		super.preInit();
		InfernumItems.initItemModels();
		InfernumBlocks.initModels();
		InfernumEntities.initRenderers();
	}
	
	public void init() {
		super.init();
	}
	
	public void postInit() {
		super.postInit();
	}

}
