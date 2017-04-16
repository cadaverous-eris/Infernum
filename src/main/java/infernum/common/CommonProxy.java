package infernum.common;

import infernum.common.items.InfernumItems;
import infernum.common.spells.InfernumSpells;
import network.PacketHandler;

public class CommonProxy {
	
	public void preInit() {
		PacketHandler.registerMessages();
		InfernumSpells.init();
		InfernumItems.init();
	}
	
	public void init() {
		
	}
	
	public void postInit() {
		
	}

}
