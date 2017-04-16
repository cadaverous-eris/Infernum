package infernum;

import infernum.common.CommonProxy;
import infernum.common.items.InfernumItems;
import infernum.common.spells.SpellRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Infernum.MODID, name = Infernum.NAME, version = Infernum.VERSION, useMetadata = true)
public class Infernum {

	public static final String MODID = "infernum";
	public static final String NAME = "Infernum";
	public static final String VERSION = "0.1";

	@SidedProxy(clientSide = "infernum.client.ClientProxy", serverSide = "infernum.common.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance
	public static Infernum instance;

	public static CreativeTabs tab = new CreativeTabs("infernum") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(InfernumItems.ZOMBIE_PIGMAN_HEART);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public String getTabLabel() {
			return "infernum.name";
		}
	};
	
	public static SpellRegistry SPELL_REGISTRY = new SpellRegistry();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}

}
