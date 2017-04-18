package infernum.common;

import infernum.Infernum;
import infernum.common.entities.InfernumEntities;
import infernum.common.items.InfernumItems;
import infernum.common.spells.InfernumSpells;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import network.PacketHandler;

public class CommonProxy {
	
	public void preInit() {
		PacketHandler.registerMessages();
		InfernumSpells.init();
		InfernumItems.init();
		InfernumEntities.init();
	}
	
	public void init() {

	}
	
	public void postInit() {
		
	}

}
