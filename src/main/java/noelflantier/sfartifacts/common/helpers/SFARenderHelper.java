package noelflantier.sfartifacts.common.helpers;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SFARenderHelper {

	public static void setLigtforTESR(World world,BlockPos pos, int lightvalue, int defaultnolight){
        int li = world.getCombinedLight(pos, lightvalue);
        li = li > 0 ? li : defaultnolight ;
        int i1 = li % 65536;
        int j1 = li / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i1, (float) j1);
	}
}
