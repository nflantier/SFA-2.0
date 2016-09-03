package noelflantier.sfartifacts.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.CommonProxy;
import noelflantier.sfartifacts.common.handlers.ModBlocks;
import noelflantier.sfartifacts.common.handlers.ModEntities;
import noelflantier.sfartifacts.common.handlers.ModEvents;
import noelflantier.sfartifacts.common.handlers.ModEventsClient;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.handlers.ModKeyBindings;
import noelflantier.sfartifacts.common.handlers.ModKeyInput;
import noelflantier.sfartifacts.common.handlers.ModTileEntities;

public class ClientProxy extends CommonProxy{

	@Override
	public void preInit(FMLPreInitializationEvent event){
		super.preInit(event);
		OBJLoader.INSTANCE.addDomain(Ressources.MODID.toLowerCase());
        ModelLoaderRegistry.registerLoader(new SFABakedLoader());
		ModBlocks.preInitRenderBlocks();
		ModItems.preInitRenderItems();
    	ModTileEntities.preInitRenderTileEntities();
		ModEntities.preInitRenderEntities();
	}
	
	@Override
	public void init(FMLInitializationEvent event){
		super.init(event);
    	ModBlocks.initRenderBlocks();
    	ModItems.initRenderItems();
    	ModTileEntities.initRenderTileEntities();
		ModEntities.initRenderEntities();
		
    	ModKeyBindings.loadBindings();
        MinecraftForge.EVENT_BUS.register(new ModKeyInput());
        MinecraftForge.EVENT_BUS.register(new ModEventsClient());
	}

	@Override
	public void postinit(FMLPostInitializationEvent event) {
		super.postinit(event);
    	ModItems.postInitRenderItems();
	}
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}
}
