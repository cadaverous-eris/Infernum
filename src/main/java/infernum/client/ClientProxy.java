package infernum.client;

import infernum.common.CommonProxy;
import infernum.common.entities.InfernumEntities;
import infernum.common.items.InfernumItems;

public class ClientProxy extends CommonProxy {
	
	public void preInit() {
		super.preInit();
		InfernumItems.initItemModels();
		InfernumEntities.initRenderers();
	}
	
	public void init() {
		super.init();
	}
	
	public void postInit() {
		super.postInit();
	}

}
