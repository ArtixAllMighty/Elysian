package net.darkhax.elysian;

import java.util.Arrays;

import net.darkhax.elysian.blocks.ElysianBlocks;
import net.darkhax.elysian.entity.ElysianEntity;
import net.darkhax.elysian.handlers.ElysianEventHandler;
import net.darkhax.elysian.handlers.EntityConstructionHandler;
import net.darkhax.elysian.handlers.GuiHandler;
import net.darkhax.elysian.items.ElysianItems;
import net.darkhax.elysian.proxy.CommonProxy;
import net.darkhax.elysian.util.Config;
import net.darkhax.elysian.util.Reference;
import net.darkhax.elysian.world.WorldProviderElysian;
import net.darkhax.elysian.world.biome.ElysianBiomes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;

//@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER)
public class Elysian {

	public static CreativeTabs tabElysian = new CreativeTabElysian(CreativeTabs.getNextID(), "elysian");

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	@Mod.Instance(Reference.MOD_ID)
	public static Elysian instance;

	public static FMLEventChannel Channel;
	public static final String packetChannel = "ElysianPacketChannel";

	public static IIcon effect;
	public static IIcon entity;

	private static final long time = System.currentTimeMillis();

	public static final int renderCarvedBlocksID = RenderingRegistry.getNextAvailableRenderId();

	@EventHandler
	public void init(FMLInitializationEvent e) {

		Channel =  NetworkRegistry.INSTANCE.newEventDrivenChannel(packetChannel);
		proxy.registerPacketHandlers(Channel);
		proxy.registerRenders();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent pre) {

		setModInfo(pre.getModMetadata());
		Config.createConfig(pre.getSuggestedConfigurationFile());
		new ElysianBlocks();
		//BLOCKS BEFORE ITEMS !
		new ElysianItems();
		new ElysianBiomes();
		new EntityConstructionHandler();
		new ElysianEventHandler();
		new ElysianEntity();
		new ElysianRecipes();

		//TODO Marked to be moved to the dimension class later on.
		DimensionManager.registerProviderType(2, WorldProviderElysian.class, true);
		DimensionManager.registerDimension(2, 2);
	}

	void setModInfo(ModMetadata meta) {

		meta.authorList = Arrays.asList("Subaraki", "Darkhax", "VydaX");
		meta.logoFile = "null";
		meta.description = "Pass through the gate!";
		meta.url = "http://darkhax.net";
		meta.autogenerated = false;
	}

	/**Used in rendering animations for dragons, golems and dragonflies */
	public static float getSysTimeF() {

		return (System.currentTimeMillis() - time) / 50F;
	}
}