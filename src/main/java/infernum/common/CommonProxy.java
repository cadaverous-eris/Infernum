package infernum.common;

import infernum.Infernum;
import infernum.common.blocks.InfernumBlocks;
import infernum.common.entities.InfernumEntities;
import infernum.common.items.InfernumItems;
import infernum.common.network.PacketHandler;
import infernum.common.spells.InfernumSpells;
import infernum.common.world.WorldGeneratorInfernum;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public void preInit() {
		PacketHandler.registerMessages();
		InfernumSpells.init();
		InfernumItems.init();
		InfernumBlocks.init();
		InfernumEntities.init();
		
		GameRegistry.registerWorldGenerator(new WorldGeneratorInfernum(), 0);
	}
	
	public void init() {
		
	}
	
	public void postInit() {
		
	}

}
