package noelflantier.sfartifacts.common.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.BlockLiquefiedAsgardite;
import noelflantier.sfartifacts.common.helpers.RegisterHelper;

public class ModFluids {

	public static Fluid fluidLiquefiedAsgardite;
	public static Block blockLiquefiedAsgardite;
	
	public static void preInitFluids() {
		fluidLiquefiedAsgardite = new Fluid(Ressources.UL_NAME_FLUID_LIQUEFIED_ASGARDITE,new ResourceLocation(Ressources.MODID+":blocks/liquefied_asgardite"),new ResourceLocation(Ressources.MODID+":blocks/liquefied_asgardite_flow")).setDensity(3000).setViscosity(6000).setTemperature(2000);
		FluidRegistry.registerFluid(fluidLiquefiedAsgardite);

		FluidRegistry.addBucketForFluid(fluidLiquefiedAsgardite);

        blockLiquefiedAsgardite = new BlockLiquefiedAsgardite(fluidLiquefiedAsgardite,Material.LAVA );//new MaterialLiquid(MapColor.LIGHT_BLUE)
    	RegisterHelper.registerBlock(blockLiquefiedAsgardite);

	}

    @SideOnly(Side.CLIENT)
	public static void preInitRenderFluids() {
    	RegisterHelper.registerModelForVariantAndStateMapper(blockLiquefiedAsgardite, new ModelResourceLocation(Ressources.MODID+ ":" + Ressources.UL_NAME_LIQUEFIED_ASGARDITE, "ignore"));	
	}
}
