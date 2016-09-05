package noelflantier.sfartifacts.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;

public class RegisterHelper {
		
	public static void registerBlock(Block block){
		GameRegistry.register(block);
	}
	
	public static void registerItem(Item item){
		GameRegistry.register(item);
	}
	
	public static void registerTileEntity(Class<? extends TileEntity> tile, String name){
		GameRegistry.registerTileEntity(tile, Ressources.MODID + "_" + name);
	}

	/*
	 * RENDER
	 */

    @SideOnly(Side.CLIENT)
    public static void registerModelForVariantAndStateMapper(Block block, ModelResourceLocation model){
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
    	
        StateMapperBase ignoreState = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return model;
            }
        };
        ModelLoader.setCustomStateMapper(block, ignoreState);
    }

    @SideOnly(Side.CLIENT)
	public static void registerVariantBlockRender(Block block, int meta, String file, String variant){
		registerVariantItemRender(Item.getItemFromBlock(block), meta, file, variant);
	}

    @SideOnly(Side.CLIENT)
	public static void registerInventoryVariantBlockRender(Block block, int meta, String file){
		registerVariantBlockRender(block,meta,file,"inventory");
	}

    @SideOnly(Side.CLIENT)
	public static void registerVariantBlockRender(Block block, int meta, ModelResourceLocation file){
		registerVariantItemRender(Item.getItemFromBlock(block),meta, file);
	}

    @SideOnly(Side.CLIENT)
	public static void registerVariantItemRender(Item item, int meta, ModelResourceLocation file){
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, file);
	}

    @SideOnly(Side.CLIENT)
	public static void registerVariantItemRender(Item item, int meta, String file, String variant){
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(Ressources.MODID+":"+file, variant));
	}

    @SideOnly(Side.CLIENT)
	public static void registerInventoryVariantItemRender(Item item, int meta, String file){
		registerVariantItemRender(item,meta,file,"inventory");
	}
}


