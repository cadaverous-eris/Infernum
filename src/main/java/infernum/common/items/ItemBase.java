package infernum.common.items;

import infernum.Infernum;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBase extends Item {
	
	public ItemBase(String name) {
		super();
		setRegistryName(name);
		setUnlocalizedName(Infernum.MODID + "." + name);
		GameRegistry.register(this);
		setCreativeTab(Infernum.tab);
	}
	
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}

}
